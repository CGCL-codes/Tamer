package org.tei.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class Utils {

    public static final String TEI_COMPARATOR_NAMESPACE = "http://tei-c.org/TextComparator";

    public static final String TEI_NAMESPACE = "http://www.tei-c.org/ns/1.0";

    /**
	 * Reads a file into an XML document object
	 * 
	 * @param file
	 * @return
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
    public static Document readInputFileAsXML(File file) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setNamespaceAware(true);
        DocumentBuilder docBuilder;
        Document doc = null;
        docBuilder = docBuilderFactory.newDocumentBuilder();
        doc = docBuilder.parse(file);
        return doc;
    }

    public static String getUniqueXMLID() {
        return "sid-" + java.util.UUID.randomUUID().toString();
    }

    /**
	 * 
	 * @param file
	 * @return
	 * @throws IOException
	 */
    public static String readInputFile(File file) throws IOException {
        String input = "";
        BufferedInputStream bis;
        bis = new BufferedInputStream(new FileInputStream(file));
        final byte[] bytes = new byte[(int) file.length()];
        bis.read(bytes);
        bis.close();
        input = new String(bytes);
        return input;
    }

    public static void writeDomDocumentToFile(Document document, File file) {
        OutputStreamWriter out = null;
        try {
            out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Transformer serializer;
        try {
            serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.STANDALONE, "yes");
            serializer.transform(new DOMSource(document), new StreamResult(out));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static String getXMLNodeAsString(Node node) {
        StringWriter writer = new StringWriter();
        Transformer serializer;
        try {
            serializer = TransformerFactory.newInstance().newTransformer();
            serializer.setOutputProperty(OutputKeys.METHOD, "xml");
            serializer.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            serializer.setOutputProperty(OutputKeys.INDENT, "yes");
            serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            serializer.transform(new DOMSource(node), new StreamResult(writer));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerFactoryConfigurationError e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return writer.toString();
    }

    public static Document getNewEmptyDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setNamespaceAware(true);
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NamespaceContext getTEINamespaceContext() {
        NamespaceContext ctx = new NamespaceContext() {

            public String getNamespaceURI(String prefix) {
                String uri;
                if (prefix.equals("tei")) uri = Utils.TEI_NAMESPACE; else if (prefix.equals("tc")) uri = Utils.TEI_COMPARATOR_NAMESPACE; else uri = null;
                return uri;
            }

            public Iterator<String> getPrefixes(String val) {
                return null;
            }

            public String getPrefix(String uri) {
                return null;
            }
        };
        return ctx;
    }
}
