package org.one.stone.soup.server.http.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.one.stone.soup.io.Connection;
import org.one.stone.soup.io.ConnectionListener;
import org.one.stone.soup.io.StreamConnection;
import org.one.stone.soup.stringhelper.StringGenerator;

public class HttpTunnelConnection implements Connection, ConnectionListener {

    private String alias;

    private String address;

    private int port;

    private String url;

    private String host;

    private InputStream iStream;

    private OutputStream oStream;

    private Socket upSocket;

    private Socket downSocket;

    public HttpTunnelConnection(String host, String tunnelToAddress, int tunnelToPort) throws UnknownHostException, IOException {
        this.host = host;
        this.address = host;
        this.port = 80;
        alias = InetAddress.getLocalHost().getHostAddress() + "-" + StringGenerator.generateUniqueId();
        url = "/tunnel/" + tunnelToAddress + "/" + tunnelToPort + "/" + alias;
        setProxy();
        openUpConnection();
        openDownConnection();
    }

    private void setProxy() {
        String proxySet = System.getProperty("proxySet");
        if (proxySet != null && proxySet.equals("true")) {
            address = System.getProperty("proxyHost");
            port = Integer.parseInt(System.getProperty("proxyPort"));
        }
    }

    private void openUpConnection() throws IOException {
        upSocket = new Socket(address, port);
        OutputStream upStream = upSocket.getOutputStream();
        upStream.write(("GET " + url + " HTTP/1.1\n").getBytes());
        upStream.write(("Host: " + host + "\n").getBytes());
        upStream.write(("User-Agent: Tunnel Client\n\n").getBytes());
        upStream.flush();
        oStream = upStream;
    }

    private void openDownConnection() throws IOException {
        downSocket = new Socket(address, port);
        OutputStream upStream = downSocket.getOutputStream();
        upStream.write(("POST " + url + " HTTP/1.1\n").getBytes());
        upStream.write(("Host: " + host + "\n").getBytes());
        upStream.write(("User-Agent: Tunnel Client\n\n").getBytes());
        upStream.flush();
        iStream = downSocket.getInputStream();
    }

    public OutputStream getOutputStream() {
        return oStream;
    }

    public InputStream getInputStream() {
        return iStream;
    }

    public void close() throws IOException {
        upSocket.close();
        downSocket.close();
    }

    public String getAlias() {
        return alias;
    }

    public long getDataSentSize() {
        return -1;
    }

    public void connectionClosed(Connection owner) {
        try {
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void downStreamConnectionCreated(StreamConnection connection) {
    }

    public void sent(byte[] data, int size, StreamConnection connection) throws IOException {
    }

    public void upStreamConnectionCreated(StreamConnection connection) {
    }
}
