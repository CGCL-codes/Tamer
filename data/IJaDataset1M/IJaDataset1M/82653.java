package com.turnengine.client.local.turn.command;

import com.javabi.command.response.AbstractCommandResponse;

/**
 * The Get Turn Number Response.
 */
public class GetTurnNumberResponse extends AbstractCommandResponse<Integer> implements IGetTurnNumberResponse {

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
