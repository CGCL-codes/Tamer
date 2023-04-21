package com.timebuddy.gae.client.actions;

import com.timebuddy.gae.client.action.Action;

/**
 * An action to retrieve the currently logged-in user.  It is an error if no
 * user is currently logged in.
 * 
 * @author Eric Galluzzo
 */
public class GetUserAction implements Action<GetUserResponse> {

    private static final long serialVersionUID = 20090823L;

    public GetUserAction() {
    }
}
