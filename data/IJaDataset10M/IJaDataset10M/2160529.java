package com.hack23.cia.service.impl.content;

import com.hack23.cia.service.api.content.AbstractContentRequest;
import com.hack23.cia.service.impl.common.UserSessionService;

/**
 * The Class AbstractParliamentRequestService.
 */
abstract class AbstractParliamentRequestService<REQUEST extends AbstractContentRequest> extends AbstractUserRequestService<REQUEST> {

    /**
	 * Instantiates a new abstract parliament request service.
	 * 
	 * @param userSessionService the user session service
	 * @param parliamentContentService the parliament content service
	 */
    protected AbstractParliamentRequestService(final UserSessionService userSessionService, final ParliamentContentService parliamentContentService) {
        super(userSessionService, parliamentContentService);
    }
}
