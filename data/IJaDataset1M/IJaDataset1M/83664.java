package abstractService.serviceManagement;

/**
 * The exception for the concrete service manager.
 * 
 * @author Michael Sch�fer
 *
 */
public class ConcreteServiceManagerException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructs a new exception.
	 *
	 */
    public ConcreteServiceManagerException() {
        super();
    }

    /**
	 * Constructs a new exception with the given string as message.
	 * @param message The exception message.
	 */
    public ConcreteServiceManagerException(String message) {
        super(message);
    }
}
