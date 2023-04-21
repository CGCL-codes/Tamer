package lngs.google;

import lngs.lotus.LotusNotesCalendarEntry;
import lngs.util.StatusMessageCallback;
import com.google.gdata.client.GoogleService.*;
import com.google.gdata.client.calendar.*;
import com.google.gdata.data.*;
import com.google.gdata.data.batch.BatchOperationType;
import com.google.gdata.data.batch.BatchUtils;
import com.google.gdata.data.batch.BatchStatus;
import com.google.gdata.data.calendar.*;
import com.google.gdata.data.calendar.CalendarEventEntry;
import com.google.gdata.data.extensions.Reminder.Method;
import com.google.gdata.data.extensions.*;
import com.google.gdata.util.*;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;
import java.util.UUID;

public class GoogleManager {

    public GoogleManager() {
    }

    /**
     * Login to Google and connect to the calendar.
     */
    public void Connect() throws Exception {
        try {
            statusMessageCallback.statusAppendStart("Logging into Google");
            String appPath = new java.io.File("").getAbsolutePath() + System.getProperty("file.separator");
            googleInRangeEntriesFullFilename = appPath = googleInRangeEntriesFilename;
            String protocol = "http:";
            if (useSSL) {
                protocol = "https:";
            }
            mainCalendarFeedUrl = new URL(protocol + "//www.google.com/calendar/feeds/" + googleUsername + "/owncalendars/full");
            privateCalendarFeedUrl = new URL(protocol + "//www.google.com/calendar/feeds/" + googleUsername + "/private/full");
            service = new CalendarService("LotusNotes-Calendar-Sync");
            if (useSSL) {
                service.useSsl();
            }
            int retryCount = 0;
            boolean doRetry = false;
            do {
                try {
                    doRetry = false;
                    service.setUserCredentials(googleUsername, googlePassword);
                } catch (InvalidCredentialsException ex) {
                    throw new Exception("The username and/or password are invalid for signing into Google.", ex);
                } catch (AuthenticationException ex) {
                    throw new Exception("Unable to login to Google. Perhaps you need to use a proxy server.", ex);
                } catch (Exception ex) {
                    if (++retryCount > maxRetryCount) throw ex;
                    Thread.sleep(retryDelayMsecs);
                    doRetry = true;
                    statusMessageCallback.statusAppendLineDiag("Retry #" + retryCount + ". Encountered " + ex.toString());
                }
            } while (doRetry);
            createCalendar();
            if (diagnosticMode) {
                TimeZone localTimeZone = TimeZone.getDefault();
                String timeZoneName = localTimeZone.getID();
                statusMessageCallback.statusAppendLineDiag("Local Machine Time Zone: " + timeZoneName);
                statusMessageCallback.statusAppendLineDiag("Dest Calendar Time Zone: " + getDestinationTimeZone());
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            statusMessageCallback.statusAppendFinished();
        }
    }

    /**
     * Get the calendar feed URL for the calendar we want to update.
     * @return The calendar feed URL for the calendar we want to update.
     */
    protected URL getDestinationCalendarUrl() throws Exception {
        CalendarFeed calendars = null;
        int retryCount = 0;
        if (destinationCalendarName == null) throw new Exception("The destination calendar has not been set, so Google calendar entries cannot be found.");
        try {
            if (destinationCalendarFeedUrl != null) {
                return destinationCalendarFeedUrl;
            }
            calendars = getCalendarList();
            for (int i = 0; i < calendars.getEntries().size(); i++) {
                CalendarEntry calendar = calendars.getEntries().get(i);
                try {
                    if (calendar == null) {
                        throw new Exception("A null calendar was returned by Google. Perhaps there was a network problem. Please try your action again.");
                    }
                    if (calendar.getTitle() == null) {
                        throw new Exception("A calendar was returned by Google that has a null title. Perhaps there was a network problem. Please try your action again.");
                    }
                    if (calendar.getTitle().getPlainText() == null) {
                        throw new Exception("A calendar was returned by Google that has a null plain-text title. Perhaps there was a network problem. Please try your action again.");
                    }
                } catch (Exception ex) {
                    if (++retryCount > maxRetryCount) throw ex;
                    Thread.sleep(retryDelayMsecs);
                    statusMessageCallback.statusAppendLineDiag("Retry #" + retryCount + ". Encountered " + ex.toString());
                    calendars = getCalendarList();
                    i = 0;
                    continue;
                }
                statusMessageCallback.statusAppendLineDiag("Found calendar named: " + calendar.getTitle().getPlainText());
                if (calendar.getTitle().getPlainText().equals(destinationCalendarName)) {
                    destinationCalendarFeedUrl = new URL(calendar.getLink("alternate", "application/atom+xml").getHref());
                    break;
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
        return destinationCalendarFeedUrl;
    }

    /**
     * Get a list of all Google calendars.
     * @return The list of all Google calendars.
     */
    protected CalendarFeed getCalendarList() throws Exception {
        CalendarFeed calendars = null;
        int retryCount = 0;
        try {
            do {
                try {
                    calendars = service.getFeed(mainCalendarFeedUrl, CalendarFeed.class);
                } catch (com.google.gdata.util.ServiceException ex) {
                    calendars = null;
                    if (++retryCount > maxRetryCount) throw ex;
                    Thread.sleep(retryDelayMsecs);
                }
            } while (calendars == null);
        } catch (Exception ex) {
            throw ex;
        }
        return calendars;
    }

    /**
     * Creates a Google calendar for the desired name (if it doesn't already exist).
     * @throws IOException
     * @throws ServiceException
     */
    public void createCalendar() throws Exception, IOException, ServiceException {
        if (getDestinationCalendarUrl() != null) {
            return;
        }
        statusMessageCallback.statusAppendLineDiag("Creating calendar named '" + destinationCalendarName + "'");
        CalendarEntry calendar = new CalendarEntry();
        calendar.setTitle(new PlainTextConstruct(destinationCalendarName));
        TimeZone localTimeZone = TimeZone.getDefault();
        String timeZoneName = localTimeZone.getID();
        statusMessageCallback.statusAppendLineDiag("Local Machine Time Zone: " + timeZoneName);
        TimeZoneProperty tzp = new TimeZoneProperty(timeZoneName);
        calendar.setTimeZone(tzp);
        calendar.setHidden(HiddenProperty.FALSE);
        calendar.setSelected(SelectedProperty.TRUE);
        calendar.setColor(new ColorProperty(DEST_CALENDAR_COLOR));
        CalendarEntry returnedCalendar = service.insert(mainCalendarFeedUrl, calendar);
        returnedCalendar.update();
        destinationCalendarFeedUrl = new URL(returnedCalendar.getLink("alternate", "application/atom+xml").getHref());
        return;
    }

    /**
     * Delete all Google calendar entries for a specific date range.
     * @return The number of entries successfully deleted.
     */
    public int deleteCalendarEntries() throws Exception {
        ArrayList<CalendarEventEntry> googleCalEntries = getCalendarEntries();
        return deleteCalendarEntries(googleCalEntries);
    }

    /**
     * Delete the Google calendar entries in the provided list.
     * @return The number of entries successfully deleted.
     */
    public int deleteCalendarEntries(ArrayList<CalendarEventEntry> googleCalEntries) throws Exception {
        try {
            if (googleCalEntries.size() == 0) return 0;
            URL feedUrl = getDestinationCalendarUrl();
            int retryCount = 0;
            CalendarEventFeed batchRequest = new CalendarEventFeed();
            for (int i = 0; i < googleCalEntries.size(); i++) {
                CalendarEventEntry entry = googleCalEntries.get(i);
                BatchUtils.setBatchId(entry, Integer.toString(i));
                BatchUtils.setBatchOperationType(entry, BatchOperationType.DELETE);
                batchRequest.getEntries().add(entry);
                String startDateStr;
                startDateStr = "";
                if (entry.getTimes().size() > 0) startDateStr = entry.getTimes().get(0).getStartTime().toString();
                statusMessageCallback.statusAppendLineDiag("Delete #" + (i + 1) + ". Subject: " + entry.getTitle().getPlainText() + "  Start Date: " + startDateStr);
            }
            CalendarEventFeed feed = null;
            do {
                try {
                    feed = service.getFeed(feedUrl, CalendarEventFeed.class);
                } catch (com.google.gdata.util.ServiceException ex) {
                    feed = null;
                    if (++retryCount > maxRetryCount) throw ex;
                    Thread.sleep(retryDelayMsecs);
                }
            } while (feed == null);
            Link batchLink = feed.getLink(Link.Rel.FEED_BATCH, Link.Type.ATOM);
            CalendarEventFeed batchResponse = null;
            do {
                try {
                    batchResponse = service.batch(new URL(batchLink.getHref()), batchRequest);
                } catch (com.google.gdata.util.ServiceException ex) {
                    batchResponse = null;
                    if (++retryCount > maxRetryCount) throw ex;
                    Thread.sleep(retryDelayMsecs);
                }
            } while (batchResponse == null);
            CheckBatchDeleteResults(batchResponse, googleCalEntries);
            return batchRequest.getEntries().size();
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Throw an exception if there were any errors in the batch delete.
     * @param batchResponse - The batch response.
     * @param googleCalEntries - The original list of items that we tried to delete.
     */
    protected void CheckBatchDeleteResults(CalendarEventFeed batchResponse, ArrayList<CalendarEventEntry> googleCalEntries) throws Exception {
        boolean isSuccess = true;
        StringBuffer batchFailureMsg = new StringBuffer("These entries in the batch delete failed:");
        for (CalendarEventEntry entry : batchResponse.getEntries()) {
            String batchId = BatchUtils.getBatchId(entry);
            if (!BatchUtils.isSuccess(entry)) {
                isSuccess = false;
                BatchStatus status = BatchUtils.getBatchStatus(entry);
                CalendarEventEntry entryOrig = googleCalEntries.get(new Integer(batchId));
                batchFailureMsg.append("\nID: " + batchId + "  Reason: " + status.getReason() + "  Subject: " + entryOrig.getTitle().getPlainText() + "  Start Date: " + entryOrig.getTimes().get(0).getStartTime().toString());
            }
        }
        if (!isSuccess) {
            throw new Exception(batchFailureMsg.toString());
        }
    }

    /**
     * Get all the Google calendar entries for a specific date range.
     * @return The found entries.
     */
    public ArrayList<CalendarEventEntry> getCalendarEntries() throws Exception {
        try {
            statusMessageCallback.statusAppendStart("Getting Google calendar entries");
            ArrayList<CalendarEventEntry> allCalEntries = new ArrayList<CalendarEventEntry>();
            URL feedUrl = getDestinationCalendarUrl();
            CalendarQuery myQuery = new CalendarQuery(feedUrl);
            myQuery.setMinimumStartTime(new com.google.gdata.data.DateTime(minStartDate.getTime()));
            myQuery.setMaximumStartTime(com.google.gdata.data.DateTime.parseDateTime("2099-12-31T23:59:59"));
            myQuery.setMaxResults(5000);
            int startIndex = 1;
            int entriesReturned = 0;
            int queryCount = 0;
            int retryCount = 0;
            CalendarEventFeed resultFeed;
            while (true) {
                myQuery.setStartIndex(startIndex);
                try {
                    resultFeed = service.query(myQuery, CalendarEventFeed.class);
                } catch (com.google.gdata.util.ServiceException ex) {
                    if (++retryCount > maxRetryCount) {
                        throw ex;
                    }
                    Thread.sleep(retryDelayMsecs);
                    statusMessageCallback.statusAppendLineDiag("Retry #" + retryCount + ". Encountered " + ex.toString());
                    continue;
                }
                queryCount++;
                entriesReturned = resultFeed.getEntries().size();
                statusMessageCallback.statusAppendLineDiag(entriesReturned + " entries returned by query #" + queryCount + ".");
                if (entriesReturned == 0) break;
                allCalEntries.addAll(resultFeed.getEntries());
                startIndex = startIndex + entriesReturned;
            }
            for (int i = 0; i < allCalEntries.size(); i++) {
                CalendarEventEntry entry = allCalEntries.get(i);
                if (entry.getStatus().equals(BaseEventEntry.EventStatus.CANCELED)) {
                    allCalEntries.remove(entry);
                    i--;
                }
            }
            if (diagnosticMode) writeInRangeEntriesToFile(allCalEntries);
            return allCalEntries;
        } catch (Exception ex) {
            throw ex;
        } finally {
            statusMessageCallback.statusAppendFinished();
        }
    }

    /**
     * Write key parts of the Google calendar entries to a text file.
     * @param calendarEntries - The calendar entries to process.
     */
    public void writeInRangeEntriesToFile(ArrayList<CalendarEventEntry> calendarEntries) throws Exception {
        try {
            if (googleInRangeEntriesWriter == null) {
                googleInRangeEntriesFile = new File(googleInRangeEntriesFullFilename);
                googleInRangeEntriesWriter = new BufferedWriter(new FileWriter(googleInRangeEntriesFile));
            }
            if (calendarEntries == null) googleInRangeEntriesWriter.write("The calendar entries list is null.\n"); else for (CalendarEventEntry calEntry : calendarEntries) {
                googleInRangeEntriesWriter.write("=== Calendar Entry ===\n");
                googleInRangeEntriesWriter.write("  Title: " + calEntry.getTitle().getPlainText() + "\n");
                List<Where> whereList = calEntry.getLocations();
                String where;
                where = "";
                if (whereList.size() > 0 && whereList.get(0).getValueString() != null) {
                    where = whereList.get(0).getValueString();
                }
                googleInRangeEntriesWriter.write("  Where: " + where + "\n");
                googleInRangeEntriesWriter.write("  IcalUID: " + calEntry.getIcalUID() + "\n");
                for (When eventTime : calEntry.getTimes()) {
                    googleInRangeEntriesWriter.write("  Start Date:   " + new Date(eventTime.getStartTime().getValue()) + "\n");
                    googleInRangeEntriesWriter.write("  End Date:     " + new Date(eventTime.getEndTime().getValue()) + "\n");
                }
                googleInRangeEntriesWriter.write("  Edited Date:  " + new Date(calEntry.getEdited().getValue()) + "\n");
                googleInRangeEntriesWriter.write("  Updated Date: " + new Date(calEntry.getUpdated().getValue()) + "\n");
                String alarmInfo = "None";
                if (calEntry.getReminder().size() != 0) alarmInfo = calEntry.getReminder().get(0).getMinutes() + " mins";
                googleInRangeEntriesWriter.write("  Alarm: " + alarmInfo + "\n");
                googleInRangeEntriesWriter.write("\n\n");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (googleInRangeEntriesWriter != null) {
                googleInRangeEntriesWriter.close();
                googleInRangeEntriesWriter = null;
            }
        }
    }

    public void deleteCalendar() throws Exception {
        try {
            CalendarFeed calendars = service.getFeed(mainCalendarFeedUrl, CalendarFeed.class);
            for (int i = 0; i < calendars.getEntries().size(); i++) {
                CalendarEntry calendar = calendars.getEntries().get(i);
                if (calendar.getTitle().getPlainText().equals(destinationCalendarName)) {
                    calendar.delete();
                }
            }
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Compare the Lotus and Google entries based on the Lotus modified timestamp
     * and other items.
     * On exit, lotusCalEntries will only contain the entries we want created and
     * googleCalEntries will only contain the entries we want deleted.
     */
    public void compareCalendarEntries(ArrayList<LotusNotesCalendarEntry> lotusCalEntries, ArrayList<CalendarEventEntry> googleCalEntries) {
        for (int i = 0; i < lotusCalEntries.size(); i++) {
            LotusNotesCalendarEntry lotusEntry = lotusCalEntries.get(i);
            for (int j = 0; j < googleCalEntries.size(); j++) {
                if (!LotusNotesCalendarEntry.isLNGSUID(googleCalEntries.get(j).getIcalUID())) {
                    googleCalEntries.remove(j--);
                } else if (!hasEntryChanged(lotusEntry, googleCalEntries.get(j))) {
                    lotusCalEntries.remove(i--);
                    googleCalEntries.remove(j--);
                    break;
                }
            }
        }
    }

    /**
     * Compare a Lotus and Google entry
     * Return true if the Lotus entry has changed since the last sync.
     * Return false if the two entries are equivalent.
     */
    public boolean hasEntryChanged(LotusNotesCalendarEntry lotusEntry, CalendarEventEntry googleEntry) {
        final int googleUIDIdx = 33;
        String syncUID = lotusEntry.getSyncUID();
        if (lotusEntry.getSubject().equals("Test2")) {
            lotusEntry.getAlarm();
        }
        if (googleEntry.getIcalUID().substring(googleUIDIdx).equals(syncUID)) {
            String lotusSubject = lotusEntry.getSubject().trim().replace("\r", "");
            if (syncAllSubjectsToValue) {
                lotusSubject = syncAllSubjectsToThisValue;
            }
            if (!googleEntry.getTitle().getPlainText().equals(lotusSubject)) {
                statusMessageCallback.statusAppendLineDiag("Entry has changed: subject. Title: " + lotusEntry.getSubject());
                return true;
            }
            List<Where> whereList = googleEntry.getLocations();
            if (syncWhere && lotusEntry.getGoogleWhereString() != null) {
                if (whereList.size() > 0 && whereList.get(0).getValueString() == null) {
                    statusMessageCallback.statusAppendLineDiag("Entry has changed: location/where 1. Title: " + lotusEntry.getSubject());
                    return true;
                }
            } else {
                if (whereList.size() > 0 && whereList.get(0).getValueString() != null) {
                    statusMessageCallback.statusAppendLineDiag("Entry has changed: location/where 2. Title: " + lotusEntry.getSubject());
                    return true;
                }
            }
            if (syncAlarms && lotusEntry.getAlarm()) {
                if (googleEntry.getReminder().size() == 0) {
                    statusMessageCallback.statusAppendLineDiag("Entry has changed: alarm 1. Title: " + lotusEntry.getSubject());
                    return true;
                }
            } else {
                if (googleEntry.getReminder().size() > 0) {
                    statusMessageCallback.statusAppendLineDiag("Entry has changed: alarm 2. Title: " + lotusEntry.getSubject());
                    return true;
                }
            }
            if (!googleEntry.getPlainTextContent().equals(createDescriptionText(lotusEntry))) {
                statusMessageCallback.statusAppendLineDiag("Entry has changed: description. Title: " + lotusEntry.getSubject());
                return true;
            }
            return false;
        }
        return true;
    }

    public void createSampleCalEntry() {
        LotusNotesCalendarEntry cal = new LotusNotesCalendarEntry();
        cal.setSubject("DeanRepeatTest");
        cal.setEntryType(LotusNotesCalendarEntry.EntryType.APPOINTMENT);
        cal.setAppointmentType("3");
        cal.setLocation("nolocation");
        cal.setRoom("noroom");
        Date dstartDate, dendDate;
        Calendar now = Calendar.getInstance();
        now.set(Calendar.YEAR, 2010);
        now.set(Calendar.MONTH, 7);
        now.set(Calendar.DAY_OF_MONTH, 2);
        now.set(Calendar.HOUR_OF_DAY, 10);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        dstartDate = now.getTime();
        cal.setStartDateTime(dstartDate);
        now.set(Calendar.HOUR_OF_DAY, 11);
        dendDate = now.getTime();
        cal.setEndDateTime(dendDate);
        DateTime startTime, endTime;
        CalendarEventEntry event = new CalendarEventEntry();
        event.setTitle(new PlainTextConstruct(cal.getSubject()));
        String whereStr = cal.getGoogleWhereString();
        if (whereStr != null) {
            Where location = new Where();
            location.setValueString(whereStr);
            event.addLocation(location);
        }
        try {
            When eventTime = new When();
            eventTime.setStartTime(DateTime.parseDateTime(cal.getStartDateTimeGoogle()));
            eventTime.setEndTime(DateTime.parseDateTime(cal.getEndDateTimeGoogle()));
            event.addTime(eventTime);
            Date dstartDate2, dendDate2;
            now.set(Calendar.DAY_OF_MONTH, 3);
            now.set(Calendar.HOUR_OF_DAY, 10);
            dstartDate2 = now.getTime();
            cal.setStartDateTime(dstartDate2);
            now.set(Calendar.HOUR_OF_DAY, 11);
            dendDate2 = now.getTime();
            cal.setEndDateTime(dendDate2);
            eventTime = new When();
            eventTime.setStartTime(DateTime.parseDateTime(cal.getStartDateTimeGoogle()));
            eventTime.setEndTime(DateTime.parseDateTime(cal.getEndDateTimeGoogle()));
            event.addTime(eventTime);
            int j = event.getTimes().size();
            j++;
            service.insert(getDestinationCalendarUrl(), event);
        } catch (Exception e) {
        }
    }

    /**
     * Create Lotus Notes calendar entries in the Google calendar.
     * @param lotusCalEntries - The list of Lotus Notes calendar entries.
     * @return The number of Google calendar entries successfully created.
     * @throws ServiceException
     * @throws IOException
     */
    public int createCalendarEntries(ArrayList<LotusNotesCalendarEntry> lotusCalEntries) throws Exception, ServiceException, IOException {
        int retryCount = 0;
        int createdCount = 0;
        for (int i = 0; i < lotusCalEntries.size(); i++) {
            LotusNotesCalendarEntry lotusEntry = lotusCalEntries.get(i);
            CalendarEventEntry event = new CalendarEventEntry();
            if (syncAllSubjectsToValue) {
                event.setTitle(new PlainTextConstruct(syncAllSubjectsToThisValue));
            } else {
                event.setTitle(new PlainTextConstruct(lotusEntry.getSubject()));
            }
            event.setIcalUID(UUID.randomUUID().toString().replaceAll("-", "") + ":" + lotusEntry.getSyncUID());
            StringBuffer sb = new StringBuffer();
            event.setContent(new PlainTextConstruct(createDescriptionText(lotusEntry)));
            if (syncWhere) {
                String whereStr = lotusEntry.getGoogleWhereString();
                if (whereStr != null) {
                    Where location = new Where();
                    location.setValueString(whereStr);
                    event.addLocation(location);
                }
            }
            DateTime startTime, endTime;
            if (lotusEntry.getEntryType() == LotusNotesCalendarEntry.EntryType.TASK || lotusEntry.getAppointmentType() == LotusNotesCalendarEntry.AppointmentType.ALL_DAY_EVENT || lotusEntry.getAppointmentType() == LotusNotesCalendarEntry.AppointmentType.ANNIVERSARY) {
                startTime = DateTime.parseDate(lotusEntry.getStartDateGoogle());
                if (lotusEntry.getEndDateTime() == null) endTime = DateTime.parseDate(lotusEntry.getStartDateGoogle(1)); else endTime = DateTime.parseDate(lotusEntry.getEndDateGoogle(1));
            } else if (lotusEntry.getAppointmentType() == LotusNotesCalendarEntry.AppointmentType.APPOINTMENT || lotusEntry.getAppointmentType() == LotusNotesCalendarEntry.AppointmentType.MEETING) {
                startTime = DateTime.parseDateTime(lotusEntry.getStartDateTimeGoogle());
                if (lotusEntry.getEndDateTime() == null) endTime = DateTime.parseDateTime(lotusEntry.getStartDateTimeGoogle()); else endTime = DateTime.parseDateTime(lotusEntry.getEndDateTimeGoogle());
            } else if (lotusEntry.getAppointmentType() == LotusNotesCalendarEntry.AppointmentType.REMINDER) {
                startTime = DateTime.parseDateTime(lotusEntry.getStartDateTimeGoogle());
                endTime = DateTime.parseDateTime(lotusEntry.getStartDateTimeGoogle());
            } else {
                throw new Exception("Couldn't determine Lotus Notes event type.\nEvent subject: " + lotusEntry.getSubject() + "\nEntry Type: " + lotusEntry.getEntryType() + "\nAppointment Type: " + lotusEntry.getAppointmentType());
            }
            When eventTime = new When();
            eventTime.setStartTime(startTime);
            eventTime.setEndTime(endTime);
            event.addTime(eventTime);
            if (syncAlarms && lotusEntry.getAlarm()) {
                Reminder reminder = new Reminder();
                reminder.setMinutes(lotusEntry.getAlarmOffsetMinsGoogle());
                reminder.setMethod(Method.ALERT);
                event.getReminder().add(reminder);
            }
            if (lotusEntry.getPrivate()) event.setVisibility(BaseEventEntry.Visibility.PRIVATE);
            retryCount = 0;
            do {
                try {
                    service.insert(getDestinationCalendarUrl(), event);
                    createdCount++;
                    statusMessageCallback.statusAppendLineDiag("Create #" + createdCount + ". Subject: " + event.getTitle().getPlainText() + "  Start Date: " + event.getTimes().get(0).getStartTime().toString());
                    break;
                } catch (Exception ex) {
                    if (ex instanceof com.google.gdata.util.ServiceException && ++retryCount <= maxRetryCount) Thread.sleep(retryDelayMsecs); else throw new Exception("Couldn't create Google entry.\nSubject: " + event.getTitle().getPlainText() + "\nStart Date: " + event.getTimes().get(0).getStartTime().toString(), ex);
                }
            } while (true);
        }
        return createdCount;
    }

    protected String createDescriptionText(LotusNotesCalendarEntry lotusEntry) {
        StringBuffer sb = new StringBuffer();
        if (syncMeetingAttendees) {
            if (lotusEntry.getChairpersonPlain() != null) {
                sb.append("Chairperson: ");
                sb.append(lotusEntry.getChairpersonPlain());
            }
            if (lotusEntry.getRequiredAttendeesPlain() != null) {
                if (sb.length() > 0) sb.append("\n");
                sb.append("Required: ");
                sb.append(lotusEntry.getRequiredAttendeesPlain());
            }
            if (lotusEntry.getOptionalAttendees() != null) {
                if (sb.length() > 0) sb.append("\n");
                sb.append("Optional: ");
                sb.append(lotusEntry.getOptionalAttendees());
            }
        }
        if (syncDescription && lotusEntry.getBody() != null) {
            if (sb.length() > 0) sb.append("\n\n\n");
            String s = lotusEntry.getBody().replaceAll("\r", "");
            sb.append(s.trim());
        }
        return sb.toString().substring(0, sb.length() < maxDescriptionChars ? sb.length() : maxDescriptionChars);
    }

    public String getDestinationTimeZone() throws Exception {
        CalendarEventFeed calendar = null;
        int retryCount = 0;
        boolean doRetry = false;
        do {
            try {
                doRetry = false;
                URL calendarUrl = getDestinationCalendarUrl();
                calendar = service.getFeed(calendarUrl, CalendarEventFeed.class);
            } catch (Exception ex) {
                if (++retryCount > maxRetryCount) throw ex;
                Thread.sleep(retryDelayMsecs);
                doRetry = true;
                statusMessageCallback.statusAppendLineDiag("Retry #" + retryCount + ". Encountered " + ex.toString());
            }
        } while (doRetry);
        return calendar.getTimeZone().getValue();
    }

    public void setUsername(String value) {
        googleUsername = value;
    }

    public void setPassword(String value) {
        googlePassword = value;
    }

    public void setCalendarName(String value) {
        destinationCalendarName = value;
    }

    public void setUseSSL(boolean value) {
        useSSL = value;
    }

    public void setSyncDescription(boolean value) {
        syncDescription = value;
    }

    public void setSyncAlarms(boolean value) {
        syncAlarms = value;
    }

    public void setSyncWhere(boolean value) {
        syncWhere = value;
    }

    public void setSyncAllSubjectsToValue(boolean value) {
        syncAllSubjectsToValue = value;
    }

    public void setSyncAllSubjectsToThisValue(String value) {
        syncAllSubjectsToThisValue = value;
    }

    public void setSyncMeetingAttendees(boolean value) {
        syncMeetingAttendees = value;
    }

    public void setMinStartDate(Date minStartDate) {
        this.minStartDate = minStartDate;
    }

    public void setMaxEndDate(Date maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public void setDiagnosticMode(boolean value) {
        diagnosticMode = value;
    }

    public void setStatusMessageCallback(StatusMessageCallback value) {
        statusMessageCallback = value;
    }

    protected StatusMessageCallback statusMessageCallback = null;

    protected URL mainCalendarFeedUrl = null;

    protected URL privateCalendarFeedUrl = null;

    protected URL destinationCalendarFeedUrl = null;

    protected CalendarService service;

    protected String googleUsername;

    protected String googlePassword;

    protected String destinationCalendarName;

    protected String DEST_CALENDAR_COLOR = "#F2A640";

    protected BufferedWriter googleInRangeEntriesWriter = null;

    protected File googleInRangeEntriesFile = null;

    protected final String googleInRangeEntriesFilename = "GoogleInRangeEntries.txt";

    protected String googleInRangeEntriesFullFilename;

    protected boolean useSSL = true;

    protected boolean diagnosticMode = false;

    protected boolean syncDescription = false;

    protected boolean syncWhere = false;

    protected boolean syncAllSubjectsToValue = false;

    protected String syncAllSubjectsToThisValue = "";

    protected boolean syncAlarms = false;

    protected boolean syncMeetingAttendees = false;

    protected Date minStartDate = null;

    protected Date maxEndDate = null;

    protected final int maxRetryCount = 10;

    protected final int retryDelayMsecs = 300;

    protected final int maxDescriptionChars = 8000;
}
