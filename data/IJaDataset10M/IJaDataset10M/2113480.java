package com.teletalk.jserver.event;

import com.teletalk.jserver.rmi.remote.RemoteEvent;

/**
 * Baseclass for all events.
 * 
 * @author Tobias L�fstrand
 * 
 * @since Beta
 */
public class Event {

    /** The object from which the Event originated. */
    protected final Object source;

    /** Indicates to the {@link EventQueue} that this event should be handled sequentially. @since 2.0 */
    protected boolean sequential = false;

    /**
	 * Creates a new Event object.
	 * 
	 * @param source the source of the event.
	 */
    public Event(Object source) {
        this(source, false);
    }

    /**
    * Creates a new Event object.
    * 
    * @param source the source of the event.
    * @param sequential flag that indicates to the {@link EventQueue} if this event should be handled sequentially.
    * 
    * @since 2.0
    */
    public Event(Object source, boolean sequential) {
        this.source = source;
        this.sequential = sequential;
    }

    /**
    * Gets the flag that indicates to the {@link EventQueue} if this event should be handled sequentially.
    * 
    * @since 2.0
    */
    public boolean isSequential() {
        return sequential;
    }

    /**
    * Sets the flag that indicates to the {@link EventQueue} if this event should be handled sequentially.
    * 
    * @since 2.0
    */
    public void setSequential(boolean sequential) {
        this.sequential = sequential;
    }

    /**
	 * Gets the class object representing the event class / group this event object belong to. 
	 * This is used to determine which type of listeners that should be notified of the event.
	 * 
	 * @return Class object representing the event class / group of this event object.
	 */
    public Class getEventClass() {
        return this.getClass();
    }

    /**
	 * Returns the source of the event.
	 * 
	 * @return the source of the event.
	 */
    public final Object getSource() {
        return source;
    }

    /**
	 * Returns a String object representing of this Event.
	 * 
	 * @return a String representation of this Event.
	 */
    public String toString() {
        return getClass().getName() + " sent from " + source;
    }

    /**
	 * Method to notify a listener that this event has occurred.
	 * 
	 * @param listener a listener to be notified.
	 */
    public void notifyListener(final Object listener) {
        if (listener instanceof EventListener) ((EventListener) listener).handleEvent(this);
    }

    /**
	 * Creates a RemoteEvent object, suitable for sending to a remote rmi client (such as 
	 * the Administration tool) using this Event object as a template.
	 * 
	 * @return a RemoteEvent object.
	 */
    public RemoteEvent createRemoteEvent() {
        return new RemoteEvent((source != null) ? source.toString() : null);
    }
}
