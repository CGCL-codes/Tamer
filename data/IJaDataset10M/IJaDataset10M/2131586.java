/**
 * MarketSessionType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.sf.jspread.marketdata.inetats.webservice;

public class MarketSessionType implements java.io.Serializable {
    private org.apache.axis.types.NormalizedString _value_;
    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor
    protected MarketSessionType(org.apache.axis.types.NormalizedString value) {
        _value_ = value;
        _table_.put(_value_,this);
    }

    public static final org.apache.axis.types.NormalizedString _allday = new org.apache.axis.types.NormalizedString("allday");
    public static final org.apache.axis.types.NormalizedString _before = new org.apache.axis.types.NormalizedString("before");
    public static final org.apache.axis.types.NormalizedString _during = new org.apache.axis.types.NormalizedString("during");
    public static final org.apache.axis.types.NormalizedString _after = new org.apache.axis.types.NormalizedString("after");
    public static final MarketSessionType allday = new MarketSessionType(_allday);
    public static final MarketSessionType before = new MarketSessionType(_before);
    public static final MarketSessionType during = new MarketSessionType(_during);
    public static final MarketSessionType after = new MarketSessionType(_after);
    public org.apache.axis.types.NormalizedString getValue() { return _value_;}
    public static MarketSessionType fromValue(org.apache.axis.types.NormalizedString value)
          throws java.lang.IllegalStateException {
        MarketSessionType enum = (MarketSessionType)
            _table_.get(value);
        if (enum==null) throw new java.lang.IllegalStateException();
        return enum;
    }
    public static MarketSessionType fromString(java.lang.String value)
          throws java.lang.IllegalStateException {
        try {
            return fromValue(new org.apache.axis.types.NormalizedString(value));
        } catch (Exception e) {
            throw new java.lang.IllegalStateException();
        }
    }
    public boolean equals(java.lang.Object obj) {return (obj == this);}
    public int hashCode() { return toString().hashCode();}
    public java.lang.String toString() { return _value_.toString();}
    public java.lang.Object readResolve() throws java.io.ObjectStreamException { return fromValue(_value_);}
}
