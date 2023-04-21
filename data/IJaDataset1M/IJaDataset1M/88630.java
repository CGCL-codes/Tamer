package common.communication.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;
import common.communication.handling.Endpoint;
import common.communication.handling.EndpointHandler;
import common.communication.messages.DisconnectMessage;
import common.communication.messages.IMessage;
import common.communication.messages.IMessageToClient;
import common.communication.messages.IMessageToServer;
import common.communication.messages.MessageHandlingException;

public final class CommunicationClient {

    private Endpoint endpoint;

    private final EndpointHandler endpointHandler = new ClientEndpointHandler();

    private final Set<CommunicationClientListener> listeners = new HashSet<CommunicationClientListener>();

    public CommunicationClient() {
    }

    public void connect(String ipAddress, int port, int timeout) throws UnknownHostException, SocketTimeoutException, IOException {
        String[] parts = ipAddress.split("\\.");
        byte[] bytes = new byte[parts.length];
        for (int i = 0; i < parts.length; i++) {
            bytes[i] = Byte.parseByte(parts[i]);
        }
        SocketAddress server = new InetSocketAddress(InetAddress.getByAddress(bytes), port);
        Socket socket = new Socket();
        socket.connect(server, timeout);
        String suffix = ":" + socket.getLocalPort();
        endpoint = new Endpoint(endpointHandler, socket, new ThreadGroup("C: client" + suffix));
        endpoint.startThreads();
    }

    public void disconnect() {
        sendAsyncMessage(new DisconnectMessage());
    }

    public IMessageToClient sendSyncMessage(IMessageToServer message) throws MessageHandlingException {
        return (IMessageToClient) endpoint.sendSyncMessage(message);
    }

    public void sendAsyncMessage(IMessageToServer message) {
        endpoint.sendAsyncMessage(message);
    }

    public void addCommunicationClientListener(CommunicationClientListener listener) {
        listeners.add(listener);
    }

    public void removeCommunicationClientListener(CommunicationClientListener listener) {
        listeners.remove(listener);
    }

    private void fireDisconnected() {
        for (CommunicationClientListener listener : listeners) {
            listener.disconnected();
        }
    }

    private void fireMessageReceived(IMessageToClient message) {
        for (CommunicationClientListener listener : listeners) {
            listener.messageReceived(message);
        }
    }

    private class ClientEndpointHandler implements EndpointHandler {

        @Override
        public void disconnected() {
            fireDisconnected();
        }

        @Override
        public void handleAsynchMessage(IMessage message) {
            fireMessageReceived((IMessageToClient) message);
        }

        @Override
        public IMessage handleSynchMessage(IMessage message) {
            throw new UnsupportedOperationException("The client cannot handle synchronous messages from the server.");
        }
    }
}
