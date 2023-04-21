package com.palmind.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class StringUtil {

    /**
	 * 将字符串转换成布尔值
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 字符串对应的boolean值
	 */
    public static final boolean Str2Bool(String str) {
        if (str == null) return false;
        if (str.equals("true")) {
            return true;
        } else if (str.equals("false")) {
            return false;
        } else {
            str = str.toLowerCase();
            return str.equals("true");
        }
    }

    /**
	 * 将字符串转换成整型
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 字符串对应的Integer值
	 */
    public static int Str2Int(String str) {
        if (str == null) return 0;
        int integer = 0;
        try {
            integer = str.charAt(0) == '#' ? Integer.parseInt(str.substring(1), 16) : Integer.parseInt(str);
        } catch (Exception e) {
            integer = 0;
        }
        return integer;
    }

    /**
	 * 将字符串转换成长整型
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 字符串对应的Long值
	 */
    public static long String2Long(String str) {
        if (str == null) return 0L;
        long l = 0L;
        try {
            l = Long.parseLong(str);
        } catch (Exception e) {
            return 0L;
        }
        return l;
    }

    /**
	 * 将字符串转换成byte数组
	 * 
	 * @param str
	 *            要转换的字符串
	 * @return 字符串对应的byte数组
	 */
    public static byte[] String2ByteArray(String str) {
        if (str == null) return null;
        byte[] bytes = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF(str);
            bytes = baos.toByteArray();
        } catch (IOException ex) {
        } finally {
            try {
                baos.close();
                dos.close();
            } catch (IOException ex1) {
            }
            baos = null;
            dos = null;
        }
        return bytes;
    }

    public static final String string2Code(String s) {
        if (s == null) return s;
        StringBuffer sb = new StringBuffer();
        int i = 0;
        char ch = 0;
        do {
            ch = s.charAt(i);
            if (ch > 255) {
                sb.append("&#x");
                sb.append(Integer.toHexString(ch));
                sb.append(';');
            } else {
                sb.append(ch);
            }
            i++;
        } while (i < s.length());
        return sb.toString();
    }
}
