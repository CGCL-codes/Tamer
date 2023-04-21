package org.teaframework.schema.service;

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Comment describing your root element
 * 
 * @version $Revision$ $Date$
 */
public class ServiceConfig implements java.io.Serializable {

    /**
     * Field _serviceEntryList
     */
    private java.util.Vector _serviceEntryList;

    public ServiceConfig() {
        super();
        _serviceEntryList = new Vector();
    }

    /**
     * Method addServiceEntry
     * 
     * 
     * 
     * @param vServiceEntry
     */
    public void addServiceEntry(org.teaframework.schema.service.ServiceEntry vServiceEntry) throws java.lang.IndexOutOfBoundsException {
        _serviceEntryList.addElement(vServiceEntry);
    }

    /**
     * Method addServiceEntry
     * 
     * 
     * 
     * @param index
     * @param vServiceEntry
     */
    public void addServiceEntry(int index, org.teaframework.schema.service.ServiceEntry vServiceEntry) throws java.lang.IndexOutOfBoundsException {
        _serviceEntryList.insertElementAt(vServiceEntry, index);
    }

    /**
     * Method enumerateServiceEntry
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateServiceEntry() {
        return _serviceEntryList.elements();
    }

    /**
     * Method getServiceEntry
     * 
     * 
     * 
     * @param index
     * @return ServiceEntry
     */
    public org.teaframework.schema.service.ServiceEntry getServiceEntry(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index >= _serviceEntryList.size())) {
            throw new IndexOutOfBoundsException("getServiceEntry: Index value '" + index + "' not in range [0.." + (_serviceEntryList.size() - 1) + "]");
        }
        return (org.teaframework.schema.service.ServiceEntry) _serviceEntryList.elementAt(index);
    }

    /**
     * Method getServiceEntry
     * 
     * 
     * 
     * @return ServiceEntry
     */
    public org.teaframework.schema.service.ServiceEntry[] getServiceEntry() {
        int size = _serviceEntryList.size();
        org.teaframework.schema.service.ServiceEntry[] mArray = new org.teaframework.schema.service.ServiceEntry[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.teaframework.schema.service.ServiceEntry) _serviceEntryList.elementAt(index);
        }
        return mArray;
    }

    /**
     * Method getServiceEntryCount
     * 
     * 
     * 
     * @return int
     */
    public int getServiceEntryCount() {
        return _serviceEntryList.size();
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
     * Method removeAllServiceEntry
     * 
     */
    public void removeAllServiceEntry() {
        _serviceEntryList.removeAllElements();
    }

    /**
     * Method removeServiceEntry
     * 
     * 
     * 
     * @param index
     * @return ServiceEntry
     */
    public org.teaframework.schema.service.ServiceEntry removeServiceEntry(int index) {
        java.lang.Object obj = _serviceEntryList.elementAt(index);
        _serviceEntryList.removeElementAt(index);
        return (org.teaframework.schema.service.ServiceEntry) obj;
    }

    /**
     * Method setServiceEntry
     * 
     * 
     * 
     * @param index
     * @param vServiceEntry
     */
    public void setServiceEntry(int index, org.teaframework.schema.service.ServiceEntry vServiceEntry) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index >= _serviceEntryList.size())) {
            throw new IndexOutOfBoundsException("setServiceEntry: Index value '" + index + "' not in range [0.." + (_serviceEntryList.size() - 1) + "]");
        }
        _serviceEntryList.setElementAt(vServiceEntry, index);
    }

    /**
     * Method setServiceEntry
     * 
     * 
     * 
     * @param serviceEntryArray
     */
    public void setServiceEntry(org.teaframework.schema.service.ServiceEntry[] serviceEntryArray) {
        _serviceEntryList.removeAllElements();
        for (int i = 0; i < serviceEntryArray.length; i++) {
            _serviceEntryList.addElement(serviceEntryArray[i]);
        }
    }

    /**
     * Method unmarshal
     * 
     * 
     * 
     * @param reader
     * @return ServiceConfig
     */
    public static org.teaframework.schema.service.ServiceConfig unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.teaframework.schema.service.ServiceConfig) Unmarshaller.unmarshal(org.teaframework.schema.service.ServiceConfig.class, reader);
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
