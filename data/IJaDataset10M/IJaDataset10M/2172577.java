package com.coldcore.coloradoftp.connection.impl;

import com.coldcore.coloradoftp.command.Reply;
import com.coldcore.coloradoftp.connection.ConnectionPool;
import com.coldcore.coloradoftp.connection.ControlConnection;
import com.coldcore.coloradoftp.connection.DataConnection;
import com.coldcore.coloradoftp.connection.DataConnectionInitiator;
import com.coldcore.coloradoftp.factory.ObjectFactory;
import com.coldcore.coloradoftp.factory.ObjectName;
import com.coldcore.coloradoftp.session.Session;
import com.coldcore.coloradoftp.session.SessionAttributeName;
import org.apache.log4j.Logger;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @see com.coldcore.coloradoftp.connection.DataConnectionInitiator
 */
public class GenericDataConnectionInitiator implements DataConnectionInitiator, Runnable {

    private static Logger log = Logger.getLogger(GenericDataConnectionInitiator.class);

    protected String ip;

    protected int port;

    protected boolean active;

    protected ControlConnection controlConnection;

    protected ConnectionPool dataConnectionPool;

    protected SocketChannel sc;

    protected Reply errorReply;

    protected Thread thr;

    protected long sleep;

    protected boolean aborted;

    public GenericDataConnectionInitiator() {
        sleep = 100L;
    }

    protected Reply getErrorReply() {
        if (errorReply == null) {
            errorReply = (Reply) ObjectFactory.getObject(ObjectName.REPLY);
            errorReply.setCode("425");
            errorReply.setText("Can't open data connection.");
        }
        return errorReply;
    }

    /** Get thread sleep time
   * @return Time in mills
   */
    public long getSleep() {
        return sleep;
    }

    /** Set thread sleep time
   * @param sleep Time in mills
   */
    public void setSleep(long sleep) {
        this.sleep = sleep;
    }

    /** Test if user got a "150" reply
   * @return TRUE if user got the reply, FALSE if not yet
   */
    protected boolean isReply150() {
        Session session = controlConnection.getSession();
        Long bytesWrote = (Long) session.getAttribute(SessionAttributeName.BYTE_MARKER_150_REPLY);
        if (bytesWrote == null || controlConnection.getBytesWrote() == bytesWrote || controlConnection.getOutgoingBufferSize() != 0) return false;
        log.debug("User got a 150 reply");
        return true;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void run() {
        while (active) {
            DataConnection dataConnection = null;
            try {
                if (!isReply150()) {
                    Thread.sleep(sleep);
                    continue;
                }
                dataConnectionPool = (ConnectionPool) ObjectFactory.getObject(ObjectName.DATA_CONNECTION_POOL);
                sc = SocketChannel.open();
                sc.connect(new InetSocketAddress(ip, port));
                if (!sc.finishConnect()) throw new RuntimeException("Failed finishConnect");
                String ip = sc.socket().getInetAddress().getHostAddress();
                log.debug("New data connection established (IP " + ip + ")");
                dataConnection = (DataConnection) ObjectFactory.getObject(ObjectName.DATA_CONNECTION);
                dataConnection.initialize(sc);
                DataConnection existing = controlConnection.getDataConnection();
                if (existing != null && !existing.isDestroyed()) {
                    log.warn("BUG: Replacing existing data connection with a new one!");
                    existing.destroyNoReply();
                }
                controlConnection.setDataConnection(dataConnection);
                dataConnection.setControlConnection(controlConnection);
                configure(dataConnection);
                dataConnectionPool.add(dataConnection);
                log.debug("New data connection is ready");
                active = false;
            } catch (Throwable e) {
                if (!aborted) {
                    log.warn("Failed to establish a connection with " + ip + ":" + port + " (ignoring)", e);
                    try {
                        dataConnection.destroyNoReply();
                    } catch (Throwable ex) {
                    }
                    try {
                        sc.close();
                    } catch (Throwable ex) {
                        log.error("Cannot close the channel (ignoring)", e);
                    }
                    controlConnection.reply(getErrorReply());
                }
                active = false;
            }
        }
        log.debug("Data connection initiator thread finished");
    }

    public boolean isActive() {
        return active;
    }

    public synchronized void activate() {
        if (active) {
            log.warn("Data connection initiator was active when activate routine was called");
            return;
        }
        active = true;
        aborted = false;
        thr = new Thread(this);
        thr.start();
    }

    public synchronized void abort() {
        aborted = true;
        if (!active) return;
        try {
            if (sc != null && sc.isOpen()) sc.close();
        } catch (Throwable e) {
            log.error("Cannot close channel (ignoring)", e);
        }
        controlConnection.reply(getErrorReply());
        Session session = controlConnection.getSession();
        session.removeAttribute(SessionAttributeName.BYTE_MARKER_150_REPLY);
        active = false;
    }

    public ControlConnection getControlConnection() {
        return controlConnection;
    }

    public void setControlConnection(ControlConnection controlConnection) {
        this.controlConnection = controlConnection;
    }

    /** Configure connection before adding it to a pool
   * @param connection Connection
   */
    public void configure(DataConnection connection) {
    }
}
