package com.quickwcm.render;

import java.util.HashMap;
import java.util.Map;

public class SessionContext {

    private Map<String, Object> attributes = new HashMap<String, Object>();

    public void setAttribute(String key, Object value) {
        attributes.put(key, value);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void clear() {
        attributes.clear();
    }
}
