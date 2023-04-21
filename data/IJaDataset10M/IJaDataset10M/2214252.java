package somecoder.prefered;

public class PreferedUnknownException extends RuntimeException {

    public PreferedUnknownException() {
    }

    public PreferedUnknownException(String message) {
        super(message);
    }

    public PreferedUnknownException(Throwable cause) {
        super(cause);
    }

    public PreferedUnknownException(String message, Throwable cause) {
        super(message, cause);
    }
}
