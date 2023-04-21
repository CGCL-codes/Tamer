package com.gregor.rrd;

public class RRDException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Create an exception.
     */
    public RRDException() {
        super();
    }

    /**
     * Create an exception with a message.
     *
     * @param message message text
     */
    public RRDException(String message) {
        super(message);
    }

    /**
     * Create an exception with a message and a root throwable.
     *
     * @param message message text
     * @param cause cause of this exception
     */
    public RRDException(String message, Throwable cause) {
        super(message, cause);
    }
}
