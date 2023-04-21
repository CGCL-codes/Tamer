package org.criticalfailure.anp.core.domain;

/**
 * @author cipher@users.sourceforge.net
 * 
 */
public class ObjectCreationException extends Exception {

    /**
     * 
     */
    public ObjectCreationException() {
    }

    /**
     * @param message
     */
    public ObjectCreationException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public ObjectCreationException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public ObjectCreationException(String message, Throwable cause) {
        super(message, cause);
    }
}
