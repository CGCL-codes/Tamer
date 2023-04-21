package be.appcraft.cmfc.domain.ships;

public class OverweightException extends Exception {

    private static final long serialVersionUID = -8613115265170513605L;

    public OverweightException() {
        super();
    }

    public OverweightException(String message, Throwable cause) {
        super(message, cause);
    }

    public OverweightException(String message) {
        super(message);
    }

    public OverweightException(Throwable cause) {
        super(cause);
    }
}
