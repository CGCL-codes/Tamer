package org.outerj.pollo.xmleditor.schema;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.util.Collection;

/**
 * Schema implementations should implement this interface.
 * Because Element's are passed as arguments to the methods,
 * rather than String, the Schema implementation could potentially
 * be made context-sensitive (support name overloading).
 *
 * @author Bruno Dumon
 */
public interface ISchema {

    public Collection getAttributesFor(Element element);

    public boolean isChildAllowed(Element parent, Element child);

    public String[] getPossibleAttributeValues(Element element, String namespaceURI, String localName);

    public Collection getAllowedSubElements(Element element);

    public Collection getAllowedSubTexts(Element element);

    public Collection validate(Document document) throws ValidationNotSupportedException, Exception;
}
