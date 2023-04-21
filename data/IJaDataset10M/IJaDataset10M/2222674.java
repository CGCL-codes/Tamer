package com.hack23.cia.model.impl.application.user;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.hack23.cia.model.api.application.ActionEventType;
import com.hack23.cia.model.api.application.ParliamentOperationType;
import com.hack23.cia.model.impl.application.common.UserSession;

/**
 * The Class AbstractParliamentActionEvent.
 */
@Entity
public abstract class AbstractParliamentActionEvent extends AbstractUserActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The citizen operation. */
    private ParliamentOperationType citizenOperation;

    /**
     * Instantiates a new abstract parliament action event.
     * 
     * @param actionEventType the action event type
     */
    public AbstractParliamentActionEvent(final ActionEventType actionEventType) {
        super(actionEventType);
    }

    /**
     * Instantiates a new abstract parliament action event.
     * 
     * @param createdDate the created date
     * @param userSession the user session
     */
    public AbstractParliamentActionEvent(final Date createdDate, final UserSession userSession) {
        super(createdDate, userSession);
    }

    /**
     * Gets the citizen operation.
     * 
     * @return the citizen operation
     */
    @Enumerated(EnumType.STRING)
    public ParliamentOperationType getCitizenOperation() {
        return citizenOperation;
    }

    /**
     * Sets the citizen operation.
     * 
     * @param citizenOperation the new citizen operation
     */
    public void setCitizenOperation(final ParliamentOperationType citizenOperation) {
        this.citizenOperation = citizenOperation;
    }
}
