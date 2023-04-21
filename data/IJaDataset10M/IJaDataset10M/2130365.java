package org.hl7.types;

/**
 *  The following is actually a union, but we make it explicit for simplicity
 * <pre>
 *      union binary_or_text switch(cs_BinaryDataEncoding) {
 *              case B64: bin_value binaryValue;
 *              case TXT: st_value textualValue;
 *      };
 * </pre>
 */
public final class binary_or_text implements org.omg.CORBA.portable.IDLEntity {

    public org.hl7.types.cs_BinaryDataEncoding itemType = null;

    public byte binaryValue[] = null;

    public String textualValue = null;

    public binary_or_text() {
    }

    public binary_or_text(org.hl7.types.cs_BinaryDataEncoding _itemType, byte[] _binaryValue, String _textualValue) {
        itemType = _itemType;
        binaryValue = _binaryValue;
        textualValue = _textualValue;
    }
}
