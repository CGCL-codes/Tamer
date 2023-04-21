package is.iclt.icenlp.core.utils;

import java.io.IOException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class XmlOperations {

    public static org.w3c.dom.Document createDocument() {
        return new org.apache.xerces.dom.DocumentImpl();
    }

    public static String docToString(Document doc) {
        String serialized = null;
        try {
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
            LSSerializer writer = impl.createLSSerializer();
            serialized = writer.writeToString(doc);
        } catch (IllegalAccessException ex) {
            System.out.println("IllegalAccessException while serializing XML!");
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            System.out.println("InstantiationException while serializing XML!");
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("ClassNotFoundException while serializing XML!");
            ex.printStackTrace();
        }
        return serialized;
    }

    /**
     * Loads an <code>org.w3c.dom.Document</code> from an XML String.
     * @param xml The XML to be parsed into a Document. The String must
     * be a well formed XML document.
     * @return The <code>Document</code> that results from the parse.
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static org.w3c.dom.Document loadXMLFrom(String xml) {
        return loadXMLFrom(new java.io.ByteArrayInputStream(xml.getBytes()));
    }

    /**
     * Loads an <code>org.w3c.dom.Document</code> from an <code>InputStream</code>.
     * The InputStream must return a well formed XML Document as a String. 
     * @param is
     * @return
     * @throws org.xml.sax.SAXException
     * @throws java.io.IOException
     */
    public static org.w3c.dom.Document loadXMLFrom(java.io.InputStream is) {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (ParserConfigurationException ex) {
            System.out.println("ParserConfigurationException while loading XML from InputStream");
            ex.printStackTrace();
        } catch (SAXException ex) {
            System.out.println("SAXException while loading XML from InputStream");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.out.println("IOException while loading XML from InputStream");
            ex.printStackTrace();
        }
        return doc;
    }
}
