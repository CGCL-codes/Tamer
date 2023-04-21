package ch.unifr.nio.framework;

import ch.unifr.nio.framework.mockups.MyClientSocketChannelHandler;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public class ResolveFailedTest extends TestCase {

    /**
     * Test of resolveFailed method, of class
     * ch.unifr.nio.framework.ClientSocketChannelHandler.
     * @throws IOException when the Dispatcher can not open a Selector
     * @throws InterruptedException if we are interrupted while sleeping
     */
    public void testResolveFailed() throws IOException, InterruptedException {
        Logger logger = Logger.getLogger(Dispatcher.class.getName());
        logger.setLevel(Level.FINEST);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        logger.addHandler(consoleHandler);
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.start();
        MyClientSocketChannelHandler handler = new MyClientSocketChannelHandler();
        dispatcher.registerClientSocketChannelHandler("öäü.öäü", 0, handler, 3000);
        for (int i = 0; !handler.hasResolveFailed() && (i < 30); i++) {
            Thread.sleep(1000);
        }
        assertTrue("resolveFailed() was never called!", handler.hasResolveFailed());
        Thread.sleep(4000);
        assertFalse("connectFailed() was called too", handler.hasConnectFailed());
    }
}
