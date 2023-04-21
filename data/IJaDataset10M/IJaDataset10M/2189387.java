package com.strikeiron.ws.taxdatabasic;

public class TaxRateCanadaData implements java.io.Serializable {

    private java.lang.String abbreviation;

    private java.lang.String province;

    private double GST;

    private double PST;

    private double total;

    private java.lang.String HST;

    public TaxRateCanadaData() {
    }

    public TaxRateCanadaData(java.lang.String abbreviation, java.lang.String province, double GST, double PST, double total, java.lang.String HST) {
        this.abbreviation = abbreviation;
        this.province = province;
        this.GST = GST;
        this.PST = PST;
        this.total = total;
        this.HST = HST;
    }

    /**
     * Gets the abbreviation value for this TaxRateCanadaData.
     * 
     * @return abbreviation
     */
    public java.lang.String getAbbreviation() {
        return abbreviation;
    }

    /**
     * Sets the abbreviation value for this TaxRateCanadaData.
     * 
     * @param abbreviation
     */
    public void setAbbreviation(java.lang.String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Gets the province value for this TaxRateCanadaData.
     * 
     * @return province
     */
    public java.lang.String getProvince() {
        return province;
    }

    /**
     * Sets the province value for this TaxRateCanadaData.
     * 
     * @param province
     */
    public void setProvince(java.lang.String province) {
        this.province = province;
    }

    /**
     * Gets the GST value for this TaxRateCanadaData.
     * 
     * @return GST
     */
    public double getGST() {
        return GST;
    }

    /**
     * Sets the GST value for this TaxRateCanadaData.
     * 
     * @param GST
     */
    public void setGST(double GST) {
        this.GST = GST;
    }

    /**
     * Gets the PST value for this TaxRateCanadaData.
     * 
     * @return PST
     */
    public double getPST() {
        return PST;
    }

    /**
     * Sets the PST value for this TaxRateCanadaData.
     * 
     * @param PST
     */
    public void setPST(double PST) {
        this.PST = PST;
    }

    /**
     * Gets the total value for this TaxRateCanadaData.
     * 
     * @return total
     */
    public double getTotal() {
        return total;
    }

    /**
     * Sets the total value for this TaxRateCanadaData.
     * 
     * @param total
     */
    public void setTotal(double total) {
        this.total = total;
    }

    /**
     * Gets the HST value for this TaxRateCanadaData.
     * 
     * @return HST
     */
    public java.lang.String getHST() {
        return HST;
    }

    /**
     * Sets the HST value for this TaxRateCanadaData.
     * 
     * @param HST
     */
    public void setHST(java.lang.String HST) {
        this.HST = HST;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TaxRateCanadaData)) return false;
        TaxRateCanadaData other = (TaxRateCanadaData) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && ((this.abbreviation == null && other.getAbbreviation() == null) || (this.abbreviation != null && this.abbreviation.equals(other.getAbbreviation()))) && ((this.province == null && other.getProvince() == null) || (this.province != null && this.province.equals(other.getProvince()))) && this.GST == other.getGST() && this.PST == other.getPST() && this.total == other.getTotal() && ((this.HST == null && other.getHST() == null) || (this.HST != null && this.HST.equals(other.getHST())));
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
        if (getAbbreviation() != null) {
            _hashCode += getAbbreviation().hashCode();
        }
        if (getProvince() != null) {
            _hashCode += getProvince().hashCode();
        }
        _hashCode += new Double(getGST()).hashCode();
        _hashCode += new Double(getPST()).hashCode();
        _hashCode += new Double(getTotal()).hashCode();
        if (getHST() != null) {
            _hashCode += getHST().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(TaxRateCanadaData.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.strikeiron.com", "TaxRateCanadaData"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abbreviation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.strikeiron.com", "abbreviation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("province");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.strikeiron.com", "province"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("GST");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.strikeiron.com", "GST"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("PST");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.strikeiron.com", "PST"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("total");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.strikeiron.com", "total"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "double"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("HST");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.strikeiron.com", "HST"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
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
