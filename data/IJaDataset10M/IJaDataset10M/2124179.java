package test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import javax.xml.parsers.smax.SMAXParserFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.smax.ContentHandler;
import org.xml.smax.DefaultHandler;
import org.xml.smax.SMAXParser;

public class TestSMAXParser {

    private InputStream is = null;

    private StringBuilder output = null;

    private SMAXParser p = null;

    @Before
    public void setUp() throws Exception {
        String sample = "<Sample att1='1'>Root<SubNode att2='2'></SubNode><SubNode att3='3'>This is a test</SubNode><SubNode>This is a test</SubNode></Sample>";
        is = new ByteArrayInputStream(sample.getBytes("UTF-8"));
        SMAXParserFactory spf = SMAXParserFactory.newInstance();
        p = spf.newSMAXParser();
    }

    @Test
    public void testTerminate() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startDocument() {
                super.startDocument();
                return ContentHandler.NextParserAction.Terminate;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|", output.toString());
    }

    @Test
    public void testProcessChildren() throws Exception {
        ContentHandler handler = new TestHandler();
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|Attribute:att1=1|TextNode:Root|Element Started:SubNode|Attribute:att2=2|Element Ended:SubNode|Element Started:SubNode|Attribute:att3=3|TextNode:This is a test|Element Ended:SubNode|Element Started:SubNode|TextNode:This is a test|Element Ended:SubNode|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testProcessChildElements() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.ProcessChildElements;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|Element Started:SubNode|Element Ended:SubNode|Element Started:SubNode|Element Ended:SubNode|Element Started:SubNode|Element Ended:SubNode|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testProcessChildTextNodes() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.ProcessChildTextNodes;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|TextNode:Root|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testProcessAttributes() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.ProcessAttributes;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|Attribute:att1=1|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testSkipChildElements() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.SkipChildElements;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|Attribute:att1=1|TextNode:Root|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testSkipChildTextNodes() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.SkipChildTextNodes;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|Attribute:att1=1|Element Started:SubNode|Attribute:att2=2|Element Ended:SubNode|Element Started:SubNode|Attribute:att3=3|Element Ended:SubNode|Element Started:SubNode|Element Ended:SubNode|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testSkipAttributes() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.SkipAttributes;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|TextNode:Root|Element Started:SubNode|Element Ended:SubNode|Element Started:SubNode|TextNode:This is a test|Element Ended:SubNode|Element Started:SubNode|TextNode:This is a test|Element Ended:SubNode|Element Ended:Sample|Ended", output.toString());
    }

    @Test
    public void testSkipChildren() throws Exception {
        ContentHandler handler = new TestHandler() {

            @Override
            public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
                super.startElement(uri, localName, qName);
                return ContentHandler.NextParserAction.SkipChildren;
            }
        };
        p.setContentHandler(handler);
        p.parse(new InputSource(is));
        Assert.assertEquals("Started|Element Started:Sample|Element Ended:Sample|Ended", output.toString());
    }

    class TestHandler extends DefaultHandler {

        @Override
        public ContentHandler.NextParserAction startDocument() {
            output = new StringBuilder();
            output.append("Started|");
            return ContentHandler.NextParserAction.ProcessChildren;
        }

        @Override
        public void endDocument() {
            output.append("Ended");
        }

        @Override
        public ContentHandler.NextParserAction startElement(String uri, String localName, String qName) {
            output.append("Element Started:" + qName + "|");
            return ContentHandler.NextParserAction.ProcessChildren;
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            output.append("Element Ended:" + qName + "|");
        }

        @Override
        public void attribute(String uri, String localName, String qName, String value) {
            output.append("Attribute:" + qName + "=" + value + "|");
        }

        @Override
        public void textNode(String text) {
            output.append("TextNode:" + text + "|");
        }
    }
}
