package org.isakiev.wic.core.polynomial;

/**
 * Polynomial exception
 *
 * @author Ruslan Isakiev
 */
public class PolynomialException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PolynomialException(String message) {
        super(message);
    }

    public PolynomialException(Throwable cause) {
        super(cause);
    }

    public PolynomialException(String message, Throwable cause) {
        super(message, cause);
    }
}
