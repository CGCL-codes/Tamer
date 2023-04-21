package org.mobicents.media.server.spi.memory;

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
public class MemoryTest {

    public MemoryTest() {
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
     * Test of allocate method, of class Memory.
     */
    @Test
    public void testAllocate() throws InterruptedException {
    }

    public void testGC() throws InterruptedException {
        for (int i = 0; i < 5000; i++) {
            Frame frame = Memory.allocate(160);
            frame.setTimestamp(Memory.clock.getTime());
            frame.setDuration(20000000L);
            Thread.sleep(20);
        }
    }
}
