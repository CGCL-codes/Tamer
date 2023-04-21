package fr.soleil.util.serialized.serializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Store Object which cannot be serialized or rebuild such as Corba's ANY object
 * @author BARBA-ROSSA
 *
 */
public class OptimizedWebObjectStorage implements IWebObjectStorage {

    private String m_strSessionID = null;

    private HashMap<String, Object> m_map = null;

    private HashMap<String, Object> m_previousMap = null;

    private long counter = 0;

    private int m_map_size_limit = 150;

    public OptimizedWebObjectStorage(String sessionID) {
        m_map = new HashMap<String, Object>();
        counter = 0;
        m_strSessionID = sessionID;
    }

    public String addObject(Object object) {
        String objectID = getObjectID();
        m_map.put(objectID, object);
        clearMap();
        return objectID;
    }

    /**
 * If we have reach the map size limit, we copy the map in the old map. The olb one will be erase. 
 *
 */
    private synchronized void clearMap() {
        if (m_map.size() >= m_map_size_limit) {
            if (m_previousMap != null) m_previousMap.clear();
            m_previousMap = m_map;
            m_map = new HashMap<String, Object>();
        }
    }

    public void addObject(String objectID, Object object) {
        m_map.put(objectID, object);
        clearMap();
    }

    public Object getObject(String objectID) {
        if (m_map.containsKey(objectID)) return m_map.get(objectID);
        if (m_previousMap != null) if (m_previousMap.containsKey(objectID)) return m_previousMap.get(objectID);
        return null;
    }

    public void removeObject(String objectID) {
        if (m_map.containsKey(objectID)) m_map.remove(objectID);
        if (m_previousMap != null) if (m_previousMap.containsKey(objectID)) m_previousMap.remove(objectID);
    }

    public boolean isObjectID(String objectID) {
        if (m_map.containsKey(objectID)) return true;
        if (m_previousMap != null) if (m_previousMap.containsKey(objectID)) return true;
        return false;
    }

    private synchronized String getObjectID() {
        counter++;
        return Long.toString(counter) + Long.toString(System.currentTimeMillis());
    }

    public HashMap<String, Object> getMap() {
        return m_map;
    }

    public void setMap(HashMap<String, Object> map) {
        m_map = map;
    }

    protected void finalize() throws Throwable {
        System.out.println("WebObjectStorage.remove : " + m_strSessionID);
        super.finalize();
    }

    public String getSessionID() {
        return m_strSessionID;
    }

    public void setSessionID(String sessionID) {
        m_strSessionID = sessionID;
    }

    public int getMap_size_limit() {
        return m_map_size_limit;
    }

    public void setMap_size_limit(int m_map_size_limit) {
        this.m_map_size_limit = m_map_size_limit;
    }
}
