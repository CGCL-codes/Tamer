package net.sf.mercator.test.util;

import com.globalretailtech.util.Format;
import junit.framework.TestCase;

/**
 * @author Igor Semenko (igorsemenko@yahoo.com)
 *
 */
public class FormatTest extends TestCase {

    /**
	 * Constructor for FormatTest.
	 * @param arg0
	 */
    public FormatTest(String arg0) {
        super(arg0);
    }

    public void testPrintStringStringStringint() {
        String result = Format.print("12345", "678", " ", 10);
        assertEquals("12345  678", result);
        result = Format.print("012345555555", "678", " ", 10);
        assertEquals("012345 678", result);
    }

    public void testSubstitution() {
        String result = Format.substitute("1{0}3", new Integer(2));
        assertEquals("123", result);
        result = Format.substitute("{0}2{1}", 0, new Integer(1));
        assertEquals("12{1}", result);
        result = Format.substitute("{0}2{1}", 1, new Integer(3));
        assertEquals("{0}23", result);
    }
}
