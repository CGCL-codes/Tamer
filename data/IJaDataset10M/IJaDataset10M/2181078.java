package net.sf.passwordpurse.core;

/**
 * @author: Brian Burch
 *
 * SelfDefinedField
 *
 * @version 1.0
 *
 * Interface used to define self-defined field contract, as
 * implemented by the LengthDefinedField class.
 */
public interface SelfDefinedField {

    public static final int OFFSET_OF_LENGTH = 0;

    public static final int LENGTH_OF_LENGTH = 2;

    public static final int OFFSET_OF_TYPE = OFFSET_OF_LENGTH + LENGTH_OF_LENGTH;

    public static final int LENGTH_OF_TYPE = 1;

    public static final int LENGTH_OF_HEADER = LENGTH_OF_LENGTH + LENGTH_OF_TYPE;

    public static final int OFFSET_OF_DATA = LENGTH_OF_HEADER;

    public static final int MINIMUM_LENGTH = LENGTH_OF_HEADER;

    public static final SelfDefinedFieldType TYPE_BYTES = new SelfDefinedFieldType("Byte", 1, false);

    public static final SelfDefinedFieldType TYPE_STRING = new SelfDefinedFieldType("String", 2, false);

    public static final SelfDefinedFieldType TYPE_SECRETS = new SelfDefinedFieldType("Secrets", 10, false);

    public static final SelfDefinedFieldType TYPE_POCKET_OF_SECRETS = new SelfDefinedFieldType("Pocket of Secrets", 11, false);

    public static final SelfDefinedFieldType TYPE_PURSE = new SelfDefinedFieldType("Purse", 22, false);

    public static final SelfDefinedFieldType TYPE_ENCRYPTED_OBJECT = new SelfDefinedFieldType("Encrypted object", 31, true);

    public static final SelfDefinedFieldType TYPE_ENCRYPTED_PURSE = new SelfDefinedFieldType("Encrypted Purse", 38, true);

    public static final SelfDefinedFieldType TYPE_INVALID = new SelfDefinedFieldType("Invalid Type", 0, false);

    /**
 * returns the encoded content of the entire field, including the
 * length and type in an immutable way.
 * @return byte[]
 */
    public byte[] getBytes();

    /**
 * returns the payload as a cloned byte array, excluding the
 * length and type header fileds.
 * @return byte[]
 */
    public byte[] getPayload();

    /**
 * returns the actual encoded content of the entire field, including
 * the length and type.
 *
 * Danger! Subclasses MUST NOT change this array!
 *
 * @return byte[]
 */
    public byte[] getBytesReadonly();

    /**
 * @return java.lang.String a meaningful representation of the payload
 */
    public String getPayloadAsString();

    /**
 * return the inclusive length of the content, which
 * would be acquired via getBytes()
 * @return int
 */
    public int size();

    /**
 * get the type of this instance
 */
    public SelfDefinedFieldType getType();

    /**
 * concrete subclasses must implement this method to decide
 * whether the payload meets any special syntax requirements.
 * They also have the opportunity to cache the payload at this time.
 *
 * Note: although most SelfDefinedField classes do not use encryption,
 * the parser must know the correct passphrase if it is needed.
 * @return boolean to signal whether the payload conforms to syntax rules
 */
    public void validate(LengthDefinedFieldParser parser) throws PurseStructuralException;

    /**
 * allow subclasses with non-trivial payloads to save them in a convenient
 * form when a new instance has just been parsed.
 *
 * Note: although most SelfDefinedField classes do not use encryption,
 * the parser must know the correct passphrase if it is needed.
 * @return boolean to signal whether the payload conforms to syntax rules
 */
    public void parsePayload(LengthDefinedFieldParser parser) throws PurseStructuralException;

    /**
 * subclasses should signal when parsePayload has finished interpreting
 * the payload (at the next level of nesting) and so its contents can
 * be trusted to have meaningful values.
 *
 * @return boolean true when content has been parsed successfully
 */
    public boolean hasContentBeenParsed();

    /**
 * subclasses should signal when parsePayload has finished interpreting
 * the payload (at the next level of nesting) and so its contents can
 * be trusted to have meaningful values.
 *
 * @return boolean true when content has been parsed successfully
 */
    public void setContentHasBeenParsed();
}
