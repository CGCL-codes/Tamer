package com.googlecode.jsonplugin;

/**
 * Wrap exceptions throwed by the JSON serializer
 */
public class JSONException extends Exception {

    public JSONException(String message) {
        super(message);
    }

    public JSONException(Throwable cause) {
        super(cause);
    }

    public JSONException(String message, Throwable cause) {
        super(message, cause);
    }
}
