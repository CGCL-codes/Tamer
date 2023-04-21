package net.jadoth.entitydatamapping.exceptions;

import java.lang.reflect.Method;

/**
 * The Class EntityDataInvalidAccessorMethodException.
 * 
 * @author Thomas M�nz
 */
public class EntityDataInvalidAccessorMethodException extends EntityMappingException {

    /** The cause method. */
    private Method causeMethod;

    /**
	 * Gets the cause method.
	 * 
	 * @return the causeMethod
	 */
    public Method getCauseMethod() {
        return causeMethod;
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1299979778137504525L;

    /**
	 * Instantiates a new entity data invalid accessor method exception.
	 */
    public EntityDataInvalidAccessorMethodException() {
        super();
    }

    /**
	 * Instantiates a new entity data invalid accessor method exception.
	 * 
	 * @param message the message
	 */
    public EntityDataInvalidAccessorMethodException(String message) {
        super(message);
    }

    /**
	 * Instantiates a new entity data invalid accessor method exception.
	 * 
	 * @param causeMethod the cause method
	 */
    public EntityDataInvalidAccessorMethodException(Method causeMethod) {
        super();
        this.causeMethod = causeMethod;
    }

    /**
	 * Instantiates a new entity data invalid accessor method exception.
	 * 
	 * @param cause the cause
	 */
    public EntityDataInvalidAccessorMethodException(Throwable cause) {
        super(cause);
    }

    /**
	 * Instantiates a new entity data invalid accessor method exception.
	 * 
	 * @param message the message
	 * @param cause the cause
	 */
    public EntityDataInvalidAccessorMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
