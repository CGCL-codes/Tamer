package com.mindquarry.events;

/**
 * Add summary documentation here.
 * 
 * @author <a href="mailto:alexander(dot)saar(at)mindquarry(dot)com">Alexander
 *         Saar</a>
 */
public abstract class EventBase implements Event {

    private String message;

    private Object source;

    private long timestamp;

    private boolean consumed = false;

    ;

    public EventBase(Object source) {
        this.source = source;
        message = "";
        timestamp = System.currentTimeMillis();
    }

    public EventBase(Object source, String message) {
        this.source = source;
        this.message = message;
        timestamp = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.events.Event#getMessage()
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.events.Event#getSource()
     */
    public Object getSource() {
        return source;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.events.Event#getTimestamp()
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.events.Event#isConsumed()
     */
    public boolean isConsumed() {
        return consumed;
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.mindquarry.events.Event#setConsumed()
     */
    public void consume() {
        consumed = true;
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('{');
        buffer.append("EVENT: id=");
        buffer.append(getID());
        buffer.append(", timestamp=");
        buffer.append(getTimestamp());
        buffer.append(", message=");
        buffer.append(getMessage());
        buffer.append('}');
        return buffer.toString();
    }
}
