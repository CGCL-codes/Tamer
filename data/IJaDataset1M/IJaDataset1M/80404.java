package uk.ac.ncl.neresc.dynasoar.messages;

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AddVMServiceRequest.
 * 
 * @version $Revision$ $Date$
 */
public class AddVMServiceRequest implements java.io.Serializable {

    /**
     * Field _vmName
     */
    private java.lang.String _vmName;

    /**
     * Field _vmPort
     */
    private java.lang.String _vmPort;

    /**
     * Field _fileAccessURIList
     */
    private java.util.Vector _fileAccessURIList;

    /**
     * Field _VMWrappedServicesListList
     */
    private java.util.Vector _VMWrappedServicesListList;

    public AddVMServiceRequest() {
        super();
        _fileAccessURIList = new Vector();
        _VMWrappedServicesListList = new Vector();
    }

    /**
     * Method addFileAccessURI
     * 
     * 
     * 
     * @param vFileAccessURI
     */
    public void addFileAccessURI(java.lang.String vFileAccessURI) throws java.lang.IndexOutOfBoundsException {
        _fileAccessURIList.addElement(vFileAccessURI);
    }

    /**
     * Method addFileAccessURI
     * 
     * 
     * 
     * @param index
     * @param vFileAccessURI
     */
    public void addFileAccessURI(int index, java.lang.String vFileAccessURI) throws java.lang.IndexOutOfBoundsException {
        _fileAccessURIList.insertElementAt(vFileAccessURI, index);
    }

    /**
     * Method addVMWrappedServicesList
     * 
     * 
     * 
     * @param vVMWrappedServicesList
     */
    public void addVMWrappedServicesList(uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList vVMWrappedServicesList) throws java.lang.IndexOutOfBoundsException {
        _VMWrappedServicesListList.addElement(vVMWrappedServicesList);
    }

    /**
     * Method addVMWrappedServicesList
     * 
     * 
     * 
     * @param index
     * @param vVMWrappedServicesList
     */
    public void addVMWrappedServicesList(int index, uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList vVMWrappedServicesList) throws java.lang.IndexOutOfBoundsException {
        _VMWrappedServicesListList.insertElementAt(vVMWrappedServicesList, index);
    }

    /**
     * Method enumerateFileAccessURI
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateFileAccessURI() {
        return _fileAccessURIList.elements();
    }

    /**
     * Method enumerateVMWrappedServicesList
     * 
     * 
     * 
     * @return Enumeration
     */
    public java.util.Enumeration enumerateVMWrappedServicesList() {
        return _VMWrappedServicesListList.elements();
    }

    /**
     * Method getFileAccessURI
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String getFileAccessURI(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _fileAccessURIList.size())) {
            throw new IndexOutOfBoundsException("getFileAccessURI: Index value '" + index + "' not in range [0.." + _fileAccessURIList.size() + "]");
        }
        return (String) _fileAccessURIList.elementAt(index);
    }

    /**
     * Method getFileAccessURI
     * 
     * 
     * 
     * @return String
     */
    public java.lang.String[] getFileAccessURI() {
        int size = _fileAccessURIList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String) _fileAccessURIList.elementAt(index);
        }
        return mArray;
    }

    /**
     * Method getFileAccessURICount
     * 
     * 
     * 
     * @return int
     */
    public int getFileAccessURICount() {
        return _fileAccessURIList.size();
    }

    /**
     * Method getVMWrappedServicesList
     * 
     * 
     * 
     * @param index
     * @return VMWrappedServicesList
     */
    public uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList getVMWrappedServicesList(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _VMWrappedServicesListList.size())) {
            throw new IndexOutOfBoundsException("getVMWrappedServicesList: Index value '" + index + "' not in range [0.." + _VMWrappedServicesListList.size() + "]");
        }
        return (uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList) _VMWrappedServicesListList.elementAt(index);
    }

    /**
     * Method getVMWrappedServicesList
     * 
     * 
     * 
     * @return VMWrappedServicesList
     */
    public uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList[] getVMWrappedServicesList() {
        int size = _VMWrappedServicesListList.size();
        uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList[] mArray = new uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList) _VMWrappedServicesListList.elementAt(index);
        }
        return mArray;
    }

    /**
     * Method getVMWrappedServicesListCount
     * 
     * 
     * 
     * @return int
     */
    public int getVMWrappedServicesListCount() {
        return _VMWrappedServicesListList.size();
    }

    /**
     * Returns the value of field 'vmName'.
     * 
     * @return String
     * @return the value of field 'vmName'.
     */
    public java.lang.String getVmName() {
        return this._vmName;
    }

    /**
     * Returns the value of field 'vmPort'.
     * 
     * @return String
     * @return the value of field 'vmPort'.
     */
    public java.lang.String getVmPort() {
        return this._vmPort;
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
     * Method removeAllFileAccessURI
     * 
     */
    public void removeAllFileAccessURI() {
        _fileAccessURIList.removeAllElements();
    }

    /**
     * Method removeAllVMWrappedServicesList
     * 
     */
    public void removeAllVMWrappedServicesList() {
        _VMWrappedServicesListList.removeAllElements();
    }

    /**
     * Method removeFileAccessURI
     * 
     * 
     * 
     * @param index
     * @return String
     */
    public java.lang.String removeFileAccessURI(int index) {
        java.lang.Object obj = _fileAccessURIList.elementAt(index);
        _fileAccessURIList.removeElementAt(index);
        return (String) obj;
    }

    /**
     * Method removeVMWrappedServicesList
     * 
     * 
     * 
     * @param index
     * @return VMWrappedServicesList
     */
    public uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList removeVMWrappedServicesList(int index) {
        java.lang.Object obj = _VMWrappedServicesListList.elementAt(index);
        _VMWrappedServicesListList.removeElementAt(index);
        return (uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList) obj;
    }

    /**
     * Method setFileAccessURI
     * 
     * 
     * 
     * @param index
     * @param vFileAccessURI
     */
    public void setFileAccessURI(int index, java.lang.String vFileAccessURI) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _fileAccessURIList.size())) {
            throw new IndexOutOfBoundsException("setFileAccessURI: Index value '" + index + "' not in range [0.." + _fileAccessURIList.size() + "]");
        }
        _fileAccessURIList.setElementAt(vFileAccessURI, index);
    }

    /**
     * Method setFileAccessURI
     * 
     * 
     * 
     * @param fileAccessURIArray
     */
    public void setFileAccessURI(java.lang.String[] fileAccessURIArray) {
        _fileAccessURIList.removeAllElements();
        for (int i = 0; i < fileAccessURIArray.length; i++) {
            _fileAccessURIList.addElement(fileAccessURIArray[i]);
        }
    }

    /**
     * Method setVMWrappedServicesList
     * 
     * 
     * 
     * @param index
     * @param vVMWrappedServicesList
     */
    public void setVMWrappedServicesList(int index, uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList vVMWrappedServicesList) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _VMWrappedServicesListList.size())) {
            throw new IndexOutOfBoundsException("setVMWrappedServicesList: Index value '" + index + "' not in range [0.." + _VMWrappedServicesListList.size() + "]");
        }
        _VMWrappedServicesListList.setElementAt(vVMWrappedServicesList, index);
    }

    /**
     * Method setVMWrappedServicesList
     * 
     * 
     * 
     * @param VMWrappedServicesListArray
     */
    public void setVMWrappedServicesList(uk.ac.ncl.neresc.dynasoar.messages.VMWrappedServicesList[] VMWrappedServicesListArray) {
        _VMWrappedServicesListList.removeAllElements();
        for (int i = 0; i < VMWrappedServicesListArray.length; i++) {
            _VMWrappedServicesListList.addElement(VMWrappedServicesListArray[i]);
        }
    }

    /**
     * Sets the value of field 'vmName'.
     * 
     * @param vmName the value of field 'vmName'.
     */
    public void setVmName(java.lang.String vmName) {
        this._vmName = vmName;
    }

    /**
     * Sets the value of field 'vmPort'.
     * 
     * @param vmPort the value of field 'vmPort'.
     */
    public void setVmPort(java.lang.String vmPort) {
        this._vmPort = vmPort;
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
        return (uk.ac.ncl.neresc.dynasoar.messages.AddVMServiceRequest) Unmarshaller.unmarshal(uk.ac.ncl.neresc.dynasoar.messages.AddVMServiceRequest.class, reader);
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
