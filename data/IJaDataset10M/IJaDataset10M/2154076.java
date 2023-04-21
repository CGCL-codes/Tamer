package org.openrtk.idl.epp0705.host;

/**
 * Class that contains the elements used to represent the host delete
 * response from the EPP server.</p>
 * Note usually instantiated by the RTK user, but rather by EPPHostDelete
 * and retrieved using that class' getResponseData() method.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0705/host/epp_HostDeleteRsp.java,v 1.2 2003/09/10 21:29:59 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:59 $<br>
 * @see com.tucows.oxrs.epp0705.rtk.xml.EPPHostDelete
 * @see org.openrtk.idl.epp0705.host.epp_HostDeleteReq
 */
public class epp_HostDeleteRsp implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic response element.
   * @see #getRsp()
   */
    public org.openrtk.idl.epp0705.epp_Response m_rsp = null;

    /**
   * Empty constructor
   */
    public epp_HostDeleteRsp() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_rsp The common and generic response element
   */
    public epp_HostDeleteRsp(org.openrtk.idl.epp0705.epp_Response _m_rsp) {
        m_rsp = _m_rsp;
    }

    /**
   * Accessor method for the common and generic response element.
   * @param value The new value for the response element
   * @see #m_rsp
   */
    public void setRsp(org.openrtk.idl.epp0705.epp_Response value) {
        m_rsp = value;
    }

    /**
   * Accessor method for the common and generic response element.
   * @return The value for the response element
   * @see #m_rsp
   */
    public org.openrtk.idl.epp0705.epp_Response getRsp() {
        return m_rsp;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_rsp [" + m_rsp + "] }";
    }
}
