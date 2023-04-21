package net.community.chest.net.auth;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.community.chest.io.encode.base64.Base64;
import net.community.chest.io.encode.hex.Hex;
import net.community.chest.reflect.ClassUtil;

/**
 * <P>Copyright 2007 as per GPLv2</P>
 * 
 * <P>Useful class for various protocols that use digests for authentications
 * (e.g. CRAM-MD5, DIGEST-MD5, etc.)</P>
 * 
 * @author Lyor G.
 * @since Sep 19, 2007 9:49:43 AM
 */
public abstract class AuthDigester {

    protected AuthDigester() {
        super();
    }

    /**
	 * @param dgName digester "name"
	 * @return digester object 
	 * @throws NoSuchAlgorithmException if unable to create such an instance
	 * @see java.security.MessageDigest#getInstance(java.lang.String)
	 */
    public static final MessageDigest getDigesterInstance(String dgName) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(dgName);
    }

    /**
	 * @return digester for MD5 algorithm
	 * @throws NoSuchAlgorithmException if unable to create such an instance
	 */
    public static final MessageDigest getMD5DigesterInstance() throws NoSuchAlgorithmException {
        return getDigesterInstance("MD5");
    }

    /**
	 * @return new <U>created</U> message digest object (MD5) to be used
	 * @throws NoSuchAlgorithmException if unable to create such a digester
	 */
    protected MessageDigest getMD5Digester() throws NoSuchAlgorithmException {
        return getMD5DigesterInstance();
    }

    /**
	 * Returns the string's characters as an array of bytes
	 * @param s input string - may NOT be null/empty
	 * @return The string's characters as an array of bytes
	 * @throws IllegalArgumentException if null/empty string
	 * @throws IllegalStateException if character in string not in 0-255 range
	 */
    public static final byte[] getAuthStringBytes(String s) {
        final int sLen = (null == s) ? 0 : s.length();
        if (sLen <= 0) throw new IllegalArgumentException("Null/empty authentication string");
        final byte[] sBytes = new byte[sLen];
        for (int sIndex = 0; sIndex < sLen; sIndex++) {
            final char sch = s.charAt(sIndex);
            if ((sch < 0) || (sch > 255)) throw new IllegalStateException("Authentication string character at position=" + sIndex + " not in 0-255 range (value=" + s + ")");
            sBytes[sIndex] = (byte) sch;
        }
        return sBytes;
    }

    /**
	 * MD5 hash pad - as per RFC2195
	 */
    public static final int MAX_MD5_PAD_LEN = 64;

    /**
	 * Calculates the MD5 digest of the challenge string and password as
	 * specified in RFC2104
	 * @param text BASE64 <U>decoded</U> challenge - may NOT be null/empty
	 * @param key key to be used - may NOT be null/empty
	 * @param md5Digester MD5 digester (can be obtained via {@link #getMD5DigesterInstance()})
	 * @return MD5 digest bytes calculation
	 * @throws IllegalArgumentException if null/empty text/key/digester
	 */
    public static final byte[] HMAC(String text, String key, MessageDigest md5Digester) {
        final int textLen = (null == text) ? 0 : text.length(), keyLen = (null == key) ? 0 : key.length();
        if ((textLen <= 0) || (keyLen <= 0) || (null == md5Digester)) throw new IllegalArgumentException("Null/empty text/key/digester");
        final byte[] xorIPAD = new byte[MAX_MD5_PAD_LEN], xorOPAD = new byte[MAX_MD5_PAD_LEN];
        byte[] keyBytes = getAuthStringBytes(key);
        if (keyLen > MAX_MD5_PAD_LEN) keyBytes = md5Digester.digest(keyBytes);
        for (int kIndex = 0; kIndex < keyBytes.length; kIndex++) {
            xorIPAD[kIndex] = (byte) (keyBytes[kIndex] ^ 0x36);
            xorOPAD[kIndex] = (byte) (keyBytes[kIndex] ^ 0x5C);
        }
        for (int pIndex = keyBytes.length; pIndex < MAX_MD5_PAD_LEN; pIndex++) {
            xorIPAD[pIndex] = 0x36;
            xorOPAD[pIndex] = 0x5C;
        }
        {
            md5Digester.update(xorIPAD);
            final byte[] chlngBytes = new byte[textLen];
            for (int cIndex = 0; cIndex < textLen; cIndex++) {
                final char cch = text.charAt(cIndex);
                if ((cch < 0) || (cch > 255)) throw new IllegalStateException("Text char at position=" + cIndex + " is not in 0-255 range");
                chlngBytes[cIndex] = (byte) cch;
            }
            md5Digester.update(chlngBytes);
        }
        {
            final byte[] imedDigest = md5Digester.digest();
            md5Digester.update(xorOPAD);
            md5Digester.update(imedDigest);
            return md5Digester.digest();
        }
    }

    /**
	 * Calculates the MD5 digest of the challenge string and password as
	 * specified in RFC2104
	 * @param text BASE64 <U>decoded</U> challenge - may NOT be null/empty
	 * @param key key to be used - may NOT be null/empty
	 * @return MD5 digest bytes calculation
	 * @throws NoSuchAlgorithmException if unable to get an MD5 digester
	 * @throws IllegalArgumentException if null/empty text/key
	 */
    public final byte[] HMAC(String text, String key) throws NoSuchAlgorithmException, IllegalArgumentException {
        return HMAC(text, key, getMD5Digester());
    }

    /**
	 * @param chlng CRAM-MD5 challenge string from server
	 * @param username username - may NOT be null/empty
	 * @param password password - may NOT be null/empty
	 * @param md5Digester MD5 digester instance (can be obtained via {@link #getMD5DigesterInstance()}
	 * @return CRAM-MD5 response for the challenge as "clear-text" - usually,
	 * this text is further BASE64 encode - if this is the default, then use
	 * {@link #getCRAMMD5ChallengeResponse(String, String, String, MessageDigest)}
	 * @throws IllegalArgumentException if null/empty challenge/username/password/digester
	 */
    public static final String getCRAMMD5ChallengeBaseResponse(String chlng, String username, String password, MessageDigest md5Digester) throws IllegalArgumentException {
        if ((null == username) || (username.length() <= 0)) throw new IllegalArgumentException("Null/Empty username supplied");
        final byte[] hashBytes = HMAC(chlng, password, md5Digester);
        final String hashStr = Hex.toString(hashBytes, '\0', false);
        return username + " " + hashStr;
    }

    /**
	 * @param chlng CRAM-MD5 challenge string from server
	 * @param username username - may NOT be null/empty
	 * @param password password - may NOT be null/empty
	 * @return CRAM-MD5 response for the challenge as "clear-text" - usually,
	 * this text is further BASE64 encode - if this is the default, then use
	 * {@link #getCRAMMD5ChallengeResponse(String, String, String, MessageDigest)}
	 * @throws NoSuchAlgorithmException if unable to get an MD5 digester instance
	 * @throws IllegalArgumentException if null/empty challenge/username/password
	 */
    public String getCRAMMD5ChallengeBaseResponse(String chlng, String username, String password) throws NoSuchAlgorithmException, IllegalArgumentException {
        return getCRAMMD5ChallengeBaseResponse(chlng, username, password, getMD5Digester());
    }

    /**
	 * @param chlng CRAM-MD5 challenge string from server
	 * @param username username - may NOT be null/empty
	 * @param password password - may NOT be null/empty
	 * @param md5Digester MD5 digester instance (can be obtained via {@link #getMD5DigesterInstance()}
	 * @return CRAM-MD5 response for the challenge as BASE64 - which is the
	 * usual default. If the base response is required then use {@link #getCRAMMD5ChallengeBaseResponse(String, String, String, MessageDigest)}
	 * @throws IllegalArgumentException if null/empty challenge/username/password/digester
	 */
    public static final String getCRAMMD5ChallengeResponse(String chlng, String username, String password, MessageDigest md5Digester) throws IllegalArgumentException {
        return Base64.encode(getCRAMMD5ChallengeBaseResponse(chlng, username, password, md5Digester));
    }

    /**
	 * @param chlng CRAM-MD5 challenge string from server
	 * @param username username - may NOT be null/empty
	 * @param password password - may NOT be null/empty
	 * @return CRAM-MD5 response for the challenge as BASE64 - which is the
	 * usual default. If the base response is required then use {@link #getCRAMMD5ChallengeBaseResponse(String, String, String, MessageDigest)}
	 * @throws NoSuchAlgorithmException if unable to get an MD5 digester instance
	 * @throws IllegalArgumentException if null/empty challenge/username/password/digester
	 */
    public String getCRAMMD5ChallengeResponse(String chlng, String username, String password) throws NoSuchAlgorithmException, IllegalArgumentException {
        return getCRAMMD5ChallengeResponse(chlng, username, password, getMD5Digester());
    }

    public static final <A extends Appendable> A appendPlainChallengeBaseResponse(A sb, String username, String password) throws IOException {
        final int uLen = (null == username) ? 0 : username.length(), pLen = (null == password) ? 0 : password.length();
        if ((uLen <= 0) || (pLen <= 0)) throw new StreamCorruptedException(ClassUtil.getArgumentsExceptionLocation(AuthDigester.class, "appendPlainChallengeBaseResponse", username, password) + " null/empty arguments");
        if (null == sb) throw new IOException(ClassUtil.getArgumentsExceptionLocation(AuthDigester.class, "appendPlainChallengeBaseResponse", username, password) + " no " + Appendable.class.getName() + " instance supplied");
        sb.append('\0').append(username).append('\0').append(password);
        return sb;
    }

    /**
	 * @param username username - may NOT be null/empty
	 * @param password password - may NOT be null/empty
	 * @return PLAIN base response text - usually this text is BASE64 encoded
	 * before being sent through the network. If this is the case, then use
	 * the {@link #getPlainChallengeResponse(String, String)} method
	 * @throws IllegalStateException if unable to build response
	 */
    public static final String getPlainChallengeBaseResponse(String username, String password) throws IllegalStateException {
        final int uLen = (null == username) ? 0 : username.length(), pLen = (null == password) ? 0 : password.length(), aLen = Math.max(uLen, 0) + 1 + Math.max(pLen, 0) + 1;
        try {
            final StringBuilder sb = ((uLen <= 0) || (pLen <= 0)) ? null : new StringBuilder(aLen), res = appendPlainChallengeBaseResponse(sb, username, password);
            return res.toString();
        } catch (IOException e) {
            throw new IllegalStateException(ClassUtil.getArgumentsExceptionLocation(AuthDigester.class, "getPlainChallengeBaseResponse", username, password) + " " + e.getClass().getName() + " when attempting to append: " + e.getMessage());
        }
    }

    /**
	 * @param username username - may NOT be null/empty
	 * @param password password - may NOT be null/empty
	 * @return PLAIN base response text - this text is BASE64 encoded
	 * expecting it to be sent to the network. If this is not the case, then
	 * use the {@link #getPlainChallengeBaseResponse(String, String)} method 
	 * @throws IllegalArgumentException if null/empty username/password
	 * @throws IllegalStateException if unable to build response
	 */
    public static final String getPlainChallengeResponse(String username, String password) throws IllegalArgumentException, IllegalStateException {
        return Base64.encode(getPlainChallengeBaseResponse(username, password));
    }
}
