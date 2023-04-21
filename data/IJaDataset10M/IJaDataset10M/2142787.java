package net.sourceforge.openconferencer.client.jabber;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.Iterator;
import java.util.List;
import net.sourceforge.openconferencer.client.Activator;
import net.sourceforge.openconferencer.client.util.LogHelper;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.proxy.ProxyInfo;
import org.jivesoftware.smack.proxy.ProxyInfo.ProxyType;

/**
 * @author aleksandar
 * 
 */
public class JabberConfiguration {

    private ConnectionConfiguration connectionConfiguration;

    private ProxyInfo proxy;

    private boolean useProxy = false;

    private String username = "";

    private String password = "";

    private String domainName = null;

    /**
	 * 
	 */
    public void load() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        String username = store.getString(JabberConstants.PREFERENCE_USERNAME);
        String password = store.getString(JabberConstants.PREFERENCE_PASSWORD);
        String server = store.getString(JabberConstants.PREFERENCE_SERVER);
        Integer port = store.getInt(JabberConstants.PREFERENCE_PORT);
        useProxy = store.getBoolean(JabberConstants.PREFERENCE_USE_PROXY);
        String proxyServer = store.getString(JabberConstants.PREFERENCE_PROXY_SERVER);
        int proxyPort = store.getInt(JabberConstants.PREFERENCE_PROXY_PORT);
        String proxyUsername = store.getString(JabberConstants.PREFERENCE_PROXY_USERNAME);
        String proxyPassword = store.getString(JabberConstants.PREFERENCE_PROXY_PASSWORD);
        if (!"".equals(proxyServer)) {
            ProxyType proxyType = ProxyType.HTTP;
            String proxyTypeValue = store.getString(JabberConstants.PREFERENCE_PROXY_TYPE);
            try {
                if (proxyTypeValue != null && !"".equals(proxyTypeValue)) proxyType = ProxyType.valueOf(proxyTypeValue);
            } catch (Exception ex) {
                LogHelper.warn("Invalid Jabber proxy type value found in preferences: " + proxyTypeValue, ex);
            }
            proxy = new ProxyInfo(proxyType, proxyServer, proxyPort, proxyUsername, proxyPassword);
        } else {
            proxy = detectProxySettings();
        }
        if (!"".equals(server)) {
            boolean compression = store.getBoolean(JabberConstants.PREFERENCE_COMPRESSION);
            boolean sasl = store.getBoolean(JabberConstants.PREFERENCE_SASL);
            if (useProxy) connectionConfiguration = new ConnectionConfiguration(server, port, proxy); else connectionConfiguration = new ConnectionConfiguration(server, port);
            connectionConfiguration.setCompressionEnabled(compression);
            connectionConfiguration.setSASLAuthenticationEnabled(sasl);
            this.username = username;
            this.password = password;
        } else {
            connectionConfiguration = new ConnectionConfiguration("", 5222);
            connectionConfiguration.setCompressionEnabled(true);
            connectionConfiguration.setSASLAuthenticationEnabled(true);
        }
        connectionConfiguration.setSelfSignedCertificateEnabled(false);
        connectionConfiguration.setNotMatchingDomainCheckEnabled(false);
        connectionConfiguration.setExpiredCertificatesCheckEnabled(false);
    }

    /**
	 * 
	 */
    public void save() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setValue(JabberConstants.PREFERENCE_USERNAME, getUsername());
        store.setValue(JabberConstants.PREFERENCE_PASSWORD, getPassword());
        store.setValue(JabberConstants.PREFERENCE_SERVER, connectionConfiguration.getHost());
        store.setValue(JabberConstants.PREFERENCE_PORT, connectionConfiguration.getPort());
        store.setValue(JabberConstants.PREFERENCE_COMPRESSION, connectionConfiguration.isCompressionEnabled());
        store.setValue(JabberConstants.PREFERENCE_SASL, connectionConfiguration.isSASLAuthenticationEnabled());
        store.setValue(JabberConstants.PREFERENCE_USE_PROXY, useProxy);
        store.setValue(JabberConstants.PREFERENCE_PROXY_TYPE, proxy.getProxyType().name());
        store.setValue(JabberConstants.PREFERENCE_PROXY_SERVER, proxy.getProxyAddress());
        store.setValue(JabberConstants.PREFERENCE_PROXY_PORT, proxy.getProxyPort());
        store.setValue(JabberConstants.PREFERENCE_PROXY_USERNAME, proxy.getProxyUsername());
        store.setValue(JabberConstants.PREFERENCE_PROXY_PASSWORD, proxy.getProxyPassword());
    }

    /**
	 * 
	 * @return
	 */
    protected ProxyInfo detectProxySettings() {
        try {
            System.setProperty("java.net.useSystemProxies", "true");
            List<Proxy> proxyList = ProxySelector.getDefault().select(new URI("http://www.google.com/"));
            Iterator<Proxy> i = proxyList.iterator();
            while (i.hasNext()) {
                Proxy proxy = (Proxy) i.next();
                InetSocketAddress addr = (InetSocketAddress) proxy.address();
                if (addr != null) {
                    ProxyType type = ProxyType.NONE;
                    switch(proxy.type()) {
                        case HTTP:
                            type = ProxyType.HTTP;
                            break;
                        case SOCKS:
                            type = ProxyType.SOCKS5;
                            break;
                    }
                    return new ProxyInfo(type, addr.getHostName(), addr.getPort(), "", "");
                }
            }
        } catch (Exception ex) {
            LogHelper.warn("Failed to detect connection proxy.", ex);
        }
        return null;
    }

    /**
	 * @return the connectionConfiguration
	 */
    public ConnectionConfiguration getConnectionConfiguration() {
        return connectionConfiguration;
    }

    /**
	 * @param connectionConfiguration
	 *            the connectionConfiguration to set
	 */
    public void setConnectionConfiguration(ConnectionConfiguration connectionConfiguration) {
        this.connectionConfiguration = connectionConfiguration;
    }

    /**
	 * @return the username
	 */
    public String getUsername() {
        return username;
    }

    /**
	 * @param username
	 *            the username to set
	 */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
	 * @return the password
	 */
    public String getPassword() {
        return password;
    }

    /**
	 * @param password
	 *            the password to set
	 */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
	 * @return the proxy
	 */
    public ProxyInfo getProxy() {
        return proxy;
    }

    /**
	 * @param proxy
	 *            the proxy to set
	 */
    public void setProxy(ProxyInfo proxy) {
        this.proxy = proxy;
    }

    /**
	 * @return the useProxy
	 */
    public boolean isUseProxy() {
        return useProxy;
    }

    /**
	 * @param useProxy
	 *            the useProxy to set
	 */
    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    /**
	 * @return the domainName
	 */
    public String getDomainName() {
        return domainName;
    }

    /**
	 * @param domainName
	 *            the domainName to set
	 */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }
}
