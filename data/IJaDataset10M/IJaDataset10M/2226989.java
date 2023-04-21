package com.turnengine.client.global.game.command;

import com.javabi.command.response.AbstractCommandResponse;
import com.turnengine.client.global.game.bean.IGameHost;

/**
 * The Get Game Host By Id Response.
 */
public class GetGameHostByIdResponse extends AbstractCommandResponse<IGameHost> implements IGetGameHostByIdResponse {

    /** The return value. */
    private IGameHost returnValue;

    /**
	 * Returns the return value.
	 * @return the return value.
	 */
    public IGameHost getReturnValue() {
        return returnValue;
    }

    /**
	 * Sets the return value.
	 * @param returnValue the return value to set.
	 */
    public void setReturnValue(IGameHost returnValue) {
        this.returnValue = returnValue;
    }
}
