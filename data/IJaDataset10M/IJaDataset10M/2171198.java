package org.paquitosoft.namtia.common.exceptions;

/**
 *
 * @author telemaco
 */
public class SessionException extends Exception {

    /** Creates a new instance of SessionException */
    public SessionException() {
        super();
    }

    public SessionException(String message) {
        super(message);
    }

    public SessionException(Exception e) {
        super(e);
    }

    public SessionException(String message, Throwable cause) {
        super(message, cause);
    }
}
