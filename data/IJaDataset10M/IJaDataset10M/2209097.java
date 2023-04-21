package php.java.bridge.http;

import php.java.bridge.AppThreadPool;
import php.java.bridge.ILogger;
import php.java.bridge.Util;

/**
 * A bridge pattern which either uses the PipeContextServer or the SocketContextServer, 
 * depending on the OS and/or the security restrictions. On windows, which cannot use named pipes,
 * a SocketContextServer is used. All other operating systems use a PipeContextServer unless the 
 * system property php.java.bridge.promiscuous is set to true or the system property
 * php.java.bridge.no_pipe_server is set to true.
 * <p>
 * A ContextServer instance represents the current web context. 
 * When the PipeContextServer is used, there can be more than one PipeContextServer instance per classloader, the ContextFactory.get() checks
 * if it is called with the same ContextServer and throws a SecurityException otherwise. So one cannot access contexts belonging to other web contexts.
 * </p><p>
 * The SocketContextServer uses only one server socket for all shared web contexts and cannot do any security checks.
 * </p>
 * @author jostb
 * @see php.java.bridge.http.SocketContextServer
 */
public final class ContextServer {

    private String contextName;

    private boolean promiscuous;

    /** Only for internal use */
    public static final String ROOT_CONTEXT_SERVER_ATTRIBUTE = ContextServer.class.getName() + ".ROOT";

    private static SocketContextServer sock = null;

    private static AppThreadPool pool;

    private static synchronized AppThreadPool getAppThreadPool() {
        if (pool != null) return pool;
        return pool = new AppThreadPool("JavaBridgeContextRunner", Integer.parseInt(Util.THREAD_POOL_MAX_SIZE));
    }

    private class SocketChannelName extends AbstractChannelName {

        public SocketChannelName(String name, IContextFactory ctx) {
            super(name, ctx);
        }

        public boolean startChannel(ILogger logger) {
            return sock.start(this, logger);
        }

        public String toString() {
            return "Socket:" + getName();
        }
    }

    /**
     * Create a new ContextServer using a thread pool.
     * @param contextName The the name of the web context to which this server belongs.
     */
    public ContextServer(String contextName, boolean promiscuous) {
        this.contextName = contextName;
        this.promiscuous = promiscuous;
    }

    /**
     * @return true for all network interfaces, false for loopback only
     * 
     */
    public boolean isPromiscuous() {
        return this.promiscuous;
    }

    private static final synchronized void destroyContextServer() {
        if (sock != null) sock.destroy();
        sock = null;
        ContextFactory.destroyAll();
        php.java.bridge.SessionFactory.destroyTimer();
        if (pool != null) pool.destroy();
        pool = null;
    }

    /**
     * Destroy the pipe or socket context server.
     */
    public void destroy() {
        destroyContextServer();
    }

    /**
     * Check if either the pipe of the socket context server is available. This function
     * may try start a SocketContextServer, if a PipeContextServer is not available. 
     * @param channelName The header value for X_JAVABRIDGE_CHANNEL, may be null.  
     * @return true if either the pipe or the socket context server is available.
     */
    public boolean isAvailable(String channelName) {
        if (!SocketContextServer.SOCKET_SERVER_AVAIL) return false;
        SocketContextServer sock = getSocketContextServer(this, getAppThreadPool(), contextName);
        return sock != null && sock.isAvailable();
    }

    private static synchronized SocketContextServer getSocketContextServer(ContextServer server, AppThreadPool pool, String contextName) {
        if (sock != null) return sock;
        return sock = new SocketContextServer(pool, server.isPromiscuous(), contextName);
    }

    /**
     * Start a channel name.
     * @param channelName The ChannelName.
     * @throws IllegalStateException if there's no Pipe- or SocketContextServer available
     */
    public void start(AbstractChannelName channelName, ILogger logger) {
        boolean started = channelName.start(logger);
        if (!started) throw new IllegalStateException("SocketContextServer not available");
    }

    /**
     * Return the channelName which be passed to the client as X_JAVABRIDGE_REDIRECT
     * @param currentCtx The current ContextFactory, see X_JAVABRIDGE_CONTEXT
     * @return The channel name of the Pipe- or SocketContextServer.
     */
    public AbstractChannelName getChannelName(IContextFactory currentCtx) {
        SocketContextServer sock = getSocketContextServer(this, getAppThreadPool(), contextName);
        return sock.isAvailable() ? new SocketChannelName(sock.getChannelName(), currentCtx) : null;
    }

    /**{@inheritDoc}*/
    public String toString() {
        return "ContextServer: " + contextName;
    }
}
