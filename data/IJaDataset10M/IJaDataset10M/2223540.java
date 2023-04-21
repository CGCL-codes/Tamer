package lt.baltic_amadeus.jqbridge.server;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class ConfigurationException extends BridgeException {

    private static final long serialVersionUID = 2395009747280188804L;

    public ConfigurationException() {
        super("General bridge configuration exception");
    }

    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigurationException(String message) {
        super(message);
    }

    public ConfigurationException(Throwable cause) {
        super("General bridge configuration exception", cause);
    }
}
