package com.googlecode.estuary.httpd.domain.response;

import java.io.IOException;
import com.googlecode.estuary.httpd.domain.HttpHeader;
import com.googlecode.estuary.httpd.domain.io.HttpOutputStream;
import com.googlecode.estuary.httpd.domain.request.AssistedHttpRequest;
import com.googlecode.estuary.httpd.domain.request.HttpRequest;

public class RequestAwareHttpResponse implements HttpResponse {

    private AssistedHttpRequest httpRequest;

    private AssistedHttpResponse httpResponse;

    public RequestAwareHttpResponse(HttpResponse httpResponse, HttpRequest httpRequest) {
        this.httpResponse = new AssistedHttpResponse(httpResponse);
        this.httpRequest = new AssistedHttpRequest(httpRequest);
    }

    public void addHeader(HttpHeader header) {
        httpResponse.addHeader(header);
    }

    public HttpOutputStream getOutputStream() {
        return httpResponse.getOutputStream();
    }

    public void setStatusLine(HttpStatusLine statusLine) {
        httpResponse.setStatusLine(statusLine);
    }

    public void transmitHead() throws IOException {
        httpResponse.transmitHead();
    }
}
