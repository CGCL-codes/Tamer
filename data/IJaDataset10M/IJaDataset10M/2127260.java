package subethaclipse.net.command.test;

import org.easymock.AbstractMatcher;
import org.easymock.MockControl;
import subethaclipse.common.RemoteFile;
import subethaclipse.net.ConnectionManager;
import subethaclipse.net.IDriverConnectionManager;
import subethaclipse.net.command.CommandException;
import subethaclipse.net.command.CommandFactory;
import subethaclipse.net.command.DriverViewRequestCommand;
import subethaclipse.net.command.DriverViewResponseCommand;
import subethaclipse.net.command.ICommand;
import subethaclipse.views.driver.DriverPublish;
import subethaclipse.views.driver.DriverSubscribe;
import subethaclipse.views.driver.IDriverPublish;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Tests the class subethaclipse.net.command.DriverViewRequestCommand
 * 
 * @author Ben
 */
public class DriverViewRequestCommandTest extends TestCase {

    private class CommandMatcher extends AbstractMatcher {

        protected boolean argumentMatches(Object arg0, Object arg1) {
            if (arg0 instanceof DriverViewResponseCommand && arg1 instanceof DriverViewResponseCommand) {
                DriverViewResponseCommand c0 = (DriverViewResponseCommand) arg0;
                DriverViewResponseCommand c1 = (DriverViewResponseCommand) arg1;
                return (c0.getName() == c1.getName()) && (c0.getFileContents() == c1.getFileContents());
            }
            return false;
        }
    }

    private DriverViewRequestCommand instance = null;

    protected void setUp() throws Exception {
        super.setUp();
        instance = new DriverViewRequestCommand();
        DriverSubscribe.setInstance(null);
        ConnectionManager.setInstance(null);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        instance = null;
    }

    public void testDeserialisation() {
        try {
            byte[] bytes = instance.getByteArray();
            ICommand deserialised = CommandFactory.getInstance().create(bytes);
            Assert.assertTrue(deserialised != null);
            Assert.assertTrue(deserialised instanceof DriverViewRequestCommand);
        } catch (Exception ex) {
            Assert.fail(ex.getMessage());
        }
    }

    public void testExecute() {
        MockControl publishControl = MockControl.createControl(IDriverPublish.class);
        IDriverPublish publish = (IDriverPublish) publishControl.getMock();
        publish.getDriverView();
        publishControl.setReturnValue(new RemoteFile("filename", "contents"));
        publishControl.replay();
        DriverViewResponseCommand command = new DriverViewResponseCommand();
        command.setName("filename");
        command.setFileContents("contents");
        MockControl managerControl = MockControl.createControl(IDriverConnectionManager.class);
        IDriverConnectionManager manager = (IDriverConnectionManager) managerControl.getMock();
        manager.sendCommand(command);
        managerControl.setMatcher(new CommandMatcher());
        manager.close();
        managerControl.replay();
        DriverPublish.setInstance(publish);
        ConnectionManager.setInstance(manager);
        try {
            instance.execute();
        } catch (CommandException e) {
            fail("Unexpected CommandException: " + e.getMessage());
        }
        DriverSubscribe.setInstance(null);
        ConnectionManager.setInstance(null);
        publishControl.verify();
        managerControl.verify();
    }
}
