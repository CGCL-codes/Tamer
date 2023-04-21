package org.owasp.jxt;

import java.io.StringWriter;
import java.io.Writer;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * EncoderWriterTest
 *
 * @author Jeffrey Ichnowski
 * @version $Revision: 8 $
 */
public class EncoderWriterTest {

    @Test
    public void testWriteArray() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        ew.write("<>&".toCharArray());
        assertEquals("&lt;&gt;&amp;", out.toString());
    }

    @Test
    public void testWriteChar() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        ew.write('<');
        assertEquals("&lt;", out.toString());
    }

    @Test
    public void testWriteArrayPart() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        char[] array = "abc<>&xyz".toCharArray();
        ew.write(array, 3, 3);
        ew.write(array, 4, 1);
        assertEquals("&lt;&gt;&amp;&gt;", out.toString());
    }

    @Test
    public void testWriteString() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        ew.write("<>&");
        assertEquals("&lt;&gt;&amp;", out.toString());
    }

    @Test
    public void testWriteStringPart() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        String str = "abc<>&xyz";
        ew.write(str, 3, 3);
        ew.write(str, 4, 1);
        assertEquals("&lt;&gt;&amp;&gt;", out.toString());
    }

    @Test
    public void testAppendChar() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        ew.append('<');
        assertEquals("&lt;", out.toString());
    }

    @Test
    public void testAppendString() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        ew.append("<>&");
        assertEquals("&lt;&gt;&amp;", out.toString());
    }

    @Test
    public void testAppendStringPart() throws Exception {
        StringWriter out = new StringWriter();
        Writer ew = Encoder.XML.wrap(out);
        CharSequence seq = "abc<>&xyz";
        ew.append(seq, 3, 6);
        ew.append(seq, 4, 5);
        assertEquals("&lt;&gt;&amp;&gt;", out.toString());
    }
}
