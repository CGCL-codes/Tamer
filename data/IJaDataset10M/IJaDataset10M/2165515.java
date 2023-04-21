package net.sourceforge.greenvine.model;

/**
 * Class SingleAssociationType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class SingleAssociationType extends net.sourceforge.greenvine.model.BaseAssociationType implements java.io.Serializable {

    /**
     * Field _entityRef.
     */
    private net.sourceforge.greenvine.model.EntityRef _entityRef;

    public SingleAssociationType() {
        super();
    }

    /**
     * Returns the value of field 'entityRef'.
     * 
     * @return the value of field 'EntityRef'.
     */
    public net.sourceforge.greenvine.model.EntityRef getEntityRef() {
        return this._entityRef;
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
     * Sets the value of field 'entityRef'.
     * 
     * @param entityRef the value of field 'entityRef'.
     */
    public void setEntityRef(final net.sourceforge.greenvine.model.EntityRef entityRef) {
        this._entityRef = entityRef;
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
     * net.sourceforge.greenvine.model.SingleAssociationType
     */
    public static net.sourceforge.greenvine.model.SingleAssociationType unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (net.sourceforge.greenvine.model.SingleAssociationType) org.exolab.castor.xml.Unmarshaller.unmarshal(net.sourceforge.greenvine.model.SingleAssociationType.class, reader);
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
