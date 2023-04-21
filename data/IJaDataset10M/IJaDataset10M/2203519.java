package org.mars_sim.msp.simulation;

import java.util.*;

/**
 * The MspCollectionEvent is an event that is thrown by an
 * MspCollection when a unit is added or removed.
 */
public class MspCollectionEvent extends EventObject {

    /** A Unit has been added **/
    public static final String ADD = "Add";

    /** The collection has been cleared **/
    public static final String CLEAR = "Clear";

    /** A unit has been removed **/
    public static final String REMOVE = "Remove";

    private String type;

    private Unit trigger;

    /**
     * Constructs a MspCollectionEvent object.
     * @param source the MspCollection source of the event.
     * @param type the type of the event.
     * @param trigger Unit triggering this event.
     */
    public MspCollectionEvent(MspCollection source, String type, Unit trigger) {
        super(source);
        this.type = type;
        this.trigger = trigger;
    }

    /**
     * Gets the type of the event.
     * @return event type
     */
    public String getType() {
        return type;
    }

    /**
     * Get the Unit that has triggered the event.
     * @return Unit trigger, this value maybe null for certian event types.
     */
    public Unit getTrigger() {
        return trigger;
    }
}
