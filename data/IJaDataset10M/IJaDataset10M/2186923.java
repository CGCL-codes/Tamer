package org.apache.http.impl.conn;

import java.io.IOException;
import org.apache.http.HttpHost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * Mockup connection adapter.
 */
public class ClientConnAdapterMockup extends AbstractClientConnAdapter {

    public ClientConnAdapterMockup(ClientConnectionManager mgr) {
        super(mgr, null);
    }

    public void close() {
    }

    public HttpRoute getRoute() {
        throw new UnsupportedOperationException("just a mockup");
    }

    public void layerProtocol(HttpContext context, HttpParams params) {
        throw new UnsupportedOperationException("just a mockup");
    }

    public void open(HttpRoute route, HttpContext context, HttpParams params) throws IOException {
        throw new UnsupportedOperationException("just a mockup");
    }

    public void shutdown() {
    }

    public void tunnelTarget(boolean secure, HttpParams params) {
        throw new UnsupportedOperationException("just a mockup");
    }

    public void tunnelProxy(HttpHost next, boolean secure, HttpParams params) {
        throw new UnsupportedOperationException("just a mockup");
    }

    public Object getState() {
        throw new UnsupportedOperationException("just a mockup");
    }

    public void setState(Object state) {
        throw new UnsupportedOperationException("just a mockup");
    }
}
