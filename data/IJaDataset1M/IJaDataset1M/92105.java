package ch.heuscher.simple.exception;

public class NoMetadataFoundException extends Exception {

    static final long serialVersionUID = ((long) 182937287) * ((long) 721872132);

    public NoMetadataFoundException() {
        super();
    }

    public NoMetadataFoundException(String message) {
        super(message);
    }

    public NoMetadataFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoMetadataFoundException(Throwable cause) {
        super(cause);
    }
}
