package it.polimi.admin;

public class CreateSWSRequestType implements java.io.Serializable {

    private java.lang.String serviceID;

    public CreateSWSRequestType() {
    }

    public CreateSWSRequestType(java.lang.String serviceID) {
        this.serviceID = serviceID;
    }

    /**
     * Gets the serviceID value for this CreateSWSRequestType.
     * 
     * @return serviceID
     */
    public java.lang.String getServiceID() {
        return serviceID;
    }

    /**
     * Sets the serviceID value for this CreateSWSRequestType.
     * 
     * @param serviceID
     */
    public void setServiceID(java.lang.String serviceID) {
        this.serviceID = serviceID;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CreateSWSRequestType)) return false;
        CreateSWSRequestType other = (CreateSWSRequestType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.serviceID == null && other.getServiceID() == null) || (this.serviceID != null && this.serviceID.equals(other.getServiceID())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getServiceID() != null) {
            _hashCode += getServiceID().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }
}
