package org.mca.qmass.core.event;

import org.mca.yala.YALog;
import org.mca.yala.YALogFactory;

/**
 * User: malpay
 * Date: 10.May.2011
 * Time: 11:08:17
 */
public class LogEventClosure implements EventClosure {

    private static final YALog logger = YALogFactory.getLog(LogEventClosure.class);

    private static EventClosure instance = new LogEventClosure();

    private LogEventClosure() {
    }

    public static EventClosure getInstance() {
        return instance;
    }

    @Override
    public Object execute(Event event) throws Exception {
        logger.debug("event : " + event);
        return this;
    }
}
