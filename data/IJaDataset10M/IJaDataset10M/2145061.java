package org.light.portal.portlet.definition;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class PortletMode.
 * 
 * @version $Revision$ $Date$
 */
public class PortletMode extends PortletModeType implements java.io.Serializable {

    public PortletMode() {
        super();
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
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return PortletModeType
     */
    public static org.light.portal.portlet.definition.PortletModeType unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.light.portal.portlet.definition.PortletModeType) Unmarshaller.unmarshal(org.light.portal.portlet.definition.PortletMode.class, reader);
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
