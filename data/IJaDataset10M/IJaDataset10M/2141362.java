package org.cantaloop.tools.testing.reflection;

public class PassthroughFallback {

    private String m_prefix;

    private String m_value;

    public PassthroughFallback(String prefix) {
        m_prefix = prefix;
    }

    public String getPassThrough() {
        String s = (m_value != null) ? m_value : "";
        return m_prefix + s;
    }

    public void setPassThrough(String value) {
        m_value = value;
    }
}
