package ch.comtools.ssh;

/**
 * @author Roger Dudler <roger.dudler@gmail.com>
 * @since 1.0
 * @version $Id$
 */
public class SSHClientException extends RuntimeException {

    /** default serial version uid. */
    private static final long serialVersionUID = 1L;

    /** message. */
    private String message;

    /**
	 * Constructor.
	 */
    public SSHClientException() {
    }

    /**
	 * @param message
	 * @param e
	 */
    public SSHClientException(String message, Throwable e) {
        this.setMessage(message);
        this.setStackTrace(e.getStackTrace());
    }

    /**
	 * @param message
	 */
    public SSHClientException(String message) {
        this.setMessage(message);
    }

    /**
	 * @see java.lang.Throwable#getMessage()
	 */
    public String getMessage() {
        return message;
    }

    /**
	 * @param message
	 */
    public void setMessage(String message) {
        this.message = message;
    }
}
