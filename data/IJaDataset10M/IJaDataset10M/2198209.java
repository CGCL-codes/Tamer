package org.eaasyst.eaa.syst.data.transients;

import org.eaasyst.eaa.syst.data.DataBeanBase;

/**
 * <p>An entity class defining a single contact on the person configuration.</p>
 *
 * @version 2.9.1
 * @author Jeff Chilton
 */
public class PersonConfigEditContact extends DataBeanBase {

    private static final long serialVersionUID = 1;

    private String contactId = null;

    private String labelKey = null;

    private String contactUsed = null;

    private String useOnList = null;

    /**
	 * <p>Creates a new <code>PersonConfigEditContact</code> object using the
	 * parameters provided.</p>
	 * 
	 * @param contactId a String containing the identifier of the contact
	 * @since Eaasy Street 2.3
	 */
    public PersonConfigEditContact(String contactId) {
        this.contactId = contactId;
        labelKey = "label." + contactId;
        contactUsed = "false";
        useOnList = "false";
    }

    /**
	 * @return
	 */
    public String getContactId() {
        return contactId;
    }

    /**
	 * @return
	 */
    public String getContactUsed() {
        return contactUsed;
    }

    /**
	 * @return
	 */
    public String getLabelKey() {
        return labelKey;
    }

    /**
	 * @return
	 */
    public String getUseOnList() {
        return useOnList;
    }

    /**
	 * @param string
	 */
    public void setContactId(String string) {
        contactId = string;
    }

    /**
	 * @param string
	 */
    public void setContactUsed(String string) {
        contactUsed = string;
    }

    /**
	 * @param string
	 */
    public void setLabelKey(String string) {
        labelKey = string;
    }

    /**
	 * @param string
	 */
    public void setUseOnList(String string) {
        useOnList = string;
    }
}
