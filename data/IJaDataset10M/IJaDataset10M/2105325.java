package org.opennms.protocols.snmp;

import java.lang.*;
import java.io.Serializable;
import org.opennms.protocols.snmp.SnmpSMI;
import org.opennms.protocols.snmp.SnmpSyntax;
import org.opennms.protocols.snmp.asn1.AsnEncoder;
import org.opennms.protocols.snmp.asn1.AsnEncodingException;
import org.opennms.protocols.snmp.asn1.AsnDecodingException;

/**
 * This class defines the SNMP 32-bit signed integer
 * used by the SNMP SMI.
 *
 * @author <a href="mailto:weave@opennms.org">Brian Weaver</a>
 * @revision $Revision: 1.8 $
 *
 */
public class SnmpInt32 extends Object implements SnmpSyntax, Cloneable, Serializable {

    /**
	 * The internal 32-bit signed quantity
	 */
    private int m_value;

    /**
	 * Added for serialization support
	 */
    static final long serialVersionUID = -3472172482048507843L;

    /**
	 * The ASN.1 type as defined by the SNMP SMI
	 * specification.
	 */
    public static final byte ASNTYPE = SnmpSMI.SMI_INTEGER;

    /**
	 * Default constructor. Builds a SnmpInt32 objeect with
	 * a value of zero.
	 */
    public SnmpInt32() {
        m_value = 0;
    }

    /**
	 * Constructs a SnmpInt32 object with the passed value.
	 *
	 * @param value	The 32-bit signed integer value for the object.
	 *
	 */
    public SnmpInt32(int value) {
        m_value = value;
    }

    /**
	 * Constructs a SnmpInt32 object with the specified value.
	 *
	 * @param value An Integer object containing the 32-bte value.
	 *
	 */
    public SnmpInt32(Integer value) {
        m_value = value.intValue();
    }

    /**
	 * Class copy constructor. Constructs a new object with the
	 * same value as the passed SnmpInt32 object.
	 *
	 * @param second The object to get the value from.
	 *
	 */
    public SnmpInt32(SnmpInt32 second) {
        m_value = second.m_value;
    }

    /**
	 * Used to access the internal 32-bit signed quantity.
	 *
	 * @return The 32-bit value
	 */
    public int getValue() {
        return m_value;
    }

    /**
	 * Used to set the 32-bit signed quantity
	 *
	 * @param value	The new value for the object.
	 *
	 */
    public void setValue(int value) {
        m_value = value;
    }

    /**
	 * Used to set the 32-bit signed quantity
	 *
	 * @param value The new value for the object
	 */
    public void setValue(Integer value) {
        m_value = value.intValue();
    }

    /**
	 * Used to retreive the ASN.1 type for this object.
	 *
	 * @return The ASN.1 value for the SnmpInt32
	 *
	 */
    public byte typeId() {
        return ASNTYPE;
    }

    /**
	 * Used to encode the integer value into an ASN.1 buffer.
	 * The passed encoder defines the method for encoding the
	 * data.
	 *
	 * @param buf		The location to write the encoded data
	 * @param offset	The start of the encoded buffer.
	 * @param encoder	The ASN.1 encoder object
	 *
	 * @return The byte immediantly after the last encoded byte.
	 *
	 */
    public int encodeASN(byte[] buf, int offset, AsnEncoder encoder) throws AsnEncodingException {
        return encoder.buildInteger32(buf, offset, typeId(), m_value);
    }

    /**
	 * Used to decode the integer value from the ASN.1 buffer.
	 * The passed encoder is used to decode the ASN.1 information
	 * and the integer value is stored in the internal object.
	 *
	 * @param buf		The encoded ASN.1 data
	 * @param offset	The offset of the first byte of data
	 * @param encoder	The ASN.1 decoder object.
	 *
	 * @return The byte immediantly after the last decoded byte of
	 *	information.
	 *
	 */
    public int decodeASN(byte[] buf, int offset, AsnEncoder encoder) throws AsnDecodingException {
        Object[] rVals = encoder.parseInteger32(buf, offset);
        if (((Byte) rVals[1]).byteValue() != typeId()) throw new AsnDecodingException("Invalid ASN.1 type");
        m_value = ((Integer) rVals[2]).intValue();
        return ((Integer) rVals[0]).intValue();
    }

    /**
	 * Returns a duplicate of the current object.
	 *
	 * @return A newly allocated duplicate object.
	 *
	 */
    public SnmpSyntax duplicate() {
        return new SnmpInt32(this);
    }

    /**
	 * Returns a duplicate of the current object.
	 *
	 * @return A newly allocated duplicate object.
	 *
	 */
    public Object clone() {
        return new SnmpInt32(this);
    }

    /**
	 * Returns the string representation of the object.
	 *
	 */
    public String toString() {
        return "SNMP Int32 [" + getValue() + "]";
    }
}
