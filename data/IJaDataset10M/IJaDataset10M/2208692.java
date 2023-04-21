package uk.org.ogsadai.resource;

import java.util.Calendar;
import java.util.Date;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import uk.org.ogsadai.common.Base64;
import uk.org.ogsadai.resource.property.DatePropertyConvertor;
import uk.org.ogsadai.util.xml.XML;

/**
 * A helper class that serialises property values and wraps them into a DOM
 * element with the property name.
 *
 * @author The OGSA-DAI Team.
 */
public class ResourcePropertySerialiser {

    /** Copyright notice */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh 2007";

    /**
     * Wraps the given property value into an element.
     * 
     * @param name
     *            name of the property
     * @param value
     *            value of the property as a string
     * @return the element that wraps the property value
     */
    public static Element toPropertyElement(ResourcePropertyName name, Node value) {
        final Document document = XML.getNewDocument();
        final Element element = document.createElementNS(name.getNamespace(), name.getLocalPart());
        document.appendChild(element);
        element.appendChild(value);
        return element;
    }

    /**
     * Convert a <tt>null</tt> into an empty element.
     * 
     * @return the element
     */
    public static Text toNullText() {
        final Document document = XML.getNewDocument();
        return document.createTextNode(null);
    }

    /**
     * Wraps the given property value into an element.
     * 
     * @param value
     *            value of the property as a string
     * @return the element that wraps the property value
     */
    public static Text toText(String value) {
        final Document document = XML.getNewDocument();
        return document.createTextNode(value);
    }

    /**
     * Wraps a character array into an element.
     * 
     * @param charArray
     *            property value
     * @return an element wrapping the property value
     */
    public static Text toText(char[] charArray) {
        return toText(new String(charArray));
    }

    /**
     * Encodes a byte array and wraps it into an element.
     * 
     * @param bytes
     *            the property value
     * @return the encoded bytes wrapped into an element.
     */
    public static Text toText(byte[] bytes) {
        return toText(Base64.encode(bytes));
    }

    /**
     * Serialises a date and wraps it into an element.
     * 
     * @param date
     *            the property value
     * @return the date wrapped into an element.
     */
    public static Text toText(Date date) {
        return (Text) DatePropertyConvertor.serialize(date)[0];
    }

    /**
     * Serialises a date and wraps it into an element.
     * 
     * @param calendar
     *            the property value
     * @return the date wrapped into an element.
     */
    public static Text toText(Calendar calendar) {
        return (Text) DatePropertyConvertor.serialize(calendar)[0];
    }
}
