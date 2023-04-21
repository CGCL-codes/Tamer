package org.dbe.demos.amazon.ws;

public class ProductInfo implements java.io.Serializable {

    private java.lang.String totalResults;

    private java.lang.String totalPages;

    private java.lang.String listName;

    private org.dbe.demos.amazon.ws.Details[] details;

    public ProductInfo() {
    }

    public ProductInfo(java.lang.String totalResults, java.lang.String totalPages, java.lang.String listName, org.dbe.demos.amazon.ws.Details[] details) {
        this.totalResults = totalResults;
        this.totalPages = totalPages;
        this.listName = listName;
        this.details = details;
    }

    /**
     * Gets the totalResults value for this ProductInfo.
     * 
     * @return totalResults
     */
    public java.lang.String getTotalResults() {
        return totalResults;
    }

    /**
     * Sets the totalResults value for this ProductInfo.
     * 
     * @param totalResults
     */
    public void setTotalResults(java.lang.String totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * Gets the totalPages value for this ProductInfo.
     * 
     * @return totalPages
     */
    public java.lang.String getTotalPages() {
        return totalPages;
    }

    /**
     * Sets the totalPages value for this ProductInfo.
     * 
     * @param totalPages
     */
    public void setTotalPages(java.lang.String totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Gets the listName value for this ProductInfo.
     * 
     * @return listName
     */
    public java.lang.String getListName() {
        return listName;
    }

    /**
     * Sets the listName value for this ProductInfo.
     * 
     * @param listName
     */
    public void setListName(java.lang.String listName) {
        this.listName = listName;
    }

    /**
     * Gets the details value for this ProductInfo.
     * 
     * @return details
     */
    public org.dbe.demos.amazon.ws.Details[] getDetails() {
        return details;
    }

    /**
     * Sets the details value for this ProductInfo.
     * 
     * @param details
     */
    public void setDetails(org.dbe.demos.amazon.ws.Details[] details) {
        this.details = details;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ProductInfo)) return false;
        ProductInfo other = (ProductInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.totalResults == null && other.getTotalResults() == null) || (this.totalResults != null && this.totalResults.equals(other.getTotalResults()))) && ((this.totalPages == null && other.getTotalPages() == null) || (this.totalPages != null && this.totalPages.equals(other.getTotalPages()))) && ((this.listName == null && other.getListName() == null) || (this.listName != null && this.listName.equals(other.getListName()))) && ((this.details == null && other.getDetails() == null) || (this.details != null && java.util.Arrays.equals(this.details, other.getDetails())));
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
        if (getTotalResults() != null) {
            _hashCode += getTotalResults().hashCode();
        }
        if (getTotalPages() != null) {
            _hashCode += getTotalPages().hashCode();
        }
        if (getListName() != null) {
            _hashCode += getListName().hashCode();
        }
        if (getDetails() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getDetails()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getDetails(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ProductInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://soap.amazon.com", "ProductInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalResults");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TotalResults"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("totalPages");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TotalPages"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listName");
        elemField.setXmlName(new javax.xml.namespace.QName("", "ListName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("details");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Details"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://soap.amazon.com", "Details"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }
}
