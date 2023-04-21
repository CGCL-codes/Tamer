package com.jaeksoft.searchlib.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.regex.Pattern;
import com.jaeksoft.searchlib.schema.FieldValueItem;

public class Md5Spliter {

    private static final String generateHash(byte[] result) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < result.length; i++) {
            String s = Integer.toHexString(result[i]);
            int length = s.length();
            if (length >= 2) {
                sb.append(s.substring(length - 2, length));
            } else {
                sb.append("0");
                sb.append(s);
            }
        }
        return sb.toString();
    }

    public static final String getMD5Hash(byte[] data, int offset, int length) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return generateHash(md5.digest());
    }

    public static final String getMD5Hash(String str) throws NoSuchAlgorithmException {
        byte[] bytes = str.getBytes();
        return getMD5Hash(bytes, 0, bytes.length);
    }

    public static final String getMD5Hash(String data, String key) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data.getBytes());
        return generateHash(md5.digest(key.getBytes()));
    }

    private Pattern keyPattern;

    public Md5Spliter(String keyPattern) {
        if (keyPattern != null) this.keyPattern = Pattern.compile(keyPattern);
    }

    public boolean acceptAnyKey(Collection<FieldValueItem> keys) throws NoSuchAlgorithmException {
        if (keyPattern == null) return true;
        for (FieldValueItem key : keys) if (keyPattern.matcher(getMD5Hash(key.getValue(), "gisi")).matches()) return true;
        return false;
    }

    protected Pattern getPattern() {
        return keyPattern;
    }
}
