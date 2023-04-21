package ingenias.exception;

public class InvalidEntity extends Exception {

    public InvalidEntity() {
        super();
    }

    public InvalidEntity(String message) {
        super(message);
    }

    public InvalidEntity(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidEntity(Throwable cause) {
        super(cause);
    }
}
