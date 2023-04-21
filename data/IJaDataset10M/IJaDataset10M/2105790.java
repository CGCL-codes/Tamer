package com.hack23.cia.web.impl.ui.controller.content;

import com.hack23.cia.service.api.application.ApplicationManager;
import com.hack23.cia.service.api.content.BallotRequest;
import com.hack23.cia.service.api.content.BallotResponse;
import com.hack23.cia.web.api.content.BallotAction;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ModelAndView;
import com.hack23.cia.web.impl.ui.viewfactory.api.common.ViewFactoryService;
import com.hack23.cia.web.impl.ui.viewfactory.api.content.VotesModelAndView;
import com.hack23.cia.web.impl.ui.viewfactory.api.content.VotesViewSpecification;

/**
 * The Class BallotActionHandler.
 */
public class BallotActionHandler extends AbstractUserActionHandler<BallotAction, BallotRequest, BallotResponse> {

    /**
	 * Instantiates a new ballot action handler.
	 * 
	 * @param viewFactoryService the view factory service
	 * @param applicationManager the application manager
	 */
    public BallotActionHandler(final ViewFactoryService viewFactoryService, final ApplicationManager applicationManager) {
        super(viewFactoryService, applicationManager);
    }

    @Override
    public final BallotRequest createServiceRequest(final BallotAction action) {
        return new BallotRequest(getUserStateService().getUserSessionId(), action.getBallotId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class getSupportedAction() {
        return BallotAction.class;
    }

    @Override
    public final ModelAndView handleSuccessResponse(final BallotAction action, final BallotResponse response) {
        return new VotesModelAndView(response.getUserSessionDTO(), action, VotesViewSpecification.BallotView, response.getFindLastVotesByBallotId());
    }
}
