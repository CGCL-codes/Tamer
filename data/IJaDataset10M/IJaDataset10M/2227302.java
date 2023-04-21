package flm.fiado.exception;

public class DataInvalidaException extends Exception {

    public DataInvalidaException() {
    }

    public DataInvalidaException(String message) {
        super(message);
    }

    public DataInvalidaException(Throwable cause) {
        super(cause);
    }

    public DataInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }
}
