package com.google.gwt.json.client;

/**
 * An exception that can be thrown when an interaction with a JSON data
 * structure fails.
 */
public class JSONException extends RuntimeException {

    /**
   * Constructs a new JSONException.
   */
    public JSONException() {
        super();
    }

    /**
   * Constructs a new JSONException with the specified message.
   */
    public JSONException(String message) {
        super(message);
    }

    /**
   * Constructs a new JSONException with the specified message and cause.
   */
    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
   * Constructs a new JSONException with the specified cause.
   */
    public JSONException(Throwable cause) {
        super(cause);
    }
}
