package logger.sd.server;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * The class <code>LogServerTest</code> contains tests for the class
 * <code>{@link ServerManager}</code>.
 * 
 * @generatedBy CodePro at 12/10/10 10:47
 * @author Wendell
 * @version $Revision: 1.0 $
 */
public class LogServerManagerTest {

    /**
	 * Run the LogServerManager() constructor test.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testLogServerManager() throws Exception {
        ServerManager result = new ServerManager();
        assertNotNull(result);
    }

    /**
	 * Run the void startServer() method test.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testStartServer() throws Exception {
        ServerManager fixture = new ServerManager();
        fixture.startServer();
    }

    /**
	 * Run the LogServer getServer() method test.
	 * 
	 * @throws Exception
	 */
    @Test
    public void testGetServer() throws Exception {
        ServerManager fixture = new ServerManager();
        fixture.createLogServer();
        assertNotNull(fixture.getServer());
    }

    /**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 * @generatedBy CodePro at 12/10/10 10:47
	 */
    public static void main(String[] args) {
        new org.junit.runner.JUnitCore().run(LogServerManagerTest.class);
    }
}
