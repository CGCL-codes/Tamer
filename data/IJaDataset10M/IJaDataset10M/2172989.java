package org.arch4j.clientcontroller;

import java.util.ArrayList;
import java.util.List;

/**
 * This <code>abstract</code> class defines basic behavior for a state.
 * <p>
 * All deriving state implementations should have a section of javadoc in
 * the class heading based on the following:
 * <p>
 * <H3>State overview</H3>
 * <UL>
 *   <LI>State name</LI>
 *     <UL>
 *       <LI>MyState</LI>
 *     </UL>
 *   <LI>Context values read by entry</LI>
 *     <UL>
 *       <LI>MyReadOnlyValues</LI>
 *     </UL>
 *   <LI>Context values added on entry</LI>
 *     <UL>
 *       <LI>MyAddedValues</LI>
 *     </UL>
 *   <LI>Context values updated on entry</LI>
 *     <UL>
 *       <LI>MyAddedValues</LI>
 *     </UL>
 *   <LI>Context values removed on entry</LI>
 *     <UL>
 *       <LI>MyRemovedValues</LI>
 *     </UL>
 *   <LI>Context values expected on exit</LI>
 *     <UL>
 *       <LI>MyExpectedValues</LI>
 *     </UL>
 *   <LI>Context values added on exit</LI>
 *     <UL>
 *       <LI>MyAddedValues</LI>
 *     </UL>
 *   <LI>Context values updated on exit</LI>
 *     <UL>
 *       <LI>MyAddedValues</LI>
 *     </UL>
 *   <LI>Context values removed on exit</LI>
 *     <UL>
 *       <LI>MyRemovedValues</LI>
 *     </UL>
 *   <LI>Transitions by priority</LI>
 *     <OL>
 *       <LI>MyOrderedTransitions</LI>
 *     </OL>
 *   <LI>EmbeddedStateMachine entry transition</LI>
 *     <UL>
 *       <LI>MyEmbeddedStateMachineEntryTransition</LI>
 *     </UL>
 * </UL>
 * <p>
 *
 * @author David Colwell
 * @version 1.0
 */
public abstract class State {

    /**
  * Reference to the embedded state machine.
  */
    private StateMachine embeddedStateMachine;

    /**
  * The list of <code>Transition</code>s from this state.
  */
    private ArrayList transitionList;

    /**
  * Constructs a <code>State</code> object.  Subclass constructors will call
  * this default constructor implicitly.
  */
    public State() {
        embeddedStateMachine = null;
        transitionList = new ArrayList();
        buildTransitionList();
    }

    /**
  * Allows subclasses to specify <code>Transition</code>s.  Note that the order
  * the <code>Transition</code>s are added implies priority.  If two
  * <code>Transition</code>s are not mutually exclusive in their events and
  * guard conditions, then the first in the list will execute and the second
  * will not.
  *
  * @see #buildTransitionList()
  */
    protected void appendTransition(Transition aTransition) {
        transitionList.add(aTransition);
    }

    /**
  * Helper method to create an embedded <code>StateMachine</code>.
  *
  * @throws StateMachineException
  *  if there is any problem processing the request.  This might include a missing
  *  key-value, application server problems, failed domain validation, or
  *  data access errors.
  */
    protected void createEmbeddedStateMachine(Transition entryTransition, StateContext context) throws StateMachineException {
        embeddedStateMachine = new StateMachine(entryTransition, context);
    }

    /**
  * Helper method to create an embedded <code>StateMachine</code>.
  *
  * @throws StateMachineException
  *  if there is any problem processing the request.  This might include a missing
  *  key-value, application server problems, failed domain validation, or
  *  data access errors.
  */
    protected void createEmbeddedStateMachine(Transition entryTransition, Transition exitTransition, StateContext context) throws StateMachineException {
        embeddedStateMachine = new StateMachine(entryTransition, exitTransition, context);
    }

    /**
  * Retrieves the embedded state machine.
  *
  * @return
  *  The embedded state machine, if it exists, otherwise <code>null</code>.
  */
    protected StateMachine getEmbeddedStateMachine() {
        return embeddedStateMachine;
    }

    /**
  * Retrieve the list of <code>Transition</code>s available from this
  * <code>State</code>.
  */
    protected final List getTransitionList() {
        return transitionList;
    }

    /**
  * Retrieves the unique state name.  Note that this should not be defined to
  * contain the '~' character as this is used as the full state name seperator.
  */
    public abstract String getStateName();

    /**
  * Perform the entry action for this state.  This will typically be
  * data retrieval and setup.  If this state has an embedded state machine,
  * a call to <code>createEmbeddedStateMachine</code> should go here as well.
  *
  * @throws StateMachineException
  *  if there is a system related problem processing the request.  This might
  *  be a missing key-value, application server problems, failed domain
  *  validation, or data access errors.
  */
    public void entryAction(StateContext context) throws StateMachineException {
    }

    /**
  * Perform the exit action for this state.  This will typically be
  * for clean-up, if necessary.
  *
  * @throws StateMachineException
  *  if there is a system related problem processing the request.  This might
  *  be a missing key-value, application server problems, failed domain
  *  validation, or data access errors.
  */
    public void exitAction(StateContext context) throws StateMachineException {
    }

    /**
  * Build the <code>transitionList</code> of <code>Transition</code>s available
  * from this <code>State</code>.  The implementation method will use
  * <code>addTransition</code> to add new <code>Transition</code>s.
  */
    public abstract void buildTransitionList();
}
