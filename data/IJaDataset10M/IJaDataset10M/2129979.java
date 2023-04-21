package sia.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Utility class for easy XML manipulation.
 * @author Misza &lt;misza@misza.net&gt;
 */
public class XMLUtils {

    /**
	 * Create a new {@link Document} using a {@link DocumentBuilderFactory} with default settings.
	 * @return a new {@link Document} instance
	 * @throws ParserConfigurationException if raised by {@link DocumentBuilderFactory#newDocumentBuilder()}
	 */
    public static Document newDocument() throws ParserConfigurationException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
    }

    /**
	 * Create a {@link Document} using a {@link DocumentBuilderFactory} with default settings
	 * by parsing an XML document from an {@link InputStream}.
	 * @param is an {@link InputStream} to read from
	 * @return a new {@link Document} instance
	 * @throws ParserConfigurationException if raised by {@link DocumentBuilderFactory#newDocumentBuilder()}
	 * @throws SAXException if raised by {@link DocumentBuilder#parse(InputStream)}
	 * @throws IOException  if raised by {@link DocumentBuilder#parse(InputStream)}
	 */
    public static Document newDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
        return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    }

    /**
	 * Creates a new XML {@link Element} with the given content and attributes.
	 * @param doc a {@link Document} instance that will create and hold the element
	 * @param name tag-name of the element
	 * @param cdata text content of the element
	 * @param attributes list of attribute/value pairs of the element
	 * @return the created {@link Element}
	 */
    public static Element mkElement(Document doc, String name, String cdata, Object... attributes) {
        Element elem = doc.createElement(name);
        if (cdata != null) elem.setTextContent(cdata);
        assert (attributes.length % 2 == 0);
        for (int i = 0; i < attributes.length / 2; i++) {
            if (attributes[2 * i + 1] != null) {
                elem.setAttribute((String) attributes[2 * i], attributes[2 * i + 1].toString());
            } else {
                elem.setAttribute((String) attributes[2 * i], "");
            }
        }
        return elem;
    }

    /**
	 * Serializes an XML {@link Document} to text.
	 * @param doc a {@link Document} instance to serialize
	 * @return textual representation of the document
	 * @throws TransformerFactoryConfigurationError TODO?
	 * @throws TransformerException TODO?
	 */
    public static String documentToString(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        StreamResult result = new StreamResult(new StringWriter());
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        return result.getWriter().toString();
    }
}
