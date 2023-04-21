package ch.ethz.dcg.spamato.filter.razor;

/**
 * @author simon
 * This class is only used to notify users of RazorServerConnection if no
 * connection can be established.
 */
public class RazorServerConnectionException extends Exception {

    public RazorServerConnectionException() {
        super();
    }

    public RazorServerConnectionException(String message) {
        super(message);
    }

    public RazorServerConnectionException(Throwable cause) {
        super(cause);
    }

    public RazorServerConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
