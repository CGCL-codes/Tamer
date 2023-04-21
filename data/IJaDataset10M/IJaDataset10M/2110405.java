package gumbo.net;

/**
 * A NetConnector for the "client-side" of a network connection. The client side
 * is responsible for initiating a connection to a server network node.
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 */
public interface ClientConnector extends NetConnector {

    /**
	 * Sets the host name and port number for the connection's remote server.
	 * @param host Server host name. Empty if null (no host).
	 * @param port Server port number. 0 if <0 (any free port).
	 */
    public void setHostPort(String host, int port);

    /**
	 * Gets the current server host name.
	 * @return The value. Possibly empty (no host), never null. 
	 */
    public String getHost();

    /**
	 * Gets the current server port number.
	 * @return The value. Possibly zero (any free port), never negative. 
	 */
    public int getPort();

    /**
	 * Blocks until a network connection to the server node is open, leaving
	 * network resources (data socket, reader, writer) non-null. If a connection
	 * is already open, nothing happens.
	 */
    public void openConnection();
}
