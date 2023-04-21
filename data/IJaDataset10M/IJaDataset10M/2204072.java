package com.loribel.commons.module.debug.impl;

import java.awt.Component;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import com.loribel.commons.abstraction.GB_Debugable;
import com.loribel.commons.util.CTools;

/**
 * Tools to build GB_Debugable functions.
 */
public abstract class GB_DebugableToolsOld {

    public static List listToDebug(String a_label, Collection a_list, Object a_circular) {
        List retour = new ArrayList();
        if (a_list == null) {
            retour.add(a_label + ": null");
            return retour;
        }
        retour.add(a_label + " [" + a_list.getClass() + " size: " + CTools.getSize(a_list) + "]");
        int l_index = 0;
        for (Iterator it = a_list.iterator(); it.hasNext(); ) {
            Object l_item = it.next();
            retour.add(objectToDebug("[index: " + l_index + "]", l_item, a_circular));
            l_index++;
        }
        return retour;
    }

    public static List mapToDebug(String a_label, Map a_map, Object a_circular) {
        List retour = new ArrayList();
        if (a_map == null) {
            retour.add(a_label + ": null");
            return retour;
        }
        retour.add(a_label + " [" + a_map.getClass() + " size: " + CTools.getSize(a_map) + "]");
        Set l_keys = a_map.keySet();
        for (Iterator it = l_keys.iterator(); it.hasNext(); ) {
            Object l_key = it.next();
            Object l_item = a_map.get(l_key);
            retour.add(objectToDebug("[key: " + l_key + "]", l_item, a_circular));
        }
        return retour;
    }

    public static List objectToDebug(String a_label, Object a_item, Object a_circular) {
        List retour = new ArrayList();
        if (a_item == null) {
            retour.add(a_label + ": null");
            return retour;
        }
        if (a_item == a_circular) {
            retour.add(a_label + "...");
            retour.add("Circular reference " + a_item.toString());
            return retour;
        }
        if (a_item instanceof Map) {
            return mapToDebug(a_label, (Map) a_item, a_circular);
        }
        if (a_item instanceof Collection) {
            return listToDebug(a_label, (Collection) a_item, a_circular);
        }
        retour.add(a_label + ": " + a_item);
        return retour;
    }

    public static GB_Debugable toDebugable(final String a_label, final Object a_data) {
        if (a_data instanceof GB_Debugable) {
            return (GB_Debugable) a_data;
        }
        GB_Debugable retour = new GB_Debugable() {

            public List toDebug() {
                return toDebugList(a_label, a_data);
            }

            public String toDebugId() {
                return a_label;
            }
        };
        return retour;
    }

    public static List toDebugList(String a_label, Object a_data) {
        return toDebugList(a_label, a_data, null);
    }

    public static List toDebugList(String a_label, Object a_data, Object a_source) {
        List retour = new ArrayList();
        if (a_data == null) {
            retour.add(a_label + "=null");
            return retour;
        }
        Collection l_ignoreList = new ArrayList();
        if (a_source != null) {
            l_ignoreList.add(a_source);
        }
        return toDebugListSafe(a_label, a_data, l_ignoreList);
    }

    static List toDebugListByIntrospection(String a_label, Object a_data) {
        List retour = new ArrayList();
        if (a_data == null) {
            retour.add(a_label + "=null");
            return retour;
        }
        Field[] l_fieldArray = a_data.getClass().getFields();
        int len = l_fieldArray.length;
        Field l_field;
        retour.add(a_label + " [introspection]");
        for (int i = 0; i < len; i++) {
            l_field = l_fieldArray[i];
            String l_name = l_field.getName();
            try {
                System.out.println(l_name + "");
                Object l_value = l_field.get(a_data);
                System.out.println("     =" + l_value);
                retour.add(toDebugList(l_name, l_value));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return retour;
    }

    static List toDebugListForArray(String a_label, List a_data, Collection a_ignoreList) {
        List retour = new ArrayList();
        int len = CTools.getSize(a_data);
        if (len == 0) {
            retour.add(a_label + "=null");
            return retour;
        } else {
            retour.add(a_label + " [size=" + len + "]");
            for (int i = 0; i < len; i++) {
                retour.add("" + i + "=" + a_data.get(i));
            }
        }
        return retour;
    }

    static List toDebugListForCollection(String a_label, Collection a_data, Collection a_ignoreList) {
        int len = CTools.getSize(a_data);
        List retour = new ArrayList();
        if (len == 0) {
            retour.add(a_label + "=null");
            return retour;
        } else {
            retour.add(a_label + " [size=" + len + "]");
            Iterator it = a_data.iterator();
            int i = 0;
            while (it.hasNext()) {
                retour.add("" + i + "=" + it.next());
                i++;
            }
        }
        return retour;
    }

    static List toDebugListForDebugable(String a_label, GB_Debugable a_data, Collection a_ignoreList) {
        List retour = new ArrayList();
        if (a_data == null) {
            retour.add(a_label + "=null");
            return retour;
        } else {
            retour.add(a_label);
            retour.add(a_data.toString());
        }
        return retour;
    }

    static List toDebugListSafe(String a_label, Object a_data, Collection a_ignoreList) {
        List retour = new ArrayList();
        if (a_ignoreList.contains(a_data)) {
            retour.add(a_label);
            retour.add(a_data + " (...)");
            return retour;
        }
        a_ignoreList.add(a_data);
        if (a_data instanceof Collection) {
            retour = toDebugListForCollection(a_label, (Collection) a_data, a_ignoreList);
        }
        if (a_data.getClass().isArray()) {
            retour = toDebugListForArray(a_label, (List) a_data, a_ignoreList);
        }
        if (a_data instanceof Component) {
        }
        if (a_data instanceof GB_Debugable) {
            List l_temp = toDebugListForDebugable(a_label, (GB_Debugable) a_data, a_ignoreList);
            if (retour != null) {
                List retour2 = new ArrayList();
                retour2.add(a_label + "=" + a_data);
                retour2.add(retour);
                retour2.add(l_temp);
                return retour;
            } else {
                return l_temp;
            }
        }
        retour.add(a_label + "=" + a_data);
        return retour;
    }
}
