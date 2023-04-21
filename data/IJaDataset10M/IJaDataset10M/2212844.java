package com.noahsloan.nutils.net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * UdpSocketListener is the base class for servers/listeners that receive UDP
 * packets. It automaticaly rebinds (up to {@link #MAX_TRIES} times) and has
 * hooks with default behavior for handling each kind of exception condition.
 * 
 * @author noah
 * 
 */
public abstract class UdpSocketListener implements Runnable {

    private static final int DEFAULT_MAX_TRIES = 5;

    /**
	 * The default buffer size used for the datagram packets. 64k is the current
	 * maximum due to the length of the payload length UDP header.
	 * 
	 * @see #newPacket()
	 */
    public static final int PACKET_SIZE = 65535;

    /**
	 * Maximum attempt this listener will make to bind the the socket.
	 * 
	 * @see #resetTries()
	 */
    public final int MAX_TRIES;

    private SocketAddress address;

    private boolean shutdown = false;

    private int tries;

    private UdpSocketListener() {
        MAX_TRIES = DEFAULT_MAX_TRIES;
    }

    /**
	 * Create a UDP listener on the given port.
	 * 
	 * @param port
	 */
    public UdpSocketListener(int port) {
        this(new InetSocketAddress(port));
    }

    /**
	 * Configure this listener with the given hostname and port to bind to.
	 * 
	 * @param hostname
	 * @param port
	 */
    public UdpSocketListener(String hostname, int port) {
        this(new InetSocketAddress(hostname, port));
    }

    /**
	 * Configure this listener with the given bind address.
	 * 
	 * @param address
	 */
    public UdpSocketListener(SocketAddress address) {
        this(address, DEFAULT_MAX_TRIES);
    }

    /**
	 * If you do want to specify a different value of maxTries, tough it out and
	 * create a SocketAddress.
	 * 
	 * @param address
	 *            the address to bind to.
	 * @param maxTries
	 *            the greatest number of times we should attempt to bind to the
	 *            SocketAddress. Note that setting this to a value less that 1
	 *            will result in no action.
	 */
    public UdpSocketListener(SocketAddress address, int maxTries) {
        this();
        this.address = address;
    }

    /**
	 * Attempts to bind and rebind to the preconfigured socket address. Various
	 * events are handled by the protected methods
	 * {@link #handleIOException(IOException)},
	 * {@link #handleSocketCreateException(SocketException)}, and
	 * {@link #handlePacket(DatagramPacket, DatagramSocket)}.
	 * <p>
	 * Creation of packets is handled by {@link #newPacket()} and sockets by
	 * {@link #newSocket(SocketAddress)}.
	 * 
	 * @see #MAX_TRIES
	 */
    public void run() {
        tries = 0;
        while (!shutdown && tries < MAX_TRIES) {
            try {
                DatagramSocket socket = newSocket(address);
                while (socket.isBound()) {
                    try {
                        DatagramPacket p = newPacket();
                        socket.receive(p);
                        handlePacket(p, socket);
                    } catch (IOException e) {
                        handleIOException(e);
                    }
                }
                tries++;
            } catch (SocketException e) {
                handleSocketCreateException(e);
            }
        }
    }

    /**
	 * Resets the try count. In other words, sets the internal state such that
	 * it is though no bind attempt has ever failed.
	 */
    protected final void resetTries() {
        tries = 0;
    }

    /**
	 * Hands a received packet off to the subclass code. No thread creation
	 * occurs, so this method blocks all other recieving. Subclasses should
	 * immeditely create a new Thread if this behavior is not desired.
	 * 
	 * @param p
	 *            the packet.
	 * @param socket
	 *            the socket the packet was received on.
	 * @throws IOException
	 *             if an IOError occurs
	 */
    protected abstract void handlePacket(DatagramPacket p, DatagramSocket socket) throws IOException;

    /**
	 * Used to create a new DatagramPacket.
	 * <p>
	 * Intended to be overriden by subclasses that are not happy with the
	 * default packet size {@link #PACKET_SIZE}.
	 * 
	 * @return a new DatagramPacket
	 * @see #PACKET_SIZE
	 */
    protected DatagramPacket newPacket() {
        byte[] buffer = new byte[PACKET_SIZE];
        return new DatagramPacket(buffer, buffer.length);
    }

    /**
	 * <p>
	 * After this method is called, the code will attempt to rebind the socket
	 * (assuming the IOException caused it to become unbound) only if
	 * {@link #shutdown()} has not been called and if {@link #MAX_TRIES}
	 * attempts to rebind have not already occured.
	 * 
	 * @param e
	 */
    protected void handleIOException(IOException e) {
        e.printStackTrace();
    }

    /**
	 * Handle any expection thrown by the creation of the socket. It is
	 * permissible to call shutdown() here if you do not wish to retry the
	 * binding.
	 * <p>
	 * You may also introduce delays if you do not wish to attempt rebinding
	 * immediately.
	 * <p>
	 * By default this method simply prints the stack trace.
	 * 
	 * @param e
	 *            the exception thrown.
	 */
    protected void handleSocketCreateException(SocketException e) {
        e.printStackTrace();
    }

    /**
	 * Meant to be overridden by sub-classes that want to use a subclass of
	 * datagram socket. The returned DatagramSocket should be bound to the given
	 * SocketAddress.
	 * 
	 * @param address
	 * @return a bound DatagramSocket
	 * @throws SocketException
	 */
    protected DatagramSocket newSocket(SocketAddress address) throws SocketException {
        return new DatagramSocket(address);
    }

    /**
	 * Shuts down this listener.
	 */
    public void shutdown() {
        this.shutdown = true;
    }

    /**
	 * When run, this class binds to a port number (first command line
	 * parameter) and prints the payload of every packet to STD Out.
	 * 
	 * This is meant to serve as a simple example of using UdpSocketListener.
	 * 
	 * @param args
	 *            1 argument, the port number to listen on.
	 */
    public static void main(String[] args) {
        try {
            final int port = Integer.parseInt(args[0]);
            new UdpSocketListener() {

                private int count = 0;

                @Override
                protected DatagramSocket newSocket(SocketAddress address) throws SocketException {
                    return super.newSocket(new InetSocketAddress(port));
                }

                @Override
                protected void handlePacket(DatagramPacket p, DatagramSocket socket) {
                    System.out.printf("\nMessage %d\n==============\n", count++);
                    System.out.write(p.getData(), p.getOffset(), p.getLength());
                }
            }.run();
        } catch (Exception e) {
            System.err.println("Parameter: <port number - required>");
        }
    }
}
