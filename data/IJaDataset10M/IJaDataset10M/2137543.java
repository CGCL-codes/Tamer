package com.unitt.framework.websocket;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Josh Morris
 */
public class WebSocketConnectConfig {

    public enum WebSocketVersion {

        Version07("7"), Version08("8"), Version10("8"), VersionRfc6455("13");

        private String specVersionValue;

        private WebSocketVersion(String aSpecVersionValue) {
            specVersionValue = aSpecVersionValue;
        }

        public String getSpecVersionValue() {
            return specVersionValue;
        }

        public static WebSocketVersion fromSpecVersionValue(String aSpecVersionValue) {
            for (WebSocketVersion wsv : WebSocketVersion.values()) {
                if (wsv.getSpecVersionValue().equals(aSpecVersionValue)) {
                    return wsv;
                }
            }
            return null;
        }
    }

    ;

    private URI url;

    private String host;

    private String origin;

    private boolean useOrigin;

    private long timeoutInMillis;

    private boolean verifyTlsDomain;

    private List<String> availableProtocols;

    private List<List<String>> availableExtensions;

    private List<String> selectedExtensions;

    private List<HandshakeHeader> clientHeaders;

    private List<HandshakeHeader> serverHeaders;

    private String selectedProtocol;

    private boolean verifySecurityKey;

    private int maxPayloadSize = 32 * 1024;

    private String proxyHost;

    private int proxyPort = -1;

    private WebSocketVersion webSocketVersion = WebSocketVersion.VersionRfc6455;

    public WebSocketConnectConfig() {
    }

    public URI getUrl() {
        return url;
    }

    public void setUrl(URI aUrl) {
        url = aUrl;
        if (getOrigin() == null) {
            setOrigin(buildOrigin());
        }
        if (getHost() == null) {
            setHost(buildHost());
        }
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String aOrigin) {
        origin = aOrigin;
    }

    public boolean isSecure() {
        return getUrl() != null && getUrl().getScheme().equalsIgnoreCase("wss");
    }

    public long getTimeoutInMillis() {
        return timeoutInMillis;
    }

    public void setTimeoutInMillis(long aTimeoutInMillis) {
        timeoutInMillis = aTimeoutInMillis;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String aHost) {
        host = aHost;
    }

    public List<HandshakeHeader> getClientHeaders() {
        return clientHeaders;
    }

    public void setClientHeaders(List<HandshakeHeader> aClientHeaders) {
        clientHeaders = aClientHeaders;
    }

    public List<HandshakeHeader> getServerHeaders() {
        return serverHeaders;
    }

    public void setServerHeaders(List<HandshakeHeader> aServerHeaders) {
        serverHeaders = aServerHeaders;
    }

    public boolean isVerifyTlsDomain() {
        return verifyTlsDomain;
    }

    public void setVerifyTlsDomain(boolean aVerifyTlsDomain) {
        verifyTlsDomain = aVerifyTlsDomain;
    }

    public List<String> getAvailableProtocols() {
        return availableProtocols;
    }

    public boolean getUseOrigin() {
        return useOrigin;
    }

    public void setUseOrigin(boolean aUseOrigin) {
        useOrigin = aUseOrigin;
    }

    public void setAvailableProtocol(String aAvailableProtocol) {
        availableProtocols = new ArrayList<String>();
        availableProtocols.add(aAvailableProtocol);
    }

    public void setAvailableProtocols(List<String> aAvailableProtocols) {
        availableProtocols = aAvailableProtocols;
    }

    public void addAvailableExtension(String aExtension) {
        if (availableExtensions == null) {
            availableExtensions = new ArrayList<List<String>>();
        }
        String[] items = null;
        if (aExtension.contains(",")) {
            items = aExtension.split(",");
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].trim();
            }
        } else {
            items = new String[] { aExtension.trim() };
        }
        availableExtensions.add(Arrays.asList(items));
    }

    public void addAvailableExtensions(List<String> aExtensions) {
        if (availableExtensions == null) {
            availableExtensions = new ArrayList<List<String>>();
        }
        availableExtensions.add(aExtensions);
    }

    public List<List<String>> getAvailableExtensions() {
        return availableExtensions;
    }

    public void setAvailableExtensions(List<List<String>> aAvailableExtensions) {
        availableExtensions = aAvailableExtensions;
    }

    public List<String> getSelectedExtensions() {
        return selectedExtensions;
    }

    public void setSelectedExtensions(List<String> aSelectedExtensions) {
        selectedExtensions = aSelectedExtensions;
    }

    public String getSelectedProtocol() {
        return selectedProtocol;
    }

    public void setSelectedProtocol(String aSelectedProtocol) {
        selectedProtocol = aSelectedProtocol;
    }

    public boolean isVerifySecurityKey() {
        return verifySecurityKey;
    }

    public void setVerifySecurityKey(boolean aVerifyHandshake) {
        verifySecurityKey = aVerifyHandshake;
    }

    public int getMaxPayloadSize() {
        return maxPayloadSize;
    }

    public void setMaxPayloadSize(int aMaxPayloadSize) {
        if (aMaxPayloadSize > 0) {
            maxPayloadSize = aMaxPayloadSize;
        }
    }

    public WebSocketVersion getWebSocketVersion() {
        return webSocketVersion;
    }

    public void setWebSocketVersion(WebSocketVersion aWebSocketVersion) {
        webSocketVersion = aWebSocketVersion;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String aProxyHost) {
        proxyHost = aProxyHost;
    }

    public int getProxyPort() {
        if (proxyPort < 0 && getUrl() != null) {
            proxyPort = getUrl().getPort();
        }
        return proxyPort;
    }

    public void setProxyPort(int aProxyPort) {
        proxyPort = aProxyPort;
    }

    public boolean hasProxy() {
        return getProxyHost() != null;
    }

    protected String buildOrigin() {
        return (isSecure() ? "https://" : "http://") + buildHost() + (getUrl().getPath() != null && getUrl().getPath().length() > 0 ? getUrl().getPath() : "");
    }

    protected String buildHost() {
        if (getUrl() != null) {
            if (getUrl().getPort() != 80 && getUrl().getPort() != 443) {
                return getUrl().getHost() + ":" + getUrl().getPort();
            }
            return getUrl().getHost();
        }
        return null;
    }
}
