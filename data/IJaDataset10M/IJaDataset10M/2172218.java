package com.hack23.cia.web.impl.ui.form.content;

import com.hack23.cia.model.api.application.content.LanguageContentKey;
import com.hack23.cia.model.api.sweden.configuration.CommitteeData;
import com.hack23.cia.service.api.dto.api.application.UserSessionDto;

/**
 * The Class CommitteeForm.
 */
public class CommitteeForm extends AbstractParliamentForm<CommitteeData> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "resourceType", "importStatus", "importedDate", "name", "shortCode" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new committee form.
	 * 
	 * @param userSessionDTO the user session dto
	 * @param committee the committee
	 */
    public CommitteeForm(final UserSessionDto userSessionDTO, final CommitteeData committee) {
        super(userSessionDTO, committee, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(LanguageContentKey.COMMITTEE_FORM_DESCRIPTION));
    }
}
