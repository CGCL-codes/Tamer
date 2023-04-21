package com.gwtent.client.reflection;

/**
 * Indicates that a source declaration was not parsed successfully.
 */
public class ParseException extends TypeOracleException {

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParseException(Throwable cause) {
        super(cause);
    }
}
