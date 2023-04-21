package org.dml.tools;

import java.util.HashMap;
import org.dml.error.BadCallError;

/**
 * overwrites are not allowed<br>
 * null values are not allowed<br>
 * you can add/get/remove, but once add-ed you must remove it to change the DATA<br>
 * there can be only one K1,K2 pair and it's associated with only one DATA<br>
 * K1 should be the key that's less likely to change as compared with K2<br>
 */
public class TwoKeyHashMap<K1, K2, DATA> {

    private final HashMap<K1, HashMap<K2, DATA>> first = new HashMap<K1, HashMap<K2, DATA>>();

    public TwoKeyHashMap() {
    }

    /**
	 * @param key1
	 * @param key2
	 * @param data
	 * @return
	 * @throws BadCallError
	 *             when overwrite detected
	 */
    public boolean ensure(K1 key1, K2 key2, DATA data) {
        RunTime.assumedNotNull(key1, key2, data);
        DATA old = this.get(key1, key2);
        boolean existed = (null != old);
        if (!existed) {
            this.add(key1, key2, data);
            old = this.get(key1, key2);
            RunTime.assumedTrue(old == data);
        } else {
            if (old != data) {
                RunTime.badCall("you wanted to overwrite an older different value");
            }
        }
        return existed;
    }

    public void add(K1 key1, K2 key2, DATA data) {
        RunTime.assumedNotNull(key1, key2, data);
        HashMap<K2, DATA> second = first.get(key1);
        if (null != second) {
            DATA old = second.get(key2);
            if (null != old) {
                if (old != data) {
                    RunTime.badCall("attempted overwrite");
                } else {
                    RunTime.badCall("already exists");
                }
                return;
            }
        } else {
            second = new HashMap<K2, DATA>();
            RunTime.assumedNull(first.put(key1, second));
            RunTime.assumedTrue(first.get(key1) == second);
        }
        RunTime.assumedNull(second.put(key2, data));
        RunTime.assumedTrue(second.get(key2) == data);
    }

    public void remove(K1 key1, K2 key2) {
        RunTime.assumedNotNull(key1, key2);
        HashMap<K2, DATA> second = first.get(key1);
        if (null != second) {
            if (null != second.remove(key2)) {
                return;
            }
        }
        RunTime.bug("requested pair was not found!");
    }

    /**
	 * @param key1
	 * @param key2
	 * @return null if not found, else the DATA
	 */
    public DATA get(K1 key1, K2 key2) {
        RunTime.assumedNotNull(key1, key2);
        HashMap<K2, DATA> second = first.get(key1);
        DATA ret = null;
        if (null != second) {
            ret = second.get(key2);
        }
        return ret;
    }

    /**
	 * 
	 */
    public void clear() {
        first.clear();
    }
}
