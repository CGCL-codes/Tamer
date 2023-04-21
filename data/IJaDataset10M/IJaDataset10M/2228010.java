package flexmud.engine.cmd;

import flexmud.cfg.Constants;
import flexmud.cfg.Preferences;
import flexmud.db.HibernateUtil;
import flexmud.engine.context.Message;
import flexmud.engine.exec.Executor;
import flexmud.log.LoggingUtil;
import flexmud.net.FakeClient;
import flexmud.net.FakeClientCommunicator;
import flexmud.util.Util;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.UUID;

public class TestMessageCommand {

    static {
        LoggingUtil.resetConfiguration();
        LoggingUtil.configureLogging(Preferences.getPreference(Preferences.LOG4J_TEST_CONFIG_FILE));
    }

    private Message message;

    @Before
    public void setup() {
        message = new Message();
        message.setName("test message");
        message.setMessage(UUID.randomUUID().toString());
        HibernateUtil.save(message);
    }

    @After
    public void tearDown() {
        HibernateUtil.delete(message);
    }

    @Test
    public void testMessageCommand() {
        FakeClientCommunicator fakeClientCommunicator = new FakeClientCommunicator();
        fakeClientCommunicator.setShouldInterceptWrite(true);
        FakeClient fakeClient = new FakeClient(fakeClientCommunicator, null);
        MessageCommand messageCommand = new MessageCommand();
        messageCommand.setClient(fakeClient);
        messageCommand.setCommandArguments(Arrays.asList(String.valueOf(message.getId())));
        Executor.exec(messageCommand);
        Util.pause(Util.ENGINE_WAIT_TIME);
        Assert.assertTrue("Message ID was not set, ergo was not saved", message.getId() != 0);
        Assert.assertEquals("Message did not output correctly", message.getMessage() + Constants.CRLF, fakeClientCommunicator.getLastSentText());
    }
}
