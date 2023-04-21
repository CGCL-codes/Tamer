package org.webcastellum.exception;

public final class CustomRequestMatchingException extends java.lang.Exception {

    /**
	 * Serial Version UID
	 */
    private static final long serialVersionUID = 1L;

    public CustomRequestMatchingException() {
    }

    public CustomRequestMatchingException(String msg) {
        super(msg);
    }

    public CustomRequestMatchingException(Throwable cause) {
        super(cause);
    }

    public CustomRequestMatchingException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
