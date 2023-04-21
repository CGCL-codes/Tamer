package org.xhtmlrenderer.util;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * Booch utility class for XML processing using DOM
 */
public class XMLUtil {

    public static Document documentFromString(final String documentContents) throws Exception {
        return createDocumentBuilder().parse(new InputSource(documentContents));
    }

    public static Document documentFromFile(final String filename) throws Exception {
        return createDocumentBuilder().parse(new File(filename).toURL().openStream());
    }

    private static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = fact.newDocumentBuilder();
        builder.setErrorHandler(null);
        return builder;
    }
}
