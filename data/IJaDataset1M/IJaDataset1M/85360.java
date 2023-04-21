package com.fourspaces.scratch.widget;

import java.util.HashMap;
import java.util.Map;

public class WidgetConfig {

    private Map<String, String> map = new HashMap<String, String>();

    public WidgetConfig(com.fourspaces.scratch.widget.annotation.WidgetConfig[] annotations) {
        for (com.fourspaces.scratch.widget.annotation.WidgetConfig wc : annotations) {
            if (wc != null) {
                map.put(wc.name(), wc.value());
            }
        }
    }

    public String getConfigValue(String key) {
        return map.get(key);
    }

    public Map<String, String> getConfigMap() {
        return map;
    }
}
