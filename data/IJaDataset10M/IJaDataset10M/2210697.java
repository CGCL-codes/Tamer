package com.onetwork.core.resource;

import java.util.HashMap;
import java.util.Map;

public class Resource {

    private static ThreadLocal<Map<ResourceType, Object>> threadLocal = new ThreadLocal<Map<ResourceType, Object>>() {

        @Override
        protected synchronized Map<ResourceType, Object> initialValue() {
            return new HashMap<ResourceType, Object>();
        }
    };

    public Resource() {
    }

    public static void add(ResourceType key, Object value) {
        Map<ResourceType, Object> resources = threadLocal.get();
        resources.put(key, value);
    }

    public static Object remove(ResourceType key) {
        Map<ResourceType, Object> resources = threadLocal.get();
        return resources.remove(key);
    }

    public static void removeAll() {
        Map<ResourceType, Object> resources = threadLocal.get();
        resources.clear();
    }

    public static <T> T get(ResourceType key) {
        Map<ResourceType, Object> resources = threadLocal.get();
        return (T) resources.get(key);
    }
}
