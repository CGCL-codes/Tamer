package hla.rti1516e.encoding;

/**
 * Interface provided by all HLA data elements.
 */
public interface DataElement {

    /**
    * Returns the octet boundary of this element.
    *
    * @return the octet boundary of this element
    */
    int getOctetBoundary();

    /**
    * Encodes this element into the specified ByteWrapper.
    *
    * @param byteWrapper destination for the encoded element
    *
    * @throws EncoderException if the element can not be encoded
    */
    void encode(ByteWrapper byteWrapper) throws EncoderException;

    /**
    * Returns the size in bytes of this element's encoding.
    *
    * @return the size in bytes of this element's encoding
    */
    int getEncodedLength();

    /**
    * Returns a byte array with this element encoded.
    *
    * @return byte array with encoded element
    *
    * @throws EncoderException if the element can not be encoded
    */
    byte[] toByteArray() throws EncoderException;

    /**
    * Decodes this element from the ByteWrapper.
    *
    * @param byteWrapper source for the decoding of this element
    *
    * @throws DecoderException if the element can not be decoded
    */
    void decode(ByteWrapper byteWrapper) throws DecoderException;

    /**
    * Decodes this element from the byte array.
    *
    * @param bytes source for the decoding of this element
    * 
    * @throws DecoderException if the element can not be decoded
    */
    void decode(byte[] bytes) throws DecoderException;
}
