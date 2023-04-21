package org.xsocket.stream;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.DataConverter;
import org.xsocket.QAUtil;
import org.xsocket.stream.BlockingConnection;
import org.xsocket.stream.IBlockingConnection;
import org.xsocket.stream.IDataHandler;
import org.xsocket.stream.IServer;
import org.xsocket.stream.INonBlockingConnection;
import org.xsocket.stream.Server;
import org.xsocket.stream.INonBlockingConnection.TransferResult;

/**
*
* @author grro@xsocket.org
*/
public final class TransferAvailableByDelimiterTest {

    private static final String DELIMITER = "\r\n\r\n\r";

    private static final String OK = "OK";

    @Test
    public void testSimple() throws Exception {
        DataSink dataSink = new DataSink();
        IServer server = new Server(new Handler(dataSink));
        StreamUtils.start(server);
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.setAutoflush(false);
        byte[] request1 = QAUtil.generateByteArray(60);
        connection.write(request1);
        connection.flush();
        QAUtil.sleep(200);
        byte[] received1 = DataConverter.toBytes(dataSink.getReceiveBuffers());
        Assert.assertTrue(QAUtil.isEquals(request1, received1));
        byte[] request2 = QAUtil.generateByteArray(20);
        connection.write(request2);
        connection.flush();
        QAUtil.sleep(200);
        byte[] received2 = DataConverter.toBytes(dataSink.getReceiveBuffers());
        Assert.assertTrue(QAUtil.isEquals(QAUtil.mergeByteArrays(request1, request2), received2));
        connection.write(DELIMITER);
        connection.flush();
        String okResponse = connection.readStringByDelimiter(DELIMITER, Integer.MAX_VALUE);
        Assert.assertTrue(okResponse.equals(OK));
        connection.close();
        server.close();
    }

    @Test
    public void testFile() throws Exception {
        File file = File.createTempFile("ere", "rsasd");
        file.deleteOnExit();
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        IServer server = new Server(new Handler(fc));
        StreamUtils.start(server);
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.setAutoflush(false);
        byte[] request = QAUtil.generateByteArray(60000);
        connection.write(request);
        connection.flush();
        connection.write(DELIMITER);
        connection.flush();
        connection.close();
        QAUtil.sleep(500);
        Assert.assertTrue(fc.size() == 60000);
        fc.close();
        file.delete();
        server.close();
    }

    private static final class DataSink implements WritableByteChannel {

        private final List<ByteBuffer> buffers = new ArrayList<ByteBuffer>();

        private boolean isOpen = true;

        public void close() throws IOException {
            isOpen = false;
        }

        public boolean isOpen() {
            return isOpen;
        }

        public int write(ByteBuffer buffer) throws IOException {
            int size = buffer.remaining();
            buffers.add(buffer);
            return size;
        }

        ByteBuffer[] getReceiveBuffers() {
            return buffers.toArray(new ByteBuffer[buffers.size()]);
        }
    }

    private static final class Handler implements IDataHandler {

        private WritableByteChannel dataSink = null;

        public Handler(WritableByteChannel dataSink) {
            this.dataSink = dataSink;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            TransferResult result = connection.transferAvailableByDelimiter(DELIMITER, dataSink);
            System.out.println(result.getWritten());
            if (result.isDelimiterFound()) {
                connection.write(OK);
                connection.write(DELIMITER);
            }
            return true;
        }
    }
}
