package com.hirosan.jron.exception;

/**
 * Invalid schedule pattern format exception which is thrown at runtime on
 * parsing the scheduler pattern.
 * 
 * @author Hiro
 *
 */
public class InvalidFormatException extends Exception {

    /**
     * Constructor.
     * 
     * @param msg Error message.
     */
    public InvalidFormatException(String msg) {
        super(msg);
    }
}
