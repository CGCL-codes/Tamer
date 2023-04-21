package com.jes.hibernate.helper;

/**
 * 
 * @copyright JJ People Inc.
 * @author Wei Li
 * Created on May 10, 2006
 */
public class InfrastructureException extends RuntimeException {

    /**
     * 
     */
    public InfrastructureException() {
        super();
    }

    /**
     * @param message
     */
    public InfrastructureException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public InfrastructureException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public InfrastructureException(Throwable cause) {
        super(cause);
    }
}
