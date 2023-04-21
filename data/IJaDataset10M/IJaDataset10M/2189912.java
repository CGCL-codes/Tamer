package org.jcyclone.ext.asocket.nio;

import org.jcyclone.ext.asocket.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Internal class used to represent a server socket listening on a
 * given port.
 */
public class NIOListenSockState extends ListenSockState {

    private static final boolean DEBUG = false;

    ServerSocketChannel nio_servsock;

    private SelectionKey selkey;

    private NIOSelectSource listen_nio_selsource;

    public NIOListenSockState(ATcpListenRequest req, SelectSourceIF listen_nio_selsource) throws IOException {
        this(req);
        this.listen_nio_selsource = (NIOSelectSource) listen_nio_selsource;
        this.listen_nio_selsource.setName("ListenSelectSource");
        selkey = (SelectionKey) listen_nio_selsource.register(nio_servsock, SelectionKey.OP_ACCEPT);
        selkey.attach(this);
    }

    protected NIOListenSockState(ATcpListenRequest req) throws IOException {
        this.port = req.port;
        this.compQ = req.compQ;
        this.writeClogThreshold = req.writeClogThreshold;
        if (DEBUG) System.err.println("ListenThread: Creating nio_servsock on port " + port);
        this.servsock = req.servsock;
        try {
            nio_servsock = ServerSocketChannel.open();
            nio_servsock.configureBlocking(false);
            nio_servsock.socket().bind(new InetSocketAddress(port));
        } catch (IOException ioe) {
            ATcpListenFailedEvent ev = new ATcpListenFailedEvent(servsock, ioe.getMessage());
            compQ.enqueueLossy(ev);
            return;
        }
        this.servsock.lss = this;
        compQ.enqueueLossy(new ATcpListenSuccessEvent(servsock));
    }

    protected Socket accept() throws IOException {
        if (nio_servsock == null) return null;
        Socket sock;
        try {
            if (DEBUG) System.err.println("LSS: Calling nio_servsock.accept");
            SocketChannel sc = nio_servsock.accept();
            if (sc == null) return null;
            sock = sc.socket();
            if (DEBUG) System.err.println("LSS: nio_servsock.accept returned " + sock);
            sock.getChannel().configureBlocking(false);
        } catch (SocketException e) {
            if (DEBUG) System.err.println("LSS: nio_servsock.accept got SocketException " + e);
            return null;
        } catch (IOException e) {
            System.err.println("LSS: accept got IOException: " + e);
            e.printStackTrace();
            ATcpServerSocketClosedEvent dead = new ATcpServerSocketClosedEvent(servsock);
            compQ.enqueueLossy(dead);
            listen_nio_selsource.deregister(selkey);
            throw e;
        }
        return sock;
    }

    protected void suspend() {
        if (nio_servsock == null) return;
        System.err.println("LSS: Suspending accept on " + servsock);
        selkey.interestOps(selkey.interestOps() & ~(SelectionKey.OP_ACCEPT));
        listen_nio_selsource.update(selkey);
    }

    protected void resume() {
        if (nio_servsock == null) return;
        System.err.println("LSS: Resuming accept on " + servsock);
        selkey.interestOps(selkey.interestOps() | SelectionKey.OP_ACCEPT);
        listen_nio_selsource.update(selkey);
    }

    protected void close() {
        if (nio_servsock == null) return;
        listen_nio_selsource.deregister(selkey);
        listen_nio_selsource = null;
        try {
            nio_servsock.close();
        } catch (IOException e) {
        }
        nio_servsock = null;
        ATcpServerSocketClosedEvent closed = new ATcpServerSocketClosedEvent(servsock);
        compQ.enqueueLossy(closed);
    }

    protected void complete(ATcpConnection conn) {
        if (DEBUG) System.err.println("LSS: complete called on conn " + conn);
        if (!compQ.enqueueLossy(conn)) {
            if (DEBUG) System.err.println("LSS: Could not enqueue_lossy new conn " + conn);
        }
    }

    public int getLocalPort() {
        return nio_servsock.socket().getLocalPort();
    }

    public Object getSocket() {
        if (nio_servsock != null) return nio_servsock.socket(); else return null;
    }
}
