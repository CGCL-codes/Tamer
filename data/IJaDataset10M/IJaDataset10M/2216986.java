package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;

/**
 * Default implementation of the WebDriverRequest interface
 */
public class WebDriverRequestImpl implements WebDriverRequest {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The CookieJar associated with this request.
     */
    private HTTPMessageEntities cookies;

    /**
     * The Headers associated with this request.
     */
    private HTTPMessageEntities headers;

    /**
     * The parameters associtated with this request.
     */
    private HTTPMessageEntities parameters;

    public void setCookies(HTTPMessageEntities cookies) {
        this.cookies = cookies;
    }

    public HTTPMessageEntities getCookies() {
        return cookies;
    }

    public void setHeaders(HTTPMessageEntities headers) {
        this.headers = headers;
    }

    public HTTPMessageEntities getHeaders() {
        return headers;
    }

    public void setRequestParameters(HTTPMessageEntities parameters) {
        this.parameters = parameters;
    }

    public HTTPMessageEntities getRequestParameters() {
        return parameters;
    }
}
