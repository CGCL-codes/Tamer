package name.vaccari.matteo.tai.phonedirectory.xpath;

import static org.junit.Assert.assertEquals;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlDocument {

    private Document document;

    private final InputStream documentInputStream;

    public XmlDocument(String documentContents) {
        documentInputStream = new ByteArrayInputStream(documentContents.getBytes());
    }

    public XmlDocument(File file) throws FileNotFoundException {
        documentInputStream = new FileInputStream(file);
    }

    public void load() {
        if (null != document) return;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            document = factory.newDocumentBuilder().parse(documentInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getRootElementName() {
        return "bookstore";
    }

    public String getNodeText(String xpath) {
        NodeList nodes;
        try {
            nodes = getNodeList(xpath);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
        return nodes.item(0).getTextContent();
    }

    public NodeList getNodeList(String xpath) throws XPathExpressionException {
        load();
        XPathFactory factory = XPathFactory.newInstance();
        XPath xp = factory.newXPath();
        return (NodeList) xp.evaluate(xpath, document, XPathConstants.NODESET);
    }
}
