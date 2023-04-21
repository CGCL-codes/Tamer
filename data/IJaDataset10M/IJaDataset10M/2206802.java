package com.tegsoft.tobe.util;

import java.util.Properties;

public class PropertyUtil {

    public static void put(Properties properties, String key, Object value) {
        properties.put(key, Converter.asNotNullString(value));
    }
}
