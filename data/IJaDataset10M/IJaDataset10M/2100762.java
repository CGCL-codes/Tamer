package com.hack23.cia.model.impl.application.events.user;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import com.hack23.cia.model.api.application.events.TopListOperationType;

/**
 * The Class AbstractTopListActionEvent.
 */
@Entity
abstract class AbstractTopListActionEvent extends AbstractParliamentActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The operation. */
    private TopListOperationType operation;

    /**
	 * Instantiates a new abstract top list action event.
	 */
    protected AbstractTopListActionEvent() {
        super();
    }

    /**
	 * Instantiates a new abstract top list action event.
	 * 
	 * @param createdDate the created date
	 * @param operation the operation
	 */
    protected AbstractTopListActionEvent(final Date createdDate, final TopListOperationType operation) {
        super(createdDate);
        this.operation = operation;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    @Enumerated(EnumType.STRING)
    public TopListOperationType getOperation() {
        return operation;
    }

    /**
	 * Sets the operation.
	 * 
	 * @param operation the new operation
	 */
    public void setOperation(final TopListOperationType operation) {
        this.operation = operation;
    }
}
