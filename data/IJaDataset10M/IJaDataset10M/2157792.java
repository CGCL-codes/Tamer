package org.basegen.base.exception;

/**
 * Represents a generic exception inside repository.
 */
public class BusinessLogicException extends Exception {

    /**
     * Creates a new <code>BusinessLogicException</code> without any details.
     */
    public BusinessLogicException() {
    }

    /**
     * Creates a new <code>BusinessLogicException</code> with especified string exception message.
     * 
     * @param msg Exception message.
     */
    public BusinessLogicException(String msg) {
        super(msg);
    }

    /**
     * Creates a new <code>BusinessLogicException</code> with especified exception message.
     * 
     * @param error Original error.
     */
    public BusinessLogicException(Throwable error) {
        super(error);
    }
}
