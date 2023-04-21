package de.juwimm.cms.http;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * helper class for setting the JDK1.4- system properties for JDK1.4- libraries with the help of JDK1.5-ProxySelector
 * @author toerberj
 *
 */
public class ProxyHelper {

    private static Logger log = Logger.getLogger(ProxyHelper.class);

    /** list of all proxies for this.uri */
    private List<Proxy> proxylist = null;

    /** list of all http-proxies */
    private List<Proxy> httpProxies = null;

    /** list of all socks-proxies */
    private List<Proxy> socksProxies = null;

    private Proxy httpProxy = null;

    private Proxy socksProxy = null;

    private URI uri = null;

    public ProxyHelper() {
        resetProxies();
    }

    public void setHttpProxyUser(String username, String password) {
        if (log.isDebugEnabled()) log.debug("setHttpProxyUser(" + username + ", " + password + ") begin");
        System.setProperty("http.proxyUser", username);
        System.setProperty("http.proxyPassword", password);
    }

    /**
	 * 
	 * @param dest 				must not be null because ProxySelector.select(URI) then throws IllegalArgumentException
	 * @param useSystemProxies	set java system property java.net.useSystemProxies before getting list of proxies?  
	 * @throws IllegalArgumentException
	 */
    public void init(URI dest, boolean useSystemProxies) throws IllegalArgumentException {
        if (log.isDebugEnabled()) log.debug("init(" + dest + ", " + useSystemProxies + ") begin");
        ProxySelector proxysel = ProxySelector.getDefault();
        System.setProperty("java.net.useSystemProxies", useSystemProxies ? "true" : "false");
        resetProxies();
        this.proxylist = proxysel.select(dest);
        setUri(dest);
        this.httpProxies = new ArrayList<Proxy>();
        this.socksProxies = new ArrayList<Proxy>();
        this.httpProxy = Proxy.NO_PROXY;
        this.socksProxy = Proxy.NO_PROXY;
        for (Proxy proxy : this.proxylist) {
            if (log.isDebugEnabled()) log.debug(proxy.toString());
            if (proxy.type() == Proxy.Type.DIRECT) {
            } else if (proxy.type() == Proxy.Type.HTTP) {
                this.httpProxies.add(proxy);
                this.httpProxy = proxy;
            } else if (proxy.type() == Proxy.Type.SOCKS) {
                this.socksProxies.add(proxy);
                this.socksProxy = proxy;
            }
        }
    }

    /**
	 * 
	 * @param hostname
	 * @param port
	 */
    public void setFtpProxySystemProperties(String hostname, String port) {
        if (log.isDebugEnabled()) log.debug("setFtpProxySystemProxies(" + hostname + ", " + port + ") begin");
        boolean set = hostname.length() > 0;
        if (!set) {
            port = "";
        }
        System.setProperty("ftp.proxyHost", hostname);
        System.setProperty("ftp.proxyPort", port);
        System.setProperty("ftp.nonProxyHosts", "");
    }

    /**
	 * not yet supported nor tested
	 * assuming http proxy is also ftp proxy
	 *
	 */
    public void setFtpProxySystemProperties(boolean reset) {
        if (log.isDebugEnabled()) log.debug("setFtpProxySystemProxies(" + reset + ") begin");
        String hostname = "";
        String port = "";
        if (!reset && this.httpProxy != null) {
            SocketAddress addr = this.httpProxy.address();
            if (addr instanceof InetSocketAddress) {
                InetSocketAddress inaddr = (InetSocketAddress) addr;
                hostname = inaddr.getHostName();
                port = String.valueOf(inaddr.getPort());
            }
        }
        setFtpProxySystemProperties(hostname, port);
    }

    /**
	 * 
	 * @param hostname
	 * @param port
	 */
    public void setHttpProxySystemProperties(String hostname, String port) {
        if (log.isDebugEnabled()) log.debug("setHttpProxySystemProxies(" + hostname + ", " + port + ") begin");
        boolean set = false;
        set = hostname.length() > 0;
        System.setProperty("proxySet", set ? "true" : "false");
        System.setProperty("http.proxySet", set ? "true" : "false");
        System.setProperty("https.proxySet", set ? "true" : "false");
        System.setProperty("http.proxyHost", hostname);
        System.setProperty("http.proxyPort", port);
        System.setProperty("https.proxyHost", hostname);
        System.setProperty("https.proxyPort", port);
        System.setProperty("proxyHost", hostname);
        System.setProperty("proxyPort", port);
        System.setProperty("http.nonProxyHosts", "");
    }

    /**
	 * set http and https System-Properties
	 */
    public void setHttpProxySystemProperties(boolean reset) {
        if (log.isDebugEnabled()) log.debug("setHttpProxySystemProxies(" + reset + ") begin");
        String hostname = "";
        String port = "";
        if (!reset && this.httpProxy != null) {
            SocketAddress addr = this.httpProxy.address();
            if (addr instanceof InetSocketAddress) {
                InetSocketAddress inaddr = (InetSocketAddress) addr;
                hostname = inaddr.getHostName();
                port = String.valueOf(inaddr.getPort());
            }
        }
        setHttpProxySystemProperties(hostname, port);
    }

    public void setSocksProxySystemProperties(String hostname, String port) {
        if (log.isDebugEnabled()) log.debug("setSocksProxySystemProxies(" + hostname + ", " + port + ") begin");
        boolean set = hostname.length() > 0;
        if (!set) {
            port = "";
        }
        System.setProperty("socksProxyHost", hostname);
        System.setProperty("socksProxyPort", port);
    }

    public void setSocksProxySystemProperties(boolean reset) {
        if (log.isDebugEnabled()) log.debug("setSocksProxySystemProxies(" + reset + ") begin");
        String hostname = "";
        String port = "";
        if (!reset && this.socksProxy != null) {
            SocketAddress addr = this.socksProxy.address();
            if (addr instanceof InetSocketAddress) {
                InetSocketAddress inaddr = (InetSocketAddress) addr;
                hostname = inaddr.getHostName();
                port = String.valueOf(inaddr.getPort());
            }
        }
        setSocksProxySystemProperties(hostname, port);
    }

    public void setSystemProperties() {
        if (log.isDebugEnabled()) log.debug("setSystemProxies() begin");
        setHttpProxySystemProperties(false);
        setSocksProxySystemProperties(false);
        setFtpProxySystemProperties(false);
    }

    private void resetProxies() {
        if (log.isDebugEnabled()) log.debug("resetProxies() begin");
        if (this.proxylist != null) this.proxylist.clear();
        if (this.httpProxies != null) this.httpProxies.clear();
        if (this.socksProxies != null) this.socksProxies.clear();
        this.proxylist = null;
        this.httpProxies = null;
        this.socksProxies = null;
        this.httpProxy = null;
        this.socksProxy = null;
    }

    /**
	 * @return Returns the uri.
	 */
    public URI getUri() {
        return uri;
    }

    /**
	 * @param uri The uri to set.
	 */
    private void setUri(URI uri) {
        this.uri = uri;
    }

    /**
	 * 
	 * @return last proxy of type Proxy.Type.HTTP or null if never with destination-URL initialized
	 */
    public Proxy getHttpProxy() {
        return this.httpProxy;
    }

    /**
	 * 
	 * @return last proxy of type Proxy.Type.SOCKS or null if never with destination-URL initialized
	 */
    public Proxy getSocksProxy() {
        return this.socksProxy;
    }

    /**
	 * 
	 * @return proxies of type Proxy.Type.HTTP or null if not yet initialized or initialization failed 
	 */
    public List<Proxy> getHttpProxies() {
        return this.httpProxies;
    }

    /**
	 * 
	 * @return proxies of type Proxy.Type.SOCKS or null if not yet initialized or initialization failed 
	 */
    public List<Proxy> getSocksProxies() {
        return this.socksProxies;
    }
}
