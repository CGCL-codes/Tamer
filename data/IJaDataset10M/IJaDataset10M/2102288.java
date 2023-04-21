package org.openrtk.idl.epp0402.host;

/**
 * Class that contains the elements used to represent host status with different type.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0402/host/epp_HostStatus.java,v 1.1 2003/03/21 16:35:42 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:35:42 $<br>
 * @see org.openrtk.idl.epp0402.host.epp_HostInfoRsp
 * @see org.openrtk.idl.epp0402.host.epp_HostUpdateAddRemove
 */
public class epp_HostStatus implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The type of the host status.
   * @see #setType(org.openrtk.idl.epp0402.host.epp_HostStatusType)
   * @see #getType()
   */
    public org.openrtk.idl.epp0402.host.epp_HostStatusType m_type = null;

    /**
   * The language used to express the host status value.
   * @see #setLang(String)
   * @see #getLang()
   */
    public String m_lang = null;

    /**
   * The value of the host status.
   * @see #setValue(String)
   * @see #getValue()
   */
    public String m_value = null;

    /**
   * Empty constructor
   */
    public epp_HostStatus() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_type The type of the host status
   * @param _m_lang The language used to express the host status value
   * @param _m_value The value of the host status
   */
    public epp_HostStatus(org.openrtk.idl.epp0402.host.epp_HostStatusType _m_type, String _m_lang, String _m_value) {
        m_type = _m_type;
        m_lang = _m_lang;
        m_value = _m_value;
    }

    /**
   * Accessor method for the type of the host status
   * @param value The host status type
   * @see #m_type
   */
    public void setType(org.openrtk.idl.epp0402.host.epp_HostStatusType value) {
        m_type = value;
    }

    /**
   * Accessor method for the type of the host status
   * @return The host status type
   * @see #m_type
   */
    public org.openrtk.idl.epp0402.host.epp_HostStatusType getType() {
        return m_type;
    }

    /**
   * Accessor method for the language used to express the host status value
   * @param value The language of the status value
   * @see #m_lang
   */
    public void setLang(String value) {
        m_lang = value;
    }

    /**
   * Accessor method for the language used to express the host status value
   * @return The language of the status value
   * @see #m_lang
   */
    public String getLang() {
        return m_lang;
    }

    /**
   * Accessor method for the value of the host status
   * @param value The host status value
   * @see #m_value
   */
    public void setValue(String value) {
        m_value = value;
    }

    /**
   * Accessor method for the value of the host status
   * @return The host status value
   * @see #m_value
   */
    public String getValue() {
        return m_value;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_type [" + m_type + "] m_lang [" + m_lang + "] m_value [" + m_value + "] }";
    }
}
