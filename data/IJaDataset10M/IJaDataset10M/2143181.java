package de.tud.kom.nat.nattrav.broker;

import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ConnectionTargetRequestor {

    /** The Singleton-instance. */
    private static final ConnectionTargetRequestor instance = new ConnectionTargetRequestor();

    /** The Singleton-getter. */
    public static final ConnectionTargetRequestor getInstance() {
        return instance;
    }

    /** The Singleton-constructor. */
    private ConnectionTargetRequestor() {
    }

    /** Hashmap of mappings from channel to address. */
    private HashMap<SelectableChannel, InetSocketAddress> mappings = new HashMap<SelectableChannel, InetSocketAddress>();

    /**
	 * Returns the target of a given selectable channel.
	 * @param channel
	 * @return
	 */
    public synchronized InetSocketAddress getRemoteAddress(SelectableChannel channel) {
        if (channel == null) throw new IllegalArgumentException("Channel must not be null!");
        InetSocketAddress isa = mappings.get(channel);
        if (isa == null) {
            if (channel instanceof DatagramChannel) isa = (InetSocketAddress) ((DatagramChannel) channel).socket().getRemoteSocketAddress(); else if (channel instanceof SocketChannel) isa = (InetSocketAddress) ((SocketChannel) channel).socket().getRemoteSocketAddress(); else throw new IllegalStateException("Unknown channel type!");
        }
        return isa;
    }

    /**
	 * Returns true if the given channel is a relay connection.
	 * @param channel channel
	 * @return true if the given channel is a relay connection, false otherwise
	 */
    public synchronized boolean isRelayed(SelectableChannel channel) {
        return mappings.containsKey(channel);
    }

    /**
	 * Adds the relay information about the given channel.
	 * 
	 * @param channel channel
	 * @param relayHost the real target
	 */
    synchronized void addRelayedConnection(SelectableChannel channel, InetSocketAddress realTarget) {
        mappings.put(channel, realTarget);
    }

    /**
	 * Removes the information about the given <tt>channel</tt>.
	 * @param channel channel
	 */
    synchronized void removeRelayedConnection(SelectableChannel channel) {
        mappings.remove(channel);
    }
}
