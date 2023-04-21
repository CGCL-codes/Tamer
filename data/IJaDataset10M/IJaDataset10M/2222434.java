package org.actioncenters.elements;

/**
 * Class Type.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Type implements java.io.Serializable {

    /**
     * Internal choice value storage
     */
    private java.lang.Object _choiceValue;

    /**
     * Field _radioButton.
     */
    private java.lang.String _radioButton;

    /**
     * Field _checkBox.
     */
    private java.lang.String _checkBox;

    /**
     * Field _singleSelectDropDown.
     */
    private java.lang.String _singleSelectDropDown;

    /**
     * Field _multiSelectDropDown.
     */
    private java.lang.String _multiSelectDropDown;

    public Type() {
        super();
    }

    /**
     * Returns the value of field 'checkBox'.
     * 
     * @return the value of field 'CheckBox'.
     */
    public java.lang.String getCheckBox() {
        return this._checkBox;
    }

    /**
     * Returns the value of field 'choiceValue'. The field
     * 'choiceValue' has the following description: Internal choice
     * value storage
     * 
     * @return the value of field 'ChoiceValue'.
     */
    public java.lang.Object getChoiceValue() {
        return this._choiceValue;
    }

    /**
     * Returns the value of field 'multiSelectDropDown'.
     * 
     * @return the value of field 'MultiSelectDropDown'.
     */
    public java.lang.String getMultiSelectDropDown() {
        return this._multiSelectDropDown;
    }

    /**
     * Returns the value of field 'radioButton'.
     * 
     * @return the value of field 'RadioButton'.
     */
    public java.lang.String getRadioButton() {
        return this._radioButton;
    }

    /**
     * Returns the value of field 'singleSelectDropDown'.
     * 
     * @return the value of field 'SingleSelectDropDown'.
     */
    public java.lang.String getSingleSelectDropDown() {
        return this._singleSelectDropDown;
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
     * Sets the value of field 'checkBox'.
     * 
     * @param checkBox the value of field 'checkBox'.
     */
    public void setCheckBox(final java.lang.String checkBox) {
        this._checkBox = checkBox;
        this._choiceValue = checkBox;
    }

    /**
     * Sets the value of field 'multiSelectDropDown'.
     * 
     * @param multiSelectDropDown the value of field
     * 'multiSelectDropDown'.
     */
    public void setMultiSelectDropDown(final java.lang.String multiSelectDropDown) {
        this._multiSelectDropDown = multiSelectDropDown;
        this._choiceValue = multiSelectDropDown;
    }

    /**
     * Sets the value of field 'radioButton'.
     * 
     * @param radioButton the value of field 'radioButton'.
     */
    public void setRadioButton(final java.lang.String radioButton) {
        this._radioButton = radioButton;
        this._choiceValue = radioButton;
    }

    /**
     * Sets the value of field 'singleSelectDropDown'.
     * 
     * @param singleSelectDropDown the value of field
     * 'singleSelectDropDown'.
     */
    public void setSingleSelectDropDown(final java.lang.String singleSelectDropDown) {
        this._singleSelectDropDown = singleSelectDropDown;
        this._choiceValue = singleSelectDropDown;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.actioncenters.elements.Type
     */
    public static org.actioncenters.elements.Type unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.actioncenters.elements.Type) org.exolab.castor.xml.Unmarshaller.unmarshal(org.actioncenters.elements.Type.class, reader);
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
