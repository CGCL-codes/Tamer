package com.grt192.networking.spot;

import java.util.Enumeration;
import java.util.Hashtable;
import com.grt192.networking.SocketListener;

/**
 * Accepts inbound connections and makes outbound connections. A GRTServant
 * represents the SunSPOT's networked presence on a single port.
 * 
 * @author ajc
 */
public class GRTServant {

    private final int port;

    private Hashtable senders;

    private Hashtable receivers;

    public GRTServant(int port) {
        this.port = port;
        senders = new Hashtable();
        receivers = new Hashtable();
    }

    /**
	 * Sends data to a provided host. The connection will be initialized and
	 * started if never initialized in the past.
	 * 
	 * @param host
	 *            address (to connect and) send data to
	 * @param data
	 *            String representation of data to send
	 */
    public void sendData(String host, String data) {
        getServentOut(host).sendData(data);
    }

    /**
	 * Initializes a bidirectional connection to a host.
	 * 
	 * @param host
	 *            address of host
	 */
    public void connect(String host) {
        getServentIn(host);
        getServentOut(host);
    }

    /**
	 * Gets a single outbound connection instance to a host, that can only write
	 * data ( no read)
	 * 
	 * @param host
	 *            address of host
	 * @return
	 */
    public synchronized OutRadio getServentOut(String host) {
        OutRadio o = (OutRadio) senders.get(host);
        if (o == null) {
            o = new OutRadio(host, port);
            o.connect();
            senders.put(host, o);
        }
        return o;
    }

    /**
	 * Gets a single inbound connection instance to a host, that is read only
	 * 
	 * @param host
	 *            addresss of host
	 * @return
	 */
    public synchronized InRadio getServentIn(String host) {
        InRadio i = (InRadio) receivers.get(host);
        if (i == null) {
            i = new InRadio(host, port);
            i.connect();
            i.start();
            receivers.put(host, i);
        }
        return i;
    }

    /**
	 * Adds a listener for a specific host's events
	 * 
	 * @param host
	 *            address of host to send events from
	 * @param s
	 *            SocketListener to receive events
	 */
    public void addSocketListenerOut(String host, SocketListener s) {
        getServentOut(host).addSocketListener(s);
    }

    /**
	 * Stop a listener from listening to a specific host's events
	 * 
	 * @param host
	 *            address of host to send events from
	 * @param s
	 *            socketlistener to receive events
	 */
    public void removeSocketListenerOut(String host, SocketListener s) {
        getServentOut(host).removeSocketListener(s);
    }

    /**
	 * Adds a listener for a specific host's events
	 * 
	 * @param host
	 *            address of host to send events from
	 * @param s
	 *            SocketListener to receive events
	 */
    public void addSocketListenerIn(String host, SocketListener s) {
        getServentIn(host).addSocketListener(s);
    }

    /**
	 * Stop a listener from listening to a specific host's events
	 * 
	 * @param host
	 *            address of host to send events from
	 * @param s
	 *            SocketListener to receive events
	 */
    public void removeSocketListenerIn(String host, SocketListener s) {
        getServentIn(host).removeSocketListener(s);
    }

    /**
	 * Adds a listener to listen to all hosts' events
	 * 
	 * @param s
	 *            SocketListener to receive events
	 */
    public void addSocketListenerOut(SocketListener s) {
        Enumeration out = senders.elements();
        while (out.hasMoreElements()) {
            ((OutRadio) out.nextElement()).addSocketListener(s);
        }
    }

    /**
	 * Stop a listener from listening to a all hosts' events
	 * 
	 * @param s
	 *            SocketListener to receive events
	 */
    public void removeSocketListenerOut(SocketListener s) {
        Enumeration out = senders.elements();
        while (out.hasMoreElements()) {
            ((OutRadio) out.nextElement()).removeSocketListener(s);
        }
    }

    /**
	 * Adds a listener to listen to all hosts' events
	 * 
	 * @param s
	 *            SocketListener to receive events
	 */
    public void addSocketListenerIn(SocketListener s) {
        Enumeration in = senders.elements();
        while (in.hasMoreElements()) {
            ((InRadio) in.nextElement()).addSocketListener(s);
        }
    }

    /**
	 * Stop a listener from listening to a all hosts' events
	 * 
	 * @param s
	 *            SocketListener to receive events
	 */
    public void removeSocketListenerIn(SocketListener s) {
        Enumeration in = senders.elements();
        while (in.hasMoreElements()) {
            ((InRadio) in.nextElement()).removeSocketListener(s);
        }
    }
}
