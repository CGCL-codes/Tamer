package tomPack.xml;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import com.sun.org.apache.xerces.internal.dom.DOMImplementationImpl;

/**
 * Utilities for XML files.
 * <p>
 * Use {@link #parseXML(File)} to read a XML file.
 */
@SuppressWarnings("nls")
public class TomXMLUtils {

    /**
	 * Return the list of child node's names.
	 */
    public static List<String> getValueList(Element rootElement) {
        List<String> valueList = new ArrayList<String>();
        NodeList nodes = rootElement.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String nodeName = node.getNodeName().trim();
            if (nodeName.startsWith("#")) {
                continue;
            }
            valueList.add(nodeName);
        }
        return valueList;
    }

    public static Document parseXML(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        if (xmlFile.length() == 0) {
            return null;
        }
        System.out.println("parsing...");
        return docBuilder.parse(xmlFile);
    }

    /** Writes a DOM document to a stream. */
    public static void outputDOM(Document doc, OutputStream stream) throws TransformerFactoryConfigurationError, TransformerException {
        outputNode(doc, stream, false);
    }

    /**
	 * Writes a Node to a stream.
	 * 
	 * @param omitXmlDeclaration
	 */
    public static void outputNode(Node node, OutputStream stream, boolean omitXmlDeclaration) throws TransformerFactoryConfigurationError, TransformerException {
        String omit = omitXmlDeclaration ? "yes" : "no";
        Source source = new DOMSource(node);
        Result result = new StreamResult(stream);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omit);
        xformer.transform(source, result);
    }

    /**
	 * Convenience method for
	 * {@link DOMImplementation#createDocument(String, String, org.w3c.dom.DocumentType)}
	 * using args (null, rootElementName, null).
	 */
    public static Document createDocument(String rootElementName) {
        DOMImplementation domImpl = DOMImplementationImpl.getDOMImplementation();
        Document doc = domImpl.createDocument(null, rootElementName, null);
        return doc;
    }
}
