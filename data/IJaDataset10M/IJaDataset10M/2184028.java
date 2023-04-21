package au.edu.archer.metadata.mde.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import au.edu.archer.metadata.spi.BadRequestException;
import au.edu.archer.metadata.spi.MetadataStoreException;

/**
 * This helper class provides some common methods for manipulating XML documents
 * 
 * @author xchernich
 * @author crawley
 */
public class XMLHelper {

    private static final Logger logger = Logger.getLogger(XMLHelper.class);

    private XMLHelper() {
    }

    /**
     * Transforms a DOM Document object to XML and writing the resulting text to
     * the supplied Writer.
     *
     * @param writer Destination for the resulting XML text
     * @param doc DOM document
     * @throws MetadataStoreException Unable to transform the DOM to XML for some reason
     */
    public static void outputXmlDocument(Writer writer, Document doc) throws MetadataStoreException {
        Source source = new DOMSource(doc);
        Result result = new StreamResult(writer);
        Transformer xformer = null;
        try {
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
        } catch (Exception ex) {
            String msg = ex.getMessage();
            logger.error("Unable to transform DOM document to XML String: " + msg);
            throw new BadRequestException(msg, ex);
        }
    }

    /**
     * Transforms a DOM Document object to XML and return it as a String.
     *
     * @param doc DOM document
     * @return the XML
     * @throws BadRequestException 
     * @throws MetadataStoreException Unable to transform the DOM to XML for some reason
     */
    public static String encodeXMLDocument(Document doc) throws BadRequestException {
        Source source = new DOMSource(doc);
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        Transformer xformer = null;
        try {
            xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
            return sw.toString();
        } catch (Exception ex) {
            String msg = ex.getMessage();
            logger.error("Unable to transform DOM document to XML String: " + msg);
            throw new BadRequestException(msg, ex);
        }
    }

    /**
     * Create an DOM Document from a data read from a {@link Reader}.  The DOM will 
     * be namespace aware.
     *
     * @param reader Source of XML text
     * @param expectedSize A hint as to how big the XML is expected to be.  A value less
     *        than or equal to zero means that the caller does not know.
     * @return doc DOM document, or {@code null}  on failure
     */
    public static Document readXMLDocument(Reader reader, int expectedSize) {
        StringBuffer sb = new StringBuffer(expectedSize <= 0 ? 1024 : expectedSize);
        try {
            int ch;
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
        } catch (IOException ex) {
            logger.error("Error reading XML data from request", ex);
        }
        return (sb.length() > 0) ? decodeXMLDocument(sb.toString()) : null;
    }

    /**
     * Create an DOM Document from a String.  The DOM will be namespace aware.
     *
     * @param data the encoded XML
     * @return doc DOM document, or {@code null} on failure
     */
    public static Document decodeXMLDocument(String data) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(data)));
        } catch (ParserConfigurationException ex) {
            logger.error("Error preparing to extract document from request", ex);
        } catch (SAXException ex) {
            logger.error("Error creating XML document", ex);
        } catch (IOException ex) {
            logger.error("Error reading data", ex);
        }
        return null;
    }

    /**
     * Create an empty XML DOM.  The DOM will be namespace aware.
     * 
     * @return the DOM
     * @throws ParserConfigurationException
     */
    public static Document createDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.newDocument();
    }
}
