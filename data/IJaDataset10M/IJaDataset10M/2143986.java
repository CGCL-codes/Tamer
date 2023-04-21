package org.exist.http.sleepy.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;
import org.exist.xmldb.XmldbURI;

/**
 *
 * @author Adam Retter <adam.retter@googlemail.com>
 */
public class URIOrderedHTTPMethodList<K, V extends HTTPRESTfulXQueryService> {

    private static final HTTPRESTfulXQueryServiceURIComparator httpRESTfulXQueryServiceURIComparator = new HTTPRESTfulXQueryServiceURIComparator();

    private final Map<K, List<V>> uriOrderedMethodList = new HashMap<K, List<V>>();

    private final ReentrantLock listLock = new ReentrantLock();

    public V put(K key, V value) {
        try {
            listLock.lock();
            List<V> list = uriOrderedMethodList.get(key);
            if (list == null) {
                list = new ArrayList<V>();
            }
            V oldValue = null;
            int oldIndex = list.indexOf(value);
            if (oldIndex > -1) {
                oldValue = list.remove(oldIndex);
            }
            list.add(value);
            Collections.sort(list, httpRESTfulXQueryServiceURIComparator);
            uriOrderedMethodList.put(key, list);
            return oldValue;
        } finally {
            listLock.unlock();
        }
    }

    public void deregister(XmldbURI uri) {
        try {
            listLock.lock();
            for (Entry<K, List<V>> entry : uriOrderedMethodList.entrySet()) {
                final List<V> servicesToRemove = new ArrayList<V>();
                final List<V> serviceList = entry.getValue();
                for (V service : serviceList) {
                    if (service.getXQueryLocation().equals(uri)) {
                        CompiledHTTPRESTfulXQueryCache.getInstance().removeService(service);
                        servicesToRemove.add(service);
                    }
                }
                for (V serviceToRemove : servicesToRemove) {
                    serviceList.remove(serviceToRemove);
                }
                uriOrderedMethodList.put(entry.getKey(), serviceList);
            }
        } finally {
            listLock.unlock();
        }
    }

    public V get(K key, String path) {
        try {
            listLock.lock();
            final List<V> list = uriOrderedMethodList.get(key);
            if (list == null) {
                return null;
            }
            for (V value : list) {
                if (value.canService(path)) {
                    return value;
                }
            }
            return null;
        } finally {
            listLock.unlock();
        }
    }
}
