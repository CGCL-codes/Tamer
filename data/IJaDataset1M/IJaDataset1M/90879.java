package org.eweb4j.mvc.config;

import java.util.HashMap;
import org.eweb4j.config.Log;
import org.eweb4j.config.LogFactory;

public class ActionClassCache {

    private static Log log = LogFactory.getMVCLogger(ActionClassCache.class);

    private static final HashMap<String, Class<?>> ht = new HashMap<String, Class<?>>();

    public static boolean containsKey(String beanID) {
        return ht.containsKey(beanID);
    }

    public static boolean containsValue(Class<?> clazz) {
        return ht.containsValue(clazz);
    }

    public static void add(String beanID, Class<?> o) {
        if (beanID != null && o != null) {
            if (!ht.containsKey(beanID)) {
                ht.put(beanID, o);
                String info = "ActionClassCache:add...finished..." + beanID;
                log.debug(info);
            }
        }
    }

    public static Class<?> get(String beanID) {
        return ht.get(beanID);
    }

    public static void clear() {
        if (!ht.isEmpty()) {
            ht.clear();
        }
    }
}
