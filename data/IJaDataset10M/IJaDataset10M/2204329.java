package org.randombits.facade;

/**
 * This exception is thrown when there is a unexpected problem in the facade
 * process.
 * 
 * @author David Peterson
 */
public class FacadeException extends RuntimeException {

    public FacadeException() {
        super();
    }

    public FacadeException(String message) {
        super(message);
    }

    public FacadeException(String message, Throwable cause) {
        super(message, cause);
    }

    public FacadeException(Throwable e) {
        super(e);
    }
}
