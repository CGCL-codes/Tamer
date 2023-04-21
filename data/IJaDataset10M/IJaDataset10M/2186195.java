package org.example.www.UVManager;

public class UV implements java.io.Serializable {

    private int id;

    private java.lang.String name;

    private java.lang.String description;

    private int year;

    private java.lang.String inCharge;

    private org.example.www.UVManager.UVStatus status;

    private int CM;

    private int TP;

    private int TD;

    private int BE;

    private int controle;

    public UV() {
    }

    public UV(int id, java.lang.String name, java.lang.String description, int year, java.lang.String inCharge, org.example.www.UVManager.UVStatus status, int CM, int TP, int TD, int BE, int controle) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.year = year;
        this.inCharge = inCharge;
        this.status = status;
        this.CM = CM;
        this.TP = TP;
        this.TD = TD;
        this.BE = BE;
        this.controle = controle;
    }

    /**
     * Gets the id value for this UV.
     * 
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id value for this UV.
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name value for this UV.
     * 
     * @return name
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * Sets the name value for this UV.
     * 
     * @param name
     */
    public void setName(java.lang.String name) {
        this.name = name;
    }

    /**
     * Gets the description value for this UV.
     * 
     * @return description
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * Sets the description value for this UV.
     * 
     * @param description
     */
    public void setDescription(java.lang.String description) {
        this.description = description;
    }

    /**
     * Gets the year value for this UV.
     * 
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year value for this UV.
     * 
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Gets the inCharge value for this UV.
     * 
     * @return inCharge
     */
    public java.lang.String getInCharge() {
        return inCharge;
    }

    /**
     * Sets the inCharge value for this UV.
     * 
     * @param inCharge
     */
    public void setInCharge(java.lang.String inCharge) {
        this.inCharge = inCharge;
    }

    /**
     * Gets the status value for this UV.
     * 
     * @return status
     */
    public org.example.www.UVManager.UVStatus getStatus() {
        return status;
    }

    /**
     * Sets the status value for this UV.
     * 
     * @param status
     */
    public void setStatus(org.example.www.UVManager.UVStatus status) {
        this.status = status;
    }

    /**
     * Gets the CM value for this UV.
     * 
     * @return CM
     */
    public int getCM() {
        return CM;
    }

    /**
     * Sets the CM value for this UV.
     * 
     * @param CM
     */
    public void setCM(int CM) {
        this.CM = CM;
    }

    /**
     * Gets the TP value for this UV.
     * 
     * @return TP
     */
    public int getTP() {
        return TP;
    }

    /**
     * Sets the TP value for this UV.
     * 
     * @param TP
     */
    public void setTP(int TP) {
        this.TP = TP;
    }

    /**
     * Gets the TD value for this UV.
     * 
     * @return TD
     */
    public int getTD() {
        return TD;
    }

    /**
     * Sets the TD value for this UV.
     * 
     * @param TD
     */
    public void setTD(int TD) {
        this.TD = TD;
    }

    /**
     * Gets the BE value for this UV.
     * 
     * @return BE
     */
    public int getBE() {
        return BE;
    }

    /**
     * Sets the BE value for this UV.
     * 
     * @param BE
     */
    public void setBE(int BE) {
        this.BE = BE;
    }

    /**
     * Gets the controle value for this UV.
     * 
     * @return controle
     */
    public int getControle() {
        return controle;
    }

    /**
     * Sets the controle value for this UV.
     * 
     * @param controle
     */
    public void setControle(int controle) {
        this.controle = controle;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof UV)) return false;
        UV other = (UV) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && this.id == other.getId() && ((this.name == null && other.getName() == null) || (this.name != null && this.name.equals(other.getName()))) && ((this.description == null && other.getDescription() == null) || (this.description != null && this.description.equals(other.getDescription()))) && this.year == other.getYear() && ((this.inCharge == null && other.getInCharge() == null) || (this.inCharge != null && this.inCharge.equals(other.getInCharge()))) && ((this.status == null && other.getStatus() == null) || (this.status != null && this.status.equals(other.getStatus()))) && this.CM == other.getCM() && this.TP == other.getTP() && this.TD == other.getTD() && this.BE == other.getBE() && this.controle == other.getControle();
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
        _hashCode += getId();
        if (getName() != null) {
            _hashCode += getName().hashCode();
        }
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getYear();
        if (getInCharge() != null) {
            _hashCode += getInCharge().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        _hashCode += getCM();
        _hashCode += getTP();
        _hashCode += getTD();
        _hashCode += getBE();
        _hashCode += getControle();
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(UV.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.example.org/UVManager/", "UV"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("name");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Name"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("year");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Year"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("inCharge");
        elemField.setXmlName(new javax.xml.namespace.QName("", "InCharge"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Status"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.example.org/UVManager/", "UVStatus"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("CM");
        elemField.setXmlName(new javax.xml.namespace.QName("", "CM"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TP");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TP"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("TD");
        elemField.setXmlName(new javax.xml.namespace.QName("", "TD"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("BE");
        elemField.setXmlName(new javax.xml.namespace.QName("", "BE"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("controle");
        elemField.setXmlName(new javax.xml.namespace.QName("", "Controle"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
