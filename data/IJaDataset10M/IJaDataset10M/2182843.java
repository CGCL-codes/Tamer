package com.hack23.cia.model.application.user;

import java.util.Date;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import com.hack23.cia.model.application.common.AbstractActionEvent;
import com.hack23.cia.model.application.common.UserSession;

/**
 * The Class ApplicationActionEvent.
 */
@Entity
@DiscriminatorValue("ApplicationActionEvent")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ApplicationActionEvent extends AbstractActionEvent {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new application action event.
     */
    public ApplicationActionEvent() {
    }

    /**
     * Instantiates a new application action event.
     * 
     * @param createdDate the created date
     * @param userSession the user session
     */
    public ApplicationActionEvent(final Date createdDate, final UserSession userSession) {
        super(createdDate, userSession);
    }
}
