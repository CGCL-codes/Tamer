package com.volantis.xml.pipeline.sax.drivers.web;

import com.volantis.shared.net.http.HTTPMessageEntities;
import java.io.InputStream;

/**
 * Default implementation of the WebDriverResponse interface.
 */
public class WebDriverResponseImpl implements WebDriverResponse {

    /**
     * The volantis copyright statement
     */
    private static final String mark = "(c) Volantis Systems Ltd 2003.";

    /**
     * The CookieJar associated with this response.
     */
    private HTTPMessageEntities cookies;

    /**
     * The Headers associated with this response
     */
    private HTTPMessageEntities headers;

    /**
     * The status code associated with this repsonse
     */
    private int statusCode;

    /**
     * The content type associated with this response
     */
    private String contentType;

    /**
     * The ignoredContent associated with this response
     */
    private InputStream ignoredContent;

    /**
     * The http version associated with this response.
     */
    private HTTPVersion httpVersion = null;

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

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set the HTTP version that was returned by the server.
     * @param version The version returned by the server.
     */
    public void setHTTPVersion(HTTPVersion version) {
        this.httpVersion = version;
    }

    /**
     * Get the HTTP version returned by the server.
     * @return The HTTP version returned by the server.
     */
    public HTTPVersion getHTTPVersion() {
        return this.httpVersion;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

    public void setIgnoredContent(InputStream inputStream) {
        this.ignoredContent = inputStream;
    }

    public InputStream getIgnoredContent() {
        return ignoredContent;
    }
}
