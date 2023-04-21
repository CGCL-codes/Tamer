package org.gridbus.broker.common.security;

/**
 * @author krishna
 * Represents a ProxyCredential which is a proxy retrieved from a MyProxy server
 */
public class MyProxyCredential extends ProxyCredential {

    private String host = null;

    private int port = 7512;

    private String username = null;

    private String password = null;

    /**
	 * 
	 */
    public MyProxyCredential() {
        super();
    }

    /**
	 * @param host
	 * @param port
	 * @param username
	 * @param password
	 */
    public MyProxyCredential(String host, int port, String username, String password) {
        this();
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        if (this.port == 0) this.port = 7512;
    }

    /**
	 * @return Returns the host.
	 */
    public String getHost() {
        return host;
    }

    /**
	 * @param myProxyServerHostname The host to set.
	 */
    public void setHost(String myProxyServerHostname) {
        this.host = myProxyServerHostname;
    }

    /**
	 * @return Returns the port.
	 */
    public int getPort() {
        return port;
    }

    /**
	 * @param myProxyServerPort
	 */
    public void setPort(int myProxyServerPort) {
        this.port = myProxyServerPort;
    }

    /**
	 * @return password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username
	 */
    public void setUsername(String username) {
        this.username = username;
    }
}
