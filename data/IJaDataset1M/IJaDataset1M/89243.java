package org.fest.swing.remote.client;

import java.net.Socket;
import org.fest.assertions.AssertExtension;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Understands assertion methods releated to sockets.
 *
 * @author Alex Ruiz
 */
public final class SocketAssert implements AssertExtension {

    private final Socket socket;

    /**
   * Creates a new </code>{@link SocketAssert}</code>.
   * @param socket the socket to verify.
   */
    public SocketAssert(Socket socket) {
        this.socket = socket;
    }

    public SocketAssert isConnectedTo(String host, int port) {
        assertThat(socket.isBound()).isTrue();
        assertThat(socket.getInetAddress().getCanonicalHostName()).isEqualTo(host);
        assertThat(socket.getPort()).isEqualTo(port);
        assertThat(socket.isClosed()).isFalse();
        return this;
    }

    public SocketAssert isClosed() {
        assertThat(socket.isClosed()).isTrue();
        return this;
    }
}
