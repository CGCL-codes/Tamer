package it.polimi.admin;

public class ConfigureSensorResponseType implements java.io.Serializable {

    private boolean response;

    public ConfigureSensorResponseType() {
    }

    public ConfigureSensorResponseType(boolean response) {
        this.response = response;
    }

    /**
     * Gets the response value for this ConfigureSensorResponseType.
     * 
     * @return response
     */
    public boolean isResponse() {
        return response;
    }

    /**
     * Sets the response value for this ConfigureSensorResponseType.
     * 
     * @param response
     */
    public void setResponse(boolean response) {
        this.response = response;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ConfigureSensorResponseType)) return false;
        ConfigureSensorResponseType other = (ConfigureSensorResponseType) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.response == other.isResponse();
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
        _hashCode += (isResponse() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }
}
