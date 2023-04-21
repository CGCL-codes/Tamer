package de.highbyte_le.weberknecht.rpx;

import java.util.HashMap;
import java.util.Map;

/**
 * authentication information received via RPX
 * 
 * @author pmairif
 */
public class RpxAuthInfo {

    private Map<String, String> values = new HashMap<String, String>();

    public void put(String key, String value) {
        values.put(key, value);
    }

    public String getValue(String key) {
        return values.get(key);
    }

    public boolean hasValue(String key) {
        return values.containsKey(key);
    }

    public Map<String, String> getValues() {
        return this.values;
    }
}
