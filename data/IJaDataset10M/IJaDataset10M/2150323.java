package org.uddi4j.datatype.business;

import java.util.Vector;
import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the address element within the UDDI version 2.0 schema.
 * This class contains the following types of methods:
 * 
 * <ul>
 *   <li>Constructor passing required fields.
 *   <li>Constructor that will instantiate the object from an appropriate XML
 *       DOM element.
 *   <li>Get/set methods for each attribute that this element can contain.
 *   <li>A get/setVector method is provided for sets of attributes.
 *   <li>SaveToXML method. Serializes this class within a passed in element.
 * </ul>
 * 
 * Typically, this class is used to construct parameters for, or interpret
 * responses from methods in the UDDIProxy class.
 *
 * <p><b>Element description:</b>
 * Data: a printable, free form address.  Typed by convention.  Sort not used.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 * @author Ravi Trivedi (ravi_trivedi@hp.com)
 */
public class Address extends UDDIElement {

    public static final String UDDI_TAG = "address";

    protected Element base = null;

    String useType = null;

    String sortCode = null;

    String tModelKey = null;

    Vector addressLine = new Vector();

    /**
	 * Default constructor.
	 * Avoid using the default constructor for validation. It does not validate
	 * required fields. Instead, use the required fields constructor to perform
	 * validation.
	 */
    public Address() {
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
    public Address(Element base) throws UDDIException {
        super(base);
        useType = getAttr(base, "useType");
        sortCode = getAttr(base, "sortCode");
        tModelKey = getAttr(base, "tModelKey");
        NodeList nl = null;
        nl = getChildElementsByTagName(base, AddressLine.UDDI_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            addressLine.addElement(new AddressLine((Element) nl.item(i)));
        }
    }

    private String getAttr(Element base, String attrname) {
        if (base.getAttributeNode(attrname) != null && base.getAttributeNode(attrname).getSpecified()) {
            return base.getAttribute(attrname);
        }
        return null;
    }

    public void setUseType(String s) {
        useType = s;
    }

    public void setSortCode(String s) {
        sortCode = s;
    }

    /**
	 * Set addressLine vector
	 *
	 * @param s  Vector of <I>AddressLine</I> objects.
	 */
    public void setAddressLineVector(Vector s) {
        addressLine = s;
    }

    /**
	 * Set addressLine
	 *
	 * @param s  Vector of <I>String</I> objects.
	 */
    public void setAddressLineStrings(Vector s) {
        addressLine = new Vector();
        for (int i = 0; i < s.size(); i++) {
            addressLine.addElement(new AddressLine((String) s.elementAt(i)));
        }
    }

    public void setTModelKey(String key) {
        tModelKey = key;
    }

    public String getUseType() {
        return useType;
    }

    public String getSortCode() {
        return sortCode;
    }

    /**
	 * Get addressLine
	 *
	 * @return s Vector of <I>AddressLine</I> objects.
	 */
    public Vector getAddressLineVector() {
        return addressLine;
    }

    /**
	 * Get addressLine
	 *
	 * @return s Vector of <I>String</I> objects.
	 */
    public Vector getAddressLineStrings() {
        Vector strings = new Vector();
        for (int i = 0; i < addressLine.size(); i++) {
            strings.addElement(((AddressLine) addressLine.elementAt(i)).getText());
        }
        return strings;
    }

    public String getTModelKey() {
        return tModelKey;
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
        if (useType != null) {
            base.setAttribute("useType", useType);
        }
        if (sortCode != null) {
            base.setAttribute("sortCode", sortCode);
        }
        if (tModelKey != null) {
            base.setAttribute("tModelKey", tModelKey);
        }
        if (addressLine != null) {
            for (int i = 0; i < addressLine.size(); i++) {
                ((AddressLine) (addressLine.elementAt(i))).saveToXML(base);
            }
        }
        parent.appendChild(base);
    }
}
