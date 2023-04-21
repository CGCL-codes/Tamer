package org.cycads.entities.change;

import java.util.EventObject;

/**
 * Event which encapsulates a change in any mutable BioJava object.
 * 
 * @author Thomas Down
 * @author Matthew Pocock
 * @author Greg Cox
 * @since 1.1
 */
public class ChangeEvent<O> extends EventObject {

    private final ChangeType type;

    private final O change;

    private final O previous;

    private final ChangeEvent chain;

    /**
	 * Construct a ChangeEvent with no change details.
	 * 
	 * @param source The object being changed.
	 * @param type The type of change being made.
	 */
    public ChangeEvent(Object source, ChangeType type) {
        this(source, type, null, null, null);
    }

    /**
	 * 
	 * Construct a ChangeEvent specifying a new value for a property, or an object to be added to a collection.
	 * 
	 * @param source The object being changed.
	 * @param type The type of change being made.
	 * @param change The new value of the property being changed.
	 */
    public ChangeEvent(Object source, ChangeType type, O change) {
        this(source, type, change, null, null);
    }

    /**
	 * 
	 * Construct a ChangeEvent specifying a new value for a property, and giving the previous value.
	 * 
	 * @param source The object being changed.
	 * @param type The type of change being made.
	 * @param change The new value of the property being changed.
	 * @param previous The old value of the property being changed.
	 */
    public ChangeEvent(Object source, ChangeType type, O change, O previous) {
        this(source, type, change, previous, null);
    }

    /**
	 * 
	 * Construct a ChangeEvent to be fired because another ChangeEvent has been received from a property object.
	 * 
	 * @param source The object being changed.
	 * @param type The type of change being made.
	 * @param change The new value of the property being changed.
	 * @param previous The old value of the property being changed.
	 * @param chain The event which caused this event to be fired.
	 */
    public ChangeEvent(Object source, ChangeType type, O change, O previous, ChangeEvent chain) {
        super(source);
        this.type = type;
        this.change = change;
        this.previous = previous;
        this.chain = chain;
    }

    /**
	 * Find the type of this event.
	 * 
	 * @return The Type value
	 */
    public ChangeType getType() {
        return type;
    }

    /**
	 * 
	 * Return an object which is to be the new value of some property, or is to be added to a collection. May return
	 * <code>null</code> is this is not meaningful.
	 * 
	 * @return The Change value
	 */
    public O getChange() {
        return change;
    }

    /**
	 * 
	 * Return the old value of a property being changed. May return <code>null</code> is this is not meaningful.
	 * 
	 * @return The Previous value
	 */
    public O getPrevious() {
        return previous;
    }

    /**
	 * 
	 * Return the event which caused this to be fired, or <code>null</code> if this change was not caused by another
	 * event.
	 * 
	 * @return The ChainedEvent value
	 */
    public ChangeEvent getChainedEvent() {
        return chain;
    }

    public String toString() {
        return super.toString() + "[" + "type:" + getType() + ", change: " + getChange() + ", previous: " + getPrevious() + ", chainedEvent: " + getChainedEvent() + "]";
    }
}
