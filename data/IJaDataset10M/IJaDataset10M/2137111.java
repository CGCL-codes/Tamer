package com.hack23.cia.model.impl.application.events.user;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.application.events.ActionEventType;
import com.hack23.cia.model.api.application.events.UserOperationType;

/**
 * The Class UserActionEvent.
 */
@Entity
@DiscriminatorValue("UserActionEvent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UserActionEvent extends AbstractUserActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The operation. */
    private UserOperationType operation;

    /**
	 * Instantiates a new user action event.
	 */
    public UserActionEvent() {
        super();
    }

    /**
	 * Instantiates a new user action event.
	 * 
	 * @param createdDate the created date
	 * @param operation the operation
	 */
    public UserActionEvent(final Date createdDate, final UserOperationType operation) {
        super(createdDate);
        this.operation = operation;
    }

    @Override
    @Transient
    public ActionEventType getActionEventType() {
        return ActionEventType.UserAction;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    @Enumerated(EnumType.STRING)
    public UserOperationType getOperation() {
        return operation;
    }

    /**
	 * Sets the operation.
	 * 
	 * @param operation the new operation
	 */
    public void setOperation(final UserOperationType operation) {
        this.operation = operation;
    }
}
