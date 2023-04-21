package org.openejb.alt.config.ejb11;

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * 
 * @version $Revision: 1.3 $ $Date: 2002/12/28 19:20:32 $
**/
public class ContainerTransaction implements java.io.Serializable {

    private java.lang.String _id;

    private java.lang.String _description;

    private java.util.Vector _methodList;

    private java.lang.String _transAttribute;

    public ContainerTransaction() {
        super();
        _methodList = new Vector();
    }

    /**
     * 
     * @param vMethod
    **/
    public void addMethod(Method vMethod) throws java.lang.IndexOutOfBoundsException {
        _methodList.addElement(vMethod);
    }

    /**
    **/
    public java.util.Enumeration enumerateMethod() {
        return _methodList.elements();
    }

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    }

    /**
    **/
    public java.lang.String getId() {
        return this._id;
    }

    /**
     * 
     * @param index
    **/
    public Method getMethod(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (Method) _methodList.elementAt(index);
    }

    /**
    **/
    public Method[] getMethod() {
        int size = _methodList.size();
        Method[] mArray = new Method[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Method) _methodList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getMethodCount() {
        return _methodList.size();
    }

    /**
    **/
    public java.lang.String getTransAttribute() {
        return this._transAttribute;
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
    **/
    public void removeAllMethod() {
        _methodList.removeAllElements();
    }

    /**
     * 
     * @param index
    **/
    public Method removeMethod(int index) {
        Object obj = _methodList.elementAt(index);
        _methodList.removeElementAt(index);
        return (Method) obj;
    }

    /**
     * 
     * @param _description
    **/
    public void setDescription(java.lang.String _description) {
        this._description = _description;
    }

    /**
     * 
     * @param _id
    **/
    public void setId(java.lang.String _id) {
        this._id = _id;
    }

    /**
     * 
     * @param index
     * @param vMethod
    **/
    public void setMethod(int index, Method vMethod) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _methodList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _methodList.setElementAt(vMethod, index);
    }

    /**
     * 
     * @param methodArray
    **/
    public void setMethod(Method[] methodArray) {
        _methodList.removeAllElements();
        for (int i = 0; i < methodArray.length; i++) {
            _methodList.addElement(methodArray[i]);
        }
    }

    /**
     * 
     * @param _transAttribute
    **/
    public void setTransAttribute(java.lang.String _transAttribute) {
        this._transAttribute = _transAttribute;
    }

    /**
     * 
     * @param reader
    **/
    public static org.openejb.alt.config.ejb11.ContainerTransaction unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.openejb.alt.config.ejb11.ContainerTransaction) Unmarshaller.unmarshal(org.openejb.alt.config.ejb11.ContainerTransaction.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
