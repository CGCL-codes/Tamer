package com.googlecode.flaxcrawler.download;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents request for {@code Downloader}. Contains necessary request headers.
 * @author ameshkov
 */
public class Request {

    private Map<String, String> headers;

    private URL url;

    /**
     * Creates an instance of the {@code Request} class. Sets {@code Request} url.
     * @param url
     */
    public Request(URL url) {
        this.url = url;
        this.headers = new HashMap<String, String>();
    }

    /**
     * Returns {@code Request} headers
     * @return
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Sets request headers
     * @param headers
     */
    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * Returns {@code Request} url
     * @return
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Returns {@code Request} header
     * @param header
     * @return
     */
    public String getHeader(String header) {
        return headers.get(header);
    }

    /**
     * Adds header to the {@code Request}
     * @param header
     * @param value
     */
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }
}
