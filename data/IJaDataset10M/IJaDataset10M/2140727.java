package org.szegedi.nioserver.protocols.http;

/**
 * Adapter interface is a bridging between HttpProtocolHandler and a concrete
 * facility that processes the request and generates a response (like a
 * servlet container implementation, or an implementation that serves static
 * files from the filesystem, and so on).
 */
public interface Adapter {

    public void serviceRequest(NioHttpRequest request);
}
