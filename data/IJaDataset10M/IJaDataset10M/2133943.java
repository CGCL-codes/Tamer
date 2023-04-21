package com.jme3.network.connection;

import com.jme3.network.events.ConnectionListener;
import com.jme3.network.events.MessageListener;
import com.jme3.network.message.Message;
import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for a connection method. Extend this if you have some other fancy
 *  way of dealing with connections. This class provides basic message handling, connection filtering
 *  and handles the selector.
 *
 * @author Lars Wesselius
 */
public abstract class Connection implements Runnable {

    protected String label;

    protected Logger log = Logger.getLogger(Connection.class.getName());

    protected final ArrayList<Client> connections = new ArrayList<Client>();

    protected Selector selector;

    protected boolean alive = false;

    protected ArrayList<ConnectorFilter> connectorFilters = new ArrayList<ConnectorFilter>();

    protected LinkedList<Client> disconnectionQueue = new LinkedList<Client>();

    protected ArrayList<ConnectionListener> connectionListeners = new ArrayList<ConnectionListener>();

    protected ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();

    protected HashMap<Class, List<MessageListener>> individualMessageListeners = new HashMap<Class, List<MessageListener>>();

    public Connection() {
        try {
            selector = Selector.open();
        } catch (IOException e) {
            log.log(Level.SEVERE, "Could not open selector.", e);
        }
    }

    /**
     * Add a connector filter for this connection.
     *
     * @param filter The filter to add.
     */
    public void addConnectorFilter(ConnectorFilter filter) {
        connectorFilters.add(filter);
    }

    /**
     * Remove a connector filter for this connection.
     * @param filter The filter to remove.
     */
    public void removeConnectorFilter(ConnectorFilter filter) {
        connectorFilters.remove(filter);
    }

    /**
     * Determine whether this connection should be filtered.
     *
     * @param address The address that should be checked.
     * @return The reason if it should be filtered.
     */
    public String shouldFilterConnector(InetSocketAddress address) {
        for (ConnectorFilter filter : connectorFilters) {
            String str = filter.filterConnector(address);
            if (str != null) return str;
        }
        return null;
    }

    public void run() {
        if (!alive) alive = true;
        try {
            if (selector.selectNow() > 0) {
                Set<SelectionKey> keys = selector.selectedKeys();
                for (Iterator<SelectionKey> it = keys.iterator(); it.hasNext(); ) {
                    SelectionKey key = it.next();
                    it.remove();
                    if (key.isValid() && key.isReadable()) {
                        read(key.channel());
                    }
                    if (key.isValid() && key.isAcceptable()) {
                        accept(key.channel());
                    }
                    if (key.isValid() && key.isWritable()) {
                        write(key.channel());
                    }
                    if (key.isValid() && key.isConnectable()) {
                        connect(key.channel());
                    }
                }
            }
            Client dcClient = disconnectionQueue.poll();
            while (dcClient != null) {
                disconnect(dcClient);
                dcClient = disconnectionQueue.poll();
            }
        } catch (ConnectException ce) {
            log.log(Level.WARNING, "[{0}][???] Connection refused.", label);
            fireClientDisconnected(null);
        } catch (IOException e) {
            log.log(Level.SEVERE, "[{0}][???] Error while selecting. Message: {1}", new Object[] { label, e.getMessage() });
        }
    }

    /**
     * Get all the connectors.
     *
     * @return A unmodifiable list with the connectors.
     */
    public List<Client> getLocalConnectors() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Get the combined connectors, meaning TCP and UDP are combined into one client.
     *
     * @return A unmodifiable list with the connectors.
     */
    public List<Client> getConnectors() {
        return Collections.unmodifiableList(connections);
    }

    /**
     * Return whether this connection is still alive.
     *
     * @return True if so, false if not.
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Accept an incoming connection.
     *
     * @param channel The channel.
     * @throws IOException When a problem occurs.
     */
    public abstract void accept(SelectableChannel channel) throws IOException;

    /**
     * Finish the connection.
     *
     * @param channel The channel.
     * @throws IOException When a problem occurs.
     */
    public abstract void connect(SelectableChannel channel) throws IOException;

    /**
     * Read from the channel.
     *
     * @param channel The channel.
     * @throws IOException When a problem occurs.
     */
    public abstract void read(SelectableChannel channel) throws IOException;

    /**
     * Write to a channel.
     *
     * @param channel The channel to write to.
     * @throws IOException When a problem occurs.
     */
    public abstract void write(SelectableChannel channel) throws IOException;

    /**
     * Connect to a server using this overload.
     *
     * @param address The address to connect to.
     * @throws IOException When a problem occurs.
     */
    public abstract void connect(SocketAddress address) throws IOException;

    /**
     * Bind to an address.
     *
     * @param address The address to bind to.
     * @throws IOException When a problem occurs.
     */
    public abstract void bind(SocketAddress address) throws IOException;

    /**
     * Send an object to the server. If this is a server, it will be
     *  broadcast to all clients.
     *
     * @param object The object to send.
     * @throws IOException When a writing error occurs.
     */
    public abstract void sendObject(Object object) throws IOException;

    /**
     * Send an object to the connector. Server method.
     *
     * @param connector The connector to send to.
     * @param object The object to send.
     * @throws IOException When a writing error occurs.
     */
    public abstract void sendObject(Client connector, Object object) throws IOException;

    /**
     * Called when the connection implementation should clean up.
     *
     * @throws IOException When a problem occurs.
     */
    public abstract void cleanup() throws IOException;

    public void addToDisconnectionQueue(Client client) {
        disconnectionQueue.add(client);
    }

    /**
     * Disconnect a client.
     *
     * @param client The client to disconnect.
     * @throws IOException When closing the client's channel has failed.
     */
    private void disconnect(Client client) throws IOException {
        if (client == null) return;
        Client localClient = null;
        synchronized (connections) {
            for (Client locClient : connections) {
                if (locClient.getPlayerID() == client.getPlayerID()) {
                    localClient = locClient;
                    break;
                }
            }
        }
        if (localClient == null) localClient = client;
        SocketChannel chan = localClient.getSocketChannel();
        if (chan != null) {
            SelectionKey key = chan.keyFor(selector);
            if (key != null) key.cancel();
            chan.close();
        }
        synchronized (connections) {
            connections.remove(localClient);
        }
        fireClientDisconnected(client);
    }

    public void addConnectionListener(ConnectionListener listener) {
        connectionListeners.add(listener);
    }

    public void removeConnectionListener(ConnectionListener listener) {
        connectionListeners.remove(listener);
    }

    public void addMessageListener(MessageListener listener) {
        messageListeners.add(listener);
    }

    public void removeMessageListener(MessageListener listener) {
        messageListeners.remove(listener);
    }

    public void addMessageListener(Class messageClass, MessageListener listener) {
        if (individualMessageListeners.containsKey(messageClass)) {
            individualMessageListeners.get(messageClass).add(listener);
        } else {
            List<MessageListener> list = new ArrayList<MessageListener>();
            list.add(listener);
            individualMessageListeners.put(messageClass, list);
        }
    }

    public void removeMessageListener(Class messageClass, MessageListener listener) {
        if (individualMessageListeners.containsKey(messageClass)) {
            individualMessageListeners.get(messageClass).remove(listener);
        }
    }

    @Deprecated
    public void addIndividualMessageListener(Class messageClass, MessageListener listener) {
        if (individualMessageListeners.containsKey(messageClass)) {
            individualMessageListeners.get(messageClass).add(listener);
        } else {
            List<MessageListener> list = new ArrayList<MessageListener>();
            list.add(listener);
            individualMessageListeners.put(messageClass, list);
        }
    }

    @Deprecated
    public void removeIndividualMessageListener(Class messageClass, MessageListener listener) {
        if (individualMessageListeners.containsKey(messageClass)) {
            individualMessageListeners.get(messageClass).remove(listener);
        }
    }

    protected void fireMessageReceived(Message message) {
        for (MessageListener listener : messageListeners) {
            listener.messageReceived(message);
        }
        List<MessageListener> list = individualMessageListeners.get(message.getClass());
        if (list == null) return;
        for (MessageListener listener : list) {
            listener.messageReceived(message);
        }
    }

    protected void fireMessageSent(Message message) {
        for (MessageListener listener : messageListeners) {
            listener.messageSent(message);
        }
        List<MessageListener> list = individualMessageListeners.get(message.getClass());
        if (list == null) return;
        for (MessageListener listener : list) {
            listener.messageSent(message);
        }
    }

    protected void fireObjectReceived(Object data) {
        for (MessageListener listener : messageListeners) {
            listener.objectReceived(data);
        }
        if (data == null) return;
        List<MessageListener> list = individualMessageListeners.get(data.getClass());
        if (list == null) return;
        for (MessageListener listener : list) {
            listener.objectReceived(data);
        }
    }

    protected void fireObjectSent(Object data) {
        for (MessageListener listener : messageListeners) {
            listener.objectSent(data);
        }
        List<MessageListener> list = individualMessageListeners.get(data.getClass());
        if (list == null) return;
        for (MessageListener listener : list) {
            listener.objectSent(data);
        }
    }

    protected void fireClientConnected(Client client) {
        for (ConnectionListener listener : connectionListeners) {
            listener.clientConnected(client);
        }
    }

    protected void fireClientDisconnected(Client client) {
        for (ConnectionListener listener : connectionListeners) {
            listener.clientDisconnected(client);
        }
    }
}
