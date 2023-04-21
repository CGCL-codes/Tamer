package org.apache.harmony.luni.net;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketImpl;
import org.apache.harmony.luni.internal.nls.Messages;

class SocketOutputStream extends OutputStream {

    private PlainSocketImpl socket;

    /**
     * Constructs a SocketOutputStream for the <code>socket</code>. Write
     * operations are forwarded to the <code>socket</code>.
     * 
     * @param socket the socket to be written
     * @see Socket
     */
    public SocketOutputStream(SocketImpl socket) {
        super();
        this.socket = (PlainSocketImpl) socket;
    }

    @Override
    public void close() throws IOException {
        socket.close();
    }

    @Override
    public void write(byte[] buffer) throws IOException {
        socket.write(buffer, 0, buffer.length);
    }

    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
        if (buffer != null) {
            if (0 <= offset && offset <= buffer.length && 0 <= count && count <= buffer.length - offset) {
                socket.write(buffer, offset, count);
            } else {
                throw new ArrayIndexOutOfBoundsException(Messages.getString("luni.13"));
            }
        } else {
            throw new NullPointerException(Messages.getString("luni.11"));
        }
    }

    @Override
    public void write(int oneByte) throws IOException {
        byte[] buffer = new byte[1];
        buffer[0] = (byte) (oneByte & 0xFF);
        socket.write(buffer, 0, 1);
    }
}
