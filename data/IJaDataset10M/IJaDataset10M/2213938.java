/*
 * This file is part of the Ptolemy project at Iowa State University.
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/.
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 * 
 * For more details and the latest version of this code please see
 * http://ptolemy.cs.iastate.edu/
 *
 * Contributor(s):
 */

package handlers;

import events.FEChanged;
import util.Display;

/***
 * This class modularizes the display update concern. 
 */
public class DisplayUpdate {

 public DisplayUpdate() {
  register(this); // Classes must register themselves in order to receive event announcements
 }

 // Event handler methods must take one argument of the received event type and throw Throwable
 public void update(FEChanged next) throws Throwable {
  // The invoke method below invokes the next handler or, 
  // if there are no more handlers, the announce block.
  next.invoke(); 
  Display.update();
  System.out.println("  Inside display update: After Invoke");
 }

 when FEChanged do update; // This class handles Changed events with the "update" method
}
