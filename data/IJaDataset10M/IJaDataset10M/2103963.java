package com.niyue.sandbox.uclock.core;

import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class TickerEventFactory implements EventFactory {

    private static Logger logger = Logger.getLogger(TickerEventFactory.class);

    public List<Event> getEvents(DateTime datetime) {
        if (logger.isDebugEnabled()) {
            logger.debug(datetime + " ticker event");
        }
        List<Event> tickerList = new ArrayList<Event>();
        Event tickerEvent = new TickerEvent(datetime);
        tickerList.add(tickerEvent);
        return tickerList;
    }
}
