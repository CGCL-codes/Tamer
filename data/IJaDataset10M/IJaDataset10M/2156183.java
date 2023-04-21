package org.openrtk.idl.epprtk.host;

/**
 * Class that contains the elements necessary to check to see if
 * a host is available in the registry.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epprtk/host/epp_HostCheckReq.java,v 1.1 2004/12/07 15:27:50 ewang2004 Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2004/12/07 15:27:50 $<br>
 * @see com.tucows.oxrs.epprtk.rtk.xml.EPPHostCheck
 * @see org.openrtk.idl.epprtk.host.epp_HostCheckRsp
 */
public class epp_HostCheckReq implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic command element.
   * @see #setCmd(org.openrtk.idl.epprtk.epp_Command)
   * @see #getCmd()
   */
    public org.openrtk.idl.epprtk.epp_Command m_cmd = null;

    /**
   * The array of host names to check in the registry.
   * @see #setNames(String[])
   * @see #getNames()
   */
    public String m_names[] = null;

    /**
   * Empty constructor
   */
    public epp_HostCheckReq() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_cmd The common and generic command element
   * @param _m_names The array of host names to check
   */
    public epp_HostCheckReq(org.openrtk.idl.epprtk.epp_Command _m_cmd, String[] _m_names) {
        m_cmd = _m_cmd;
        m_names = _m_names;
    }

    /**
   * Accessor method for the common and generic command element
   * @param value The command element
   * @see #m_cmd
   */
    public void setCmd(org.openrtk.idl.epprtk.epp_Command value) {
        m_cmd = value;
    }

    /**
   * Accessor method for the common and generic command element
   * @return The command element
   * @see #m_cmd
   */
    public org.openrtk.idl.epprtk.epp_Command getCmd() {
        return m_cmd;
    }

    /**
   * Accessor method for the array of host names to check in the registry
   * @param value The array of host names
   * @see #m_names
   */
    public void setNames(String[] value) {
        m_names = value;
    }

    /**
   * Accessor method for the array of host names to check in the registry
   * @return The array of host names
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
