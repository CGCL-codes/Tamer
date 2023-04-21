package net.kano.joustsim.app.config;

public class LoadingException extends Exception {

    public LoadingException() {
    }

    public LoadingException(String message) {
        super(message);
    }

    public LoadingException(Throwable cause) {
        super(cause);
    }

    public LoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
