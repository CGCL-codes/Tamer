package br.net.woodstock.rockframework.security.crypt;

import br.net.woodstock.rockframework.security.SecurityException;

public class CrypterException extends SecurityException {

    private static final long serialVersionUID = 1L;

    public CrypterException(final String message) {
        super(message);
    }

    public CrypterException(final Throwable cause) {
        super(cause);
    }

    public CrypterException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
