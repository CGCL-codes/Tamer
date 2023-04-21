package com.hack23.cia.service.impl.user;

import java.util.Date;
import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.ActionEvent;
import com.hack23.cia.model.application.impl.common.UserSession;
import com.hack23.cia.model.application.impl.user.UserActionEvent;
import com.hack23.cia.service.api.common.ServiceResponse;
import com.hack23.cia.service.api.user.UserRequest;
import com.hack23.cia.service.api.user.UserResponse;
import com.hack23.cia.service.impl.common.ParliamentService;
import com.hack23.cia.service.impl.common.UserSessionService;

/**
 * The Class UserRequestService.
 */
public class UserRequestService extends AbstractUserRequestService<UserRequest> {

    /** The parliament service. */
    private final ParliamentService parliamentService;

    /**
	 * Instantiates a new user request service.
	 * 
	 * @param userSessionService
	 *            the user session service
	 * @param parliamentService
	 *            the parliament service
	 */
    public UserRequestService(final UserSessionService userSessionService, final ParliamentService parliamentService) {
        super(userSessionService);
        this.parliamentService = parliamentService;
    }

    @Override
    public final ActionEvent createActionEvent(final UserRequest request, final UserSession userSession) {
        return new UserActionEvent(new Date(), userSession, request.getOperation());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class getSupportedService() {
        return UserRequest.class;
    }

    @Override
    public final ServiceResponse handleServiceRequest(final UserRequest request, final UserSessionDTO userSession) {
        switch(request.getOperation()) {
            case StartPage:
                break;
            case UserHistory:
                break;
            default:
                break;
        }
        return new UserResponse(userSession, parliamentService.getPoliticalParties());
    }
}
