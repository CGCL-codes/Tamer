package org.mobicents.media.server.scheduler;

import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author kulikov
 */
public class SATest {

    private SA sa;

    public SATest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        sa = new SA();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of process method, of class SA.
     */
    @Test
    public void testProcess() {
        long[] x = new long[10];
        double[] y = new double[10];
        for (int i = 0; i < 10; i++) {
            x[i] = i;
            y[i] = x[i] * Math.sin(i);
        }
        long result = sa.process(x, y);
    }
}
