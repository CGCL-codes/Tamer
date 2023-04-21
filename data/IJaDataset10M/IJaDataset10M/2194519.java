package org.tuotoo.crypto;

/**
 * This interface represents an algorithm that verifies cryptographic
 * signatures.
 */
public interface ISignatureVerificationAlgorithm {

    /**
     * Tests if the signature of a specified message is valid.
     * 
     * @param a_message
     *            a message
     * @param a_signature
     *            a signature
     * @return true if the signature of a specified message is valid; false
     *         otherwiese
     */
    public boolean verify(byte[] a_message, byte[] a_signature);

    /**
     * Tests if the signature of a specified message is valid.
     * 
     * @param a_message
     *            a message
     * @param message_offset
     *            start of message
     * @param message_len
     *            length of message
     * @param a_signature
     *            a signature
     * @param signature_offset
     *            start of signature
     * @param signature_len
     *            length of signature
     * @return true if the signature of a specified message is valid; false
     *         otherwiese
     */
    public boolean verify(byte[] a_message, int message_offset, int message_len, byte[] a_signature, int signature_offset, int signature_len);

    /**
     * Tries to decode a signature in a way as it would meet the W3C standard
     * for XML signature values. Without this decoding, RSA XML signatures
     * cannot be verified.
     * 
     * @param a_encodedSignature
     *            an encoded signature
     * @return the decoded signature or null if an error occured
     * @see http://www.w3.org/TR/xmldsig-core/#sec-SignatureAlg
     */
    public byte[] decodeForXMLSignature(byte[] a_encodedSignature);

    /**
     * Returns a description of the the signature algorithm for XML signatures
     * as defined in http://www.w3.org/TR/xmldsig-core/#sec-AlgID. This
     * description is optional, documents may be signed without it.
     * 
     * @return a description of the the signature algorithm for XML signatures
     * @see http://www.w3.org/TR/xmldsig-core/#sec-AlgID
     */
    public String getXMLSignatureAlgorithmReference();
}
