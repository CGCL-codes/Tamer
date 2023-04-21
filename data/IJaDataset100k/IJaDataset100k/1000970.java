package org.jcvi.platetools;

/**
 *
 *
 * @author jsitz@jcvi.org
 */
public class FatalLoaderException extends RuntimeException {

    /** The Serial Version UID */
    private static final long serialVersionUID = 6208549987179699407L;

    /**
     * Creates a new <code>FatalLoaderException</code>.
     * @param message
     */
    public FatalLoaderException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>FatalLoaderException</code>.
     * @param message
     * @param cause
     */
    public FatalLoaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
