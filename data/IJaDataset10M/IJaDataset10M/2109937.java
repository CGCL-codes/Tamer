package org.eaasyst.eaa.syst.data.persistent;

import org.eaasyst.eaa.syst.data.PersistentDataBean;
import org.eaasyst.eaa.syst.data.PersistentDataBeanBase;

/**
 * <p>This entity class defines a <code>ResourceAuthorization</code>
 * entity, which associates a user group with a resource group.</p>
 *
 * @version 2.9.1
 * @author Jeff Chilton
 */
public class ResourceAuthorization extends PersistentDataBeanBase {

    private static final long serialVersionUID = 1;

    private String userGroupId = null;

    private ResourceGroup resourceGroup = null;

    /**
	 * <p>Updates this bean using the data from the passed bean.</p>
	 *
	 * @param member the <code>ResourceAuthorization</code> object containing
	 * the updated data
	 * @since Eaasy Street 2.3.3
	 */
    public void updateBeanFields(PersistentDataBean bean) {
        ResourceAuthorization ra = (ResourceAuthorization) bean;
        userGroupId = ra.getUserGroupId();
        resourceGroup = ra.getResourceGroup();
    }

    /**
	 * <p>Returns a String containing the comma-separated values of
	 * the contents of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated values of
	 * this entity bean
	 * @since Eaasy Street 2.3.3
	 */
    protected String getDataAsCsv() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(userGroupId);
        buffer.append(",\"");
        buffer.append(resourceGroup.getTitle());
        buffer.append("\"");
        return buffer.toString();
    }

    /**
	 * <p>Returns a String containing the comma-separated column 
	 * headings for the csv values of this entity bean.</p>
	 * 
	 * @return a String containing the comma-separated column 
	 * headings for the csv values of this entity bean
	 * @since Eaasy Street 2.3.3
	 */
    protected String getHeadingsAsCsv() {
        return "\"User Group\",\"Resource Group\"";
    }

    /**
	 * Returns the resourceGroup.
	 * @return ResourceGroup
	 */
    public ResourceGroup getResourceGroup() {
        return resourceGroup;
    }

    /**
	 * Returns the userGroupId.
	 * @return string
	 */
    public String getUserGroupId() {
        return userGroupId;
    }

    /**
	 * Sets the resourceGroup.
	 * @param resource The resourceGroup to set
	 */
    public void setResourceGroup(ResourceGroup resourceGroup) {
        this.resourceGroup = resourceGroup;
    }

    /**
	 * Sets the userGroupId.
	 * @param userGroupId The userGroupId to set
	 */
    public void setUserGroupId(String userGroupId) {
        this.userGroupId = userGroupId;
    }
}
