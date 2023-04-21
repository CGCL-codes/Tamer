package com.hack23.cia.web.impl.ui.form.content;

import com.hack23.cia.model.api.application.content.LanguageContentKey;
import com.hack23.cia.model.api.sweden.events.ElectionData;
import com.hack23.cia.service.api.dto.api.application.UserSessionDto;

/**
 * The Class ElectionForm.
 */
public class ElectionForm extends AbstractParliamentForm<ElectionData> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "resourceType", "importStatus", "importedDate", "name" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new election form.
	 * 
	 * @param userSessionDTO the user session dto
	 * @param election the election
	 */
    public ElectionForm(final UserSessionDto userSessionDTO, final ElectionData election) {
        super(userSessionDTO, election, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(LanguageContentKey.ELECTION_FORM_DESCRIPTION));
    }
}
