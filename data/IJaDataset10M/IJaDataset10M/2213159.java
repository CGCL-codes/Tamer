package com.hack23.cia.web.api.configuration;

import com.hack23.cia.model.api.application.events.PortalOperationType;
import com.hack23.cia.model.api.dto.common.PortalDto;

/**
 * The Class PortalAction.
 */
public class PortalAction extends AbstractConfigurationAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** The operation. */
    private final PortalOperationType operation;

    /** The portal. */
    private final PortalDto portal;

    /**
	 * Instantiates a new portal action.
	 * 
	 * @param operation the operation
	 * @param portal the portal
	 */
    public PortalAction(final PortalOperationType operation, final PortalDto portal) {
        super();
        this.operation = operation;
        this.portal = portal;
    }

    /**
	 * Gets the operation.
	 * 
	 * @return the operation
	 */
    public final PortalOperationType getOperation() {
        return operation;
    }

    /**
	 * Gets the portal.
	 * 
	 * @return the portal
	 */
    public final PortalDto getPortal() {
        return portal;
    }
}
