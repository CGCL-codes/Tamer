package com.hack23.cia.web.views.form.admin;

import com.hack23.cia.model.application.dto.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.sweden.impl.Parliament;
import com.hack23.cia.web.views.form.common.AbstractBaseEntityForm;

/**
 * The Class ParliamentForm.
 */
public class ParliamentForm extends AbstractBaseEntityForm<Parliament> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "shortCode", "description", "url" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new parliament form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param parliament
	 *            the parliament
	 */
    public ParliamentForm(final UserSessionDTO userSessionDTO, final Parliament parliament) {
        super(userSessionDTO, parliament, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.PARLIAMENT_FORM_DESCRIPTION));
    }
}
