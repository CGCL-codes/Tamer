package com.generalynx.common.utils;

/**
 * We want that toString is called on object member and is compared by comparable member 
 */
public class ComparableLabel implements Comparable {

    private Object m_object;

    private Comparable m_comparable;

    public ComparableLabel(Comparable comparable) {
        m_object = comparable;
        m_comparable = comparable;
    }

    public ComparableLabel(Object object, Comparable comparable) {
        m_object = object;
        m_comparable = comparable;
    }

    public Object getObject() {
        return m_object;
    }

    public Comparable getComparable() {
        return m_comparable;
    }

    public String toString() {
        return m_object.toString();
    }

    public int compareTo(Object o) {
        ComparableLabel other = (ComparableLabel) o;
        return Tools.nullSafeCompare(m_comparable, other.m_comparable);
    }
}
