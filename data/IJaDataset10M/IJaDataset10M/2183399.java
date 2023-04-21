package org.atlantal.api.app.mail;

import org.atlantal.utils.AtlantalException;

/**
 * @author f.masurel
 *
 */
public class MailException extends AtlantalException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor
     */
    public MailException() {
        super();
    }

    /**
     * Constructor
     * @param message message
     */
    public MailException(String message) {
        super(message);
    }

    /**
     * Constructor
     * @param message message
     * @param cause cause
     */
    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor
     * @param cause cause
     */
    public MailException(Throwable cause) {
        super(cause);
    }
}
