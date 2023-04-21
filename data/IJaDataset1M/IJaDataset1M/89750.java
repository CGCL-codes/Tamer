package com.aelitis.azureus.core.proxy.impl;

import java.util.*;
import java.nio.channels.*;
import org.gudy.azureus2.core3.logging.*;
import org.gudy.azureus2.core3.util.*;
import com.aelitis.azureus.core.proxy.*;

/**
 * @author parg
 *
 */
public class AEProxyConnectionImpl implements AEProxyConnection {

    private static final LogIDs LOGID = LogIDs.NET;

    protected AEProxyImpl server;

    protected SocketChannel source_channel;

    protected volatile AEProxyState proxy_read_state = null;

    protected volatile AEProxyState proxy_write_state = null;

    protected volatile AEProxyState proxy_connect_state = null;

    protected long time_stamp;

    protected boolean is_connected;

    protected boolean is_closed;

    protected List listeners = new ArrayList(1);

    protected AEProxyConnectionImpl(AEProxyImpl _server, SocketChannel _socket, AEProxyHandler _handler) {
        server = _server;
        source_channel = _socket;
        setTimeStamp();
        try {
            proxy_read_state = _handler.getInitialState(this);
        } catch (Throwable e) {
            failed(e);
        }
    }

    public String getName() {
        String name = source_channel.socket().getInetAddress() + ":" + source_channel.socket().getPort() + " -> ";
        return (name);
    }

    public SocketChannel getSourceChannel() {
        return (source_channel);
    }

    public void setReadState(AEProxyState state) {
        proxy_read_state = state;
    }

    public void setWriteState(AEProxyState state) {
        proxy_write_state = state;
    }

    public void setConnectState(AEProxyState state) {
        proxy_connect_state = state;
    }

    protected boolean read(SocketChannel sc) {
        try {
            return (proxy_read_state.read(sc));
        } catch (Throwable e) {
            failed(e);
            return (false);
        }
    }

    protected boolean write(SocketChannel sc) {
        try {
            return (proxy_write_state.write(sc));
        } catch (Throwable e) {
            failed(e);
            return (false);
        }
    }

    protected boolean connect(SocketChannel sc) {
        try {
            return (proxy_connect_state.connect(sc));
        } catch (Throwable e) {
            failed(e);
            return (false);
        }
    }

    public void requestWriteSelect(SocketChannel sc) {
        server.requestWriteSelect(this, sc);
    }

    public void cancelWriteSelect(SocketChannel sc) {
        server.cancelWriteSelect(sc);
    }

    public void requestConnectSelect(SocketChannel sc) {
        server.requestConnectSelect(this, sc);
    }

    public void cancelConnectSelect(SocketChannel sc) {
        server.cancelConnectSelect(sc);
    }

    public void requestReadSelect(SocketChannel sc) {
        server.requestReadSelect(this, sc);
    }

    public void cancelReadSelect(SocketChannel sc) {
        server.cancelReadSelect(sc);
    }

    public void failed(Throwable reason) {
        try {
            if (Logger.isEnabled()) Logger.log(new LogEvent(LOGID, "AEProxyProcessor: " + getName() + " failed", reason));
            close();
        } catch (Throwable e) {
            Debug.printStackTrace(e);
        }
    }

    public void close() {
        is_closed = true;
        try {
            try {
                cancelReadSelect(source_channel);
                cancelWriteSelect(source_channel);
                source_channel.close();
            } catch (Throwable e) {
                Debug.printStackTrace(e);
            }
            for (int i = 0; i < listeners.size(); i++) {
                try {
                    ((AEProxyConnectionListener) listeners.get(i)).connectionClosed(this);
                } catch (Throwable e) {
                    Debug.printStackTrace(e);
                }
            }
        } finally {
            server.close(this);
        }
    }

    public boolean isClosed() {
        return (is_closed);
    }

    public void setConnected() {
        setTimeStamp();
        is_connected = true;
    }

    protected boolean isConnected() {
        return (is_connected);
    }

    public void setTimeStamp() {
        time_stamp = SystemTime.getCurrentTime();
    }

    protected long getTimeStamp() {
        return (time_stamp);
    }

    public void addListener(AEProxyConnectionListener l) {
        listeners.add(l);
    }

    public void removeListener(AEProxyConnectionListener l) {
        listeners.remove(l);
    }

    protected String getStateString() {
        return (getName() + "connected = " + is_connected + ", closed = " + is_closed + ", " + "chan: reg = " + source_channel.isRegistered() + ", open = " + source_channel.isOpen() + ", " + "read:" + (proxy_read_state == null ? null : proxy_read_state.getStateName()) + ", " + "write:" + (proxy_write_state == null ? null : proxy_write_state.getStateName()) + ", " + "connect:" + (proxy_connect_state == null ? null : proxy_connect_state.getStateName()));
    }
}
