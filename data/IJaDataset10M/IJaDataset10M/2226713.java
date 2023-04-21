package org.apache.batik.dom.svg;

import org.apache.batik.dom.AbstractDocument;
import org.apache.batik.dom.util.XMLSupport;

/**
 * This class provides a common superclass for elements which contain
 * descriptive text.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: SVGDescriptiveElement.java,v 1.1 2005/11/21 09:51:24 dev Exp $
 */
public abstract class SVGDescriptiveElement extends SVGStylableElement {

    /**
     * Creates a new SVGDescriptiveElement object.
     */
    protected SVGDescriptiveElement() {
    }

    /**
     * Creates a new SVGDescriptiveElement object.
     * @param prefix The namespace prefix.
     * @param owner The owner document.
     */
    protected SVGDescriptiveElement(String prefix, AbstractDocument owner) {
        super(prefix, owner);
    }

    /**
     * <b>DOM</b>: Returns the xml:lang attribute value.
     */
    public String getXMLlang() {
        return XMLSupport.getXMLLang(this);
    }

    /**
     * <b>DOM</b>: Sets the xml:lang attribute value.
     */
    public void setXMLlang(String lang) {
        setAttributeNS(XMLSupport.XML_NAMESPACE_URI, XMLSupport.XML_LANG_ATTRIBUTE, lang);
    }

    /**
     * <b>DOM</b>: Returns the xml:space attribute value.
     */
    public String getXMLspace() {
        return XMLSupport.getXMLSpace(this);
    }

    /**
     * <b>DOM</b>: Sets the xml:space attribute value.
     */
    public void setXMLspace(String space) {
        setAttributeNS(XMLSupport.XML_NAMESPACE_URI, XMLSupport.XML_SPACE_ATTRIBUTE, space);
    }
}
