package net.sourceforge.romulan.events;

/**
 * Commodity class to hold the source object that generated the event
 * @author etux
 *
 */
public abstract class AbstractEvent implements Event {

    private static final long serialVersionUID = -1695677684170597109L;

    private Object source;

    public AbstractEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }
}
