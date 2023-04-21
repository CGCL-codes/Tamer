package org.dualr.litelog.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class LiteLogUtil {

    private static LiteLogUtil _instance = null;

    public static LiteLogUtil getInstance() {
        if (_instance == null) {
            _instance = new LiteLogUtil();
        }
        return _instance;
    }

    public String formateDate(Date date, String formate) {
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        return sdf.format(date);
    }

    public String[] splitByComma(String str) {
        String[] strArray = str.split(",");
        return strArray;
    }

    public String subString(String content, int num) {
        int length = content.length();
        if (length < num) {
            return content;
        } else {
            return content.substring(0, num) + "...";
        }
    }

    public String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        String mdStr = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i])); else md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        mdStr = md5StrBuff.toString();
        return mdStr;
    }

    public String getPropsValue(String filePath, String key) {
        String value = null;
        InputStream in = this.getClass().getResourceAsStream(filePath);
        Properties props = new Properties();
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        value = props.getProperty(key);
        return value;
    }

    public void setPropsValue(String filePath, String key, String value) {
        InputStream in = this.getClass().getResourceAsStream(filePath);
        Properties props = new Properties();
        try {
            props.load(in);
            props.setProperty(key, value);
            props.put(key, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPropsValue(String key) {
        return this.getPropsValue("/litelog.properties", key);
    }
}
