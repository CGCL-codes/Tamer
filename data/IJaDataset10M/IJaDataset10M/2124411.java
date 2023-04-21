package eforce.bes.exceptions;

public class DequeueException extends Exception {

    public DequeueException() {
        super();
    }

    public DequeueException(String message) {
        super(message);
    }

    public DequeueException(Throwable cause) {
        super(cause);
    }

    public DequeueException(String message, Throwable cause) {
        super(message, cause);
    }
}
