package com.hack23.cia.web.viewfactory.api.admin;

import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.web.action.common.ControllerAction;

/**
 * The Class AbstractAgencyModelAndView.
 */
public abstract class AbstractAgencyModelAndView extends AbstractConfigurationModelAndView {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The agency. */
    private final Agency agency;

    /**
	 * Instantiates a new abstract agency model and view.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param controllerAction
	 *            the controller action
	 * @param agency
	 *            the agency
	 */
    public AbstractAgencyModelAndView(final UserSessionDTO userSessionDTO, final ControllerAction controllerAction, final Agency agency) {
        super(userSessionDTO, controllerAction);
        this.agency = agency;
    }

    /**
	 * Gets the agency.
	 * 
	 * @return the agency
	 */
    public final Agency getAgency() {
        return agency;
    }
}
