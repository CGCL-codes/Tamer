package subethaclipse.net.command.test;

import org.easymock.MockControl;
import subethaclipse.net.ConnectionManager;
import subethaclipse.net.IConnectionManager;
import subethaclipse.net.command.CommandFactory;
import subethaclipse.net.command.DisconnectCommand;
import subethaclipse.net.command.ICommand;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Vaughan
 */
public class DisconnectCommandTest extends TestCase {

    private DisconnectCommand command;

    protected void setUp() throws Exception {
        super.setUp();
        command = new DisconnectCommand();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSystemPriority() {
        Assert.assertEquals(DisconnectCommand.SEQUENCE_SYSTEM, command.getSequenceNumber());
        Assert.assertTrue(command.isSystemCommand());
        Assert.assertTrue(command.isSequenceNumberInitialised());
    }

    public void testDeserialisation() {
        try {
            byte[] bytes = command.getByteArray();
            ICommand deserialised = CommandFactory.getInstance().create(bytes);
            Assert.assertTrue(deserialised != null);
            Assert.assertTrue(deserialised instanceof DisconnectCommand);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    public void testExecution() {
        try {
            MockControl connectControl = MockControl.createControl(IConnectionManager.class);
            IConnectionManager manager = (IConnectionManager) connectControl.getMock();
            manager.close();
            manager.close();
            connectControl.replay();
            ConnectionManager.setInstance(manager);
            command.execute();
            ConnectionManager.setInstance(null);
            connectControl.verify();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }
}
