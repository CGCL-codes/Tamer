package skycastle.machinesystem;

/**
 * Something that can be connected to another port with a connection.
 * <p/>
 * A port can also be directly connected to the physical environment it is located in, e.g. a substance input port
 * to recieve the averaged environment mix of substances.
 * <p/>
 * An attachment may act as a sieved port or an active port, that selectively transmits substances / objects.
 *
 * @author Hans H�ggstr�m
 */
public interface Port<C extends Connection> {

    /**
     * @return the connection connected to this port, or null if it has no connection.
     */
    C getConnection();

    /**
     * @param connection the connection connected to this port, or null if it has no connection.
     */
    void setConnection(C connection);

    /**
     * Creates a new connection between this port and the other port.
     * <p/>
     * The connection is also added to the universe for simulation.
     *
     * @param otherPort the port to connect to.  Should not be null or the same port.
     */
    void createConnection(Port<C> otherPort);

    /**
     * Disconnects the connection connected to this port from both its ports, and removes it from the universe.
     */
    void disconnect();
}
