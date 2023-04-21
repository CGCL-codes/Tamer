package ubc.lersse.sqlia.utils;

import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * OVERVIEW: XmlUtils is a collection of procedures used to work with org.w3c.dom Documents
 * 			 in an effective way.
 */
public final class XmlUtils {

    private static DocumentBuilder documentBuilder;

    private static boolean hasDocumentBuilder;

    private static Transformer transformer;

    private static boolean hasTransformer;

    /**
	 * EFFECTS: Initializes {@code documentBuilder} and {@code transformer}. Sets {@code hasDocumentBuilder}
	 * 			and {@code hasTransformer} flags appropriately, based on the success of constructing
	 * 			DocumentBuilder and Transformer objects.
	 */
    static {
        try {
            TransformerFactory f = TransformerFactory.newInstance();
            transformer = f.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            hasTransformer = true;
        } catch (TransformerConfigurationException e) {
            hasTransformer = false;
        }
        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            documentBuilder = f.newDocumentBuilder();
            hasDocumentBuilder = true;
        } catch (ParserConfigurationException e) {
            hasDocumentBuilder = false;
        }
    }

    /**
	 * EFFECTS: Returns node and it's children in string representation, without xml document type
	 * 			declaration (i.e in format of "<node><child>child value</child></node>"). Throws
	 *			TransformerException if node is null, or cannot be transformed into a String for
	 *			any reason.
	 */
    public static String xmlToString(Node node) throws TransformerException {
        if (node == null) {
            throw new TransformerException(XmlUtils.class.getName() + ".xmlToString(Node): node cannot be null.");
        }
        if (!hasTransformer) {
            throw new TransformerConfigurationException(XmlUtils.class.getName() + ".xmlToString(Node): unable to create Transformer.");
        }
        StringWriter sw = new StringWriter();
        synchronized (transformer) {
            transformer.transform(new DOMSource(node), new StreamResult(sw));
        }
        return sw.toString();
    }

    /**
	 * EFFECTS: Returns a new org.w3c.dom.Document. Throws ParserConfigurationException if Document
	 * 			cannot be created for any reason.
	 */
    public static Document createDocument() throws ParserConfigurationException {
        if (!hasDocumentBuilder) {
            throw new ParserConfigurationException(XmlUtils.class.getName() + ".createDocument(): unable to create DocumentBuilder.");
        }
        synchronized (documentBuilder) {
            Document d = documentBuilder.newDocument();
            return d;
        }
    }
}
