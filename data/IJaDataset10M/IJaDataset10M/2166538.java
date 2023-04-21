package org.xsocket.stream;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.ClosedConnectionException;
import org.xsocket.MaxReadSizeExceededException;
import org.xsocket.QAUtil;

/**
*
* @author grro@xsocket.org
*/
public final class HandlerThrowsIOExceptionTest {

    private static final String DELIMITER = "\r";

    private static final String THROW_EXCPTION_CMD = "throw a io exception!";

    @Test
    public void testDataHandler() throws Exception {
        Handler hdl = new Handler();
        IServer server = new Server(hdl);
        StreamUtils.start(server);
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.write("some cmd" + DELIMITER);
        String result = connection.readStringByDelimiter(DELIMITER);
        Assert.assertEquals(result, "some cmd");
        try {
            connection.write(THROW_EXCPTION_CMD + DELIMITER);
            QAUtil.sleep(100);
            Assert.assertFalse(hdl.connection.isOpen());
            String result2 = connection.readStringByDelimiter(DELIMITER);
            Assert.fail("a ClosedConnectionException should have been thrown");
        } catch (ClosedConnectionException shouldbeThrown) {
        }
        connection.close();
        server.close();
    }

    private static final class Handler implements IConnectHandler, IDataHandler {

        private INonBlockingConnection connection = null;

        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            this.connection = connection;
            return true;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException, BufferUnderflowException, MaxReadSizeExceededException {
            String cmd = connection.readStringByDelimiter(DELIMITER);
            if (cmd.equals(THROW_EXCPTION_CMD)) {
                throw new IOException("kill");
            } else {
                connection.write(cmd + DELIMITER);
            }
            return true;
        }
    }
}
