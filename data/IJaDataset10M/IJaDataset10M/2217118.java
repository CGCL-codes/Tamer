package com.volantis.mcs.repository.jdbc;

import javax.sql.DataSource;

/**
 * Configuration needed to create a local JDBC repository.
 *
 * <p>
 * <strong>Warning: This is a facade provided for use by user code, not for
 * implementation by user code. User implementations of this interface are
 * highly likely to be incompatible with current and future releases of the
 * product at binary and source levels.</strong>
 * </p>
 *
 * <table border="1" cellpadding="3" cellspacing="0" width="100%">
 *
 * <tr bgcolor="#ccccff" class="TableHeadingColor">
 * <td colspan="2"><font size="+2">
 * <b>Property Summary</b></font></td>
 * </tr>
 *
 * <tr id="enabled">
 * <td align="right" valign="top" width="1%"><b>enabled</b></td>
 * <td>determines whether the connection pool is
 * enabled. If this is set to true then it is and
 * {@link JDBCRepositoryFactory#createMCSConnectionPool(MCSConnectionPoolConfiguration, DataSource) createMCSConnectionPool}
 * returns a new pooled {@link DataSource}, otherwise it returns the
 * {@link DataSource} that was passed in.</td>
 * </tr>
 *
 * <tr id="maxConnections">
 * <td align="right" valign="top" width="1%"><b>maximum&nbsp;connections</b></td>
 * <td>the maximum number of
 * connections that the pool can have open at one time. Attempts to open
 * additional connections after this number is reached will wait until a
 * connection becomes available.</td>
 * </tr>
 *
 * <tr id="maxFreeConnections">
 * <td align="right" valign="top" width="1%"><b>maximum&nbsp;free&nbsp;connections</b></td>
 * <td>the maximum
 * number of free connections that the pool will have open at one time.
 * Additional free connections will be closed.</td>
 * </tr>
 *
 * <tr id="minFreeConnections">
 * <td align="right" valign="top" width="1%"><b>minimum&nbsp;free&nbsp;connections</b></td>
 * <td>the minimum
 * number of free connections that the pool will have open at any one time.
 * If the number of free connections is less than this then the pool will
 * open additional connections on the underlying {@link DataSource} until
 * either at least this many free connections are reached, or the maximum
 * number of connections in total is reached.</td>
 * </tr>
 *
 * <tr id="initialConnections">
 * <td align="right" valign="top" width="1%"><b>initial&nbsp;connections</b></td>
 * <td>the initial
 * number of connections that the pool will open when it is started, this
 * must be at least the same as
 * <a href="#minFreeConnections">minimum free connections</a>.</td>
 * </tr>
 *
 * <tr id="keepAliveActive">
 * <td align="right" valign="top" width="1%"><b>keep&nbsp;alive&nbsp;active</b></td>
 * <td>determines whether
 * the connection pool will attempt to keep the free connections in the
 * pool alive by making periodic queries on them. If true then it will,
 * otherwise it will not.</td>
 * </tr>
 *
 * <tr id="keepAlivePollInterval">
 * <td align="right" valign="top" width="1%"><b>keep&nbsp;alive&nbsp;poll&nbsp;interval</b></td>
 * <td>the
 * interval in milliseconds between successive keep alive queries. This is
 * only used if <a href="#keepAliveActive">keep alive active</a> is set to
 * true.</td>
 * </tr>
 *
 * </table>
 *
 * @volantis-api-include-in PublicAPI
 * @volantis-api-include-in ProfessionalServicesAPI
 * @volantis-api-include-in InternalAPI
 * @see JDBCRepositoryFactory#createMCSConnectionPoolConfiguration()
 * @see JDBCRepositoryFactory#createMCSConnectionPool(MCSConnectionPoolConfiguration, javax.sql.DataSource)
 * @since 3.5.1
 */
public interface MCSConnectionPoolConfiguration {

    /**
     * Getter for the <a href="#enabled">enabled</a> property.
     *
     * @return Value of the <a href="#enabled">enabled</a>
     *         property.
     */
    boolean isEnabled();

    /**
     * Setter for the <a href="#enabled">enabled</a> property.
     *
     * @param enabled New value of the
     *                <a href="#enabled">enabled</a> property.
     */
    void setEnabled(boolean enabled);

    /**
     * Getter for the <a href="#maxConnections">maximum connections</a> property.
     *
     * @return Value of the <a href="#maxConnections">maximum connections</a>
     *         property.
     */
    int getMaxConnections();

    /**
     * Setter for the <a href="#maxConnections">maximum connections</a> property.
     *
     * @param maxConnections New value of the
     *                       <a href="#maxConnections">maximum connections</a> property.
     */
    void setMaxConnections(int maxConnections);

    /**
     * Getter for the <a href="#maxFreeConnections">maximum free connections</a> property.
     *
     * @return Value of the <a href="#maxFreeConnections">maximum free connections</a>
     *         property.
     */
    int getMaxFreeConnections();

    /**
     * Setter for the <a href="#maxFreeConnections">maximum free connections</a> property.
     *
     * @param maxFreeConnections New value of the
     *                           <a href="#maxFreeConnections">maximum free connections</a> property.
     */
    void setMaxFreeConnections(int maxFreeConnections);

    /**
     * Getter for the <a href="#minFreeConnections">minimum free connections</a> property.
     *
     * @return Value of the <a href="#minFreeConnections">minimum free connections</a>
     *         property.
     */
    int getMinFreeConnections();

    /**
     * Setter for the <a href="#minFreeConnections">minimum free connections</a> property.
     *
     * @param minFreeConnections New value of the
     *                           <a href="#minFreeConnections">minimum free connections</a> property.
     */
    void setMinFreeConnections(int minFreeConnections);

    /**
     * Getter for the <a href="#initialConnections">initial connections</a> property.
     *
     * @return Value of the <a href="#initialConnections">initial connections</a>
     *         property.
     */
    int getInitialConnections();

    /**
     * Setter for the <a href="#initialConnections">initial connections</a> property.
     *
     * @param initialConnections New value of the
     *                           <a href="#initialConnections">initial connections</a> property.
     */
    void setInitialConnections(int initialConnections);

    /**
     * Getter for the <a href="#keepAliveActive">keep alive active</a> property.
     *
     * @return Value of the <a href="#keepAliveActive">keep alive active</a>
     *         property.
     */
    boolean isKeepAliveActive();

    /**
     * Setter for the <a href="#keepAliveActive">keep alive active</a> property.
     *
     * @param keepAliveActive New value of the
     *                        <a href="#keepAliveActive">keep alive active</a> property.
     */
    void setKeepAliveActive(boolean keepAliveActive);

    /**
     * Getter for the <a href="#keepAlivePollInterval">keep alive poll interval</a> property.
     *
     * @return Value of the <a href="#keepAlivePollInterval">keep alive poll interval</a>
     *         property.
     */
    int getKeepAlivePollInterval();

    /**
     * Setter for the <a href="#keepAlivePollInterval">keep alive poll interval</a> property.
     *
     * @param keepAlivePollInterval New value of the
     *                              <a href="#keepAlivePollInterval">keep alive poll interval</a> property.
     */
    void setKeepAlivePollInterval(int keepAlivePollInterval);
}
