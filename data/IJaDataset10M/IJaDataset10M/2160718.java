package com.hack23.cia.web.api.content;

import com.hack23.cia.model.api.application.events.TopListOperationType;

/**
 * The Class PartyAction.
 */
public class PartyAction extends AbstractTopListAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 967092220196995079L;

    /** The party. */
    private final String party;

    /**
	 * Instantiates a new party action.
	 * 
	 * @param operation the operation
	 * @param party the party
	 */
    public PartyAction(final TopListOperationType operation, final String party) {
        super(operation);
        this.party = party;
    }

    /**
	 * Gets the party.
	 * 
	 * @return the party
	 */
    public final String getParty() {
        return party;
    }
}
