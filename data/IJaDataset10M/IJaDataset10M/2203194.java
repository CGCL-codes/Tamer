package net.solarnetwork.central.dras.biz.dao;

import static net.solarnetwork.central.dras.biz.Notifications.*;
import static net.solarnetwork.central.scheduler.SchedulerConstants.*;
import java.util.HashMap;
import java.util.Map;
import net.solarnetwork.central.dras.dao.EventDao;
import net.solarnetwork.central.dras.domain.Event;
import net.solarnetwork.central.scheduler.EventHandlerSupport;
import org.joda.time.DateTime;
import org.osgi.service.event.EventAdmin;
import org.osgi.service.event.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * {@link EventHandler} for event change requests, to schedule
 * publication of the event at the event date.
 * 
 * @author matt
 * @version $Revision: 1694 $
 */
@Service
public class EventScheduler extends EventHandlerSupport {

    private EventAdmin eventAdmin;

    private EventDao eventDao;

    /**
	 * Constructor.
	 * 
	 * @param eventAdmin the EventAdmin
	 * @param eventDao the EventDao
	 */
    @Autowired
    public EventScheduler(EventAdmin eventAdmin, EventDao eventDao) {
        this.eventAdmin = eventAdmin;
        this.eventDao = eventDao;
    }

    @Override
    protected void handleEventInternal(org.osgi.service.event.Event event) {
        final Long eventId = (Long) event.getProperty(ENTITY_IDENTITY);
        Event drasEvent = eventDao.get(eventId);
        if (drasEvent == null) {
            log.warn("Event not found for ID {}, not scheduling job", eventId);
            return;
        }
        final DateTime publishDate;
        if (drasEvent.getNotificationDate() != null && drasEvent.getNotificationDate().isAfterNow()) {
            publishDate = drasEvent.getNotificationDate();
        } else if (drasEvent.getEndDate().isAfterNow()) {
            publishDate = new DateTime();
        } else {
            log.warn("Event {} ended in the past, or notification date not available", eventId);
            return;
        }
        Map<String, Object> jobProps = new HashMap<String, Object>(5);
        jobProps.put("eventId", eventId);
        Map<String, Object> publishProps = new HashMap<String, Object>(5);
        publishProps.put(JOB_TOPIC, "net/solarnetwork/central/dras/ddrs/PUBLISH_EVENT");
        publishProps.put(JOB_GROUP, "DDRS");
        publishProps.put(JOB_ID, "event:" + eventId);
        publishProps.put(JOB_DATE, publishDate.getMillis());
        publishProps.put(JOB_PROPERTIES, jobProps);
        org.osgi.service.event.Event publishJob = new org.osgi.service.event.Event(TOPIC_JOB_REQUEST, publishProps);
        eventAdmin.postEvent(publishJob);
    }
}
