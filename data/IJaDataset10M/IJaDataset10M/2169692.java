package org.escapek.client.core.services;

/**
 * Event sent to object changes listeners
 * @author nicolasjouanin
 *
 */
public class ObjectChangeEvent {

    public enum EventType {

        OBJECT_CREATED, OBJECT_DELETED, OBJECT_CHANGED
    }

    protected Object source;

    private EventType type;

    public ObjectChangeEvent(Object source, EventType type) {
        setSource(source);
        setType(type);
    }

    /**
	 * Get the object which has been changed
	 * @return the changed object
	 */
    public Object getSource() {
        return source;
    }

    /**
	 * Set the object which has been changed
	 * @param source
	 */
    public void setSource(Object source) {
        this.source = source;
    }

    /**
	 * The type of change event
	 * @return
	 */
    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }
}
