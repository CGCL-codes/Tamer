package org.light.portal.portlet.definition;

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The init-param element contains a name/value pair as an 
 *  initialization param of the portlet
 *  Used in:portlet
 *  
 * 
 * @version $Revision$ $Date$
 */
public class InitParamType implements java.io.Serializable {

    /**
     * Field _id
     */
    private java.lang.String _id;

    /**
     * Field _descriptionList
     */
    private java.util.Vector _descriptionList;

    /**
     * Field _name
     */
    private org.light.portal.portlet.definition.Name _name;

    /**
     * Field _value
     */
    private org.light.portal.portlet.definition.Value _value;

    public InitParamType() {
        super();
        _descriptionList = new Vector();
    }

    /**
     * Method addDescription
     * 
     * 
     * 
     * @param vDescription
     */
    public void addDescription(org.light.portal.portlet.definition.Description vDescription) throws java.lang.IndexOutOfBoundsException {
        _descriptionList.addElement(vDescription);
    }

    /**
     * Method addDescription
     * 
     * 
     * 
     * @param index
     * @param vDescription
     */
    public void addDescription(int index, org.light.portal.portlet.definition.Description vDescription) throws java.lang.IndexOutOfBoundsException {
        _descriptionList.insertElementAt(vDescription, index);
    }

    /**
     * Method enumerateDescription
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateDescription() {
        return _descriptionList.elements();
    }

    /**
     * Method getDescription
     * 
     * 
     * 
     * @param index
     * @return Description
     */
    public org.light.portal.portlet.definition.Description getDescription(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index >= _descriptionList.size())) {
            throw new IndexOutOfBoundsException("getDescription: Index value '" + index + "' not in range [0.." + (_descriptionList.size() - 1) + "]");
        }
        return (org.light.portal.portlet.definition.Description) _descriptionList.elementAt(index);
    }

    /**
     * Method getDescription
     * 
     * 
     * 
     * @return Description
     */
    public org.light.portal.portlet.definition.Description[] getDescription() {
        int size = _descriptionList.size();
        org.light.portal.portlet.definition.Description[] mArray = new org.light.portal.portlet.definition.Description[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.light.portal.portlet.definition.Description) _descriptionList.elementAt(index);
        }
        return mArray;
    }

    /**
     * Method getDescriptionCount
     * 
     * 
     * 
     * @return int
     */
    public int getDescriptionCount() {
        return _descriptionList.size();
    }

    /**
     * Returns the value of field 'id'.
     * 
     * @return String
     * @return the value of field 'id'.
     */
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return Name
     * @return the value of field 'name'.
     */
    public org.light.portal.portlet.definition.Name getName() {
        return this._name;
    }

    /**
     * Returns the value of field 'value'.
     * 
     * @return Value
     * @return the value of field 'value'.
     */
    public org.light.portal.portlet.definition.Value getValue() {
        return this._value;
    }

    /**
     * Method isValid
     * 
     * 
     * 
     * @return boolean
     */
    public boolean isValid() {
        try {
            validate();
        } catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    }

    /**
     * Method marshal
     * 
     * 
     * 
     * @param out
     */
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * Method marshal
     * 
     * 
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Method removeAllDescription
     * 
     */
    public void removeAllDescription() {
        _descriptionList.removeAllElements();
    }

    /**
     * Method removeDescription
     * 
     * 
     * 
     * @param index
     * @return Description
     */
    public org.light.portal.portlet.definition.Description removeDescription(int index) {
        java.lang.Object obj = _descriptionList.elementAt(index);
        _descriptionList.removeElementAt(index);
        return (org.light.portal.portlet.definition.Description) obj;
    }

    /**
     * Method setDescription
     * 
     * 
     * 
     * @param index
     * @param vDescription
     */
    public void setDescription(int index, org.light.portal.portlet.definition.Description vDescription) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index >= _descriptionList.size())) {
            throw new IndexOutOfBoundsException("setDescription: Index value '" + index + "' not in range [0.." + (_descriptionList.size() - 1) + "]");
        }
        _descriptionList.setElementAt(vDescription, index);
    }

    /**
     * Method setDescription
     * 
     * 
     * 
     * @param descriptionArray
     */
    public void setDescription(org.light.portal.portlet.definition.Description[] descriptionArray) {
        _descriptionList.removeAllElements();
        for (int i = 0; i < descriptionArray.length; i++) {
            _descriptionList.addElement(descriptionArray[i]);
        }
    }

    /**
     * Sets the value of field 'id'.
     * 
     * @param id the value of field 'id'.
     */
    public void setId(java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(org.light.portal.portlet.definition.Name name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(org.light.portal.portlet.definition.Value value) {
        this._value = value;
    }

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return InitParamType
     */
    public static org.light.portal.portlet.definition.InitParamType unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.light.portal.portlet.definition.InitParamType) Unmarshaller.unmarshal(org.light.portal.portlet.definition.InitParamType.class, reader);
    }

    /**
     * Method validate
     * 
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
