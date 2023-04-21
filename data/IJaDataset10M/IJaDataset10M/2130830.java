package com.google.gwt.maps.jsio.client;

/**
 * Thrown when a JSWrapper is attached to a JavaScriptObject that is already
 * being managed by a JSWrapper. 
 */
@SuppressWarnings("serial")
public class MultipleWrapperException extends RuntimeException {

    public MultipleWrapperException() {
    }

    public MultipleWrapperException(String message) {
        super(message);
    }
}
