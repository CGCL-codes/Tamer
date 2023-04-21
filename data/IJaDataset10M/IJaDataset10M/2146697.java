package com.peterhi;

import com.peterhi.client.DatagramClient;
import com.peterhi.client.SocketClient;
import com.peterhi.client.DatagramSession;
import com.peterhi.net.Protocol;
import com.peterhi.net.messages.UdpMessage;
import com.peterhi.net.messages.UdpResponse;
import com.peterhi.server.SocketServer;
import java.net.InetSocketAddress;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author YUN TAO
 */
public class UdpResponseTest {

    private static Object lock = new Object();

    private static boolean succeeded;

    private static SocketClient client;

    private static SocketServer server;

    public UdpResponseTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        DatagramClient.getInstance().addHandler(Protocol.UDP_RESPONSE, new UdpResponseHandler());
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        client.close();
        server.close();
    }

    @Before
    public void setUp() {
        try {
            server.start(9080);
            client.connect(new InetSocketAddress("localhost", 9080));
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    @After
    public void tearDown() {
        try {
            client.close();
            server.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Test
    public void testUdpResponse() {
        try {
            UdpMessage message = new UdpMessage();
            message.id = 6;
            DatagramClient.getInstance().post(message);
            synchronized (lock) {
                lock.wait(5000);
                if (!succeeded) {
                    fail();
                    return;
                }
                doAsserts();
                System.out.println("done!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            fail();
        }
    }

    private void doAsserts() {
    }

    static class UdpResponseHandler implements com.peterhi.client.DatagramHandler<UdpResponse> {

        public void handle(DatagramSession sess, UdpResponse message) {
            assert (message.code == Code.OK);
            succeeded = true;
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }
}
