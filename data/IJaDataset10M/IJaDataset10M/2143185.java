package org.omnidoc.xml;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.omnidoc.Document;

public class XMLDOMDocument implements Document {

    org.w3c.dom.Document doc;

    String name;

    public XMLDOMDocument(org.w3c.dom.Document doc, String name) {
        this.doc = doc;
        this.name = name;
    }

    public XMLDOMDocument(org.w3c.dom.Node node, String name) {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        try {
            Transformer tx = tfactory.newTransformer();
            DOMSource source = new DOMSource(node);
            DOMResult result = new DOMResult();
            tx.transform(source, result);
            this.doc = (org.w3c.dom.Document) result.getNode();
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
        this.name = name;
    }

    public org.w3c.dom.Document asXMLDocument() {
        return doc;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Document duplicate() {
        TransformerFactory tfactory = TransformerFactory.newInstance();
        try {
            Transformer tx = tfactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            DOMResult result = new DOMResult();
            tx.transform(source, result);
            return new XMLDOMDocument((org.w3c.dom.Document) result.getNode(), this.name);
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }
}
