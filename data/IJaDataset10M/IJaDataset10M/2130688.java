package com.sks.utils;

import java.io.IOException;
import java.util.Properties;

public class SiteUrl {

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(SiteUrl.class.getClassLoader().getResourceAsStream("siteurl.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readUrl(String key) {
        return (String) properties.get(key);
    }

    public static String readUrl(String key, Object[] params) {
        String value = (String) properties.get(key);
        if (params == null) {
            return value;
        } else {
            for (int i = 0; i < params.length; i++) {
                value = value.replace("{" + i + "}", params[i].toString());
            }
        }
        return value;
    }
}
