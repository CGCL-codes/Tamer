package org.apache.ws.commons.tcpmon.core.filter.http;

/**
 * Handler that rewrites a plain HTTP request to an HTTP proxy request.
 */
public class HttpProxyClientHandler extends AbstractHttpRequestHandler {

    private final String targetHost;

    private final int targetPort;

    public HttpProxyClientHandler(String targetHost, int targetPort) {
        this.targetHost = targetHost;
        this.targetPort = targetPort;
    }

    @Override
    public String processRequestLine(String requestLine) {
        String[] parts = requestLine.split(" ");
        return parts[0] + " http://" + targetHost + ":" + targetPort + parts[1] + " " + parts[2];
    }
}
