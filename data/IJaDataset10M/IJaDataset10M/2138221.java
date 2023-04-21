package org.webical.dao.impl.util;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.webical.ApplicationSettings;
import org.webical.Calendar;
import org.webical.Event;
import org.webical.TestUtils;
import org.webical.User;
import org.webical.dao.DaoException;
import org.webical.dao.hibernateImpl.CalendarDaoHibernateImpl;
import org.webical.dao.hibernateImpl.EventDaoWebDavHibernateBufferedImpl;
import org.webical.dao.impl.DataBaseTest;
import org.webical.dao.util.ComponentFactory;
import org.webical.dao.util.WebDavCalendarSynchronisation;

/**
 * @author paul
 * Class for testing the synchronisation of remote WebDav calendars
 *
 */
public class WebDavCalendarSynchronisationTest extends DataBaseTest {

    private static Log log = LogFactory.getLog(WebDavCalendarSynchronisationTest.class);

    WebDavCalendarSynchronisation webDavCalendarSynchronisation = new WebDavCalendarSynchronisation();

    private EventDaoWebDavHibernateBufferedImpl eventDao;

    private CalendarDaoHibernateImpl calendarDao;

    /**
	 *  method for getting a remote calendar with wrong input
	 */
    public void testWrongInputGetRemoteFile() {
        try {
            webDavCalendarSynchronisation.getEventsFromRemoteCalendar(null);
            fail("Input not checked: no calendar");
        } catch (DaoException e) {
        }
        try {
            webDavCalendarSynchronisation.getEventsFromRemoteCalendar(new Calendar());
            fail("Input not checked: no url");
        } catch (DaoException e) {
        }
        Calendar calendar = new Calendar();
        try {
            calendar.setUrl("http:/w@w.fouturl");
            webDavCalendarSynchronisation.getEventsFromRemoteCalendar(calendar);
            fail("Input not checked: wrong url(1)");
        } catch (DaoException e) {
        }
        try {
            calendar.setUrl("http://localhost/filecalendar");
            webDavCalendarSynchronisation.getEventsFromRemoteCalendar(calendar);
            fail("Input not checked: wrong url(2)");
        } catch (DaoException e) {
        }
    }

    /**
	 *  Method for testing retrieving a remote webdav calendar
	 * @throws DaoException 
	 */
    public void testCorrectInputGetRemoteFile() throws DaoException {
        Calendar calendar = getCalendar("webical");
        webDavCalendarSynchronisation.getEventsFromRemoteCalendar(calendar);
    }

    /**
	 * Method for testing the build of a remote calendar ics 
	 * In this case there are no changes.
	 */
    public void testBuildRemoteCalendarFromCalendar() {
        Calendar calendar = getCalendar("webical");
        net.fortuna.ical4j.model.Calendar ical4jCalendar = new net.fortuna.ical4j.model.Calendar();
        try {
            ComponentFactory.buildComponentsFromIcal4JCalendar(calendar, ical4jCalendar);
            List<Event> currentEvents = eventDao.getAllEvents(calendar);
            int oldEventCount = currentEvents.size();
            webDavCalendarSynchronisation.writeToRemoteCalendarFile(calendar, currentEvents);
            int newEventCount = eventDao.getAllEvents(calendar).size();
            assertEquals(oldEventCount, newEventCount);
        } catch (ParseException e) {
            fail("ParseException");
        } catch (DaoException e) {
            fail("DaoException");
        }
    }

    private Calendar getCalendar(String userId) {
        User user = new User();
        user.setUserId(userId);
        try {
            return calendarDao.getCalendars(user).get(0);
        } catch (DaoException e) {
            log.error(e, e);
            fail("Could not retrieve calendar " + e);
        }
        return null;
    }

    /**
	 * Sets up the dao for each test
	 * @throws Exception 
	 */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        eventDao = new EventDaoWebDavHibernateBufferedImpl();
        ApplicationSettings applicationSettings = new ApplicationSettings();
        applicationSettings.setCalendarRefreshTimeMs(300);
        TestUtils.initializeApplicationSettingsFactory(applicationSettings);
        calendarDao = new CalendarDaoHibernateImpl();
    }

    /**
	 * Create a default event
	 * @return
	 */
    public Event getEvent() {
        Event event = new Event();
        event.setAttach(new HashSet<String>(Arrays.asList(new String[] { "attach" })));
        event.setAttendee(new HashSet<String>(Arrays.asList(new String[] { "attendee" })));
        event.setCategories(new HashSet<String>(Arrays.asList(new String[] { "category" })));
        event.setClazz("clazz");
        event.setComment(new HashSet<String>(Arrays.asList(new String[] { "comment" })));
        event.setContact(new HashSet<String>(Arrays.asList(new String[] { "contact" })));
        event.setCreated(new Date());
        event.setDescription("description");
        event.setDtEnd(new Date());
        event.setDtStamp(new Date());
        event.setDtStart(new Date());
        event.setDuration("duration?");
        event.setExDate(new HashSet<Date>(Arrays.asList(new Date[] { new Date() })));
        event.setExRule(new HashSet<String>(Arrays.asList(new String[] { "exrule" })));
        event.setLocation("adam");
        event.setOrganizer("me");
        event.setPriority(1);
        event.setrDate(new HashSet<Date>(Arrays.asList(new Date[] { new Date() })));
        event.setRelated(new HashSet<String>(Arrays.asList(new String[] { "related" })));
        event.setResources(new HashSet<String>(Arrays.asList(new String[] { "resources" })));
        event.setrRule(new HashSet<String>(Arrays.asList(new String[] { "rrule" })));
        event.setrStatus(new HashSet<String>(Arrays.asList(new String[] { "rstatus" })));
        event.setSeq(2);
        event.setStatus("status");
        event.setSummary("summary");
        event.setTransp("transp");
        event.setUid("gfd");
        event.setUrl("http://www.func.nl");
        Map<String, String> xprops = new HashMap<String, String>();
        xprops.put("X-prop-1", "somethingorother");
        event.setxProps(xprops);
        return event;
    }
}
