package com.turnengine.client.local.player.command;

import com.javabi.command.response.AbstractCommandResponse;

/**
 * The Set Player Limit Response.
 */
public class SetPlayerLimitResponse extends AbstractCommandResponse<Integer> implements ISetPlayerLimitResponse {

    /** The return value. */
    private Integer returnValue;

    /**
	 * Returns the return value.
	 * @return the return value.
	 */
    public Integer getReturnValue() {
        return returnValue;
    }

    /**
	 * Sets the return value.
	 * @param returnValue the return value to set.
	 */
    public void setReturnValue(Integer returnValue) {
        this.returnValue = returnValue;
    }
}
