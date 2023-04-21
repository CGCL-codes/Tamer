package com.acgvision.core.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rémi Debay <remi.debay@acgcenter.com>
 */
public class MonitorTest {

    public MonitorTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getMeasures method, of class Monitor.
     */
    @Test
    public void testGetMeasures() {
    }

    /**
     * Test of setMeasures method, of class Monitor.
     */
    @Test
    public void testSetMeasures() {
    }

    @Test
    public void testCleaner() {
        Monitor j = new Monitor();
        Monitor result = null;
        result = (Monitor) j.clean();
        j.setName("test");
        result = (Monitor) j.clean();
        assertTrue(j.getName().equals(result.getName()));
    }
}
