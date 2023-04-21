package jegg.impl;

/**
 * Lookup a port by name.
 */
public class LocatePortMessage {

    /** The name of the port to lookup. */
    private String name;

    /**
     * Construct a message specifying the port to
     * lookup.
     * @param portName the name of the port to lookup.
     */
    public LocatePortMessage(String portName) {
        name = portName;
    }

    /**
     * Return the name to lookup in the registry.
     * @return the port name to  lookup.
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return "LocatePortMessage(" + name + ")";
    }
}
