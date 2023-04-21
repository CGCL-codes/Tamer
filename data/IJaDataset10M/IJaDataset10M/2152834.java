package org.xngr.utils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utilities for reading and writing of XML documents.
 * 
 * @version $Revision: 1.12 $, $Date: 2007/06/28 13:14:23 $
 * @author Edwin Dankert <edankert@gmail.com>
 */
public class DocumentUtilities {

    private static final boolean DEBUG = false;

    private static final EntityResolver DUMMY_RESOLVER = new DummyEntityResolver();

    /**
	 * Returns the default entity resolver, does not resolve external entities!
	 * 
	 * @return the default Entity resolver.
	 */
    public static EntityResolver getEntityResolver() {
        return DUMMY_RESOLVER;
    }

    /**
	 * Reads the document for this URL.
	 * 
	 * @param url
	 *            the URL of the document.
	 * 
	 * @return the Dom4J document.
	 */
    public static synchronized Document readDocument(URL url, boolean validate) throws IOException, SAXParseException {
        if (DEBUG) System.out.println("DocumentUtilities.readDocument( " + url + ")");
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setCoalescing(true);
            document = factory.newDocumentBuilder().parse(url.toExternalForm());
        } catch (SAXException e) {
            if (e instanceof SAXParseException) {
                throw (SAXParseException) e;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (DEBUG) System.out.println("DocumentUtilities.readDocument( " + url + ") [" + document + "]");
        return document;
    }

    public static synchronized Document readRemoteDocument(URL url, boolean validate) throws IOException, SAXParseException {
        if (DEBUG) System.out.println("DocumentUtilities.readDocument( " + url + ")");
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setCoalescing(true);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDefaultUseCaches(false);
            connection.setUseCaches(false);
            connection.setRequestProperty("User-Agent", "eXchaNGeR/" + System.getProperty("xngr.version") + " (http://xngr.org/)");
            connection.connect();
            InputStream stream = connection.getInputStream();
            document = factory.newDocumentBuilder().parse(stream);
            stream.close();
            connection.disconnect();
        } catch (SAXException e) {
            if (e instanceof SAXParseException) {
                throw (SAXParseException) e;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if (DEBUG) System.out.println("DocumentUtilities.readDocument( " + url + ") [" + document + "]");
        return document;
    }

    public static synchronized Document readDocument(InputSource source, boolean validate) throws IOException, SAXParseException {
        if (DEBUG) System.out.println("DocumentUtilities.readDocument( " + source + ")");
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            factory.setCoalescing(true);
            document = factory.newDocumentBuilder().parse(source);
        } catch (SAXException e) {
            if (e instanceof SAXParseException) {
                throw (SAXParseException) e;
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        return document;
    }

    /**
	 * Writes the document to the location specified by the URL.
	 * 
	 * @param document
	 *            the dom4j document.
	 * @param url
	 *            the URL of the document.
	 */
    public static synchronized void writeDocument(Document document, URL url) {
        if (DEBUG) System.out.println("DocumentUtilities.writeDocument( " + document + ", " + url + ")");
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new FileOutputStream(url.getFile()));
            transformer.setOutputProperty("media-type", "text/xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("version", "1.0");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void writeDocument(OutputStream stream, Document document) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(stream);
            transformer.setOutputProperty("media-type", "text/xml");
            transformer.setOutputProperty("indent", "yes");
            transformer.setOutputProperty("method", "xml");
            transformer.setOutputProperty("version", "1.0");
            transformer.transform(source, result);
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void handleParseException(SAXParseException e, JFrame frame, URL url) {
        String file = url.getFile();
        int index = file.lastIndexOf('/') + 1;
        handleParseException(e, frame, "Error Parsing Document : " + file.substring(index) + "\n");
    }

    public static void handleParseException(SAXParseException e, JFrame frame, String message) {
        int line = e.getLineNumber();
        Exception ex = e.getException();
        message = (message != null) ? message : "";
        if (ex == null) {
            JOptionPane.showMessageDialog(frame, message + "Error on line " + line + ":\n" + e.getMessage(), "Parser Error", JOptionPane.ERROR_MESSAGE);
        } else {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, message + e.getMessage() + "\n" + ex.getMessage(), "Parser Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static class DummyEntityResolver implements EntityResolver {

        public InputSource resolveEntity(String publicId, String systemId) {
            return new InputSource(new ByteArrayInputStream(CommonEntities.asDTD().getBytes()));
        }
    }

    public static List<Element> getElements(Document document, String name) {
        return getElements(document.getDocumentElement(), name);
    }

    public static List<Element> getElements(Element element, String name) {
        List<Element> result = new ArrayList<Element>();
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                if (name.equals(getName((Element) node))) {
                    result.add((Element) node);
                }
            }
        }
        return result;
    }

    public static Element getElement(Document document, String name) {
        return getElement(document.getDocumentElement(), name);
    }

    public static Element getElement(Element element, String name) {
        NodeList list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                if (name.equals(getName((Element) node))) {
                    return (Element) node;
                }
            }
        }
        return null;
    }

    public static Element appendElement(Element element, String name) {
        Element e = element.getOwnerDocument().createElement(name);
        element.appendChild(e);
        return e;
    }

    public static String getElementTextContent(Element element, String name) {
        Element result = getElement(element, name);
        if (result != null) {
            return result.getTextContent();
        }
        return null;
    }

    public static void setElementContent(Element element, String name, String value) {
        Element result = getElement(element, name);
        if (result != null) {
            result.setTextContent(value);
        }
    }

    public static void setElementContent(Element element, String name, boolean value) {
        Element result = getElement(element, name);
        if (result != null) {
            result.setTextContent("" + value);
        }
    }

    public static void appendElementWithTextContent(Element element, String name, String value) {
        Element result = appendElement(element, name);
        if (result != null) {
            result.setTextContent(value);
        }
    }

    public static Boolean getElementBooleanContent(Element element, String name) {
        Element result = getElement(element, name);
        if (result != null) {
            return "true".equals(result.getTextContent());
        }
        return false;
    }

    public static String getName(Element element) {
        if (element.getLocalName() == null) {
            return element.getTagName();
        }
        return element.getLocalName();
    }

    public static String getName(Attr attribute) {
        if (attribute.getLocalName() == null) {
            return attribute.getName();
        }
        return attribute.getLocalName();
    }
}
