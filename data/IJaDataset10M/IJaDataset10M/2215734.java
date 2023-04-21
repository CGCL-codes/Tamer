package org.encuestame.utils.exception;

/**
 * EnMe generic exception.
 * @author Picado, Juan juanATencuestame.org
 * @since Jul 3, 2011
 */
public class EnMeGenericException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 4773692936156441054L;

    /**
     * Constructor.
     */
    public EnMeGenericException() {
        super();
    }

    /**
    * Exception.
    * @param message message
    * @param cause cause
    */
    public EnMeGenericException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
    * Exception.
    * @param message message
    */
    public EnMeGenericException(final String message) {
        super(message);
    }

    /**
    * Exception.
    * @param cause cause
    */
    public EnMeGenericException(final Throwable cause) {
        super(cause);
    }
}
