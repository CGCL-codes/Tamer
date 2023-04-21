package de.objectcode.time4u.server.ejb.seam.api.report;

/**
 * Helper class to transfer a generic value with label.
 * 
 * @author junglas
 */
public class ValueLabelPair {

    Object m_value;

    Object m_label;

    public ValueLabelPair(final Object value, final Object label) {
        m_value = value;
        m_label = label;
    }

    public Object getValue() {
        return m_value;
    }

    public Object getLabel() {
        return m_label;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ValueLabelPair)) {
            return false;
        }
        final ValueLabelPair castObj = (ValueLabelPair) obj;
        return m_value.equals(castObj.m_value) && m_label.equals(castObj.m_label);
    }

    @Override
    public int hashCode() {
        int hash = m_value.hashCode();
        hash = 13 * hash + m_label.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer("ValueLabelPair(");
        buffer.append("value=").append(m_value);
        buffer.append(", label=").append(m_label);
        buffer.append(")");
        return buffer.toString();
    }
}
