package org.actioncenters.xml.reports;

/**
 * Report Root Element
 * 
 * @version $Revision$ $Date$
 */
@SuppressWarnings("serial")
public class Report implements java.io.Serializable {

    /**
     * Field _title.
     */
    private java.lang.String _title;

    /**
     * Field _introduction.
     */
    private java.lang.String _introduction;

    /**
     * Field _workspace.
     */
    private org.actioncenters.xml.reports.Workspace _workspace;

    public Report() {
        super();
    }

    /**
     * Returns the value of field 'introduction'.
     * 
     * @return the value of field 'Introduction'.
     */
    public java.lang.String getIntroduction() {
        return this._introduction;
    }

    /**
     * Returns the value of field 'title'.
     * 
     * @return the value of field 'Title'.
     */
    public java.lang.String getTitle() {
        return this._title;
    }

    /**
     * Returns the value of field 'workspace'.
     * 
     * @return the value of field 'Workspace'.
     */
    public org.actioncenters.xml.reports.Workspace getWorkspace() {
        return this._workspace;
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
     * Sets the value of field 'introduction'.
     * 
     * @param introduction the value of field 'introduction'.
     */
    public void setIntroduction(final java.lang.String introduction) {
        this._introduction = introduction;
    }

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(final java.lang.String title) {
        this._title = title;
    }

    /**
     * Sets the value of field 'workspace'.
     * 
     * @param workspace the value of field 'workspace'.
     */
    public void setWorkspace(final org.actioncenters.xml.reports.Workspace workspace) {
        this._workspace = workspace;
    }

    /**
     * Method unmarshal.
     * 
     * @param reader
     * @throws org.exolab.castor.xml.MarshalException if object is
     * null or if any SAXException is thrown during marshaling
     * @throws org.exolab.castor.xml.ValidationException if this
     * object is an invalid instance according to the schema
     * @return the unmarshaled org.actioncenters.xml.reports.Report
     */
    public static org.actioncenters.xml.reports.Report unmarshal(final java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.actioncenters.xml.reports.Report) org.exolab.castor.xml.Unmarshaller.unmarshal(org.actioncenters.xml.reports.Report.class, reader);
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
