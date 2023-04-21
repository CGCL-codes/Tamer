package org.apache.avalon.cornerstone.services.security;

import java.io.Serializable;

/**
 * Class representing a unit of proof of identity.
 */
public class Credential implements Serializable {

    private String m_type = null;

    private Object m_value = null;

    public Credential(String type, Object value) {
        m_type = type;
        m_value = value;
    }

    public String getType() {
        return m_type;
    }

    public Object getValue() {
        return m_value;
    }

    public void setType(String type) {
        m_type = type;
    }

    public void setValue(Object value) {
        m_value = value;
    }
}
