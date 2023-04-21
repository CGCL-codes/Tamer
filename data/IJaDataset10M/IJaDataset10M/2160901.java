package tests.com.scholardesk.utilities;

import java.io.IOException;
import com.scholardesk.utilities.StringUtil;
import junit.framework.TestCase;

/**
 * Tests the {@link com.scholardesk.utilities.StringUtil} class.
 * 
 * @author Christopher Dunavant
 * @version $Revision: 1 $
 */
public class StringUtilTest extends TestCase {

    public StringUtilTest(String name) {
        super(name);
    }

    /**
	 * Tests that an input string's first letter is capitalized
	 * @throws IOException
	 */
    public void testCapitalize() throws IOException {
        String in_string = StringUtil.capitalize("string");
        String out_string = "String";
        assertEquals(in_string, out_string);
    }

    /**
	 * Tests that an input string's first letter is lowercased
	 * @throws IOException
	 */
    public void testLowercase() throws IOException {
        String in_string = StringUtil.lowercase("String");
        String out_string = "string";
        assertEquals(in_string, out_string);
        in_string = StringUtil.lowercase("STring");
        out_string = "sTring";
        assertEquals(in_string, out_string);
    }

    /**
	 * Tests that a substring can be substituted in an original string
	 */
    public void testSubstitute() throws IOException {
        String orig_string = "/controller/submit";
        String replace_string = "/controller/";
        String with_string = "/";
        String in_string = StringUtil.substitute(orig_string, replace_string, with_string);
        String out_string = "/submit";
        assertEquals(in_string, out_string);
        replace_string = "/";
        with_string = ".";
        in_string = StringUtil.substitute(orig_string, replace_string, with_string);
        out_string = ".controller.submit";
        assertEquals(in_string, out_string);
        orig_string = "This is a\nbreak.";
        replace_string = "\n";
        with_string = "<br/>";
        in_string = StringUtil.substitute(orig_string, replace_string, with_string);
        out_string = "This is a<br/>break.";
        assertEquals(in_string, out_string);
    }

    public void testStrip() throws IOException {
        String orig_string = "/controller/submit";
        String strip_string = "/controller/";
        String in_string = StringUtil.strip(orig_string, strip_string);
        String out_string = "submit";
        assertEquals(in_string, out_string);
        strip_string = "submit";
        in_string = StringUtil.strip(orig_string, strip_string);
        out_string = "/controller/";
        assertEquals(in_string, out_string);
    }

    public void testGetToken() throws IOException {
        String orig_string = "com/scholardesk/Person";
        String token = "/";
        int pos = 2;
        String in_string = StringUtil.getToken(orig_string, token, pos);
        assertEquals(in_string, "Person");
        pos = 1;
        in_string = StringUtil.getToken(orig_string, token, pos);
        assertEquals(in_string, "scholardesk");
        pos = 0;
        in_string = StringUtil.getToken(orig_string, token, pos);
        assertEquals(in_string, "com");
        pos = 4;
        in_string = StringUtil.getToken(orig_string, token, pos);
        assertEquals(in_string, null);
        token = ".";
        in_string = StringUtil.getToken(orig_string, token, pos);
        assertEquals(in_string, null);
    }

    public void testGetLastToken() throws IOException {
        String orig_string = "com/scholardesk/Person";
        String token = "/";
        String in_string = StringUtil.getLastToken(orig_string, token);
        assertEquals(in_string, "Person");
        orig_string = "Person/";
        in_string = StringUtil.getLastToken(orig_string, token);
        assertEquals(in_string, null);
    }

    public void testAddUnderScores() throws IOException {
        String orig_string = "FooBar";
        String in_string = StringUtil.addUnderScores(orig_string);
        String out_string = "foo_bar";
        assertEquals(in_string, out_string);
        String orig_string2 = "Foo";
        String in_string2 = StringUtil.addUnderScores(orig_string2);
        String out_string2 = "foo";
        assertEquals(in_string2, out_string2);
    }

    public void testRemoveUnderScores() throws IOException {
        String orig_string = "foo_bar";
        String in_string = StringUtil.removeUnderScores(orig_string);
        String out_string = "FooBar";
        assertEquals(in_string, out_string);
    }

    public void testEscapeJavascript() throws IOException {
        String orig_string = "'";
        String in_string = StringUtil.escapeJavascript(orig_string);
        String out_string = "\\'";
        assertEquals(in_string, out_string);
        String orig_string2 = "\"";
        String in_string2 = StringUtil.escapeJavascript(orig_string2);
        String out_string2 = "\\\"";
        assertEquals(in_string2, out_string2);
        String orig_string3 = "\\";
        String in_string3 = StringUtil.escapeJavascript(orig_string3);
        String out_string3 = "\\\\";
        assertEquals(in_string3, out_string3);
        String orig_string4 = "\r";
        String in_string4 = StringUtil.escapeJavascript(orig_string4);
        String out_string4 = "\\r";
        assertEquals(in_string4, out_string4);
        String orig_string5 = "\n";
        String in_string5 = StringUtil.escapeJavascript(orig_string5);
        String out_string5 = "\\n";
        assertEquals(in_string5, out_string5);
        String orig_string6 = "What's \"is\" c:\\file.txt\r\n";
        String in_string6 = StringUtil.escapeJavascript(orig_string6);
        String out_string6 = "What\\'s \\\"is\\\" c:\\\\file.txt\\r\\n";
        assertEquals(in_string6, out_string6);
    }
}
