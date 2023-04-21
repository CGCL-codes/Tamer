package org.impalaframework.service.contribution;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of a map which contains both local contributions as well as
 * contributions from another map (the external contributions map), typically
 * from the service registry. Read operations will cascade from the local to the
 * external contributions map. For example, <code>get</code> will first look
 * in the local map, then the external map. <code>size()</code> will count the
 * entries of both maps.
 * <p>
 * Write operations operate only on the local contributions. For example, if
 * <code>remove</code> is called, and the key is present only in the external
 * contributions map, the key will <b>not</b> be removed!
 */
@SuppressWarnings("unchecked")
public class ContributionMap implements Map {

    private Map localContributions = new ConcurrentHashMap();

    private BaseServiceRegistryMap externalContributions;

    public void clear() {
        this.localContributions.clear();
    }

    public boolean containsKey(Object key) {
        boolean hasKey = this.localContributions.containsKey(key);
        if (!hasKey) hasKey = this.externalContributions.containsKey(key);
        return hasKey;
    }

    public boolean containsValue(Object value) {
        boolean hasValue = this.localContributions.containsValue(value);
        if (!hasValue) hasValue = this.externalContributions.containsValue(value);
        return hasValue;
    }

    public Set entrySet() {
        Set localSet = this.localContributions.entrySet();
        Set externalSet = this.externalContributions.entrySet();
        Set allSet = new LinkedHashSet();
        allSet.addAll(localSet);
        allSet.addAll(externalSet);
        return allSet;
    }

    public Object get(Object key) {
        Object value = this.localContributions.get(key);
        if (value == null) {
            value = this.externalContributions.get(key);
        }
        return value;
    }

    public boolean isEmpty() {
        boolean isEmpty = this.localContributions.isEmpty();
        if (isEmpty) {
            isEmpty = this.externalContributions.isEmpty();
        }
        return isEmpty;
    }

    public Set<String> keySet() {
        Set<String> localSet = this.localContributions.keySet();
        Set<String> externalSet = this.externalContributions.keySet();
        Set<String> allSet = new LinkedHashSet<String>();
        allSet.addAll(localSet);
        allSet.addAll(externalSet);
        return allSet;
    }

    public Object put(Object key, Object value) {
        return this.localContributions.put(key, value);
    }

    public void putAll(Map map) {
        this.localContributions.putAll(map);
    }

    public Object remove(Object key) {
        return this.localContributions.remove(key);
    }

    public int size() {
        int localSize = this.localContributions.size();
        int externalSize = this.externalContributions.size();
        return localSize + externalSize;
    }

    public Collection values() {
        Collection localValues = this.localContributions.values();
        Collection externalValues = this.externalContributions.values();
        Collection allValues = new ArrayList();
        allValues.addAll(localValues);
        allValues.addAll(externalValues);
        return allValues;
    }

    public void setExternalContributions(BaseServiceRegistryMap externalContributions) {
        this.externalContributions = externalContributions;
    }

    public void setLocalContributions(Map localContributions) {
        this.localContributions = localContributions;
    }

    @Override
    public java.lang.String toString() {
        StringBuffer sb = new StringBuffer();
        String localString = (String) localContributions.toString();
        String externalString = (String) externalContributions.toString();
        sb.append("Local contributions: ").append(localString).append(", external contributions: ").append(externalString);
        return sb.toString();
    }
}
