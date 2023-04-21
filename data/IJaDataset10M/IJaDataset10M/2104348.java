package com.hack23.cia.web.impl.ui.controller.content;

import com.hack23.cia.service.api.application.ApplicationManager;
import com.hack23.cia.service.api.content.ParliamentMemberRequest;
import com.hack23.cia.service.api.content.ParliamentMemberResponse;
import com.hack23.cia.web.api.content.ParliamentMemberAction;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ViewFactoryService;
import com.hack23.cia.web.impl.ui.viewfactory.impl.content.ParliamentMemberSummaryModelAndView;
import com.hack23.cia.web.impl.ui.viewfactory.impl.content.ParliamentMemberSummaryModelAndView.ParliamentMemberSummaryViewSpecification;

/**
 * The Class ParliamentMemberActionHandler.
 */
public class ParliamentMemberActionHandler extends AbstractUserActionHandler<ParliamentMemberAction, ParliamentMemberRequest, ParliamentMemberResponse> {

    /**
	 * Instantiates a new parliament member action handler.
	 * 
	 * @param viewFactoryService the view factory service
	 * @param applicationManager the application manager
	 */
    public ParliamentMemberActionHandler(final ViewFactoryService viewFactoryService, final ApplicationManager applicationManager) {
        super(viewFactoryService, applicationManager);
    }

    @Override
    public final ParliamentMemberRequest createServiceRequest(final ParliamentMemberAction action) {
        return new ParliamentMemberRequest(getUserStateService().getUserSessionId(), action.getParliamentMemberId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class getSupportedAction() {
        return ParliamentMemberAction.class;
    }

    @Override
    public final ModelAndView handleSuccessResponse(final ParliamentMemberAction action, final ParliamentMemberResponse response) {
        return new ParliamentMemberSummaryModelAndView(response.getUserSessionDTO(), action, ParliamentMemberSummaryViewSpecification.ParliamentMemberSummaryView, response.getParliamentMember(), response.getFriendList(), response.getEnemyList(), response.getFriendPartyList(), response.getEnemyPartyList(), response.getVotes(), response.getRegisterInformation());
    }
}
