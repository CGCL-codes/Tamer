package com.google.api.ads.dfp.v201108;

public class LineItemErrorReason implements java.io.Serializable {

    private java.lang.String _value_;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    protected LineItemErrorReason(java.lang.String value) {
        _value_ = value;
        _table_.put(_value_, this);
    }

    public static final java.lang.String _ALREADY_STARTED = "ALREADY_STARTED";

    public static final java.lang.String _UPDATE_RESERVATION_NOT_ALLOWED = "UPDATE_RESERVATION_NOT_ALLOWED";

    public static final java.lang.String _ALL_ROADBLOCK_NOT_ALLOWED = "ALL_ROADBLOCK_NOT_ALLOWED";

    public static final java.lang.String _FRACTIONAL_PERCENTAGE_NOT_ALLOWED = "FRACTIONAL_PERCENTAGE_NOT_ALLOWED";

    public static final java.lang.String _DISCOUNT_NOT_ALLOWED = "DISCOUNT_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_CANCELED_LINE_ITEM_NOT_ALLOWED = "UPDATE_CANCELED_LINE_ITEM_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_PENDING_APPROVAL_LINE_ITEM_NOT_ALLOWED = "UPDATE_PENDING_APPROVAL_LINE_ITEM_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_ARCHIVED_LINE_ITEM_NOT_ALLOWED = "UPDATE_ARCHIVED_LINE_ITEM_NOT_ALLOWED";

    public static final java.lang.String _FRONTLOADED_NOT_ALLOWED = "FRONTLOADED_NOT_ALLOWED";

    public static final java.lang.String _CREATE_OR_UPDATE_LEGACY_DFP_LINE_ITEM_TYPE_NOT_ALLOWED = "CREATE_OR_UPDATE_LEGACY_DFP_LINE_ITEM_TYPE_NOT_ALLOWED";

    public static final java.lang.String _COPY_LINE_ITEM_FROM_DIFFERENT_COMPANY_NOT_ALLOWED = "COPY_LINE_ITEM_FROM_DIFFERENT_COMPANY_NOT_ALLOWED";

    public static final java.lang.String _INVALID_SIZE_FOR_PLATFORM = "INVALID_SIZE_FOR_PLATFORM";

    public static final java.lang.String _INVALID_LINE_ITEM_TYPE_FOR_PLATFORM = "INVALID_LINE_ITEM_TYPE_FOR_PLATFORM";

    public static final java.lang.String _INVALID_WEB_PROPERTY_FOR_PLATFORM = "INVALID_WEB_PROPERTY_FOR_PLATFORM";

    public static final java.lang.String _AFMA_BACKFILL_NOT_ALLOWED = "AFMA_BACKFILL_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_ENVIRONMENT_TYPE_NOT_ALLOWED = "UPDATE_ENVIRONMENT_TYPE_NOT_ALLOWED";

    public static final java.lang.String _COMPANIONS_NOT_ALLOWED = "COMPANIONS_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_FROM_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED = "UPDATE_FROM_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_TO_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED = "UPDATE_TO_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED";

    public static final java.lang.String _UPDATE_BACKFILL_WEB_PROPERTY_NOT_ALLOWED = "UPDATE_BACKFILL_WEB_PROPERTY_NOT_ALLOWED";

    public static final java.lang.String _INVALID_COMPANION_DELIVERY_OPTION_FOR_ENVIRONMENT_TYPE = "INVALID_COMPANION_DELIVERY_OPTION_FOR_ENVIRONMENT_TYPE";

    public static final java.lang.String _COMPANION_DELIVERY_OPTION_REQUIRE_PREMIUM = "COMPANION_DELIVERY_OPTION_REQUIRE_PREMIUM";

    public static final java.lang.String _DUPLICATE_MASTER_SIZES = "DUPLICATE_MASTER_SIZES";

    public static final java.lang.String _INVALID_ENVIRONMENT_TYPE = "INVALID_ENVIRONMENT_TYPE";

    public static final java.lang.String _INVALID_ENVIRONMENT_TYPE_FOR_PLATFORM = "INVALID_ENVIRONMENT_TYPE_FOR_PLATFORM";

    public static final java.lang.String _INVALID_TYPE_FOR_CONTRACTED_UNITS_BOUGHT = "INVALID_TYPE_FOR_CONTRACTED_UNITS_BOUGHT";

    public static final java.lang.String _VIDEO_INVALID_ROADBLOCKING = "VIDEO_INVALID_ROADBLOCKING";

    public static final java.lang.String _BACKFILL_TYPE_NOT_ALLOWED = "BACKFILL_TYPE_NOT_ALLOWED";

    public static final java.lang.String _INVALID_SIZE_FOR_ENVIRONMENT = "INVALID_SIZE_FOR_ENVIRONMENT";

    public static final LineItemErrorReason ALREADY_STARTED = new LineItemErrorReason(_ALREADY_STARTED);

    public static final LineItemErrorReason UPDATE_RESERVATION_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_RESERVATION_NOT_ALLOWED);

    public static final LineItemErrorReason ALL_ROADBLOCK_NOT_ALLOWED = new LineItemErrorReason(_ALL_ROADBLOCK_NOT_ALLOWED);

    public static final LineItemErrorReason FRACTIONAL_PERCENTAGE_NOT_ALLOWED = new LineItemErrorReason(_FRACTIONAL_PERCENTAGE_NOT_ALLOWED);

    public static final LineItemErrorReason DISCOUNT_NOT_ALLOWED = new LineItemErrorReason(_DISCOUNT_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_CANCELED_LINE_ITEM_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_CANCELED_LINE_ITEM_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_PENDING_APPROVAL_LINE_ITEM_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_PENDING_APPROVAL_LINE_ITEM_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_ARCHIVED_LINE_ITEM_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_ARCHIVED_LINE_ITEM_NOT_ALLOWED);

    public static final LineItemErrorReason FRONTLOADED_NOT_ALLOWED = new LineItemErrorReason(_FRONTLOADED_NOT_ALLOWED);

    public static final LineItemErrorReason CREATE_OR_UPDATE_LEGACY_DFP_LINE_ITEM_TYPE_NOT_ALLOWED = new LineItemErrorReason(_CREATE_OR_UPDATE_LEGACY_DFP_LINE_ITEM_TYPE_NOT_ALLOWED);

    public static final LineItemErrorReason COPY_LINE_ITEM_FROM_DIFFERENT_COMPANY_NOT_ALLOWED = new LineItemErrorReason(_COPY_LINE_ITEM_FROM_DIFFERENT_COMPANY_NOT_ALLOWED);

    public static final LineItemErrorReason INVALID_SIZE_FOR_PLATFORM = new LineItemErrorReason(_INVALID_SIZE_FOR_PLATFORM);

    public static final LineItemErrorReason INVALID_LINE_ITEM_TYPE_FOR_PLATFORM = new LineItemErrorReason(_INVALID_LINE_ITEM_TYPE_FOR_PLATFORM);

    public static final LineItemErrorReason INVALID_WEB_PROPERTY_FOR_PLATFORM = new LineItemErrorReason(_INVALID_WEB_PROPERTY_FOR_PLATFORM);

    public static final LineItemErrorReason AFMA_BACKFILL_NOT_ALLOWED = new LineItemErrorReason(_AFMA_BACKFILL_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_ENVIRONMENT_TYPE_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_ENVIRONMENT_TYPE_NOT_ALLOWED);

    public static final LineItemErrorReason COMPANIONS_NOT_ALLOWED = new LineItemErrorReason(_COMPANIONS_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_FROM_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_FROM_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_TO_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_TO_BACKFILL_LINE_ITEM_TYPE_NOT_ALLOWED);

    public static final LineItemErrorReason UPDATE_BACKFILL_WEB_PROPERTY_NOT_ALLOWED = new LineItemErrorReason(_UPDATE_BACKFILL_WEB_PROPERTY_NOT_ALLOWED);

    public static final LineItemErrorReason INVALID_COMPANION_DELIVERY_OPTION_FOR_ENVIRONMENT_TYPE = new LineItemErrorReason(_INVALID_COMPANION_DELIVERY_OPTION_FOR_ENVIRONMENT_TYPE);

    public static final LineItemErrorReason COMPANION_DELIVERY_OPTION_REQUIRE_PREMIUM = new LineItemErrorReason(_COMPANION_DELIVERY_OPTION_REQUIRE_PREMIUM);

    public static final LineItemErrorReason DUPLICATE_MASTER_SIZES = new LineItemErrorReason(_DUPLICATE_MASTER_SIZES);

    public static final LineItemErrorReason INVALID_ENVIRONMENT_TYPE = new LineItemErrorReason(_INVALID_ENVIRONMENT_TYPE);

    public static final LineItemErrorReason INVALID_ENVIRONMENT_TYPE_FOR_PLATFORM = new LineItemErrorReason(_INVALID_ENVIRONMENT_TYPE_FOR_PLATFORM);

    public static final LineItemErrorReason INVALID_TYPE_FOR_CONTRACTED_UNITS_BOUGHT = new LineItemErrorReason(_INVALID_TYPE_FOR_CONTRACTED_UNITS_BOUGHT);

    public static final LineItemErrorReason VIDEO_INVALID_ROADBLOCKING = new LineItemErrorReason(_VIDEO_INVALID_ROADBLOCKING);

    public static final LineItemErrorReason BACKFILL_TYPE_NOT_ALLOWED = new LineItemErrorReason(_BACKFILL_TYPE_NOT_ALLOWED);

    public static final LineItemErrorReason INVALID_SIZE_FOR_ENVIRONMENT = new LineItemErrorReason(_INVALID_SIZE_FOR_ENVIRONMENT);

    public java.lang.String getValue() {
        return _value_;
    }

    public static LineItemErrorReason fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
        LineItemErrorReason enumeration = (LineItemErrorReason) _table_.get(value);
        if (enumeration == null) throw new java.lang.IllegalArgumentException();
        return enumeration;
    }

    public static LineItemErrorReason fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
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

    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(LineItemErrorReason.class);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("https://www.google.com/apis/ads/publisher/v201108", "LineItemError.Reason"));
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }
}
