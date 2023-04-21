package net.sf.jimo.modules.updater.api;

public class UpdaterException extends Exception {

    public UpdaterException() {
    }

    public UpdaterException(String message) {
        super(message);
    }

    public UpdaterException(Throwable cause) {
        super(cause);
    }

    public UpdaterException(String message, Throwable cause) {
        super(message, cause);
    }
}
