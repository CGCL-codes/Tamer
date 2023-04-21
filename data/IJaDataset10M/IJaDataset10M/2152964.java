package net.sf.immc.util.security.hashEncrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash算法加密工具类。
 * <pre>
 * <code>Hash Algorithms：SHA-1, HAVAL, MD2, MD5, SHA-256, SHA-384, SHA-512</code>
 * </pre>
 * 
 * @author <b>Oxidy</b>, Copyright &#169; 2007-2010
 * @version 0.1,2010/12/29
 */
public class HashCrypt {

    private String algorithm;

    private String encodingMode;

    public HashCrypt() {
    }

    /**
     * Selects the hash algorithm used by methods that create hashes. 
     * The valid choices are "sha1", "sha256", "sha384", "sha512", "md2", "md5", and "haval".
     * 
     * @param paramString
     * @return
     */
    public void put_HashAlgorithm(String paramString) {
        algorithm = paramString;
    }

    /**
     * Controls the encoding of binary data to a printable string for many methods. 
     * The valid modes are "Base64", "Base32", "UU", "QP" (for quoted-printable), "URL" (for url-encoding), and "Hex".
     * 
     * @param paramString
     */
    public void put_EncodingMode(String paramString) {
        encodingMode = paramString;
    }

    /**
     * Hashes a string and returns an encoded (printable) string of the binary hash. The hash algorithm to be used is 
     * controlled by the HashAlgorithm property, which can be set to "sha1", "sha384", "sha512", "md2", "md5", or 
     * "haval". The Charset property controls the character encoding of the string that is hashed. Languages such as 
     * VB.NET, C#, and Visual Basic work with Unicode strings. If it is desired to hash Unicode directly (2 bytes/char)
     * then set the Charset property to "Unicode". To implicitly convert to another charset before hashing, set the 
     * Charset property to the desired charset. For example, if Charset is set to "iso-8859-1", the input string is 
     * first implicitly converted to iso-8859-1 (1 byte per character) before hashing. The full list of supported 
     * charsets is listed in the EncryptString method description.
     *   The encoding of the output string is controlled by the EncodingMode property, which can be set to "Base64", 
     * "QP", or "Hex".
     *   The HAVAL hash algorithm is affected by two other properties: HavalRounds and KeyLength. The
     * HavalRounds may have values of 3, 4, or 5. The KeyLength may have values of 128, 160, 192, 224, or 256. 
     *    Returns true for success, false for failure.]
     *    
     * @param str
     * @return
     */
    public String hashStringENC(String paramString) {
        return getHash(paramString, algorithm);
    }

    private String getHash(String message, String algorithm) {
        try {
            byte[] buffer = message.getBytes();
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(buffer);
            byte[] digest = md.digest();
            String hex = null;
            for (int i = 0; i < digest.length; i++) {
                int b = digest[i] & 0xff;
                if (Integer.toHexString(b).length() == 1) hex = hex + "0";
                hex = hex + Integer.toHexString(b);
            }
            return hex;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Hash using HAVAL
     * There are two additional properties relevant to HAVAL:
     * HavalRounds, and KeyLength.
     * HavalRounds can have values of 3, 4, or 5.
     * KeyLength can have values of 128, 160, 192, 224, or 256
     * 
     * @param paramInt
     */
    public void put_HavalRounds(int paramInt) {
    }

    /**
     * Hash using HAVAL
     * There are two additional properties relevant to HAVAL:
     * HavalRounds, and KeyLength.
     * HavalRounds can have values of 3, 4, or 5.
     * KeyLength can have values of 128, 160, 192, 224, or 256
     * 
     * @param numvalue
     */
    public void put_KeyLength(int paramInt) {
    }
}
