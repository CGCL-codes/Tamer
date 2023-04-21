package gnu.beanfactory.util;

import junit.framework.*;
import gnu.beanfactory.util.*;
import gnu.beanfactory.*;

/**
 *  JUnit Test Case
 **/
public class RegexTestCase extends TestCase {

    public RegexTestCase(String s) {
        super(s);
    }

    public void testMatch() {
        assertTrue(Regex.match("m/o/", "Hello World"));
        try {
            Regex.match("m/hello/", null);
        } catch (Exception e) {
            fail("Regex.match() should allow null input");
        }
    }

    public void testSplit() {
        String[] s = Regex.split("/,/", "1,2,3,4,5");
        assertTrue(s.length == 5);
        try {
            Regex.split("/,/", null);
        } catch (Exception e) {
            fail("Regex.split() should accept null input");
        }
    }

    public void testEscapeHtml() {
        String out = Regex.escapeHtml("<hello>");
        assertEquals("&lt;hello&gt;", Regex.escapeHtml("<hello>"));
        assertEquals("&quot;&lt;hello&gt;&quot;", Regex.escapeHtmlAttribute("\"<hello>\""));
    }
}
