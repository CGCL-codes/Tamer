package org.tanso.ts.base;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * The generic socket server which can use TCP or UDP in underlying transport.
 * Each server can be binded to a single port which will be listened by a
 * thread.
 * 
 * @author Haiping Huang
 */
public abstract class GenericSocketServer implements Runnable {

    private ServerSocket serverTCP;

    private DatagramSocket serverUDP;

    /**
	 * UDP buffer
	 */
    private byte[] recvBuf;

    /**
	 * Flag to control the server
	 */
    private volatile boolean running = false;

    /**
	 * port number to listen
	 */
    private int port = 0;

    /**
	 * Working mode. Default is TCP
	 */
    private int mode = 0;

    private Thread listenerThread;

    public void run() {
        switch(mode) {
            case SocketProtocol.TCP:
                run_TCP();
                break;
            case SocketProtocol.UDP:
                run_UDP();
                break;
        }
    }

    /**
	 * Test whether the server is running.
	 * 
	 * @return true: is running; false: not running
	 */
    public boolean isRunning() {
        return this.running;
    }

    private void run_TCP() {
        Socket client = null;
        while (running) {
            try {
                serverTCP.setSoTimeout(5000);
                client = serverTCP.accept();
            } catch (SocketTimeoutException e) {
                continue;
            } catch (IOException e) {
                try {
                    this.serverTCP.close();
                    this.running = false;
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            onRecvTCP(client);
        }
        try {
            this.serverTCP.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void run_UDP() {
        recvBuf = new byte[this.getUDPBufSize()];
        DatagramPacket ip = new DatagramPacket(recvBuf, getUDPBufSize());
        while (running) {
            try {
                serverUDP.receive(ip);
            } catch (IOException e) {
                serverUDP.close();
                this.running = false;
            }
            onRecvUDP(ip);
        }
        this.serverUDP.close();
        this.recvBuf = null;
        ip = null;
    }

    /**
	 * Get the buffer size for UDP packet
	 * 
	 * @return Buffer size
	 */
    protected int getUDPBufSize() {
        return 1000;
    }

    /**
	 * Start listen on a port.
	 * 
	 * @param mode
	 *            Specify using TCP or UDP
	 * @param port
	 *            The port number.
	 * @return true: listening started. false: error happens (already running,
	 *         port in use, etc.)
	 */
    public boolean listen(int mode, int portNum) {
        if (running) {
            return false;
        }
        this.mode = mode;
        this.port = portNum;
        switch(mode) {
            case SocketProtocol.TCP:
                return listen_TCP(portNum);
            case SocketProtocol.UDP:
                return listen_UDP(portNum);
            default:
                return false;
        }
    }

    private boolean listen_TCP(int portNum) {
        try {
            serverTCP = new ServerSocket(portNum, 50, null);
            running = true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        listenerThread = new Thread(this);
        listenerThread.setName("TCPListenerThread");
        listenerThread.start();
        return true;
    }

    private boolean listen_UDP(int portNum) {
        try {
            serverUDP = new DatagramSocket(portNum, null);
            running = true;
        } catch (SocketException e) {
            e.printStackTrace();
            return false;
        }
        listenerThread = new Thread(this);
        listenerThread.setName("UDPListenerThread");
        listenerThread.start();
        return true;
    }

    /**
	 * Set the flag for stop, stops the listening thread on the next packet. If
	 * the subclass want to stop immediately, please override this method with a
	 * super call first
	 */
    public void stop() {
        this.running = false;
    }

    /**
	 * Get the listening port
	 * 
	 * @return The listening port
	 */
    public int getPort() {
        return this.port;
    }

    /**
	 * Get the working mode: TCP / UDP
	 * 
	 * @return working mode
	 */
    public int getMode() {
        return this.mode;
    }

    /**
	 * Set the protocol for the Server. This can be done only when the server
	 * isn't running.
	 * 
	 * @param mode
	 *            SocketProtocol.TCP or SocketProtocol.UDP
	 */
    public void setMode(int mode) {
        if (false == this.running) {
            this.mode = mode;
        }
    }

    /**
	 * Connect to a TCP server
	 * 
	 * @param ip
	 *            The IP address of the server
	 * @param port
	 *            The server's port
	 * @return The connected socket. null if connect error
	 */
    protected Socket connectToTCP(String ip, int port) {
        Socket client = null;
        try {
            client = new Socket(InetAddress.getByName(ip), port);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return client;
    }

    /**
	 * Send a UDP packet.
	 * 
	 * @param packet
	 *            The packet to be sent.
	 * @return true: send ok. false: send error
	 */
    protected boolean sendUDPPacket(DatagramPacket packet) {
        try {
            this.serverUDP.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
	 * Dealing with a new TCP connection. The derived implementation should
	 * close the connection after receiving the packets.
	 * 
	 * @param clientSocket
	 *            The newly connected client
	 */
    protected abstract void onRecvTCP(Socket clientSocket);

    /**
	 * Dealing with a new UDP packet
	 * 
	 * @param ip
	 *            The incoming UDP packet
	 */
    protected abstract void onRecvUDP(DatagramPacket ip);
}
