package org.yosemite.system;

/**
 * @author Santal Li
 *
 * For get cause of Exception is be introduce only by JDK1.4 and later version
 * so, using this Exception do the same work of it.
 * And I think the declare exception mechanism is not easy to using, so all the
 * excetpion will be runtime exception
 */
public class CauseableException extends RuntimeException {

    protected Throwable cause;

    /**
     * Construct a new exception with the specified detail message and cause.
     *
     * @param message The detail message
     * @param cause The underlying cause
     */
    CauseableException(String msg, Throwable cause) {
        super(msg + "(cause by:" + cause.toString() + ")");
        this.cause = cause;
    }

    /**
     * Construct a new exception with the specified detail message.
     *
     * @param message The detail message
     */
    CauseableException(String msg) {
        super(msg);
    }

    /**
     * Construct a new exception with the specified cause and a derived detail
     * message.
     * 
     * @param cause The underlying cause
     */
    CauseableException(Throwable cause) {
        this((cause == null) ? null : cause.toString(), cause);
    }

    /**
     * Construct a new exception with <code>null</code> as its detail message.
     */
    CauseableException() {
        super();
    }

    /**
     * 
     */
    public Throwable getCause() {
        return cause;
    }
}
