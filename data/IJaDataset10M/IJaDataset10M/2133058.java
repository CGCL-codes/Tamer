package com.hack23.cia.model.application.impl.admin;

import java.util.Date;
import javax.persistence.Entity;
import com.hack23.cia.model.application.api.ActionEventType;
import com.hack23.cia.model.application.impl.common.UserSession;

/**
 * The Class AbstractSoftwareAgentActionEvent.
 */
@Entity
public abstract class AbstractSoftwareAgentActionEvent extends AbstractAdminActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new abstract software agent action event.
	 * 
	 * @param actionEventType
	 *            the action event type
	 */
    public AbstractSoftwareAgentActionEvent(final ActionEventType actionEventType) {
        super(actionEventType);
    }

    /**
	 * Instantiates a new abstract software agent action event.
	 * 
	 * @param createdDate
	 *            the created date
	 * @param userSession
	 *            the user session
	 */
    public AbstractSoftwareAgentActionEvent(final Date createdDate, final UserSession userSession) {
        super(createdDate, userSession);
    }
}
