package ultramc.connect;

import java.nio.channels.*;
import java.io.IOException;

public class ServerConnection {

    private Selector m_selector;

    private SocketChannel m_channel;

    private ServerConnectionPool m_pool;

    public ServerConnection(SocketChannel channel, ServerConnectionPool pool) throws IOException {
        m_pool = pool;
        m_channel = channel;
        m_channel.configureBlocking(false);
        m_selector = Selector.open();
        channel.register(m_selector, SelectionKey.OP_READ);
    }

    public Selector getSelector() {
        return (m_selector);
    }

    public SocketChannel getChannel() {
        return (m_channel);
    }

    public void closeConnection() {
        try {
            m_selector.close();
            m_channel.close();
        } catch (IOException ioe) {
        }
    }

    public void recycleConnection() {
        m_pool.returnConnection(this);
    }
}
