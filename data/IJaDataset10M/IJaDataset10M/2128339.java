package com.hack23.cia.model.application.impl.user;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.application.impl.common.UserSession;

/**
 * The Class CommitteeReportActionEvent.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DiscriminatorValue("CommitteeReportActionEvent")
public class CommitteeReportActionEvent extends AbstractParliamentActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The committee report id. */
    private Long committeeReportId;

    /**
	 * Instantiates a new committee report action event.
	 */
    public CommitteeReportActionEvent() {
    }

    /**
	 * Instantiates a new committee report action event.
	 * 
	 * @param createdDate
	 *            the created date
	 * @param userSession
	 *            the user session
	 * @param commiteeReportId
	 *            the commitee report id
	 */
    public CommitteeReportActionEvent(final Date createdDate, final UserSession userSession, final Long commiteeReportId) {
        super(createdDate, userSession);
        this.committeeReportId = commiteeReportId;
    }

    /**
	 * Gets the committee report id.
	 * 
	 * @return the committee report id
	 */
    public Long getCommitteeReportId() {
        return committeeReportId;
    }

    /**
	 * Sets the committee report id.
	 * 
	 * @param committeeReportId
	 *            the new committee report id
	 */
    public void setCommitteeReportId(Long committeeReportId) {
        this.committeeReportId = committeeReportId;
    }
}
