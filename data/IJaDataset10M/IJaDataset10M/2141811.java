package de.robowars.comm.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.log4j.Category;
import org.apache.log4j.PropertyConfigurator;
import de.robowars.comm.CommException;
import de.robowars.comm.transport.Application;
import de.robowars.comm.transport.RobotEnumeration;
import junit.framework.TestCase;

/**
 * RoboWars project
 * @author kevin
 *
 */
public class ClientSocketTest extends TestCase {

    /** The logger instance for this class. */
    private static Category log = Category.getInstance(ClientSocketTest.class);

    private Thread thread = null;

    private static final int TEST_PORT = 12345;

    /**
	 * Constructor for ClientSocketTest.
	 * @param arg0
	 */
    public ClientSocketTest(String arg0) {
        super(arg0);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(ClientSocketTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        PropertyConfigurator.configure("4_Implementation/conf/log4j.conf");
        thread = new Thread(new Server());
        thread.start();
    }

    protected void tearDown() throws Exception {
        thread.interrupt();
        super.tearDown();
    }

    public void testStrawberry() throws Exception {
        ClientSocket cs;
        cs = new ClientSocket(null, "localhost", TEST_PORT, null);
        log.debug("new ClientSocket created");
        Thread.sleep(1000);
        log.debug("close() on ClientSocket");
        cs.close();
        log.debug("close() on ClientSocket called.");
        Thread.sleep(5000);
    }

    private Application getTestContent() {
        Application result = new Application();
        result.setPlayerName("hans");
        result.setRobotType(RobotEnumeration.SEVENTH);
        return result;
    }

    private class Server implements Runnable {

        ServerSocket ss;

        Socket s;

        public Server() throws IOException {
            ss = new ServerSocket(TEST_PORT);
        }

        public void run() {
            try {
                s = ss.accept();
                log.debug("Connection established.");
                ss.close();
                Thread.sleep(2000);
                log.debug("Close connection.");
                s.shutdownOutput();
                s.shutdownInput();
                s.close();
                s = null;
                Thread.sleep(500);
            } catch (Exception x) {
                log.fatal(x);
            }
            log.debug("Terminate server.");
        }
    }
}
