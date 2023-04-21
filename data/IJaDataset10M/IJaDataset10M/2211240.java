package exceptions;

import javax.servlet.ServletException;

public class NoSuchFamilyException extends ServletException {

    public NoSuchFamilyException() {
        super();
    }

    public NoSuchFamilyException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public NoSuchFamilyException(String message) {
        super(message);
    }

    public NoSuchFamilyException(Throwable rootCause) {
        super(rootCause);
    }
}
