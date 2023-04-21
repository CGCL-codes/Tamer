package org.apache.ws.commons.tcpmon.core.filter.http;

public class HostRewriter extends AbstractHttpRequestResponseHandler {

    private static final String[] rewritableRequestHeaders = { "Referer" };

    private static final String[] rewritableResponseHeaders = { "Location" };

    private final String targetHost;

    private final int targetPort;

    private final String targetBaseUri;

    private String orgBaseUri;

    public HostRewriter(String targetHost, int targetPort, boolean isSecure) {
        this.targetHost = targetHost;
        this.targetPort = targetPort;
        StringBuilder buffer = new StringBuilder();
        buffer.append(isSecure ? "https" : "http");
        buffer.append("://");
        buffer.append(targetHost);
        if ((!isSecure && targetPort != 80) || (isSecure && targetPort != 443)) {
            buffer.append(":");
            buffer.append(targetPort);
        }
        buffer.append("/");
        targetBaseUri = buffer.toString();
    }

    public String getTargetBaseUri() {
        return targetBaseUri;
    }

    public String getOrgBaseUri() {
        return orgBaseUri;
    }

    private void rewriteUriHeaders(Headers headers, String[] names, String fromBaseUri, String toBaseUri) {
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            Header header = headers.getFirst(name);
            if (header != null) {
                String value = header.getValue();
                if (value.startsWith(fromBaseUri)) {
                    headers.set(name, toBaseUri + value.substring(fromBaseUri.length()));
                }
            }
        }
    }

    @Override
    public void processRequestHeaders(Headers headers) {
        Header header = headers.getFirst("Host");
        if (header != null) {
            orgBaseUri = "http://" + header.getValue() + "/";
            headers.set("Host", targetHost + ":" + targetPort);
            rewriteUriHeaders(headers, rewritableRequestHeaders, orgBaseUri, targetBaseUri);
        }
    }

    @Override
    public void processResponseHeaders(Headers headers) {
        if (orgBaseUri != null) {
            rewriteUriHeaders(headers, rewritableResponseHeaders, targetBaseUri, orgBaseUri);
        }
    }
}
