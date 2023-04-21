package com.hack23.cia.service.impl.content;

import java.util.Date;
import com.hack23.cia.model.api.application.administration.UserSessionData;
import com.hack23.cia.model.api.application.events.ActionEvent;
import com.hack23.cia.service.api.common.ServiceRequest;
import com.hack23.cia.service.api.common.ServiceResponse;
import com.hack23.cia.service.api.control.UserRequest;
import com.hack23.cia.service.api.control.UserResponse;
import com.hack23.cia.service.api.dto.api.application.UserSessionDto;
import com.hack23.cia.service.impl.common.UserSessionService;

/**
 * The Class UserRequestService.
 */
public class UserRequestService extends AbstractUserRequestService<UserRequest> {

    /**
	 * Instantiates a new user request service.
	 * 
	 * @param userSessionService the user session service
	 * @param parliamentContentService the parliament content service
	 */
    public UserRequestService(final UserSessionService userSessionService, final ParliamentContentService parliamentContentService) {
        super(userSessionService, parliamentContentService);
    }

    @Override
    protected final ActionEvent createActionEvent(final UserRequest request, final UserSessionData userSession) {
        return getApplicationModelFactory().getApplicationEventsModelFactory().createUserActionEvent(new Date(), request.getOperation());
    }

    @Override
    public Class<? extends ServiceRequest> getSupportedService() {
        return UserRequest.class;
    }

    @Override
    protected final ServiceResponse handleServiceRequest(final UserRequest request, final UserSessionDto userSession) {
        switch(request.getOperation()) {
            case StartPage:
                break;
            case UserHistory:
                break;
            default:
                break;
        }
        return new UserResponse(userSession, getParliamentContentService().getPoliticalParties());
    }
}
