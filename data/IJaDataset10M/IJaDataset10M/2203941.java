package net.jxta.exception;

/**
 * The class JxtaException and its subclasses are a form of Exception that 
 * indicates conditions that a reasonable application might want to catch.
 * 
 */
@SuppressWarnings("serial")
public class JxtaException extends Exception {

    /**
     *  Constructs an Exception with no specified detail message.
     */
    public JxtaException() {
        super();
    }

    /**
     *  Constructs an Exception with the specified detail message.
     *
     *@param  message the detail message.
     */
    public JxtaException(String message) {
        super(message);
    }

    /**
     *    Constructs a new exception with the specified detail message and cause.
     *
     *@param  cause the cause (which is saved for later retrieval by the 
     *         Throwable.getCause() method).
     *         (A null value is permitted, and indicates that the 
     *         cause is nonexistent or unknown.)
     */
    public JxtaException(Throwable cause) {
        super(cause);
    }

    /**
     *    Constructs a new exception with the specified detail message and cause.
     *
     *@param  message message the detail message
     *@param  cause the cause (which is saved for later retrieval by the 
     *         Throwable.getCause() method).
     *         (A null value is permitted, and indicates that the 
     *         cause is nonexistent or unknown.)
     */
    public JxtaException(String message, Throwable cause) {
        super(message, cause);
    }
}
