package net.sf.practicalxml;

import javax.xml.XMLConstants;
import org.w3c.dom.Element;

/**
 *  A collection of static utility methods for updating/extracting the DOM
 *  with values defined by the XML Schema specification. Most of these
 *  methods delegate to conversion methods in {@link XmlUtil}.
 */
public class XsiUtil {

    /**
     *  Sets the <code>xsi:nil</code> attribute to the passed value.
     */
    public static void setXsiNil(Element elem, boolean isNil) {
        if (isNil) elem.setAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil", "true"); else elem.removeAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil");
    }

    /**
     *  Returns the value of the <code>xsi:nil</code> attribute on the passed
     *  element, <code>false</code> if the attribute is not set.
     */
    public static boolean getXsiNil(Element elem) {
        String attr = elem.getAttributeNS(XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI, "nil");
        return attr.equalsIgnoreCase("true") || attr.equals("1");
    }
}
