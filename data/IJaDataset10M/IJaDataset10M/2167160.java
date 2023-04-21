package com.hyper9.vmm.client;

import java.io.Serializable;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Information used to make a connection to a virtual host server.
 * 
 * @author akutz
 */
public final class ConnectionInfo implements IsSerializable, Serializable {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = -4014132046567174711L;

    /**
     * The server alias.
     */
    private String serverAlias;

    /**
     * The password to use when connecting to the server.
     */
    private String password;

    /**
     * The username to use when connecting to the server.
     */
    private String userName;

    /**
     * The session token associated with an existed session.
     */
    private String sessionToken;

    /**
     * Set to true to ignore certificate warnings when connecting to the server.
     */
    private boolean ignoreCert;

    /**
     * The address of the server (IP or FQDN).
     */
    private String serverAddress;

    /**
     * The interval to poll the server for updates (in seconds).
     */
    private int pollInterval;

    /**
     * The port to connect to the server on.
     */
    private int port;

    /**
     * The server's type.
     */
    private String serverType;

    /**
     * Set to true to use SSL when connecting to the server.
     */
    private boolean useSsl;

    /**
     * The default public constructor that must exist so this class can be
     * serializable for GWT.
     */
    public ConnectionInfo() {
    }

    /**
     * Initializes a new instance of the ConnectionInfo class.
     * 
     * @param serverAlias The server alias.
     * @param userName The user name.
     * @param password The password.
     */
    public ConnectionInfo(String serverAlias, String userName, String password) {
        this.serverAlias = serverAlias;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Gets the server alias.
     * 
     * @return The server alias.
     */
    public String getServerAlias() {
        return this.serverAlias;
    }

    /**
     * Gets the password to use when connecting to the server.
     * 
     * @return The password to use when connecting to the server.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Gets the username to use when connecting to the server.
     * 
     * @return The username to use when connecting to the server.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Gets the session token to use when connecting to the server.
     * 
     * @return The session token to use when connecting to the server.
     */
    public String getSessionToken() {
        return this.sessionToken;
    }

    /**
     * @param sessionToken The session token to use when connecting to the
     *        server. If this value is specified then the userName and password
     *        values will be ignored.
     */
    public void setSessionToken(final String sessionToken) {
        this.sessionToken = sessionToken;
    }

    /**
     * @return The IP address or the FQDN of the server to connect to.
     */
    public String getServerAddress() {
        return this.serverAddress;
    }

    /**
     * @return The interval to poll the server for updates (in seconds).
     */
    public int getPollInterval() {
        return this.pollInterval;
    }

    /**
     * @return The port to connect to the server on.
     */
    public int getPort() {
        return this.port;
    }

    /**
     * @return The server's type.
     */
    public String getServerType() {
        return this.serverType;
    }

    /**
     * @return True if the server certification should be ignored; otherwise
     * false.
     */
    public boolean isIgnoreCert() {
        return this.ignoreCert;
    }

    /**
     * @return True if SSL should be used; otherwise false.
     */
    public boolean isUseSsl() {
        return this.useSsl;
    }

    /**
     * @param ignoreCert Set to true to ignore certificate warnings when
     * connecting to the server.
     */
    public void setIgnoreCert(final boolean ignoreCert) {
        this.ignoreCert = ignoreCert;
    }

    /**
     * @param ignoreCert Set to true to ignore certificate warnings when
     * connecting to the server.
     */
    public void setIgnoreCert(final String ignoreCert) {
        this.ignoreCert = Boolean.valueOf(ignoreCert);
        ;
    }

    /**
     * @param serverAddress The IP address or the FQDN of the server to connect
     * to.
     */
    public void setServerAddress(final String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * @param password The password to use when connecting to the server.
     */
    public void setPassword(final String password) {
        this.password = password;
    }

    /**
     * @param pollInterval The interval to poll the server for updates (in
     * seconds).
     */
    public void setPollInterval(final int pollInterval) {
        this.pollInterval = pollInterval;
    }

    /**
     * @param pollInterval The interval to poll the server for updates (in
     * seconds).
     */
    public void setPollInterval(final String pollInterval) {
        this.pollInterval = Integer.valueOf(pollInterval);
    }

    /**
     * @param port The port to connect to the server on.
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * @param port The port to connect to the server on.
     */
    public void setPort(final String port) {
        this.port = Integer.valueOf(port);
    }

    /**
     * @param serverType The server's type.
     */
    public void setServerType(final String serverType) {
        this.serverType = serverType;
    }

    /**
     * @param userName The username to use when connecting to the server.
     */
    public void setUserName(final String userName) {
        this.userName = userName;
    }

    /**
     * @param useSsl Set to true to use SSL when connecting to the server.
     */
    public void setUseSsl(final boolean useSsl) {
        this.useSsl = useSsl;
    }

    /**
     * @param useSsl Set to true to use SSL when connecting to the server.
     */
    public void setUseSsl(final String useSsl) {
        this.useSsl = Boolean.valueOf(useSsl);
    }
}
