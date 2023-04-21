package gcal2excel;

import com.google.gdata.client.Query;
import com.google.gdata.client.calendar.CalendarQuery;
import com.google.gdata.client.calendar.CalendarService;
import com.google.gdata.data.DateTime;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.calendar.CalendarEventFeed;
import com.google.gdata.data.extensions.When;
import com.google.gdata.util.ServiceException;
import com.google.gdata.data.*;
import com.google.gdata.data.extensions.Where;
import com.google.gdata.data.PlainTextConstruct;
import com.google.gdata.data.TextConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.ListIterator;
import jxl.*;
import jxl.write.*;
import jxl.write.WriteException;

/**
 * Creates an excel file from google calendar
 * using Google Calendar API and JExcel API
 */
public class Converter {

    private final String METAFEED_URL_BASE = "http://www.google.com/calendar/feeds/";

    private final String SINGLE_FEED_URL_SUFFIX = "/private/full";

    private static URL singleFeedUrl = null;

    private final long MILISECONDS_IN_HOUR = 60 * 60 * 1000;

    private String userName;

    private String userPassword;

    private String calendarId;

    private String errorMessage;

    public Converter(String userName, String userPassword, String calendarId) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.calendarId = calendarId;
        this.errorMessage = "";
    }

    public boolean convert(String starts, String ends) {
        String fileName = this.userName + " TimeSheet From " + starts + " To " + ends + ".xls";
        Vector<Event> events = null;
        events = getCalendarData(this.userName, this.userPassword, this.calendarId, starts, ends);
        if (events == null) return false;
        return writeExcel(events, fileName);
    }

    private boolean writeExcel(Vector<Event> events, String fileName) {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName));
            WritableSheet sheet = workbook.createSheet("Time Sheet", 0);
            WritableFont labelFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, true);
            WritableCellFormat labelFormat = new WritableCellFormat(labelFont);
            Label labelTask = new Label(0, 0, "Task", labelFormat);
            sheet.addCell(labelTask);
            Label labelHours = new Label(2, 0, "Hours", labelFormat);
            sheet.addCell(labelHours);
            Label labelStart = new Label(4, 0, "Start", labelFormat);
            sheet.addCell(labelStart);
            Label labelEnds = new Label(6, 0, "Ends", labelFormat);
            sheet.addCell(labelEnds);
            Label labelNotes = new Label(8, 0, "Notes", labelFormat);
            sheet.addCell(labelNotes);
            Label labelLocation = new Label(10, 0, "Location", labelFormat);
            sheet.addCell(labelLocation);
            int row = 2;
            ListIterator iterator = events.listIterator();
            while (iterator.hasNext()) {
                Event event = (Event) iterator.next();
                jxl.write.Label task = new jxl.write.Label(0, row, event.getTask());
                sheet.addCell(task);
                jxl.write.Number hours = new jxl.write.Number(2, row, event.getHours());
                sheet.addCell(hours);
                jxl.write.Label starts = new jxl.write.Label(4, row, event.getStarts());
                sheet.addCell(starts);
                jxl.write.Label ends = new jxl.write.Label(6, row, event.getEnds());
                sheet.addCell(ends);
                jxl.write.Label comments = new jxl.write.Label(8, row, event.getNotes());
                sheet.addCell(comments);
                jxl.write.Label location = new jxl.write.Label(10, row, event.getLocation());
                sheet.addCell(location);
                row++;
            }
            Label labelTotal = new Label(0, (row + 2), "Total Hours Billed: ", labelFormat);
            sheet.addCell(labelTotal);
            jxl.write.Formula formula = new jxl.write.Formula(2, (row + 2), "SUM(C3:C" + row + ")");
            sheet.addCell(formula);
            workbook.write();
            workbook.close();
            return true;
        } catch (IOException e) {
            this.errorMessage = "Oh no - can't write the file, may be the file is in use.";
            System.err.println(this.errorMessage);
            e.printStackTrace();
            return false;
        } catch (jxl.write.WriteException e) {
            this.errorMessage = "Oh no - can't write the excel file.";
            System.err.println(this.errorMessage);
            e.printStackTrace();
            return false;
        }
    }

    private Vector<Event> getCalendarData(String userName, String userPassword, String calendarId, String starts, String ends) {
        CalendarService myService = new CalendarService("idreamincode-Gtalk2Spreadsheet-v2");
        Vector<Event> events = null;
        try {
            singleFeedUrl = new URL(METAFEED_URL_BASE + calendarId + SINGLE_FEED_URL_SUFFIX);
            System.out.println(singleFeedUrl.toString());
        } catch (MalformedURLException e) {
            this.errorMessage = "Uh oh - you've got an invalid URL.";
            System.err.println(this.errorMessage);
            e.printStackTrace();
            return null;
        }
        try {
            myService.setUserCredentials(userName, userPassword);
            System.out.println("Date Range query");
            events = dateRangeQuery(myService, DateTime.parseDate(starts), DateTime.parseDate(ends));
        } catch (IOException e) {
            this.errorMessage = "There was a problem communicating with the service.";
            System.err.println(this.errorMessage);
            e.printStackTrace();
        } catch (ServiceException e) {
            this.errorMessage = "The server had a problem handling your request.\n" + "Probably your user credential or calendar id is wrong";
            System.err.println(this.errorMessage);
            e.printStackTrace();
        }
        return events;
    }

    /**
   * Prints the titles and start and end times of all the events.
   *
   * @param service An authenticated CalendarService object.
   * @param startTime Start time (inclusive) of events to print.
   * @param endTime End time (exclusive) of events to print.
   * @throws ServiceException If the service is unable to handle the request.
   * @throws IOException Error communicating with the server.
   */
    private Vector<Event> dateRangeQuery(CalendarService service, DateTime startTime, DateTime endTime) throws ServiceException, IOException {
        CalendarQuery myQuery = new CalendarQuery(singleFeedUrl);
        myQuery.setMinimumStartTime(startTime);
        myQuery.setMaximumStartTime(endTime);
        myQuery.addCustomParameter(new Query.CustomParameter("orderby", "starttime"));
        myQuery.addCustomParameter(new Query.CustomParameter("sortorder", "ascending"));
        myQuery.addCustomParameter(new Query.CustomParameter("singleevents", "true"));
        myQuery.addCustomParameter(new Query.CustomParameter("max-results", "10000"));
        CalendarEventFeed resultFeed = service.query(myQuery, CalendarEventFeed.class);
        System.out.println("Events from " + startTime.toString() + " to " + endTime.toString() + ":");
        System.out.println();
        int count = 1;
        Vector<Event> events = new Vector<Event>();
        for (int i = 0; i < resultFeed.getEntries().size(); i++) {
            CalendarEventEntry entry = resultFeed.getEntries().get(i);
            String title = entry.getTitle().getPlainText();
            java.util.List<When> eventTimes = entry.getTimes();
            String starts = null;
            String ends = null;
            String notes = null;
            String location = null;
            notes = ((PlainTextConstruct) (((TextContent) entry.getContent()).getContent())).getText();
            System.out.println("plainText Content:\t" + notes);
            location = ((Where) entry.getLocations().get(0)).getValueString();
            System.out.println("plainText Content:\t" + location);
            double duration = 0;
            Iterator<When> iterator = eventTimes.iterator();
            while (iterator.hasNext()) {
                When when = iterator.next();
                starts = when.getStartTime().toUiString();
                ends = when.getEndTime().toUiString();
                duration = getWhenDiff(when);
            }
            events.addElement(new Event(title, duration, starts, ends, notes, location));
            System.out.println("#" + count);
            System.out.println("----------------------");
            System.out.println("\t" + starts);
            System.out.println("\t" + title);
            System.out.println("\t" + ends);
            System.out.println("\t" + duration);
            System.out.println("\tnotes:" + notes);
            System.out.println("\tlocation:" + location);
            System.out.println("----------------------");
            count++;
        }
        System.out.println();
        return events;
    }

    private double getWhenDiff(When when) {
        double hours = 0.0;
        try {
            long diff = when.getEndTime().getValue() - when.getStartTime().getValue();
            if (diff > 0) {
                hours = (double) diff / (MILISECONDS_IN_HOUR);
            }
        } catch (Exception e) {
            this.errorMessage = "Problem occured while calcualting time difference";
            System.err.println(this.errorMessage);
            e.printStackTrace();
        }
        return hours;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
