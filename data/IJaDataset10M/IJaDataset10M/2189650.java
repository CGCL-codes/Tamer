package com.hack23.cia.service.api.control;

import com.hack23.cia.model.api.application.events.UserAccountOperationType;

/**
 * The Class RegisterUserRequest.
 */
public class RegisterUserRequest extends AbstractUserAccountRequest {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new register user request.
	 * 
	 * @param userSessionId the user session id
	 * @param operation the operation
	 */
    public RegisterUserRequest(final Long userSessionId, final UserAccountOperationType operation) {
        super(userSessionId, operation);
    }
}
