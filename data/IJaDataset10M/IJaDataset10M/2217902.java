package org.xsocket.server;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xsocket.IBlockingConnection;
import org.xsocket.BlockingConnection;
import org.xsocket.INonBlockingConnection;

/**
*
* @author grro@xsocket.org
*/
public final class ConnectionScopedTest extends AbstractServerTest {

    private static IMultithreadedServer server = null;

    private static int id = 0;

    @BeforeClass
    public static void setUp() {
        server = createServer(6372, new ServerHandler());
    }

    @AfterClass
    public static void tearDown() {
        server.shutdown();
    }

    @Test
    public void testMixed() throws Exception {
        setUp();
        IBlockingConnection connection = new BlockingConnection("127.0.0.1", server.getPort());
        int sessionId = connection.receiveInt();
        connection.write((long) 5);
        Assert.assertEquals(sessionId, connection.receiveInt());
        IBlockingConnection connection2 = new BlockingConnection("127.0.0.1", server.getPort());
        int sessionId2 = connection2.receiveInt();
        Assert.assertFalse(sessionId == sessionId2);
        connection.write((long) 8);
        Assert.assertEquals(sessionId, connection.receiveInt());
        connection2.write((long) 34);
        Assert.assertEquals(sessionId2, connection2.receiveInt());
        connection.close();
        connection2.close();
        tearDown();
    }

    private static final class ServerHandler implements IDataHandler, IConnectHandler, IConnectionScoped {

        private int sessionId = 0;

        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            sessionId = ++id;
            connection.write((int) sessionId);
            return true;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException {
            connection.readAvailable();
            connection.write((int) sessionId);
            return true;
        }

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
