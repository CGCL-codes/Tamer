package krico.javali.utiltest;

import krico.javalitest.JavaliCase;
import krico.javali.util.*;
import junit.framework.*;
import java.io.File;

public class URLUtilsTest extends JavaliCase {

    public URLUtilsTest(String t) {
        super(t);
    }

    public void testDecode() {
        String sent = null;
        String exp = null;
        sent = "%20";
        exp = " ";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "%20%%20";
        exp = " % ";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "%20%%%20";
        exp = " %% ";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "%20%%%%20";
        exp = " %%% ";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "%20%2%%20";
        exp = " %2% ";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "a%20";
        exp = "a ";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "a";
        exp = "a";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
        sent = "a%20a";
        exp = "a a";
        assertEquals("Wrong decode", exp, URLUtils.decode(sent));
    }

    public void testEncode() {
        String sent = null;
        String exp = null;
        sent = "%20";
        exp = " ";
        assertEquals("Wrong encode", sent, URLUtils.encode(exp));
        sent = "%20%20";
        exp = "  ";
        assertEquals("Wrong encode", sent, URLUtils.encode(exp));
        sent = "a%20";
        exp = "a ";
        assertEquals("Wrong encode", sent, URLUtils.encode(exp));
        sent = "a";
        exp = "a";
        assertEquals("Wrong encode", sent, URLUtils.encode(exp));
        sent = "a%20a";
        exp = "a a";
        assertEquals("Wrong encode", sent, URLUtils.encode(exp));
    }

    public void testUntag() {
        doUntag("&gt;", ">");
        doUntag("&lt;", "<");
        doUntag(" &lt;", " <");
        doUntag(" &gt;", " >");
        doUntag("&lt; ", "< ");
        doUntag("&gt; ", "> ");
        doUntag("&gt;&lt;", "><");
        doUntag("&lt;&gt;", "<>");
        doUntag("&gt;&gt;", ">>");
        doUntag("&lt;&lt;", "<<");
        doUntag("&lt;bla&lt;", "<bla<");
        doUntag("&gt;bla&lt;", ">bla<");
        doUntag("&gt;bla&gt;", ">bla>");
    }

    public void doUntag(String exp, String sent) {
        assertEquals("Wrong untag", exp, URLUtils.untag(sent));
    }

    public static Test suite() {
        return new TestSuite(URLUtilsTest.class);
    }
}
