package org.skycastle.connection;

import org.skycastle.old.messaging.Message;

/**
 * An utility class that can be used to simulate a network connection between a client and server. Useful e.g.
 * for unit testing.
 * <p/>
 * Provides a client and a server side connection handler, and forwards messages between them. Messages are
 * forwarded syncronously, by calling the listeners on the other side and returning when they are done
 * processing.
 * <p/>
 * The connect method should be called first on the client side to establish the connection, and disconnect
 * can be used to severe the connection.
 *
 * @author Hans Häggström
 */
public final class PassThroughConnectionHandlers {

    private final ServerSideHandler myServerSideHandler = new ServerSideHandler();

    private final ClientSideHandler myClientSideHandler = new ClientSideHandler();

    /**
     * Creates a new {@link PassThroughConnectionHandlers}.
     */
    public PassThroughConnectionHandlers() {
        setStatuses(ConnectionHandlerStatus.STARTING);
    }

    /**
     * @return the {@link ConnectionHandler} that simulates the server side. Any messages sent from it are
     *         sent to listeners on the client side immediately.
     *         <p/>
     *         Before the connection works, the client has to call its connect method.
     */
    public ConnectionHandler getServerSideConnectionHandler() {
        return myServerSideHandler;
    }

    /**
     * @return the {@link ConnectionHandler} that simulates the client side. Any messages sent from it are
     *         sent to listeners on the server side immediately.
     *         <p/>
     *         Before the connection works, the client has to call its connect method.
     */
    public ClientConnectionHandler getClientSideConnectionHandler() {
        return myClientSideHandler;
    }

    private void setStatuses(final ConnectionHandlerStatus status) {
        myClientSideHandler.setStatus(status);
        myServerSideHandler.setStatus(status);
    }

    private boolean connectionOk() {
        return myClientSideHandler.isConnected() && myServerSideHandler.isConnected();
    }

    private final class ServerSideHandler extends AbstractConnectionHandler {

        public void sendMessage(final Message message) {
            if (connectionOk()) {
                myClientSideHandler.notifyMessageRecieved(message);
            }
        }

        public void disconnect() {
            setStatuses(ConnectionHandlerStatus.DISCONNECTED);
        }
    }

    private final class ClientSideHandler extends AbstractConnectionHandler implements ClientConnectionHandler {

        public void connect() {
            setStatuses(ConnectionHandlerStatus.CONNECTED);
        }

        public void sendMessage(final Message message) {
            if (connectionOk()) {
                myServerSideHandler.notifyMessageRecieved(message);
            }
        }

        public void disconnect() {
            setStatuses(ConnectionHandlerStatus.DISCONNECTED);
        }
    }
}
