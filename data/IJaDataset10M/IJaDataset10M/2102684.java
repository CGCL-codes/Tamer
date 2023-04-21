package com.tomecode.mjprocessor.execute;

/**
 * Basic {@link Exception}
 * 
 * @author Frastia Tomas
 * 
 */
public class MJProcessorException extends Exception {

    private static final long serialVersionUID = 3534835760817638963L;

    public MJProcessorException(String msg) {
        super(msg);
    }

    public MJProcessorException(String message, Exception e) {
        super(message, e);
    }
}
