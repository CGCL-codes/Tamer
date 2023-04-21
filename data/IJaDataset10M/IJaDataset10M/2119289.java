package ch.unibe.eindermu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author Marcus Liwicki
 */
public class XmlHandler {

    private Document xmlDocument = null;

    private ArrayList<InputStream> schemata = new ArrayList<InputStream>();

    private DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

    public void addSchema(InputStream schema) {
        this.schemata.add(schema);
    }

    public void loadFromFile(File file) throws IOException {
        loadFromStream(new FileInputStream(file));
    }

    public void loadFromStream(InputStream stream) throws IOException {
        try {
            if (!schemata.isEmpty()) {
                factory.setNamespaceAware(true);
                factory.setValidating(true);
                try {
                    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
                    factory.setAttribute("http://java.sun.com/xml/jaxp/properties/schemaSource", schemata.toArray());
                } catch (IllegalArgumentException x) {
                    System.err.println("The XML parser implementation of this java vm does not support XML schema validation. The reason is: " + x.getMessage() + "\nXML will be parsed without validation");
                    factory.setValidating(false);
                    factory.setNamespaceAware(false);
                }
            }
            final StringList errors = new StringList();
            DocumentBuilder parser = factory.newDocumentBuilder();
            parser.setErrorHandler(new ErrorHandler() {

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    errors.add(exception.getMessage());
                }

                @Override
                public void error(SAXParseException exception) throws SAXException {
                    errors.add(exception.getMessage());
                }
            });
            xmlDocument = parser.parse(stream);
            if (!errors.isEmpty()) {
                throw new IOException(errors.join("\n"));
            }
        } catch (SAXException e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } finally {
            stream.close();
            for (InputStream s : this.schemata) {
                s.close();
            }
        }
    }

    public void createNewXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder parser = factory.newDocumentBuilder();
        this.xmlDocument = parser.newDocument();
        this.xmlDocument.setXmlStandalone(true);
    }

    public void saveToFile(File file) throws TransformerException {
        transformToOutput(new StreamResult(file));
    }

    public void saveToStream(OutputStream stream) throws TransformerException {
        transformToOutput(new StreamResult(stream));
    }

    private void transformToOutput(StreamResult output) throws TransformerException {
        TransformerFactory xformFactory = TransformerFactory.newInstance();
        xformFactory.setAttribute("indent-number", 4);
        Transformer idTransform = xformFactory.newTransformer();
        idTransform.setOutputProperty(OutputKeys.INDENT, "yes");
        Source input = new DOMSource(xmlDocument);
        idTransform.transform(input, output);
    }

    public Document getDocument() {
        return this.xmlDocument;
    }
}
