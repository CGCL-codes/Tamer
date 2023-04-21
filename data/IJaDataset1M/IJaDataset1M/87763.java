package ursus.io;

import ursus.io.crypto.Encryption;
import java.nio.channels.SocketChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.ByteBuffer;
import java.io.IOException;

/**
 * Basic implementation of IHandler, designed to be used by both Client and Server.
 * Should be extended to support client or server functionality.
 * @author Anthony
 */
public class Handler implements IHandler {

    /**
     * Any Listener that implements this type and is installed
     * will be called when a handler disconnects.
     *
     * @since 0.3a2
     */
    public static final String IOEVENT_DISCONNECT = "ioevent:disconnect";

    /**
     * Any Listener that implements this type and is installed will
     * be called when a handler connects.
     *
     * @since 0.3a2
     */
    public static final String IOEVENT_CONNECT = "ioevent:connect";

    /**
     * Any Listener that implements this type will be called when a handler
     * throws an error, the Object passed to the Listener will be a String
     * describing the error thrown.
     *
     * @since 0.3a2
     */
    public static final String IOEVENT_ERROR = "ioevent:error";

    protected SocketChannel socket;

    protected SelectionKey key;

    protected ByteBuffer bytein;

    protected ByteBuffer byteout;

    protected int toRead;

    protected byte[] currentObject;

    protected Encryption encryption;

    /**
     * Constructs a new Handler.
     * @param selector The selector this handler will attach itself to.
     * @param socket The SocketChannel this handler will use as an underlying connection.
     * @param bufferSize The size of the input and output buffers.
     * @param encryption The Encryption instance used to encrypt this session.
     *
     */
    public Handler(Selector selector, SocketChannel socket, int bufferSize, Encryption encryption) throws IOException {
        this.encryption = encryption;
        bytein = ByteBuffer.allocate(bufferSize);
        byteout = ByteBuffer.allocate(bufferSize);
        this.socket = socket;
        socket.configureBlocking(false);
        this.key = socket.register(selector, SelectionKey.OP_READ);
        key.attach(this);
        key.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
        read(IOEVENT_CONNECT, null);
    }

    public synchronized void run() {
        try {
            bytein.clear();
            int total = socket.read(bytein);
            bytein.flip();
            if (total != 0 && total != -1) {
                processRead();
            } else if (total == -1) disconnect();
        } catch (IOException e) {
            read(IOEVENT_ERROR, null);
            e.printStackTrace();
        }
    }

    public void read(String type, Object o) {
    }

    public void write(String type, Object o) {
        processWrite(type, o);
    }

    public SocketChannel getSocketChannel() {
        return socket;
    }

    public ByteBuffer getInputBuffer() {
        return bytein;
    }

    public ByteBuffer getOutputBuffer() {
        return byteout;
    }

    public SelectionKey getSelectionKey() {
        return key;
    }

    private void processRead() {
        if (toRead == 0) {
            byte[] lbytes = new byte[4];
            bytein.get(lbytes);
            int length = getInt(lbytes);
            currentObject = new byte[length];
            if (bytein.capacity() >= length + 4) {
                bytein.get(currentObject);
                processRead(currentObject);
                currentObject = null;
            } else {
                bytein.get(currentObject, 0, bytein.capacity() - 4);
                toRead = length - (bytein.capacity() - 4);
            }
        } else {
            if (bytein.capacity() >= toRead) {
                bytein.position(0);
                bytein.get(currentObject, currentObject.length - toRead, toRead);
                processRead(currentObject);
                currentObject = null;
                toRead = 0;
            } else {
                bytein.get(currentObject, currentObject.length - toRead, bytein.capacity());
                toRead = toRead - bytein.capacity();
            }
        }
    }

    private synchronized void processWrite(String type, Object o) {
        try {
            Encryption encryption = this.encryption;
            SocketChannel socket = this.socket;
            ByteBuffer byteout = this.byteout;
            java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();
            java.io.ObjectOutputStream out = new java.io.ObjectOutputStream(bout);
            out.writeObject(type);
            out.writeObject(o);
            out.flush();
            byte[] bytes = bout.toByteArray();
            bytes = encryption.encrypt(bytes);
            byte[] length = this.toByteArray(bytes.length);
            if (bytes.length <= byteout.capacity() - 4) {
                byteout.clear();
                byteout.put(length);
                byteout.put(bytes);
                byteout.position(0);
                socket.write(byteout);
            } else {
                byteout.clear();
                byteout.put(length);
                byteout.put(bytes, 0, byteout.capacity() - 4);
                byteout.position(0);
                socket.write(byteout);
                int toWrite = bytes.length - (byteout.capacity() - 4);
                while (toWrite != 0) {
                    if (toWrite <= byteout.capacity()) {
                        byteout.clear();
                        byteout.put(bytes, (bytes.length - toWrite), toWrite);
                        byteout.position(0);
                        socket.write(byteout);
                        toWrite = 0;
                    } else {
                        byteout.clear();
                        byteout.put(bytes, (bytes.length - toWrite), byteout.capacity());
                        toWrite = toWrite - byteout.capacity();
                        byteout.position(0);
                        socket.write(byteout);
                    }
                }
            }
        } catch (IOException e) {
            read(IOEVENT_ERROR, e);
            e.printStackTrace();
        }
    }

    private void processRead(byte[] bytes) {
        try {
            bytes = encryption.decrypt(bytes);
            java.io.ObjectInputStream in = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(bytes));
            String type = (String) in.readObject();
            Object object = in.readObject();
            read(type, object);
        } catch (IOException e) {
            read(IOEVENT_ERROR, e);
            e.printStackTrace();
        } catch (ClassNotFoundException ce) {
            read(IOEVENT_ERROR, ce);
            ce.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            read(IOEVENT_DISCONNECT, null);
            key.cancel();
            socket.socket().shutdownInput();
            socket.socket().shutdownOutput();
        } catch (IOException e) {
            read(IOEVENT_ERROR, e);
            e.printStackTrace();
        }
    }

    /**
     *  Converts an int using bitwise operations into a byte array.
     */
    protected byte[] toByteArray(int i) {
        byte[] array = new byte[4];
        array[0] = (byte) i;
        array[1] = (byte) (i >> 8);
        array[2] = (byte) (i >> 16);
        array[3] = (byte) (i >> 24);
        return array;
    }

    /**
     *  Reconstructs an int from a bitwise byte array;
     */
    protected int getInt(byte[] array) {
        int i = ((array[3] & 0xFF) << 24) | ((array[2] & 0xFF) << 16) | ((array[1] & 0xFF) << 8) | (array[0] & 0xFF);
        return i;
    }
}
