package org.homedns.krolain.util;

import java.util.Locale;

/**
 *
 * @author  Krolain
 */
public class ResourceInfo extends java.lang.Object implements java.lang.Comparable {

    public String m_Name;

    public String m_FileName;

    public Locale m_Locale;

    /** Creates a new instance of DictionInfo */
    public ResourceInfo() {
    }

    public int compareTo(Object o) {
        if (o instanceof ResourceInfo) {
            ResourceInfo obj = (ResourceInfo) o;
            return m_Name.compareTo(obj.m_Name);
        } else return m_Name.compareTo(o);
    }

    public String toString() {
        return m_Name;
    }
}
