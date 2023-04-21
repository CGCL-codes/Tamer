package flexmud.net;

import flexmud.cfg.Preferences;
import flexmud.log.LoggingUtil;
import flexmud.util.Util;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class ClientCommunicatorStartStopTest {

    private static ClientCommunicator clientCommunicator;

    static {
        LoggingUtil.resetConfiguration();
        LoggingUtil.configureLogging(Preferences.getPreference(Preferences.LOG4J_CONFIG_FILE));
    }

    @Before
    public void setup() {
    }

    @After
    public void tearDown() {
        Util.pause(Util.CLIENT_SHUTDOWN_WAIT_TIME);
    }

    @Test
    public void testClientCommunicatorStarts() throws IOException {
        clientCommunicator = Util.getNewClientCommunicator();
        Assert.assertNotNull("Client communicator was not created", clientCommunicator);
        clientCommunicator.start();
        Assert.assertTrue("Client communicator is not running", clientCommunicator.isRunning());
        Assert.assertTrue("Client communicator cannot accept connections", clientCommunicator.canAcceptConnection());
        Assert.assertTrue("Client communicator will not run commands", clientCommunicator.shouldRunCommands());
    }

    @Test
    public void testClientCommunicatorStops() {
        if (clientCommunicator != null) {
            clientCommunicator.stop();
            Util.pause(Util.CLIENT_SHUTDOWN_WAIT_TIME);
        }
        Assert.assertNotNull("Client communicator should not be null", clientCommunicator);
        Assert.assertFalse("Client communicator should not be running", clientCommunicator.isRunning());
        Assert.assertFalse("Client communicator should not run commands", clientCommunicator.shouldRunCommands());
        clientCommunicator = null;
    }
}
