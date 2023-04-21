package com.timebuddy.gae.client.actions;

import com.timebuddy.gae.client.action.Action;

public class GenerateTokenAction implements Action<GenerateTokenResponse> {

    private static final long serialVersionUID = 20090825L;

    private Long userId;

    /**
	 * Eric made me do it!
	 * Required for serialization.
	 * @deprecated
	 */
    public GenerateTokenAction() {
    }

    public GenerateTokenAction(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return this.userId;
    }
}
