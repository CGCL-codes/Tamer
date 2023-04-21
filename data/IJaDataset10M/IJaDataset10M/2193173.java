package com.ohioedge.j2ee.api.org.cust.req.ejb;

/**
 * 
 * @author Sandeep Dixit
 * @version 1.350, 01/12/03
 * @see org.j2eebuilder.view.ManagedComponentObjectFactory#getDataVO(java.lang.Object,
 *      java.lang.Class)
 ** @since OEC1.2
 */
public class RequirementStatusBean extends org.j2eebuilder.model.ejb.SignatureImpl {

    public Integer requirementStatusID;

    public String name;

    public String description;

    public Integer getRequirementStatusID() {
        return requirementStatusID;
    }

    public void setRequirementStatusID(Integer requirementStatusID) {
        this.requirementStatusID = requirementStatusID;
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
