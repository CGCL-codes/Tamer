package org.apache.commons.httpclient;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A connection manager that provides access to a single HttpConnection.  This
 * manager makes no attempt to provide exclusive access to the contained
 * HttpConnection.
 *
 * @author <a href="mailto:becke@u.washington.edu">Michael Becke</a>
 * @author Eric Johnson
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * @author Laura Werner
 * 
 * @since 2.0
 */
public class SimpleHttpConnectionManager implements HttpConnectionManager {

    private static final Log LOG = LogFactory.getLog(SimpleHttpConnectionManager.class);

    private static final String MISUSE_MESSAGE = "SimpleHttpConnectionManager being used incorrectly.  Be sure that" + " HttpMethod.releaseConnection() is always called and that only one thread" + " and/or method is using this connection manager at a time.";

    /**
     * Since the same connection is about to be reused, make sure the
     * previous request was completely processed, and if not
     * consume it now.
     * @param conn The connection
     */
    static void finishLastResponse(HttpConnection conn) {
        InputStream lastResponse = conn.getLastResponseInputStream();
        if (lastResponse != null) {
            conn.setLastResponseInputStream(null);
            try {
                lastResponse.close();
            } catch (IOException ioe) {
                conn.close();
            }
        }
    }

    /** The http connection */
    protected HttpConnection httpConnection;

    /**
     * Collection of parameters associated with this connection manager.
     */
    private HttpConnectionManagerParams params = new HttpConnectionManagerParams();

    /**
     * The time the connection was made idle.
     */
    private long idleStartTime = Long.MAX_VALUE;

    /**
     * Used to test if {@link #httpConnection} is currently in use 
     * (i.e. checked out).  This is only used as a sanity check to help
     * debug cases where this connection manager is being used incorrectly.
     * It will not be used to enforce thread safety.
     */
    private volatile boolean inUse = false;

    public SimpleHttpConnectionManager() {
    }

    /**
     * @see HttpConnectionManager#getConnection(HostConfiguration)
     */
    public HttpConnection getConnection(HostConfiguration hostConfiguration) {
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
    public void setConnectionStaleCheckingEnabled(boolean connectionStaleCheckingEnabled) {
        this.params.setStaleCheckingEnabled(connectionStaleCheckingEnabled);
    }

    /**
     * @see HttpConnectionManager#getConnectionWithTimeout(HostConfiguration, long)
     * 
     * @since 3.0
     */
    public HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) {
        if (httpConnection == null) {
            httpConnection = new HttpConnection(hostConfiguration);
            httpConnection.setHttpConnectionManager(this);
            httpConnection.getParams().setDefaults(this.params);
        } else {
            if (!hostConfiguration.hostEquals(httpConnection) || !hostConfiguration.proxyEquals(httpConnection)) {
                if (httpConnection.isOpen()) {
                    httpConnection.close();
                }
                httpConnection.setHost(hostConfiguration.getHost());
                httpConnection.setPort(hostConfiguration.getPort());
                httpConnection.setProtocol(hostConfiguration.getProtocol());
                httpConnection.setLocalAddress(hostConfiguration.getLocalAddress());
                httpConnection.setProxyHost(hostConfiguration.getProxyHost());
                httpConnection.setProxyPort(hostConfiguration.getProxyPort());
            } else {
                finishLastResponse(httpConnection);
            }
        }
        idleStartTime = Long.MAX_VALUE;
        if (inUse) LOG.warn(MISUSE_MESSAGE);
        inUse = true;
        return httpConnection;
    }

    /**
     * @see HttpConnectionManager#getConnection(HostConfiguration, long)
     * 
     * @deprecated Use #getConnectionWithTimeout(HostConfiguration, long)
     */
    public HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) {
        return getConnectionWithTimeout(hostConfiguration, timeout);
    }

    /**
     * @see HttpConnectionManager#releaseConnection(org.apache.commons.httpclient.HttpConnection)
     */
    public void releaseConnection(HttpConnection conn) {
        if (conn != httpConnection) {
            throw new IllegalStateException("Unexpected release of an unknown connection.");
        }
        finishLastResponse(httpConnection);
        inUse = false;
        idleStartTime = System.currentTimeMillis();
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
    public void setParams(final HttpConnectionManagerParams params) {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        this.params = params;
    }

    /**
     * @since 3.0
     */
    public void closeIdleConnections(long idleTimeout) {
        long maxIdleTime = System.currentTimeMillis() - idleTimeout;
        if (idleStartTime <= maxIdleTime) {
            httpConnection.close();
        }
    }
}
