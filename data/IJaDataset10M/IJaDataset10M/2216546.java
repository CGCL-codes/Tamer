package com.hack23.cia.service.impl.user;

import java.util.Date;
import java.util.List;
import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.ActionEvent;
import com.hack23.cia.model.application.impl.common.UserSession;
import com.hack23.cia.model.application.impl.user.SearchActionEvent;
import com.hack23.cia.model.sweden.impl.ParliamentMember;
import com.hack23.cia.service.api.common.ServiceResponse;
import com.hack23.cia.service.api.user.SearchRequest;
import com.hack23.cia.service.api.user.SearchResponse;
import com.hack23.cia.service.impl.common.ParliamentService;
import com.hack23.cia.service.impl.common.UserSessionService;

/**
 * The Class SearchRequestService.
 */
public class SearchRequestService extends AbstractParliamentRequestService<SearchRequest> {

    /**
	 * Instantiates a new search request service.
	 * 
	 * @param userSessionService
	 *            the user session service
	 * @param parliamentService
	 *            the parliament service
	 */
    public SearchRequestService(final UserSessionService userSessionService, final ParliamentService parliamentService) {
        super(userSessionService, parliamentService);
    }

    @Override
    public final ActionEvent createActionEvent(final SearchRequest request, final UserSession userSession) {
        return new SearchActionEvent(new Date(), userSession, request.getSearchArgument());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class getSupportedService() {
        return SearchRequest.class;
    }

    @Override
    public final ServiceResponse handleServiceRequest(final SearchRequest request, final UserSessionDTO userSession) {
        List<ParliamentMember> findParliamentMembersByName = getParliamentService().findParliamentMembersByName(request.getSearchArgument());
        SearchResponse searchResponse = new SearchResponse(userSession, findParliamentMembersByName);
        return searchResponse;
    }
}
