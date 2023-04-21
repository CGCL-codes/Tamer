package com.hack23.cia.service.api.admin;

import com.hack23.cia.model.application.Agency;

/**
 * The Class NewPortalRequest.
 */
public class NewPortalRequest extends AbstractAdminRequest {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The agency. */
    private final Agency agency;

    /**
     * Instantiates a new new portal request.
     * 
     * @param userSessionId the user session id
     * @param agency the agency
     */
    public NewPortalRequest(final Long userSessionId, final Agency agency) {
        super(userSessionId);
        this.agency = agency;
    }

    /**
     * Gets the agency.
     * 
     * @return the agency
     */
    public final Agency getAgency() {
        return agency;
    }
}
