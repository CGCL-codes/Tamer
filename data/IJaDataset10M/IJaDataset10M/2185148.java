package org.gamegineer.table.internal.net.transport.tcp;

import java.nio.channels.SelectableChannel;
import net.jcip.annotations.NotThreadSafe;

/**
 * Fake implementation of
 * {@link org.gamegineer.table.internal.net.transport.tcp.AbstractEventHandler}
 */
@NotThreadSafe
class FakeEventHandler extends AbstractEventHandler {

    /** The event handler channel. */
    private final SelectableChannel channel_;

    /**
     * Initializes a new instance of the {@code FakeEventHandler} class with a
     * default fake channel.
     * 
     * @param transportLayer
     *        The transport layer associated with the event handler; must not be
     *        {@code null}.
     */
    FakeEventHandler(final AbstractTransportLayer transportLayer) {
        this(transportLayer, new FakeSelectableChannel());
    }

    /**
     * Initializes a new instance of the {@code FakeEventHandler} class with the
     * specified channel.
     * 
     * @param transportLayer
     *        The transport layer associated with the event handler; must not be
     *        {@code null}.
     * @param channel
     *        The event handler channel; must not be {@code null}.
     */
    FakeEventHandler(final AbstractTransportLayer transportLayer, final SelectableChannel channel) {
        super(transportLayer);
        assert channel != null;
        channel_ = channel;
    }

    @Override
    void close(@SuppressWarnings("unused") final Exception exception) {
        assert isTransportLayerThread();
        setState(State.CLOSED);
    }

    @Override
    SelectableChannel getChannel() {
        assert isTransportLayerThread();
        return channel_;
    }

    @Override
    int getInterestOperations() {
        assert isTransportLayerThread();
        return 0;
    }

    @Override
    void run() {
        assert isTransportLayerThread();
    }
}
