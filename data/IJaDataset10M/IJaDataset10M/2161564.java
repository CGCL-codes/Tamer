package org.tamacat.httpd.config;

import java.net.MalformedURLException;
import java.net.URL;
import org.tamacat.httpd.core.HttpHandler;

/**
 * <p>It is setting of the service URL.
 */
public class ServiceUrl {

    private URL host;

    private String handlerName;

    private String path;

    private ReverseUrl reverseUrl;

    private UrlType type;

    private ServerConfig serverConfig;

    private String loadBalancerMethod = "RoundRobin";

    /**
	 * <p>Constructor for ServiceConfig.
	 * @param serverConfig
	 */
    public ServiceUrl(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    /**
	 * <p>Default Constructor for ServiceConfig.
	 */
    public ServiceUrl() {
    }

    /**
	 * <p>Returns the URL of host.
	 * @return URL of host.
	 */
    public URL getHost() {
        return host;
    }

    /**
	 * <p>Returns the URL path.
	 * @return URL path
	 */
    public String getPath() {
        return path;
    }

    /**
	 * <p>Returns the {@link ReverseUrl}. 
	 * @return ReverseUrl
	 */
    public ReverseUrl getReverseUrl() {
        return reverseUrl;
    }

    /**
	 * <p>Returns the {@link ServiceConfig}.
	 * @return ServerConfig
	 */
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    /**
	 * <p>Returns the type of Service URL.
	 * @return UrlType
	 */
    public UrlType getType() {
        return type;
    }

    /**
	 * <p>Check the {@code ServiceType}.
	 * @param type
	 * @return if ServiceType is equals, returns true
	 */
    public boolean isType(UrlType type) {
        if (this.type == null) return false;
        return this.type.equals(type);
    }

    /**
	 * <p>Set the ServiceConfig
	 * @param serviceConfig
	 */
    public void setServiceUrl(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    /**
	 * <p>Set the URL of host. (The path is deleted)
	 * @param host
	 */
    public void setHost(URL host) {
        if (host != null) {
            try {
                this.host = new URL(host.getProtocol(), host.getHost(), host.getPort(), "");
            } catch (MalformedURLException e) {
            }
        }
    }

    /**
	 * <p>Set the URL path. (The path is deleted)
	 * @param path
	 */
    public void setPath(String path) {
        this.path = path;
    }

    /**
	 * <p>Set the {@link ReverseURL}.
	 * @param reverseUrl
	 */
    public void setReverseUrl(ReverseUrl reverseUrl) {
        this.reverseUrl = reverseUrl;
    }

    /**
	 * <p>Set the {@link UrlType}.
	 * @param type
	 */
    public void setType(UrlType type) {
        this.type = type;
    }

    /**
	 * <p>Returns the handler name.
	 */
    public String getHandlerName() {
        return handlerName;
    }

    /**
	 * <p>Set the name of the {@link HttpHandler}.
	 * @param handlerName
	 */
    public void setHandlerName(String handlerName) {
        this.handlerName = handlerName;
    }

    public String getLoadBalancerMethod() {
        return loadBalancerMethod;
    }

    public void setLoadBalancerMethod(String loadBalancerMethod) {
        this.loadBalancerMethod = loadBalancerMethod;
    }
}
