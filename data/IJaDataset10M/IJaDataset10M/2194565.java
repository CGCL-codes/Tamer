package booksandfilms.shared.exception;

import java.io.Serializable;

@SuppressWarnings("serial")
public class CannotDeleteException extends Exception implements Serializable {

    public CannotDeleteException() {
    }

    public CannotDeleteException(String message) {
        super(message);
    }

    public CannotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotDeleteException(Throwable cause) {
        super(cause);
    }
}
