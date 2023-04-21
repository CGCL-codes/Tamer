package com.windsor.node.plugin.frs23.identities;

import java.io.Serializable;

/**
 * @author mchmarny
 * 
 */
public class Organization extends BaseClass implements Serializable {

    private static final long serialVersionUID = 1;

    public String OrganizationFormalName;

    public String OrganizationDUNSNumber;

    public String OrganizationTypeText;

    public String EmployerIdentifier;

    public String StateBusinessIdentifier;

    public String UltimateParentName;

    public String UltimateParentDUNSNumber;

    public String[] getFieldNames() {
        return new String[] { "OrganizationFormalName", "OrganizationDUNSNumber", "OrganizationTypeText", "EmployerIdentifier", "StateBusinessIdentifier", "UltimateParentName", "UltimateParentDUNSNumber" };
    }
}
