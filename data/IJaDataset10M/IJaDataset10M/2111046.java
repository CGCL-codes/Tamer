package org.achup.elgenerador.validator;

/**
 *
 * @author Marco Bassaletti
 */
public class ValidatorException extends RuntimeException {

    private static final long serialVersionUID = 0L;

    public ValidatorException(Throwable cause) {
        super(cause);
    }

    public ValidatorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidatorException(String message) {
        super(message);
    }

    public ValidatorException() {
    }
}
