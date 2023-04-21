package com.hack23.cia.web.api.content;

import com.hack23.cia.model.api.application.events.LanguageContentOperationType;
import com.hack23.cia.model.api.dto.common.AgencyDto;
import com.hack23.cia.model.api.dto.common.LanguageContentDto;
import com.hack23.cia.web.api.configuration.AbstractConfigurationAction;

/**
 * The Class LanguageContentAction.
 */
public class LanguageContentAction extends AbstractConfigurationAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 967092220196995079L;

    /** The agency. */
    private final AgencyDto agency;

    /** The language content. */
    private final LanguageContentDto languageContent;

    /** The operation. */
    private final LanguageContentOperationType operation;

    /**
	 * Instantiates a new language content action.
	 * 
	 * @param operation the operation
	 * @param agency the agency
	 * @param languageContent the language content
	 */
    public LanguageContentAction(final LanguageContentOperationType operation, final AgencyDto agency, final LanguageContentDto languageContent) {
        this.operation = operation;
        this.agency = agency;
        this.languageContent = languageContent;
    }

    /**
	 * Gets the agency.
	 * 
	 * @return the agency
	 */
    public final AgencyDto getAgency() {
        return agency;
    }

    /**
	 * Gets the language content.
	 * 
	 * @return the language content
	 */
    public final LanguageContentDto getLanguageContent() {
        return languageContent;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    public final LanguageContentOperationType getOperation() {
        return operation;
    }
}
