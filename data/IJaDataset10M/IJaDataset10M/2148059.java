package net.sf.karatasi.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import org.junit.Assert;

/** A simple client to test the file upload - goodcase and badcase tests. */
public class NioClient {

    /** send a PUT request, upload a file, and receive the server response.
     * @param fileForUpload
     * @param serverAddr
     * @return
     * @throws FileNotFoundException if the file for the FileInputStream was not found.
     * @throws IOException if th SocketChanel was not opened successfully.
     */
    public String handlePutRequestWithResponse(final File fileForUpload, final InetSocketAddress serverAddr) throws FileNotFoundException, IOException {
        final long fileLength = fileForUpload.length();
        final ByteBuffer requestBuffer = ByteBuffer.allocate((int) fileLength + 1024);
        final String requestHeader = "PUT /databases/test3?version=5&device=Aladin HTTP/1.1\r\n" + "Host: localhost\r\n" + "Authorization: Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==\r\n" + "Content-Type: application/octet-stream\r\n" + "Content-Length: " + fileLength + "\r\n" + "\r\n";
        requestBuffer.put(requestHeader.getBytes());
        final ByteBuffer responseBuffer = ByteBuffer.allocate(1024);
        final FileInputStream streamFromFile = new FileInputStream(fileForUpload);
        try {
            final FileChannel chanFromFile = streamFromFile.getChannel();
            try {
                chanFromFile.read(requestBuffer);
                final SocketChannel chanToServer = SocketChannel.open(serverAddr);
                try {
                    chanToServer.configureBlocking(false);
                    final Selector sel = Selector.open();
                    try {
                        final SelectionKey readyForWrite = chanToServer.register(sel, SelectionKey.OP_WRITE);
                        final SelectionKey readyForRead = chanToServer.register(sel, SelectionKey.OP_READ);
                        try {
                            boolean finished = false;
                            while (!finished) {
                                sel.select();
                                final Iterator<SelectionKey> availableOperations = sel.selectedKeys().iterator();
                                while (availableOperations.hasNext()) {
                                    final SelectionKey op = availableOperations.next();
                                    availableOperations.remove();
                                    if (!op.isValid()) {
                                        continue;
                                    }
                                    if (op.isWritable()) {
                                        if (!nioHandleWrite(op, requestBuffer)) {
                                            readyForWrite.cancel();
                                        }
                                    } else if (op.isReadable()) {
                                        if (!nioHandleRead(op, responseBuffer)) {
                                            finished = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        } finally {
                            readyForRead.cancel();
                            readyForWrite.cancel();
                            chanToServer.close();
                        }
                    } finally {
                        sel.close();
                    }
                } finally {
                    chanToServer.close();
                }
            } finally {
                chanFromFile.close();
            }
        } finally {
            streamFromFile.close();
        }
        if (!responseBuffer.hasArray()) {
            Assert.fail("readBuffer has no array - needs alternate implementation");
        }
        return responseBuffer.array().toString();
    }

    /** Private method to handle a Writable event.
     * @param writeKey the writable selection
     * @param writeBuffer the buffer to be sent
     * @return true if writing is indeed possible / false if not
     */
    private boolean nioHandleWrite(final SelectionKey writeKey, final ByteBuffer writeBuffer) {
        final SocketChannel socketChannel = (SocketChannel) writeKey.channel();
        int numWritten;
        try {
            numWritten = socketChannel.read(writeBuffer);
        } catch (final IOException e) {
            return false;
        }
        if (numWritten == -1) {
            return false;
        }
        return true;
    }

    /** Private method to handle a Readable event.
     * @param readKey the readable selection
     * @param readBuffer the buffer which collects the data
     * @return true if data could be read successfully / false if not
     */
    private boolean nioHandleRead(final SelectionKey readKey, final ByteBuffer readBuffer) {
        final SocketChannel socketChannel = (SocketChannel) readKey.channel();
        int numRead;
        try {
            numRead = socketChannel.read(readBuffer);
        } catch (final IOException e) {
            return false;
        }
        if (numRead == -1) {
            return false;
        }
        return true;
    }
}
