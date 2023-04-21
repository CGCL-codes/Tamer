package com.hack23.cia.web.views.form.admin;

import com.hack23.cia.model.application.dto.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.application.impl.common.Language;
import com.hack23.cia.web.views.form.common.AbstractBaseEntityForm;

/**
 * The Class LanguageForm.
 */
public class LanguageForm extends AbstractBaseEntityForm<Language> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "resourceType", "name", "shortCode", "activeByDefault", "usageOrder" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new language form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param language
	 *            the language
	 */
    public LanguageForm(final UserSessionDTO userSessionDTO, final Language language) {
        super(userSessionDTO, language, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.LANGUAGE_FORM_DESCRIPTION));
    }
}
