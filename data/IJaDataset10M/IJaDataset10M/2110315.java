package org.spirit.form;

import org.spirit.form.base.BotListBaseForm;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListLinkGroupsForm extends BotListBaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 9218457404133156313L;

    private String generatedId;

    private String groupName;

    /**
	 * @return the generatedId
	 */
    public String getGeneratedId() {
        return generatedId;
    }

    /**
	 * @param generatedId the generatedId to set
	 */
    public void setGeneratedId(String generatedId) {
        this.generatedId = generatedId;
    }

    /**
	 * @return the groupName
	 */
    public String getGroupName() {
        return groupName;
    }

    /**
	 * @param groupName the groupName to set
	 */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
