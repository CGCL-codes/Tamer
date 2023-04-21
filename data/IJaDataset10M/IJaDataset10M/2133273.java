package issrg.utils.handler;

/**
 *
 * @author ls97
 */
public class ConfigException extends Exception {

    /** Creates a new instance of ConfigException */
    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    /**
     * @param message
     * @param cause
     */
    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param cause
     */
    public ConfigException(Throwable cause) {
        super(cause);
    }
}
