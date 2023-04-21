package us.wthr.jdem846.exception;

@SuppressWarnings("serial")
public class CanvasException extends Exception {

    public CanvasException() {
    }

    public CanvasException(String message) {
        super(message);
    }

    public CanvasException(String message, Throwable thrown) {
        super(message, thrown);
    }
}
