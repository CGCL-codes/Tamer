package org.simpleframework.http.connect;

import static java.nio.channels.SelectionKey.OP_ACCEPT;
import java.net.SocketAddress;
import javax.net.ssl.SSLContext;
import org.simpleframework.http.Server;
import org.simpleframework.util.select.DirectReactor;
import org.simpleframework.util.select.Reactor;

/**
 * The <code>Notifier</code> object is represents the interface to
 * the server that the clients can connect to. This is responsible
 * for making call backs to the <code>Acceptor</code> when there
 * is a new connection waiting to be accepted. When the connection
 * is to be closed the interface object can be closed.
 * 
 * @author Niall Gallagher 
 */
class Notifier {

    /**
    * This is the acceptor that is used to accept the connections.
    */
    private Acceptor acceptor;

    /**
    * This is the reactor used to notify the acceptor of sockets.
    */
    private Reactor reactor;

    /**
    * Constructor for the <code>Notifier</code> object. This needs
    * a socket address and a processor to hand created pipelines
    * to. This creates a <code>Reactor</code> which will notify the
    * acceptor when there is a new connection waiting to be accepted.
    * 
    * @param address this is the address to listen for new sockets
    * @param processor this is where pipelines are handed to
    */
    public Notifier(SocketAddress address, Server processor) throws Exception {
        this(address, null, processor);
    }

    /**
    * Constructor for the <code>Notifier</code> object. This needs
    * a socket address and a processor to hand created pipelines
    * to. This creates a <code>Reactor</code> which will notify the
    * acceptor when there is a new connection waiting to be accepted.
    * 
    * @param address this is the address to listen for new sockets
    * @param engine this is the SSL engine used for secure HTTPS
    * @param processor this is where pipelines are handed to
    */
    public Notifier(SocketAddress address, SSLContext context, Server processor) throws Exception {
        this.acceptor = new Acceptor(address, context, processor);
        this.reactor = new DirectReactor();
        this.process();
    }

    /**
    * This is used to register the <code>Acceptor</code> to listen
    * for new connections that are ready to be accepted. Once this
    * is registered it will remain registered until the interface
    * is closed, at which point the socket is closed.
    * 
    * @throws Exception thrown if there is a problem registering
    */
    private void process() throws Exception {
        reactor.process(acceptor, OP_ACCEPT);
    }

    /**
    * This is used to close the connection and the server socket
    * used to accept connections. This will perform a close of the
    * connected server socket that have been created from using
    * the <code>Acceptor</code> object. 
    * 
    * @throws Exception thrown if there is a problem closing
    */
    public void close() throws Exception {
        acceptor.close();
        reactor.stop();
    }
}
