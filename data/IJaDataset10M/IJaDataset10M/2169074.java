package org.creativor.rayson.transport.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The class <code>PacketExceptionTest</code> contains tests for the class
 * <code>{@link PacketException}</code>.
 * <p>
 * Copyright Creativor Studio (c) 2011
 * 
 * @generatedBy CodePro at 11-5-7 上午3:08
 * @author Nick Zhang
 * @version $Revision: 1.0 $
 */
public class PacketExceptionTest {

    /**
	 * Launch the test.
	 * 
	 * @param args
	 *            the command line arguments
	 * 
	 * @generatedBy CodePro at 11-5-7 上午3:08
	 */
    public static void main(String[] args) {
        new org.junit.runner.JUnitCore().run(PacketExceptionTest.class);
    }

    /**
	 * Perform pre-test initialization.
	 * 
	 * @throws Exception
	 *             if the initialization fails for some reason
	 * 
	 * @generatedBy CodePro at 11-5-7 上午3:08
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * Perform post-test clean-up.
	 * 
	 * @throws Exception
	 *             if the clean-up fails for some reason
	 * 
	 * @generatedBy CodePro at 11-5-7 上午3:08
	 */
    @After
    public void tearDown() throws Exception {
    }

    /**
	 * Run the PacketException(String) constructor test.
	 * 
	 * @throws Exception
	 * 
	 * @generatedBy CodePro at 11-5-7 上午3:08
	 */
    @Test
    public void testPacketException_1() throws Exception {
        String message = "";
        PacketException result = new PacketException(message);
        assertNotNull(result);
        assertEquals(null, result.getCause());
        assertEquals("org.creativor.rayson.transport.common.PacketException: ", result.toString());
        assertEquals("", result.getMessage());
        assertEquals("", result.getLocalizedMessage());
    }
}
