package fr.gedeon.telnetservice.osgiconsole;

public class ExecuteCommandException extends Exception {

    private static final long serialVersionUID = -7321909689957476858L;

    public ExecuteCommandException(String message) {
        super(message);
    }

    public ExecuteCommandException(String message, Throwable cause) {
        super(message, cause);
    }
}
