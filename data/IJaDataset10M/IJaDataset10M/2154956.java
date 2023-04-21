package org.plazmaforge.framework.core.exception;

/**
 * @author Oleh Hapon
 */
public class ApplicationError extends Error {

    public ApplicationError() {
        super();
    }

    public ApplicationError(String message) {
        super(message);
    }

    public ApplicationError(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationError(Throwable cause) {
        super(cause);
    }
}
