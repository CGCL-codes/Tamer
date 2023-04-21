package uk.co.demon.ursus.dom.pmr;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/** at present hardcoded to using default */
public class PMRDocumentBuilderFactory extends DocumentBuilderFactory {

    public PMRDocumentBuilderFactory() {
    }

    public DocumentBuilder newDocumentBuilder() {
        return new PMRDocumentBuilder();
    }
}
