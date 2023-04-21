package edu.arizona.cs.mbel.signature;

/** An interface with constants for Serialization flags
  * @author Michael Stepp
  */
public interface SerializationTypeConstants {

    public static final byte SERIALIZATION_TYPE_BOOLEAN = SignatureConstants.ELEMENT_TYPE_BOOLEAN;

    public static final byte SERIALIZATION_TYPE_CHAR = SignatureConstants.ELEMENT_TYPE_CHAR;

    public static final byte SERIALIZATION_TYPE_I1 = SignatureConstants.ELEMENT_TYPE_I1;

    public static final byte SERIALIZATION_TYPE_U1 = SignatureConstants.ELEMENT_TYPE_U1;

    public static final byte SERIALIZATION_TYPE_I2 = SignatureConstants.ELEMENT_TYPE_I2;

    public static final byte SERIALIZATION_TYPE_U2 = SignatureConstants.ELEMENT_TYPE_U2;

    public static final byte SERIALIZATION_TYPE_I4 = SignatureConstants.ELEMENT_TYPE_I4;

    public static final byte SERIALIZATION_TYPE_U4 = SignatureConstants.ELEMENT_TYPE_U4;

    public static final byte SERIALIZATION_TYPE_I8 = SignatureConstants.ELEMENT_TYPE_I8;

    public static final byte SERIALIZATION_TYPE_U8 = SignatureConstants.ELEMENT_TYPE_U8;

    public static final byte SERIALIZATION_TYPE_R4 = SignatureConstants.ELEMENT_TYPE_R4;

    public static final byte SERIALIZATION_TYPE_R8 = SignatureConstants.ELEMENT_TYPE_R8;

    public static final byte SERIALIZATION_TYPE_STRING = SignatureConstants.ELEMENT_TYPE_STRING;

    public static final byte SERIALIZATION_TYPE_SZARRAY = SignatureConstants.ELEMENT_TYPE_SZARRAY;

    public static final byte SERIALIZATION_TYPE_TYPE = 0x50;

    public static final byte SERIALIZATION_TYPE_TAGGED_OBJECT = 0x51;

    public static final byte SERIALIZATION_TYPE_FIELD = 0x53;

    public static final byte SERIALIZATION_TYPE_PROPERTY = 0x54;

    public static final byte SERIALIZATION_TYPE_ENUM = 0x55;
}
