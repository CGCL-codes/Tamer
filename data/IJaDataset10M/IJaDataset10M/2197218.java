package org.nakedobjects.application.value;

import org.nakedobjects.application.ApplicationException;

/**
 * Indicates that a text entry string could not be parsed correctly, preventing the value object from being
 * set up.
 */
public class ValueParseException extends ApplicationException {

    public ValueParseException(final String message) {
        super(message);
    }

    public ValueParseException(final Throwable cause) {
        super("Could not parse", cause);
    }

    public ValueParseException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
