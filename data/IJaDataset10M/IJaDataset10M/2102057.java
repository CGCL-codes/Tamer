package com.csam.jwebsockets.server;

/**
 *
 * @author Nathan Crause <nathan at crause.name>
 * @version 1.0
 */
public class MissingRequiredPropertyException extends Exception {

    /**
     * Creates a new instance of <code>MissingRequiredPropertyException</code> without detail message.
     */
    public MissingRequiredPropertyException() {
    }

    /**
     * Constructs an instance of <code>MissingRequiredPropertyException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public MissingRequiredPropertyException(String msg) {
        super(msg);
    }
}
