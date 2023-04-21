package be.cm.apps.playground.util;

import org.junit.Test;

public class NanoMeterTest {

    @Test
    public void testLogStderr() {
        NanoMeter logger = new NanoMeter(System.err);
        logger.log("start nanometer");
        logger.log("freeMemory: " + Runtime.getRuntime().freeMemory());
        logger.log("totalMemory: " + Runtime.getRuntime().totalMemory());
        logger.log("maxMemory: " + Runtime.getRuntime().maxMemory());
        logger.log("availableProcessors: " + Runtime.getRuntime().availableProcessors());
        logger.log("end nanometer");
    }

    @Test
    public void testLogSleep() throws Exception {
        NanoMeter logger = new NanoMeter();
        logger.log("start nanometer");
        logger.log("after no sleep");
        Thread.sleep(0);
        logger.log("after 0ms sleep");
        Thread.sleep(0, 100000);
        logger.log("after 0.1ms sleep");
        Thread.sleep(1);
        logger.log("after 1ms sleep");
        Thread.sleep(5);
        logger.log("after 5ms sleep");
        Thread.sleep(10);
        logger.log("after 10ms sleep");
        Thread.sleep(100);
        logger.log("after 100ms sleep");
    }
}
