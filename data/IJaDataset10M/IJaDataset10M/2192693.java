package gumbo.wip.net;

import gumbo.net.NetBrokerListener;

/**
 * Base interface for a network data connection "broker". Provides a strategy
 * for dynamic network resource management via NetConnectors in support of data
 * connection "sessions" and network error recovery. Responsible for the
 * acquisition, maintenance, and re-acquisition of network data resources.
 * <p>
 * Typically, a session will be "persistent", with the broker re-establishing
 * the connection if it is lost, or it will be "transient", with the protocol
 * breaking and re-establishing the connection for each session (e.g. each
 * request-response message exchange).
 * @author Jon Barrilleaux (jonb@jmbaai.com) of JMB and Associates Inc.
 */
public interface XXXNetBroker extends Runnable {

    /**
	 * Requests that the broker stop itself and its connector, thereby freeing
	 * all network resources as soon as possible. Once stopped a broker may not
	 * be re-usable.
	 */
    public void pleaseStop();

    /**
	 * Returns true if pleaseStop() has been called.
	 */
    public boolean isStopping();

    /**
	 * Adds a network event listener.
	 * @param listener Shared exposed listener. Never null. Ignores duplicates.
	 */
    public void addNetBrokerListener(NetBrokerListener listener);

    /**
	 * Removes a network event listener.
	 * @param listener Temp input listener. Ignores null or missing.
	 */
    public void removeNetBrokerListener(Object listener);

    public static enum EventType {

        /**
		 * Fired when the broker is ready to handle data connections.
		 */
        BROKER_READY, /**
		 * Fired when the broker terminates abnormally.
		 */
        BROKER_FAILED, /**
		 * Fired when the broker is done, for whatever reason.
		 */
        BROKER_DONE, /**
		 * Fired when a new connection has been established and is ready for 
		 * immediate use.
		 */
        DATA_READY, /**
		 * Fired when a new connection attempt fails, or an old connection or
		 * data stream terminates abnormally.
		 */
        DATA_FAILED, /**
		 * Fired when a new connection attempt fails, or an old connection
		 * terminates for whatever reason.
		 */
        DATA_DONE, /**
		 * Fired when a new server has been established and is listening
		 * for client data connections.
		 */
        SERVER_READY, /**
		 * Fired when a new server attempt fails, or an old server
		 * terminates abnormally.
		 */
        SERVER_FAILED, /**
		 * Fired when a new server attempt fails, or an old server terminates
		 * for whatever reason.
		 */
        SERVER_DONE
    }
}
