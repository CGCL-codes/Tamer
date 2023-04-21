package jdb.exception;

/**
 */
public class NotAddableHereException extends Exception {

    /**
     * 
     */
    public NotAddableHereException() {
        super();
    }

    /**
     * @param message
     */
    public NotAddableHereException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public NotAddableHereException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public NotAddableHereException(String message, Throwable cause) {
        super(message, cause);
    }
}
