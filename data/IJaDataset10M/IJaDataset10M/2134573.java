package gov.sandia.ccaffeine.dc.user_iface.MVC.event;

/**
 * Used to notify components that an entity
 * wants to print a string.  A view entity might
 * respond by writing the string to standard out.
 */
public class PrintEvent extends java.util.EventObject {

    static final long serialVersionUID = 1;

    protected String message = null;

    /**
     * Retrieve the string that is to be printed.
     * @return The string that is to be printed.
     */
    public String getMessage() {
        return (this.message);
    }

    /**
     * Create an PrintEvent.
     * This event can be used to notify components
     * that an entity wants to print a string.
     * A view entity might respond by writing the
     * string to standard out.
     * @param source The entity that created this event.
     * @param message The mesage to be printed.
     */
    public PrintEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
}
