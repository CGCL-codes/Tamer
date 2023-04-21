package org.slosc.rest.core;

import java.util.Map;
import java.util.HashMap;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 * Date    : Oct 19, 2008
 * @version: 1.0
 */
public class ApplicationContext extends ApplicationConfiguration {

    private Map<String, Object> contextParams = new HashMap<String, Object>();

    public ApplicationContext() {
    }

    public ApplicationContext(ApplicationConfiguration other) {
        super(other);
    }

    public Object get(String key) {
        return contextParams.get(key);
    }

    public void add(String key, Object value) {
        contextParams.put(key, value);
    }
}
