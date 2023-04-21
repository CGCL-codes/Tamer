package com.hack23.cia.model.application.impl.admin;

import java.util.Date;
import javax.persistence.Entity;
import com.hack23.cia.model.application.impl.common.UserSession;

/**
 * The Class AbstractAgencyActionEvent.
 */
@Entity
public abstract class AbstractAgencyActionEvent extends AbstractConfigurationActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The agency id. */
    private Long agencyId;

    /**
	 * Instantiates a new abstract agency action event.
	 */
    public AbstractAgencyActionEvent() {
    }

    /**
	 * Instantiates a new abstract agency action event.
	 * 
	 * @param createdDate
	 *            the created date
	 * @param userSession
	 *            the user session
	 * @param agencyId
	 *            the agency id
	 */
    public AbstractAgencyActionEvent(final Date createdDate, final UserSession userSession, final Long agencyId) {
        super(createdDate, userSession);
        this.agencyId = agencyId;
    }

    /**
	 * Gets the agency id.
	 * 
	 * @return the agency id
	 */
    public Long getAgencyId() {
        return agencyId;
    }

    /**
	 * Sets the agency id.
	 * 
	 * @param agencyId
	 *            the new agency id
	 */
    public void setAgencyId(Long agencyId) {
        this.agencyId = agencyId;
    }
}
