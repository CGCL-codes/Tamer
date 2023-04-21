package net.sf.opentranquera.xkins.core;

/**
 * Exception para checked Exceptions
 * @author Guillermo Meyer
 */
public class XkinsException extends Exception {

    /**
     *
     */
    public XkinsException() {
        super();
    }

    /**
     * @param message
     */
    public XkinsException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public XkinsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public XkinsException(Throwable cause) {
        super(cause);
    }
}
