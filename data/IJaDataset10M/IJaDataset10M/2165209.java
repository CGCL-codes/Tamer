package org.retro.xml;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Misc routines for parsing DOM documents, including writing an XML document to string/file.
 *
 * <ul>
 *  <li>writeXMLString
 *  <li>writeXMLFile
 * </ul>
 * @author Ricky Clarkson(original)
 * @author Berlin Brown(mods)
 *
 */
public class NewXMLUtility {

    NewXMLUtility() {
    }

    /**
	 * Convert an XML document to a formatted String.
	 */
    public static String writeXMLString(Document doc) throws Exception {
        OutputFormat _format = new OutputFormat(doc);
        _format.setLineWidth(80);
        _format.setIndenting(true);
        _format.setIndent(2);
        StringWriter _sWriter = new StringWriter();
        try {
            XMLSerializer serializer = new XMLSerializer(_sWriter, _format);
            serializer.serialize(doc);
            return _sWriter.toString();
        } catch (Exception _y) {
            throw new Exception(_y.getMessage());
        }
    }

    public static void writeXMLFile(Document _doc, String _fname) throws IOException {
        File f = new File(_fname);
        OutputFormat _format = new OutputFormat(_doc);
        _format.setLineWidth(80);
        _format.setIndenting(true);
        _format.setIndent(2);
        BufferedWriter _br = new BufferedWriter(new FileWriter(f));
        XMLSerializer ser = new XMLSerializer(_br, _format);
        ser.serialize(_doc);
        _br.flush();
        _br.close();
    }

    public static Document xmlToDom(String uri) {
        DocumentBuilderFactory documentBuilderFactory;
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setValidating(true);
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            class XMLErrorHandler implements ErrorHandler {

                public void warning(SAXParseException exception) throws SAXException {
                    throw new RuntimeException("Fatal-Error:" + exception.getMessage());
                }

                public void error(SAXParseException exception) throws SAXException {
                    warning(exception);
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                    warning(exception);
                }
            }
            XMLErrorHandler errorHandler = new XMLErrorHandler();
            documentBuilder.setErrorHandler(errorHandler);
        } catch (ParserConfigurationException exception) {
            throw new RuntimeException("Fatal-Error:" + exception.getMessage());
        }
        Document document;
        try {
            document = documentBuilder.parse(uri);
        } catch (Exception exception) {
            throw new RuntimeException("Fatal-Error:" + exception.getMessage());
        }
        return document;
    }
}
