package com.maicuole.framework.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 * A RAM verison cache implentation
 * @author huan
 *
 */
public class RamCache implements Cache {

    private Map<String, Object> map = new HashMap<String, Object>();

    private List<String> configs;

    public Object get(String key) throws CacheException {
        return map.get(key);
    }

    public void put(String key, Object value) throws CacheException {
        map.put(key, value);
    }

    public boolean supports(String key) {
        for (String configKey : configs) {
            if (StringUtils.equals(key, configKey)) {
                return true;
            }
        }
        return false;
    }

    public void setConfigs(List<String> configs) {
        this.configs = configs;
    }
}
