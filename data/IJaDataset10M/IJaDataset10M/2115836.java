package flex.messaging.endpoints;

import flex.messaging.MessageException;
import flex.messaging.log.LogEvent;

/**
 * Exception class used to indicate duplicate client sessions were detected.
 */
public class DuplicateSessionException extends MessageException {

    /**
     * @exclude
     */
    public static final String DUPLICATE_SESSION_DETECTED_CODE = "Server.Processing.DuplicateSessionDetected";

    /**
     * @exclude
     */
    private static final long serialVersionUID = -741704726700619666L;

    /**
     * Default constructor. 
     * Sets the code to a default value of <code>DUPLICATE_SESSION_DETECTED_CODE</code>.
     */
    public DuplicateSessionException() {
        setCode(DUPLICATE_SESSION_DETECTED_CODE);
    }

    /**
     * Override to log at the DEBUG level.
     */
    public short getPreferredLogLevel() {
        return LogEvent.DEBUG;
    }

    /**
     * Override to suppress stack trace logging.
     */
    public boolean isLogStackTraceEnabled() {
        return false;
    }
}
