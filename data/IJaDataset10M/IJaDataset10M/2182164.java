package uk.ac.ncl.neresc.dynasoar.messages;

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
 * Class DetailsRequest.
 * 
 * @version $Revision$ $Date$
 */
public class DetailsRequest implements java.io.Serializable {

    /**
     * Field _serviceName
     */
    private java.lang.String _serviceName;

    public DetailsRequest() {
        super();
    }

    /**
     * Returns the value of field 'serviceName'.
     * 
     * @return String
     * @return the value of field 'serviceName'.
     */
    public java.lang.String getServiceName() {
        return this._serviceName;
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
     * Sets the value of field 'serviceName'.
     * 
     * @param serviceName the value of field 'serviceName'.
     */
    public void setServiceName(java.lang.String serviceName) {
        this._serviceName = serviceName;
    }

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return Object
     */
    public static java.lang.Object unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (uk.ac.ncl.neresc.dynasoar.messages.DetailsRequest) Unmarshaller.unmarshal(uk.ac.ncl.neresc.dynasoar.messages.DetailsRequest.class, reader);
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
