package com.hack23.cia.service.api.content;

/**
 * The Class CommitteeReportRequest.
 */
public class CommitteeReportRequest extends AbstractParliamentRequest {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The committee report id. */
    private final Long committeeReportId;

    /**
	 * Instantiates a new committee report request.
	 * 
	 * @param userSessionId the user session id
	 * @param committeeReportId the committee report id
	 */
    public CommitteeReportRequest(final Long userSessionId, final Long committeeReportId) {
        super(userSessionId);
        this.committeeReportId = committeeReportId;
    }

    /**
	 * Gets the committee report id.
	 * 
	 * @return the committee report id
	 */
    public final Long getCommitteeReportId() {
        return committeeReportId;
    }
}
