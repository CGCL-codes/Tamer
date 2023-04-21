package jolie;

public class InvalidIdException extends Exception {

    private static final long serialVersionUID = 1L;

    public InvalidIdException(String id) {
        super("invalid identifier: " + id);
    }
}
