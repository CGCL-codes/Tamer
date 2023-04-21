package org.openejb.alt.config.ejb11;

import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * 
 * @version $Revision: 1.3 $ $Date: 2002/12/28 19:20:32 $
**/
public class Session implements java.io.Serializable {

    private java.lang.String _id;

    private java.lang.String _description;

    private java.lang.String _displayName;

    private java.lang.String _smallIcon;

    private java.lang.String _largeIcon;

    private java.lang.String _ejbName;

    private java.lang.String _home;

    private java.lang.String _remote;

    private java.lang.String _ejbClass;

    private java.lang.String _sessionType;

    private java.lang.String _transactionType;

    private java.util.Vector _envEntryList;

    private java.util.Vector _ejbRefList;

    private java.util.Vector _securityRoleRefList;

    private java.util.Vector _resourceRefList;

    public Session() {
        super();
        _envEntryList = new Vector();
        _ejbRefList = new Vector();
        _securityRoleRefList = new Vector();
        _resourceRefList = new Vector();
    }

    /**
     * 
     * @param vEjbRef
    **/
    public void addEjbRef(EjbRef vEjbRef) throws java.lang.IndexOutOfBoundsException {
        _ejbRefList.addElement(vEjbRef);
    }

    /**
     * 
     * @param vEnvEntry
    **/
    public void addEnvEntry(EnvEntry vEnvEntry) throws java.lang.IndexOutOfBoundsException {
        _envEntryList.addElement(vEnvEntry);
    }

    /**
     * 
     * @param vResourceRef
    **/
    public void addResourceRef(ResourceRef vResourceRef) throws java.lang.IndexOutOfBoundsException {
        _resourceRefList.addElement(vResourceRef);
    }

    /**
     * 
     * @param vSecurityRoleRef
    **/
    public void addSecurityRoleRef(SecurityRoleRef vSecurityRoleRef) throws java.lang.IndexOutOfBoundsException {
        _securityRoleRefList.addElement(vSecurityRoleRef);
    }

    /**
    **/
    public java.util.Enumeration enumerateEjbRef() {
        return _ejbRefList.elements();
    }

    /**
    **/
    public java.util.Enumeration enumerateEnvEntry() {
        return _envEntryList.elements();
    }

    /**
    **/
    public java.util.Enumeration enumerateResourceRef() {
        return _resourceRefList.elements();
    }

    /**
    **/
    public java.util.Enumeration enumerateSecurityRoleRef() {
        return _securityRoleRefList.elements();
    }

    /**
    **/
    public java.lang.String getDescription() {
        return this._description;
    }

    /**
    **/
    public java.lang.String getDisplayName() {
        return this._displayName;
    }

    /**
    **/
    public java.lang.String getEjbClass() {
        return this._ejbClass;
    }

    /**
    **/
    public java.lang.String getEjbName() {
        return this._ejbName;
    }

    /**
     * 
     * @param index
    **/
    public EjbRef getEjbRef(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _ejbRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (EjbRef) _ejbRefList.elementAt(index);
    }

    /**
    **/
    public EjbRef[] getEjbRef() {
        int size = _ejbRefList.size();
        EjbRef[] mArray = new EjbRef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (EjbRef) _ejbRefList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getEjbRefCount() {
        return _ejbRefList.size();
    }

    /**
     * 
     * @param index
    **/
    public EnvEntry getEnvEntry(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _envEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (EnvEntry) _envEntryList.elementAt(index);
    }

    /**
    **/
    public EnvEntry[] getEnvEntry() {
        int size = _envEntryList.size();
        EnvEntry[] mArray = new EnvEntry[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (EnvEntry) _envEntryList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getEnvEntryCount() {
        return _envEntryList.size();
    }

    /**
    **/
    public java.lang.String getHome() {
        return this._home;
    }

    /**
    **/
    public java.lang.String getId() {
        return this._id;
    }

    /**
    **/
    public java.lang.String getLargeIcon() {
        return this._largeIcon;
    }

    /**
    **/
    public java.lang.String getRemote() {
        return this._remote;
    }

    /**
     * 
     * @param index
    **/
    public ResourceRef getResourceRef(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _resourceRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (ResourceRef) _resourceRefList.elementAt(index);
    }

    /**
    **/
    public ResourceRef[] getResourceRef() {
        int size = _resourceRefList.size();
        ResourceRef[] mArray = new ResourceRef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (ResourceRef) _resourceRefList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getResourceRefCount() {
        return _resourceRefList.size();
    }

    /**
     * 
     * @param index
    **/
    public SecurityRoleRef getSecurityRoleRef(int index) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _securityRoleRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        return (SecurityRoleRef) _securityRoleRefList.elementAt(index);
    }

    /**
    **/
    public SecurityRoleRef[] getSecurityRoleRef() {
        int size = _securityRoleRefList.size();
        SecurityRoleRef[] mArray = new SecurityRoleRef[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (SecurityRoleRef) _securityRoleRefList.elementAt(index);
        }
        return mArray;
    }

    /**
    **/
    public int getSecurityRoleRefCount() {
        return _securityRoleRefList.size();
    }

    /**
    **/
    public java.lang.String getSessionType() {
        return this._sessionType;
    }

    /**
    **/
    public java.lang.String getSmallIcon() {
        return this._smallIcon;
    }

    /**
    **/
    public java.lang.String getTransactionType() {
        return this._transactionType;
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
    public void removeAllEjbRef() {
        _ejbRefList.removeAllElements();
    }

    /**
    **/
    public void removeAllEnvEntry() {
        _envEntryList.removeAllElements();
    }

    /**
    **/
    public void removeAllResourceRef() {
        _resourceRefList.removeAllElements();
    }

    /**
    **/
    public void removeAllSecurityRoleRef() {
        _securityRoleRefList.removeAllElements();
    }

    /**
     * 
     * @param index
    **/
    public EjbRef removeEjbRef(int index) {
        Object obj = _ejbRefList.elementAt(index);
        _ejbRefList.removeElementAt(index);
        return (EjbRef) obj;
    }

    /**
     * 
     * @param index
    **/
    public EnvEntry removeEnvEntry(int index) {
        Object obj = _envEntryList.elementAt(index);
        _envEntryList.removeElementAt(index);
        return (EnvEntry) obj;
    }

    /**
     * 
     * @param index
    **/
    public ResourceRef removeResourceRef(int index) {
        Object obj = _resourceRefList.elementAt(index);
        _resourceRefList.removeElementAt(index);
        return (ResourceRef) obj;
    }

    /**
     * 
     * @param index
    **/
    public SecurityRoleRef removeSecurityRoleRef(int index) {
        Object obj = _securityRoleRefList.elementAt(index);
        _securityRoleRefList.removeElementAt(index);
        return (SecurityRoleRef) obj;
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
     * @param _displayName
    **/
    public void setDisplayName(java.lang.String _displayName) {
        this._displayName = _displayName;
    }

    /**
     * 
     * @param _ejbClass
    **/
    public void setEjbClass(java.lang.String _ejbClass) {
        this._ejbClass = _ejbClass;
    }

    /**
     * 
     * @param _ejbName
    **/
    public void setEjbName(java.lang.String _ejbName) {
        this._ejbName = _ejbName;
    }

    /**
     * 
     * @param index
     * @param vEjbRef
    **/
    public void setEjbRef(int index, EjbRef vEjbRef) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _ejbRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _ejbRefList.setElementAt(vEjbRef, index);
    }

    /**
     * 
     * @param ejbRefArray
    **/
    public void setEjbRef(EjbRef[] ejbRefArray) {
        _ejbRefList.removeAllElements();
        for (int i = 0; i < ejbRefArray.length; i++) {
            _ejbRefList.addElement(ejbRefArray[i]);
        }
    }

    /**
     * 
     * @param index
     * @param vEnvEntry
    **/
    public void setEnvEntry(int index, EnvEntry vEnvEntry) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _envEntryList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _envEntryList.setElementAt(vEnvEntry, index);
    }

    /**
     * 
     * @param envEntryArray
    **/
    public void setEnvEntry(EnvEntry[] envEntryArray) {
        _envEntryList.removeAllElements();
        for (int i = 0; i < envEntryArray.length; i++) {
            _envEntryList.addElement(envEntryArray[i]);
        }
    }

    /**
     * 
     * @param _home
    **/
    public void setHome(java.lang.String _home) {
        this._home = _home;
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
     * @param _largeIcon
    **/
    public void setLargeIcon(java.lang.String _largeIcon) {
        this._largeIcon = _largeIcon;
    }

    /**
     * 
     * @param _remote
    **/
    public void setRemote(java.lang.String _remote) {
        this._remote = _remote;
    }

    /**
     * 
     * @param index
     * @param vResourceRef
    **/
    public void setResourceRef(int index, ResourceRef vResourceRef) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _resourceRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resourceRefList.setElementAt(vResourceRef, index);
    }

    /**
     * 
     * @param resourceRefArray
    **/
    public void setResourceRef(ResourceRef[] resourceRefArray) {
        _resourceRefList.removeAllElements();
        for (int i = 0; i < resourceRefArray.length; i++) {
            _resourceRefList.addElement(resourceRefArray[i]);
        }
    }

    /**
     * 
     * @param index
     * @param vSecurityRoleRef
    **/
    public void setSecurityRoleRef(int index, SecurityRoleRef vSecurityRoleRef) throws java.lang.IndexOutOfBoundsException {
        if ((index < 0) || (index > _securityRoleRefList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _securityRoleRefList.setElementAt(vSecurityRoleRef, index);
    }

    /**
     * 
     * @param securityRoleRefArray
    **/
    public void setSecurityRoleRef(SecurityRoleRef[] securityRoleRefArray) {
        _securityRoleRefList.removeAllElements();
        for (int i = 0; i < securityRoleRefArray.length; i++) {
            _securityRoleRefList.addElement(securityRoleRefArray[i]);
        }
    }

    /**
     * 
     * @param _sessionType
    **/
    public void setSessionType(java.lang.String _sessionType) {
        this._sessionType = _sessionType;
    }

    /**
     * 
     * @param _smallIcon
    **/
    public void setSmallIcon(java.lang.String _smallIcon) {
        this._smallIcon = _smallIcon;
    }

    /**
     * 
     * @param _transactionType
    **/
    public void setTransactionType(java.lang.String _transactionType) {
        this._transactionType = _transactionType;
    }

    /**
     * 
     * @param reader
    **/
    public static org.openejb.alt.config.ejb11.Session unmarshal(java.io.Reader reader) throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException {
        return (org.openejb.alt.config.ejb11.Session) Unmarshaller.unmarshal(org.openejb.alt.config.ejb11.Session.class, reader);
    }

    /**
    **/
    public void validate() throws org.exolab.castor.xml.ValidationException {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    }
}
