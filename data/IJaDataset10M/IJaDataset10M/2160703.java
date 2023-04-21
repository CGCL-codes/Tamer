package org.dml.tools;

import java.util.HashMap;

/**
 * @param <K>
 *            key
 * @param <V>
 *            value
 * 
 *            NOTE: do not rename classes starting with ThreadLocal* because they are set in RecursionDetector.aj aspect
 *            as excluded from call tracing and stuff - else they infinite loop
 */
public class ThreadLocalHashMap<K, V> extends ThreadLocal<HashMap<K, V>> {

    public ThreadLocalHashMap() {
        super();
    }

    @Override
    protected synchronized HashMap<K, V> initialValue() {
        return new HashMap<K, V>();
    }
}
