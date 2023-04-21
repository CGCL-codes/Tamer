package org.blueoxygen.lotion.contact.actions;

import java.util.ArrayList;
import java.util.List;
import org.blueoxygen.lotion.BusinessPartner;

/**
 * @author alex
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class EditContact extends ViewContact {

    protected BusinessPartner businessPartner;

    private List businessPartners = new ArrayList();

    private String businessPartnerId = "";

    public String execute() {
        String result = super.execute();
        businessPartners = pm.findAllSorted(BusinessPartner.class, "name");
        return SUCCESS;
    }

    /**
	 * @return Returns the businessPartner.
	 */
    public BusinessPartner getBusinessPartner() {
        return businessPartner;
    }

    /**
	 * @param businessPartner The businessPartner to set.
	 */
    public void setBusinessPartner(BusinessPartner businessPartner) {
        this.businessPartner = businessPartner;
    }

    /**
	 * @return Returns the businessPartnerId.
	 */
    public String getBusinessPartnerId() {
        return businessPartnerId;
    }

    /**
	 * @param businessPartnerId The businessPartnerId to set.
	 */
    public void setBusinessPartnerId(String businessPartnerId) {
        this.businessPartnerId = businessPartnerId;
    }

    /**
	 * @return Returns the businessPartners.
	 */
    public List getBusinessPartners() {
        return businessPartners;
    }

    /**
	 * @param businessPartners The businessPartners to set.
	 */
    public void setBusinessPartners(List businessPartners) {
        this.businessPartners = businessPartners;
    }
}
