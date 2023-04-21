package frost.pluginmanager;

/**
 * @author saces
 *
 */
public class PluginNotFoundException extends Exception {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * 
	 */
    public PluginNotFoundException() {
    }

    /**
	 * @param message
	 */
    public PluginNotFoundException(String message) {
        super(message);
    }

    /**
	 * @param cause
	 */
    public PluginNotFoundException(Throwable cause) {
        super(cause);
    }

    /**
	 * @param message
	 * @param cause
	 */
    public PluginNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
