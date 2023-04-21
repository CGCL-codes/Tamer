package org.custommonkey.xmlunit;

import java.io.StringReader;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import junit.framework.TestSuite;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * JUnit test for TolerantSaxDocumentBuilder
 */
public class test_TolerantSaxDocumentBuilder extends XMLTestCase {

    private TolerantSaxDocumentBuilder builder;

    private SAXParser parser;

    private static final String SIMPLEST_XML = "<root><node>text</node></root>";

    public void testSimpleDocument() throws Exception {
        String simpleXML = XML_DECLARATION + SIMPLEST_XML;
        Document simpleXMLDocument = XMLUnit.buildControlDocument(simpleXML);
        assertParsedDocumentEqual(simpleXMLDocument, simpleXML);
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") == -1);
    }

    private void assertParsedDocumentEqual(Document control, String test) throws Exception {
        InputSource parseSource = new InputSource(new StringReader(test));
        parser.setProperty("http://xml.org/sax/properties/lexical-handler", builder);
        parser.parse(parseSource, builder);
        assertXMLEqual(control, builder.getDocument());
    }

    public void testSimpleDocumentWithComments() throws Exception {
        String xmlWithComments = XML_DECLARATION + "<more>" + SIMPLEST_XML + "<!--this is a comment -->" + SIMPLEST_XML + "</more>";
        Document documentWithComments = XMLUnit.buildControlDocument(xmlWithComments);
        assertParsedDocumentEqual(documentWithComments, xmlWithComments);
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") == -1);
    }

    public void testSimpleDocumentWithProcessingInstruction() throws Exception {
        String xmlWithProcInstruction = XML_DECLARATION + "<more>" + SIMPLEST_XML + "<?processing instruction?>" + SIMPLEST_XML + "</more>";
        Document documentWithProcInstruction = XMLUnit.buildControlDocument(xmlWithProcInstruction);
        assertParsedDocumentEqual(documentWithProcInstruction, xmlWithProcInstruction);
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") == -1);
    }

    public void testStartElementWithNoEnd() throws Exception {
        builder.startDocument();
        builder.startElement(null, null, "root", null);
        Document oneElementDocument = XMLUnit.buildControlDocument("<root/>");
        assertXMLEqual(oneElementDocument, builder.getDocument());
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") == -1);
    }

    public void testEndElementWithNoStart() throws Exception {
        builder.startDocument();
        builder.startElement(null, null, "root", null);
        builder.endElement(null, null, "node");
        builder.endElement(null, null, "root");
        Document oneElementDocument = XMLUnit.buildControlDocument("<root/>");
        assertXMLEqual(oneElementDocument, builder.getDocument());
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") != -1);
    }

    public void testEndElementBeforeStart() throws Exception {
        builder.startDocument();
        builder.endElement(null, null, "root");
        builder.startElement(null, null, "root", null);
        Document oneElementDocument = XMLUnit.buildControlDocument("<root/>");
        assertXMLEqual(oneElementDocument, builder.getDocument());
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") != -1);
    }

    public void testTextBeforeStartElement() throws Exception {
        String someText = "how could this happen?!";
        builder.startDocument();
        builder.characters(someText.toCharArray(), 0, someText.length());
        builder.startElement(null, null, "root", null);
        Document oneElementDocument = XMLUnit.buildControlDocument("<root/>");
        assertXMLEqual(oneElementDocument, builder.getDocument());
        assertTrue(builder.getTrace(), builder.getTrace().indexOf("WARNING") != -1);
    }

    public void setUp() throws Exception {
        builder = new TolerantSaxDocumentBuilder(XMLUnit.getTestParser());
        parser = SAXParserFactory.newInstance().newSAXParser();
    }

    public test_TolerantSaxDocumentBuilder(String name) {
        super(name);
    }

    public static TestSuite suite() {
        return new TestSuite(test_TolerantSaxDocumentBuilder.class);
    }
}
