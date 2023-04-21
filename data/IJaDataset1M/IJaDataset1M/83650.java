package com.coladoro.plugin.ed2k.tests.protocol;

import org.apache.mina.common.CloseFuture;
import org.apache.mina.common.ConnectFuture;
import org.apache.mina.common.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.coladoro.plugin.ed2k.Ed2kPlugin;
import com.coladoro.plugin.ed2k.server.Ed2kServer;
import com.coladoro.plugin.ed2k.session.Ed2kServerSessionInitializer;

/**
 * @author tanis
 */
public class Ed2kServerConnectionTest {

    /**
     * Logger of the test.
     */
    final Logger logger = LoggerFactory.getLogger(Ed2kServerConnectionTest.class);

    private IoSession session;

    private Ed2kPlugin plugin;

    /**
     * Starts the connectors
     * 
     * @throws Exception
     */
    @BeforeTest
    public void init() throws Exception {
        plugin = new Ed2kPlugin();
        plugin.startReceiver(PORT);
        plugin.startSender();
    }

    /**
     * Port for testing purposes.
     */
    public static final int PORT = 18888;

    @Test
    public void testConnection() throws Exception {
        Ed2kServer testServer = new Ed2kServer("Emule serverlist 1", "193.138.221.213", 4242, 0);
        Ed2kPlugin.getGlobalSession().setConnectingServer(testServer);
        ConnectFuture connectFuture = plugin.getConnector().connect(testServer.getServerAddress(), new Ed2kServerSessionInitializer());
        logger.info("Connecting to the server: " + testServer);
        connectFuture.awaitUninterruptibly();
        session = connectFuture.getSession();
        Thread.sleep(5500);
        if (Ed2kPlugin.getGlobalSession().getServerSession() != null && Ed2kPlugin.getGlobalSession().getServerSession().equals(session)) {
            assert true;
        } else {
            assert false;
        }
    }

    /**
     * Ends the connectors.
     */
    @AfterTest
    public void end() throws Exception {
        CloseFuture future = session.close();
        plugin.end();
        future.awaitUninterruptibly();
    }
}
