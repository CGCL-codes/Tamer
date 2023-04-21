package org.apache.commons.httpclient;

import org.apache.commons.httpclient.params.HttpConnectionManagerParams;

/**
 * An interface for classes that manage HttpConnections.
 * 
 * @see org.apache.commons.httpclient.HttpConnection
 * @see org.apache.commons.httpclient.HttpClient#HttpClient(HttpConnectionManager)
 *
 * @author Michael Becke
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:oleg@ural.ru">Oleg Kalnichevski</a>
 * 
 * @since 2.0
 */
public interface HttpConnectionManager {

    /**
     * Gets an HttpConnection for a given host configuration. If a connection is
     * not available this method will block until one is.
     *
     * The connection manager should be registered with any HttpConnection that
     * is created.
     *
     * @param hostConfiguration the host configuration to use to configure the
     * connection
     * 
     * @return an HttpConnection for the given configuration
     * 
     * @see HttpConnection#setHttpConnectionManager(HttpConnectionManager)
     */
    HttpConnection getConnection(HostConfiguration hostConfiguration);

    /**
     * Gets an HttpConnection for a given host configuration. If a connection is
     * not available, this method will block for at most the specified number of
     * milliseconds or until a connection becomes available.
     *
     * The connection manager should be registered with any HttpConnection that
     * is created.
     *
     * @param hostConfiguration the host configuration to use to configure the
     * connection
     * @param timeout - the time (in milliseconds) to wait for a connection to
     * become available, 0 to specify an infinite timeout
     * 
     * @return an HttpConnection for the given configuraiton
     * 
     * @throws HttpException if no connection becomes available before the
     * timeout expires
     * 
     * @see HttpConnection#setHttpConnectionManager(HttpConnectionManager)
     * 
     * @deprecated Use #getConnectionWithTimeout(HostConfiguration, long) 
     */
    HttpConnection getConnection(HostConfiguration hostConfiguration, long timeout) throws HttpException;

    /**
     * Gets an HttpConnection for a given host configuration. If a connection is
     * not available, this method will block for at most the specified number of
     * milliseconds or until a connection becomes available.
     *
     * The connection manager should be registered with any HttpConnection that
     * is created.
     *
     * @param hostConfiguration the host configuration to use to configure the
     * connection
     * @param timeout - the time (in milliseconds) to wait for a connection to
     * become available, 0 to specify an infinite timeout
     * 
     * @return an HttpConnection for the given configuraiton
     * 
     * @throws ConnectionPoolTimeoutException if no connection becomes available before the
     * timeout expires
     * 
     * @see HttpConnection#setHttpConnectionManager(HttpConnectionManager)
     * 
     * @since 3.0
     */
    HttpConnection getConnectionWithTimeout(HostConfiguration hostConfiguration, long timeout) throws ConnectionPoolTimeoutException;

    /**
     * Releases the given HttpConnection for use by other requests.
     *
     * @param conn - The HttpConnection to make available.
     */
    void releaseConnection(HttpConnection conn);

    /**
     * Closes connections that have been idle for at least the given amount of time.  Only
     * connections that are currently owned, not checked out, are subject to idle timeouts.
     * 
     * @param idleTimeout the minimum idle time, in milliseconds, for connections to be closed
     * 
     * @since 3.0
     */
    void closeIdleConnections(long idleTimeout);

    /**
     * Returns {@link HttpConnectionManagerParams parameters} associated 
     * with this connection manager.
     * 
     * @since 3.0
     * 
     * @see HttpConnectionManagerParams
     */
    HttpConnectionManagerParams getParams();

    /**
     * Assigns {@link HttpConnectionManagerParams parameters} for this 
     * connection manager.
     * 
     * @since 3.0
     * 
     * @see HttpConnectionManagerParams
     */
    void setParams(final HttpConnectionManagerParams params);
}
