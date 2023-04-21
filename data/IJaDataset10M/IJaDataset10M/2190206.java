package de.fhg.igd.jhsm;

/**
 * "A transition is a relationship between two states indicating that an object
 * in the first state will perform certain actions and enter the second state
 * when a specified event occurs and specified conditions are satisfied. 
 * On such a change of state a transition is said to fire.
 * Until a transition fires it is said to be in source state; after it fires it
 * is said to be in target state." 
 * - Booch et al, The UML User Guide.
 *
 * @see Condition
 * @see Event
 *
 * @author Jan Haevecker
 * @version "$Id: Transition.java 1913 2007-08-08 02:41:53Z jpeters $"
 */
public interface Transition extends Component {

    /**
     * Checks if the guard condition is met.
     *
     * @param context Used to publish and retrieve global data.
     */
    boolean guard(Context context);

    /**
     * Checks if a given event is a trigger for the transition.
     */
    boolean hasInterest(Event event);

    /**
     * Checks if the transition is triggerless. 
     *
     * @return <code>true</code> if the transition has no trigger event, 
     *      <code>false</code> if it has one.
     */
    boolean isTriggerless();

    /**
     * Returns the target state of the transition.
     */
    State getTarget();

    /**
     * Set's the target state of the transition.
     */
    public void setTarget(State target);

    /**
     * Getter for property guardCondition.
     *
     * @return Value of property guardCondition.
     */
    public Condition getGuardCondition();

    /**
     * Setter for property guardCondition.
     *
     * @param guardCondition New value of property guardCondition.
     */
    public void setGuardCondition(Condition guardCondition);

    /**
     * Getter for property triggerEvent.
     *
     * @return Value of property triggerEvent.
     */
    public Event getTriggerEvent();

    /**
     * Setter for property triggerEvent.
     *
     * @param triggerEvent New value of property triggerEvent.
     */
    public void setTriggerEvent(Event triggerEvent);
}
