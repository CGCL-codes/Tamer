package org.xsocket.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.junit.Assert;
import org.junit.Test;
import org.xsocket.QAUtil;
import org.xsocket.connection.IConnectHandler;
import org.xsocket.connection.INonBlockingConnection;
import org.xsocket.connection.Server;

/**
*
* @author grro@xsocket.org
*/
public final class NotificationManualSynchronizedTest {

    @Test
    public void testSimple() throws Exception {
        Server server = new Server(new ServerHandler());
        server.start();
        ClientHandler hdl = new ClientHandler();
        INonBlockingConnection con = new NonBlockingConnection("localhost", server.getLocalPort(), hdl);
        con.write("CMD_CALCULATE\r\n");
        QAUtil.sleep(1000);
        String[] received = hdl.getReceived().toArray(new String[0]);
        for (String cmd : received) {
            Assert.assertTrue(cmd.startsWith("CMD_"));
        }
        con.close();
        server.close();
    }

    private static final class ClientHandler implements IDataHandler {

        private List<String> received = Collections.synchronizedList(new ArrayList<String>());

        public boolean onData(INonBlockingConnection connection) throws IOException {
            received.add(connection.readStringByDelimiter("\r\n"));
            return true;
        }

        public List<String> getReceived() {
            return received;
        }
    }

    private static final class ServerHandler implements IConnectHandler, IDataHandler, IDisconnectHandler {

        private final Timer timer = new Timer(true);

        public boolean onConnect(INonBlockingConnection connection) throws IOException {
            TimeNotifier notifier = new TimeNotifier(connection);
            timer.schedule(notifier, 10, 10);
            synchronized (connection) {
                connection.setAttachment(notifier);
            }
            return true;
        }

        public boolean onDisconnect(INonBlockingConnection connection) throws IOException {
            TimeNotifier notifier;
            synchronized (connection) {
                notifier = (TimeNotifier) connection.getAttachment();
            }
            if (notifier != null) {
                notifier.cancel();
            }
            return true;
        }

        public boolean onData(INonBlockingConnection connection) throws IOException {
            String cmd;
            synchronized (connection) {
                cmd = connection.readStringByDelimiter("\r\n");
            }
            connection.write(cmd + ":4545\r\n");
            return true;
        }
    }

    private static final class TimeNotifier extends TimerTask {

        private INonBlockingConnection connection = null;

        public TimeNotifier(INonBlockingConnection connection) {
            this.connection = connection;
        }

        @Override
        public void run() {
            try {
                synchronized (connection) {
                    connection.write("CMD_TIME:" + System.currentTimeMillis() + "\r\n");
                }
            } catch (Exception e) {
                System.out.println("error occured by sending time to " + connection.getRemoteAddress() + ":" + connection.getRemotePort());
            }
        }
    }
}
