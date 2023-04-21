package org.xsocket.connection.spi;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.util.concurrent.Executors;
import org.junit.Assert;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.QAUtil;
import org.xsocket.connection.BlockingConnection;
import org.xsocket.connection.IBlockingConnection;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.IDataHandler;
import org.xsocket.connection.IDisconnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.IServer;
import org.xsocket.connection.Server;
import org.xsocket.connection.ConnectionUtils;
import org.xsocket.connection.spi.IServerIoProvider;

/**
*
* @author grro@xsocket.org
*/
public final class Example {

    private static final String DELIMITER = "\r\n";

    private static final String KILL = "KILL";

    public static void main(String... args) throws Exception {
        new Example().testSimple();
    }

    public void testSimple() throws Exception {
        System.setProperty(IServerIoProvider.PROVIDER_CLASSNAME_KEY, ExampleServerIoProvider.class.getName());
        Handler handler = new Handler();
        IServer server = new Server(0, handler);
        server.setWorkerpool(Executors.newCachedThreadPool());
        ConnectionUtils.start(server);
        IBlockingConnection connection = new BlockingConnection("127.0.0.1", server.getLocalPort());
        String greeting = connection.readStringByDelimiter(DELIMITER);
        Assert.assertEquals("Helo", greeting);
        connection.write("it`s me, the client" + DELIMITER);
        String echo = connection.readStringByDelimiter(DELIMITER);
        Assert.assertEquals("it`s me, the client", echo);
        connection.write(KILL + DELIMITER);
        QAUtil.sleep(200);
        Assert.assertTrue(handler.isDisconnected);
        connection.close();
        server.close();
        System.out.println("example io simple simple test passed ");
    }

    private static final class Handler implements IConnectHandler, IDataHandler, IDisconnectHandler {

        private boolean isDisconnected = false;

        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            connection.write("Helo" + DELIMITER);
            return true;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            String message = connection.readStringByDelimiter(DELIMITER);
            if (message.equals(KILL)) {
                connection.close();
            } else {
                connection.write(message + DELIMITER);
            }
            return true;
        }

        public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
            isDisconnected = true;
            return true;
        }
    }
}
