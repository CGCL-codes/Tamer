package net.livebookstore.exception;

/**
 * Base exception for LiveBookstore application.
 * 
 * @author Xuefeng
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 8454720689190309547L;

    public ApplicationException() {
        super();
    }

    public ApplicationException(Throwable t) {
        super(t);
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable t) {
        super(message, t);
    }
}
