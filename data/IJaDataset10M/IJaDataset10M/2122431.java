package net.q1cc.cfs.jamp.event;

/**
 *
 * @author claus
 */
public interface JAmpEvent {

    @Override
    public String toString();

    public String getName();

    public String getDebug();

    public EventType getType();

    public int getEventID();

    public Object getData();
}
