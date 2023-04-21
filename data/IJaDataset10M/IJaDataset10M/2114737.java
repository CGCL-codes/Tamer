package ch.unifr.nio.framework.transform;

import ch.unifr.nio.framework.mockups.TestChannelHandler;
import java.lang.reflect.Field;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * @author Ronny Standtke <Ronny.Standtke@gmx.net>
 */
public class TrafficShaperCoordinatorTest extends TestCase {

    /**
     * Test of start method, of class TrafficShaperCoordinator.
     * @throws Exception if any exception occurs
     */
    public void testStart() throws Exception {
        System.out.println("start");
        Logger logger = Logger.getLogger("ch.unifr.nio.framework");
        logger.setLevel(Level.FINEST);
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.FINEST);
        logger.addHandler(consoleHandler);
        TrafficShaperCoordinator trafficShaperCoordinator = new TrafficShaperCoordinator(Executors.newSingleThreadScheduledExecutor(), 1, 1000, false);
        trafficShaperCoordinator.start();
        Field futureField = TrafficShaperCoordinator.class.getDeclaredField("future");
        futureField.setAccessible(true);
        Object futureObject = futureField.get(trafficShaperCoordinator);
        assertNull("TrafficShaperCoordinator must not be scheduled without " + "any traffic shapers added!", futureObject);
        TestChannelHandler testChannelHandler = new TestChannelHandler();
        BufferForwarder bufferForwarder = new BufferForwarder(ByteBufferForwardingMode.DIRECT);
        trafficShaperCoordinator.addTrafficShaper(testChannelHandler, bufferForwarder);
        Future future = (Future) futureField.get(trafficShaperCoordinator);
        assertFalse("adding a traffic shaper to an empty but running " + "TrafficShaperCoordinator must schedule it for execution!", future == null ? true : future.isDone());
        trafficShaperCoordinator.removeTrafficShaper(testChannelHandler);
        future = (Future) futureField.get(trafficShaperCoordinator);
        assertTrue("removing all traffic shapers from a started " + "TrafficShaperCoordinator must cancel its execution!", future == null ? false : future.isDone());
    }

    /**
     * Test of setDelay method, of class TrafficShaperCoordinator.
     */
    public void testSetDelay() {
        System.out.println("setDelay");
        int delay = 1000;
        TrafficShaperCoordinator instance = new TrafficShaperCoordinator(Executors.newSingleThreadScheduledExecutor(), 1, 1000, false);
        instance.setDelay(delay);
    }

    /**
     * Test of stop method, of class TrafficShaperCoordinator.
     */
    public void testStop() {
        System.out.println("stop");
        TrafficShaperCoordinator instance = new TrafficShaperCoordinator(Executors.newSingleThreadScheduledExecutor(), 1, 1000, false);
        instance.stop();
    }
}
