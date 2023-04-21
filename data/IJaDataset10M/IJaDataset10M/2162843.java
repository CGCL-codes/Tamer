package org.highcon.pddpd.plugins.consumers;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.highcon.pddpd.lib.ConfigurationException;
import org.highcon.pddpd.lib.ConsumerSkeleton;

public class ObjConsumer extends ConsumerSkeleton {

    Vector addresses = new Vector();

    Vector streams = new Vector();

    int count = -1;

    protected void cleanup() {
        Enumeration addrs = this.streams.elements();
        while (addrs.hasMoreElements()) {
            try {
                ((ObjectOutputStream) addrs.nextElement()).close();
            } catch (IOException e) {
                message("Error closing output stream - " + e.getLocalizedMessage());
                continue;
            }
        }
        this.addresses = null;
        this.streams = null;
        this.count = -1;
    }

    protected synchronized void consume(Object in) {
        count = (count + 1) % streams.size();
        while (count == 0 && null == streams.get(0)) {
            message("No open sockets to send object");
            pause(5000);
        }
        try {
            ((ObjectOutputStream) (this.streams.get(count))).writeObject(in);
        } catch (IOException e) {
            message("Exception while sending object - " + e.getLocalizedMessage());
            pause(1000 / streams.size());
            consume(in);
        }
    }

    protected void initialize() {
    }

    public Hashtable getConfig() {
        Hashtable config = new Hashtable();
        Enumeration addrs = this.addresses.elements();
        String value = "";
        while (addrs.hasMoreElements()) {
            value += (String) addrs.nextElement();
            if (addrs.hasMoreElements()) {
                value += "/";
            }
        }
        config.put("destinations", value);
        return config;
    }

    public Hashtable getSchema() {
        Hashtable schema = new Hashtable();
        schema.put("destinations", "The list of destinations in the following format: host1:###/host2:###/host3:### where ### is the port number");
        return null;
    }

    public Object getValue(String name) {
        return null;
    }

    public synchronized void setConfig(Hashtable config) throws ConfigurationException {
        if (config.containsKey("destinations")) {
            String[] dest = ((String) config.get("destinations")).split("/");
            Vector newAddresses = new Vector(Arrays.asList(dest));
            String error_msg = "";
            for (int x = 0; x < this.addresses.size(); x++) {
                if (!newAddresses.contains((String) this.addresses.get(x))) {
                    try {
                        ((ObjectOutputStream) this.streams.get(x)).close();
                    } catch (IOException e) {
                        error_msg += ("Exception while closing socket - " + e.getLocalizedMessage() + "\n");
                    }
                    this.streams.remove(x);
                    this.addresses.remove(x);
                }
            }
            for (int x = 0; x < newAddresses.size(); x++) {
                if (!this.addresses.contains((String) newAddresses.get(x))) {
                    int port = 0;
                    String[] addrparts = ((String) newAddresses.get(x)).split(":");
                    try {
                        port = Integer.parseInt(addrparts[1]);
                    } catch (Exception e) {
                        error_msg += "Could not extract port number from address " + (String) newAddresses.get(x) + "\n";
                        continue;
                    }
                    Socket sock = null;
                    try {
                        sock = new Socket(addrparts[0], port);
                    } catch (UnknownHostException e1) {
                        error_msg += "Could not resolve " + addrparts[0] + "\n";
                        continue;
                    } catch (IOException e1) {
                        error_msg += "Exception occured opening socket - " + e1.getLocalizedMessage() + "\n";
                        continue;
                    }
                    ObjectOutputStream objout;
                    try {
                        objout = new ObjectOutputStream(sock.getOutputStream());
                    } catch (IOException e2) {
                        error_msg += "Exception occured creating output stream - " + e2.getLocalizedMessage() + "\n";
                        continue;
                    }
                    this.streams.add(objout);
                    this.addresses.add((String) newAddresses.get(x));
                }
            }
            if (!error_msg.equals("")) {
                throw new ConfigurationException(error_msg);
            }
        }
    }

    public String getConfigFilename() {
        return "ObjConsumer.conf";
    }
}
