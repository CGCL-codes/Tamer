package com.ohioedge.j2ee.api.org.order.ejb;

import org.j2eebuilder.view.BusinessDelegateException;
import org.j2eebuilder.model.ManagedTransientObject;
import java.util.Collection;
import org.j2eebuilder.util.LogManager;

/**
 * CustomerRequirementVendorStatusBean is a java bean used for communication
 * between JSPs and RequirementStatus EJB.
 * 
 * @author Sandeep Dixit
 * @version 1.3.1
 */
public class CustomerRequirementVendorStatusBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    private Integer customerRequirementVendorStatusID;

    private String name;

    private String description;

    public CustomerRequirementVendorStatusBean() {
    }

    public Integer getCustomerRequirementVendorStatusID() {
        return customerRequirementVendorStatusID;
    }

    public void setCustomerRequirementVendorStatusID(Integer customerRequirementVendorStatusID) {
        this.customerRequirementVendorStatusID = customerRequirementVendorStatusID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
