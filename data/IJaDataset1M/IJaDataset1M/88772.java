package com.hack23.cia.web.views.form;

import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.application.impl.common.LanguageContent;

/**
 * The Class LanguageContentForm.
 */
public class LanguageContentForm extends AbstractBaseEntityForm<LanguageContent> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "languageContentType", "contentPropertyName", "description", "content" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new language content form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param languageContent
	 *            the language content
	 */
    public LanguageContentForm(final UserSessionDTO userSessionDTO, final LanguageContent languageContent) {
        super(userSessionDTO, languageContent, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.LANGUAGE_CONTENT_FORM_DESCRIPTION));
    }
}
