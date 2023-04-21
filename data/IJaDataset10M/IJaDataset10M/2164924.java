package com.patientis.framework.controls;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import com.patientis.client.state.State;
import com.patientis.framework.logging.Log;

/**
 * ISMemory tracks control creation to simplify memory management.
 *
 * <br/>  
 */
public class ISMemory {

    /**
	 * Data for the memory object
	 */
    private class ObjectData {

        public String className = null;

        public long applicationViewId = 0L;

        public String trace = null;
    }

    /**
	 * Map of all created objects hashcode, class name
	 */
    private java.util.Map<Integer, ObjectData> objectMap = new Hashtable<Integer, ObjectData>(1024);

    /**
	 * Count of all classes created classname, count
	 */
    private java.util.Map<String, Integer> objectCount = new Hashtable<String, Integer>(1024);

    /**
	 * Singleton
	 */
    private static ISMemory memory = null;

    /**
	 * Instantiation lock
	 */
    private static Object lock = new Object();

    /**
	 * Singleton
	 */
    private ISMemory() {
    }

    /**
	 * 
	 */
    public void clear() {
        objectMap.clear();
        objectCount.clear();
        State.setDebug(true);
    }

    /**
	 * Singleton
	 * 
	 * @return single instance
	 */
    public static synchronized ISMemory getInstance() {
        if (memory == null) {
            synchronized (lock) {
                if (memory == null) {
                    memory = new ISMemory();
                }
            }
        }
        return memory;
    }

    /**
	 * Add an object to the map
	 * 
	 * @param o
	 */
    public void add(Object o) {
        if (o != null && State.isDebug()) {
            ObjectData data = new ObjectData();
            data.className = o.getClass().getSimpleName();
            objectMap.put(o.hashCode(), data);
            incrementCount(objectCount, o.getClass().getSimpleName());
        }
    }

    /**
	 * Store the name of the object or increment the class count
	 * 
	 * @param objectCount map of Object simple name, count
	 * @param o Object to get simple name from 
	 */
    public static void incrementCount(Map<String, Integer> objectCount, String name) {
        Integer count = 0;
        if (objectCount.containsKey(name)) {
            count = objectCount.get(name);
        }
        count++;
        objectCount.put(name, count);
    }

    /**
	 * Remove from active list of objects
	 * 
	 * @param o
	 */
    public void remove(Object o) {
        if (o != null && State.isDebug()) {
            objectMap.remove(o.hashCode());
        }
    }

    /**
	 * Store the view this object was created under 
	 * 
	 * @param o
	 * @param applicationViewId
	 */
    public void updateApplicationControlId(Object o, long applicationViewId) {
        if (State.isDebug() || State.getUserId() < 50000000) {
            if (o != null && objectMap.containsKey(o.hashCode())) {
                ObjectData data = objectMap.get(o.hashCode());
                data.applicationViewId = applicationViewId;
                objectMap.put(o.hashCode(), data);
            } else if (o != null) {
                ObjectData data = new ObjectData();
                data.applicationViewId = applicationViewId;
                data.className = o.getClass().getSimpleName();
                objectMap.put(o.hashCode(), data);
            }
        }
    }

    /**
	 * Get the application control id for this component 
	 * 
	 * @param o
	 * @param methodId
	 */
    public long getApplicationControlId(Object o) {
        if (o != null && objectMap.containsKey(o.hashCode())) {
            ObjectData data = objectMap.get(o.hashCode());
            return data.applicationViewId;
        } else {
            return 0L;
        }
    }

    /**
	 * Print the memory information
	 */
    public void printLog() {
        Log.debug("Objects in memory");
        java.util.Map<String, Integer> activeCount = new Hashtable<String, Integer>();
        List<Integer> keys = new ArrayList<Integer>();
        keys.addAll(objectMap.keySet());
        for (Integer key1 : keys) {
            if (objectMap.get(key1) != null) {
                incrementCount(activeCount, objectMap.get(key1).className);
                if (objectMap.get(key1).className.contains("ControlPanel")) {
                    System.out.println(objectMap.get(key1).trace);
                }
            }
        }
        Log.debug("Active objects");
        for (String key1 : activeCount.keySet()) {
            Log.debug(key1 + "\t" + activeCount.get(key1));
        }
        Log.debug("Object counts");
        for (String key1 : objectCount.keySet()) {
            Log.debug(key1 + "\t" + objectCount.get(key1));
        }
        Log.debug("Application Controls in memory");
        java.util.Map<Long, Boolean> viewCount = new java.util.HashMap<Long, Boolean>();
        for (Integer key1 : objectMap.keySet()) {
            long applicationViewId = objectMap.get(key1).applicationViewId;
            if (!(viewCount.containsKey(applicationViewId))) {
                Log.debug(applicationViewId);
                viewCount.put(applicationViewId, new Boolean(true));
            }
        }
    }
}
