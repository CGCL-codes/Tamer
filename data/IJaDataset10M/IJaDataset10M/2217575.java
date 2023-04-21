package equilibrium.commons.report.config;

/**
 * @author Michal Bartyzel
 * @author Mariusz Sieraczkiewicz
 * 
 * @since 0.1
 */
public class ConfigurationReaderException extends RuntimeException {

    public ConfigurationReaderException() {
        super();
    }

    public ConfigurationReaderException(final String message) {
        super(message);
    }

    public ConfigurationReaderException(final Throwable cause) {
        super(cause);
    }

    public ConfigurationReaderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
