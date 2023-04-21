package com.sun.jmx.snmp;

/**
 * Represents an SNMP gauge.
 *
 * <p><b>This API is a Sun Microsystems internal API  and is subject 
 * to change without notice.</b></p>
 * @version     4.8     11/17/05
 * @author      Sun Microsystems, Inc
 */
public class SnmpGauge extends SnmpUnsignedInt {

    /**
     * Constructs a new <CODE>SnmpGauge</CODE> from the specified integer value.
     * @param v The initialization value.
     * @exception IllegalArgumentException The specified value is negative
     * or larger than {@link SnmpUnsignedInt#MAX_VALUE SnmpUnsignedInt.MAX_VALUE}. 
     */
    public SnmpGauge(int v) throws IllegalArgumentException {
        super(v);
    }

    /**
     * Constructs a new <CODE>SnmpGauge</CODE> from the specified <CODE>Integer</CODE> value.
     * @param v The initialization value.
     * @exception IllegalArgumentException The specified value is negative
     * or larger than {@link SnmpUnsignedInt#MAX_VALUE SnmpUnsignedInt.MAX_VALUE}. 
     */
    public SnmpGauge(Integer v) throws IllegalArgumentException {
        super(v);
    }

    /**
     * Constructs a new <CODE>SnmpGauge</CODE> from the specified long value.
     * @param v The initialization value.
     * @exception IllegalArgumentException The specified value is negative
     * or larger than {@link SnmpUnsignedInt#MAX_VALUE SnmpUnsignedInt.MAX_VALUE}. 
     */
    public SnmpGauge(long v) throws IllegalArgumentException {
        super(v);
    }

    /**
     * Constructs a new <CODE>SnmpGauge</CODE> from the specified <CODE>Long</CODE> value.
     * @param v The initialization value.
     * @exception IllegalArgumentException The specified value is negative
     * or larger than {@link SnmpUnsignedInt#MAX_VALUE SnmpUnsignedInt.MAX_VALUE}. 
     */
    public SnmpGauge(Long v) throws IllegalArgumentException {
        super(v);
    }

    /**
     * Returns a textual description of the type object.
     * @return ASN.1 textual description.
     */
    public final String getTypeName() {
        return name;
    }

    /**
     * Name of the type.
     */
    static final String name = "Gauge32";
}
