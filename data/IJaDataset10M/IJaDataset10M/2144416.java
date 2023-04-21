package it.xargon.bxmp;

import java.io.*;
import java.util.*;
import it.xargon.events.*;
import it.xargon.smartnet.*;

class BXmpServerImpl extends EventsSource<BXmpServer.Events> implements BXmpServer {

    private HashMap<TcpConnection, BXmpConnection> xmpconns = null;

    private int maxJobs = 0;

    private BXmpFactory fact = null;

    private TcpServer tcpserv = null;

    private TcpServer.Events tcpeventsink = new TcpServer.Events() {

        public void started(TcpServer serv) {
            e_started(serv);
        }

        public void build(TcpServer serv, TcpConnection conn) {
            e_build(conn);
        }

        public void removed(TcpServer serv, TcpConnection conn) {
            e_removed(conn);
        }

        public void stopped(TcpServer serv) {
            e_stopped(serv);
        }

        public void serverSocketException(TcpServer serv, Exception ex) {
            raiseEvent.exception(new BXmpException(ex), BXmpServerImpl.this);
        }
    };

    public BXmpServerImpl(BXmpFactory factory, TcpServer serv, int maxJobsPerConnection) {
        super(factory.getThreadPool());
        fact = factory;
        maxJobs = maxJobsPerConnection;
        xmpconns = new HashMap<TcpConnection, BXmpConnection>();
        tcpserv = serv;
        tcpserv.register(tcpeventsink);
    }

    private void e_started(TcpServer serv) {
        Thread.currentThread().setName("XmpServer - " + tcpserv.toString());
        raiseEvent.started(this);
    }

    private void e_build(TcpConnection conn) {
        BXmpConnection xmpconn = new BXmpConnectionImpl(fact, conn, maxJobs);
        synchronized (xmpconns) {
            xmpconns.put(conn, xmpconn);
        }
        raiseEvent.accepted(xmpconn);
    }

    private void e_removed(TcpConnection conn) {
        BXmpConnection xmpconn = null;
        synchronized (xmpconns) {
            xmpconn = xmpconns.remove(conn);
        }
        raiseEvent.removed(xmpconn);
    }

    private void e_stopped(TcpServer serv) {
        raiseEvent.stopped(this);
    }

    public Collection<BXmpConnection> getConnections() {
        synchronized (xmpconns) {
            return Collections.unmodifiableCollection(new HashSet<BXmpConnection>(xmpconns.values()));
        }
    }

    public String toString() {
        return "LXmpServerImpl:" + tcpserv.toString();
    }

    public boolean isRunning() {
        return tcpserv.isRunning();
    }

    public void start() throws IOException {
        tcpserv.start();
    }

    public Collection<BXmpConnection> stop() throws IOException {
        Collection<BXmpConnection> result = getConnections();
        tcpserv.stop();
        return result;
    }

    public void shutdown() throws IOException {
        tcpserv.shutdown();
    }

    public BXmpFactory getFactory() {
        return fact;
    }

    public TcpServer getTcpServer() {
        return tcpserv;
    }
}
