package de.hbrs.inf.atarrabi.action.export;

/**
 * Indicates an unsuccessful upload of a file.
 * @author Martin Dames
 */
public class ExportException extends Exception {

    private static final long serialVersionUID = -1323443074386158319L;

    public ExportException() {
        super();
    }

    public ExportException(String message) {
        super(message);
    }

    public ExportException(String message, Throwable cause) {
        super(message, cause);
    }
}
