package org.metastatic.jessie.pki.der;

/**
 * The set of tags for DER types.
 *
 * @author Casey Marshall (rsdio@metastatic.org)
 */
public interface DER {

    int UNIVERSAL = 0x00;

    int APPLICATION = 0x40;

    int CONTEXT = 0x80;

    int PRIVATE = 0xC0;

    int CONSTRUCTED = 0x20;

    int ANY = 0x00;

    int BOOLEAN = 0x01;

    int INTEGER = 0x02;

    int BIT_STRING = 0x03;

    int OCTET_STRING = 0x04;

    int NULL = 0x05;

    int OBJECT_IDENTIFIER = 0x06;

    int REAL = 0x09;

    int ENUMERATED = 0x0a;

    int RELATIVE_OID = 0x0d;

    int SEQUENCE = 0x10;

    int SET = 0x11;

    Object CONSTRUCTED_VALUE = new Object();

    int NUMERIC_STRING = 0x12;

    int PRINTABLE_STRING = 0x13;

    int T61_STRING = 0x14;

    int VIDEOTEX_STRING = 0x15;

    int IA5_STRING = 0x16;

    int GRAPHIC_STRING = 0x19;

    int ISO646_STRING = 0x1A;

    int GENERAL_STRING = 0x1B;

    int UTF8_STRING = 0x0C;

    int UNIVERSAL_STRING = 0x1C;

    int BMP_STRING = 0x1E;

    int UTC_TIME = 0x17;

    int GENERALIZED_TIME = 0x18;

    int PRE_ENCODED = -1;
}
