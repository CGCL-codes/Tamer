package org.openwms.core.service.exception;

/**
 * A ServiceException is a checked application exception thrown in service layer
 * classes.
 * 
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 * @version $Revision: 1538 $
 * @since 0.1
 */
public class ServiceException extends Exception {

    private static final long serialVersionUID = -4416392376427389375L;

    /**
     * Create a new ServiceException with a message text.
     * 
     * @param message
     *            Detail message
     */
    public ServiceException(String message) {
        super(message);
    }

    /**
     * Create a new ServiceException with a cause exception.
     * 
     * @param cause
     *            Root cause
     */
    public ServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * Create a new ServiceException with a message text and the cause
     * exception.
     * 
     * @param message
     *            Detail message
     * @param cause
     *            Root cause
     */
    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
