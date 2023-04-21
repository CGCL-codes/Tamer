package com.openholdem.structs.exceptions;

/**
 * @author mattmann
 * @version $Revision: 1.2 $
 * 
 * <p>A generic exception in the poker game.</p>
 * 
 */
public class PokerException extends Exception {

    public static final long serialVersionUID = 923839292929L;

    /**
     * 
     */
    public PokerException() {
        super();
    }

    /**
     * @param message
     */
    public PokerException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public PokerException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public PokerException(String message, Throwable cause) {
        super(message, cause);
    }
}
