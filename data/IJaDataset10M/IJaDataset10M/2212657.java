package org.openejb.alt.config.sys;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * 
 * @version $Revision: 1.5 $ $Date: 2002/12/28 19:20:39 $
**/
public class JndiProvider implements java.io.Serializable, org.openejb.alt.config.Service {

    private java.lang.String _id;

    private java.lang.String _provider;

    private java.lang.String _jar;

    /**
     * internal content storage
    **/
    private java.lang.String _content = "";

    public JndiProvider() {
        super();
    }

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * @return the value of field 'content'.
    **/
    public java.lang.String getContent() {
        return this._content;
    }

    /**
     * Returns the value of field 'id'.
     * @return the value of field 'id'.
    **/
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * Returns the value of field 'jar'.
     * @return the value of field 'jar'.
    **/
    public java.lang.String getJar() {
        return this._jar;
    }

    /**
     * Returns the value of field 'provider'.
     * @return the value of field 'provider'.
    **/
    public java.lang.String getProvider() {
        return this._provider;
    }

    /**
    **/
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
     * @param out
    **/
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.DocumentHandler handler) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * @param content the value of field 'content'.
    **/
    public void setContent(java.lang.String content) {
        this._content = content;
    }

    /**
     * Sets the value of field 'id'.
     * @param id the value of field 'id'.
    **/
    public void setId(java.lang.String id) {
        this._id = id;
    }

    /**
     * Sets the value of field 'jar'.
     * @param jar the value of field 'jar'.
    **/
    public void setJar(java.lang.String jar) {
        this._jar = jar;
    }

    /**
     * Sets the value of field 'provider'.
     * @param provider the value of field 'provider'.
    **/
    public void setProvider(java.lang.String provider) {
        this._provider = provider;
    }

    /**
     * 
     * @param reader
    **/
    public static org.openejb.alt.config.sys.JndiProvider unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.openejb.alt.config.sys.JndiProvider) Unmarshaller.unmarshal(org.openejb.alt.config.sys.JndiProvider.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
