package org.nds.logging.test;

import org.nds.logging.Logger;
import org.nds.logging.LoggerFactory;
import android.test.AndroidTestCase;

public class AndroidLoggingTest extends AndroidTestCase {

    private static final Logger log2 = LoggerFactory.getLogger("TAGTEST");

    private static final Logger log = LoggerFactory.getLogger(AndroidLoggingTest.class);

    public AndroidLoggingTest() {
    }

    @Override
    protected void setUp() throws Exception {
    }

    public void testLogger() throws InterruptedException {
        log.trace("test log trace/verbose");
        log.debug("test log debug");
        log.info("test log info.");
        log.warn("test log warning");
        log.error("test log error");
    }

    public void testLogger2() throws InterruptedException {
        log2.trace("test2 log trace/verbose");
        log2.debug("test2 log debug");
        log2.info("test2 log info.");
        log2.warn("test2 log warning");
        log2.error("test2 log error");
    }
}
