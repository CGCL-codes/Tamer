package org.xteam.sled.semantic;

import java.util.HashMap;
import java.util.Map;

public class FieldInformations {

    public static final int CHECKED = 0;

    public static final int GUARANTEED = 1;

    public static final int UNCHECKED = 2;

    private static class FieldInfo {

        int checking = CHECKED;

        Map<String, Integer> spec = new HashMap<String, Integer>();

        boolean hasNames = false;

        boolean fullMap = false;
    }

    private Map<Field, FieldInfo> infos = new HashMap<Field, FieldInfo>();

    public void setChecking(Field f, int check) {
        FieldInfo info = getInfo(f);
        info.checking = check;
    }

    public boolean hasNames(Field f) {
        return getInfo(f).hasNames;
    }

    public void setNames(Field field, Map<String, Integer> spec, boolean fullMap) {
        FieldInfo info = getInfo(field);
        info.spec = spec;
        info.hasNames = true;
        info.fullMap = fullMap;
    }

    public Map<String, Integer> namesOf(Field field) {
        return getInfo(field).spec;
    }

    private FieldInfo getInfo(Field f) {
        FieldInfo info = infos.get(f);
        if (info == null) {
            infos.put(f, info = new FieldInfo());
        }
        return info;
    }
}
