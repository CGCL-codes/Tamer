package org.mundau.market.event;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * 
 * @author elizeu
 *
 */
public class EventBuilder {

    protected static Logger logger = Logger.getLogger(EventBuilder.class);

    public static AbstractRequestEvent createEvent(RequestType rType) {
        AbstractRequestEvent event = null;
        logger.setLevel((Level) Level.INFO);
        switch(rType) {
            case AUCTION:
                event = new BidRequestEvent();
                break;
            case LEASING:
                event = new LeasingRequestEvent();
                break;
        }
        logger.debug("Event-" + event.getId() + ": " + rType.toString());
        return event;
    }
}
