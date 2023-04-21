package com.google.api.adwords.v201109.cm;

/**
 * User list targeting as a collection of conversion types.
 *             
 *             {@link RemarketingUserList} can be created in the following
 * ways :
 *             <ul>
 *             <li> Using a single conversion type name: The service
 * will create a new {@link ConversionType}
 *             and associate it with the {@link RemarketingUserList}.
 * </li>
 *             <li> Using one or many existing conversion type Ids: The
 * service will associate the
 *             conversion types with the {@link RemarketingUserList}.
 * The Id of the {@link ConversionType} can be obtained using
 *             {@link ConversionTrackerService}.
 *             </li>
 *             </ul>
 */
public class RemarketingUserList extends com.google.api.adwords.v201109.cm.UserList implements java.io.Serializable {

    private com.google.api.adwords.v201109.cm.UserListConversionType[] conversionTypes;

    public RemarketingUserList() {
    }

    public RemarketingUserList(java.lang.Long id, java.lang.Boolean isReadOnly, java.lang.String name, java.lang.String description, com.google.api.adwords.v201109.cm.UserListMembershipStatus status, com.google.api.adwords.v201109.cm.AccessReason accessReason, com.google.api.adwords.v201109.cm.AccountUserListStatus accountUserListStatus, java.lang.Long membershipLifeSpan, java.lang.Long size, com.google.api.adwords.v201109.cm.SizeRange sizeRange, com.google.api.adwords.v201109.cm.UserListType type, java.lang.String userListType, com.google.api.adwords.v201109.cm.UserListConversionType[] conversionTypes) {
        super(id, isReadOnly, name, description, status, accessReason, accountUserListStatus, membershipLifeSpan, size, sizeRange, type, userListType);
        this.conversionTypes = conversionTypes;
    }

    /**
     * Gets the conversionTypes value for this RemarketingUserList.
     * 
     * @return conversionTypes   * Conversion types associated with this user list.
     *                     <span class="constraint Selectable">This field
     * can be selected using the value "ConversionTypes".</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null} when it is contained within
     * {@link Operator}s : ADD.</span>
     */
    public com.google.api.adwords.v201109.cm.UserListConversionType[] getConversionTypes() {
        return conversionTypes;
    }

    /**
     * Sets the conversionTypes value for this RemarketingUserList.
     * 
     * @param conversionTypes   * Conversion types associated with this user list.
     *                     <span class="constraint Selectable">This field
     * can be selected using the value "ConversionTypes".</span>
     *                     <span class="constraint Required">This field is
     * required and should not be {@code null} when it is contained within
     * {@link Operator}s : ADD.</span>
     */
    public void setConversionTypes(com.google.api.adwords.v201109.cm.UserListConversionType[] conversionTypes) {
        this.conversionTypes = conversionTypes;
    }

    public com.google.api.adwords.v201109.cm.UserListConversionType getConversionTypes(int i) {
        return this.conversionTypes[i];
    }

    public void setConversionTypes(int i, com.google.api.adwords.v201109.cm.UserListConversionType _value) {
        this.conversionTypes[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof RemarketingUserList)) return false;
        RemarketingUserList other = (RemarketingUserList) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && ((this.conversionTypes == null && other.getConversionTypes() == null) || (this.conversionTypes != null && java.util.Arrays.equals(this.conversionTypes, other.getConversionTypes())));
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
        if (getConversionTypes() != null) {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getConversionTypes()); i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getConversionTypes(), i);
                if (obj != null && !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(RemarketingUserList.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "RemarketingUserList"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conversionTypes");
        elemField.setXmlName(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "conversionTypes"));
        elemField.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/cm/v201109", "UserListConversionType"));
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
