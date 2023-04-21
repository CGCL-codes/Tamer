package com.turnengine.client.global.announcement.command;

import com.javabi.command.response.AbstractCommandResponse;

/**
 * The Delete Announcement Response.
 */
public class DeleteAnnouncementResponse extends AbstractCommandResponse<Boolean> implements IDeleteAnnouncementResponse {

    /** The return value. */
    private Boolean returnValue;

    /**
	 * Returns the return value.
	 * @return the return value.
	 */
    public Boolean getReturnValue() {
        return returnValue;
    }

    /**
	 * Sets the return value.
	 * @param returnValue the return value to set.
	 */
    public void setReturnValue(Boolean returnValue) {
        this.returnValue = returnValue;
    }
}
