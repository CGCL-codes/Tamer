package com.jhyle.sce.entity;

import gnu.trove.map.hash.THashMap;
import java.util.Collections;
import java.util.Map;

public abstract class Entity {

    private static Map<String, Map<String, Entity>> lockedEntities = new THashMap<String, Map<String, Entity>>();

    private static int unknownCounter = 0;

    public abstract String getName();

    public abstract String getType();

    public abstract String getLabel();

    public abstract boolean equals(Object o);

    public static synchronized int getUnknownCount() {
        return ++unknownCounter;
    }

    public static synchronized Map<String, Entity> getLockedBy(String user) {
        Map<String, Entity> map = lockedEntities.get(user);
        if (map == null) {
            return Collections.emptyMap();
        } else {
            return map;
        }
    }

    public static synchronized void removeLockedBy(String user) {
        lockedEntities.remove(user);
    }

    public boolean lock(String user) {
        String entity = getEntityId();
        synchronized (Entity.class) {
            Map<String, Entity> map = lockedEntities.get(user);
            if (map == null) {
                map = new THashMap<String, Entity>();
                lockedEntities.put(user, map);
            }
            if (map.containsKey(entity)) {
                return false;
            } else {
                map.put(entity, this);
                return true;
            }
        }
    }

    public String isLocked() {
        String entity = getEntityId();
        synchronized (Entity.class) {
            for (String username : lockedEntities.keySet()) {
                if (lockedEntities.get(username).containsKey(entity)) {
                    return username;
                }
            }
        }
        return null;
    }

    public boolean unlock() {
        String entity = getEntityId();
        synchronized (Entity.class) {
            for (Map<String, Entity> map : lockedEntities.values()) {
                if (map.containsKey(entity)) {
                    map.remove(entity);
                    return true;
                }
            }
        }
        return false;
    }

    private String getEntityId() {
        return getType() + getName();
    }
}
