package org.uddi4j.request;

import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.uddi4j.datatype.Name;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.IdentifierBag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the find_tModel element within the UDDI version 2.0 schema.
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
 * <p><b>Element description:</b>
 * <p>This message is used to search for summary results that lists registered
 * tModel data that matches specific criteria.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 * @author Ozzy (ozzy@hursley.ibm.com)
 */
public class FindTModel extends UDDIElement {

    public static final String UDDI_TAG = "find_tModel";

    protected Element base = null;

    String maxRows = null;

    FindQualifiers findQualifiers = null;

    Name name = null;

    IdentifierBag identifierBag = null;

    CategoryBag categoryBag = null;

    /**
    * Default constructor.
    * Avoid using the default constructor for validation. It does not validate
    * required fields. Instead, use the required fields constructor to perform
    * validation.
    */
    public FindTModel() {
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
    public FindTModel(Element base) throws UDDIException {
        super(base);
        maxRows = base.getAttribute("maxRows");
        NodeList nl = null;
        nl = getChildElementsByTagName(base, FindQualifiers.UDDI_TAG);
        if (nl.getLength() > 0) {
            findQualifiers = new FindQualifiers((Element) nl.item(0));
        }
        nl = getChildElementsByTagName(base, Name.UDDI_TAG);
        if (nl.getLength() > 0) {
            name = new Name((Element) nl.item(0));
        }
        nl = getChildElementsByTagName(base, IdentifierBag.UDDI_TAG);
        if (nl.getLength() > 0) {
            identifierBag = new IdentifierBag((Element) nl.item(0));
        }
        nl = getChildElementsByTagName(base, CategoryBag.UDDI_TAG);
        if (nl.getLength() > 0) {
            categoryBag = new CategoryBag((Element) nl.item(0));
        }
    }

    public void setMaxRows(String s) {
        maxRows = s;
    }

    public void setMaxRows(int s) {
        maxRows = Integer.toString(s);
    }

    public void setFindQualifiers(FindQualifiers s) {
        findQualifiers = s;
    }

    public void setName(Name s) {
        name = s;
    }

    public void setName(String s) {
        if (s != null && !s.equals("")) {
            name = new Name();
            name.setText(s);
        }
    }

    public void setIdentifierBag(IdentifierBag s) {
        identifierBag = s;
    }

    public void setCategoryBag(CategoryBag s) {
        categoryBag = s;
    }

    public String getMaxRows() {
        return maxRows;
    }

    public int getMaxRowsInt() {
        return Integer.parseInt(maxRows);
    }

    public FindQualifiers getFindQualifiers() {
        return findQualifiers;
    }

    public Name getName() {
        return name;
    }

    public String getNameString() {
        if (name != null) return name.getText(); else return null;
    }

    public IdentifierBag getIdentifierBag() {
        return identifierBag;
    }

    public CategoryBag getCategoryBag() {
        return categoryBag;
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
        base.setAttribute("generic", UDDIElement.GENERIC);
        if (maxRows != null) {
            base.setAttribute("maxRows", maxRows);
        }
        if (findQualifiers != null) {
            findQualifiers.saveToXML(base);
        }
        if (name != null) {
            name.saveToXML(base);
        }
        if (identifierBag != null) {
            identifierBag.saveToXML(base);
        }
        if (categoryBag != null) {
            categoryBag.saveToXML(base);
        }
        parent.appendChild(base);
    }
}
