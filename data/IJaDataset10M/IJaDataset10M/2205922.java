package info.wisl.netmodules;

import info.wisl.*;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * Concrete class extending NetModule which performs TCP measurements for WISL.
 * 
 * <p>Expects 2 mandatory parameters:
 * <ul>
 *   <li>destination - address (either dotted quads or string representation) 
 *   <li>port - the port number to use for the destination
 * </ul>
 *
 * <p>Fires 2 events:     
 * <ul>
 *   <li>tcptime - how long it takes to connect to a tcp server
 *   <li>tcpfail - fired when a tcp connection fails 
 * </ul>
 *
 * @author Bill Tice
 */
public class TCPModule extends NetModule {

    private static final String LOSS = "L";

    private static final String SUCCESS = "S";

    /** The addr of the host to connect to */
    private String host;

    /** The port number of the host to connect to */
    private int port;

    /**
     * Instantiates the module. Sets active to true
     */
    public TCPModule(Wisl w) {
        super(w);
        NMID = 20;
        host = new String();
        setHost("");
        setPort(-1);
    }

    /**
     * Returns the string representing the hostname
     *
     * @return the hostname
     */
    public String getHost() {
        return host;
    }

    /**
     * Returns the integer representing the hostname's port number
     *
     * @return the hostname's port number
     */
    public int getPort() {
        return port;
    }

    public void setHost(String h) {
        host = h;
    }

    public void setPort(int p) {
        port = p;
    }

    /**
     * Parses the parameter vector sent by each Listener to determine what
     * needs to be done for each Listener.
     */
    public void parseParameters(Vector params) {
        Parameter p;
        int i;
        for (i = 0; i < params.size(); i++) {
            p = (Parameter) params.elementAt(i);
            if (p.getName().equalsIgnoreCase("destination")) {
                setHost((String) (p.getValue()));
                Debugger.print(Wisl.DEBUG_VERBOSE, "TCPModule: host= " + getHost());
            }
            if (p.getName().equalsIgnoreCase("port")) {
                setPort(new Integer((String) p.getValue()).intValue());
                Debugger.print(Wisl.DEBUG_VERBOSE, "TCPModule: port= " + getPort());
            }
        }
        super.parseParameters(params);
        if (getHost().equals("")) handleException(new NetModuleException("No destination set for TCPModule"));
        if (getPort() < 0) handleException(new NetModuleException("No port set for TCPModule"));
    }

    /**
     * Performs the TCP connections and report the times to our reportTime
     * method
     */
    public void run() {
        Debugger.print(Wisl.DEBUG_VERBOSE, "Beginning TCPModule Thread");
        boolean fail = false;
        long beginTimeStamp;
        long endTimeStamp;
        long periodTimeStamp;
        beginTimeStamp = endTimeStamp = 0;
        InetAddress addr = null;
        Socket sock;
        while (!getExit()) {
            periodTimeStamp = new Date().getTime();
            try {
                addr = InetAddress.getByName(getHost());
            } catch (UnknownHostException uhe) {
                reportFail();
            }
            beginTimeStamp = new Date().getTime();
            try {
                sock = new Socket(addr, getPort());
                endTimeStamp = new Date().getTime();
                sock.close();
            } catch (IOException ioe) {
                fail = true;
                reportFail();
            }
            if (!fail) reportTime(endTimeStamp - beginTimeStamp);
            fail = false;
            try {
                long difference;
                difference = new Date().getTime() - periodTimeStamp;
                if (period > difference) sleep(period - difference);
            } catch (Exception e) {
                handleException(new NetModuleException("Error while waiting to take measurement in TCPModule"));
            }
        }
    }

    /**
     * Called by the run method to report the time of TCP connections. Creates
     * a NetModuleEvent and passes it to fireEvent.
     *
     * @param time a long indicating how many milliseconds it took to perform a
     *        TCP connection
     */
    public synchronized void reportTime(float time) {
        NetModuleEvent nme;
        nme = new NetModuleEvent("tcptime", new Float(time), this);
        fireEvent(nme);
        sendToServer(nme);
    }

    /**
     * Reports a failure event (if the TCP connection fails)
     *
     * @return calls fireEvent with a NetModuleEvent reporting a "tcpfail"
     */
    public synchronized void reportFail() {
        NetModuleEvent nme;
        nme = new NetModuleEvent("tcpfail", null, this);
        fireEvent(nme);
        sendToServer(nme);
    }

    /**
     * Sends measurement data to server.  Concrete method must hardcode the
     * necessary logic (what to send and how often)
     *
     * @param nme the event to send to the server
     *
     * @return void
     */
    public void sendToServer(NetModuleEvent nme) {
        super.sendToServer(nme);
    }
}
