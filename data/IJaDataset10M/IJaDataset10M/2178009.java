package de.fokus.fraunhofer.motion.soba.business.exceptions;

/**
 * @author Hajo
 *
 */
public class InvalidAccountException extends Exception {

    /**
     * 
     */
    public InvalidAccountException() {
        super();
    }

    /**
     * @param arg0
     */
    public InvalidAccountException(String arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     */
    public InvalidAccountException(Throwable arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     */
    public InvalidAccountException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }
}
