package org.highcon.pddpd.plugins.producers;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import org.highcon.pddpd.lib.ConfigurationException;
import org.highcon.pddpd.lib.Producer;

/**
 * @author paul
 *
 */
public class ObjectProducer extends Producer {

    private boolean alive = true;

    int port = 0;

    public void run() {
        ServerSocket serv = null;
        Socket sock;
        try {
            serv = new ServerSocket(this.port);
            serv.setSoTimeout(500);
        } catch (IOException e) {
            messageQ.add("Trouble creating ServerSocket: " + e.getLocalizedMessage());
        }
        while (alive) {
            try {
                sock = serv.accept();
                ObjectInputStream ois = new ObjectInputStream(sock.getInputStream());
                for (; ; ) {
                    try {
                        outQueue.push(ois.readObject());
                    } catch (EOFException eof) {
                        break;
                    } catch (ClassNotFoundException e) {
                        messageQ.add("Class for object received was not found: " + e.getLocalizedMessage());
                    }
                }
                sock.close();
            } catch (SocketTimeoutException ste) {
                continue;
            } catch (IOException e1) {
                messageQ.add("IO Error: " + e1.getLocalizedMessage());
            }
        }
        this.myLock.unlock();
    }

    public Hashtable getConfig() {
        Hashtable config = new Hashtable();
        config.put("port", new Integer(this.port));
        return config;
    }

    public Hashtable getSchema() {
        Hashtable config = new Hashtable();
        config.put("port", "The port the plugin will listen on (int)");
        return config;
    }

    public Object getValue(String name) {
        if (name.equals("port")) {
            return new Integer(this.port);
        }
        return null;
    }

    public void setConfig(Hashtable config) throws ConfigurationException {
        if (config.containsKey("port")) {
            int port = 0;
            if (config.get("port") instanceof String) {
                try {
                    port = Integer.parseInt((String) config.get("port"));
                } catch (NumberFormatException nfe) {
                    messageQ.add("Port configuration option did not parse to an int: " + (String) config.get("port"));
                }
                if (port < 1 && port > 32654) {
                    messageQ.add("Port configuration option out of range: " + port);
                }
                this.port = port;
            } else {
                messageQ.add("Port configuration option was of type: " + config.get("port").getClass().getName());
            }
        }
    }

    public void kill(boolean shutdown) {
        this.alive = false;
        this.myLock.lock();
    }

    public String getConfigFilename() {
        return "ObjectProducer.conf";
    }
}
