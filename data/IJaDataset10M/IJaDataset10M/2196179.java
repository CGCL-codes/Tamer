package com.volantis.vdp.scs.proxy.https;

import java.io.*;
import java.net.*;
import org.apache.log4j.Logger;
import com.volantis.vdp.scs.connectors.client.connection.ClientBroker;
import com.volantis.vdp.scs.util.Request;
import com.volantis.vdp.scs.util.ResponseHeader;
import com.volantis.vdp.scs.util.Samples;
import com.volantis.vdp.scs.proxy.Proxy;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.mcs.localization.LocalizationFactory;

/**
 * A class sending and receiving a https request and response. Wrapping an requesting process in
 * the separate class is needed as this operation requires processing in own thread.
 */
public class ProxyHTTPS implements Proxy {

    private static final LogDispatcher logger = LocalizationFactory.createLogger(ProxyHTTPS.class);

    private Socket socket;

    private DataInputStream dataIn;

    private DataOutputStream dataOut;

    private ClientBroker clientBroker;

    private boolean run;

    private Thread thread;

    /**
     * Creates the ProxyHTTPS instance basing on sent request and client broker.
     * @param request the HTTPs request that should be sent to server
     * @param clientBroker a broker used to obtain the response
     */
    public ProxyHTTPS(Request request, ClientBroker clientBroker) {
        this.clientBroker = clientBroker;
        this.clientBroker.setProxy(this);
        this.clientBroker.setProxyHTTPS(true);
        try {
            String[] pairHostAndPort = request.getURL().split(":");
            String host = "";
            int port = 443;
            if (pairHostAndPort.length == 2) {
                host = pairHostAndPort[0];
                port = Integer.valueOf(pairHostAndPort[1]).intValue();
            } else {
                host = pairHostAndPort[0];
            }
            socket = new Socket(host, port);
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            sendConnectedResponse(request);
            receive();
        } catch (UnknownHostException uke) {
            logger.error(uke.getMessage());
        } catch (IOException ioe) {
            logger.error(ioe.getMessage());
        }
    }

    /**
     * Receives the response from the server. The method is processed in separate thread
     */
    private void receive() {
        this.run = true;
        Runnable r = new Runnable() {

            public void run() {
                try {
                    int SIZE_OF_BUFFER = 4 * 4096;
                    int size = 0;
                    byte[] buffer = new byte[SIZE_OF_BUFFER];
                    while (run) {
                        if ((size = dataIn.read(buffer)) > 0) {
                            clientBroker.send(buffer, 0, size);
                        } else if (size == -1) {
                            close();
                            clientBroker.close();
                        }
                    }
                } catch (IOException e) {
                }
            }
        };
        thread = new Thread(r);
        thread.start();
    }

    /**
     * Mediates in sending the request.
     * @param request redirected request
     */
    public void redirect(Request request) {
        send(request.getHeaderBytes());
    }

    /**
     * Mediates in sending the request.
     * @param data redirected request given as a byte array
     */
    public void redirect(byte[] data) {
        send(data);
    }

    /**
     * Sends the given data to server
     * @param data the data sent to a server
     */
    public synchronized void send(byte[] data) {
        try {
            dataOut.write(data);
            dataOut.flush();
        } catch (IOException ioe) {
            String[] mess = new String[1];
            mess[0] = ioe.getMessage();
            logger.error("cannot-write-to-stream");
        }
    }

    /**
     * Sends the given data to server.
     * @param data the data sent to a server
     * @param off the offset of the sent part in the given data array
     * @param len how many bytes from the given data array should be sent
     */
    public synchronized void send(byte[] data, int off, int len) {
        try {
            dataOut.write(data, off, len);
            dataOut.flush();
        } catch (IOException ioe) {
            String[] mess = new String[1];
            mess[0] = ioe.getMessage();
            logger.error("cannot-write-to-stream");
        }
    }

    /**
     * Stops the thread responsible for sending and receiving the request, releasing
     * the resorces bound with this request.
     */
    public void close() {
        try {
            this.run = false;
            dataIn.close();
            dataOut.close();
            socket.close();
            if (thread != null) thread.interrupt();
            thread = null;
            System.gc();
        } catch (IOException ioe) {
        }
    }

    /**
     * Sends the response to the client if the connection negotiation was successful
     * @param request
     */
    private void sendConnectedResponse(Request request) {
        String response = Samples.connectionEstablishedResponse(request.getProtocol());
        clientBroker.send(response.getBytes());
    }
}
