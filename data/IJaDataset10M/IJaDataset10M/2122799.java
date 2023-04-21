package net.sf.amemailchecker.proxy;

import net.sf.amemailchecker.app.model.ProxySettings;
import net.sf.amemailchecker.mail.Protocol;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;

public class ProxyDispatcher {

    private static ProxyDispatcher instance;

    public static ProxyDispatcher getDispatcher() {
        if (instance == null) instance = new ProxyDispatcher();
        return instance;
    }

    private ProxySettings proxySettings;

    public Proxy getProxy() {
        if (proxySettings == null || !proxySettings.isUseProxy()) return Proxy.NO_PROXY;
        Proxy.Type proxyType = proxySettings.isUseProxy() ? determineProxyType(proxySettings.getServerSettings().getProtocolType()) : Proxy.Type.DIRECT;
        SocketAddress socketAddress = proxySettings.isUseProxy() ? new InetSocketAddress(proxySettings.getServerSettings().getHost(), proxySettings.getServerSettings().getPort()) : null;
        if (proxySettings.isUseAuthentication()) Authenticator.setDefault(new ProxyAuthenticator(proxySettings.getUserCredentials()));
        return new Proxy(proxyType, socketAddress);
    }

    public Proxy.Type determineProxyType(int protocolType) {
        Protocol protocol = Protocol.getById(protocolType);
        return (protocol.equals(Protocol.SOCKS)) ? Proxy.Type.SOCKS : Proxy.Type.HTTP;
    }

    public ProxySettings getProxySettings() {
        return proxySettings;
    }

    public void setProxySettings(ProxySettings proxySettings) {
        this.proxySettings = proxySettings;
    }
}
