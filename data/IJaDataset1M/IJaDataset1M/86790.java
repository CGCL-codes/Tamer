package com.tchepannou.rails.core.exception;

/**
 * This exception is thrown whenever the mapping of a model failed
 * 
 * @author herve
 */
public class MappingException extends RuntimeException {

    public MappingException(Throwable cause) {
        super(cause);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException() {
    }
}
