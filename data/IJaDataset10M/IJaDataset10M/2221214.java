package org.openscience.jmol;

import junit.framework.*;
import java.util.*;
import java.io.*;

/**
 * Unit tests for the SharcReader class.
 */
public class TestSharcReader extends TestCase {

    /**
	 * Creates a Test for the given method.
	 */
    public TestSharcReader(String name) {
        super(name);
    }

    /**
	 * SharcReader fixture.
	 */
    SharcReader sr1;

    /**
	 * Set up for testing.
	 */
    public void setUp() {
        try {
            sr1 = new SharcReader(new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("Data/refs_c4h12si1_data.sharc"))));
        } catch (IOException ex) {
            fail(ex.toString());
        }
    }

    /**
	 * Test reading.
	 */
    public void testReading() {
        try {
            assert (sr1.hasNext());
            SharcShielding ss1 = sr1.next();
            assert (sr1.hasNext());
            assertEquals("GIAO-G94/RHF/6-31+GD//B3LYP/6-31GD", ss1.getMethod());
            assert (ss1.containsElement("C"));
            assertEquals(200.16, ss1.getShielding("C"), 0.001);
            assert (ss1.containsElement("H"));
            assertEquals(32.53, ss1.getShielding("H"), 0.001);
            assert (ss1.containsElement("Si"));
            assertEquals(446.32, ss1.getShielding("Si"), 0.001);
            ss1 = sr1.next();
            assert (sr1.hasNext());
            assertEquals("GIAO-G94/RHF/6-311+G2DP//B3LYP/6-31GD", ss1.getMethod());
            assert (ss1.containsElement("C"));
            assertEquals(192.6, ss1.getShielding("C"), 0.001);
            assert (ss1.containsElement("H"));
            assertEquals(32.07, ss1.getShielding("H"), 0.001);
            assert (ss1.containsElement("Si"));
            assertEquals(385.83, ss1.getShielding("Si"), 0.001);
        } catch (IOException ex) {
            fail(ex.toString());
        }
    }

    /**
	 * Returns a Test containing all the tests.
	 */
    public static Test suite() {
        TestSuite suite = new TestSuite(TestSharcReader.class);
        return suite;
    }
}
