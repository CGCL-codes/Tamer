package org.toolme.rms;

import org.toolme.testUtils.TestUtils;
import jmunit.framework.cldc10.AssertionFailedException;
import jmunit.framework.cldc10.TestCase;

/**
 * Various test cases for the RecordStoreKey class.
 */
public class RecordStoreKeyTest extends TestCase {

    /**
	 * Random key string length.
	 */
    private static final int KEYLENGTH = 32;

    /**
	 * Number of tests.
	 */
    private static final int TESTCOUNT = 1;

    /**
	 * Initialise.
	 */
    public RecordStoreKeyTest() {
        super(TESTCOUNT, "RecordStoreKeyTest");
    }

    /**
	 * @param testNumber
	 *            current test number.
	 * @throws Throwable
	 *             test exception.
	 */
    public final void test(final int testNumber) throws Throwable {
        switch(testNumber) {
            case 0:
                testGetKeyBytes();
                break;
            default:
                break;
        }
    }

    /**
	 * Test of RecordStoreKey class. 
	 * 
	 * There seems to be a randomised error with this test. I think the
	 * "TestUtils.generateRamdomString" method sometimes generates an invalid
	 * string.
	 * 
	 * @throws AssertionFailedException test exception.
	 */
    public final void testGetKeyBytes() throws AssertionFailedException {
        String keyStr = TestUtils.generateRamdomString(KEYLENGTH);
        RecordStoreKey key = new RecordStoreKey(keyStr);
        byte[] keyBytes = key.getKeyBytes();
        String excepted = keyStr;
        String actual = new String(keyBytes);
        assertEquals("Key bytes should be the same as the init key bytes.", excepted, actual);
    }
}
