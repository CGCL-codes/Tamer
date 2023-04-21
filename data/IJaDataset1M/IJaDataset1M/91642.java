package de.alexanderwilden.jatoli;

public class JatoliConnectionException extends Exception {

    private static final long serialVersionUID = 1L;

    public JatoliConnectionException(String message) {
        super(message);
    }

    public JatoliConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JatoliConnectionException(Throwable cause) {
        super(cause);
    }
}
