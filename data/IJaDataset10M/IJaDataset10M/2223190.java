package net.sourceforge.transumanza.task;

import java.util.LinkedHashMap;
import java.util.Map;

public class ExecutionResults {

    Map results = new LinkedHashMap();

    public Object get(String key) {
        return results.get(key);
    }

    public void put(String key, Object value) {
        results.put(key, value);
    }
}
