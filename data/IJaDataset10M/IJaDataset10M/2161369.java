package org.xsocket.connection;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.DataConverter;
import org.xsocket.QAUtil;
import org.xsocket.connection.BlockingConnection;
import org.xsocket.connection.IBlockingConnection;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;
import org.xsocket.connection.ConnectionUtils;

/**
*
* @author grro@xsocket.org
*/
public final class ReadableTest {

    private static final String DELIMITER = "\r\n";

    @Test
    public void testLengthField() throws Exception {
        IServer server = new Server(new ServerHandler());
        ConnectionUtils.start(server);
        IBlockingConnection bc = new BlockingConnection("localhost", server.getLocalPort());
        bc.setAutoflush(false);
        byte[] request = QAUtil.generateByteArray(20);
        bc.write(request);
        bc.write(DELIMITER);
        bc.flush();
        ByteBuffer readBuffer = ByteBuffer.allocate(request.length);
        bc.read(readBuffer);
        readBuffer.flip();
        byte[] response = DataConverter.toBytes(readBuffer);
        Assert.assertTrue(QAUtil.isEquals(request, response));
        bc.close();
        server.close();
    }

    private static final class ServerHandler implements IDataHandler {

        public boolean onData(INonBlockingConnection connection) throws IOException {
            connection.write(connection.readByteBufferByDelimiter(DELIMITER, Integer.MAX_VALUE));
            return true;
        }
    }
}
