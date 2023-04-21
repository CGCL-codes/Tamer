package org.codecover.eclipse.tscmanager.exceptions;

/**
 * This exception is thrown if a test session container file couldn't be
 * created.
 * 
 * @author Robert Hanussek
 * @version 1.0 ($Id: TSCFileCreateException.java 1 2007-12-12 17:37:26Z t-scheller $)
 */
@SuppressWarnings("serial")
public class TSCFileCreateException extends Exception {

    public TSCFileCreateException(String message, Throwable cause) {
        super(message, cause);
    }

    public TSCFileCreateException(String message) {
        super(message);
    }
}
