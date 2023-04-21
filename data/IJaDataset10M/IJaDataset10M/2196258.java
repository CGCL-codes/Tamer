package ch.unifr.nio.framework;

/**
 * An abstract ChannelHandler for client socket connections.
 *
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public abstract class AbstractClientSocketChannelHandler extends AbstractChannelHandler implements ClientSocketChannelHandler {

    /**
     * creates a new AbstractClientSocketChannelHandler
     */
    public AbstractClientSocketChannelHandler() {
    }

    /**
     * creates a new AbstractClientSocketChannelHandler
     *
     * @param directReading if
     * <code>true</code>, a direct buffer is used for reading, otherwise a
     * non-direct buffer
     * @param initialReadingCapacity the initial capacity of the reading buffer
     * in byte
     * @param maxReadingCapacity the maximum capacity of the reading buffer in
     * byte
     * @param directWriting if
     * <code>true</code>, a direct buffer is used for buffering data from
     * incomplete write operations, otherwise a non-direct buffer
     */
    public AbstractClientSocketChannelHandler(boolean directReading, int initialReadingCapacity, int maxReadingCapacity, boolean directWriting) {
        super(directReading, initialReadingCapacity, maxReadingCapacity, directWriting);
    }
}
