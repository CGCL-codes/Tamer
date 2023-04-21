package com.jvantage.ce.property;

public class PropertyDispenserException extends org.apache.commons.lang.exception.NestableException implements java.io.Serializable {

    /**
     * Creates a new instance of <code>PersistenceException</code> without detail message.
     */
    public PropertyDispenserException() {
    }

    /**
     * Constructs an instance of <code>PersistenceException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public PropertyDispenserException(String msg) {
        super(msg);
    }

    public PropertyDispenserException(Throwable e) {
        super(e);
    }

    /**
     * Constructs an instance of <code>PersistenceException</code> with the specified detail message.
     *
     * @param msg the detail message.
     * @param nested throwable.
     */
    public PropertyDispenserException(String msg, Throwable nestedException) {
        super(msg, nestedException);
    }
}
