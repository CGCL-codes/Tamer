package model.data;

import java.util.HashMap;
import java.util.Iterator;

public class MapSortedMap implements Iterable<String> {

    private HashMap<String, Result> data;

    public MapSortedMap() {
        data = new HashMap<String, Result>();
    }

    public void put(String key, String subkey, String value) {
        Result map = data.get(key);
        if (map == null) {
            map = new Result();
            data.put(key, map);
        }
        map.put(subkey, value);
    }

    public Result get(String key) {
        return data.get(key);
    }

    public Iterator<String> iterator() {
        return data.keySet().iterator();
    }
}
