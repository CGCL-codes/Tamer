package org.nakedobjects.distribution.xml;

import org.nakedobjects.distribution.ServerDistribution;
import org.nakedobjects.distribution.command.Request;
import org.nakedobjects.distribution.command.Response;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import org.apache.log4j.Logger;
import com.thoughtworks.xstream.XStream;

class XServerConnection implements ServerConnection, Runnable {

    private static final Logger LOG = Logger.getLogger(XServerConnection.class);

    private static int nextId = 1;

    private int id = nextId++;

    private Socket socket;

    private ObjectOutputStream output;

    private ObjectInputStream input;

    private ServerDistribution server;

    private Thread t;

    public XServerConnection(Socket socket, ServerDistribution server) {
        this.socket = socket;
        this.server = server;
        try {
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            t.interrupt();
            socket.close();
        } catch (IOException e) {
            LOG.error("failed to close connection", e);
        }
    }

    public String getClient() {
        return socket.getInetAddress().getHostAddress();
    }

    public void start() {
        t = new Thread(this, "Connection " + id);
        t.start();
    }

    public void run() {
        while (true) {
            try {
                String requestData = (String) input.readObject();
                LOG.debug("request received \n" + requestData);
                String responseData = respond(requestData);
                LOG.debug("send response \n" + responseData);
                output.writeObject(responseData);
                output.flush();
            } catch (SocketException e) {
                LOG.info("shutting down receiver (" + e + ")");
                break;
            } catch (IOException e) {
                LOG.info("connection exception; closing connection", e);
                break;
            } catch (ClassNotFoundException e) {
                LOG.error("unknown class received; closing connection: " + e.getMessage(), e);
                break;
            }
        }
    }

    private String respond(String requestData) {
        XStream xstream = new XStream();
        Request request = (Request) xstream.fromXML(requestData);
        LOG.debug("request received " + request);
        String responseData;
        try {
            request.execute(server);
            Response response;
            response = new Response(request);
            response.setUpdates(server.getUpdates());
            LOG.debug("sending " + response);
            responseData = xstream.toXML(response);
        } catch (Exception e) {
            LOG.debug("sending exception " + e);
            responseData = xstream.toXML(e);
        }
        return responseData;
    }
}
