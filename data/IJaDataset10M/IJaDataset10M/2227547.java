package org.homeunix.thecave.moss.exception;

public class DocumentSaveException extends Exception {

    public static final long serialVersionUID = 0;

    public DocumentSaveException() {
        super();
    }

    public DocumentSaveException(String message) {
        super(message);
    }

    public DocumentSaveException(String message, Throwable cause) {
        super(message, cause);
    }

    public DocumentSaveException(Throwable cause) {
        super(cause);
    }
}
