package com.corratech.ws.sugarcrm.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Properties;

public class SugarUtil {

    private static Properties props = null;

    private SugarUtil() {
    }

    public static synchronized Properties loadProperties() {
        if (props == null) {
            props = new Properties();
            InputStream is = SugarUtil.class.getClassLoader().getResourceAsStream("sugarcrm.properties");
            try {
                props.load(is);
                is.close();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return props;
    }

    public static String stringToMD5(String text) {
        String md5 = null;
        try {
            StringBuffer code = new StringBuffer();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte bytes[] = text.getBytes();
            byte digest[] = messageDigest.digest(bytes);
            for (int k = 0; k < digest.length; ++k) {
                code.append(Integer.toHexString(0x0100 + (digest[k] & 0x00FF)).substring(1));
            }
            md5 = code.toString();
        } catch (Exception e) {
            System.out.println("Error convert to md5 password.");
        }
        return md5;
    }
}
