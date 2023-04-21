package au.edu.qut.yawl.editor.foundations;

import junit.framework.*;

public class TestXMLUtilities extends TestCase {

    public TestXMLUtilities(String pName) {
        super(pName);
    }

    public static Test suite() {
        return new TestSuite(TestXMLUtilities.class);
    }

    public static void main(String args[]) {
        junit.textui.TestRunner.run(suite());
    }

    protected void setUp() {
    }

    public void testQuoteXML() {
        String quotedXML;
        quotedXML = XMLUtilities.quoteXML("<crap>eek</crap>");
        assertEquals(quotedXML, "<![CDATA[<crap>eek</crap>]]>");
        quotedXML = XMLUtilities.quoteXML(quotedXML);
        assertEquals(quotedXML, "<![CDATA[<crap>eek</crap>]]>");
    }

    public void testUnqoteXML() {
        String plainXML;
        plainXML = XMLUtilities.unquoteXML("<![CDATA[<crap>eek</crap>]]>");
        assertEquals(plainXML, "<crap>eek</crap>");
        plainXML = XMLUtilities.unquoteXML(plainXML);
        assertEquals(plainXML, "<crap>eek</crap>");
    }

    public void testToValidElementName() {
        String convertedString;
        convertedString = XMLUtilities.toValidXMLName("New Net #1");
        assertEquals(convertedString, "New_Net_1");
        convertedString = XMLUtilities.toValidXMLName("   New Net #2   ");
        assertEquals(convertedString, "___New_Net_2___");
        convertedString = XMLUtilities.toValidXMLName("!@#$^&*$%^&*()_123!@#$!#@!!%&*");
        assertEquals(convertedString, "_123");
        convertedString = XMLUtilities.toValidXMLName("!@#$^&*$%^&*().123!@#$!#@!!%&*");
        assertEquals(convertedString, ".123");
        convertedString = XMLUtilities.toValidXMLName("!@#$^&*$%^&*()-123!@#$!#@!!%&*");
        assertEquals(convertedString, "-123");
        convertedString = XMLUtilities.toValidXMLName("!@#$");
        assertEquals(convertedString, "");
    }
}
