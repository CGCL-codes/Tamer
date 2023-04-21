package subethaclipse.net.command.test;

import org.easymock.MockControl;
import subethaclipse.net.command.CommandException;
import subethaclipse.net.command.CommandFactory;
import subethaclipse.net.command.ICommand;
import subethaclipse.net.command.ViewChangedCommand;
import subethaclipse.views.driver.DriverSubscribe;
import subethaclipse.views.driver.IDriverSubscribe;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * @author Vaughan
 */
public class ViewChangedCommandTest extends TestCase {

    private ViewChangedCommand command;

    protected void setUp() throws Exception {
        super.setUp();
        command = new ViewChangedCommand();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testName() {
        command.setName("Test");
        Assert.assertEquals("Test", command.getName());
    }

    public void testContents() {
        command.setFileContents("Contents");
        Assert.assertEquals("Contents", command.getFileContents());
    }

    public void testDeserialisation() {
        try {
            command.setName("Test");
            command.setRealName("RealName");
            command.setFileContents("Contents");
            byte[] bytes = command.getByteArray();
            ICommand deserialised = CommandFactory.getInstance().create(bytes);
            Assert.assertTrue(deserialised instanceof ViewChangedCommand);
            ViewChangedCommand c = (ViewChangedCommand) deserialised;
            Assert.assertEquals("Test", c.getName());
            Assert.assertEquals("RealName", c.getRealName());
            Assert.assertEquals("Contents", c.getFileContents());
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        }
    }

    public void testExecute() {
        MockControl subscribeControl = MockControl.createControl(IDriverSubscribe.class);
        IDriverSubscribe subscribe = (IDriverSubscribe) subscribeControl.getMock();
        subscribe.openNewEditor("Test", "Contents");
        subscribe.setRealFileName("RealName");
        subscribeControl.replay();
        try {
            DriverSubscribe.setInstance(subscribe);
            command.setName("Test");
            command.setRealName("RealName");
            command.setFileContents("Contents");
            command.execute();
        } catch (Exception e) {
            Assert.fail(e.getMessage());
        } finally {
            DriverSubscribe.setInstance(null);
            subscribeControl.verify();
        }
    }

    public void testExecuteNullContents() {
        MockControl subscribeControl = MockControl.createControl(IDriverSubscribe.class);
        IDriverSubscribe subscribe = (IDriverSubscribe) subscribeControl.getMock();
        subscribeControl.replay();
        try {
            DriverSubscribe.setInstance(subscribe);
            command.setName("Test");
            command.setFileContents(null);
            command.execute();
            Assert.fail("Expected CommandException");
        } catch (CommandException e) {
        } finally {
            DriverSubscribe.setInstance(null);
            subscribeControl.verify();
        }
    }
}
