package net.sf.reqbook.services.events;

/**
 * $Id: EventFactory.java,v 1.1 2006/01/09 09:36:09 pavel_sher Exp $
 *
 * @author Pavel Sher
 */
public class EventFactory implements EventCodes {

    public static ReqbookEvent progressUpdatedEvent(int currentProgress) {
        return new ReqbookEvent(PROGRESS_UPDATED, new Integer(currentProgress));
    }
}
