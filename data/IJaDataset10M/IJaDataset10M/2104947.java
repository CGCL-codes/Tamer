package org.openrtk.idl.epp0604.domain;

/**
 * Class that contains the elements necessary to check to see if
 * a domain is available in the registry.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0604/domain/epp_DomainCheckReq.java,v 1.2 2003/09/10 21:29:58 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:58 $<br>
 * @see com.tucows.oxrs.epp0604.rtk.xml.EPPDomainCheck
 * @see org.openrtk.idl.epp0604.domain.epp_DomainCheckRsp
 */
public class epp_DomainCheckReq implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic command element.
   * @see #setCmd(org.openrtk.idl.epp0604.epp_Command)
   * @see #getCmd()
   */
    public org.openrtk.idl.epp0604.epp_Command m_cmd = null;

    /**
   * The array of domain names to check in the registry.
   * @see #setNames(String[])
   * @see #getNames()
   */
    public String m_names[] = null;

    /**
   * Empty constructor
   */
    public epp_DomainCheckReq() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_cmd The common and generic command element
   * @param _m_names The array of domain names to check
   */
    public epp_DomainCheckReq(org.openrtk.idl.epp0604.epp_Command _m_cmd, String[] _m_names) {
        m_cmd = _m_cmd;
        m_names = _m_names;
    }

    /**
   * Accessor method for the common and generic command element
   * @param value The command element
   * @see #m_cmd
   */
    public void setCmd(org.openrtk.idl.epp0604.epp_Command value) {
        m_cmd = value;
    }

    /**
   * Accessor method for the common and generic command element
   * @return The command element
   * @see #m_cmd
   */
    public org.openrtk.idl.epp0604.epp_Command getCmd() {
        return m_cmd;
    }

    /**
   * Accessor method for the array of domain names to check in the registry
   * @param value The array of domain names
   * @see #m_names
   */
    public void setNames(String[] value) {
        m_names = value;
    }

    /**
   * Accessor method for the array of domain names to check in the registry
   * @return The array of domain names
   * @see #m_names
   */
    public String[] getNames() {
        return m_names;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_cmd [" + m_cmd + "] m_names [" + (m_names != null ? java.util.Arrays.asList(m_names) : null) + "] }";
    }
}
