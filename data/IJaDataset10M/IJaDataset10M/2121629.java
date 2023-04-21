package com.bitgate.util.service;

import static com.bitgate.util.debug.Debug.critical;
import static com.bitgate.util.debug.Debug.debug;
import static com.bitgate.util.debug.Debug.getStackTrace;
import static com.bitgate.util.debug.Debug.info;
import static com.bitgate.util.debug.Debug.isDebugEnabled;
import static com.bitgate.util.debug.Debug.warning;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.Pipe;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManagerFactory;
import com.bitgate.util.config.Config;
import com.bitgate.util.dynaload.Dynaload;
import com.bitgate.util.jmx.JmxServer;
import com.bitgate.util.service.client.ClientContext;
import com.bitgate.util.service.client.ClientContextCache;
import com.bitgate.util.service.client.ClientContextFactory;
import com.bitgate.util.service.protocol.ProtocolInterface;
import com.bitgate.util.socket.ClientChannel;
import com.bitgate.util.socket.ClientChannelCache;
import com.bitgate.util.socket.InsecureChannel;
import com.bitgate.util.socket.SecureChannel;
import com.bitgate.util.threads.DynamicThread;
import com.bitgate.util.threads.DynamicThreadGroup;

/**
 * This creates a <code>Service</code> that can be used to handle connections for clients using a specified protocol.
 * It is the heart of any server that you create to handle connections from clients for a specific purpose.  This
 * class uses a combination of service protocol handlers, and tunable specifics to make it behave exactly as you want:
 * high performance, or high throughput.  It loads its handlers using the <code>Dynaload</code> mechanism, so protocol
 * handlers can be loaded and unloaded at will, without having to take the system down to upgrade it.  It also makes
 * use of the <code>Cache</code> mechanisms to store information about clients and creates <code>Statistics</code>
 * updates for connection rates and the like.
 * <p/>
 * Services use the &quot;conf/defaults/protocols.xml&quot; configuration file to cross-reference protocol setup
 * information.  This can be overridden by setting a property called &quot;protocols.config&quot;.
 *
 * @author Kenji Hollis &lt;kenji@bitgatesoftware.com&gt;
 * @version $Id: //depot/bitgate/bsse/src/bitgate/util/service/Service.java#1 $
 */
public class Service implements Runnable {

    private final Selector selector;

    private final ServerSocketChannel ssChannel;

    private final CharsetDecoder decoder;

    private final ServiceContext sc;

    private final DynamicThreadGroup dtGroup;

    private static final AtomicReference<String> systemEncoding = new AtomicReference<String>();

    private ProtocolInterface pInterface;

    private Dynaload dClassloader;

    private ServiceHosts sHosts;

    private Pipe connectionPipe;

    private volatile boolean isRunning;

    private static final int SERVER_BACKLOG = 32;

    /** Property name used to attach a <code>ClientContext</code> to a <code>ClientChannel</code>. */
    public static final String CONTEXT_PROPERTY = "Service.ClientContext";

    /** Property name used to attach a <code>DynamicThread</code> to a <code>ClientChannel</code>. */
    public static final String DYNAMIC_THREAD_PROPERTY = "Service.DynamicThread";

    /** This is the configuration root registration node name. */
    public static final String CONFIG_ROOT = "services";

    private static final String PROTOCOLS_CONFIG_ROOT = "protocols";

    private static final String PROTOCOLS_CONFIG_PROPERTY = "protocols.config";

    private static final String PROTOCOLS_CONFIG_FILENAME = "conf/defaults/protocols.xml";

    /** This is the system default encoding, if not overridden in the "tuning" area of the services. */
    public static final String DEFAULT_SYSTEM_ENCODING = "UTF8";

    /**
     * Instantiates a new service object based on settings in the <code>ServiceContext</code> object.
     *
     * @param sc The <code>ServiceContext</code> object.
     * @throws ServiceException on any errors.
     */
    public Service(ServiceContext sc) throws ServiceException {
        if (!Config.getDefault().isRegistered(CONFIG_ROOT)) {
            critical("Services have not been loaded - cannot start a service.");
            throw new ServiceException("Unable to start service: services.xml file has not been registered.");
        }
        if (sc == null) {
            throw new ServiceException("Service called without a valid context.");
        }
        info("Locating protocol '" + sc.getProtocol() + "' in configuration.");
        String protocolsFilename = null;
        protocolsFilename = System.getProperty(PROTOCOLS_CONFIG_PROPERTY);
        if (protocolsFilename == null) {
            protocolsFilename = PROTOCOLS_CONFIG_FILENAME;
        }
        Config.getDefault().register(protocolsFilename, PROTOCOLS_CONFIG_ROOT);
        if (!Config.getDefault().isRegistered(PROTOCOLS_CONFIG_ROOT)) {
            if (isDebugEnabled()) {
                debug("Protocol entries have not been loaded - could not be loaded, or an error loaded reading the file.");
            }
            throw new ServiceException("Unable to start service: protocols.xml file has not been registered.");
        }
        this.sc = sc;
        this.isRunning = true;
        String sysEncoding = Config.getDefault().parseConfig(CONFIG_ROOT, "/services/object[@type='tuning']/property[@name='" + "service.default-charset']/@value");
        if (sysEncoding != null && !sysEncoding.equals("")) {
            systemEncoding.set(sysEncoding);
        } else {
            systemEncoding.set(DEFAULT_SYSTEM_ENCODING);
        }
        info("Using default system charset of '" + systemEncoding.get() + "'");
        this.decoder = Charset.forName(systemEncoding.get()).newDecoder();
        try {
            this.selector = Selector.open();
            this.ssChannel = ServerSocketChannel.open();
            this.ssChannel.configureBlocking(false);
        } catch (IOException e) {
            throw new ServiceException("Unable to initialize service '" + sc.getName() + "': " + e.getMessage());
        }
        setupServerSocket();
        setupServerPipe();
        setupServerProtocol();
        this.dtGroup = new DynamicThreadGroup(5, 10, 30, new Worker(pInterface, this));
    }

    /**
     * This function sets up the internal <code>ServerSocket</code> object.
     * 
     * @throws ServiceException on any errors during the socket setup.
     */
    private void setupServerSocket() throws ServiceException {
        try {
            this.ssChannel.socket().setReuseAddress(true);
        } catch (SocketException e) {
            if (isDebugEnabled()) {
                debug("Unable to set socket reuse: " + e.getMessage());
            }
        }
        try {
            if (this.sc.getIp() != null) {
                this.ssChannel.socket().bind(new InetSocketAddress(this.sc.getIp(), this.sc.getPort()), SERVER_BACKLOG);
            } else {
                this.ssChannel.socket().bind(new InetSocketAddress(this.sc.getPort()), SERVER_BACKLOG);
            }
        } catch (IOException e) {
            if (this.sc.getIp() != null) {
                throw new ServiceException("Unable to bind service to '" + this.sc.getIp() + ":" + this.sc.getPort() + "': " + e.getMessage());
            }
            throw new ServiceException("Unable to bind server to '[unbound]:" + this.sc.getPort() + "': " + e.getMessage());
        }
        try {
            this.ssChannel.register(this.selector, SelectionKey.OP_ACCEPT);
        } catch (ClosedChannelException e) {
            if (this.sc.getIp() != null) {
                throw new ServiceException("Unable to register service to '" + this.sc.getIp() + ":" + this.sc.getPort() + "': " + e.getMessage());
            }
            throw new ServiceException("Unable to register service to '[unbound]:" + this.sc.getPort() + "': " + e.getMessage());
        }
        if (this.sc.getIp() != null) {
            info("Starting service '" + this.sc.getIp() + ":" + this.sc.getPort() + "' protocol='" + this.sc.getProtocol() + "'");
        } else {
            info("Starting service '[unbound]:" + this.sc.getPort() + "' protocol='" + this.sc.getProtocol() + "'");
        }
    }

    /**
     * Sets up the server communication pipe.  This is relatively self-explanatory.  This pipe is used to communicate with the
     * server internally from the running threads, so it's basically treated as an IPC pipe.
     */
    private void setupServerPipe() {
        try {
            this.connectionPipe = Pipe.open();
            if (isDebugEnabled()) {
                debug("Created communication pipe for forced disconnections.");
            }
        } catch (IOException e) {
            this.connectionPipe = null;
            critical("Unable to create a connection communication pipe: " + e.getMessage());
        }
        try {
            SelectableChannel pipeChannel = this.connectionPipe.source();
            pipeChannel.configureBlocking(false);
            pipeChannel.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            warning("Unable to register pipe for reading: " + e.getMessage());
        }
    }

    /**
     * Sets up the main server protocol listings, and starts the server specified.  If the server could not be loaded, the server
     * does not contain the correct version, pattern, or other problem, the server will throw an exception.
     * 
     * @throws ServiceException on any errors.
     */
    private void setupServerProtocol() throws ServiceException {
        info("Locating protocol '" + sc.getProtocol() + "' in configuration.");
        String objectService = new String("/protocols/object[@type='protocol." + sc.getProtocol().toLowerCase() + "']");
        String classJar = Config.getDefault().parseConfig("protocols", objectService + "/property[@type='protocol.jarfile']/@value");
        String className = Config.getDefault().parseConfig("protocols", objectService + "/property[@type='protocol.classname']/@value");
        info("    + [" + sc.getProtocol() + "] Jarfile='" + classJar + "'");
        info("    + [" + sc.getProtocol() + "] Classname='" + className + "'");
        info("    + [" + sc.getProtocol() + "] Default Port='" + Config.getDefault().parseConfig(PROTOCOLS_CONFIG_ROOT, objectService + "/object[@type='protocol.defaults']/property[@type='protocol.port']/@value") + "'");
        info("    + [" + sc.getProtocol() + "] Default Secure='" + Config.getDefault().parseConfig(PROTOCOLS_CONFIG_ROOT, objectService + "/object[@type='protocol.defaults']/property[@type='protocol.secure']/@value") + "'");
        try {
            sHosts = new ServiceHosts(sc.getName());
        } catch (ServiceHostsException e) {
            info("    ! No service host allow/deny entries defined for this service.");
            this.sHosts = null;
        }
        try {
            dClassloader = new Dynaload(classJar);
        } catch (ClassNotFoundException e) {
            warning("Protocol '" + sc.getProtocol() + "' refers to jar file '" + classJar + "', but could not be found.");
            dClassloader = null;
        }
        if (dClassloader == null) {
            throw new ServiceException("Unable to load service handler for protocol type '" + this.sc.getProtocol() + "'");
        }
        Class<?> dClass = null;
        try {
            dClass = this.dClassloader.loadClass(className);
        } catch (ClassNotFoundException e) {
            warning("Protocol '" + this.sc.getProtocol() + "' jar file was found in '" + classJar + "' but could not be instantiated.");
        }
        try {
            if (dClass != null) {
                pInterface = (ProtocolInterface) dClass.newInstance();
            }
        } catch (InstantiationException e) {
            warning("Protocol '" + this.sc.getProtocol() + "' class name '" + className + "' could not be successfully instantiated.");
            pInterface = null;
        } catch (IllegalAccessException e) {
            warning("Protocol '" + this.sc.getProtocol() + "' class name '" + className + "' could not be successfully instantiated: " + e.getMessage());
            pInterface = null;
        }
        if (pInterface == null) {
            throw new ServiceException("Unable to initialize service '" + sc.getName() + "'");
        }
        pInterface.init(sc);
        pInterface.registerPipe(connectionPipe);
        if (JmxServer.getDefault().isInitialized()) {
            pInterface.initJmx(JmxServer.getDefault().getMBeanServer());
        }
    }

    /**
     * Stops the currently running service.  It does not shut down the currently running clients that are connected to the
     * service at the time it is closed.
     */
    public void stop() {
        isRunning = false;
        selector.wakeup();
    }

    /**
     * Retrieves data from the <code>Pipe</code>.
     * 
     * TODO: YUCK.  This code sucks.  I need to refactor this into another class or a controller sub-class
     * that is a possible inner class inside this one to handle the connection pipe commands.
     * 
     * XXX: Should this be done using an ObjectStreamReader with Serializable objects instead?  That seems
     * slower, but if it's internal and using a pipe for internal IPC, maybe it's a better option.
     */
    private void handlePipeRead() {
        byte[] bytes = new byte[4096];
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        int numBytesRead = 0;
        CharBuffer localBuf = null;
        try {
            numBytesRead = connectionPipe.source().read(byteBuffer);
        } catch (IOException e) {
            if (isDebugEnabled()) {
                debug("Unable to read data from the IPC Pipe (?!!): " + e.getMessage());
            }
            return;
        }
        if (numBytesRead < 0) {
            critical("IPC Pipe closed.");
            return;
        }
        byteBuffer.flip();
        try {
            localBuf = decoder.decode(byteBuffer);
        } catch (Exception e) {
            if (isDebugEnabled()) {
                debug("Unable to decode input buffer: " + e);
            }
        }
        if (localBuf == null) {
            if (isDebugEnabled()) {
                debug("Local buffer is null, and it shouldn't be.");
            }
            return;
        }
        String command = localBuf.toString();
        ClientChannel cChannel = null;
        String commands[] = command.split("\n");
        for (int i = 0; i < commands.length; i++) {
            String currentCommand = commands[i];
            if (currentCommand.startsWith("W") || currentCommand.startsWith("R")) {
                int sKey = (currentCommand.startsWith("W")) ? SelectionKey.OP_WRITE : SelectionKey.OP_READ;
                currentCommand = currentCommand.substring(1);
                cChannel = ClientChannelCache.getDefault().get(currentCommand);
                if (isDebugEnabled()) {
                    debug("Set writable channel: retrieved client channel from serial '" + currentCommand + "'");
                }
                if (cChannel != null) {
                    try {
                        SelectionKey selKey = null;
                        ClientContext cContext = ClientContextCache.getInstance().get(Long.parseLong(currentCommand));
                        selKey = cChannel.getSocketChannel().register(this.selector, sKey);
                        cChannel.addProperty(CONTEXT_PROPERTY, cContext);
                        selKey.attach(cChannel);
                        if (isDebugEnabled()) {
                            debug("Registered client channel for serial '" + currentCommand + "' to write.");
                            if (cContext != null) {
                                debug("Client context retrieved, and reattached.");
                            } else {
                                debug("Unable to retrieve client context for reattachment.");
                            }
                        }
                    } catch (ClosedChannelException e) {
                    }
                } else {
                    if (isDebugEnabled()) {
                        debug("Client serial '" + currentCommand + "' cannot be found.");
                    }
                }
            } else if (currentCommand.startsWith("D")) {
                currentCommand = currentCommand.substring(1);
                cChannel = ClientChannelCache.getDefault().get(currentCommand);
                if (isDebugEnabled()) {
                    debug("Set close: retrieved client channel from serial '" + currentCommand + "'");
                }
                if (cChannel != null) {
                    try {
                        long serialId = Long.parseLong(currentCommand);
                        ClientContextCache.getInstance().remove(serialId);
                        try {
                            cChannel.getSocketChannel().register(this.selector, 0);
                        } catch (CancelledKeyException e) {
                            if (isDebugEnabled()) {
                                debug("Forced disconnection of serial '" + currentCommand + "' failed: Cannot deregister, already cancelled from select.");
                            }
                        }
                        cChannel.close();
                        if (isDebugEnabled()) {
                            debug("Forced disconnection for serial '" + currentCommand + "'");
                        }
                    } catch (ClosedChannelException e) {
                        if (isDebugEnabled()) {
                            debug("Unable to disconnect serial socket for writing: " + e.getMessage());
                        }
                    } catch (IOException e) {
                        if (isDebugEnabled()) {
                            debug("Unable to disconnect serial socket: " + e.getMessage());
                        }
                    }
                } else {
                    if (isDebugEnabled()) {
                        debug("Client serial '" + currentCommand + "' cannot be found.");
                    }
                }
            } else if (currentCommand.startsWith("SD")) {
                if (isDebugEnabled()) {
                    debug("Service shutdown requested, shutting down.");
                }
                this.pInterface.shutdown();
                this.isRunning = false;
            }
        }
    }

    /**
     * Handles the connection acceptance of a socket connection.
     * 
     * @param selKey The <code>SelectionKey</code> object to add to the object.
     * @param sockChannel The <code>SocketChannel</code> to apply the acceptance to.
     */
    private void handleAccept(SelectionKey selKey, SocketChannel sockChannel) {
        if (sockChannel != null) {
            try {
                sockChannel.configureBlocking(false);
            } catch (IOException e) {
                if (isDebugEnabled()) {
                    debug("Unable to configure current connection to non-blocking: " + e.getMessage());
                }
                return;
            }
            try {
                sockChannel.socket().setTcpNoDelay(true);
            } catch (IOException e) {
                if (isDebugEnabled()) {
                    debug("Unable to configure socket to no-delay: " + e.getMessage());
                }
                return;
            }
            info("Connection from '" + sockChannel.socket().getInetAddress().getHostAddress() + "'");
        } else {
            warning("Accepted socket in service '" + this.sc.getProtocol() + "' is null.");
            return;
        }
        String hostAddress = sockChannel.socket().getInetAddress().getHostAddress();
        ClientContext cContext = null;
        if (this.sHosts != null) {
            if (!this.sHosts.hostAllowed(hostAddress)) {
                if (isDebugEnabled()) {
                    debug("IP Address '" + hostAddress + "' host denied.");
                }
                try {
                    sockChannel.close();
                } catch (IOException e) {
                    if (isDebugEnabled()) {
                        debug("Unable to close connection for host '" + hostAddress + "': " + e.getMessage());
                    }
                }
                selKey.cancel();
                return;
            }
            if (isDebugEnabled()) {
                debug("IP Address '" + hostAddress + "' host allowed.");
            }
        }
        if (this.sc.isSecure()) {
            KeyStore ks = null;
            try {
                ks = KeyStore.getInstance(this.sc.getKeyInstance());
            } catch (KeyStoreException e) {
                if (isDebugEnabled()) {
                    debug("Unable to create SSL key store instance: " + e.getMessage());
                }
                return;
            }
            File kf = new File(this.sc.getKeyFile());
            try {
                ks.load(new FileInputStream(kf), this.sc.getStorePassword().toCharArray());
            } catch (FileNotFoundException e) {
                if (isDebugEnabled()) {
                    debug("Unable to locate key file '" + this.sc.getKeyFile() + "': " + e.getMessage());
                }
                return;
            } catch (IOException e) {
                if (isDebugEnabled()) {
                    debug("Unable to load key file '" + this.sc.getKeyFile() + "': " + e.getMessage());
                }
                return;
            } catch (NoSuchAlgorithmException e) {
                if (isDebugEnabled()) {
                    debug("No algorithm handler available for key file '" + this.sc.getKeyFile() + "': " + e.getMessage());
                }
                return;
            } catch (CertificateException e) {
                if (isDebugEnabled()) {
                    debug("Certificate exception in key file '" + this.sc.getKeyFile() + "': " + e.getMessage());
                }
                return;
            }
            KeyManagerFactory kmf = null;
            TrustManagerFactory tmf = null;
            try {
                kmf = KeyManagerFactory.getInstance("SunX509");
            } catch (NoSuchAlgorithmException e) {
                if (isDebugEnabled()) {
                    debug("Unable to create an SSL key factory for type 'SunX509': " + e.getMessage());
                }
                return;
            }
            try {
                tmf = TrustManagerFactory.getInstance("SunX509");
            } catch (NoSuchAlgorithmException e) {
                if (isDebugEnabled()) {
                    debug("Unable to create an SSL key trust factory for type 'SunX509': " + e.getMessage());
                }
                return;
            }
            try {
                kmf.init(ks, this.sc.getKeyPassword().toCharArray());
            } catch (KeyStoreException e) {
                if (isDebugEnabled()) {
                    debug("Unable to initialize key store using password '" + this.sc.getKeyPassword() + "': " + e.getMessage());
                }
                return;
            } catch (NoSuchAlgorithmException e) {
                if (isDebugEnabled()) {
                    debug("No algorithm handler available for key file '" + this.sc.getKeyFile() + "': " + e.getMessage());
                }
                return;
            } catch (UnrecoverableKeyException e) {
                if (isDebugEnabled()) {
                    debug("Unrecoverable problem with key file '" + this.sc.getKeyFile() + "': " + e.getMessage());
                }
                return;
            }
            try {
                tmf.init(ks);
            } catch (KeyStoreException e) {
                if (isDebugEnabled()) {
                    debug("Unable to initialize trust store: " + e.getMessage());
                }
                return;
            }
            SSLContext sslContext = null;
            try {
                sslContext = SSLContext.getInstance(this.sc.getSslContext());
            } catch (NoSuchAlgorithmException e) {
                if (isDebugEnabled()) {
                    debug("Unable to retrieve SSL context instance for '" + this.sc.getSslContext() + "': " + e.getMessage());
                }
                return;
            }
            try {
                sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
            } catch (KeyManagementException e) {
                if (isDebugEnabled()) {
                    debug("Unable to initialize SSL context: " + e.getMessage());
                }
                return;
            }
            SSLEngine engine = sslContext.createSSLEngine();
            engine.setUseClientMode(false);
            try {
                engine.beginHandshake();
            } catch (SSLException e) {
                if (isDebugEnabled()) {
                    debug("Unable to start handshaking for SSL Engine: " + e.getMessage());
                }
                return;
            }
            cContext = ClientContextFactory.getContext(new SecureChannel(sockChannel, engine));
        } else {
            cContext = ClientContextFactory.getContext(new InsecureChannel(sockChannel));
        }
        if (cContext == null) {
            if (isDebugEnabled()) {
                debug("Unable to create socket: socket channel returned a null object.");
            }
        } else {
            ClientContextCache.getInstance().add(cContext.getSerialId(), cContext);
        }
        if (cContext == null) {
            if (isDebugEnabled()) {
                debug("Client context could not be created.  Cannot add this client connection.");
            }
            return;
        }
        try {
            ClientChannel cChannel = cContext.getClientChannel();
            DynamicThread dThread = dtGroup.getAvailableThread();
            int registerKeys = pInterface.getRegisterOps();
            if ((registerKeys & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
                if (isDebugEnabled()) {
                    debug("Underlying protocol interface contains a default key of OP_ACCEPT erroneously!");
                }
                registerKeys |= ~SelectionKey.OP_ACCEPT;
            }
            if ((registerKeys & SelectionKey.OP_CONNECT) == SelectionKey.OP_CONNECT) {
                if (isDebugEnabled()) {
                    debug("Underlying protocol interface contains a default key of OP_CONNECT erroneously!");
                }
                registerKeys |= ~SelectionKey.OP_CONNECT;
            }
            cChannel.addProperty(CONTEXT_PROPERTY, cContext);
            cChannel.addProperty(DYNAMIC_THREAD_PROPERTY, dThread);
            selKey.attach(cChannel);
            cChannel.getSocketChannel().register(this.selector, registerKeys).attach(cChannel);
            if (isDebugEnabled()) {
                debug("Socket channel object registered, selection key set to '" + registerKeys + "'");
            }
            pInterface.addConnection(cContext);
            pInterface.registerPipe(this.connectionPipe);
            ClientChannelCache.getDefault().set(Long.toString(cContext.getSerialId()), cChannel);
        } catch (Exception e) {
            if (isDebugEnabled()) {
                debug("Unable to register or attach selection key for channel: " + e.getClass().getName() + ": " + getStackTrace(e));
            }
        }
    }

    /**
     * Returns the currently active communication <code>Pipe</code> object.
     * 
     * @return <code>Pipe</code> object.
     */
    public Pipe getConnectionPipe() {
        return this.connectionPipe;
    }

    /**
     * Returns the system-wide charset encoding.
     * 
     * @return <code>String</code> containing the encoding type.
     */
    public static String getSystemEncoding() {
        return systemEncoding.get();
    }

    /**
     * Returns the {@link ServiceContext} object.
     * 
     * @return {@link ServiceContext} object.
     */
    public ServiceContext getServiceContext() {
        return sc;
    }

    /**
     * Used by the thread of this service - runs the thread.  This thread runs endless, and must be stopped to shutdown
     * the service.
     */
    public void run() {
        info("Service '" + sc.getProtocol() + "' on port '" + sc.getPort() + "' starting.  Starting worker threads.");
        while (this.isRunning) {
            try {
                selector.select();
            } catch (IOException e) {
                if (isDebugEnabled()) {
                    debug("Unexpected exception caught during select in service '" + this.sc.getProtocol() + "': " + e.getMessage());
                }
                break;
            }
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();
            while (it.hasNext()) {
                SelectionKey selKey = it.next();
                try {
                    selKey.readyOps();
                } catch (CancelledKeyException e) {
                    selKey.cancel();
                    continue;
                }
                if (selKey.isAcceptable()) {
                    SocketChannel sockChannel = null;
                    try {
                        sockChannel = ((ServerSocketChannel) selKey.channel()).accept();
                    } catch (IOException e) {
                        if (isDebugEnabled()) {
                            debug("Unable to accept connection from client: " + e.getMessage());
                        }
                        selKey.cancel();
                        continue;
                    } catch (NullPointerException e) {
                    }
                    handleAccept(selKey, sockChannel);
                } else if (selKey.isReadable()) {
                    Object attachment = selKey.attachment();
                    if (attachment == null) {
                        if (isDebugEnabled()) {
                            debug("Handle pipe read.");
                        }
                        handlePipeRead();
                        it.remove();
                        continue;
                    }
                    ClientChannel cChannel = (ClientChannel) attachment;
                    if (cChannel.isOpen()) {
                        ClientContext cContext = (ClientContext) cChannel.getProperty(CONTEXT_PROPERTY);
                        DynamicThread dThread = (DynamicThread) cChannel.getProperty(DYNAMIC_THREAD_PROPERTY);
                        dThread.sendObject(new WorkerReadMessage(cContext));
                    } else {
                        if (isDebugEnabled()) {
                            debug("Socket requested read, indicates closed, cancelling select key.");
                        }
                        selKey.cancel();
                    }
                } else if (selKey.isWritable()) {
                    Object attachment = selKey.attachment();
                    if (attachment == null) {
                        continue;
                    }
                    ClientChannel cChannel = (ClientChannel) attachment;
                    if (cChannel.isOpen()) {
                        ClientContext cContext = (ClientContext) cChannel.getProperty(CONTEXT_PROPERTY);
                        DynamicThread dThread = (DynamicThread) cChannel.getProperty(DYNAMIC_THREAD_PROPERTY);
                        dThread.sendObject(new WorkerWriteMessage(cContext));
                    } else {
                        if (isDebugEnabled()) {
                            debug("Socket requested write, indicates closed, cancelling select key.");
                        }
                        selKey.cancel();
                    }
                }
                it.remove();
            }
            if (!this.isRunning) {
                break;
            }
        }
        try {
            ssChannel.close();
        } catch (IOException e) {
            if (isDebugEnabled()) {
                debug("Unable to shut down incoming socket: " + e.getMessage());
            }
        }
        try {
            selector.close();
        } catch (IOException e) {
            if (isDebugEnabled()) {
                debug("Unable to close selector: " + e.getMessage());
            }
        }
        info("Service '" + sc.getProtocol() + "' on port '" + sc.getPort() + "' normal shutdown.");
    }
}
