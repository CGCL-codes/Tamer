package com.sesca.voip.transport;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.SocketException;
import java.nio.ByteBuffer;
import org.zoolu.net.IpAddress;
import com.sesca.misc.Logger;

public class HttpTunnelConnection extends Thread {

    private boolean initialized = false;

    /** The reading buffer size */
    protected static final int BUFFER_SIZE = 65535;

    /**
	 * Default value for the maximum time that the tcp connection can remain
	 * active after been halted (in milliseconds)
	 */
    public static final int DEFAULT_SOCKET_TIMEOUT = 2000;

    /** The TCP socket */
    protected HttpTunnelSocket socket;

    /**
	 * Maximum time that the connection can remain active after been halted (in
	 * milliseconds)
	 */
    protected int socket_timeout;

    /**
	 * Maximum time that the connection remains active without receiving data (in
	 * milliseconds)
	 */
    protected long alive_time;

    /** InputStream/OutputStream error */
    protected Exception error;

    /** Whether it has been halted */
    protected boolean stop;

    /** Whether it is running */
    protected boolean is_running;

    protected ByteBuffer outputBuffer;

    protected ByteBuffer inputBuffer;

    /** TcpConnection listener */
    protected HttpTunnelConnectionListener listener;

    public HttpTunnelConnection(HttpTunnelSocket socket, HttpTunnelConnectionListener listener) {
        this.setPriority(8);
        Logger.info("HttpConnection2 constructed");
        init(socket, 0, listener);
        start();
    }

    public HttpTunnelConnection(HttpTunnelSocket socket, long alive_time, HttpTunnelConnectionListener listener) {
        this.setPriority(8);
        Logger.info("HttpConnection1 constructed");
        init(socket, alive_time, listener);
        start();
    }

    /** Runs the http tunnel receiver */
    public void run() {
        this.setPriority(8);
        long expire = 0;
        if (alive_time > 0) expire = System.currentTimeMillis() + alive_time;
        try {
            if (error != null) throw error;
            while (!this.stop) {
                int len = 0;
                if (true) {
                    try {
                        inputBuffer.clear();
                        len = socket.channel.read(inputBuffer);
                    } catch (InterruptedIOException ie) {
                        if (alive_time > 0 && System.currentTimeMillis() > expire) {
                            Logger.warning("Connection closed due to timeout.");
                            halt();
                        }
                        continue;
                    } catch (SocketException se) {
                        Logger.error("Socketti on kiinni");
                        halt();
                    }
                }
                if (len < 0) {
                    Logger.warning("No incoming traffic. HttpTunnelConnection falls into sleep.");
                    Thread.sleep(100);
                } else if (len > 0) {
                    inputBuffer.flip();
                    byte[] bbuff = new byte[BUFFER_SIZE];
                    inputBuffer.get(bbuff, 0, len);
                    String tunniste = new String(bbuff, 0, 4);
                    String message = new String(bbuff);
                    Logger.hysteria("\nIncoming packet:");
                    Logger.hysteria(message);
                    Logger.hysteria("Timestamp:" + System.currentTimeMillis());
                    Logger.hysteria("Frame size:" + len);
                    if (!tunniste.equals("HTTP")) {
                        if (listener != null) listener.onReceivedData(this, bbuff, len); else Logger.warning("HttpTunnelConnection: listener=null");
                    } else {
                        Logger.hysteria(bbuff.toString());
                    }
                    if (alive_time > 0) expire = System.currentTimeMillis() + alive_time;
                } else if (len == 0) {
                    Thread.sleep(2);
                }
            }
        } catch (Exception e) {
            error = e;
            Logger.warning("HTTP tunnel connection closed unexpectedly");
            e.printStackTrace();
            this.stop = true;
        }
        is_running = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (listener != null) listener.onConnectionTerminated(this, error);
        listener = null;
    }

    /** Sends data */
    public void send(byte[] buff, int offset, int len, int flush) throws IOException {
        if (!this.stop) {
            if ((flush & 1) == 1) {
            }
            outputBuffer.clear();
            outputBuffer.put(buff);
            outputBuffer.flip();
            socket.channel.write(outputBuffer);
            if ((flush & 2) == 2) {
            }
        }
    }

    /** Sends data */
    public void send(byte[] buff, int flush) throws IOException {
        send(buff, 0, buff.length, flush);
    }

    /** Stops running */
    public void halt() {
        this.stop = true;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Logger.warning("HttpTunnelConnection.halt() HTTP tunnel connection closed.");
    }

    /** Gets the remote IP address */
    public IpAddress getRemoteAddress() {
        return socket.getAddress();
    }

    /** Gets the remote port */
    public int getRemotePort() {
        return socket.getPort();
    }

    /** Gets the TcpSocket */
    public HttpTunnelSocket getSocket() {
        return socket;
    }

    private void init(HttpTunnelSocket socket, long alive_time, HttpTunnelConnectionListener listener) {
        if (!initialized) {
            initialized = true;
            this.listener = listener;
            this.socket = socket;
            this.socket_timeout = DEFAULT_SOCKET_TIMEOUT;
            this.alive_time = alive_time;
            this.stop = false;
            this.is_running = true;
            outputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
            this.error = null;
            try {
            } catch (Exception e) {
                error = e;
            }
        }
    }

    /** Gets a String representation of the Object */
    public String toString() {
        return "http:" + socket.getLocalAddress() + ":" + socket.getLocalPort() + "<->" + socket.getAddress() + ":" + socket.getPort();
    }

    /** Whether the service is running */
    public boolean isRunning() {
        return is_running;
    }
}
