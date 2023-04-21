package ch.iserver.ace.net.protocol;

/**
 * DeserializeException thrown when an error while 
 * deserialization occurs.
 */
public class DeserializeException extends Exception {

    /**
	 * the exception that caused this exception to be created
	 */
    private Exception reason;

    /**
	 *
	 * Creates a new DeserializeException with the causing
	 * reason exception.
	 *
	 * @param reason		the causing exception
	 */
    public DeserializeException(Exception reason) {
        this.reason = reason;
    }

    /**
	 * Constructor.
	 * 
	 * @param message an error message
	 */
    public DeserializeException(String message) {
        super(message);
    }

    /**
	 * Gets the reason.
	 * @return	the reason exception
	 */
    public Exception getReason() {
        return reason;
    }
}
