package net.sourceforge.plantuml;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Pragma {

    private final Map<String, String> values = new LinkedHashMap<String, String>();

    public void define(String name, String value) {
        values.put(name, value);
        if (name.equalsIgnoreCase("graphviz_dot")) {
            OptionFlags.getInstance().setDotExecutable(value);
        }
    }

    public boolean isDefine(String name) {
        return values.containsKey(name);
    }

    public void undefine(String name) {
        values.remove(name);
    }

    public String getValue(String name) {
        final String result = values.get(name);
        if (result == null) {
            throw new IllegalArgumentException();
        }
        return result;
    }

    protected Set<Map.Entry<String, String>> entrySet() {
        return Collections.unmodifiableSet(values.entrySet());
    }
}
