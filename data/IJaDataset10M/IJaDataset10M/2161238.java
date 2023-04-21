package org.openrtk.idl.epp02.contact;

public class epp_ContactPhone implements org.omg.CORBA.portable.IDLEntity {

    public String m_extension = null;

    public String m_value = null;

    public epp_ContactPhone() {
    }

    public epp_ContactPhone(String _m_extension, String _m_value) {
        m_extension = _m_extension;
        m_value = _m_value;
    }

    public void setExtension(String value) {
        m_extension = value;
    }

    public String getExtension() {
        return m_extension;
    }

    public void setValue(String value) {
        m_value = value;
    }

    public String getValue() {
        return m_value;
    }

    public String toString() {
        return this.getClass().getName() + ": { m_extension [" + m_extension + "] m_value [" + m_value + "] }";
    }
}
