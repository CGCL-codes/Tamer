package org.ws4d.java.communication.udp;

import java.io.IOException;
import org.ws4d.java.util.concurrency.AbstractSemaphoreUser;

/**
 * Abstract class for the UDP Server.
 *
 */
public abstract class AbstractUDPServer extends AbstractSemaphoreUser {

    /**
	 * windows: you have to adjust the ASYNC_BUFFER_SIZE in
	 * kvm/VmExtra/h/async.h if you need more than 3000 bytes per packet.
	 */
    public static final int MAX_DGRAM_SIZE = 3000;

    /**
	 * Default constructor.
	 */
    public AbstractUDPServer() {
    }

    /**
	 * This method starts the receivers, each in a new thread.
	 */
    public abstract void startReceivers();

    /**
	 * This method stops the receivers and closes the datagram connections.
	 */
    public void stopReceivers() {
        setRunning(false);
    }

    /**
	 * Add a UDP multicast listener.
	 * 
	 * @param mcastlistener a reference to a listener.
	 */
    public abstract void addMulticastListener(DatagramListener mcastlistener);

    /**
	 * Remove a multicast listener.
	 * 
	 * @param mcastlistener the listener to be removed.
	 */
    public abstract void removeMulticastListener(DatagramListener mcastlistener);

    /**
	 * Add a UDP unicast listener.
	 * 
	 * @param ucastlistener a reference to a listener.
	 */
    public abstract void addUnicastListener(DatagramListener ucastlistener);

    /**
	 * Remove a unicast listener.
	 * 
	 * @param ucastlistener the listener to be removed.
	 */
    public abstract void removeUnicastListener(DatagramListener ucastlistener);

    /**
	 * Register a listener which will receive unicasts and multicasts.
	 * 
	 * @param acastlistener the listener to add.
	 */
    public abstract void addAnycastListener(DatagramListener acastlistener);

    /**
	 * Remove a listener from the unicast and multicast receiver lists.
	 * 
	 * @param acastlistener the listener to remove.
	 */
    public abstract void removeAnycastListener(DatagramListener acastlistener);

    /**
	 * This method can be used to send a datagram to a specified receiver.
	 * 
	 * @param receiverAddrs the address of the receiver of the datagram.
	 * @param data the data for the datagram.
	 * @throws IOException an I/O exception.
	 */
    public abstract void sendDatagram(String receiverAddrs, byte[] data) throws IOException;

    /**
	 * This method can be used to send a datagram to the DPWS defined multicast
	 * address.
	 * 
	 * @param data the data for the datagram.
	 * @throws IOException an I/O exception.
	 */
    public abstract void sendMulticastDatagram(byte[] data) throws IOException;

    /**
	 * The <code>OutputStream</code> returned by this method can be used to
	 * send datagrams to a receiver specified by receiverAddress.
	 * 
	 * @param receiverAddress the address of the datagram receiver in the form
	 *            (datagram://<host/ip>:<port>)
	 * @return a <code>DatagramOutputStream</code> object.
	 */
    public abstract AbstractDatagramOutputStream getOutputStream(String receiverAddress);

    /**
	 * The <code>OutputStream</code> returned by this method can be used to
	 * send datagrams to multicast receivers.
	 * 
	 * @return a <code>DatagramOutputStream</code> object.
	 */
    public abstract AbstractDatagramOutputStream getMulticastOutputStream();
}
