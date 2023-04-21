package com.elibera.gateway.elements;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;

public class DummyHTTPMethod extends HttpMethodBase {

    private String url;

    public DummyHTTPMethod(String url) {
        this.url = url;
    }

    @Override
    public String getName() {
        return "HttpMethodBase";
    }

    @Override
    public URI getURI() throws URIException {
        return new URI(url, false);
    }

    @Override
    public long getResponseContentLength() {
        return 0;
    }

    @Override
    public Header getResponseHeader(String headerName) {
        if (headerName != null && headerName.compareTo("Content-Type") == 0) return new Header(headerName, "text/html");
        return null;
    }

    @Override
    public int getStatusCode() {
        return 200;
    }

    @Override
    public void releaseConnection() {
    }
}
