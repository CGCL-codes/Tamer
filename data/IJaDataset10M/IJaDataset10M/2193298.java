package de.mogwai.common.utils;

import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * XML - Utilities.
 * 
 * @author $Author: mirkosertic $
 * @version $Date: 2008-06-17 14:26:35 $
 */
public final class XMLUtils {

    private static XMLUtils me;

    private DocumentBuilderFactory documentFactory;

    private DocumentBuilder documentBuilder;

    private TransformerFactory transformerFactory;

    private XMLUtils() throws ParserConfigurationException {
        documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
        transformerFactory = TransformerFactory.newInstance();
    }

    public static XMLUtils getInstance() throws ParserConfigurationException {
        if (me == null) {
            me = new XMLUtils();
        }
        return me;
    }

    public Document newDocument() {
        return documentBuilder.newDocument();
    }

    public Document newDocumentFromString(String aString) throws XMLException {
        return newDocumentFromReader(new StringReader(aString));
    }

    public Document newDocumentFromStream(InputStream aStream) throws XMLException {
        try {
            return documentBuilder.parse(new InputSource(aStream));
        } catch (Exception e) {
            throw new XMLException(e);
        }
    }

    public Document newDocumentFromReader(Reader aReader) throws XMLException {
        try {
            return documentBuilder.parse(new InputSource(aReader));
        } catch (Exception e) {
            throw new XMLException(e);
        }
    }

    public Element addElementWithText(Document aDocument, Node aNode, String aTagName, String aText) {
        Element theElement = aDocument.createElement(aTagName);
        theElement.appendChild(aDocument.createTextNode(aText));
        aNode.appendChild(theElement);
        return theElement;
    }

    public Element addElement(Document aDocument, Node aNode, String aTagName) {
        Element theElement = aDocument.createElement(aTagName);
        aNode.appendChild(theElement);
        return theElement;
    }

    public String transformToString(Document aDocument) throws XMLException {
        try {
            StringWriter theWriter = new StringWriter();
            Transformer theTransformer = transformerFactory.newTransformer();
            theTransformer.transform(new DOMSource(aDocument), new StreamResult(theWriter));
            return theWriter.toString();
        } catch (Exception e) {
            throw new XMLException(e);
        }
    }
}
