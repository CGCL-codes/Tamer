package org.archive.httpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpConnectionManager;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * A simple, but thread-safe HttpClient {@link HttpConnectionManager}.
 * Based on {@link org.apache.commons.httpclient.SimpleHttpConnectionManager}.
 * 
 * <b>Java &gt;= 1.4 is recommended.</b>
 * 
 * @author Christian Kohlschuetter 
 */
public final class ThreadLocalHttpConnectionManager implements HttpConnectionManager {

    private static final CloserThread closer = new CloserThread();

    private static final Logger logger = Logger.getLogger(ThreadLocalHttpConnectionManager.class.getName());

    private final ThreadLocal tl = new ThreadLocal() {

        protected synchronized Object initialValue() {
            return new ConnectionInfo();
        }
    };

    private ConnectionInfo getConnectionInfo() {
        return (ConnectionInfo) tl.get();
    }

    private final class ConnectionInfo {

        /** The http connection */
        private HttpConnection conn = null;

        /**
         * The time the connection was made idle.
         */
        private long idleStartTime = Long.MAX_VALUE;
    }

    public ThreadLocalHttpConnectionManager() {
    }

    /**
     * Since the same connection is about to be reused, make sure the
     * previous request was completely processed, and if not
     * consume it now.
     * @param conn The connection
     * @return true, if the connection is reusable
     */
    private static boolean finishLastResponse(final HttpConnection conn) {
        InputStream lastResponse = conn.getLastResponseInputStream();
        if (lastResponse != null) {
            conn.setLastResponseInputStream(null);
            try {
                lastResponse.close();
                return true;
            } catch (IOException ioe) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Collection of parameters associated with this connection manager.
     */
    private HttpConnectionManagerParams params = new HttpConnectionManagerParams();

    /**
     * @see HttpConnectionManager#getConnection(HostConfiguration)
     */
    public HttpConnection getConnection(final HostConfiguration hostConfiguration) {
        return getConnection(hostConfiguration, 0);
    }

    /**
     * Gets the staleCheckingEnabled value to be set on HttpConnections that are created.
     * 
     * @return <code>true</code> if stale checking will be enabled on HttpConections
     * 
     * @see HttpConnection#isStaleCheckingEnabled()
     * 
     * @deprecated Use {@link HttpConnectionManagerParams#isStaleCheckingEnabled()},
     * {@link HttpConnectionManager#getParams()}.
     */
    public boolean isConnectionStaleCheckingEnabled() {
        return this.params.isStaleCheckingEnabled();
    }

    /**
     * Sets the staleCheckingEnabled value to be set on HttpConnections that are created.
     * 
     * @param connectionStaleCheckingEnabled <code>true</code> if stale checking will be enabled 
     * on HttpConections
     * 
     * @see HttpConnection#setStaleCheckingEnabled(boolean)
     * 
     * @deprecated Use {@link HttpConnectionManagerParams#setStaleCheckingEnabled(boolean)},
     * {@link HttpConnectionManager#getParams()}.
     */
    public void setConnectionStaleCheckingEnabled(final boolean connectionStaleCheckingEnabled) {
        this.params.setStaleCheckingEnabled(connectionStaleCheckingEnabled);
    }

    /**
     * @see HttpConnectionManager#getConnectionWithTimeout(HostConfiguration, long)
     * 
     * @since 3.0
     */
    public HttpConnection getConnectionWithTimeout(final HostConfiguration hostConfiguration, final long timeout) {
        final ConnectionInfo ci = getConnectionInfo();
        HttpConnection httpConnection = ci.conn;
        if (httpConnection == null || !finishLastResponse(httpConnection) || !hostConfiguration.hostEquals(httpConnection) || !hostConfiguration.proxyEquals(httpConnection)) {
            if (httpConnection != null && httpConnection.isOpen()) {
                closer.closeConnection(httpConnection);
            }
            httpConnection = new HttpConnection(hostConfiguration);
            httpConnection.setHttpConnectionManager(this);
            httpConnection.getParams().setDefaults(this.params);
            ci.conn = httpConnection;
            httpConnection.setHost(hostConfiguration.getHost());
            httpConnection.setPort(hostConfiguration.getPort());
            httpConnection.setProtocol(hostConfiguration.getProtocol());
            httpConnection.setLocalAddress(hostConfiguration.getLocalAddress());
            httpConnection.setProxyHost(hostConfiguration.getProxyHost());
            httpConnection.setProxyPort(hostConfiguration.getProxyPort());
        }
        ci.idleStartTime = Long.MAX_VALUE;
        return httpConnection;
    }

    /**
     * @see HttpConnectionManager#getConnection(HostConfiguration, long)
     * 
     * @deprecated Use #getConnectionWithTimeout(HostConfiguration, long)
     */
    public HttpConnection getConnection(final HostConfiguration hostConfiguration, final long timeout) {
        return getConnectionWithTimeout(hostConfiguration, timeout);
    }

    /**
     * @see HttpConnectionManager#releaseConnection(org.apache.commons.httpclient.HttpConnection)
     */
    public void releaseConnection(final HttpConnection conn) {
        final ConnectionInfo ci = getConnectionInfo();
        HttpConnection httpConnection = ci.conn;
        if (conn != httpConnection) {
            throw new IllegalStateException("Unexpected release of an unknown connection.");
        }
        finishLastResponse(httpConnection);
        ci.idleStartTime = System.currentTimeMillis();
    }

    /**
     * Returns {@link HttpConnectionManagerParams parameters} associated 
     * with this connection manager.
     * 
     * @since 2.1
     * 
     * @see HttpConnectionManagerParams
     */
    public HttpConnectionManagerParams getParams() {
        return this.params;
    }

    /**
     * Assigns {@link HttpConnectionManagerParams parameters} for this 
     * connection manager.
     * 
     * @since 2.1
     * 
     * @see HttpConnectionManagerParams
     */
    public void setParams(final HttpConnectionManagerParams p) {
        if (p == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = p;
    }

    /**
     * @since 3.0
     */
    public void closeIdleConnections(final long idleTimeout) {
        long maxIdleTime = System.currentTimeMillis() - idleTimeout;
        final ConnectionInfo ci = getConnectionInfo();
        if (ci.idleStartTime <= maxIdleTime) {
            ci.conn.close();
        }
    }

    private static final class CloserThread extends Thread {

        private List<HttpConnection> connections = new ArrayList<HttpConnection>();

        private static final int SLEEP_INTERVAL = 5000;

        public CloserThread() {
            super("HttpConnection closer");
            setDaemon(true);
            start();
        }

        public void closeConnection(final HttpConnection conn) {
            synchronized (connections) {
                connections.add(conn);
            }
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(SLEEP_INTERVAL);
                    List<HttpConnection> s;
                    synchronized (connections) {
                        s = connections;
                        connections = new ArrayList<HttpConnection>();
                    }
                    logger.log(Level.INFO, "Closing " + s.size() + " HttpConnections");
                    for (final Iterator<HttpConnection> it = s.iterator(); it.hasNext(); ) {
                        HttpConnection conn = it.next();
                        conn.close();
                        conn.setHttpConnectionManager(null);
                        it.remove();
                    }
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
