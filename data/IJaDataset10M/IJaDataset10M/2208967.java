package com.googlecode.layout4j.parser;

import java.io.IOException;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.xml.sax.SAXException;
import com.googlecode.layout4j.parser.HtmlToXmlConverter;
import com.googlecode.layout4j.parser.LayoutException;

/**
 * As a client, i want to be able to convert HTML to LayoutML. So that i can 
 * persist clean layouts.
 * 
 * @author Matt Humphreys
 */
public class HtmlToXmlConverterTest {

    private static final boolean ADD_XML_FURNITURE = true;

    /**
	 * Given some simple HTML, when i convert it i want to get LayoutML.
	 */
    @Test
    public void testSimple() throws LayoutException, SAXException, IOException {
        String source = ResourceUtil.read("htmlToXml.simple.source.html");
        String actual = HtmlToXmlConverter.toXml(source, ADD_XML_FURNITURE);
        System.out.println("RESULT: " + actual);
        String expected = ResourceUtil.read("htmlToXml.simple.expected.html");
        System.out.println("EXPECTED: " + expected);
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expected, actual);
        XMLAssert.assertXMLIdentical(diff, true);
    }

    /**
	 * Given some simple HTML, when i convert it i want to get LayoutML.
	 */
    @Test
    public void testRowsCols() throws LayoutException, SAXException, IOException {
        String source = ResourceUtil.read("htmlToXml.rowsCols.source.html");
        String actual = HtmlToXmlConverter.toXml(source, ADD_XML_FURNITURE);
        System.out.println("RESULT: " + actual);
        String expected = ResourceUtil.read("htmlToXml.rowsCols.expected.html");
        System.out.println("EXPECTED: " + expected);
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = new Diff(expected, actual);
        XMLAssert.assertXMLIdentical(diff, true);
    }
}
