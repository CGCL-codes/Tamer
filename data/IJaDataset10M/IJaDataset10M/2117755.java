package net.sourceforge.greenvine.model;

/**
 * Class DatasourceType.
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class DatasourceType implements java.io.Serializable {

    /**
     * Field _name.
     */
    private java.lang.String _name;

    /**
     * Field _vendor.
     */
    private net.sourceforge.greenvine.database.propertytypes.types.DatabaseVendorType _vendor;

    /**
     * Field _connection.
     */
    private net.sourceforge.greenvine.model.Connection _connection;

    /**
     * Field _entities.
     */
    private net.sourceforge.greenvine.model.Entities _entities;

    public DatasourceType() {
        super();
    }

    /**
     * Returns the value of field 'connection'.
     * 
     * @return the value of field 'Connection'.
     */
    public net.sourceforge.greenvine.model.Connection getConnection() {
        return this._connection;
    }

    /**
     * Returns the value of field 'entities'.
     * 
     * @return the value of field 'Entities'.
     */
    public net.sourceforge.greenvine.model.Entities getEntities() {
        return this._entities;
    }

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'Name'.
     */
    public java.lang.String getName() {
        return this._name;
    }

    /**
     * Returns the value of field 'vendor'.
     * 
     * @return the value of field 'Vendor'.
     */
    public net.sourceforge.greenvine.database.propertytypes.types.DatabaseVendorType getVendor() {
        return this._vendor;
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
     * Sets the value of field 'connection'.
     * 
     * @param connection the value of field 'connection'.
     */
    public void setConnection(final net.sourceforge.greenvine.model.Connection connection) {
        this._connection = connection;
    }

    /**
     * Sets the value of field 'entities'.
     * 
     * @param entities the value of field 'entities'.
     */
    public void setEntities(final net.sourceforge.greenvine.model.Entities entities) {
        this._entities = entities;
    }

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(final java.lang.String name) {
        this._name = name;
    }

    /**
     * Sets the value of field 'vendor'.
     * 
     * @param vendor the value of field 'vendor'.
     */
    public void setVendor(final net.sourceforge.greenvine.database.propertytypes.types.DatabaseVendorType vendor) {
        this._vendor = vendor;
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
     * net.sourceforge.greenvine.model.DatasourceType
     */
    public static net.sourceforge.greenvine.model.DatasourceType unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (net.sourceforge.greenvine.model.DatasourceType) org.exolab.castor.xml.Unmarshaller.unmarshal(net.sourceforge.greenvine.model.DatasourceType.class, reader);
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
