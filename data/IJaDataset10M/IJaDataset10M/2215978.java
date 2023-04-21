package org.fantasy.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 参数Map
 * 
 * @author: 王文成
 * @version: 1.0
 * @since 2009-3-24
 */
public class ParamMap<K, V> implements Map<K, V> {

    private static final Log log = LogFactory.getLog(ResultMap.class);

    private Map<K, V> map;

    /**
	 * 默认 clone map
	 * 
	 * @param map
	 */
    public ParamMap(Map<K, V> map) {
        this.map = map;
    }

    /**
	 * 删除Key
	 * 
	 * @param keys
	 * @return
	 */
    public Object[] removes(Object[] keys) {
        List<Object> removedKeys = new ArrayList<Object>();
        for (int i = 0; i < keys.length; i++) {
            Object key = keys[i];
            if (this.map.containsKey(key)) {
                removedKeys.add(this.map.remove(key));
            }
        }
        return removedKeys.toArray();
    }

    /**
	 * 转换为HashMap
	 * 
	 * @return
	 */
    public HashMap<K, V> toHashMap() {
        return new HashMap<K, V>(this.map);
    }

    /**
	 * 获取字符串
	 * 
	 * @param key
	 * @return
	 */
    public String getString(Object key) {
        Object value = map.get(key);
        return StringUtil.asString(value);
    }

    /**
	 * 获取字符串
	 * 
	 * @param key
	 * @param defValue
	 * @return
	 */
    public String getString(Object key, String defValue) {
        String value = getString(key);
        if (StringUtil.isValid(value)) {
            return value;
        }
        return defValue;
    }

    /**
	 * 判断数据有效性 object != null && object.toString().length() > 0
	 * 
	 * @param key
	 * @return
	 */
    public boolean isValid(Object key) {
        return getString(key).length() > 0;
    }

    /**
	 * 判断多个数据的有效性
	 * 
	 * @param key
	 * @return
	 */
    public boolean isValid(String[] keys) {
        for (int i = 0; i < keys.length; i++) {
            if (!isValid(keys[i])) {
                return false;
            }
        }
        return true;
    }

    /**
	 * 获取整型数
	 * 
	 * @param key
	 * @return
	 */
    public Integer getInt(K key) {
        return getInt(key, null);
    }

    /**
	 * 获取整型数
	 * 
	 * @param key
	 * @return
	 */
    public Integer getInt(K key, Integer defValue) {
        Number value = getNumber(key);
        return value != null ? value.intValue() : defValue;
    }

    public Long getLong(K key) {
        Number value = getNumber(key);
        return value != null ? value.longValue() : null;
    }

    public Double getDouble(K key) {
        Number value = getNumber(key);
        return value != null ? value.doubleValue() : null;
    }

    public boolean getBoolean(K key) {
        return getString(key).equals(Boolean.TRUE.toString());
    }

    public Number getNumber(K key) {
        Object value = get(key);
        if (value instanceof Number) {
            return (Number) value;
        } else {
            if (StringUtil.isValid(value)) return new Double(StringUtil.asString(value)); else return null;
        }
    }

    /**
	 * 获取日期字符串 以pattern格式化
	 * 
	 * @param key
	 * @return
	 */
    public String getDateString(K key, String pattern) {
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Object value = this.map.get(key);
        return dateFormat.format((Date) value);
    }

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.map.containsKey(value);
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return this.map.entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        return this.map.equals(obj);
    }

    public V get(Object key) {
        return this.map.get(key);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public Set<K> keySet() {
        return this.map.keySet();
    }

    public V put(K key, V value) {
        return this.map.put(key, value);
    }

    public void putAll(Map<? extends K, ? extends V> t) {
        this.map.putAll(t);
    }

    public V remove(Object key) {
        return this.map.remove(key);
    }

    public int size() {
        return this.map.size();
    }

    @Override
    public String toString() {
        return this.map.toString();
    }

    public Collection<V> values() {
        return this.map.values();
    }

    public static void main(String args[]) {
        Object s = "";
        log.debug(String.valueOf(CharSequence.class.isInstance(s)));
    }
}
