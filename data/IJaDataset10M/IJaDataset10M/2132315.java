package org.iptc.nar.core.datatype;

import org.iptc.nar.interfaces.datatype.IntlStringType;

public class IntlStringTypeImpl extends I18nAttributesTypeImpl implements IntlStringType {

    private Long m_identity;

    private String m_value;

    public Long getIdentity() {
        return m_identity;
    }

    public void setIdentity(Long identity) {
        m_identity = identity;
    }

    public String getValue() {
        return m_value;
    }

    public void setValue(String value) {
        this.m_value = value;
    }
}
