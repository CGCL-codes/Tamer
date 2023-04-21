package net.sf.mybatchfwk;

/**
 * Exception that can be used to throw an error during a task execution
 * @author J�r�me Bert�che (cyberteche@users.sourceforge.net)
 */
public class TaskExecutionException extends RuntimeException {

    private static final long serialVersionUID = 5323624385858873253L;

    public TaskExecutionException() {
        super();
    }

    public TaskExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public TaskExecutionException(String message) {
        super(message);
    }

    public TaskExecutionException(Throwable cause) {
        super(cause);
    }
}
