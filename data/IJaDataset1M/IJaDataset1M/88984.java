package net.firstpartners.nounit.utility.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.firstpartners.nounit.utility.TextUtil;

/**
 * Tests the functions in the Util Class
 */
public class TestTextUtil extends TestCase {

    /**
     * Constructor Required by Junit
     * @param name
     */
    public TestTextUtil(String name) {
        super(name);
    }

    /**
     * Method to setup logging test
     */
    public void setUp() {
    }

    /**
     * Enable Junit to run this Class individually
     * @param args
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Enable Junit to run this class
     * @return TestDataCaptureDefaults.class
     */
    public static Test suite() {
        return new TestSuite(TestTextUtil.class);
    }

    /**
     * Tests replace method
     */
    public void testReplace() {
        assertEquals("mainstring", TextUtil.replace("mainstring", "", ""));
        assertEquals("", TextUtil.replace("", "sub", "string"));
        assertEquals("string", TextUtil.replace("mainstring", "main", ""));
        assertEquals("mainstrin", TextUtil.replace("mainstring", "g", ""));
        assertEquals("mystring", TextUtil.replace("mainstring", "ain", "y"));
        assertEquals("substring", TextUtil.replace("mainstring", "main", "sub"));
    }

    /**
     * Tests replaceAll method
     */
    public void testReplaceAll() {
        assertEquals(null, TextUtil.replaceAll(null, null, null));
        assertEquals("", TextUtil.replaceAll("", "", ""));
        assertEquals("Abc", TextUtil.replaceAll("abc", "a", "A"));
        assertEquals("aXYZc", TextUtil.replaceAll("abc", "b", "XYZ"));
        assertEquals("abC", TextUtil.replaceAll("abc", "c", "C"));
        assertEquals("abcdef", TextUtil.replaceAll("abcdef", "", "XXX"));
        assertEquals("ABCdef", TextUtil.replaceAll("abcdef", "abc", "ABC"));
        assertEquals("abcDEF", TextUtil.replaceAll("abcdef", "def", "DEF"));
        assertEquals("abCDEf", TextUtil.replaceAll("abcdef", "cde", "CDE"));
        assertEquals("AbcdefAbcxyzdef", TextUtil.replaceAll("abcdefabcxyzdef", "a", "A"));
        assertEquals("abcdeFabcxyzdeF", TextUtil.replaceAll("abcdefabcxyzdef", "f", "F"));
        assertEquals("ABCdefABCxyzdef", TextUtil.replaceAll("abcdefabcxyzdef", "abc", "ABC"));
        assertEquals("abcDEFabcxyzDEF", TextUtil.replaceAll("abcdefabcxyzdef", "def", "DEF"));
        assertEquals("abcdefabcXYZdef", TextUtil.replaceAll("abcdefabcxyzdef", "xyz", "XYZ"));
        assertEquals("abcdefabcxyzdef", TextUtil.replaceAll("abcdefabcxyzdef", "F", "XXX"));
        assertEquals("ABCABCABCxyzdef", TextUtil.replaceAll("abcabcabcxyzdef", "abc", "ABC"));
        assertEquals("1231231231231231", TextUtil.replaceAll("11111111111", "11", "123"));
        assertEquals("AAAAAA", TextUtil.replaceAll("aaaaaa", "a", "A"));
        assertEquals("A", TextUtil.replaceAll("a", "a", "A"));
    }

    /**
     * Test the remove funtionality
     */
    public void testRemove() {
        assertTrue(TextUtil.removeAll(" Some  String", " ").equals("SomeString"));
    }

    /**
     * Test the remove trailing functionality
     */
    public void testRemoveTrailing() {
        assertTrue(TextUtil.removeTrailing("  xxxx  ", " ").equals("  xxxx"));
    }
}
