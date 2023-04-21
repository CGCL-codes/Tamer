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
 * Contributor(s): Hridesh Rajan
 */

package handlers;

import events.GenAvailable;
import util.*;

public class Fittest {
 Generation last;

 public Fittest() {
  // register this instance as an event handler
  register(this);
 }

 // a handler that performs the fitness check on newly available generations
 public void check(GenAvailable next) throws Throwable {
  next.invoke();
  if (last ==null)
   last = next.g();
  else {
   Fitness f1 = next.g().getFitness();
   Fitness f2 = last.getFitness();
   if (f1.average() > f2.average())
    last = next.g();
  }
 }
 when GenAvailable do check
}
