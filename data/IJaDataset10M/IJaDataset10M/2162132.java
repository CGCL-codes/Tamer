package com.hack23.cia.web.views.form.admin;

import com.hack23.cia.model.application.dto.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.web.views.form.common.AbstractBaseEntityForm;

/**
 * The Class AgencyForm.
 */
public class AgencyForm extends AbstractBaseEntityForm<Agency> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name" };

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
    public AgencyForm(final UserSessionDTO userSessionDTO, final Agency agency) {
        super(userSessionDTO, agency, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.AGENCY_FORM_DESCRIPTION));
    }
}
