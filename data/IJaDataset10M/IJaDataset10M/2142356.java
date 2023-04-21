package com.hack23.cia.web.controller.common;

import com.hack23.cia.service.api.application.ApplicationManager;
import com.hack23.cia.service.api.common.ApplicationErrorRequest;
import com.hack23.cia.service.api.common.ApplicationErrorResponse;
import com.hack23.cia.web.action.common.AbstractAction;
import com.hack23.cia.web.viewfactory.api.common.ErrorMessageModelAndView;
import com.hack23.cia.web.viewfactory.api.common.ModelAndView;
import com.hack23.cia.web.viewfactory.api.common.ViewFactoryService;
import com.hack23.cia.web.viewfactory.api.common.ErrorMessageModelAndView.ErrorViewSpecification;

/**
 * The Class DefaultActionHandler.
 */
public class DefaultActionHandler extends AbstractGenericHandler<AbstractAction, ApplicationErrorRequest, ApplicationErrorResponse> {

    /**
	 * Instantiates a new default action handler.
	 * 
	 * @param viewFactoryService
	 *            the view factory service
	 * @param applicationManager
	 *            the application manager
	 */
    public DefaultActionHandler(final ViewFactoryService viewFactoryService, final ApplicationManager applicationManager) {
        super(viewFactoryService, applicationManager);
    }

    @Override
    public final ApplicationErrorRequest createServiceRequest(final AbstractAction action) {
        return new ApplicationErrorRequest(getUserStateService().getUserSessionId(), new StackTraceElement[0], "Missing Action Handler for :" + action.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public final Class getSupportedAction() {
        return AbstractAction.class;
    }

    @Override
    public final ModelAndView handleSuccessResponse(final AbstractAction action, final ApplicationErrorResponse response) {
        ApplicationErrorResponse applicationErrorResponse = response;
        return new ErrorMessageModelAndView(response.getUserSessionDTO(), action, ErrorViewSpecification.ErrorMessageView, applicationErrorResponse.getErrorMessage());
    }
}
