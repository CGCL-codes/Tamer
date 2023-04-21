package nodomain.applewhat.torrentdemonio.protocol.tracker;

/**
 * @author Alberto Manzaneque
 *
 */
public class TrackerProtocolException extends Exception {

    /**
	 * 
	 */
    public TrackerProtocolException() {
    }

    /**
	 * @param message
	 */
    public TrackerProtocolException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public TrackerProtocolException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public TrackerProtocolException(String message, Throwable cause) {
        super(message, cause);
    }
}
