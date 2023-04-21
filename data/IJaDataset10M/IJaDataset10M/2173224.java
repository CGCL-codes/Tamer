package APJP.HTTP11;

import java.util.ArrayList;
import java.util.List;

public class HTTPMessageHeaders {

    private List<HTTPMessageHeader> httpMessageHeaders;

    public HTTPMessageHeaders() {
        super();
        httpMessageHeaders = new ArrayList<HTTPMessageHeader>();
    }

    public HTTPMessageHeader getHTTPMessageHeader(String key) {
        for (HTTPMessageHeader httpMessageHeader : httpMessageHeaders) {
            if (httpMessageHeader.getKey().equalsIgnoreCase(key) == true) {
                return httpMessageHeader;
            }
        }
        return null;
    }

    public HTTPMessageHeader[] getHTTPMessageHeaders(String key) {
        List<HTTPMessageHeader> httpMessageHeaders = new ArrayList<HTTPMessageHeader>();
        for (HTTPMessageHeader httpMessageHeader : this.httpMessageHeaders) {
            if (httpMessageHeader.getKey().equalsIgnoreCase(key) == true) {
                httpMessageHeaders.add(httpMessageHeader);
            }
        }
        return httpMessageHeaders.toArray(new HTTPMessageHeader[] {});
    }

    public HTTPMessageHeader[] getHTTPMessageHeaders() {
        List<HTTPMessageHeader> httpMessageHeaders = new ArrayList<HTTPMessageHeader>();
        for (HTTPMessageHeader httpMessageHeader : this.httpMessageHeaders) {
            httpMessageHeaders.add(httpMessageHeader);
        }
        return httpMessageHeaders.toArray(new HTTPMessageHeader[] {});
    }

    public void addHTTPMessageHeader(HTTPMessageHeader httpMessageHeader) {
        httpMessageHeaders.add(httpMessageHeader);
    }

    public void removeHTTPMessageHeader(HTTPMessageHeader httpMessageHeader) {
        httpMessageHeaders.remove(httpMessageHeader);
    }
}
