package org.openrtk.idl.epp0503.domain;

/**
 * Class that contains the elements used to represent domain contact
 * with the type: ADMIN, BILLING, TECH.</p>
 * Note that the registrant "contact" is not an epp_DomainContact.  The
 * registrant's contact id is associated with a domain directly as a String
 * in such classes as epp_DomainCreateReq and epp_DomainUpdateChange.</p>
 * $Header: /cvsroot/epp-rtk/epp-rtk/java/src/org/openrtk/idl/epp0503/domain/epp_DomainContact.java,v 1.1 2003/03/21 16:18:28 tubadanm Exp $<br>
 * $Revision: 1.1 $<br>
 * $Date: 2003/03/21 16:18:28 $<br>
 * @see org.openrtk.idl.epp0503.domain.epp_DomainUpdateAddRemove
 * @see org.openrtk.idl.epp0503.domain.epp_DomainCreateReq
 * @see org.openrtk.idl.epp0503.domain.epp_DomainInfoRsp
 */
public class epp_DomainContact implements org.omg.CORBA.portable.IDLEntity {

    /**
   * The type of the domain contact.
   * @see #setType(org.openrtk.idl.epp0503.domain.epp_DomainContactType)
   * @see #getType()
   */
    public org.openrtk.idl.epp0503.domain.epp_DomainContactType m_type = null;

    /**
   * The identifier of the domain contact.
   * @see #setId(String)
   * @see #getId()
   */
    public String m_id = null;

    /**
   * Empty constructor
   */
    public epp_DomainContact() {
    }

    /**
   * The constructor with initializing variables.
   * @param _m_type The type of the domain contact
   * @param _m_id The identifier of the domain contact
   */
    public epp_DomainContact(org.openrtk.idl.epp0503.domain.epp_DomainContactType _m_type, String _m_id) {
        m_type = _m_type;
        m_id = _m_id;
    }

    /**
   * Accessor method for the type of the domain contact
   * @param value The domain contact type
   * @see #m_type
   */
    public void setType(org.openrtk.idl.epp0503.domain.epp_DomainContactType value) {
        m_type = value;
    }

    /**
   * Accessor method for the type of the domain contact
   * @return The domain contact type
   * @see #m_type
   */
    public org.openrtk.idl.epp0503.domain.epp_DomainContactType getType() {
        return m_type;
    }

    /**
   * Accessor method for the identifier of the domain contact
   * @param value The domain contact identifier
   * @see #m_id
   */
    public void setId(String value) {
        m_id = value;
    }

    /**
   * Accessor method for the identifier of the domain contact
   * @return The domain contact identifier
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
        return this.getClass().getName() + ": { m_type [" + m_type + "] m_id [" + m_id + "] }";
    }
}
