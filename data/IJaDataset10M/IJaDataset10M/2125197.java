package net.sf.balm.common.collections;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * 层级Map，每一个Map都可以具有一个上级Map，如果在当前Map中无法获取的指定的值，则将改请求 转发到上级Map，这个过程是递归的。
 * 
 * @author dz
 */
public class HierarchicalMap extends AbstractMap {

    private HierarchicalMap parent;

    private Map map;

    public HierarchicalMap(final Map map) {
        this(null, map);
    }

    public HierarchicalMap(HierarchicalMap parent, final Map map) {
        this.parent = parent;
        this.map = map;
    }

    public HierarchicalMap getParent() {
        return parent;
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    public void putAll(Map t) {
        map.putAll(t);
    }

    public Object remove(Object key) {
        return map.remove(key);
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

    public void clear() {
        map.clear();
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof HierarchicalMap)) {
            return false;
        }
        return new EqualsBuilder().append(parent, ((HierarchicalMap) o).parent).append(map, ((HierarchicalMap) o).map).isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder().append(map).append(parent).toHashCode();
    }

    public Object getObject(String key) {
        Object result = map.get(key);
        if (result == null && parent != null) {
            result = parent.getObject(key);
        }
        return result;
    }
}
