package org.open4j.cache;

import java.util.Collection;
import java.util.List;

public interface IOpenCache {

    public void put(String key, Object value);

    public void putAll(String key, Collection<Object> coll);

    public void putAll(String key, Object[] arr);

    public abstract void set(String key, Object value);

    public void clear();

    public void clear(String key);

    public void remove(String key);

    public List<String> keyList();

    public List<Object> valueList();

    public Object getObject(String key);

    public String getString(String key);

    public Integer getInteger(String key);

    public Long getLong(String key);

    public Double getDouble(String key);

    public List<Object> getObjectList(String key);

    public int size();

    public int allSize();
}
