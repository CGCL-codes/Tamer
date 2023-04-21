package com.hack23.cia.model.impl.application.user;

import java.util.Date;
import javax.persistence.Entity;
import com.hack23.cia.model.api.application.ActionEventType;
import com.hack23.cia.model.impl.application.common.AbstractActionEvent;
import com.hack23.cia.model.impl.application.common.UserSession;

/**
 * The Class AbstractUserActionEvent.
 */
@Entity
public abstract class AbstractUserActionEvent extends AbstractActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new abstract user action event.
     * 
     * @param actionEventType the action event type
     */
    public AbstractUserActionEvent(final ActionEventType actionEventType) {
        super(actionEventType);
    }

    /**
     * Instantiates a new abstract user action event.
     * 
     * @param createdDate the created date
     * @param userSession the user session
     */
    public AbstractUserActionEvent(final Date createdDate, final UserSession userSession) {
        super(createdDate, userSession);
    }
}
