package org.arcsoft.bbcash;

import java.io.Serializable;

/**
 *
 *
 * @author $Author: mrspaceman $
 * @version $Revision: 1.4 $
 * $Date: 2001/04/13 07:00:23 $
 */
public class FinancialInstitution implements Serializable {

    /** the sort code for the intitution (unique to each) */
    private String m_sortCode = "";

    /** the sort code for the intitution (unique to each) */
    private String m_name = "";

    /** the sort code for the intitution (unique to each) */
    private Address m_address;

    /** the sort code for the intitution (unique to each) */
    private WebsiteAddress m_websiteAddress;

    /** the sort code for the intitution (unique to each) */
    private EMailAddress m_emailAddress;

    /** the sort code for the intitution (unique to each) */
    private PhoneNumber m_phoneNumber;

    /** constructor
    	@param id - the unique id of the transaction record
    */
    public FinancialInstitution(String sortcode) {
        m_sortCode = sortcode;
        m_address = new Address();
        m_websiteAddress = new WebsiteAddress();
        m_emailAddress = new EMailAddress();
        m_phoneNumber = new PhoneNumber();
    }

    /** fetch the date that this transaction should start being recorded */
    public String getSortCode() {
        return m_sortCode;
    }

    /** fetch the date that this transaction should start being recorded */
    public String getName() {
        return m_name;
    }

    /** set the date that this transaction should start being recorded */
    public void setName(String aString) {
        m_name = aString;
    }

    /** fetch the date that this transaction should start being recorded */
    public Address getAddress() {
        return m_address;
    }

    /** set the date that this transaction should start being recorded */
    public void setAddress(Address anAddress) {
        m_address = anAddress;
    }

    /** fetch the date that this transaction should start being recorded */
    public WebsiteAddress getWebsiteAddress() {
        return m_websiteAddress;
    }

    /** set the date that this transaction should start being recorded */
    public void setWebsiteAddress(WebsiteAddress aWebsiteAddress) {
        m_websiteAddress = aWebsiteAddress;
    }

    /** fetch the date that this transaction should start being recorded */
    public EMailAddress getEMailAddress() {
        return m_emailAddress;
    }

    /** set the date that this transaction should start being recorded */
    public void setEMailAddress(EMailAddress anEMailAddress) {
        m_emailAddress = anEMailAddress;
    }

    /** fetch the date that this transaction should start being recorded */
    public PhoneNumber getPhoneNumber() {
        return m_phoneNumber;
    }

    /** set the date that this transaction should start being recorded */
    public void setPhoneNumber(PhoneNumber aPhoneNumber) {
        m_phoneNumber = aPhoneNumber;
    }
}
