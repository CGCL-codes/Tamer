package org.hrodberaht.directus.exception;

import java.text.MessageFormat;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 * @version 1.0
 * @since 1.0
 */
public class MessageRuntimeException extends RuntimeException {

    private Object[] args = null;

    public MessageRuntimeException(String message) {
        super(message);
    }

    public MessageRuntimeException(String message, Throwable e) {
        super(message, e);
    }

    public MessageRuntimeException(Throwable e) {
        super(e);
    }

    public MessageRuntimeException(String message, Object... args) {
        super(message);
        this.args = args;
    }

    public MessageRuntimeException(String message, Throwable e, Object... args) {
        super(message, e);
        this.args = args;
    }

    public MessageRuntimeException(Throwable e, Object... args) {
        super(e);
        this.args = args;
    }

    @Override
    public String toString() {
        if (args != null) {
            return MessageRuntimeException.class.getName() + ": " + MessageFormat.format(super.getMessage(), args);
        }
        return super.toString();
    }

    @Override
    public String getMessage() {
        if (args != null) {
            return MessageFormat.format(super.getMessage(), args);
        }
        return super.getMessage();
    }
}
