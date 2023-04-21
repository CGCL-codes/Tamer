package la4j.err;

public class MatrixInversionException extends Exception {

    private static final long serialVersionUID = 1L;

    public MatrixInversionException() {
        this("");
    }

    public MatrixInversionException(String message) {
        super(message);
    }
}
