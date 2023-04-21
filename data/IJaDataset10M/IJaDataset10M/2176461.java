package org.wvc.widgets.impl;

import java.util.HashMap;
import java.util.Map;

/**
 * @author julian
 *
 */
public class WebWidgetStateImpl implements org.wvc.widgets.WebWidgetState {

    private Map<String, Object> properties = new HashMap<String, Object>();

    public Object getProperty(String name) {
        return this.properties.get(name);
    }

    public void setProperty(String name, Object value) {
        this.properties.put(name, value);
    }
}
