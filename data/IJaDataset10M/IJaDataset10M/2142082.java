package org.learnaholic.application.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 */
public class MemoryItemsImpl implements MemoryItems {

    private final Map<String, MemoryItem> map = new LinkedHashMap<String, MemoryItem>();

    private final List<MemoryListener> listeners = new ArrayList<MemoryListener>();

    public MemoryItemsImpl() {
    }

    public MemoryItemsImpl(String key, MemoryItem firstItem) {
        map.put(key, firstItem);
    }

    /**
	 * Gets a sub map from a set of keys.
	 * @param keys the set of keys.
	 * @return a sub map of the current map.
	 */
    public MemoryItems getSubMap(Set<String> keys) {
        MemoryItems ret = new MemoryItemsImpl();
        for (Iterator<String> iterator = keys.iterator(); iterator.hasNext(); ) {
            String key = iterator.next();
            ret.put(key, map.get(key));
        }
        return ret;
    }

    public List<String> getAllPropertyValues(String propertyName) {
        List<String> ret = new ArrayList<String>();
        for (Iterator<MemoryItem> iterator = iterator(); iterator.hasNext(); ) {
            ret.add(iterator.next().getListItem().getProperty(propertyName).getValue());
        }
        return ret;
    }

    public void put(String key, MemoryItem item) {
        map.put(key, item);
        notifyAdded(new MemoryItemsImpl(key, item));
    }

    public void putAll(MemoryItems items) {
        map.putAll(((MemoryItemsImpl) items).map);
        notifyAdded(items);
    }

    public MemoryItem get(String key) {
        return map.get(key);
    }

    public MemoryItem remove(String key) {
        MemoryItem ret = map.remove(key);
        if (null != ret) {
            notifyRemoved(new MemoryItemsImpl(key, ret));
        }
        return ret;
    }

    public void clear() {
        MemoryItems removedItems = new MemoryItemsImpl();
        removedItems.putAll(this);
        map.clear();
        notifyRemoved(removedItems);
    }

    /**
	 * @param l
	 */
    public void addMemoryListener(MemoryListener l) {
        listeners.add(l);
    }

    /**
	 * @param l
	 */
    public void removeMemoryListener(MemoryListener l) {
        listeners.remove(l);
    }

    private void notifyAdded(MemoryItems addedItems) {
        MemoryItemsEvent evt = new MemoryItemsEvent(addedItems);
        synchronized (listeners) {
            for (Iterator<MemoryListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
                iterator.next().itemsAdded(evt);
            }
        }
    }

    private void notifyRemoved(MemoryItems addedItems) {
        MemoryItemsEvent evt = new MemoryItemsEvent(addedItems);
        synchronized (listeners) {
            for (Iterator<MemoryListener> iterator = listeners.iterator(); iterator.hasNext(); ) {
                iterator.next().itemsRemoved(evt);
            }
        }
    }

    /**
	 * @return
	 */
    public Iterator<MemoryItem> iterator() {
        return map.values().iterator();
    }
}
