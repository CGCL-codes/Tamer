package hanasu.p2p;

/**
 * @author Marc Miltenberger
 * Is used for connection related events.
 */
public interface IConnection extends IMessageReceived {

    /**
	 * A connection has been established
	 * @param connection the established Connection
	 */
    public void connectionEstablished(Connection connection);

    /**
	 * A connection has been closed
	 * @param connection the closed connection
	 */
    public void connectionClosed(Connection connection);

    /**
	 * The connection could not be established
	 * @param connection the failed connection
	 */
    public void connectionFailed(Connection connection);
}
