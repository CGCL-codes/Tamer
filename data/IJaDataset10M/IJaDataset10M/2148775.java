package com.sun.jmx.snmp;

/**
 * Contains SNMP data type constants.
 * All members are static and can be used by any application.
 *
 *
 * <p><b>This API is a Sun Microsystems internal API  and is subject 
 * to change without notice.</b></p>
 * @version     4.11     11/17/05
 * @author      Sun Microsystems, Inc
 * @author      Cisco Systems, Inc.
 */
public interface SnmpDataTypeEnums {

    /**
   * ASN.1 tag for representing the boolean type.
   */
    public static final int BooleanTag = 1;

    /**
   * ASN.1 tag for representing the integer type.
   */
    public static final int IntegerTag = 2;

    /**
   * ASN.1 tag for representing the bit string type.
   */
    public static final int BitStringTag = 2;

    /**
   * ASN.1 tag for representing the octet string type.
   */
    public static final int OctetStringTag = 4;

    /**
   * ASN.1 tag for representing the null type.
   */
    public static final int NullTag = 5;

    /**
   * ASN.1 tag for representing the Object Identifier type.
   */
    public static final int ObjectIdentiferTag = 6;

    /**
  * Represents a unknown syntax type. No meaning in an ASN.1 context.
  */
    public static final int UnknownSyntaxTag = 0xFF;

    /**
  * ASN.1 tag for a <CODE>SEQUENCE</CODE> or <CODE>SEQUENCE OF</CODE>.
  */
    public static final int SequenceTag = 0x30;

    /**
  * Represents an SNMP table. No meaning in an ASN.1 context.
  */
    public static final int TableTag = 0xFE;

    /**
   * ASN.1 Tag for application context.
   */
    public static final int ApplFlag = 64;

    /**
  * ASN.1 tag for implicit context.
  */
    public static final int CtxtFlag = 128;

    /**
   * ASN.1 tag for representing an IP address as defined in RFC 1155.
   */
    public static final int IpAddressTag = ApplFlag | 0;

    /**
   * ASN.1 tag for representing a <CODE>Counter32</CODE> as defined in RFC 1155.
   */
    public static final int CounterTag = ApplFlag | 1;

    /**
   * ASN.1 tag for representing a <CODE>Gauge32</CODE> as defined in RFC 1155.
   */
    public static final int GaugeTag = ApplFlag | 2;

    /**
   * ASN.1 tag for representing a <CODE>Timeticks</CODE> as defined in RFC 1155.
   */
    public static final int TimeticksTag = ApplFlag | 3;

    /**
   * ASN.1 tag for representing an <CODE>Opaque</CODE> type as defined in RFC 1155.
   */
    public static final int OpaqueTag = ApplFlag | 4;

    /**
   * ASN.1 tag for representing a <CODE>Counter64</CODE> as defined in RFC 1155.
   */
    public static final int Counter64Tag = ApplFlag | 6;

    /**
   * ASN.1 tag for representing an <CODE>Nsap</CODE> as defined in RFC 1902.
   */
    public static final int NsapTag = ApplFlag | 5;

    /**
   * ASN.1 tag for representing an <CODE>Unsigned32</CODE> integer as defined in RFC 1902.
   */
    public static final int UintegerTag = ApplFlag | 7;

    /**
   * ASN.1 tag for representing a <CODE>NoSuchObject</CODE> as defined in RFC 1902.
   */
    public static final int errNoSuchObjectTag = CtxtFlag | 0;

    /**
   * ASN.1 tag for representing a <CODE>NoSuchInstance</CODE> as defined in RFC 1902.
   */
    public static final int errNoSuchInstanceTag = CtxtFlag | 1;

    /**
   * ASN.1 tag for representing an <CODE>EndOfMibView</CODE> as defined in RFC 1902.
   */
    public static final int errEndOfMibViewTag = CtxtFlag | 2;
}
