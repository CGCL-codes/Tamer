package newtonERP.orm.exceptions;

/**
 * Class used in the entity creator
 * 
 * @author r3hallejo
 */
public class OrmEntityCreationException extends OrmException {

    /**
     * 
     */
    private static final long serialVersionUID = 5641629724853451303L;

    /**
	 * @param message the message of the exception
	 */
    public OrmEntityCreationException(String message) {
        super(message);
    }

    /**
	 * 
	 */
    public OrmEntityCreationException() {
        super();
    }

    /**
	 * @param message the message
	 * @param cause the cause
	 */
    public OrmEntityCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
	 * @param cause the cause
	 */
    public OrmEntityCreationException(Throwable cause) {
        super(cause);
    }
}
