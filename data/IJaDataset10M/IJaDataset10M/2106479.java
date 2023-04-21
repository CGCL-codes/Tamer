package com.hack23.cia.model.impl.application.events.admin;

import java.util.Date;
import javax.persistence.Entity;

/**
 * The Class AbstractConfigurationActionEvent.
 */
@Entity
abstract class AbstractConfigurationActionEvent extends AbstractAdminActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
	 * Instantiates a new abstract configuration action event.
	 */
    protected AbstractConfigurationActionEvent() {
        super();
    }

    /**
	 * Instantiates a new abstract configuration action event.
	 * 
	 * @param createdDate the created date
	 */
    protected AbstractConfigurationActionEvent(final Date createdDate) {
        super(createdDate);
    }
}
