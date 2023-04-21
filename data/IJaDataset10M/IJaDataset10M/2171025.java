package com.incendiaryblue.msg;

import com.incendiaryblue.appframework.ServerConfig;
import com.incendiaryblue.config.XMLConfigurationException;
import com.incendiaryblue.config.XMLContext;
import com.incendiaryblue.util.ObjectPool;
import org.w3c.dom.*;
import java.net.*;
import java.io.*;
import java.util.*;

public abstract class SocketMessageDelivery extends MessageDeliveryImpl {

    private String server;

    private int maxConnections;

    private int port;

    private int timeout;

    private ObjectPool sockets;

    private int numConnections = 0;

    /**
	 * Configures this object from an XML element. An object of this type
	 * requires the following attributes, which MUST contain names of properties
	 * in the server config file:<br>
	 *
	 * <ul><li>maxConnections - the maximum number of simultaneous connections
	 * to the socket through this object
	 * <li>server - the IP address of the server to connect to
	 * <li>port - the port number on the host server to bind to
	 * <li>timeout - the number of milliseconds before a timeout occurs (0
	 * means forever)
	 */
    public Object configure(Element em, XMLContext con) throws XMLConfigurationException {
        try {
            String maxConnsProp = em.getAttribute("maxConnections");
            String serverProp = em.getAttribute("server");
            String portProp = em.getAttribute("port");
            String timeoutProp = em.getAttribute("timeout");
            this.maxConnections = Integer.parseInt(ServerConfig.get(maxConnsProp));
            this.server = ServerConfig.get(serverProp);
            this.port = Integer.parseInt(ServerConfig.get(portProp));
            this.timeout = Integer.parseInt(ServerConfig.get(timeoutProp));
            sockets = new ObjectPool(maxConnections) {

                protected Object getNewObject() throws IOException {
                    Socket s = new Socket(server, port);
                    s.setSoTimeout(timeout);
                    s.setKeepAlive(true);
                    return s;
                }

                protected void dispose(Object o) throws IOException {
                    Socket s = (Socket) o;
                    if (s != null) {
                        s.close();
                    }
                    System.err.println("dispose() called on socket pool object...");
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
            throw new XMLConfigurationException("Error configuring SocketMessageDeliveryImpl");
        }
        return this;
    }

    /**
	 * Send a byte array across the socket and block and wait for a reply. If
	 * the reply takes longer than the value set as timeout, an IOException
	 * will be thrown. If there are more than the maximum allowed simultaneous
	 * connections on the socket (defined in the XML config), the thread blocks
	 * until there is a free connection.<p>
	 *
	 * The method terminates the message request with a -1 before waiting for a
	 * response.
	 *
	 * @param bytes the array of bytes to send as a request
	 * @return a resposne as an array of bytes from remote host
	 *
	 * @throws IOException if an error occurs transmitting to or receiving from
	 *         the remote host
	 */
    public byte[] send(byte[] bytes) throws IOException {
        byte[] result = null;
        Socket s = null;
        try {
            s = (Socket) sockets.getObject();
        } catch (RuntimeException e) {
            throw new IOException("Unable to get new connection to hub server");
        }
        try {
            result = doSend(bytes, s);
        } catch (IOException e) {
            System.err.println("Lost socket - getting new pool...");
            e.printStackTrace();
            sockets.removeObject(s);
            try {
                s = (Socket) sockets.getObject();
            } catch (RuntimeException f) {
                throw new IOException("Unable to get new connection to hub server.");
            }
            try {
                result = doSend(bytes, s);
            } catch (IOException f) {
                sockets.removeObject(s);
                s = null;
                throw f;
            }
        } finally {
            if (s != null) {
                try {
                    sockets.releaseObject(s);
                } catch (Exception e) {
                }
            }
        }
        return result;
    }

    public byte[] doSend(byte[] bytes, Socket s) throws IOException {
        OutputStream out = s.getOutputStream();
        InputStream in = s.getInputStream();
        byte[] result = send(bytes, in, out);
        out.flush();
        return result;
    }

    public abstract byte[] send(byte[] bytes, InputStream socketIn, OutputStream socketOut) throws IOException;

    public synchronized void destroy() {
        System.out.println("Calling destroy() on a SocketMessageDelivery...");
        sockets.destroy();
    }
}
