package net.sf.jdpa;

/**
 * @author Andreas Nilsson
 */
public class CompilationException extends RealizationException {

    public CompilationException() {
    }

    public CompilationException(String message) {
        super(message);
    }

    public CompilationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompilationException(Throwable cause) {
        super(cause);
    }
}
