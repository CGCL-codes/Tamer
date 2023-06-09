package org.openrtk.idl.epp0604.host;

/**
 * Class that contains the elements necessary to modify a host
 * in the registry.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0604/host/epp_HostUpdateReq.java,v 1.2 2003/09/10 21:29:58 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:58 $<br>
 * @see com.tucows.oxrs.epp0604.rtk.xml.EPPHostUpdate
 * @see org.openrtk.idl.epp0604.host.epp_HostUpdateRsp
 */
public class epp_HostUpdateReq implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic command element.
   * @see #setCmd(org.openrtk.idl.epp0604.epp_Command)
   * @see #getCmd()
   */
    public org.openrtk.idl.epp0604.epp_Command m_cmd = null;

    /**
   * The name of the host object to be modified in the registry.
   * @see #setName(String)
   * @see #getName()
   */
    public String m_name = null;

    /**
   * The add element that contains attribute values to be added to the host object.
   * @see #setAdd(org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove)
   * @see #getAdd()
   */
    public org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove m_add = null;

    /**
   * The remove element that contains attribute values to be removed from the host object.
   * @see #setRemove(org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove)
   * @see #getRemove()
   */
    public org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove m_remove = null;

    /**
   * The change element that contains attribute values to be changed in the host object.
   * @see #setChange(org.openrtk.idl.epp0604.host.epp_HostUpdateChange)
   * @see #getChange()
   */
    public org.openrtk.idl.epp0604.host.epp_HostUpdateChange m_change = null;

    /**
   * Empty constructor
   */
    public epp_HostUpdateReq() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_cmd The common and generic command element
   * @param _m_name The name of the host object to be modified in the registry
   * @param _m_add The add element that contains attribute values to be added to the host object
   * @param _m_remove The remove element that contains attribute values to be removed from the host object
   * @param _m_change The change element that contains attribute values to be changed
   */
    public epp_HostUpdateReq(org.openrtk.idl.epp0604.epp_Command _m_cmd, String _m_name, org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove _m_add, org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove _m_remove, org.openrtk.idl.epp0604.host.epp_HostUpdateChange _m_change) {
        m_cmd = _m_cmd;
        m_name = _m_name;
        m_add = _m_add;
        m_remove = _m_remove;
        m_change = _m_change;
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
   * Accessor method for the name of the host object
   * @param value The host name
   * @see #m_name
   */
    public void setName(String value) {
        m_name = value;
    }

    /**
   * Accessor method for the name of the host object
   * @return The host name
   * @see #m_name
   */
    public String getName() {
        return m_name;
    }

    /**
   * Accessor method for the add element that contains attribute values to be added to the host object
   * @param value The add element
   * @see #m_add
   */
    public void setAdd(org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove value) {
        m_add = value;
    }

    /**
   * Accessor method for the add element that contains attribute values to be added to the host object
   * @return The add element
   * @see #m_add
   */
    public org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove getAdd() {
        return m_add;
    }

    /**
   * Accessor method for the remove element that contains attribute values to be removed from the host object
   * @param value The remove element
   * @see #m_remove
   */
    public void setRemove(org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove value) {
        m_remove = value;
    }

    /**
   * Accessor method for the remove to be associated with the host object
   * @return The remove element
   * @see #m_remove
   */
    public org.openrtk.idl.epp0604.host.epp_HostUpdateAddRemove getRemove() {
        return m_remove;
    }

    /**
   * Accessor method for the change element that contains attribute values to be changed
   * @param value The change element
   * @see #m_change
   */
    public void setChange(org.openrtk.idl.epp0604.host.epp_HostUpdateChange value) {
        m_change = value;
    }

    /**
   * Accessor method for the change element that contains attribute values to be changed
   * @return The change element
   * @see #m_change
   */
    public org.openrtk.idl.epp0604.host.epp_HostUpdateChange getChange() {
        return m_change;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_cmd [" + m_cmd + "] m_name [" + m_name + "] m_add [" + m_add + "] m_remove [" + m_remove + "] m_change [" + m_change + "] }";
    }
}
