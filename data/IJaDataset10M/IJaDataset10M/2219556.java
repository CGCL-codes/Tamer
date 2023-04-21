package org.actioncenters.elements;

/**
 * Class Booleanfieldtype.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Booleanfieldtype extends org.actioncenters.elements.Fieldtype implements java.io.Serializable {

    /**
     * Field _type.
     */
    private org.actioncenters.elements.Type _type;

    /**
     * Field _initialBooleanValue.
     */
    private java.lang.String _initialBooleanValue;

    /**
     * Field _true.
     */
    private org.actioncenters.elements.True _true;

    /**
     * Field _false.
     */
    private org.actioncenters.elements.False _false;

    public Booleanfieldtype() {
        super();
    }

    /**
     * Returns the value of field 'false'.
     * 
     * @return the value of field 'False'.
     */
    public org.actioncenters.elements.False getFalse() {
        return this._false;
    }

    /**
     * Returns the value of field 'initialBooleanValue'.
     * 
     * @return the value of field 'InitialBooleanValue'.
     */
    public java.lang.String getInitialBooleanValue() {
        return this._initialBooleanValue;
    }

    /**
     * Returns the value of field 'true'.
     * 
     * @return the value of field 'True'.
     */
    public org.actioncenters.elements.True getTrue() {
        return this._true;
    }

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'Type'.
     */
    public org.actioncenters.elements.Type getType() {
        return this._type;
    }

    /**
     * Method isValid.
     * 
     * @return true if this object is valid according to the schema
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
     * 
     * 
     * @param out
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void marshal(final java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
     * @throws java.io.IOException if an IOException occurs during
     * marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     */
    public void marshal(final org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'false'.
     * 
     * @param _false
     * @param false the value of field 'false'.
     */
    public void setFalse(final org.actioncenters.elements.False _false) {
        this._false = _false;
    }

    /**
     * Sets the value of field 'initialBooleanValue'.
     * 
     * @param initialBooleanValue the value of field
     * 'initialBooleanValue'.
     */
    public void setInitialBooleanValue(final java.lang.String initialBooleanValue) {
        this._initialBooleanValue = initialBooleanValue;
    }

    /**
     * Sets the value of field 'true'.
     * 
     * @param _true
     * @param true the value of field 'true'.
     */
    public void setTrue(final org.actioncenters.elements.True _true) {
        this._true = _true;
    }

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(final org.actioncenters.elements.Type type) {
        this._type = type;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled
     * org.actioncenters.elements.Booleanfieldtype
     */
    public static org.actioncenters.elements.Booleanfieldtype unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.actioncenters.elements.Booleanfieldtype) org.exolab.castor.xml.Unmarshaller.unmarshal(org.actioncenters.elements.Booleanfieldtype.class, reader);
    }

    /**
     * 
     * 
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
