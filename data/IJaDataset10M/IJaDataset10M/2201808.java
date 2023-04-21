package lt.baltic_amadeus.jqbridge.server;

/**
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class PortNotFoundException extends BridgeException {

    private static final long serialVersionUID = -6493852650571118202L;

    public PortNotFoundException(String name) {
        super("Port " + name + " is not present");
    }
}
