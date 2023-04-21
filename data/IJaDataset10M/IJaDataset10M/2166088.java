package com.jleorz.common;

import java.util.HashMap;
import java.util.Map;

public class ApplicationConfig {

    private Map<String, String> map = new HashMap<String, String>();

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String getConfig(String key) {
        return map.get(key);
    }

    private static ApplicationConfig config;

    private ApplicationConfig() {
    }

    public static synchronized ApplicationConfig getInstance() {
        if (config == null) config = new ApplicationConfig();
        return config;
    }
}
