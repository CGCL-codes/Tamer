package org.loon.framework.android.game.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.loon.framework.android.game.core.LGUID;
import org.loon.framework.android.game.core.LUUID;

public class PassWordUtils {

    private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    private static MessageDigest digest = null;

    private static final int ENCODE_XORMASK = 0x5A;

    private static final char ENCODE_DELIMETER = '\002';

    private static final char ENCODE_CHAR_OFFSET1 = 'A';

    private static final char ENCODE_CHAR_OFFSET2 = 'h';

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
        }
    }

    /**
	 * 将byte[]转为16进制字符串
	 * 
	 * @param b
	 * @return
	 */
    public static String toHexString(byte[] b) {
        StringBuffer resultSb = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    /**
	 * 将byte转为16进制字符串
	 * 
	 * @param b
	 * @return
	 */
    public static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) n = 256 + n;
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
	 * 将int转为16进制字符串
	 * 
	 * @param i
	 * @return
	 */
    public static String toHexString(final int i) {
        StringBuffer buf = new StringBuffer();
        appendHex(buf, i);
        return buf.toString();
    }

    /**
	 * 将int插入StringBuffer
	 * 
	 * @param buf
	 * @param i
	 */
    public static void appendHex(final StringBuffer buf, final int i) {
        buf.append(Integer.toHexString((i >> 24) & 0xff));
        buf.append(Integer.toHexString((i >> 16) & 0xff));
        buf.append(Integer.toHexString((i >> 8) & 0xff));
        buf.append(Integer.toHexString(i & 0xff));
    }

    /**
	 * 调用md5算法
	 * 
	 * @param origin
	 * @return
	 */
    public static String toMD5(String origin) {
        String result = null;
        try {
            result = new String(origin);
            result = PassWordUtils.toHexString(digest.digest(result.getBytes()));
        } catch (Exception ex) {
            return null;
        }
        return result;
    }

    /**
	 * 返回一组字母英文的混合密码
	 * 
	 * @param length
	 * @return
	 */
    public static String toBlendPassword(int length) {
        if (length < 1) return null;
        String strChars[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "a" };
        StringBuffer strPassword = new StringBuffer();
        int nRand = (int) Math.round(Math.random() * 100D);
        for (int i = 0; i < length; i++) {
            nRand = (int) Math.round(Math.random() * 100D);
            strPassword.append(strChars[nRand % (strChars.length - 1)]);
        }
        return strPassword.toString();
    }

    /**
	 * 返回一组数字密码
	 * 
	 * @param length
	 * @return
	 */
    public static String toNumberPassword(int length) {
        if (length < 1) return null;
        String strChars[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        StringBuffer strPassword = new StringBuffer();
        int nRand = (int) Math.round(Math.random() * 100D);
        for (int i = 0; i < length; i++) {
            nRand = (int) Math.round(Math.random() * 100D);
            strPassword.append(strChars[nRand % (strChars.length - 1)]);
        }
        return strPassword.toString();
    }

    /**
	 * 以Cookie格式将指定字符串解码
	 * 
	 * @param cookieVal
	 * @return
	 */
    public static String[] decodePasswordCookie(String cookieVal) {
        if ((cookieVal == null) || (cookieVal.length() <= 0)) {
            return null;
        }
        char[] chars = cookieVal.toCharArray();
        byte[] bytes = new byte[chars.length / 2];
        int b;
        for (int n = 0, m = 0; n < bytes.length; n++) {
            b = chars[m++] - ENCODE_CHAR_OFFSET1;
            b |= ((chars[m++] - ENCODE_CHAR_OFFSET2) << 4);
            bytes[n] = (byte) (b ^ (ENCODE_XORMASK + n));
        }
        cookieVal = new String(bytes);
        int pos = cookieVal.indexOf(ENCODE_DELIMETER);
        int pos1 = cookieVal.lastIndexOf(ENCODE_DELIMETER);
        String memberid = (pos < 0) ? "" : cookieVal.substring(0, pos);
        String username = (pos < 0) ? "" : cookieVal.substring(pos + 1, pos1);
        String password = (pos1 < 0) ? "" : cookieVal.substring(pos1 + 1);
        return new String[] { memberid, username, password };
    }

    /**
	 * 以Cookie格式将指定字符串编码
	 * 
	 * @param memberid
	 * @param username
	 * @param password
	 * @return
	 */
    public static String encodePasswordCookie(String memberid, String username, String password) {
        StringBuffer buf = new StringBuffer();
        if ((memberid != null) && (username != null) && (password != null)) {
            byte[] bytes = (memberid + ENCODE_DELIMETER + username + ENCODE_DELIMETER + password).getBytes();
            int b;
            for (int n = 0; n < bytes.length; n++) {
                b = bytes[n] ^ (ENCODE_XORMASK + n);
                buf.append((char) (ENCODE_CHAR_OFFSET1 + (b & 0x0F)));
                buf.append((char) (ENCODE_CHAR_OFFSET2 + ((b >> 4) & 0x0F)));
            }
        }
        return buf.toString();
    }

    /**
	 * 返回一个uuid字符串
	 * 
	 * @return
	 */
    public static String getUUID() {
        return LUUID.make();
    }

    /**
	 * 返回一个guid字符串
	 * 
	 * @return
	 */
    public static String getGUID() {
        return new LGUID().toString();
    }
}
