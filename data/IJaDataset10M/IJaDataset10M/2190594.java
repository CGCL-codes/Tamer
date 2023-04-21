package nu.esox.accounting;

import nu.esox.util.*;

@SuppressWarnings("serial")
public class NamedAndNumbered extends Observable implements Comparable<NamedAndNumbered> {

    public static final String PROPERTY_NUMBER = "PROPERTY_NUMBER";

    public static final String PROPERTY_NAME = "PROPERTY_NAME";

    private int m_number;

    private String m_name = "";

    public NamedAndNumbered() {
    }

    public String toString() {
        return getNumber() + " " + getName();
    }

    public int getNumber() {
        return m_number;
    }

    public final String getName() {
        return m_name;
    }

    public final void setNumber(int number) {
        if (m_number == number) return;
        m_number = number;
        fireValueChanged(PROPERTY_NUMBER, new Integer(m_number));
    }

    public final void setName(String name) {
        if (name == null) name = "";
        if (m_name.equals(name)) return;
        m_name = name;
        fireValueChanged(PROPERTY_NAME, m_name);
    }

    public int compareTo(NamedAndNumbered o) {
        return m_number - o.m_number;
    }
}
