package net.sourceforge.zyps;

import junit.framework.*;

public class ClockTest extends TestCase {

    private static int SLEEPTIME = 100;

    private static int MARGIN = 10;

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        return new TestSuite(ClockTest.class);
    }

    public void testGetInstance() {
        Clock clock1 = Clock.getInstance();
        Clock clock2 = Clock.getInstance();
        assertTrue(clock1 == clock2);
    }

    public void testGetTime() {
        long elapsedTimeDifference;
        elapsedTimeDifference = elapsedTimeDifference((float) 0.5);
        assertTrue("Elapsed time difference: " + elapsedTimeDifference, elapsedTimeDifference >= -((SLEEPTIME + MARGIN) * 0.5) && elapsedTimeDifference <= -((SLEEPTIME - MARGIN) * 0.5));
        elapsedTimeDifference = elapsedTimeDifference(2);
        assertTrue("Elapsed time difference: " + elapsedTimeDifference, elapsedTimeDifference >= SLEEPTIME - MARGIN && elapsedTimeDifference <= SLEEPTIME + MARGIN);
        elapsedTimeDifference = elapsedTimeDifference(0);
        assertTrue("Elapsed time difference: " + elapsedTimeDifference, elapsedTimeDifference >= -SLEEPTIME - MARGIN && elapsedTimeDifference <= -SLEEPTIME + MARGIN);
    }

    private long elapsedTimeDifference(float speed) {
        Clock clock = Clock.getInstance();
        long priorSystemTime = System.currentTimeMillis();
        clock.setTime(priorSystemTime);
        clock.setSpeed(speed);
        try {
            Thread.sleep(SLEEPTIME);
        } catch (InterruptedException exception) {
        }
        long elapsedSystemTime = System.currentTimeMillis() - priorSystemTime;
        long elapsedClockTime = clock.getTime() - priorSystemTime;
        return elapsedClockTime - elapsedSystemTime;
    }
}
