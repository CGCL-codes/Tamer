package org.xeustechnologies.googleapi.spelling;

import java.net.Proxy.Type;

/**
 * This class holds extra configuration, which is not part of Google's Spell
 * check service, but may be required by this API
 * 
 * @author Kamran Zafar
 * 
 */
public class Configuration {

    private String proxyHost;

    private int proxyPort;

    private String proxyScheme;

    private boolean proxyEnabled = false;

    public void setProxy(String proxyHost, int proxyPort, String proxyScheme) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.proxyScheme = proxyScheme;
        proxyEnabled = true;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public int getProxyPort() {
        return proxyPort;
    }

    public Type getProxyScheme() {
        return Type.valueOf(proxyScheme.toUpperCase());
    }

    public boolean isProxyEnabled() {
        return proxyEnabled;
    }
}
