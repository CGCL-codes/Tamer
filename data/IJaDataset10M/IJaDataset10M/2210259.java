package com.divosa.security.exception;

/**
 * Super class for all Exceptions in the repository layer. Among others, this class delivers a constructor which takes the
 * actually occurring Exception (mostly a Hibernate runtime exception).
 * 
 * @author Bart Ottenkamp
 */
public class RepositoryLayerException extends Exception {

    /**
     * Constructor that wraps the actual occurring Exception.
     * 
     * @param arg0 the Exception actually occurring
     */
    public RepositoryLayerException(final Throwable arg0) {
        super(arg0);
    }

    public RepositoryLayerException(String message) {
        super(message);
    }

    public RepositoryLayerException(String message, Throwable t) {
        super(message, t);
    }
}
