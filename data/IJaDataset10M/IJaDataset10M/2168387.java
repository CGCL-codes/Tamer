package name.herlin.command;

/**
 * Exception thrown by the performExcecute method of a Command if not all
 * required input parameters of a command are not set. More formally, this
 * exception is thrown when Command.isReadyToExecute returns false.
 */
public class UnsetInputPropertiesException extends CommandException {

    private static final long serialVersionUID = 1L;

    /**
     * @param message the exception message
     * @param cause a root cause
     */
    public UnsetInputPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause a root cause
     */
    public UnsetInputPropertiesException(Throwable cause) {
        super(cause);
    }

    /**
     * Default constructor
     *
     */
    public UnsetInputPropertiesException() {
        super();
    }

    /**
     * @param message the exception message
     */
    public UnsetInputPropertiesException(String message) {
        super(message);
    }
}
