package bg.tu_sofia.refg.imsqti.testitem;

public class DeserializationException extends Exception {

    private static final long serialVersionUID = 4971969571660375119L;

    public DeserializationException() {
        super();
    }

    public DeserializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeserializationException(String message) {
        super(message);
    }

    public DeserializationException(Throwable cause) {
        super(cause.getMessage(), cause);
    }
}
