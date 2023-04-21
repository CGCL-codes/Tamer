package com.googlecode.psiprobe.tools.url;

import java.net.MalformedURLException;

public class URLParser {

    private String protocol = null;

    private String host = null;

    private int port = -1;

    private String path = null;

    public URLParser(String url) throws MalformedURLException {
        if (url != null && url.length() > 0) {
            int ppos = url.indexOf("://");
            if (ppos >= 0) {
                protocol = url.substring(0, ppos);
                url = url.substring(ppos + 3);
            }
            String hostport;
            ppos = url.indexOf("/");
            if (ppos >= 0) {
                hostport = url.substring(0, ppos);
                path = url.substring(ppos + 1);
            } else {
                hostport = url;
            }
            ppos = hostport.indexOf(":");
            if (ppos >= 0) {
                host = hostport.substring(0, ppos);
                String port = hostport.substring(ppos + 1);
                try {
                    this.port = Integer.parseInt(port);
                } catch (NumberFormatException e) {
                    throw new MalformedURLException("Invalid port " + port);
                }
            } else {
                host = hostport;
            }
        } else {
            throw new MalformedURLException("Empty URL");
        }
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
