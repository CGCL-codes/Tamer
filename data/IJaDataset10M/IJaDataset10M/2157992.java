package com.kaixinff.net;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpResponse {

    private HttpURLConnection conn;

    private byte[] bytes;

    private String charset;

    public HttpResponse(HttpURLConnection conn, String charset) {
        this.conn = conn;
        this.charset = charset;
    }

    void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public HttpURLConnection getConn() {
        return conn;
    }

    public String getCharset() {
        return charset;
    }

    public String getContent() throws UnsupportedEncodingException {
        return new String(bytes, charset);
    }

    public byte[] getBytes() {
        return bytes;
    }

    public int getResponseCode() throws IOException {
        return conn.getResponseCode();
    }

    public String getHeaderField(String name) {
        return conn.getHeaderField(name);
    }

    public URL getURL() {
        return conn.getURL();
    }

    public String getHost() {
        return conn.getRequestProperty("host");
    }
}
