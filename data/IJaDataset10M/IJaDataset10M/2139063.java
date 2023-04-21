package net.sf.mxlosgi.core;

import net.sf.mxlosgi.utils.DNSUtil;

public class ConnectionConfig {

    private String host;

    private String serviceName;

    private int port = 5222;

    private SecurityMode securityMode = SecurityMode.enabled;

    private boolean compressionEnabled = true;

    private int responseStanzaTimeout = 30 * 1000;

    private int connectionTimeout = 30 * 1000;

    /**
	 * 
	 */
    public ConnectionConfig() {
    }

    /**
	 * @param serviceName
	 */
    public ConnectionConfig(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
	 * 
	 * @return
	 */
    public String getHost() {
        if (serviceName == null || serviceName.isEmpty()) {
            return null;
        }
        if (host == null) {
            DNSUtil.HostAddress hostAddress = DNSUtil.resolveXMPPDomain(serviceName);
            init(hostAddress.getHost(), serviceName, hostAddress.getPort());
        }
        return host;
    }

    /**
	 * 
	 * @return
	 */
    public int getPort() {
        return port;
    }

    /**
	 * 
	 * @return
	 */
    public SecurityMode getSecurityMode() {
        return securityMode;
    }

    /**
	 * 
	 * @return
	 */
    public String getServiceName() {
        return serviceName;
    }

    /**
	 * 
	 * @return
	 */
    public boolean isCompressionEnabled() {
        return compressionEnabled;
    }

    /**
	 * 
	 * @param compressionEnabled
	 */
    public void setCompressionEnabled(boolean compressionEnabled) {
        this.compressionEnabled = compressionEnabled;
    }

    /**
	 * 
	 * @param host
	 */
    public void setHost(String host) {
        this.host = host;
    }

    /**
	 * 
	 * @param port
	 */
    public void setPort(int port) {
        this.port = port;
    }

    /**
	 * 
	 * @param securityMode
	 */
    public void setSecurityMode(SecurityMode securityMode) {
        this.securityMode = securityMode;
    }

    /**
	 * 
	 * @param serviceName
	 */
    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
        this.host = null;
    }

    private void init(String host, String serviceName, int port) {
        this.host = host;
        this.serviceName = serviceName;
        this.port = port;
    }

    /**
	 * 
	 * @return
	 */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
	 * 
	 * @param second
	 */
    public void setConnectionTimeout(int second) {
        this.connectionTimeout = second;
    }

    /**
	 * @return the responseStanzaTimeout
	 */
    public int getResponseStanzaTimeout() {
        return responseStanzaTimeout;
    }

    /**
	 * @param responseStanzaTimeout the responseStanzaTimeout to set
	 */
    public void setResponseStanzaTimeout(int responseStanzaTimeout) {
        this.responseStanzaTimeout = responseStanzaTimeout;
    }

    /**
	 * An enumeration for TLS security modes that are available when
	 * making a connection to the XMPP server.
	 */
    public static enum SecurityMode {

        /**
		 * Securirty via TLS encryption is required in order to
		 * connect. If the server does not offer TLS or if the TLS
		 * negotiaton fails, the connection to the server will fail.
		 */
        required, /**
		 * Security via TLS encryption is used whenever it's
		 * available. This is the default setting.
		 */
        enabled, /**
		 * Security via TLS encryption is disabled and only
		 * un-encrypted connections will be used. If only TLS
		 * encryption is available from the server, the connection
		 * will fail.
		 */
        disabled
    }
}
