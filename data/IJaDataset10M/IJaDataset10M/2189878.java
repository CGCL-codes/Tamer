package org.apache.commons.lang3;

import net.community.chest.AbstractTestSupport;
import org.junit.Test;

/**
 * @author Lyor G.
 * @since Nov 1, 2011 8:18:45 AM
 */
public class ExtraStringUtilsTest extends AbstractTestSupport {

    public ExtraStringUtilsTest() {
        super();
    }

    @Test
    public void testStringTrimToSize() {
        final String TEST_STRING = "testStringTrimToSize";
        assertSame("Not same string instance", TEST_STRING, ExtraStringUtils.trimToSize(TEST_STRING, TEST_STRING.length() + 1));
        for (int length = 0; length < TEST_STRING.length(); length++) {
            final String expected = TEST_STRING.substring(0, length), actual = ExtraStringUtils.trimToSize(TEST_STRING, length);
            assertNotSame("Same instance for length=" + length, expected, actual);
            assertEquals("Mismatched data for length=" + length, expected, actual);
        }
    }

    @Test
    public void testCharSequenceTrimToSize() {
        final CharSequence TEST_SEQUENCE = new StringBuilder("testStringTrimToSize");
        assertSame("Not same string instance", TEST_SEQUENCE, ExtraStringUtils.trimToSize(TEST_SEQUENCE, TEST_SEQUENCE.length() + 1));
        for (int length = 0; length < TEST_SEQUENCE.length(); length++) {
            final CharSequence expected = TEST_SEQUENCE.subSequence(0, length), actual = ExtraStringUtils.trimToSize(TEST_SEQUENCE, length);
            assertNotSame("Same instance for length=" + length, expected, actual);
            assertEquals("Mismatched data for length=" + length, expected.toString(), actual.toString());
        }
    }
}
