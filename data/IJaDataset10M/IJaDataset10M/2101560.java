package uk.co.caprica.vlcj.dbus;

/**
 * Exception thrown if a DBus service is not available.
 */
public class DBusServiceNotAvailableException extends RuntimeException {

    /**
   * Serial version.
   */
    private static final long serialVersionUID = 1L;

    /**
   * Create an exception.
   * 
   * @param cause root-cause of the failure
   */
    public DBusServiceNotAvailableException(Throwable cause) {
        super(cause);
    }
}
