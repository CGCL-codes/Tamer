package org.uddi4j.util;

import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.w3c.dom.Element;

/**
 * Represents the serviceKey element within the UDDI version 2.0 schema.
 * This class contains the following types of methods:
 * 
 * <ul>
 *   <li>A constructor that passes the required fields.
 *   <li>A Constructor that will instantiate the object from an appropriate XML
 *       DOM element.
 *   <li>Get/set methods for each attribute that this element can contain.
 *   <li>A get/setVector method is provided for sets of attributes.
 *   <li>A SaveToXML method that serializes this class within a passed in
 *       element.
 * </ul>
 * 
 * Typically, this class is used to construct parameters for, or interpret
 * responses from, methods in the UDDIProxy class.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 */
public class ServiceKey extends UDDIElement {

    public static final String UDDI_TAG = "serviceKey";

    protected Element base = null;

    String text = null;

    /**
    * Default constructor.
    * Avoid using the default constructor for validation. It does not validate
    * required fields. Instead, use the required fields constructor to perform
    * validation.
    */
    public ServiceKey() {
    }

    /**
    * Construct the object with required fields.
    *
    * @param value  String value
    */
    public ServiceKey(String value) {
        setText(value);
    }

    /**
    * Construct the object from a DOM tree. Used by
    * UDDIProxy to construct an object from a received UDDI
    * message.
    *
    * @param base   Element with the name appropriate for this class.
    *
    * @exception UDDIException Thrown if DOM tree contains a SOAP fault
    *  or a disposition report indicating a UDDI error.
    */
    public ServiceKey(Element base) throws UDDIException {
        super(base);
        text = getText(base);
    }

    public void setText(String s) {
        text = s;
    }

    public String getText() {
        return text;
    }

    /**
    * Save an object to the DOM tree. Used to serialize an object
    * to a DOM tree, usually to send a UDDI message.
    *
    * <BR>Used by UDDIProxy.
    *
    * @param parent Object will serialize as a child element under the
    *  passed in parent element.
    */
    public void saveToXML(Element parent) {
        base = parent.getOwnerDocument().createElementNS(UDDIElement.XMLNS, UDDIElement.XMLNS_PREFIX + UDDI_TAG);
        if (text != null) {
            base.appendChild(parent.getOwnerDocument().createTextNode(text));
        }
        parent.appendChild(base);
    }
}
