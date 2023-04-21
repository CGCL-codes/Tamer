package org.unitmetrics.util.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Collection of static methods to ease xml access.
 * @author Martin Kersten
 */
public final class XMLUtil {

    /**
	 * Creates a DOM document from within a xml-source using a 
	 * non-validation parser.
	 * @param file The xml-file to read from.
	 * @return The document generated from the xml-file
	 * @throws SAXException Deligated exception of the parser.
	 * @throws ParserConfigurationException Deligated exception of the parser.
	 * @throws IOException If an i/o-exception happend.
	 */
    public static Document getXMLDocument(Reader source) throws SAXException, ParserConfigurationException, IOException {
        return getXMLDocument(new InputSource(source));
    }

    public static Document getXMLDocument(InputStream source) throws SAXException, ParserConfigurationException, IOException {
        return getXMLDocument(new InputSource(source));
    }

    private static Document getXMLDocument(InputSource source) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(source);
        return document;
    }

    /**
	 * Creates a DOM document within a xml-file using a 
	 * non-validation parser.
	 * @param file The xml-file to read from.
	 * @return The document generated from the xml-file
	 * @throws SAXException Deligated exception of the parser.
	 * @throws ParserConfigurationException Deligated exception of the parser.
	 * @throws IOException If an i/o-exception happend.
	 */
    public static Document getXMLDocument(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        return document;
    }

    /**
     * Handles the document elements using the ElementNodeHandler.
     * This method may be used to interpret a DOM document.
     */
    public static void processDocument(Document document, IDocumentVisitor handler) {
        if (document == null || handler == null) throw new NullPointerException();
        evaluateChildNodes(document, handler);
    }

    /**
     * Evaluates the child-nodes using the ElementNodeHandler.
     * <p>
     * If object is neither a instance of a DOM document nor a instance
     * of a DOM node the method does nothing.
     * </p>
     * @param object The object may be an Document instance or an
     * 						ElementNode instance of an DOM document.
     * @param handler The ElementNodeHandler to call on the element nodes.
     */
    private static void evaluateChildNodes(Object object, IDocumentVisitor handler) {
        if (object == null || handler == null) throw new NullPointerException();
        if (object instanceof Document) {
            NodeList nodes = ((Document) object).getChildNodes();
            for (int index = 0; index < nodes.getLength(); index++) {
                Node child = nodes.item(index);
                evaluateChildNodes(child, handler);
            }
        } else if (object instanceof Node) {
            Node node = (Node) object;
            if (handler.beginNode(node)) {
                NodeList nodes = node.getChildNodes();
                for (int index = 0; index < nodes.getLength(); index++) {
                    Node child = nodes.item(index);
                    evaluateChildNodes(child, handler);
                }
            }
            handler.endNode(node);
        }
    }
}
