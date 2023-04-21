package com.turnengine.client.local.upkeep.command;

import com.javabi.command.response.AbstractCommandResponse;
import com.turnengine.client.local.upkeep.bean.IUpkeep;

/**
 * The Add Upkeep Response.
 */
public class AddUpkeepResponse extends AbstractCommandResponse<IUpkeep> implements IAddUpkeepResponse {

    /** The return value. */
    private IUpkeep returnValue;

    /**
	 * Returns the return value.
	 * @return the return value.
	 */
    public IUpkeep getReturnValue() {
        return returnValue;
    }

    /**
	 * Sets the return value.
	 * @param returnValue the return value to set.
	 */
    public void setReturnValue(IUpkeep returnValue) {
        this.returnValue = returnValue;
    }
}
