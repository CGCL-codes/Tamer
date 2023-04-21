package org.torweg.pulse.site;

import org.torweg.pulse.service.PulseException;

/**
 * indicates errors retrieving a {@code View}.
 * 
 * @author Thomas Weber
 * @version $Revision: 1396 $
 */
public class ViewNotFoundException extends PulseException {

    /**
	 * serialVersionUID.
	 */
    private static final long serialVersionUID = -2439025645552913144L;

    /**
	 * constructs a new exception with {@code null} as its detail message.
	 */
    public ViewNotFoundException() {
        super();
    }

    /**
	 * constructs a new exception with the specified detail message.
	 * 
	 * @param message
	 *            the message of the exception
	 */
    public ViewNotFoundException(final String message) {
        super(message);
    }

    /**
	 * constructs a new exception with the specified detail message and cause.
	 * 
	 * @param message
	 *            the message of the exception
	 * @param cause
	 *            the cause of the exception
	 */
    public ViewNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
	 * constructs a new exception with the specified detail message and cause.
	 * 
	 * @param cause
	 *            the cause of the exception
	 */
    public ViewNotFoundException(final Throwable cause) {
        super(cause);
    }
}
