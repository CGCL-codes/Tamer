package au.edu.diasb.emmet;

public class InvalidExpiryException extends Exception {

    private static final long serialVersionUID = 4245021884161798535L;

    public InvalidExpiryException(String message) {
        super(message);
    }

    public InvalidExpiryException(String message, Throwable cause) {
        super(message, cause);
    }
}
