package org.shiftone.jrat.core.web.http;

import org.shiftone.jrat.util.io.IOUtil;
import org.shiftone.jrat.util.log.Logger;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ConnectionAttachment {

    private static final Logger LOG = Logger.getLogger(Server.class);

    private SelectionKey key;

    private ByteBuffer readBuffer;

    private ByteBuffer writeBuffer;

    private Request request;

    private Response response;

    public ConnectionAttachment() {
        readBuffer = ByteBuffer.allocate(IOUtil.DEFAULT_BUFFER_SIZE);
        writeBuffer = ByteBuffer.allocate(IOUtil.DEFAULT_BUFFER_SIZE * 4);
        request = new Request();
        response = new Response(new WritingOutputStream());
    }

    public void setSelectionKey(SelectionKey key) throws IOException {
        this.key = key;
    }

    public boolean readRequest() throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        int numRead;
        try {
            numRead = channel.read(readBuffer);
        } catch (IOException ex) {
            numRead = -1;
        }
        if (numRead == -1) {
            key.cancel();
            channel.close();
        } else {
            if (!request.readInput(new ByteArrayInputStream(readBuffer.array(), 0, readBuffer.position()))) {
                writeBuffer.clear();
                key.interestOps(key.interestOps() & ~SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                return true;
            }
        }
        return false;
    }

    public void writeRequest() throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        writeBuffer.flip();
        channel.write(writeBuffer);
        if (writeBuffer.position() == writeBuffer.limit()) {
            key.cancel();
            channel.close();
        } else {
            writeBuffer.compact();
        }
    }

    private class WritingOutputStream extends OutputStream {

        public void write(byte b[]) throws IOException {
            writeBuffer.put(b);
        }

        public void write(byte b[], int off, int len) throws IOException {
            writeBuffer.put(b, off, len);
        }

        public void write(int b) throws IOException {
            writeBuffer.put((byte) b);
        }
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
