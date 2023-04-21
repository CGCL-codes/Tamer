package PatchConversion;

import junit.framework.*;

public class XMLReaderTest extends TestCase {

    public XMLReaderTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(XMLReaderTest.class);
    }

    public void testConvertInputEscapedChars() {
        String s;
        s = XMLReader.convertInputEscapedChars("");
        assertTrue(s.equals(""));
        s = XMLReader.convertInputEscapedChars("hi there");
        assertTrue(s.equals("hi there"));
        s = XMLReader.convertInputEscapedChars("a&amp;b");
        assertTrue(s.equals("a&b"));
        s = XMLReader.convertInputEscapedChars("&amp; &amp;");
        assertTrue(s.equals("& &"));
        s = XMLReader.convertInputEscapedChars("&gt;&lt;");
        assertTrue(s.equals("><"));
    }

    public void testConvertOutputEscapedChars() {
        String s;
        s = XMLReader.convertOutputEscapedChars("");
        assertTrue(s.equals(""));
        s = XMLReader.convertOutputEscapedChars("hi there");
        assertTrue(s.equals("hi there"));
        s = XMLReader.convertOutputEscapedChars("a&b");
        assertTrue(s.equals("a&amp;b"));
        s = XMLReader.convertOutputEscapedChars("& &");
        assertTrue(s.equals("&amp; &amp;"));
        s = XMLReader.convertOutputEscapedChars("><");
        assertTrue(s.equals("&gt;&lt;"));
    }
}
