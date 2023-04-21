package com.hack23.cia.model.impl.application.events.admin;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.api.application.events.ActionEventType;
import com.hack23.cia.model.api.application.events.PortalOperationType;

/**
 * The Class PortalActionEvent.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@DiscriminatorValue("PortalActionEvent")
public class PortalActionEvent extends AbstractConfigurationActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The operation. */
    private PortalOperationType operation;

    /** The portal id. */
    private Long portalId;

    /**
	 * Instantiates a new portal action event.
	 */
    public PortalActionEvent() {
        super();
    }

    /**
	 * Instantiates a new portal action event.
	 * 
	 * @param createdDate the created date
	 * @param operation the operation
	 * @param portalId the portal id
	 */
    public PortalActionEvent(final Date createdDate, final PortalOperationType operation, final Long portalId) {
        super(createdDate);
        this.operation = operation;
        this.portalId = portalId;
    }

    @Override
    @Transient
    public ActionEventType getActionEventType() {
        return ActionEventType.PortalAction;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    @Enumerated(EnumType.STRING)
    public PortalOperationType getOperation() {
        return operation;
    }

    /**
	 * Gets the portal id.
	 * 
	 * @return the portal id
	 */
    public Long getPortalId() {
        return portalId;
    }

    /**
	 * Sets the operation.
	 * 
	 * @param operation the new operation
	 */
    public void setOperation(final PortalOperationType operation) {
        this.operation = operation;
    }

    /**
	 * Sets the portal id.
	 * 
	 * @param portalId the new portal id
	 */
    public void setPortalId(final Long portalId) {
        this.portalId = portalId;
    }
}
