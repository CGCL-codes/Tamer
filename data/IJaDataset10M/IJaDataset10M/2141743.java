package javax.webbeans.exceptions.definition;

public class NonexistentConstructorException extends NonexistentMemberException {

    public NonexistentConstructorException(String message) {
        super(message);
    }

    public NonexistentConstructorException(String message, Throwable cause) {
        super(message, cause);
    }
}
