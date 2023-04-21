package model.data;

import java.util.HashMap;
import java.util.Iterator;

public class MapMap implements Iterable<String> {

    private HashMap<String, HashMap<String, String>> data;

    public MapMap() {
        data = new HashMap<String, HashMap<String, String>>();
    }

    public void put(String key, String subkey) {
        put(key, subkey, "");
    }

    public void put(String key, String subkey, String value) {
        HashMap<String, String> map = data.get(key);
        if (map == null) {
            map = new HashMap<String, String>();
            data.put(key, map);
        }
        map.put(subkey, value);
    }

    public HashMap<String, String> get(String key) {
        return data.get(key);
    }

    public Iterator<String> iterator() {
        return data.keySet().iterator();
    }

    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    public void putAll(String key, HashMap<String, String> sum) {
        HashMap<String, String> map = data.get(key);
        if (map == null) {
            map = new HashMap<String, String>();
            data.put(key, map);
        }
        map.putAll(sum);
    }

    public int size() {
        return data.size();
    }
}
