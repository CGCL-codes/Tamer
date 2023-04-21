package com.peterhi.client.nio;

import java.io.IOException;
import java.net.PasswordAuthentication;
import java.nio.BufferUnderflowException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javolution.util.FastSet;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.stream.HandlerChain;
import org.xsocket.stream.IConnectHandler;
import org.xsocket.stream.IDataHandler;
import org.xsocket.stream.IDisconnectHandler;
import org.xsocket.stream.INonBlockingConnection;
import org.xsocket.stream.NonBlockingConnection;
import com.peterhi.StatusCode;
import com.peterhi.ThreadManager;
import com.peterhi.client.Application;
import com.peterhi.client.impl.managers.StoreManager;
import com.peterhi.client.nio.events.ConnectionEvent;
import com.peterhi.io.IO;
import com.peterhi.net.LWObject;
import com.peterhi.net.Protocol;
import com.peterhi.util.Str;

public class NioSession {

    private NetworkManager owner;

    private INonBlockingConnection conn;

    private boolean connecting = false;

    private StatusCode lastDCStatus;

    private Collection<IDisconnectHandler> dcHandlers = new FastSet<IDisconnectHandler>();

    private ScheduledFuture<?> future;

    public NioSession(NetworkManager owner) {
        this.owner = owner;
    }

    public StatusCode getLastDCStatus() {
        return lastDCStatus;
    }

    public void setLastDCStatus(StatusCode lastDCstatus) {
        this.lastDCStatus = lastDCstatus;
    }

    public boolean addIDisconnectHandler(IDisconnectHandler handler) {
        return dcHandlers.add(handler);
    }

    public boolean removeIDisconnectHandler(IDisconnectHandler handler) {
        return dcHandlers.remove(handler);
    }

    public void removeAllIDisconnectHandlers() {
        dcHandlers.clear();
    }

    public synchronized void connect(String host, int port) {
        try {
            connecting = true;
            setLastDCStatus(StatusCode.Unknown);
            startInterruptTask();
            HandlerChain chain = new HandlerChain();
            chain.addLast(new IDataHandler() {

                public boolean onData(INonBlockingConnection conn) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
                    return this_onData(conn);
                }
            });
            chain.addLast(new IConnectHandler() {

                public boolean onConnect(INonBlockingConnection conn) throws IOException {
                    return this_onConnect(conn);
                }
            });
            chain.addLast(new IDisconnectHandler() {

                public boolean onDisconnect(INonBlockingConnection conn) throws IOException {
                    return this_onDisconnect(conn);
                }
            });
            conn = new NonBlockingConnection(host, port, chain);
        } catch (IOException ex) {
            ex.printStackTrace();
            setLastDCStatus(StatusCode.ConnFail);
            owner.fireOnDisconnected(new ConnectionEvent(this));
        }
    }

    private void startInterruptTask() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
        future = ThreadManager.getThreadManager().schedule(new Callable<Object>() {

            public Object call() throws Exception {
                if (!connecting) {
                    return null;
                }
                if (getStore().getID() == null) {
                    disconnect(StatusCode.Timeout);
                }
                return null;
            }
        }, Protocol.LOGIN_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void disconnect(StatusCode reason) {
        setLastDCStatus(reason);
        if (conn != null) {
            if (conn.isOpen()) {
                try {
                    conn.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            conn = null;
        }
    }

    public boolean this_onConnect(INonBlockingConnection conn) throws IOException {
        owner.fireOnConnected(new ConnectionEvent(this));
        return true;
    }

    public boolean this_onData(INonBlockingConnection conn) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
        try {
            int len = conn.getNumberOfAvailableBytes();
            byte[] data = conn.readBytesByLength(len);
            NioHandler handler = verifyPacket(conn, data);
            handler.handle(owner, this, data);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            disconnect(StatusCode.NetInterrupt);
            throw ex;
        } catch (Exception ex) {
            ex.printStackTrace();
            return true;
        }
    }

    private NioHandler verifyPacket(INonBlockingConnection conn, byte[] data) {
        byte version = data[4];
        if (version != Protocol.VERSION) {
            disconnect(StatusCode.IncompatibleVersion);
            return null;
        }
        int id = IO.getInt(data, 0);
        NioHandler handler = owner.getNioHandler(id);
        return handler;
    }

    public boolean this_onDisconnect(INonBlockingConnection conn) throws IOException {
        cleanUpConnection();
        Iterator<IDisconnectHandler> dcItor = dcHandlers.iterator();
        while (dcItor.hasNext()) {
            IDisconnectHandler l = dcItor.next();
            l.onDisconnect(conn);
        }
        owner.fireOnDisconnected(new ConnectionEvent(this));
        return true;
    }

    public void cleanUpConnection() {
        connecting = false;
        PasswordAuthentication auth = getStore().getAuth();
        if (auth != null) Str.clear(auth.getPassword());
        getStore().setID(null);
        getStore().setAuth(null);
        getStore().setChannel(null);
        cancelInterrupter();
    }

    public void cancelInterrupter() {
        if (future != null) {
            future.cancel(true);
            future = null;
        }
    }

    void send(LWObject o) throws IOException {
        int i = o.getID();
        final byte[] data = o.serialize(6);
        IO.putInt(data, 0, i);
        data[4] = Protocol.VERSION;
        conn.write(data, 0, data.length);
    }

    private StoreManager getStore() {
        return Application.getApplication().getManager(StoreManager.class);
    }
}
