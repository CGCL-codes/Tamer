package org.tolven.core.util;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.tolven.core.TolvenPropertiesLocal;

public class TolvenPropertiesMap implements Map<String, String> {

    private String localAddr;

    private TolvenPropertiesLocal propertiesBean;

    public TolvenPropertiesMap(String localAddr, TolvenPropertiesLocal propertiesBean) {
        this.localAddr = localAddr;
        this.propertiesBean = propertiesBean;
    }

    public String get(Object key) {
        String rslt;
        String ipkey = key + "." + localAddr;
        rslt = propertiesBean.getProperty(ipkey);
        if (rslt == null) {
            rslt = propertiesBean.getProperty(key.toString());
        }
        return rslt;
    }

    @Override
    public void clear() {
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Set<java.util.Map.Entry<String, String>> entrySet() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public String put(String key, String value) {
        return null;
    }

    @Override
    public void putAll(Map<? extends String, ? extends String> m) {
    }

    @Override
    public String remove(Object key) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Collection<String> values() {
        return null;
    }
}
