package com.od.jtimeseries.net.httpd.response;

/**
 * This class is an ObjectDefinitions addition to prevent response caching
 */
public class NoCacheResponse extends NanoHttpResponse {

    public NoCacheResponse(String status, String mimeType) {
        super(status, mimeType);
        setNoCacheHeaders();
    }

    private void setNoCacheHeaders() {
        addHeader("Cache-Control", "no-cache");
    }
}
