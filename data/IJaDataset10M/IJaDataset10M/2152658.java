package zing.config.xmlobjects;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.*;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * 
 * 
 * @version $Revision$ $Date$
**/
public class TestType implements java.io.Serializable {

    private java.lang.String _testtypeid;

    private Perfparams _perfparams;

    public TestType() {
        super();
    }

    /**
     * Returns the value of field 'perfparams'.
     * 
     * @return the value of field 'perfparams'.
    **/
    public Perfparams getPerfparams() {
        return this._perfparams;
    }

    /**
     * Returns the value of field 'testtypeid'.
     * 
     * @return the value of field 'testtypeid'.
    **/
    public java.lang.String getTesttypeid() {
        return this._testtypeid;
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
     * 
     * @param out
    **/
    public void marshal(java.io.Writer out) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, out);
    }

    /**
     * 
     * 
     * @param handler
    **/
    public void marshal(org.xml.sax.ContentHandler handler) throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        Marshaller.marshal(this, handler);
    }

    /**
     * Sets the value of field 'perfparams'.
     * 
     * @param perfparams the value of field 'perfparams'.
    **/
    public void setPerfparams(Perfparams perfparams) {
        this._perfparams = perfparams;
    }

    /**
     * Sets the value of field 'testtypeid'.
     * 
     * @param testtypeid the value of field 'testtypeid'.
    **/
    public void setTesttypeid(java.lang.String testtypeid) {
        this._testtypeid = testtypeid;
    }

    /**
     * 
     * 
     * @param reader
    **/
    public static zing.config.xmlobjects.TestType unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (zing.config.xmlobjects.TestType) Unmarshaller.unmarshal(zing.config.xmlobjects.TestType.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
