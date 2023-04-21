package org.mortbay.loadbalancer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Channel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mortbay.util.InetAddrPort;
import org.mortbay.util.LifeCycleThread;
import org.mortbay.util.LogSupport;

public class Listener extends LifeCycleThread {

    private static Log log = LogFactory.getLog(Listener.class);

    Policy _policy;

    Selector _selector;

    ServerSocketChannel _acceptChannel;

    InetSocketAddress _address;

    ByteBufferPool _bufferPool;

    public Listener() throws IOException {
    }

    public Listener(ByteBufferPool pool, InetSocketAddress address, Policy policy) throws IOException {
        _address = address;
        _bufferPool = pool;
        _policy = policy;
    }

    public Listener(ByteBufferPool pool, InetAddrPort address, Policy policy) throws IOException {
        _address = new InetSocketAddress(address.getInetAddress(), address.getPort());
        _bufferPool = pool;
        _policy = policy;
    }

    public Selector getSelector() {
        return _selector;
    }

    public Policy getPolicy() {
        return _policy;
    }

    public InetSocketAddress getInetSocketAddress() {
        return _address;
    }

    public void setInetSocketAddress(InetSocketAddress address) {
        if (isStarted()) throw new IllegalStateException("Started");
        _address = address;
    }

    public ByteBufferPool getBufferPool() {
        return _bufferPool;
    }

    public void setBufferPool(ByteBufferPool bufferPool) {
        _bufferPool = bufferPool;
    }

    public void start() throws Exception {
        if (isStarted()) throw new IllegalStateException("Started");
        if (_bufferPool == null) throw new IllegalStateException("No BufferPool");
        _acceptChannel = ServerSocketChannel.open();
        _acceptChannel.configureBlocking(false);
        _acceptChannel.socket().bind(_address);
        log.info("Listening on " + _acceptChannel);
        _selector = Selector.open();
        _acceptChannel.register(_selector, SelectionKey.OP_ACCEPT);
        if (log.isDebugEnabled()) log.debug("Selector " + _selector);
        super.start();
    }

    public void stop() throws InterruptedException {
        super.stop();
        try {
            _selector.close();
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
        }
        try {
            _acceptChannel.close();
        } catch (Exception e) {
            log.warn(LogSupport.EXCEPTION, e);
        }
    }

    public void loop() throws Exception {
        if (log.isDebugEnabled()) log.debug("client keys=" + _selector.keys());
        if (_selector.select() > 0) {
            Set ready = _selector.selectedKeys();
            Iterator iter = ready.iterator();
            while (iter.hasNext()) {
                SelectionKey key = (SelectionKey) iter.next();
                iter.remove();
                Channel channel = key.channel();
                if (log.isDebugEnabled()) log.debug("Ready key " + key + " for " + channel);
                if (!channel.isOpen()) key.cancel(); else if (channel instanceof ServerSocketChannel) {
                    SocketChannel socket_channel = ((ServerSocketChannel) channel).accept();
                    socket_channel.configureBlocking(false);
                    socket_channel.socket().setTcpNoDelay(true);
                    Connection connection = new Connection(_bufferPool, this, socket_channel, 16);
                    socket_channel.register(_selector, SelectionKey.OP_READ, connection);
                } else if (channel instanceof SocketChannel) {
                    Connection connection = (Connection) key.attachment();
                    try {
                        if ((key.interestOps() & SelectionKey.OP_WRITE) != 0) connection.clientWriteWakeup(key); else if ((key.interestOps() & SelectionKey.OP_READ) != 0) connection.client2server(key);
                    } catch (ClosedChannelException e) {
                        LogSupport.ignore(log, e);
                    }
                }
            }
        }
    }
}
