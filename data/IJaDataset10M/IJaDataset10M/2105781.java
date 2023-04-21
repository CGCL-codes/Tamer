package net.sourceforge.myvd.protocol.ldap.mina.asn1.codec;

/**
 * Defines common decoding methods for byte array decoders.
 * 
 * @author <a href="mailto:dev@directory.apache.org">Apache Directory Project</a>
 * @version $Id: BinaryDecoder.java 437004 2008-08-25 22:53:17 +0000 (Fri, 25 Aug 2008) elecharny $
 */
public interface BinaryDecoder extends Decoder {

    /**
     * Decodes a byte array and returns the results as a byte array.
     * 
     * @param pArray
     *            A byte array which has been encoded with the appropriate
     *            encoder
     * @return a byte array that contains decoded content
     * @throws net.sourceforge.myvd.protocol.ldap.mina.asn1.codec.DecoderException
     *             A decoder exception is thrown if a Decoder encounters a
     *             failure condition during the decode process.
     */
    byte[] decode(byte[] pArray) throws DecoderException;
}
