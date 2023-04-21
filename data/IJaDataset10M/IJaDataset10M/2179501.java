package lt.baltic_amadeus.jqbridge.msg;

/** Describes a destination of a message without going into provider-specific details.
 * 
 * @author Baltic Amadeus, JSC
 * @author Antanas Kompanas
 *
 */
public class BridgeDestination {

    private String name;

    public BridgeDestination(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String asPropertyString() {
        if (name == null) return ""; else return name;
    }
}
