package org.jtools.mvc;

/**
 * TODO type-description
 * @author <a href="mailto:rainer.noack@jtools.org">Rainer Noack</a>
 */
public class MVCException extends Exception {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    /**
     * @param message
     */
    public MVCException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public MVCException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public MVCException(Throwable cause) {
        super(cause);
    }
}
