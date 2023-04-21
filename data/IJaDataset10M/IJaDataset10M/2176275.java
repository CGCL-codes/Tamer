package ntorrent.connection.model;

import java.net.Proxy.Type;

public class ProxyProfileImpl implements ProxyProfile {

    private static final long serialVersionUID = 1L;

    private String host = null;

    private String password = null;

    private Integer port = null;

    private Type proxyType = Type.DIRECT;

    private String username = null;

    private boolean usingAuth = false;

    @Override
    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Integer getPort() {
        return this.port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    @Override
    public Type getProxyType() {
        return this.proxyType;
    }

    public void setProxyType(Type proxyType) {
        this.proxyType = proxyType;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean usingAuthentication() {
        return this.usingAuth;
    }

    public void setUsingAuthentication(boolean usingAuth) {
        this.usingAuth = usingAuth;
    }

    @Override
    public ProxyProfile getClonedInstance() throws CloneNotSupportedException {
        return (ProxyProfile) this.clone();
    }

    @Override
    public String toString() {
        String out = "Proxy=" + this.proxyType + " - ";
        if (usingAuth) {
            out += this.username + "@";
        }
        if (this.host != null && this.port != null) {
            out += this.host + ":" + this.port;
        }
        return out;
    }
}
