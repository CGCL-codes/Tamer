package abstractService.compensationManagement;

/**
 * The exception for the compensation management.
 * 
 * @author Michael Sch�fer
 *
 */
public class CompensationManagerException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs a new exception.
	 *
	 */
    public CompensationManagerException() {
        super();
    }

    /**
	 * Constructs a new exception with the given string as message.
	 * @param message The exception message.
	 */
    public CompensationManagerException(String message) {
        super(message);
    }
}
