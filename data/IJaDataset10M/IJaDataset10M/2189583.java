package com.aelitis.azureus.core.proxy.socks;

/**
 * @author parg
 *
 */
public interface AESocksProxy {

    public static final String PV_4 = "V4";

    public static final String PV_4a = "V4a";

    public static final String PV_5 = "V5";

    public int getPort();

    public AESocksProxyPlugableConnection getDefaultPlugableConnection(AESocksProxyConnection basis);

    /**
		 * Set the next SOCKS proxy in a chain - i.e. this socks proxy's default plugable connection
		 * will connect onwards using this SOCKS proxy
		 *  
		 * @param host
		 * @param port
		 */
    public void setNextSOCKSProxy(String host, int port, String proxy_version);

    public String getNextSOCKSProxyHost();

    public int getNextSOCKSProxyPort();

    public String getNextSOCKSProxyVersion();

    public void setAllowExternalConnections(boolean permit);
}
