package android.core;

import junit.framework.TestCase;
import android.test.suitebuilder.annotation.SmallTest;

/**
 * Tests for functionality of class Integer to parse integers.
 */
public class ParseIntTest extends TestCase {

    @SmallTest
    public void testParseInt() throws Exception {
        assertEquals(0, Integer.parseInt("0", 10));
        assertEquals(473, Integer.parseInt("473", 10));
        assertEquals(0, Integer.parseInt("-0", 10));
        assertEquals(-255, Integer.parseInt("-FF", 16));
        assertEquals(102, Integer.parseInt("1100110", 2));
        assertEquals(2147483647, Integer.parseInt("2147483647", 10));
        assertEquals(-2147483648, Integer.parseInt("-2147483648", 10));
        try {
            Integer.parseInt("2147483648", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("-2147483649", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("21474836470", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("-21474836480", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("21474836471", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("-21474836481", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("214748364710", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("-214748364811", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("99", 8);
            fail();
        } catch (NumberFormatException e) {
        }
        try {
            Integer.parseInt("Kona", 10);
            fail();
        } catch (NumberFormatException e) {
        }
        assertEquals(411787, Integer.parseInt("Kona", 27));
    }
}
