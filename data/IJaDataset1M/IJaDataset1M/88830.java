package savenews.backend.exceptions;

/**
 * Thrown when application is about to overwrite a file.
 * @author Eduardo Ferreira
 */
public class FileAlreadyExistsException extends AppException {

    /** SVUID */
    private static final long serialVersionUID = 1L;

    public FileAlreadyExistsException() {
        super();
    }

    public FileAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileAlreadyExistsException(String message) {
        super(message);
    }

    public FileAlreadyExistsException(Throwable cause) {
        super(cause);
    }
}
