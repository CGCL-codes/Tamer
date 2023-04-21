package net.sourceforge.mile4j.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * @author Luc Pezet <lpezet@gmail.com>
 *
 */
public class XMLTest {

    @Test
    public void bogus() {
        assertTrue(true);
    }

    public void parseTilesDefsWithDOM() throws Exception {
        DocumentBuilderFactory oFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder oBuilder = oFactory.newDocumentBuilder();
        Document oDoc = oBuilder.parse(this.getClass().getResourceAsStream("/tiles-def.xml"));
        assertNotNull(oDoc);
        XPathFactory oXPathFactory = XPathFactory.newInstance();
        XPath oXPath = oXPathFactory.newXPath();
        Node oTilesDefinitionsNode = (Node) oXPath.evaluate("/tiles-definitions", oDoc, XPathConstants.NODE);
        Node oNewChild = null;
        oTilesDefinitionsNode.appendChild(oNewChild);
    }

    public void parseTilesDefsWithSAX() throws Exception {
        XMLReader oReader = XMLReaderFactory.createXMLReader();
        WeirdHandler oHandler = new WeirdHandler();
        oReader.setContentHandler(oHandler);
        oReader.parse(new InputSource(this.getClass().getResourceAsStream("/tiles-def.xml")));
    }

    class WeirdHandler extends DefaultHandler {

        private Locator mLocator;

        @Override
        public void setDocumentLocator(Locator pLocator) {
            mLocator = pLocator;
        }

        @Override
        public void startElement(String pUri, String pLocalName, String pName, Attributes pAttributes) throws SAXException {
            System.out.println(String.format("Element %s at %s,%s", pName, mLocator.getLineNumber(), mLocator.getColumnNumber()));
        }
    }
}
