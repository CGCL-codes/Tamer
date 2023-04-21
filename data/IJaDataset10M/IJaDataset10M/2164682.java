package org.eweb4j.cache;

import java.util.HashMap;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;

/**
 * 全局单件对象缓存
 * 
 * @author cfuture.aw
 * @since v1.a.0
 * 
 */
public class SingleBeanCache {

    private static final HashMap<Object, Object> ht = new HashMap<Object, Object>();

    private static Log log = LogFactory.getConfigLogger(SingleBeanCache.class);

    public static boolean containsKey(String beanID) {
        return ht.containsKey(beanID);
    }

    public static boolean containsKey(Class<?> clazz) {
        return ht.containsKey(clazz);
    }

    public static void add(String beanID, Object o) {
        if (beanID != null && o != null) {
            String info = null;
            if (!ht.containsKey(beanID)) {
                ht.put(beanID, o);
                info = "SingleBeanCache:add...finished..." + o;
                log.debug(info);
            }
        }
    }

    public static void add(Class<?> clazz, Object o) {
        if (!ht.containsKey(clazz)) ht.put(clazz, o);
    }

    public static Object get(String beanID) {
        return ht.get(beanID);
    }

    public static Object get(Class<?> clazz) {
        return ht.get(clazz);
    }

    public static void clear() {
        if (!ht.isEmpty()) ht.clear();
    }
}
