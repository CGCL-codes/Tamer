package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

import java.util.EventObject;

/**
 * Used to notify components that the cca server
 * has terminated its communication link with this client.
 * A client might
 * respond by exiting the program.
 * <p>
 * Can also be used to notify components
 * that an entity wants the application to exit.
 * A view might respond by sending a "exit"
 * message to the cca server.
 * <p>
 * Possible Scenario <br>
 * The end-user clicks on the file->exit menu item <br>
 * The cca notifies the client that it is closing its
 * communication link with the client <br>
 * The client responds by shutting down the application <br>
 */
public class ExitEvent extends EventObject {

    static final long serialVersionUID = 1;

    /**
   * Create an ExitEvent.
   * The event can be used to notify components that
   * the cca server has terminated its communication link with
   * this client.  A client might respond by
   * exiting tht program.
   * <p>
   * This event can also be used to notify components
   * that an entity wants the application to exit.
   * A view might respond by sending a "exit"
   * message to the cca server.
   * @param source The object that created this event.
   */
    public ExitEvent(Object source) {
        super(source);
    }
}
