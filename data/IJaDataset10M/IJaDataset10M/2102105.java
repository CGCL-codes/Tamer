package whereisnow.resources;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1DigestGenerator {

    private static String convertToHex(byte[] data) {
        StringBuffer buf = null;
        buf = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            int halfbyte = (data[i] >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9)) {
                    buf.append((char) ('0' + halfbyte));
                } else {
                    buf.append((char) ('a' + (halfbyte - 10)));
                }
                halfbyte = data[i] & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }

    public static String getSHA1Digest(String inputStr) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = null;
        byte[] sha1hash = null;
        md = MessageDigest.getInstance("SHA");
        sha1hash = new byte[40];
        md.update(inputStr.getBytes("iso-8859-1"), 0, inputStr.length());
        sha1hash = md.digest();
        return convertToHex(sha1hash);
    }
}
