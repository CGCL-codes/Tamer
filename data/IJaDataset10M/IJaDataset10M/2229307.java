package net.sf.edevtools.tools.logviewer.core.tests.socket;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * TODO comment
 * 
 * @author Christoph Graupner
 * @since 0.1.0
 * @version 0.1.0
 */
public class TestSocketServer {

    /**
	 * Logger for this class
	 */
    private static final Logger logger = Logger.getLogger(TestSocketServer.class.getName());

    private TestServerThread fThreadServ;

    private final int fPort;

    /**
	 * TODO comment
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        Handler fh;
        fh = new ConsoleHandler();
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
        TestSocketServer lTest = null;
        try {
            lTest = new TestSocketServer(8888);
            out(-1, "Running loop ");
            for (int i = 0; i < 5; i++) {
                out(i, "Loop Start");
                out(i, "Starting");
                lTest.startService();
                try {
                    out(i, "Sleep begin");
                    Thread.sleep(7000);
                    out(i, "Sleep end");
                } catch (InterruptedException ex) {
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
                try {
                    out(i, "Stopping");
                    lTest.stopService();
                    out(i, "Stopped");
                } catch (IOException ex) {
                    logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
                }
                out(i, "Ended loop ");
            }
        } catch (IOException ex1) {
            logger.log(Level.SEVERE, ex1.getLocalizedMessage(), ex1);
        }
        if (lTest != null) lTest.dispose();
        out(-1, "main end");
    }

    private static void out(int aI, String aString) {
        System.out.println("[main] L" + aI + ": " + aString);
    }

    public TestSocketServer(int aPort) throws IOException {
        fPort = aPort;
    }

    public void dispose() {
        if (fThreadServ != null) {
            try {
                stopService();
            } catch (IOException ex) {
                logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
            }
        }
    }

    public void startService() {
        out2("Starting Service");
        fThreadServ = new TestServerThread(fPort);
        fThreadServ.start();
    }

    public void stopService() throws IOException {
        out2("Stop called");
        fThreadServ.stopService();
        try {
            fThreadServ.join();
            out2("server thread stopped");
        } catch (InterruptedException ex) {
            logger.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
        }
        fThreadServ = null;
        out2("Stop call ended");
    }

    private void out2(String aString) {
        System.out.println("[Server] " + aString);
    }
}
