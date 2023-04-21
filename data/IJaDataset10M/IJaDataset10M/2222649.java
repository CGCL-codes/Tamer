package evolaris.framework.sys.business;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Provides methods for encoding/decoding ids. Useful for marshalling ids passed in URLs.
 * @author robert.brandner
 *
 */
public class IdCodec {

    private static final long SECRET_PRIMENUMBER = 1009L;

    private static final long SECRET_OFFSET = 1969;

    private static final int DIGEST_PART = 7;

    /**
	 * create a nice code from specified id
	 * @param id
	 * @return
	 */
    public static String encodeId(long id) {
        id += SECRET_OFFSET;
        id *= SECRET_PRIMENUMBER;
        String code = Long.toHexString(id);
        String digest = digest(id, DIGEST_PART);
        return digest + code;
    }

    /**
	 * gets id from specified code
	 * @param code
	 * @return the original id, or -1 if code is illegal or any other error occurs
	 */
    public static long decodeId(String code) {
        String digest = code.substring(0, DIGEST_PART);
        String idStr = code.substring(DIGEST_PART);
        long id = Long.parseLong(idStr, 16);
        String check = digest(id, DIGEST_PART);
        id /= SECRET_PRIMENUMBER;
        id -= SECRET_OFFSET;
        if (check.equals(digest)) {
            return id;
        } else {
            return -1;
        }
    }

    private static String digest(long code, int part) {
        byte[] buffer = Long.toHexString(code).getBytes();
        byte[] digest = null;
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            sha.update(buffer);
            digest = sha.digest();
        } catch (NoSuchAlgorithmException e) {
            digest = buffer;
        }
        StringBuffer buf = new StringBuffer();
        for (int i = digest.length - 1; i >= 0; i--) {
            int b = Math.abs(digest[i]);
            String h = Long.toHexString(b);
            buf.append(h);
        }
        return buf.toString().substring(0, part);
    }
}
