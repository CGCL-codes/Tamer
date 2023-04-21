package cgl.shindig.config;

/**
 * Exception class used for configuration errors.
 */
public class ConfigurationException extends Exception {

    /**
     * Creates a configuration exception.
     *
     * @param message configuration message
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates a configuration exception that is caused by another exception.
     *
     * @param message configuration error message
     * @param cause root cause of the configuration error
     */
    public ConfigurationException(String message, Exception cause) {
        super(message, cause);
    }
}
