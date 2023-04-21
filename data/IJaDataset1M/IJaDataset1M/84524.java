package com.hack23.cia.web.views.form;

import com.hack23.cia.model.application.dto.common.UserSessionDTO;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.sweden.impl.ParliamentMember;

/**
 * The Class ParliamentMemberForm.
 */
public class ParliamentMemberForm extends AbstractBaseEntityForm<ParliamentMember> {

    /** The Constant NATURAL_COL_ORDER. */
    private static final Object[] NATURAL_COL_ORDER = new Object[] { "id", "name", "electoralRegion", "party" };

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new parliament member form.
	 * 
	 * @param userSessionDTO
	 *            the user session dto
	 * @param parliamentMember
	 *            the parliament member
	 */
    public ParliamentMemberForm(final UserSessionDTO userSessionDTO, final ParliamentMember parliamentMember) {
        super(userSessionDTO, parliamentMember, NATURAL_COL_ORDER);
        setDescription(userSessionDTO.getLanguageResource(Agency.LanguageContentKey.PARLIAMENT_MEMBER_FORM_DESCRIPTION));
    }
}
