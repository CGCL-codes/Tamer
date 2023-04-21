package org.apache.ws.commons.tcpmon.core.filter.http;

import java.util.LinkedList;
import java.util.List;

/**
 * Filter that parses HTTP responses and invokes a set of {@link HTTPResponseHandler}
 * implementations.
 */
public class HttpResponseFilter extends HttpFilter {

    private final List<HttpResponseHandler> handlers = new LinkedList<HttpResponseHandler>();

    public HttpResponseFilter(boolean decodeTransferEncoding) {
        super(decodeTransferEncoding);
    }

    public void addHandler(HttpResponseHandler handler) {
        handlers.add(handler);
    }

    @Override
    protected String processFirstLine(String firstLine) {
        for (HttpResponseHandler handler : handlers) {
            firstLine = handler.processResponseLine(firstLine);
        }
        return firstLine;
    }

    @Override
    protected void processHeaders(Headers headers) {
        for (HttpResponseHandler handler : handlers) {
            handler.processResponseHeaders(headers);
        }
    }

    @Override
    protected void completed() {
        for (HttpResponseHandler handler : handlers) {
            handler.responseCompleted();
        }
    }
}
