package net.sf.dz.daemon.tcp.server;

import net.sf.dz.pnp.MulticastServer;
import net.sf.dz.pnp.custom.SimpleBroadcastServer;
import net.sf.dz.util.CollectionSynchronizer;
import net.sf.dz.util.HostHelper;
import net.sf.dz.util.SSLContextFactory;
import net.sf.jukebox.conf.Configuration;
import net.sf.jukebox.jmx.JmxAttribute;
import net.sf.jukebox.sem.MutexSemaphore;
import net.sf.jukebox.sem.SemaphoreGroup;
import net.sf.jukebox.service.ActiveService;
import net.sf.jukebox.service.PassiveService;
import net.sf.jukebox.service.ServiceUnavailableException;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLServerSocket;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * The TCP connection listener.
 * <p/>
 * Provides:
 * <ul>
 * <li> Configuration;
 * <li> Connection setup, including SSL;
 * <li> Client handling logic;
 * <li> Service announcements.
 * </ul>
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2001-2007
 * @version $Id: AbstractListener.java,v 1.16 2007-03-26 09:26:09 vtt Exp $
 */
public abstract class AbstractListener extends PassiveService {

    /**
   * Set of addresses to listen on. Empty set means that we're listening on
   * all local addresses.
   */
    private Set<String> addressSet;

    /**
   * The port to listen to.
   */
    private int port;

    /**
   * The port to broadcast on.
   */
    private int broadcastPort;

    /**
   * Is the connection requested to be secure.
   */
    private boolean secure;

    /**
   * Password for the key store. VT: Don't tell me it's insecure, just fix the
   * implementation to be more secure, all right?
   */
    private String password;

    /**
   * The listener services.
   */
    private Set<Listener> listenerSet = new TreeSet<Listener>();

    /**
   * Set of active clients.
   */
    private Set<ConnectionHandler> clientSet = new HashSet<ConnectionHandler>();

    /**
   * Exclusive lock to avoid race conditions between {@link #cleanup
   * cleanup()} and new connections.
   */
    private MutexSemaphore cleanupLock = new MutexSemaphore();

    /**
   * The multicast server.
   */
    private MulticastServer multicastServer;

    @JmxAttribute(description = "Listening port")
    public int getListenPort() {
        return port;
    }

    @JmxAttribute(description = "Broadcasting port")
    public int getBroadcastPort() {
        return broadcastPort;
    }

    /**
   * @return Iterator on connection handlers.
   */
    public final Iterator<ConnectionHandler> iterator() {
        return clientSet.iterator();
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected final void configure() {
        Configuration cf = getConfiguration();
        String cfroot = getConfigurationRoot();
        addressSet = new TreeSet<String>(cf.getList(cfroot + ".tcp.bind_address"));
        port = cf.getInteger(cfroot + ".tcp.port", getDefaultListenPort());
        broadcastPort = cf.getInteger(cfroot + ".tcp.broadcast_port", getDefaultBroadcastPort());
        secure = cf.getBoolean(cfroot + ".tcp.secure", false);
        if (secure) {
            password = cf.getString(cfroot + ".tcp.password");
        }
        logger.info("Binding to: " + addressSet);
        configure2();
    }

    /**
   * Configure the subclass.
   */
    protected abstract void configure2();

    /**
   * {@inheritDoc}
   */
    @Override
    protected final void startup() throws Throwable {
        getConfiguration();
        startup2();
        if (addressSet.isEmpty()) {
            logger.info("Listening on all local addresses");
            Listener l = new Listener(null, port);
            if (l.start().waitFor()) {
                synchronized (listenerSet) {
                    listenerSet.add(l);
                }
            } else {
                logger.warn("Failed to start listener on *");
            }
        } else {
            Set<InetAddress> validAddresses = HostHelper.getLocalAddresses();
            for (Iterator<String> i = addressSet.iterator(); i.hasNext(); ) {
                String address = i.next();
                InetAddress configuredAddress = InetAddress.getByName(address);
                if (!validAddresses.contains(configuredAddress)) {
                    logger.warn("Address specified in the configuration is not locally present: " + address);
                    i.remove();
                    continue;
                }
                Listener l = new Listener(InetAddress.getByName(address), port);
                if (l.start().waitFor()) {
                    synchronized (listenerSet) {
                        listenerSet.add(l);
                    }
                } else {
                    logger.warn("Failed to start listener on '" + address + "'");
                }
            }
        }
        if (listenerSet.isEmpty()) {
            throw new IllegalStateException("No listeners could be started");
        }
        multicastServer = new SimpleBroadcastServer(new HashSet<String>(addressSet), broadcastPort);
        multicastServer.announce(getAnnounce());
        multicastServer.start();
    }

    /**
   * Do a service specific startup.
   *
   * @throws Throwable if anything goes wrong.
   */
    protected abstract void startup2() throws Throwable;

    /**
   * Provide a service signature. This signature must uniquely identify the
   * module for the purpose of broadcast announcement.
   *
   * @return The service signature.
   */
    @JmxAttribute(description = "Service signature")
    public abstract String getServiceSignature();

    /**
   * Provide a reasonable default for the {@link #port port} to listen to.
   *
   * @return Default port to listen on.
   */
    @JmxAttribute(description = "Default listening port")
    public abstract int getDefaultListenPort();

    /**
   * Provide a reasonable default for the {@link #port port} to broadcast on.
   *
   * @return Default port to broadcast on.
   */
    @JmxAttribute(description = "Default broadcasting port")
    public abstract int getDefaultBroadcastPort();

    /**
   * Syntax sugar to change an announce message.
   *
   * @param message Message to announce.
   */
    protected final void announce(String message) {
        if (multicastServer != null && multicastServer.isReady()) {
            multicastServer.announce(message);
        }
    }

    /**
   * Get the message to announce to our clients.
   *
   * @return The announce message.
   */
    protected synchronized String getAnnounce() {
        return "/" + port + "/" + (secure ? "secure" : "insecure");
    }

    /**
   * {@inheritDoc}
   */
    @Override
    protected void shutdown() throws Throwable {
        logger.info("Shutting down");
        multicastServer.stop();
        SemaphoreGroup stop = new SemaphoreGroup();
        for (Iterator<Listener> i = new CollectionSynchronizer<Listener>().copy(listenerSet).iterator(); i.hasNext(); ) {
            Listener l = i.next();
            stop.add(l.stop());
            i.remove();
        }
        logger.info("Shut down listeners");
        for (Iterator<ConnectionHandler> i = new CollectionSynchronizer<ConnectionHandler>().copy(clientSet).iterator(); i.hasNext(); ) {
            ConnectionHandler ch = i.next();
            ch.send("E Shutting down");
            stop.add(ch.stop());
        }
        stop.waitForAll();
        logger.info("Shut down clients");
        cleanup();
        shutdown2();
    }

    /**
   * Shut down the subclass.
   *
   * @throws Throwable if anything goes wrong.
   */
    protected abstract void shutdown2() throws Throwable;

    /**
   * Create a connection handler.
   *
   * @param socket Socket to use.
   * @param br     Reader to use.
   * @param pw     Writer to use.
   *
   * @return The connection handler.
   */
    protected abstract ConnectionHandler createHandler(Socket socket, BufferedReader br, PrintWriter pw);

    /**
   * Find out whether more than one connection is allowed.
   *
   * @return false if multiple connections are allowed.
   */
    protected abstract boolean isUnique();

    /**
   * Clean up after the last active connection is gone. This method must be
   * idempotent.
   *
   * @throws Throwable if anything goes wrong.
   */
    protected abstract void cleanup() throws Throwable;

    protected Class getOuterClass() {
        return getClass();
    }

    /**
   * Connection listener.
   */
    protected final class Listener extends ActiveService implements Comparable, ListenerMBean {

        /**
     * Address to listen on.
     */
        final InetAddress addr;

        /**
     * Port to listen on.
     */
        final int port;

        /**
     * Server socket to listen with.
     */
        ServerSocket ss;

        /**
     * Create an instance.
     *
     * @param addr Local address to listen on.
     * @param port Local port to listen on.
     */
        Listener(InetAddress addr, int port) {
            this.addr = addr;
            this.port = port;
        }

        @JmxAttribute(description = "Host pattern to listen to")
        public String getHost() {
            return (addr == null ? "*" : addr.toString()) + ":" + port;
        }

        @JmxAttribute(description = "true if secure connection is requested by configuration")
        public boolean isSecureRequested() {
            return secure;
        }

        @JmxAttribute(description = "true if connected in secure mode")
        public boolean isSecure() {
            return ss instanceof SSLServerSocket;
        }

        /**
     * {@inheritDoc}
     */
        @Override
        protected void startup() throws Throwable {
            try {
                if (secure) {
                    logger.info("Secure connection requested");
                    try {
                        ss = SSLContextFactory.createContext(password).getServerSocketFactory().createServerSocket(port, 256, addr);
                    } catch (SSLException sslex) {
                        logger.warn("Can't establish a secure listener on " + addr + ":" + port, sslex);
                        logger.warn("Reverting to insecure connection");
                    }
                }
                if (ss == null) {
                    ss = new ServerSocket(port, 256, addr);
                }
                logger.info("Listening on " + addr + ":" + port);
            } catch (Throwable t) {
                throw new ServiceUnavailableException("Can't listen on " + addr + ":" + port, t);
            }
        }

        /**
     * Keep accepting the TCP clients.
     *
     * @throws Throwable if anything goes wrong.
     */
        @Override
        protected final void execute() throws Throwable {
            while (isEnabled()) {
                Socket s = ss.accept();
                logger.info("Client arrived from " + s.getInetAddress() + ":" + s.getPort());
                if (!isEnabled()) {
                    logger.warn("Shutting down - dropped " + s.getInetAddress() + ":" + s.getPort());
                    s.close();
                    return;
                }
                try {
                    cleanupLock.waitFor();
                    BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    PrintWriter pw = new PrintWriter(s.getOutputStream());
                    ConnectionHandler ch = createHandler(s, br, pw);
                    if (isUnique() && !clientSet.isEmpty()) {
                        logger.info("Unique - dropping existing connection");
                        try {
                            synchronized (clientSet) {
                                clientSet.add(ch);
                            }
                            drop(ch, s);
                        } finally {
                            synchronized (clientSet) {
                                clientSet.remove(ch);
                            }
                        }
                    }
                    if (!ch.start().waitFor()) {
                        logger.info("Client coming from " + s.getInetAddress() + ":" + s.getPort() + " failed to complete handshake - dropped");
                        if (clientSet.isEmpty()) {
                            logger.info("Oops, performing cleanup()");
                            cleanup();
                        }
                        return;
                    }
                    logger.info("" + s.getInetAddress() + ":" + s.getPort() + ": started");
                } finally {
                    cleanupLock.release();
                }
            }
        }

        /**
     * Disconnect existing connection[s] and notify them about the newcomer.
     *
     * @param ch    Connection handler *not* to stop.
     * @param other Socket created for the other connection - for logging
     *              purposes.
     *
     * @throws InterruptedException if the wait is interrupted.
     */
        private void drop(ConnectionHandler ch, Socket other) throws InterruptedException {
            SemaphoreGroup stopped = new SemaphoreGroup();
            synchronized (clientSet) {
                for (Iterator<ConnectionHandler> i = clientSet.iterator(); i.hasNext(); ) {
                    ConnectionHandler oldHandler = i.next();
                    if (oldHandler == ch) {
                        continue;
                    }
                    oldHandler.send("E Disconnected: another client came from " + other.getInetAddress() + ":" + other.getPort());
                    stopped.add(oldHandler.stop());
                }
            }
            stopped.waitForAll();
            logger.info("Dropped existing connections");
        }

        /**
     * {@inheritDoc}
     */
        @Override
        protected void shutdown() throws Throwable {
        }

        /**
     * {@inheritDoc}
     */
        public int compareTo(Object other) {
            if (!other.getClass().equals(getClass())) {
                throw new ClassCastException("Expecting " + getClass().getName() + ", got " + other.getClass().getName());
            }
            Listener l = (Listener) other;
            return (addr + ":" + port).compareTo(l.addr + ":" + l.port);
        }
    }

    /**
   * The connection handler.
   */
    protected abstract class ConnectionHandler extends PassiveService {

        /**
     * Socket to connect through.
     */
        protected Socket socket;

        /**
     * Reader to use.
     */
        protected BufferedReader br;

        /**
     * Writer to use.
     */
        protected PrintWriter pw;

        /**
     * Parser thread to use.
     */
        protected Thread parser;

        /**
     * Create an instance.
     *
     * @param socket Socket to use.
     * @param br     Reader to use.
     * @param pw     Writer to use.
     */
        public ConnectionHandler(Socket socket, BufferedReader br, PrintWriter pw) {
            this.socket = socket;
            this.br = br;
            this.pw = pw;
        }

        /**
     * Tell the listener what devices we have.
     */
        public abstract void iHave();

        /**
     * Send a message to the client.
     *
     * @param message Message to send.
     */
        public synchronized void send(String message) {
            if (!isEnabled()) {
                throw new IllegalStateException("Not enabled now, stopped?");
            }
            pw.println(message);
            pw.flush();
        }

        /**
     * {@inheritDoc}
     */
        @Override
        protected void startup() throws Throwable {
            sayHello();
            parser = new Thread(createParser());
            parser.start();
            synchronized (clientSet) {
                clientSet.add(this);
            }
            logger.info("Active connections: " + clientSet.size());
        }

        /**
     * Execute the protocol handshake.
     */
        protected void sayHello() {
        }

        /**
     * Create a command parser.
     *
     * @return Command parser.
     */
        protected abstract CommandParser createParser();

        /**
     * {@inheritDoc}
     */
        @Override
        protected void shutdown() throws Throwable {
            logger.info("Active connections: " + clientSet.size());
            logger.info("Shutting down...");
            try {
                parser.interrupt();
                synchronized (clientSet) {
                    clientSet.remove(this);
                }
                socket.close();
                br.close();
                pw.close();
                logger.info("Shut down");
            } catch (Throwable t) {
                logger.warn("Unexpected exception:", t);
            }
            logger.info("Active connections: " + clientSet.size());
            if (clientSet.isEmpty()) {
                logger.info("Last active connection is gone, cleaning up");
                {
                    try {
                        cleanupLock.waitFor();
                        cleanup();
                    } finally {
                        cleanupLock.release();
                    }
                }
            }
        }

        /**
     * Command parser.
     */
        protected abstract class CommandParser implements Runnable {

            /**
       * Keep reading the data from {@link ConnectionHandler#br the reader} and
       * {@link #parse(String) parsing} it.
       */
            public void run() {
                while (true) {
                    String line = null;
                    try {
                        line = br.readLine();
                        if (!isEnabled()) {
                            logger.info("Interrupted, input ignored: '" + line + "'");
                            return;
                        }
                        if (line == null) {
                            logger.info("Lost the client");
                            stop();
                            return;
                        }
                        synchronized (this) {
                            parse(line);
                        }
                    } catch (InterruptedException ignored) {
                        logger.info("Interrupted");
                        if (isEnabled()) {
                            stop();
                        }
                        return;
                    } catch (SocketException sex) {
                        logger.info("Socket error: " + sex.getMessage());
                        if (isEnabled()) {
                            stop();
                        }
                        return;
                    } catch (SSLException sslex) {
                        logger.info("SSL problem: " + sslex.getMessage());
                        if (isEnabled()) {
                            stop();
                        }
                        return;
                    } catch (Throwable t) {
                        if (t instanceof IOException && "Stream closed".equals(t.getMessage())) {
                            return;
                        }
                        logger.warn("Huh? Command received: '" + line + "'", t);
                        send("E " + ((t.getMessage() == null) ? "Bad command" : t.getMessage()) + ": " + line);
                    }
                }
            }

            /**
       * Parse the received command.
       *
       * @param command Command to parse.
       *
       * @throws Throwable if anything goes wrong.
       */
            public final void parse(String command) throws Throwable {
                if ("q".equalsIgnoreCase(command)) {
                    logger.info("Client disconnected");
                    stop();
                } else if ("".equalsIgnoreCase(command)) {
                } else if ("heartbeat".equals(command)) {
                    send("OK");
                    return;
                } else {
                    parse2(command);
                }
            }

            /**
       * Parse the command further.
       *
       * @param command Command to parse.
       *
       * @throws Throwable if anything goes wrong.
       */
            protected abstract void parse2(String command) throws Throwable;
        }
    }
}
