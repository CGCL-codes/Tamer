package org.tanukisoftware.wrapper.event;

/**
 * WrapperPingEvent are fired each time a ping is received from the Wrapper
 *  process.   This event is mainly useful for debugging and statistic
 *  collection purposes.
 * <p>
 * WARNING - Great care should be taken when receiving events of this type.
 *  They are sent from within the Wrapper's internal timing thread.  If the
 *  listner takes too much time working with the event, Wrapper performance
 *  could be adversely affected.  If unsure, it is recommended that events
 *  of this type not be included.
 *
 * @author Leif Mortenson <leif@tanukisoftware.com>
 */
public class WrapperPingEvent extends WrapperCoreEvent {

    /**
     * Creates a new WrapperPingEvent.
     */
    public WrapperPingEvent() {
    }

    /**
     * Returns a string representation of the event.
     *
     * @return A string representation of the event.
     */
    public String toString() {
        return "WrapperPingEvent";
    }
}
