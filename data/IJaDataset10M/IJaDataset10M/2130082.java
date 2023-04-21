package com.hack23.cia.model.application.impl.user;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.hack23.cia.model.application.impl.common.UserSession;

/**
 * The Class AbstractParliamentActionEvent.
 */
@Entity
public abstract class AbstractParliamentActionEvent extends AbstractUserActionEvent {

    /**
	 * The Enum Operation.
	 */
    public enum Operation {

        /** The Forum. */
        Forum, /** The Grading. */
        Grading, /** The Opinions. */
        Opinions, /** The Short_ comment. */
        Short_Comment, /** The Vote. */
        Vote
    }

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The citizen operation. */
    private Operation citizenOperation;

    /**
	 * Instantiates a new abstract parliament action event.
	 */
    public AbstractParliamentActionEvent() {
        super();
    }

    /**
	 * Instantiates a new abstract parliament action event.
	 * 
	 * @param createdDate
	 *            the created date
	 * @param userSession
	 *            the user session
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
    public Operation getCitizenOperation() {
        return citizenOperation;
    }

    /**
	 * Sets the citizen operation.
	 * 
	 * @param citizenOperation
	 *            the new citizen operation
	 */
    public void setCitizenOperation(Operation citizenOperation) {
        this.citizenOperation = citizenOperation;
    }
}
