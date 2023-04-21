package ch.unifr.nio.framework;

import java.io.IOException;

/**
 * A ChannelHandler for SocketChannels on client sides that uses the NIO
 * framework to resolve the host name of the target system and to establish the
 * connection to the target system.
 *
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public interface ClientSocketChannelHandler extends ChannelHandler {

    /**
     * called by the framework if resolving the host name failed
     */
    void resolveFailed();

    /**
     * Called by the framework if connecting to the given host succeeded.
     * WARNING: Do not use blocking calls within this method or handling of the
     * connection by the NIO Framework will be stalled.
     */
    void connectSucceeded();

    /**
     * called by the framework if connecting to the given host failed
     *
     * @param exception the exception that occured when the connection failed
     */
    void connectFailed(IOException exception);
}
