package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

/**
 * Used to notify components
 * that an entity wishes to
 * emit a heartbeat.
 * A view might respond by sending
 * a heartbeat to the cca srever.
 */
public class HeartbeatEvent extends java.util.EventObject {

    static final long serialVersionUID = 1;

    /**
   * Create a HeartbeatEvent.
   * Can be
   * used to notify components
   * that an entity wishes to
   * emit a heartbeat.
   * A view might respond by sending
   * a heartbeat to the cca srever.
   * @param source The entity that created this event.
   */
    public HeartbeatEvent(Object source) {
        super(source);
    }
}
