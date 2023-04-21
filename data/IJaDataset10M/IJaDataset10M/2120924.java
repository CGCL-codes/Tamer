package edu.luc.cs.trull;

import java.beans.PropertyChangeListener;

/**
 * A building block for a concurrent software system.  
 * A Trull component is a visual or nonvisual 
 * JavaBean that has event-driven reactive behavior in the form of 
 * zero or more of the following aspects:
 * <ul>
 * <li>Communication in the form of incoming and outgoing events 
 * (of type PropertyChangeEvent).</li>
 * <li>Lifecycle methods start, stop, suspend, and resume.</li>
 * <li>Termination in the form of outgoing events sent to  
 * another component, usually the parent in the component tree.</li>
 * </ul>
 * A class for a custom Trull component is usually declared
 * by subclassing from <code>Composite</code> and providing any
 * necessary instance variables for the object-oriented aspects
 * of the expression.  The event-driven aspects of the component
 * are then provided, usually in the constructor, by using the
 * method <code>addComponent</code> with Trull components that
 * provide the desired behavior.  Code that interacts with
 * the object-oriented aspects of a Trull component may be
 * embedded in the event-driven behavior in the form of actions,
 * event functions, predicates, and valuators.
 * @see java.beans.PropertyChangeEvent
 * @see edu.luc.cs.trull.Composite
 * @see edu.luc.cs.trull.EventFunction
 * @see edu.luc.cs.trull.EventPredicate
 * @see edu.luc.cs.trull.EventValuator
 */
public interface Component extends PropertyChangeListener, PropertyChangeSource, Startable, Suspendable, Terminating {
}
