package org.openqa.selenium.server;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Test;

/**
 * Unit tests for SeleniumServer.
 * 
 * @author Matthew Purland
 */
public class SeleniumServerUnitTest {

    private int positiveJettyThreads = SeleniumServer.DEFAULT_JETTY_THREADS;

    private SeleniumServer server;

    @Test
    public void constructor_setsThisAsSeleniumServerInRemoteControlConfiguration() throws Exception {
        RemoteControlConfiguration remoteConfiguration = new RemoteControlConfiguration();
        server = new SeleniumServer(remoteConfiguration);
        assertEquals(server, remoteConfiguration.getSeleniumServer());
    }

    @After
    public void tearDown() {
        if (server != null) {
            server.stop();
        }
    }

    /**
	 * Test happy path that if an "okay" number of threads is given then it will
	 * start up correctly.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testJettyThreadsPositive() throws Exception {
        RemoteControlConfiguration configuration = new RemoteControlConfiguration();
        configuration.setJettyThreads(positiveJettyThreads);
        server = new SeleniumServer(configuration);
        server.start();
        assertEquals("Jetty threads given is not correct.", positiveJettyThreads, server.getJettyThreads());
    }
}
