package org.programmerplanet.intracollab.web.ticket;

import java.util.Date;
import org.programmerplanet.intracollab.model.Ticket;
import org.programmerplanet.intracollab.web.project.ActivityItem;

/**
 * An <code>ActivityItem</code> for a <code>Ticket</code>.
 * 
 * @author <a href="jfifield@programmerplanet.org">Joseph Fifield</a>
 * 
 * Copyright (c) 2009 Joseph Fifield
 */
public class TicketActivityItem implements ActivityItem {

    private Ticket ticket;

    public TicketActivityItem(Ticket ticket) {
        this.ticket = ticket;
    }

    /**
	 * @see org.programmerplanet.intracollab.web.project.ActivityItem#getDate()
	 */
    public Date getDate() {
        return ticket.getCreated();
    }

    /**
	 * @see org.programmerplanet.intracollab.web.project.ActivityItem#getDescription()
	 */
    public String getDescription() {
        return "Ticket #" + ticket.getId() + " created by " + ticket.getCreatedBy();
    }
}
