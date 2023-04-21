package jtmsctl.gbxremote2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

/**
 *
 * @author th
 */
public class GBXRemote2StreamHandler extends Thread {

    /**
   * Creates a new instance of GBXRemote2ConnectionHandler
   *
   * @param host
   * @param port
   *
   * @throws IOException
   */
    public GBXRemote2StreamHandler(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        in = socket.getInputStream();
        out = socket.getOutputStream();
        int size = readInt();
        if (size < 64) {
            char[] buffer = new char[64];
            InputStreamReader inputReader = new InputStreamReader(in);
            int bytesRead = inputReader.read(buffer, 0, size);
            if (bytesRead == size) {
                if (!"GBXRemote 2".equals(String.valueOf(buffer, 0, size))) {
                    throw new IOException("ERROR: Unsupported protocol type (" + String.valueOf(buffer, 0, size) + ")");
                }
            } else {
                throw new IOException("ERROR: Defect protocol data");
            }
        } else {
            throw new IOException("ERROR: Wrong protocol type");
        }
        this.responseMap = new HashMap<Integer, byte[]>();
        this.setDaemon(true);
        this.setPriority(Thread.MAX_PRIORITY);
        this.start();
    }

    /**
   * Method description
   *
   *
   * @throws IOException
   * @throws InterruptedException
   */
    public void close() throws IOException, InterruptedException {
        closed = true;
        this.join();
        socket.close();
    }

    /**
   * Method description
   *
   *
   * @param handle
   *
   * @return
   *
   * @throws IOException
   */
    public synchronized byte[] receiveGBXResponse(int handle) throws IOException {
        while (!responseMap.containsKey(handle)) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        byte[] responseData = (byte[]) responseMap.get(handle);
        responseMap.remove(handle);
        return responseData;
    }

    /**
   * Method description
   *
   */
    public void run() {
        while (!closed) {
            try {
                if (in.available() > 0) {
                    synchronized (this) {
                        int size = readInt();
                        int handle = readInt();
                        byte[] responseData = new byte[size];
                        in.read(responseData, 0, size);
                        if ((handle > 0) && (transport != null)) {
                            new EventShot(responseData, System.currentTimeMillis());
                        } else {
                            responseMap.put(handle, responseData);
                            notifyAll();
                        }
                    }
                }
                sleep(50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * Method description
   *
   *
   * @param handle
   * @param requestData
   *
   * @throws IOException
   */
    public synchronized void transmitGBXRequest(int handle, byte[] requestData) throws IOException {
        sendInt(requestData.length);
        sendInt(handle);
        out.write(requestData);
        out.flush();
    }

    /**
   * Method description
   *
   *
   * @param transport
   */
    void setTransport(GBXRemote2Transport transport) {
        this.transport = transport;
    }

    /**
   * Method description
   *
   *
   * @return
   *
   * @throws IOException
   */
    private synchronized int readInt() throws IOException {
        int value = 0;
        int c3 = in.read();
        int c2 = in.read();
        int c1 = in.read();
        value = in.read();
        value <<= 8;
        value += c1;
        value <<= 8;
        value += c2;
        value <<= 8;
        value += c3;
        return value;
    }

    /**
   * Method description
   *
   *
   * @param v
   *
   * @throws IOException
   */
    private synchronized void sendInt(int v) throws IOException {
        int c = v & 0x00ff;
        out.write((byte) c);
        c = (v >>> 8) & 0x00ff;
        out.write((byte) c);
        c = (v >>> 16) & 0x00ff;
        out.write((byte) c);
        c = (v >>> 24) & 0x00ff;
        out.write((byte) c);
        out.flush();
    }

    private class EventShot extends Thread {

        /**
     * Constructs ...
     *
     *
     * @param _requestData
     * @param _timestamp
     */
        EventShot(byte[] _requestData, long _timestamp) {
            requestData = _requestData;
            timestamp = _timestamp;
            setDaemon(true);
            start();
        }

        /**
     * Method description
     *
     */
        public void run() {
            transport.proceedCallback(requestData, timestamp);
        }

        private byte[] requestData;

        private long timestamp;
    }

    private GBXRemote2Transport transport = null;

    private boolean closed = false;

    private InputStream in;

    private OutputStream out;

    private HashMap<Integer, byte[]> responseMap;

    private Socket socket;
}
