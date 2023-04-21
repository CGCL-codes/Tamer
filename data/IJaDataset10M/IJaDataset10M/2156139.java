package com.turnengine.client.local.action.command;

import com.javabi.command.response.AbstractCommandResponse;
import com.turnengine.client.local.action.bean.IActionCondition;

/**
 * The Add Action Condition Response.
 */
public class AddActionConditionResponse extends AbstractCommandResponse<IActionCondition> implements IAddActionConditionResponse {

    /** The return value. */
    private IActionCondition returnValue;

    /**
	 * Returns the return value.
	 * @return the return value.
	 */
    public IActionCondition getReturnValue() {
        return returnValue;
    }

    /**
	 * Sets the return value.
	 * @param returnValue the return value to set.
	 */
    public void setReturnValue(IActionCondition returnValue) {
        this.returnValue = returnValue;
    }
}
