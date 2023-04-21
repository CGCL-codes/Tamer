package org.goodjava.shift.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Matt created on Aug 14, 2005
 */
public abstract class ShiftMap {

    private HashMap map = new HashMap();

    /**
     * Returns the entries in the map as an Iterator.
     * @return
     */
    public final Iterator entryIterator() {
        List entries = new ArrayList();
        for (Iterator iter = map.keySet().iterator(); iter.hasNext(); ) {
            entries.add(map.get(iter.next()));
        }
        return entries.iterator();
    }

    /**
     * Returns the map.
     * @return
     */
    protected final Map getMap() {
        return map;
    }

    /**
     * Returns the size of the map.
     * @return
     */
    public final int size() {
        return map.size();
    }

    /**
     * Returns true if the map contains the key, false if it doesn't.
     * @param key
     * @return
     */
    public final boolean containsKey(final String key) {
        return map.containsKey(key);
    }
}
