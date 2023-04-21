package com.turnengine.client.local.alliance.command;

import com.javabi.command.response.AbstractCommandResponse;
import com.turnengine.client.local.alliance.bean.IAlliance;
import java.util.List;

/**
 * The Get Alliance List Response.
 */
public class GetAllianceListResponse extends AbstractCommandResponse<List<IAlliance>> implements IGetAllianceListResponse {

    /** The return value. */
    private List<IAlliance> returnValue;

    /**
	 * Returns the return value.
	 * @return the return value.
	 */
    public List<IAlliance> getReturnValue() {
        return returnValue;
    }

    /**
	 * Sets the return value.
	 * @param returnValue the return value to set.
	 */
    public void setReturnValue(List<IAlliance> returnValue) {
        this.returnValue = returnValue;
    }
}
