package org.jadapter.tests.adapters;

import org.jadapter.tests.examples.Csv;
import java.util.Map;

public class ExtraVarArgAdapter implements Csv {

    private Map<?, ?> map;

    public ExtraVarArgAdapter(Map<?, ?> map, Object... args) {
        this.map = map;
    }

    public String csv() {
        if (map == null) return "";
        StringBuffer buffer = new StringBuffer();
        for (Object key : map.keySet()) buffer.append(key + ":" + map.get(key) + ",");
        if (map.size() > 0) buffer.deleteCharAt(buffer.length() - 1);
        return buffer.toString();
    }
}
