package com.hack23.cia.web.views.form;

import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.application.impl.common.Portal;

/**
 * The Class PortalForm.
 */
public class PortalForm extends AbstractBaseEntityForm<Portal> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "name", "matchesUrl", "titleDescription" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new portal form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param portal
	 *            the portal
	 */
    public PortalForm(final UserSessionDTO userSessionDTO, final Portal portal) {
        super(userSessionDTO, portal, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.PORTAL_FORM_DESCRIPTION));
    }
}
