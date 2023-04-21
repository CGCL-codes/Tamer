package org.tide.webapp;

import org.w3c.dom.*;

public class InitParameter extends WebAppComponent {

    protected String m_name;

    protected String m_value;

    protected String m_description;

    InitParameter() {
    }

    InitParameter(String name, String value, String description) {
        m_name = name;
        m_value = value;
        m_description = description;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public String getValue() {
        return m_value;
    }

    public void setValue(String value) {
        m_value = value;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(String description) {
        m_description = description;
    }

    public boolean equalsTo(Object o) {
        if (o instanceof InitParameter) {
            return getName().equals(((InitParameter) o).getName());
        } else {
            return false;
        }
    }
}
