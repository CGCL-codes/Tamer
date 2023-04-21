package org.rollinitiative.d20;

import static org.junit.Assert.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author bebopjmm
 * @since sprint-0.1
 */
public class AdjustableValueTest {

    static final Log LOG = LogFactory.getLog(AdjustableValueTest.class);

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        DOMConfigurator.configure("log4j.xml");
        LOG.info("\n\n+++++\n+ AdjustableValueTest \n+++++");
    }

    /**
     * @since sprint-0.1
     */
    @Test
    public void testInit() {
        LOG.info("\n+++ testInit +++");
        AdjustableValue adjVal = new AdjustableValue(5);
        assertTrue(adjVal.subscribers.size() == 0);
        assertTrue(adjVal.getBase() == 5);
        assertTrue(adjVal.getCurrent() == 5);
    }

    /**
     * @since sprint-0.1
     */
    @Test
    public void testSetBase() {
        LOG.info("\n+++ testSetBase +++");
        AdjustableValue adjVal = new AdjustableValue(5);
        adjVal.setBase(10);
        assertTrue(adjVal.getBase() == 10);
    }

    /**
     * Test method for Adding, changing, and removing adjustments.
     * 
     * @since sprint-0.1
     */
    @Test
    public void testAdjustments() {
        LOG.info("\n+++ testAdjustments +++");
        AdjustableValue adjVal = new AdjustableValue(5);
        TestListener listener = new TestListener();
        adjVal.subscribe(listener);
        assertTrue(adjVal.subscribers.size() == 1);
        Adjustment adjust = new Adjustment(AdjustmentCategory.INHERENT, (short) 5, "test.adj1");
        listener.expectedVal = 10;
        adjVal.addAdjustment(adjust);
        assertTrue(adjVal.getBase() == 5);
        assertTrue(adjVal.getCurrent() == 10);
        assertTrue(listener.wasNotified);
        listener.wasNotified = false;
        listener.expectedVal = 15;
        adjust.setValue((short) 10);
        assertTrue(adjVal.getCurrent() == 15);
        assertTrue(adjVal.getBase() == 5);
        assertTrue(listener.wasNotified);
        listener.wasNotified = false;
        listener.expectedVal = 5;
        adjVal.removeAdjustment(adjust);
        assertTrue(adjust.subscribers.size() == 0);
        assertTrue(adjVal.getBase() == 5);
        assertTrue(adjVal.getCurrent() == 5);
        assertTrue(listener.wasNotified);
    }

    /**
     * @author bebopjmm
     * @since sprint-0.1
     */
    class TestListener implements AdjustableValueListener {

        public int expectedVal;

        public boolean wasNotified = false;

        public void valueChanged(AdjustableValue adjustable) {
            wasNotified = true;
            LOG.debug("Notification of valueChanged");
            assertTrue(adjustable.getCurrent() == expectedVal);
        }
    }
}
