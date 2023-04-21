package org.spirit.form;

import org.spirit.form.base.BotListBaseForm;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListAclManagerForm extends BotListBaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6139017571367147447L;

    private Long aclId;

    private Long userId;

    /**
	 * @return the aclId
	 */
    public Long getAclId() {
        return aclId;
    }

    /**
	 * @param aclId the aclId to set
	 */
    public void setAclId(Long aclId) {
        this.aclId = aclId;
    }

    /**
	 * @return the userId
	 */
    public Long getUserId() {
        return userId;
    }

    /**
	 * @param userId the userId to set
	 */
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
