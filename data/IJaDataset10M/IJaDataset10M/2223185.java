package org.simpleframework.tool.search;

import java.io.IOException;
import java.io.InputStream;
import org.apache.wicket.util.upload.RequestContext;
import org.simpleframework.http.Request;

class RequestAdapter implements RequestContext {

    private final Request request;

    public RequestAdapter(Request request) {
        this.request = request;
    }

    public String getContentType() {
        return request.getContentType().toString();
    }

    public int getContentLength() {
        return request.getContentLength();
    }

    public InputStream getInputStream() throws IOException {
        return request.getInputStream();
    }
}
