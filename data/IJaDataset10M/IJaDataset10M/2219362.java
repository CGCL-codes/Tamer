package com.gele.tools.dbedit.castor;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Define the Layout for the NORTH Panel
 * 
 * @version $Revision$ $Date$
 */
public class DefNorthPanel implements java.io.Serializable {

    /**
   * Field _name
   */
    private java.lang.String _name;

    public DefNorthPanel() {
        super();
    }

    /**
   * Returns the value of field 'name'.
   * 
   * @return String
   * @return the value of field 'name'.
   */
    public java.lang.String getName() {
        return this._name;
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
   * Sets the value of field 'name'.
   * 
   * @param name the value of field 'name'.
   */
    public void setName(java.lang.String name) {
        this._name = name;
    }

    /**
   * Method unmarshal
   * 
   * 
   * 
   * @param reader
   * @return DefNorthPanel
   */
    public static com.gele.tools.dbedit.castor.DefNorthPanel unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (com.gele.tools.dbedit.castor.DefNorthPanel) Unmarshaller.unmarshal(com.gele.tools.dbedit.castor.DefNorthPanel.class, reader);
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
