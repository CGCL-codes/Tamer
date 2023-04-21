package com.lechucksoftware.proxy.lib.reflection.android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Default implementation for {@code ProxySelector}.
 */
@SuppressWarnings("unchecked")
class ProxySelectorImpl extends ProxySelector {

    private static final int HTTP_PROXY_PORT = 80;

    private static final int HTTPS_PROXY_PORT = 443;

    private static final int FTP_PROXY_PORT = 80;

    private static final int SOCKS_PROXY_PORT = 1080;

    private static Properties netProps = null;

    static {
        AccessController.doPrivileged(new java.security.PrivilegedAction() {

            public Object run() {
                File f = new File(System.getProperty("java.home") + File.separator + "lib" + File.separator + "net.properties");
                if (f.exists()) {
                    try {
                        FileInputStream fis = new FileInputStream(f);
                        InputStream is = new BufferedInputStream(fis);
                        netProps = new Properties();
                        netProps.load(is);
                        is.close();
                    } catch (IOException e) {
                    }
                }
                return null;
            }
        });
    }

    public ProxySelectorImpl() {
        super();
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        if (uri == null || sa == null || ioe == null) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public List<Proxy> select(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("uri == null");
        }
        String scheme = uri.getScheme();
        if (null == scheme) {
            throw new IllegalArgumentException();
        }
        String host = uri.getHost();
        Proxy proxy = Proxy.NO_PROXY;
        if ("http".equals(scheme)) {
            proxy = selectHttpProxy(host);
        } else if ("https".equals(scheme)) {
            proxy = selectHttpsProxy();
        } else if ("ftp".equals(scheme)) {
            proxy = selectFtpProxy(host);
        } else if ("socket".equals(scheme)) {
            proxy = selectSocksProxy();
        }
        List<Proxy> proxyList = new ArrayList<Proxy>(1);
        proxyList.add(proxy);
        return proxyList;
    }

    private Proxy selectHttpProxy(String uriHost) {
        String host;
        String port = null;
        Proxy.Type type = Proxy.Type.DIRECT;
        String nonProxyHosts = getSystemProperty("http.nonProxyHosts");
        if (isNonProxyHost(uriHost, nonProxyHosts)) {
            return Proxy.NO_PROXY;
        }
        host = getSystemProperty("http.proxyHost");
        if (null != host) {
            type = Proxy.Type.HTTP;
            port = getSystemPropertyOrAlternative("http.proxyPort", "proxyPort", String.valueOf(HTTP_PROXY_PORT));
        } else if ((host = getSystemProperty("proxyHost", null)) != null) {
            type = Proxy.Type.HTTP;
            port = getSystemPropertyOrAlternative("proxyPort", "http.proxyPort", String.valueOf(HTTP_PROXY_PORT));
        } else if ((host = getSystemProperty("socksProxyHost")) != null) {
            type = Proxy.Type.SOCKS;
            port = getSystemProperty("socksProxyPort", String.valueOf(SOCKS_PROXY_PORT));
        }
        int defaultPort = (type == Proxy.Type.SOCKS) ? SOCKS_PROXY_PORT : HTTP_PROXY_PORT;
        return createProxy(type, host, port, defaultPort);
    }

    private Proxy selectHttpsProxy() {
        String host;
        String port = null;
        Proxy.Type type = Proxy.Type.DIRECT;
        host = getSystemProperty("https.proxyHost");
        if (null != host) {
            type = Proxy.Type.HTTP;
            port = getSystemProperty("https.proxyPort", String.valueOf(HTTPS_PROXY_PORT));
        } else {
            host = getSystemProperty("socksProxyHost");
            if (null != host) {
                type = Proxy.Type.SOCKS;
                port = getSystemProperty("socksProxyPort", String.valueOf(SOCKS_PROXY_PORT));
            }
        }
        int defaultPort = (type == Proxy.Type.SOCKS) ? SOCKS_PROXY_PORT : HTTPS_PROXY_PORT;
        return createProxy(type, host, port, defaultPort);
    }

    private Proxy selectFtpProxy(String uriHost) {
        String host;
        String port = null;
        Proxy.Type type = Proxy.Type.DIRECT;
        String nonProxyHosts = getSystemProperty("ftp.nonProxyHosts");
        if (isNonProxyHost(uriHost, nonProxyHosts)) {
            return Proxy.NO_PROXY;
        }
        host = getSystemProperty("ftp.proxyHost");
        if (null != host) {
            type = Proxy.Type.HTTP;
            port = getSystemProperty("ftp.proxyPort", String.valueOf(FTP_PROXY_PORT));
        } else {
            host = getSystemProperty("socksProxyHost");
            if (null != host) {
                type = Proxy.Type.SOCKS;
                port = getSystemProperty("socksProxyPort", String.valueOf(SOCKS_PROXY_PORT));
            }
        }
        int defaultPort = (type == Proxy.Type.SOCKS) ? SOCKS_PROXY_PORT : FTP_PROXY_PORT;
        return createProxy(type, host, port, defaultPort);
    }

    private Proxy selectSocksProxy() {
        String host;
        String port = null;
        Proxy.Type type = Proxy.Type.DIRECT;
        host = getSystemProperty("socksProxyHost");
        if (null != host) {
            type = Proxy.Type.SOCKS;
            port = getSystemProperty("socksProxyPort", String.valueOf(SOCKS_PROXY_PORT));
        }
        return createProxy(type, host, port, SOCKS_PROXY_PORT);
    }

    private boolean isNonProxyHost(String host, String nonProxyHosts) {
        if (null == host || null == nonProxyHosts) {
            return false;
        }
        int length = nonProxyHosts.length();
        char ch;
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < nonProxyHosts.length(); i++) {
            ch = nonProxyHosts.charAt(i);
            switch(ch) {
                case '.':
                    buf.append("\\.");
                    break;
                case '*':
                    buf.append(".*");
                    break;
                default:
                    buf.append(ch);
            }
        }
        String nonProxyHostsReg = buf.toString();
        return host.matches(nonProxyHostsReg);
    }

    private Proxy createProxy(Proxy.Type type, String host, String port, int defaultPort) {
        Proxy proxy;
        if (type == Proxy.Type.DIRECT) {
            proxy = Proxy.NO_PROXY;
        } else {
            int iPort;
            try {
                iPort = Integer.parseInt(port);
            } catch (NumberFormatException e) {
                iPort = defaultPort;
            }
            proxy = new Proxy(type, InetSocketAddress.createUnresolved(host, iPort));
        }
        return proxy;
    }

    private String getSystemProperty(final String property) {
        return getSystemProperty(property, null);
    }

    private String getSystemProperty(final String property, final String defaultValue) {
        return System.getProperty(property, defaultValue);
    }

    private String getSystemPropertyOrAlternative(final String key, final String alternativeKey, final String defaultValue) {
        String value = getSystemProperty(key);
        if (value == null) {
            value = getSystemProperty(alternativeKey);
            if (null == value) {
                value = defaultValue;
            }
        }
        return value;
    }
}
