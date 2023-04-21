package org.uddi4j.response;

import java.util.Vector;
import org.uddi4j.UDDIElement;
import org.uddi4j.UDDIException;
import org.uddi4j.datatype.Description;
import org.uddi4j.datatype.Name;
import org.uddi4j.util.BusinessKey;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Represents the relatedBusinessInfo element within the UDDI version 2.0 schema.
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
 * <p>This structure contains information about one or more relationships between
 * two businessEntitys. The information can be a businessKey, name and optional
 * description data, and a collection element named sharedRelationships.
 * The sharedRelationships element can contain zero or more keyedReference
 * elements. The information in the keyedReference and businessKey elements,
 * for a specific businessEntity, represent complete relationships when they
 * match publisher assertions made by the publisher for each businessEntity.
 *
 * @author Ravi Trivedi (ravi_trivedi@hp.com)
 * @author Ozzy (ozzy@hursley.ibm.com)
 */
public class RelatedBusinessInfo extends UDDIElement {

    public static final String UDDI_TAG = "relatedBusinessInfo";

    protected Element base = null;

    BusinessKey businessKey = null;

    Vector nameVector = new Vector();

    Vector description = new Vector();

    Vector sharedRelationships = new Vector();

    /**
    * Default constructor.
    * Avoid using the default constructor for validation. It does not validate
    * required fields. Instead, use the required fields constructor to perform
    * validation.
    */
    public RelatedBusinessInfo() {
    }

    /**
    * Required fields constructor.
    * This constructor initialises the object with the fields 
    * required by the uddi specification.
    * @param businessKey BusinessKey
    * @param names Vector of Name objects
    * @param sharedRelationShips Vector of SharedRelationship objects
    */
    public RelatedBusinessInfo(BusinessKey businessKey, Vector names, Vector sharedRelationships) {
        this.businessKey = businessKey;
        this.nameVector = names;
        this.sharedRelationships = sharedRelationships;
    }

    /**
    * Required fields constructor.
    * This constructor initialises the object with the fields 
    * required by the uddi specification.
    * @param businessKey String 
    * @param names Vector of Name objects
    * @param sharedRelationships Vector of SharedRelationship objects
    */
    public RelatedBusinessInfo(String businessKey, Vector names, Vector sharedRelationships) {
        this.businessKey = new BusinessKey(businessKey);
        this.nameVector = names;
        this.sharedRelationships = sharedRelationships;
    }

    /**
    * Required fields constructor.
    * This constructor initialises the object with the fields 
    * required by the uddi specification.
    * @param businessKey String 
    * @param name String The default name for this RelatedBusinessInfo
    * @param sharedRelationships Vector of SharedRelationship objects
    */
    public RelatedBusinessInfo(String businessKey, String name, Vector sharedRelationships) {
        this.businessKey = new BusinessKey(businessKey);
        setDefaultNameString(name, null);
        this.sharedRelationships = sharedRelationships;
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
    public RelatedBusinessInfo(Element base) throws UDDIException {
        super(base);
        NodeList nl = null;
        nl = getChildElementsByTagName(base, BusinessKey.UDDI_TAG);
        if (nl.getLength() > 0) {
            businessKey = new BusinessKey((Element) nl.item(0));
        }
        nl = getChildElementsByTagName(base, Name.UDDI_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            nameVector.addElement(new Name((Element) nl.item(i)));
        }
        nl = getChildElementsByTagName(base, Description.UDDI_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            description.addElement(new Description((Element) nl.item(i)));
        }
        nl = getChildElementsByTagName(base, SharedRelationships.UDDI_TAG);
        for (int i = 0; i < nl.getLength(); i++) {
            sharedRelationships.addElement(new SharedRelationships((Element) nl.item(i)));
        }
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setSharedRelationshipsVector(Vector)} or
    * {@link #setDefaultSharedRelationships(SharedRelationships)} instead
    */
    public void setSharedRelationships(SharedRelationships s) {
        setDefaultSharedRelationships(s);
    }

    /**
    * This method stores this name as the Default SharedRelationship 
    * (i.e., places it in the first location in the Vector).
    */
    public void setDefaultSharedRelationships(SharedRelationships s) {
        if (this.sharedRelationships.size() > 0) {
            this.sharedRelationships.setElementAt(s, 0);
        } else {
            this.sharedRelationships.addElement(s);
        }
    }

    /**
    * @param s  Vector of <I> SharedRelationships </I> objects
    */
    public void setSharedRelationshipsVector(Vector s) {
        sharedRelationships = s;
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setNameVector(Vector)} or
    * {@link #setDefaultName(Name)} instead
    */
    public void setName(Name s) {
        setDefaultName(s);
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setNameVector(Vector)} or
    * {@link #setDefaultNameString(String, String)} instead
    */
    public void setName(String s) {
        setDefaultNameString(s, null);
    }

    /**
    * This method stores this name as the Default Name (i.e., places it in the first
    * location in the Vector).
    */
    public void setDefaultName(Name name) {
        if (nameVector.size() > 0) {
            nameVector.setElementAt(name, 0);
        } else {
            nameVector.addElement(name);
        }
    }

    /**
    * This method stores this String, in the given language as the Default Name
    * (i.e., places it in the first location in the Vector).
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

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setDescriptionVector(Vector)} or
    * {@link #setDefaultDescription(Description)} instead
    */
    public void setDescription(Description s) {
        setDefaultDescription(s);
    }

    /**
    * @deprecated This method has been deprecated. Use
    * {@link #setDescriptionVector(Vector)} or
    * {@link #setDefaultDescriptionString(String, String)} instead
    */
    public void setDescriptionString(String s) {
        setDefaultDescriptionString(s, null);
    }

    /**
    * This method stores this Description as the Default Description
    * (i.e., places it in the first location in the Vector).
    *
    * @param s  Description
    */
    public void setDefaultDescription(Description s) {
        if (description.size() > 0) {
            description.setElementAt(s, 0);
        } else {
            description.addElement(s);
        }
    }

    /**
    * This method stores this String as the Default Description
    * (i.e., places it in the first location in the Vector).
    *
    * @param s  String
    */
    public void setDefaultDescriptionString(String s, String lang) {
        if (description.size() > 0) {
            description.setElementAt(new Description(s, lang), 0);
        } else {
            description.addElement(new Description(s, lang));
        }
    }

    /**
    * Set description vector.
    *
    * @param s  Vector of <I>Description</I> objects.
    */
    public void setDescriptionVector(Vector s) {
        description = s;
    }

    public void setBusinessKey(String s) {
        businessKey = new BusinessKey(s);
    }

    /**
   * @deprecated This method has been deprecated. Use
   * {@link #getSharedRelationshipsVector()} or
   * {@link #getDefaultSharedRelationships()} instead
   */
    public SharedRelationships getSharedRelationships() {
        return getDefaultSharedRelationships();
    }

    /**
    * Get the default SharedRelationships. 
    * (i.e., the one in the first position in the vector)
    * @return SharedRelationships
    */
    public SharedRelationships getDefaultSharedRelationships() {
        if (sharedRelationships.size() > 0) return (SharedRelationships) sharedRelationships.elementAt(0); else return null;
    }

    /**
    * Get all SharedRelationships.
    *
    * @return  Vector of <I>SharedRelationships</I> objects.
    */
    public Vector getSharedRelationshipsVector() {
        return sharedRelationships;
    }

    /**
   * @deprecated This method has been deprecated. Use
   * {@link #getNameVector()} or
   * {@link #getDefaultName()} instead
   */
    public Name getName() {
        return getDefaultName();
    }

    /**
   * @deprecated This method has been deprecated. Use
   * {@link #getNameVector()} or
   * {@link #getDefaultNameString()} instead
   */
    public String getNameString() {
        return getDefaultNameString();
    }

    /**
    * Get the default name. 
    * (i.e., the one in the first position in the vector)
    * @return Name
    */
    public Name getDefaultName() {
        if (nameVector.size() > 0) return (Name) nameVector.elementAt(0); else return null;
    }

    /**
    * Get default name string.
    * (i.e., the one in the first position in the vector)
    * @return  String
    */
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

    /**
   * @deprecated This method has been deprecated. Use
   * {@link #getDescriptionVector()} or
   * {@link #getDefaultDescription()} instead
   */
    public Description getDescription() {
        return getDefaultDescription();
    }

    /**
   * @deprecated This method has been deprecated. Use
   * {@link #getDescriptionVector()} or
   * {@link #getDefaultDescriptionString()} instead
   */
    public String getDescriptionString() {
        return getDefaultDescriptionString();
    }

    /**
   * Get the default Description.
   * (i.e., the one in the first position in the vector)
   * @return Description
   */
    public Description getDefaultDescription() {
        if (description.size() > 0) return ((Description) description.elementAt(0)); else return null;
    }

    /**
   * Get the default Description as a String.
   * (i.e., the first string representing the first Description in the vector)
   * @return String
   */
    public String getDefaultDescriptionString() {
        if (description.size() > 0) return ((Description) description.elementAt(0)).getText(); else return null;
    }

    /**
    * Get all deascriptions.
    * 
    * @return Vector of <I>Description</i> objects.
    */
    public Vector getDescriptionVector() {
        return description;
    }

    public String getBusinessKey() {
        return this.businessKey.getText();
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
        if (businessKey != null) {
            businessKey.saveToXML(base);
        }
        if (nameVector != null && nameVector.size() > 0) {
            for (int i = 0; i < nameVector.size(); i++) {
                ((Name) nameVector.elementAt(i)).saveToXML(base);
            }
        }
        if (description != null && description.size() > 0) {
            for (int i = 0; i < description.size(); i++) {
                ((Description) description.elementAt(i)).saveToXML(base);
            }
        }
        if (sharedRelationships != null && sharedRelationships.size() > 0) {
            for (int i = 0; i < sharedRelationships.size(); i++) {
                ((SharedRelationships) sharedRelationships.elementAt(i)).saveToXML(base);
            }
        }
        parent.appendChild(base);
    }
}
