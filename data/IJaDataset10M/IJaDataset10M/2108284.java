package jdb.exception;

/**
 */
public class DuplicateDataException extends DataImportException {

    /**
     * 
     */
    public DuplicateDataException() {
        super();
    }

    /**
     * @param message
     */
    public DuplicateDataException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public DuplicateDataException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public DuplicateDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
