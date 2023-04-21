package org.netbeans.modules.parsing.impl;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author hanz
 */
public class TestComparator {

    private String text;

    private boolean failed = false;

    public TestComparator(String text) {
        this.text = text;
    }

    public void check(String line) {
        if (failed) return;
        if (!text.startsWith(line)) {
            failed = true;
            throw new IllegalArgumentException(line + "\nBut expecting:\n" + text);
        }
        text = text.substring(line.length());
        if (text.startsWith("\n")) text = text.substring(1);
    }

    private Map<Class, Map<Object, Integer>> classToObjects = new HashMap<Class, Map<Object, Integer>>();

    public int get(Object o) {
        if (failed) return -1;
        Map<Object, Integer> objects = classToObjects.get(o.getClass());
        if (objects == null) {
            objects = new HashMap<Object, Integer>();
            classToObjects.put(o.getClass(), objects);
        }
        Integer i = objects.get(o);
        if (i == null) {
            i = objects.size() + 1;
            objects.put(o, i);
        }
        return i;
    }

    public String getResult() {
        return text;
    }
}
