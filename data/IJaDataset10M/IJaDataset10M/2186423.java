package openrpg2.common.core.group;

/**
 * Simple Exception class to denote a missing or invalid Network interface
 * @author Snowdog
 */
public class InvalidRoleException extends java.lang.Exception {

    /**
     * Creates a new InvalidRoleException with a null detail message.
     */
    public InvalidRoleException() {
        super();
    }

    /**
     * Creates a new InvalidRoleException with a detail message.
     * @param msg Detail message about why this exception was thrown
     */
    public InvalidRoleException(String msg) {
        super(msg);
    }

    /**
     *  Creates a new InvalidRoleException with a detail message and a cause
     * @param msg Detail message about why this exception was thrown
     * @param t Throwable cause
     */
    public InvalidRoleException(String msg, Throwable t) {
        super(msg, t);
    }

    /**
     *  Creates a new InvalidRoleException with a null detail message and a cause
     * @param t Throwable cause
     */
    public InvalidRoleException(Throwable t) {
        super(t);
    }
}
