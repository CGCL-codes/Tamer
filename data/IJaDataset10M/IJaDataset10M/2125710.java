package com.idna.dm.util.cache;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.Map.Entry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.idna.dm.util.reflection.StackTraceReporter;

/**
 * Static access class that can be used as a cache which will automagically
 * clear unused data. This type of cache differs from a normal cache in that it
 * will remove objects that no longer have references to them in memory. This
 * differs to a typical cache in that a typical cache will still continue to
 * store unused data for a configurable period of time. This cache will not do
 * that. Use this utility class to store anything you like with a temporary
 * life-cycle.
 * <p>
 * a {@link WeakReference} is used internally to wrap the key which stops the
 * hashmap from keeping a strong reference to it's keys. When all other
 * references to the key object no longer exist in memory the garbage collector
 * will remove the key because the WeakHashMap does not hold a strong reference
 * to the underlying object. This will in turn cause the weakHashMap to also
 * remove the key and value from the map, thus automatically clearing the cache.
 * <p>
 * NOTE-1: be careful what object you use as a key. Singleton objects (such as
 * {@link Class}) do not make good weak keys they tend to hang around in memory.
 * Use a key which will have a predictable lifecycle in memory, and that will
 * eventually expire.
 * <p>
 * NOTE-2: If a value holds a reference to the key, the key-value entry will
 * never get cleared from the WeakHashMap.
 * 
 * @see GlobalDataCache
 * @see WeakReference
 * @see WeakHashMap
 * 
 * @author gawain.hammond
 * 
 */
public class ReusableDataCache<T> {

    private final Log logger = LogFactory.getLog(this.getClass());

    private Map<Object, T> cache = Collections.synchronizedMap(new WeakHashMap<Object, T>());

    /**
	 * Analogous to the {@link Map#get(Object)} method
	 * 
	 * @param key
	 * @return
	 */
    public T getData(Object key) {
        T value = cache.get(key);
        StackTraceElement ele = StackTraceReporter.getCallingClass();
        String caller = "Caller: " + ele.getClassName() + "." + ele.getMethodName();
        logger.trace("Getting data for key='" + key + "': value='" + value + "'. Objects in cache: " + cache.size() + ". " + caller);
        return value;
    }

    /**
	 * Analogous to the {@link Map#put(Object, Object)} method
	 * 
	 * @param key
	 * @param value
	 */
    public void putData(Object key, T value) {
        cache.put(key, value);
        StackTraceElement ele = StackTraceReporter.getCallingClass();
        String caller = "Caller: " + ele.getClassName() + "." + ele.getMethodName();
        logger.trace("Putting data for key='" + key + "': value='" + value + "'. Objects in cache: " + cache.size() + ". " + caller);
    }

    public int size() {
        return cache.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("@" + System.identityHashCode(this) + ": ");
        sb.append("{");
        Map<Object, T> cacheCopy = new HashMap<Object, T>(cache);
        for (Entry<Object, T> entry : cacheCopy.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append(", ");
        }
        sb.append(" }");
        return sb.toString();
    }
}
