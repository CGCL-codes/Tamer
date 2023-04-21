package com.cell.util;

import java.util.Vector;

/**
 * @author waza
 *
 * Config.ini
 * 
 * OPTION1=abc
 * OPTION1=def
 * OPTION1=ghi
 * OPTION2=op2
 * OPTIONS1=1,2,3,4,5
 * ...
 * 
 * PropertyGroup p = new PropertyGroup(".../Config.ini","=");
 * 
 * String[] Option1    = p.getGroup("OPTION1");
 * Option1[0] == abc
 * Option1[1] == def
 * Option1[2] == ghi
 * 
 * String Option2 = p.getString("OPTION2"); 
 * Option2 == op2
 * 
 * int[] Options1 = p.getIntegerArray("OPTIONS1",",");
 * Options1[0] == 1
 * Options1[1] == 2
 * Options1[2] == 3
 * Options1[3] == 4
 * Options1[4] == 5
 */
public class PropertyGroup extends Property<Vector<String>> {

    public PropertyGroup() {
    }

    public PropertyGroup(String text, String separator) {
        loadText(text, separator);
    }

    protected boolean putText(String k, String v) {
        return put(k, v);
    }

    public boolean put(String key, String value) {
        if (Map.containsKey(key)) {
            get(key).addElement(value);
            return true;
        } else {
            Vector<String> group = new Vector<String>();
            group.addElement(value);
            return Map.put(key, group) == null;
        }
    }

    @Override
    public boolean putObject(String k, Object v) {
        return put(k, v.toString());
    }

    public String getString(String key) {
        return getSingle(key);
    }

    public String[] getGroup(String key) {
        Vector<String> group = Map.get(key);
        if (group != null) {
            String[] ret = new String[group.size()];
            group.copyInto(ret);
            return ret;
        } else {
            return new String[0];
        }
    }

    public String getSingle(String key) {
        Vector<String> group = Map.get(key);
        if (group != null) {
            return group.elementAt(0);
        } else {
            return null;
        }
    }
}
