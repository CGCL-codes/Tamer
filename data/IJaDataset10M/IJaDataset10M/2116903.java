package org.helium.transept.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.net.URL;
import com.sun.java.util.collections.Iterator;
import org.helium.transept.exception.MalformedHeaderException;

public class Request extends Message {

    private String method = null;

    private String host = null;

    private InetAddress[] hostAddrs = null;

    private int port = 80;

    private String protocol = null;

    private String path = null;

    private String urlString = null;

    Request(Socket in) throws IOException, MalformedHeaderException {
        super(in);
        read();
    }

    Request(InputStream in) throws IOException, MalformedHeaderException {
        super(in);
        read();
    }

    public void read() throws IOException, MalformedHeaderException {
        int firstSpace = firstLine.indexOf(" ");
        int secondSpace = firstLine.indexOf(" ", firstSpace + 1);
        String address = null;
        if (firstSpace == -1) {
            method = null;
            address = firstLine;
        } else if (secondSpace == -1) {
            method = firstLine.substring(0, firstSpace);
            address = firstLine.substring(firstSpace + 1);
        } else {
            method = firstLine.substring(0, firstSpace);
            address = firstLine.substring(firstSpace + 1, secondSpace);
            protocol = firstLine.substring(secondSpace + 1);
        }
        String hostContent = null;
        if (method.equalsIgnoreCase("CONNECT")) {
            hostContent = address;
        } else {
            hostContent = getHeaderValue("Host");
            try {
                setURL(address);
            } catch (MalformedURLException me) {
                throw new MalformedHeaderException("Bad request URL: " + me.getMessage());
            }
        }
        if (hostContent != null) {
            int colonPos = hostContent.indexOf(":");
            if (colonPos == -1) {
                host = hostContent;
            } else {
                host = hostContent.substring(0, colonPos);
                try {
                    port = Integer.parseInt(hostContent.substring(colonPos + 1));
                } catch (NumberFormatException ne) {
                    throw new MalformedHeaderException("Bad port number in CONNECT request");
                }
            }
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String x) {
        method = x;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String x) {
        path = x;
        urlString = null;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int x) {
        port = x;
        urlString = null;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String x) {
        protocol = x;
        urlString = null;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String x) {
        host = x;
        urlString = null;
    }

    public InetAddress[] getHostAddrs() throws UnknownHostException {
        if (hostAddrs == null) {
            hostAddrs = InetAddress.getAllByName(host);
        }
        return hostAddrs;
    }

    public void setHostAddrs(InetAddress[] x) {
        hostAddrs = x;
    }

    public String getURL() {
        if (urlString != null) {
            return urlString;
        }
        String urlProtocol = protocol.substring(0, protocol.indexOf('/')).toLowerCase();
        URL url = null;
        if ((port != 80) || (!(urlProtocol.equals("http")))) {
            try {
                url = new URL(urlProtocol, host, port, path);
                urlString = url.toString();
            } catch (MalformedURLException e) {
                urlString = urlProtocol + "://" + host + ":" + port + path;
            }
        } else {
            try {
                url = new URL(urlProtocol, host, path);
                urlString = url.toString();
            } catch (MalformedURLException e) {
                urlString = urlProtocol + "://" + host + path;
            }
        }
        return urlString;
    }

    public void setURL(String urlString) throws MalformedURLException {
        String urlProtocol = null;
        String oldUrlProtocol = protocol.substring(0, protocol.indexOf('/'));
        String urlHost = null;
        int urlPort = -1;
        String urlPath = null;
        try {
            URL url = new URL(urlString);
            urlProtocol = url.getProtocol();
            urlHost = url.getHost();
            urlPort = url.getPort();
            urlPath = url.getFile();
        } catch (MalformedURLException e) {
            int methodSep = urlString.indexOf("://");
            if (methodSep == -1) {
                throw e;
            }
            int hostSep = urlString.indexOf('/', methodSep + 3);
            urlProtocol = urlString.substring(0, hostSep);
            urlHost = urlString.substring(methodSep + 3, hostSep);
            int portSep = urlHost.indexOf(':');
            if (portSep != -1) {
                try {
                    urlPort = Integer.parseInt(urlHost.substring(portSep + 1));
                    urlHost = urlHost.substring(0, portSep);
                } catch (NumberFormatException ne) {
                    throw e;
                }
            } else {
                port = -1;
            }
        }
        if (!(urlProtocol.equalsIgnoreCase(oldUrlProtocol))) {
            protocol = urlProtocol.toUpperCase() + protocol.substring(protocol.indexOf('/'));
        }
        if ((urlPort != -1) && ((!(urlProtocol.equalsIgnoreCase("http"))) || (urlPort != 80))) {
            port = urlPort;
        }
        if (!(urlHost.equals(host))) {
            host = urlHost;
            hostAddrs = null;
        }
        if (!(urlPath.equals(path))) {
            path = urlPath;
        }
        this.urlString = urlString;
    }

    public void write(OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        boolean isConnect = method.equalsIgnoreCase("CONNECT");
        if (isConnect) {
            writer.write("CONNECT " + getHost() + ":" + getPort() + " " + this.protocol + "\r\n");
        } else {
            String method = this.method;
            String path = this.getPath();
            String protocol = this.protocol;
            if (method == null) {
                method = "GET";
            }
            if (path == null) {
                path = "/";
            }
            if (protocol == null) {
                protocol = "HTTP/1.0";
            }
            writer.write(method + " " + path + " " + protocol + "\r\n");
        }
        Iterator headersIter = getHeaders().iterator();
        while (headersIter.hasNext()) {
            Header header = (Header) (headersIter.next());
            if ((!(isConnect)) && (header.getName().equalsIgnoreCase("Host"))) {
                int port = getPort();
                if (port == 80) {
                    writer.write("Host: " + getHost() + "\r\n");
                } else {
                    writer.write("Host: " + getHost() + ":" + getPort() + "\r\n");
                }
            } else {
                writer.write(header.getName() + ": " + header.getValue() + "\r\n");
            }
        }
        writer.write("\r\n");
        writer.flush();
    }

    public void writePassThru(OutputStream out) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(out);
        boolean isConnect = method.equalsIgnoreCase("CONNECT");
        if (isConnect) {
            writer.write("CONNECT " + getHost() + ":" + getPort() + " " + this.protocol + "\r\n");
        } else {
            String method = this.method;
            String address = this.getURL();
            String protocol = this.protocol;
            if (method == null) {
                method = "GET";
            }
            if (address == null) {
                address = "/";
            }
            if (protocol == null) {
                protocol = "HTTP/1.0";
            }
            writer.write(method + " " + address + " " + protocol + "\r\n");
        }
        Iterator headersIter = getHeaders().iterator();
        while (headersIter.hasNext()) {
            Header header = (Header) (headersIter.next());
            if ((!(isConnect)) && (header.getName().equalsIgnoreCase("Host"))) {
                int port = getPort();
                if (port == 80) {
                    writer.write("Host: " + getHost() + "\r\n");
                } else {
                    writer.write("Host: " + getHost() + ":" + getPort() + "\r\n");
                }
            } else {
                writer.write(header.getName() + ": " + header.getValue() + "\r\n");
            }
        }
        writer.write("\r\n");
        writer.flush();
    }
}
