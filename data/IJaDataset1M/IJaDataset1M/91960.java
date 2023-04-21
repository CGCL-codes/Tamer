package net.assimilator.examples.core.step10;

import net.assimilator.event.*;

/**
 * The HelloEvent is a RemoteServiceEvent
 * 
 * @see net.assimilator.event.RemoteServiceEvent
 */
public class HelloEvent extends RemoteServiceEvent {

    /** A unique id number for the hello event **/
    public static final long ID = 9999999999L;

    /** Holds the property for the time the event was created */
    private long when;

    /** 
     * Creates a HelloEvent 
     */
    public HelloEvent(Object source) {
        super(source);
        setWhen(System.currentTimeMillis());
    }

    /** 
     * Getter for property when.
     * 
     * @return Value of property when.
     */
    public long getWhen() {
        return when;
    }

    /** 
     * Setter for property when.
     * 
     * @param when New value of property when.
     */
    public void setWhen(long when) {
        this.when = when;
    }
}
