package com.google.api.adwords.v201003.cm;

/**
 * List of mobile targets.
 */
public class MobileTargetList extends com.google.api.adwords.v201003.cm.TargetList implements java.io.Serializable {

    private com.google.api.adwords.v201003.cm.MobileTarget[] targets;

    public MobileTargetList() {
    }

    public MobileTargetList(java.lang.Long campaignId, java.lang.String targetListType, com.google.api.adwords.v201003.cm.MobileTarget[] targets) {
        super(campaignId, targetListType);
        this.targets = targets;
    }

    /**
     * Gets the targets value for this MobileTargetList.
     * 
     * @return targets   * List of mobile targets. An empty list means all mobile devices/carriers
     * are targeted.
     *                     <span class="constraint ContentsDistinct">This
     * field must contain distinct elements.</span>
     *                     <span class="constraint ContentsNotNull">This
     * field must not contain {@code null} elements.</span>
     */
    public com.google.api.adwords.v201003.cm.MobileTarget[] getTargets() {
        return targets;
    }

    /**
     * Sets the targets value for this MobileTargetList.
     * 
     * @param targets   * List of mobile targets. An empty list means all mobile devices/carriers
     * are targeted.
     *                     <span class="constraint ContentsDistinct">This
     * field must contain distinct elements.</span>
     *                     <span class="constraint ContentsNotNull">This
     * field must not contain {@code null} elements.</span>
     */
    public void setTargets(com.google.api.adwords.v201003.cm.MobileTarget[] targets) {
        this.targets = targets;
    }

    public com.google.api.adwords.v201003.cm.MobileTarget getTargets(int i) {
        return this.targets[i];
    }

    public void setTargets(int i, com.google.api.adwords.v201003.cm.MobileTarget _value) {
        this.targets[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof MobileTargetList)) return false;
        MobileTargetList other = (MobileTargetList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.targets == null && other.getTargets() == null) || (this.targets != null && java.util.Arrays.equals(this.targets, other.getTargets())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getTargets() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getTargets()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTargets(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(MobileTargetList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "MobileTargetList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("targets");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "targets"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201003", "MobileTarget"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
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
