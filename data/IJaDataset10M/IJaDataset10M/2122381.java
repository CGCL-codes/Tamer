package net.sourceforge.ecm.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;

/**
 * TODO abr forgot to document this class
 */
public class Singleton {

    /** A default document builder, namespace aware. */
    public static final DocumentBuilder DOCUMENT_BUILDER;

    static {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DOCUMENT_BUILDER = documentBuilderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new Error("Error initialising default document builder", e);
        }
    }

    /** A default document transformer. */
    public static final Transformer DOCUMENT_TRANSFORMER;

    static {
        try {
            DOCUMENT_TRANSFORMER = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException e) {
            throw new Error("Error initialising default document transformer", e);
        }
    }
}
