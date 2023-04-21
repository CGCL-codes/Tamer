package com.antlersoft.query;

import java.util.Enumeration;

/**
 * An enumeration that combines two other enumerations.
 * @author Michael A. MacDonald
 *
 */
public class CombineEnum implements Enumeration {

    private Enumeration m_first;

    Enumeration m_second;

    boolean m_use_first;

    public CombineEnum(Enumeration first, Enumeration second) {
        m_first = first;
        m_second = second;
        m_use_first = true;
    }

    public boolean hasMoreElements() {
        boolean result = false;
        if (m_use_first) {
            result = m_first.hasMoreElements();
            if (!result) {
                m_use_first = false;
            }
        }
        if (!m_use_first) {
            if (m_second != null) result = m_second.hasMoreElements();
        }
        return result;
    }

    public Object nextElement() {
        if (m_use_first) {
            if (!m_first.hasMoreElements()) m_use_first = false; else return m_first.nextElement();
        }
        return m_second.nextElement();
    }
}
