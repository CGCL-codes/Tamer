package org.spirit.form;

import org.spirit.form.base.BotListBaseForm;

/**
 * This is class is used by botverse.
 * 
 * @author Berlin Brown
 * 
 */
public class BotListGroupManagerForm extends BotListBaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1366886650924350483L;

    private Long groupId;

    private Long userId;

    /**
	 * @return the groupId
	 */
    public Long getGroupId() {
        return groupId;
    }

    /**
	 * @param groupId the groupId to set
	 */
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
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
