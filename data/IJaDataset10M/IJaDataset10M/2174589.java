package javax.webbeans.exceptions.definition;

public class NonexistentFieldException extends NonexistentMemberException {

    public NonexistentFieldException(String message) {
        super(message);
    }

    public NonexistentFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
