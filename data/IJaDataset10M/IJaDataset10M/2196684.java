package org.hl7.xml.parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A DynamicContentHandler used for data types. It has many methods implemented to just do nothing.
 */
public abstract class DataTypeContentHandler extends DynamicContentHandlerBase implements DynamicContentHandler, Cloneable {

    /**
	 * Use this object to avoid creating a new instance of AttributesImpl on each startElement(). Make sure you call
	 * attributes_.clear() before you use it.
	 * 
	 * DON'T USE THIS, for one it is not at the right place (this is a parser class, not builder) and it is still too much
	 * duplication. Instead use the attributes object in the PresentationHelper class.
	 * 
	 * @deprecated
	 */
    protected final AttributesImpl attributes_ = new AttributesImpl();

    public abstract void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException;

    public abstract void endElement(String namespaceURI, String localName, String qName) throws SAXException;

    /**
	 * A standard override to make an object cloneable.
	 * 
	 * @return a clone object
	 */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
