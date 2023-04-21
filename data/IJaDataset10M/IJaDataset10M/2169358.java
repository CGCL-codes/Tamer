package net.sourceforge.jaulp.id.generator;

import org.junit.Before;
import org.junit.Test;

/**
 * The Class SystemTimeIdGeneratorTest.
 */
public class SystemTimeIdGeneratorTest {

    /**
	 * Sets the up.
	 *
	 * @throws Exception the exception
	 */
    @Before
    public void setUp() throws Exception {
    }

    /**
	 * Test get next id.
	 */
    @Test
    public void testGetNextId() {
        for (int i = 0; i < 1000; i++) {
            System.err.println(SystemTimeIdGenerator.getInstance().getNextId());
        }
    }
}
