package de.ios.framework.db2;

import de.ios.framework.basic.*;
import java.util.*;

/**
 * This class implememnts a cache for DBObjects.
 * @see DBObjectServer
 */
public final class DBCache {

    /**
   * Constructor
   */
    public DBCache(int size) {
        super();
        maxCapacity = size;
        cacheActive = size > 0;
    }

    /**
   * Get the cache-size.
   */
    public int getCacheSize() {
        return maxCapacity;
    }

    /**
   * Activates caching.
   */
    public void setActive(boolean active) {
        cacheActive = active;
        clear();
    }

    /**
   * Is caching active?
   */
    public boolean isActive() {
        return cacheActive;
    }

    /**
   * Clears the complete cache.
   */
    public synchronized void clear() {
        Debug.println(Debug.INFO, this, "Clearing cache....");
        rl.removeAllElements();
        hs.clear();
    }

    /**
   * Get one object from cache. If the object is actualy not in cache,
   * null will be returned,
   * @param oid   Object-id of the object.
   * @param table Table of the object.
   */
    public synchronized DBObject get(long oid, int classId) {
        if (cacheActive) {
            tempKey.setObject(oid, classId);
            RingElement re = (RingElement) hs.get(tempKey);
            if (re != null) {
                DBObject obj = ((DBObjectHashCode) re.getObject()).obj;
                if (obj != null) obj = (DBObject) obj.clone();
                return obj;
            }
        }
        return null;
    }

    /**
   * Insert one DBObject in cache.
   */
    public synchronized void put(DBObject obj, int classId) {
        if (!cacheActive) return;
        obj = (DBObject) obj.clone();
        DBObjectHashCode dhc = new DBObjectHashCode(obj, classId);
        RingElement re = (RingElement) hs.get(dhc);
        if (re != null) {
            rl.insert(re, rl.HEAD);
            re.setObject(dhc);
        } else {
            if (rl.size() >= maxCapacity) {
                re = rl.moveTailToHead();
                hs.remove(re.getObject());
                re.setObject(dhc);
            } else rl.insert(re = new RingElement(dhc), rl.HEAD);
        }
        hs.put(dhc, re);
    }

    /**
   * Removes all objects of one class.
   */
    public void removeByClass(int classId) {
        if (!cacheActive) return;
        if (Debug.DBG) Debug.println(Debug.INFO, this, "removing all object with classid=" + classId);
        Enumeration e = hs.keys();
        RingElement re;
        DBObjectHashCode dhc;
        int c = 0;
        while (e.hasMoreElements()) {
            dhc = (DBObjectHashCode) e.nextElement();
            if (dhc.classId == classId) {
                re = (RingElement) hs.get(dhc);
                hs.remove(dhc);
                rl.remove(re);
                c++;
            }
        }
        if (Debug.DBG) Debug.println(Debug.INFO, this, "" + c + " objects with classid=" + classId + " removed from cache.");
    }

    /**
   * Removes one object.
   */
    public void remove(DBObject obj, int classId) {
        if (!cacheActive) return;
        if (obj != null) {
            tempKey.setObject(obj.getOId(), classId);
            RingElement re = (RingElement) hs.get(tempKey);
            if (re != null) {
                hs.remove(re.getObject());
                rl.remove(re);
            }
        }
    }

    Hashtable hs = new Hashtable(1031);

    RingList rl = new RingList();

    protected int maxCapacity = 100;

    boolean cacheActive = true;

    /** Temporary used key-value.*/
    DBObjectHashCode tempKey = new DBObjectHashCode(new DBObject(), -1);
}

/**
 * Helperclass for managing cached DBObjects.
 */
final class DBObjectHashCode {

    public int classId;

    public long id;

    public DBObject obj;

    public DBObjectHashCode(DBObject _obj, int _classId) {
        obj = _obj;
        classId = _classId;
        id = obj.getOId();
    }

    public void setObject(long _id, int _classId) {
        classId = _classId;
        id = _id;
    }

    public int hashCode() {
        return (int) id;
    }

    public boolean equals(Object obj) {
        DBObjectHashCode oc = (DBObjectHashCode) obj;
        return (oc.id == id) && (oc.classId == classId);
    }
}
