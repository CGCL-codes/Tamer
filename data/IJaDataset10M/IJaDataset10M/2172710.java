package org.nakedobjects.nof.boot.system;

import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.nof.core.conf.Configuration;
import org.nakedobjects.nof.core.context.NakedObjectsContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

public abstract class AbstractServerMonitor {

    private static final Logger LOG = Logger.getLogger(AbstractServerMonitor.class);

    private static final String ADDRESS = Configuration.ROOT + "monitor.address";

    private boolean acceptConnection = true;

    public void listen() {
        String hostAddress = NakedObjectsContext.getConfiguration().getString(ADDRESS);
        InetAddress address;
        try {
            address = hostAddress == null ? null : InetAddress.getByName(hostAddress);
            int port = getPort();
            ServerSocket serverSocket = new ServerSocket(port, 2, address);
            serverSocket.setSoTimeout(5000);
            LOG.info("waiting for monitor connection on " + serverSocket);
            while (acceptConnection) {
                Socket client = null;
                try {
                    client = serverSocket.accept();
                    LOG.info("client connection on " + client);
                } catch (SocketTimeoutException ignore) {
                    continue;
                } catch (IOException e) {
                    LOG.error("request failed", e);
                    continue;
                }
                try {
                    handleRequest(client);
                } catch (Exception e) {
                    LOG.error("request failed", e);
                }
            }
        } catch (UnknownHostException e) {
            throw new NakedObjectRuntimeException(e);
        } catch (IOException e) {
            throw new NakedObjectRuntimeException(e);
        }
    }

    protected abstract int getPort();

    private void handleRequest(Socket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
        String request = reader.readLine();
        if (request == null || request.length() == 0) {
            LOG.info("Connection dropped");
        } else {
            handleRequest(writer, request);
        }
        writer.close();
        reader.close();
    }

    public abstract void setTarget(NakedObjectsSystem system);

    public void shutdown() {
        acceptConnection = false;
    }

    protected abstract void handleRequest(PrintWriter writer, String request) throws IOException;
}
