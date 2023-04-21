package com.hack23.cia.web.views.ui;

import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;

/**
 * The Class AgencyForm.
 */
public class AgencyForm extends AbstractBaseEntityForm<Agency> {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new agency form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param agency
	 *            the agency
	 */
    protected AgencyForm(final UserSessionDTO userSessionDTO, final Agency agency) {
        super(userSessionDTO, agency);
    }
}
