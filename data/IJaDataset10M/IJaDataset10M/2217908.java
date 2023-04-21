package org.xsocket.stream;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.SSLTestContextFactory;
import org.xsocket.TestUtil;
import org.xsocket.stream.BlockingConnection;
import org.xsocket.stream.IBlockingConnection;
import org.xsocket.stream.IConnectHandler;
import org.xsocket.stream.IDataHandler;
import org.xsocket.stream.IMultithreadedServer;
import org.xsocket.stream.INonBlockingConnection;
import org.xsocket.stream.MultithreadedServer;

/**
*
* @author grro@xsocket.org
*/
public final class DelayedSSLTest {

    private static final int WRITE_RATE = 3;

    private static final String DELIMITER = System.getProperty("line.separator");

    @Test
    public void testXSocket() throws Exception {
        IMultithreadedServer sslTestServer = new MultithreadedServer(0, new SSLHandler(), true, new SSLTestContextFactory().getSSLContext());
        new Thread(sslTestServer).start();
        IBlockingConnection connection = new BlockingConnection(sslTestServer.getLocalAddress(), sslTestServer.getLocalPort(), new SSLTestContextFactory().getSSLContext(), true);
        connection.setAutoflush(true);
        connection.write("test" + DELIMITER);
        long start = System.currentTimeMillis();
        String response = connection.readStringByDelimiter(DELIMITER, Integer.MAX_VALUE);
        TestUtil.assertTimeout(System.currentTimeMillis() - start, 1800, 3700);
        Assert.assertEquals("test", response);
        connection.write("test2" + DELIMITER);
        start = System.currentTimeMillis();
        response = connection.readStringByDelimiter(DELIMITER, Integer.MAX_VALUE);
        TestUtil.assertTimeout(System.currentTimeMillis() - start, 2000, 3800);
        Assert.assertEquals("test2", response);
        connection.close();
        sslTestServer.close();
    }

    private static final class SSLHandler implements IDataHandler, IConnectHandler {

        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            connection.setWriteTransferRate(WRITE_RATE);
            return false;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            String word = connection.readStringByDelimiter(DELIMITER, Integer.MAX_VALUE);
            connection.write(word + DELIMITER);
            return true;
        }
    }
}
