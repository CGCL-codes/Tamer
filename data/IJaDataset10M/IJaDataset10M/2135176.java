package net.spatula.tally_ho.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author spatula
 * 
 */
public class TextUtils {

    public static String javaDateTimeFormat = "MMM. dd, yyyy kk:mm";

    public static String gibberish() {
        return gibberish(6, 4);
    }

    public static String gibberish(int min, int slop) {
        final String[] in = { "b", "bl", "br", "c", "ch", "cl", "cr", "d", "dr", "f", "fl", "fr", "g", "gl", "gr", "h", "j", "k", "kl", "kn", "kr", "l", "m", "ml", "n", "p", "ph", "pl", "pr", "r", "s", "sc", "sh", "sk", "sl", "sm", "sn", "sp", "st", "sw", "t", "th", "tr", "v", "vr", "w", "wh", "wr", "z" };
        final String[] nt = { "b", "bl", "br", "c", "ch", "cl", "cr", "d", "dr", "f", "fl", "fr", "g", "gl", "gr", "h", "j", "k", "kl", "kn", "kr", "l", "ll", "m", "ml", "mr", "n", "nl", "nr", "p", "ph", "pl", "pr", "r", "rl", "rr", "s", "sc", "sh", "sk", "sl", "sm", "sn", "sp", "sr", "ss", "st", "sw", "t", "th", "tr", "v", "vl", "vr", "w", "wh", "wr", "z" };
        final String[] t = { "b", "c", "d", "f", "g", "j", "k", "l", "m", "n", "p", "r", "s", "t", "v", "w", "z", "lm", "rm", "sh", "sk", "sm", "sp", "st" };
        final String[] v = { "a", "e", "ee", "i", "o", "oo", "u", "y", "ai", "au", "ea", "eu", "ia", "io", "oa", "oi", "ou" };
        int len = ((int) (Math.random() * slop)) + min;
        String terminal = t[(int) (Math.random() * t.length)];
        len -= terminal.length();
        int ntlen = nt.length;
        int vlen = v.length;
        StringBuffer result = new StringBuffer();
        result.append(in[(int) (Math.random() * in.length)]);
        result.append(v[(int) (Math.random() * vlen)]);
        while (result.length() < len) {
            result.append(nt[(int) (Math.random() * ntlen)]);
            result.append(v[(int) (Math.random() * vlen)]);
        }
        result.append(terminal);
        return result.toString();
    }

    public static String calculateMD5(byte[] digest) {
        StringBuffer buffer = new StringBuffer(32);
        for (byte b : digest) {
            int intValue = ((int) b) & 0xff;
            if (intValue < 0x10) {
                buffer.append("0");
            }
            buffer.append(Integer.toHexString(intValue));
        }
        return buffer.toString();
    }

    public static String getCurrentDateTime() {
        return formatDate(new Date());
    }

    public static String formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(javaDateTimeFormat);
        return format.format(date) + " UTC";
    }

    public static String makeGetterName(String field) {
        return makeAccessorName(field, "get");
    }

    private static String makeAccessorName(String field, String accessor) {
        String upCaseFirstLetter = field.substring(0, 1).toUpperCase();
        String rest = field.length() > 1 ? field.substring(1) : "";
        return accessor + upCaseFirstLetter + rest;
    }

    public static String makeSetterName(String field) {
        return makeAccessorName(field, "set");
    }
}
