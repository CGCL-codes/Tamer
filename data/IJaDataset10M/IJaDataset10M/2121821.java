package ipmss.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.inject.Named;

/**
 * The Class MyMessageDigest.
 *
 * @author Michał Czarnik
 */
public class MyMessageDigest {

    /** The algorithm. */
    String algorithm;

    /** The digest. */
    MessageDigest digest;

    /** The char encoding. */
    String charEncoding = "utf8";

    /**
     * Instantiates a new my message digest.
     *
     * @param algorithm the algorithm
     * @throws NoSuchAlgorithmException the no such algorithm exception
     */
    public MyMessageDigest(String algorithm) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algorithm);
    }

    /**
    * Instantiates a new my message digest.
    *
    * @param algorithm the algorithm
    * @param charEncoding the char encoding
    * @throws NoSuchAlgorithmException the no such algorithm exception
    */
    public MyMessageDigest(String algorithm, String charEncoding) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algorithm);
        this.charEncoding = charEncoding;
    }

    /**
     * Sets the char encoding.
     *
     * @param charEncoding the new char encoding
     */
    void setCharEncoding(String charEncoding) {
        this.charEncoding = charEncoding;
    }

    /**
     * Convert message.
     *
     * @param message the message
     * @return the string
     * @throws UnsupportedEncodingException the unsupported encoding exception
     */
    public String convertMessage(String message) throws UnsupportedEncodingException {
        digest.update(message.getBytes(charEncoding), 0, message.length());
        byte[] byteMessage = digest.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < byteMessage.length; i++) {
            int halfbyte = (byteMessage[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) buf.append((char) ('0' + halfbyte)); else buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = byteMessage[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
