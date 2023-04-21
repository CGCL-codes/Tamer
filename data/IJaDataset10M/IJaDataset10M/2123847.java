package org.pinggcal;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.calendar.CalendarEntry;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarFeed;
import com.google.gdata.data.calendar.ColorProperty;
import com.google.gdata.data.calendar.HiddenProperty;
import com.google.gdata.data.calendar.TimeZoneProperty;
import com.google.gdata.data.extensions.Reminder;
import com.google.gdata.data.extensions.When;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.util.ServiceException;

/**
 * 
 * @author Mirco
 */
public class Main {

    private static String userName = "";

    private static String password = "";

    private static String serverName = "";

    private static final String serviceName = "ping-gcal";

    private static final String METAFEED_URL_BASE = "http://www.google.com/calendar/feeds/";

    private static final String EVENT_FEED_URL_SUFFIX = "/private/full";

    private static final String eventFeedUrl = METAFEED_URL_BASE + userName + EVENT_FEED_URL_SUFFIX;

    private static boolean alreadyExistCalendar = false;

    private static TimeZone currentTimeZone = Calendar.getInstance().getTimeZone();

    /**
	 * @param args
	 *            the command line arguments
	 */
    public static void main(String[] args) {
        System.out.println("Ping-GCal");
        boolean pingAnswer = false;
        CalendarEntry calendar = null;
        if (checkAppParams(args)) {
            pingAnswer = PingUtil.testServer(serverName);
            if (!pingAnswer) {
                try {
                    CalendarService myService = new CalendarService(serviceName);
                    myService.setUserCredentials(userName, password);
                    System.out.println("Login to Google Calendar as : " + userName);
                    System.out.println("Current TimeZone is : " + currentTimeZone.getID());
                    System.out.println("Checking your calendars...");
                    URL feedUrl = new URL("http://www.google.com/calendar/feeds/default/allcalendars/full");
                    CalendarFeed resultFeed = myService.getFeed(feedUrl, CalendarFeed.class);
                    for (int i = 0; i < resultFeed.getEntries().size(); i++) {
                        CalendarEntry entry = resultFeed.getEntries().get(i);
                        if (entry.getTitle().getPlainText().equalsIgnoreCase(serverName)) {
                            alreadyExistCalendar = true;
                            calendar = entry;
                            System.out.println("Found calendar: " + serverName);
                            break;
                        }
                    }
                    if (!alreadyExistCalendar) {
                        calendar = createCalendar(myService, serverName);
                        System.out.println("Created calendar: " + serverName);
                    }
                    URL EVENTS_URL = new URL(METAFEED_URL_BASE + calendar.getId().substring(calendar.getId().lastIndexOf("/") + 1) + EVENT_FEED_URL_SUFFIX);
                    CalendarEventEntry eventEntry = new CalendarEventEntry();
                    eventEntry.setTitle(new PlainTextConstruct(String.format("Server %s down or network unreachable.", serverName)));
                    eventEntry.setContent(new PlainTextConstruct(""));
                    DateTime startTime = new DateTime(now(), currentTimeZone);
                    DateTime endTime = new DateTime(nowPlusSomeMinutes(1), currentTimeZone);
                    When eventTime = new When();
                    eventTime.setStartTime(startTime);
                    eventTime.setEndTime(endTime);
                    eventEntry.addTime(eventTime);
                    Where where = new Where("", "", currentTimeZone.getID());
                    eventEntry.addLocation(where);
                    int reminderMinutes = 0;
                    Method methodType = Method.SMS;
                    Reminder reminder = new Reminder();
                    reminder.setMinutes(reminderMinutes);
                    reminder.setMethod(methodType);
                    eventEntry.getReminder().add(reminder);
                    myService.insert(EVENTS_URL, eventEntry);
                    System.out.println("Created event: " + eventEntry.getTitle().getPlainText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static CalendarEntry createCalendar(CalendarService myService, String calendarName) {
        CalendarEntry returnedCalendar = null;
        try {
            CalendarEntry calendar = new CalendarEntry();
            calendar.setTitle(new PlainTextConstruct(calendarName));
            calendar.setSummary(new PlainTextConstruct("Calendar created by TestPingGCal."));
            calendar.setTimeZone(new TimeZoneProperty(currentTimeZone.getID()));
            calendar.setHidden(HiddenProperty.FALSE);
            calendar.setColor(new ColorProperty("#2952A3"));
            calendar.addLocation(new Where("", "", currentTimeZone.getID()));
            URL postUrl = new URL("http://www.google.com/calendar/feeds/default/owncalendars/full");
            returnedCalendar = myService.insert(postUrl, calendar);
        } catch (IOException iOException) {
        } catch (ServiceException serviceException) {
        }
        return returnedCalendar;
    }

    private static Date now() {
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    private static Date nowPlusSomeMinutes(int munutes) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, munutes);
        return cal.getTime();
    }

    private static boolean checkAppParams(String args[]) {
        boolean res = false;
        if (args.length == 3) {
            userName = args[0];
            password = args[1];
            serverName = args[2];
            res = true;
        } else {
            System.out.println("Usage:");
            System.out.println("java -jar ping-gcal username password server");
            System.out.println();
        }
        return res;
    }
}
