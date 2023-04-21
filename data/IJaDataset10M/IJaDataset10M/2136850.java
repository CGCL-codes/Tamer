package org.uddi4j.request;

import java.util.Vector;
import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.uddi4j.datatype.Name;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.TModelBag;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the find_service element within the UDDI version 2.0 schema.
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
 * <p>This message is used to search for summary results that list registered
 * businessService data that matches specific criteria.
 *
 * @author David Melgar (dmelgar@us.ibm.com)
 * @author Ravi Trivedi (ravi_trivedi@hp.com)
 * @author Vivek Chopra (vivek@soaprpc.com)
 * @author Ozzy (ozzy@hursley.ibm.com)
 */
public class FindService extends UDDIElement {

    public static final String UDDI_TAG = "find_service";

    protected Element base = null;

    String maxRows = null;

    String businessKey = null;

    FindQualifiers findQualifiers = null;

    CategoryBag categoryBag = null;

    TModelBag tModelBag = null;

    Vector nameVector = new Vector();

    /**
    * Default constructor.
    * Avoid using the default constructor for validation. It does not validate
    * required fields. Instead, use the required fields constructor to perform
    * validation.
    */
    public FindService() {
    }

    /**
    * Construct the object with required fields.
    *
    * @param businessKey    String
    */
    public FindService(String businessKey) {
        this.businessKey = businessKey;
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
    public FindService(Element base) throws UDDIException {
        super(base);
        maxRows = base.getAttribute("maxRows");
        businessKey = base.getAttribute("businessKey");
        NodeList nl = null;
        nl = getChildElementsByTagName(base, FindQualifiers.UDDI_TAG);
        if (nl.getLength() > 0) {
            findQualifiers = new FindQualifiers((Element) nl.item(0));
        }
        nl = getChildElementsByTagName(base, Name.UDDI_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            nameVector.addElement(new Name((Element) nl.item(i)));
        }
        nl = getChildElementsByTagName(base, CategoryBag.UDDI_TAG);
        if (nl.getLength() > 0) {
            categoryBag = new CategoryBag((Element) nl.item(0));
        }
        nl = getChildElementsByTagName(base, TModelBag.UDDI_TAG);
        if (nl.getLength() > 0) {
            tModelBag = new TModelBag((Element) nl.item(0));
        }
    }

    public void setMaxRows(String s) {
        maxRows = s;
    }

    public void setMaxRows(int s) {
        maxRows = Integer.toString(s);
    }

    public void setBusinessKey(String s) {
        businessKey = s;
    }

    public void setFindQualifiers(FindQualifiers s) {
        findQualifiers = s;
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setNameVector(Vector)} or
    * {@link #setDefaultName(Name)}
    */
    public void setName(Name s) {
        setDefaultName(s);
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setNameVector(Vector)} or
    * {@link #setDefaultNameString(String, String)}
    */
    public void setName(String s) {
        setDefaultNameString(s, null);
    }

    /**
    * This method stores this name as the Default Name
    * (i.e., places it in the first location in the Vector)
    */
    public void setDefaultName(Name name) {
        if (nameVector.size() > 0) {
            nameVector.setElementAt(name, 0);
        } else {
            nameVector.addElement(name);
        }
    }

    /**
    * This method stores this String, in the given language, as the
    * Default Name (i.e., places it in the first location in the Vector).
    */
    public void setDefaultNameString(String value, String lang) {
        Name name = new Name(value, lang);
        if (nameVector.size() > 0) {
            nameVector.setElementAt(name, 0);
        } else {
            nameVector.addElement(name);
        }
    }

    /**
    * @param s  Vector of <I> Name </I> objects
    */
    public void setNameVector(Vector s) {
        nameVector = s;
    }

    public void setCategoryBag(CategoryBag s) {
        categoryBag = s;
    }

    public void setTModelBag(TModelBag s) {
        tModelBag = s;
    }

    public String getMaxRows() {
        return maxRows;
    }

    public int getMaxRowsInt() {
        return Integer.parseInt(maxRows);
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public FindQualifiers getFindQualifiers() {
        return findQualifiers;
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #getNameVector()} or
    * {@link #getDefaultName()}
    */
    public Name getName() {
        return getDefaultName();
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #getNameVector()} or
    * {@link #getDefaultNameString()}
    */
    public String getNameString() {
        return getDefaultNameString();
    }

    public Name getDefaultName() {
        if (nameVector.size() > 0) return (Name) nameVector.elementAt(0); else return null;
    }

    public String getDefaultNameString() {
        if ((nameVector).size() > 0) {
            return ((Name) nameVector.elementAt(0)).getText();
        } else {
            return null;
        }
    }

    /**
    * Get all names.
    *
    * @return  Vector of <I>Name</I> objects.
    */
    public Vector getNameVector() {
        return nameVector;
    }

    public CategoryBag getCategoryBag() {
        return categoryBag;
    }

    public TModelBag getTModelBag() {
        return tModelBag;
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
        if (businessKey != null) {
            base.setAttribute("businessKey", businessKey);
        }
        if (findQualifiers != null) {
            findQualifiers.saveToXML(base);
        }
        if (nameVector != null) {
            for (int i = 0; i < nameVector.size(); i++) {
                ((Name) (nameVector.elementAt(i))).saveToXML(base);
            }
        }
        if (categoryBag != null) {
            categoryBag.saveToXML(base);
        }
        if (tModelBag != null) {
            tModelBag.saveToXML(base);
        }
        parent.appendChild(base);
    }
}
