package com.oletraveler.snippet


import scala.xml._
import net.liftweb.http.js.jquery.JqWiringSupport
import net.liftweb.http.js.JsCmd
import net.liftweb.http.js.JsCmds
import net.liftweb.http.WiringUI
import net.liftweb.util.Cell
import net.liftweb.util.ValueCell
import net.liftweb.http.SHtml._

import com.oletraveler.model.Person

/**
 * Created by IntelliJ IDEA.
 * User: tstevens
 * Date: 4/7/11
 * Time: 10:50 AM
 * To change this template use File | Settings | File Templates.
 */

class WiredPerson {
  private object Person {
    val entity = ValueCell(new Person)
    val edit = ValueCell(false)
    val email = edit.lift(b => entity.lift(_.email).get)
  }

  def edit = {
    WiringUI.toNode(Person.edit, JqWiringSupport.fade)((mode: Boolean, ns: NodeSeq) => {
      editable(
        mode,
        mode,
        (b: Boolean) => ajaxButton(Text("Done"), () => {Person.edit.set(false); println("user edit to false"); JsCmds.Noop}),
        (b: Boolean) => ajaxButton(Text("Edit"),  () => {Person.edit.set(true); println("user edit to true"); JsCmds.Noop})
      )
    })
  }

  def email = WiringUI.toNode(Person.email, JqWiringSupport.fade)((email: Option[String], ns: NodeSeq) => {
    editable(
      email.getOrElse(""),
      Person.edit.get,
      (n: String) => ajaxText(n, s => {Person.entity.email = Some(s); Person.entity.set(Person.entity.get); JsCmds.Noop}),
      (n: String) => Text(n)
    )
  })



  /**
   * If true, execute the t function, if false, execute the f function.
   * */
  def editable[T](o: T, mode: Boolean, t: (T)=> NodeSeq, f: (T) => NodeSeq) : NodeSeq = {
    mode match {
      case true => {
          val ns = t(o)
          println("case true, returning ns: " + ns)
          ns
      }
      case false => {
          val ns = f(o)
          println("case false, returning ns: " + ns)
          ns
      }
    }
  }

}