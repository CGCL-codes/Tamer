package com.raverun.dbrb;

/**
 *
 * @author  gavinb
 * @version 
 */
public class DatabaseErrorException extends Exception {

    /**
     * Creates new <code>DatabaseErrorException</code> without detail message.
     */
    public DatabaseErrorException() {
    }

    /**
     * Constructs an <code>DatabaseErrorException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public DatabaseErrorException(String msg) {
        super(msg);
    }
}
