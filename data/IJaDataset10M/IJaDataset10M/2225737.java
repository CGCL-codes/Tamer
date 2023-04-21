package org.fpse.server.message;

public class FormatingFailedException extends Exception {

    private static final long serialVersionUID = 1703854292839132199L;

    public FormatingFailedException() {
        super();
    }

    public FormatingFailedException(String message) {
        super(message);
    }

    public FormatingFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormatingFailedException(Throwable cause) {
        super(cause);
    }
}
