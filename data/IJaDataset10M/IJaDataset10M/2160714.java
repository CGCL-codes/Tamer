package uvw.event;

import uvw.event.syscalls.*;

/**
 * Interface for all the event handlers used in Uberviewer. The only 
 * method specified here is the notifyEvent method that shall be called 
 * by the <code>EventManager</code> instance of the running Uberviewer.
 * @since 0.1
 *
 * @see EventManager
 */
public interface EventHandler {

    /**
 * Used when an event occured and must be handled.
 *
 * @param event the event which arrived
 */
    public void notifyEvent(UberloggerEvent event);
}
