package com.google.api.adwords.v201109.o;

public class OpportunityIdeaType implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected OpportunityIdeaType(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _KEYWORD = "KEYWORD";

    public static final java.lang.String _BID = "BID";

    public static final java.lang.String _BUDGET = "BUDGET";

    public static final OpportunityIdeaType KEYWORD = new OpportunityIdeaType(_KEYWORD);

    public static final OpportunityIdeaType BID = new OpportunityIdeaType(_BID);

    public static final OpportunityIdeaType BUDGET = new OpportunityIdeaType(_BUDGET);

    public java.lang.String getValue() {
        return _value_;
    }

    public static OpportunityIdeaType fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        OpportunityIdeaType enumeration = (OpportunityIdeaType) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static OpportunityIdeaType fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
        return fromValue(value);
    }

    public boolean equals(java.lang.Object obj) {
        return (obj == this);
    }

    public int hashCode() {
        return toString().hashCode();
    }

    public java.lang.String toString() {
        return _value_;
    }

    public java.lang.Object readResolve() throws java.io.ObjectStreamException {
        return fromValue(_value_);
    }

    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.EnumSerializer(_javaType, _xmlType);
    }

    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
        return new org.apache.axis.encoding.ser.EnumDeserializer(_javaType, _xmlType);
    }

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(OpportunityIdeaType.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://adwords.google.com/api/adwords/o/v201109", "OpportunityIdeaType"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}
