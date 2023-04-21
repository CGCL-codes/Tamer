package pl.edu.mimuw.loxim.protogen.lang.java.template.exception;

public class ProtocolException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public ProtocolException() {
        super();
    }

    public ProtocolException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProtocolException(String message) {
        super(message);
    }

    public ProtocolException(Throwable cause) {
        super(cause);
    }
}
