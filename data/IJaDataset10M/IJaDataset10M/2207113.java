package com.iver.andami.plugins.config.generate;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Depends.
 * 
 * @version $Revision: 15983 $ $Date: 2007-11-07 12:11:19 +0100 (Wed, 07 Nov 2007) $
 */
public class Depends implements java.io.Serializable {

    /**
     * Field _pluginName
     */
    private java.lang.String _pluginName;

    public Depends() {
        super();
    }

    /**
     * Returns the value of field 'pluginName'.
     * 
     * @return the value of field 'pluginName'.
     */
    public java.lang.String getPluginName() {
        return this._pluginName;
    }

    /**
     * Method isValid
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
     * @param out
     */
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'pluginName'.
     * 
     * @param pluginName the value of field 'pluginName'.
     */
    public void setPluginName(java.lang.String pluginName) {
        this._pluginName = pluginName;
    }

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.iver.andami.plugins.config.generate.Depends) Unmarshaller.unmarshal(com.iver.andami.plugins.config.generate.Depends.class, reader);
    }

    /**
     * Method validate
     */
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
