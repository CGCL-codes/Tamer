package com.hack23.cia.web.impl.ui.viewfactory.api.admin;

import com.hack23.cia.model.api.dto.application.AgencyDto;
import com.hack23.cia.model.api.dto.application.UserSessionDto;
import com.hack23.cia.web.api.common.ControllerAction;

/**
 * The Class AbstractAgencyModelAndView.
 */
public abstract class AbstractAgencyModelAndView extends AbstractConfigurationModelAndView {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The agency. */
    private final AgencyDto agency;

    /**
     * Instantiates a new abstract agency model and view.
     * 
     * @param userSessionDTO the user session dto
     * @param controllerAction the controller action
     * @param agencyDto the agency dto
     */
    public AbstractAgencyModelAndView(final UserSessionDto userSessionDTO, final ControllerAction controllerAction, final AgencyDto agencyDto) {
        super(userSessionDTO, controllerAction);
        this.agency = agencyDto;
    }

    /**
     * Gets the agency.
     * 
     * @return the agency
     */
    public final AgencyDto getAgency() {
        return agency;
    }
}
