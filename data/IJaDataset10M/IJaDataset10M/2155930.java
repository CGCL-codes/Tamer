package org.openrtk.idl.epp0503.contact;

/**
 * Class that contains the elements necessary to transfer a contact
 * in the registry or to query the transfer status from the registry.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/contact/epp_ContactTransferReq.java,v 1.2 2003/09/10 21:29:56 tubadanm Exp $<br>
 * $Revision: 1.2 $<br>
 * $Date: 2003/09/10 21:29:56 $<br>
 * @see com.tucows.oxrs.epp0503.rtk.xml.EPPContactTransfer
 * @see org.openrtk.idl.epp0503.contact.epp_ContactTransferRsp
 */
public class epp_ContactTransferReq implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The common and generic command element.
   * @see #setCmd(org.openrtk.idl.epp0503.epp_Command)
   * @see #getCmd()
   */
    public org.openrtk.idl.epp0503.epp_Command m_cmd = null;

    /**
   * The common and gereric transfer request element.
   * The element encapsulates the authorization information and the transfer operation type.
   * @see #setTrans(org.openrtk.idl.epp0503.epp_TransferRequest)
   * @see #getTrans()
   */
    public org.openrtk.idl.epp0503.epp_TransferRequest m_trans = null;

    /**
   * The identifier for the contact object for the transfer request.
   * @see #setId(String)
   * @see #getId()
   */
    public String m_id = null;

    /**
   * Empty constructor
   */
    public epp_ContactTransferReq() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_cmd The common and generic command element
   * @param _m_trans The common and gereric transfer request element
   * @param _m_id The identifier for the contact object
   */
    public epp_ContactTransferReq(org.openrtk.idl.epp0503.epp_Command _m_cmd, org.openrtk.idl.epp0503.epp_TransferRequest _m_trans, String _m_id) {
        m_cmd = _m_cmd;
        m_trans = _m_trans;
        m_id = _m_id;
    }

    /**
   * Accessor method for the common and generic command element
   * @param value The command element
   * @see #m_cmd
   */
    public void setCmd(org.openrtk.idl.epp0503.epp_Command value) {
        m_cmd = value;
    }

    /**
   * Accessor method for the common and generic command element
   * @return The command element
   * @see #m_cmd
   */
    public org.openrtk.idl.epp0503.epp_Command getCmd() {
        return m_cmd;
    }

    /**
   * Accessor method for the common and gereric transfer request element
   * @param value The contact transfer request object
   * @see #m_trans
   */
    public void setTrans(org.openrtk.idl.epp0503.epp_TransferRequest value) {
        m_trans = value;
    }

    /**
   * Accessor method for the common and gereric transfer request element
   * @return The contact transfer request object
   * @see #m_trans
   */
    public org.openrtk.idl.epp0503.epp_TransferRequest getTrans() {
        return m_trans;
    }

    /**
   * @deprecated
   * @see #setId(String)
   */
    public void setRoid(String value) {
        setId(value);
    }

    /**
   * @deprecated
   * @see #getId()
   */
    public String getRoid() {
        return getId();
    }

    /**
   * Accessor method for the identifier for the contact object
   * @param value The contact id
   * @see #m_id
   */
    public void setId(String value) {
        m_id = value;
    }

    /**
   * Accessor method for the identifier for the contact object
   * @return The contact id
   * @see #m_id
   */
    public String getId() {
        return m_id;
    }

    /**
   * Converts this class into a string.
   * Typically used to view the object in debug output.
   * @return The string representation of this object instance
   */
    public String toString() {
        return this.getClass().getName() + ": { m_cmd [" + m_cmd + "] m_trans [" + m_trans + "] m_id [" + m_id + "] }";
    }
}
