package org.zkoss.idom.input;

import org.zkoss.idom.*;

/**
 * The default iDOM factory.
 *
 * @author tomyeh
 */
public class DefaultIDOMFactory implements IDOMFactory {

    /** Constructor.
	 */
    public DefaultIDOMFactory() {
    }

    public Attribute newAttribute(String lname, String value) {
        return new Attribute(lname, value);
    }

    public Attribute newAttribute(Namespace ns, String lname, String value) {
        return new Attribute(ns, lname, value);
    }

    public CData newCData(String text) {
        return new CData(text);
    }

    public Comment newComment(String text) {
        return new Comment(text);
    }

    public DocType newDocType(String elementName, String publicId, String systemId) {
        return new DocType(elementName, publicId, systemId);
    }

    public Document newDocument(Element rootElement, DocType docType) {
        return new Document(rootElement, docType);
    }

    public Element newElement(Namespace ns, String lname) {
        return new Element(ns, lname);
    }

    public Element newElement(String lname) {
        return new Element(lname);
    }

    public ProcessingInstruction newProcessingInstruction(String target, String data) {
        return new ProcessingInstruction(target, data);
    }

    public EntityReference newEntityRef(String name) {
        return new EntityReference(name);
    }

    public Text newText(String text) {
        return new Text(text);
    }
}
