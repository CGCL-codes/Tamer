package net.sf.syracuse.net;

import java.nio.channels.SelectableChannel;

/**
 * An interface to queue actions onto the {@code NetworkEventThread}.
 *
 * @author Chris Conrad
 * @since 1.0.0
 */
public interface NetworkEventThread {

    /**
     * Add an interest operation to a {@code Channel} already registered with the {@code NetworkEventThread}.
     *
     * @param channel     the {@code Channel} to add interest operations to
     * @param interestOps the interest operations for the {@code Channel}.  See {@link java.nio.channels.SelectionKey}
     *                    for the list of acceptable interest operations.
     */
    void addChannelInterestOps(SelectableChannel channel, int interestOps);

    /**
     * Register a {@code Channel} to the {@code NetworkEventThread}'s {@code Channel} list with the interest operations
     * specified.
     *
     * @param channel     the {@code Channel} to register
     * @param interestOps the interest operations for the {@code Channel}.  See {@link java.nio.channels.SelectionKey}
     *                    for the list of acceptable interest operations.
     */
    void registerChannel(SelectableChannel channel, int interestOps);
}
