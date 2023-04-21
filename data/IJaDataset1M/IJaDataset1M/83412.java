package lngs.lotus;

import lngs.util.StatusMessageCallback;
import lotus.domino.*;
import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LotusNotesManager {

    public LotusNotesManager() {
        notesVersion = "unknown";
        String appPath = new java.io.File("").getAbsolutePath() + System.getProperty("file.separator");
        lnFoundEntriesFullFilename = appPath + lnFoundEntriesFilename;
        lnInRangeEntriesFullFilename = appPath + lnInRangeEntriesFilename;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setMailFile(String mailfile) {
        this.mailfile = mailfile;
    }

    public void setRequiresAuth(boolean requiresAuth) {
        this.requiresAuth = requiresAuth;
    }

    public void setCredentials(String username, String password) {
        setRequiresAuth(true);
        this.username = username;
        this.password = password;
    }

    public void setDiagnosticMode(boolean value) {
        diagnosticMode = value;
    }

    public void setMinStartDate(Date minStartDate) {
        this.minStartDate = minStartDate;
    }

    public void setMaxEndDate(Date maxEndDate) {
        this.maxEndDate = maxEndDate;
    }

    public void setStatusMessageCallback(StatusMessageCallback value) {
        statusMessageCallback = value;
    }

    public String getNotesVersion() {
        return notesVersion;
    }

    /**
     * Retrieve a list of Lotus Notes calendar entries.
     * @param dominoServer The Domino server to retrieve data from, e.g. "IBM-Domino".
     *    Pass in null to retrieve from a local mail file.
     * @param mailFileName The mail file to read from, e.g. "mail/johnsmith.nsf".
     */
    public ArrayList<LotusNotesCalendarEntry> getCalendarEntries() throws Exception {
        boolean wasNotesThreadInitialized = false;
        ArrayList<LotusNotesCalendarEntry> calendarEntries = new ArrayList<LotusNotesCalendarEntry>();
        LotusNotesCalendarEntry cal = null;
        statusMessageCallback.statusAppendStart("Getting Lotus Notes calendar entries");
        try {
            ClassLoader.getSystemClassLoader().loadClass("lotus.domino.NotesThread");
        } catch (Exception ex) {
            throw new Exception("The Lotus Notes Java interface file (Notes.jar) could not be found.\nMake sure Notes.jar is in your classpath.", ex);
        }
        try {
            NotesThread.sinitThread();
            wasNotesThreadInitialized = true;
            Session session = NotesFactory.createSession((String) null, (String) null, password);
            notesVersion = session.getNotesVersion();
            String dominoServerTemp = server;
            if (server.equals("")) dominoServerTemp = null;
            Database db = session.getDatabase(dominoServerTemp, mailfile, false);
            if (db == null) throw new Exception("Couldn't create Lotus Notes Database object.");
            lotus.domino.DateTime minStartDateLN = session.createDateTime(minStartDate);
            lotus.domino.DateTime maxEndDateLN = session.createDateTime(maxEndDate);
            DateFormat dfStart = new SimpleDateFormat("yyyy/MM/dd 00:00:00");
            String strStartDate = dfStart.format(minStartDateLN.toJavaDate());
            DateFormat dfEnd = new SimpleDateFormat("yyyy/MM/dd 12:59:59");
            String strEndDate = dfEnd.format(maxEndDateLN.toJavaDate());
            String calendarQuery = "SELECT (@IsAvailable(CalendarDateTime) & (@Explode(CalendarDateTime) *= @Explode(@TextToTime(\"" + strStartDate + "-" + strEndDate + "\"))))";
            DocumentCollection queryResults = db.search(calendarQuery);
            boolean addDoc;
            Document doc;
            doc = queryResults.getFirstDocument();
            while (doc != null) {
                Item lnItem;
                addDoc = true;
                if (diagnosticMode) {
                    writeEntryToFile(doc);
                }
                cal = new LotusNotesCalendarEntry();
                lnItem = doc.getFirstItem("Subject");
                if (!isItemEmpty(lnItem)) cal.setSubject(lnItem.getText()); else cal.setSubject("<no subject>");
                lnItem = doc.getFirstItem("Body");
                if (!isItemEmpty(lnItem)) cal.setBody(lnItem.getText());
                lnItem = doc.getFirstItem("Form");
                if (!isItemEmpty(lnItem)) cal.setEntryType(lnItem.getText()); else cal.setEntryType(LotusNotesCalendarEntry.EntryType.APPOINTMENT);
                if (cal.getEntryType() == LotusNotesCalendarEntry.EntryType.APPOINTMENT) {
                    lnItem = doc.getFirstItem("AppointmentType");
                    if (!isItemEmpty(lnItem)) cal.setAppointmentType(lnItem.getText());
                }
                lnItem = doc.getFirstItem("Room");
                if (!isItemEmpty(lnItem)) cal.setRoom(lnItem.getText());
                lnItem = doc.getFirstItem("Location");
                if (!isItemEmpty(lnItem)) cal.setLocation(lnItem.getText());
                lnItem = doc.getFirstItem("$Alarm");
                if (!isItemEmpty(lnItem)) {
                    cal.setAlarm(true);
                    lnItem = doc.getFirstItem("$AlarmOffset");
                    if (!isItemEmpty(lnItem)) cal.setAlarmOffsetMins(Integer.parseInt(lnItem.getText()));
                }
                lnItem = doc.getFirstItem("OrgConfidential");
                if (!isItemEmpty(lnItem)) {
                    if (lnItem.getText().equals("1")) cal.setPrivate(true);
                }
                lnItem = doc.getFirstItem("REQUIREDATTENDEES");
                if (!isItemEmpty(lnItem)) {
                    cal.setRequiredAttendees(lnItem.getText());
                }
                lnItem = doc.getFirstItem("OPTIONALATTENDEES");
                if (!isItemEmpty(lnItem)) {
                    cal.setOptionalAttendees(lnItem.getText());
                }
                lnItem = doc.getFirstItem("CHAIR");
                if (!isItemEmpty(lnItem)) {
                    cal.setChairperson(lnItem.getText());
                }
                lnItem = doc.getFirstItem("APPTUNID");
                if (!isItemEmpty(lnItem)) {
                    if (lnItem.getText().matches("(?i).*https?:.*")) {
                        addDoc = false;
                    }
                }
                cal.setModifiedDateTime(doc.getLastModified().toJavaDate());
                lnItem = doc.getFirstItem("OrgRepeat");
                if (addDoc) {
                    if (!isItemEmpty(lnItem)) {
                        Vector startDates = null;
                        Vector endDates = null;
                        lnItem = doc.getFirstItem("StartDateTime");
                        if (!isItemEmpty(lnItem)) startDates = lnItem.getValueDateTimeArray();
                        lnItem = doc.getFirstItem("EndDateTime");
                        if (!isItemEmpty(lnItem)) endDates = lnItem.getValueDateTimeArray();
                        if (startDates != null) {
                            for (int i = 0; i < startDates.size(); i++) {
                                DateTime notesDate = (DateTime) startDates.get(i);
                                Date startDate = notesDate.toJavaDate();
                                if (isDateInRange(startDate)) {
                                    cal.setUID(doc.getUniversalID());
                                    cal.setStartDateTime(startDate);
                                    if (endDates != null) {
                                        notesDate = (DateTime) endDates.get(i);
                                        cal.setEndDateTime(notesDate.toJavaDate());
                                    }
                                    calendarEntries.add(cal.clone());
                                }
                            }
                        }
                    } else {
                        cal.setUID(doc.getUniversalID());
                        lnItem = doc.getFirstItem("StartDateTime");
                        if (!isItemEmpty(lnItem)) cal.setStartDateTime(lnItem.getDateTimeValue().toJavaDate());
                        lnItem = doc.getFirstItem("EndDateTime");
                        if (isItemEmpty(lnItem)) lnItem = doc.getFirstItem("EndDate");
                        if (!isItemEmpty(lnItem)) cal.setEndDateTime(lnItem.getDateTimeValue().toJavaDate());
                        if (isDateInRange(cal.getStartDateTime())) calendarEntries.add(cal);
                    }
                }
                doc = queryResults.getNextDocument();
            }
            return calendarEntries;
        } catch (Exception ex) {
            String exMsg = "There was a problem reading Lotus Notes calendar entries.";
            if (ex instanceof NotesException) {
                exMsg = exMsg + "\nNotesException ID: " + ((NotesException) ex).id;
            }
            if (cal != null && cal.getSubject() != null) {
                throw new Exception(exMsg + "\nThe subject of the entry being processed: " + cal.getSubject(), ex);
            } else {
                throw new Exception(exMsg, ex);
            }
        } finally {
            if (!wasNotesThreadInitialized) {
                throw new Exception("There was a problem initializing the Lotus Notes thread.\nMake sure the Lotus dll/so/dylib directory is in your path.\nAlso look at the Troubleshooting section of the Help file.");
            }
            if (diagnosticMode) writeInRangeEntriesToFile(calendarEntries);
            if (lnFoundEntriesWriter != null) {
                lnFoundEntriesWriter.close();
                lnFoundEntriesWriter = null;
            }
            NotesThread.stermThread();
            statusMessageCallback.statusAppendFinished();
        }
    }

    /**
     * Determine if the calendar entry date is in the range of dates to be processed.
     * @param entryDate - The calendar date to inspect.
     * @return True if the date is in the range, false otherwise.
     */
    public boolean isDateInRange(Date entryDate) {
        if (entryDate != null && entryDate.compareTo(minStartDate) >= 0 && entryDate.compareTo(maxEndDate) <= 0) return true;
        return false;
    }

    /**
     * Write all the items in a Lotus Notes Document (aka calendar entry) to a text file.
     * @param doc - The Document to process.
     */
    public void writeEntryToFile(lotus.domino.Document doc) throws IOException, NotesException {
        if (lnFoundEntriesWriter == null) {
            lnFoundEntriesFile = new File(lnFoundEntriesFullFilename);
            lnFoundEntriesWriter = new BufferedWriter(new FileWriter(lnFoundEntriesFile));
        }
        List<String> itemsAndValues = new ArrayList<String>();
        lnFoundEntriesWriter.write("=== Calendar Entry ===\n");
        if (doc.isDeleted()) {
            lnFoundEntriesWriter.write("  Doc is flagged Deleted.\n\n");
            return;
        }
        if (doc.isEncrypted()) {
            lnFoundEntriesWriter.write("  Doc is flagged Encrypted.\n\n");
            return;
        }
        if (doc.isSigned()) {
            lnFoundEntriesWriter.write("  Doc is flagged Signed.\n\n");
            return;
        }
        itemsAndValues.add("  LastModified: " + doc.getLastModified() + "\n");
        itemsAndValues.add("  UniversalID: " + doc.getUniversalID() + "\n");
        String itemName;
        for (Object itemObj : doc.getItems()) {
            if (itemObj instanceof Item) itemName = ((Item) itemObj).getName(); else continue;
            Item lnItem = doc.getFirstItem(itemName);
            if (lnItem != null) itemsAndValues.add("  " + itemName + ": " + lnItem.getText() + "\n");
        }
        Collections.sort(itemsAndValues, String.CASE_INSENSITIVE_ORDER);
        for (String itemStr : itemsAndValues) {
            lnFoundEntriesWriter.write(itemStr);
        }
        lnFoundEntriesWriter.write("\n\n");
    }

    public void writeInRangeEntriesToFile(List<LotusNotesCalendarEntry> calendarEntries) throws Exception {
        try {
            lnInRangeEntriesFile = new File(lnInRangeEntriesFullFilename);
            lnInRangeEntriesWriter = new BufferedWriter(new FileWriter(lnInRangeEntriesFile));
            if (calendarEntries == null) lnInRangeEntriesWriter.write("The calendar entries list is null.\n"); else for (LotusNotesCalendarEntry entry : calendarEntries) {
                lnInRangeEntriesWriter.write("=== " + entry.getSubject() + "\n");
                lnInRangeEntriesWriter.write("  UID: " + entry.getUID() + "\n");
                lnInRangeEntriesWriter.write("  Start Date:    " + entry.getStartDateTime() + "\n");
                lnInRangeEntriesWriter.write("  End Date:      " + entry.getEndDateTime() + "\n");
                lnInRangeEntriesWriter.write("  Modified Date: " + entry.getModifiedDateTime() + "\n");
                lnInRangeEntriesWriter.write("  Location: " + entry.getLocation() + "\n");
                lnInRangeEntriesWriter.write("  Room: " + entry.getRoom() + "\n");
                lnInRangeEntriesWriter.write("  Alarm: " + entry.getAlarm() + "\n");
                lnInRangeEntriesWriter.write("  Alarm Offset Mins: " + entry.getAlarmOffsetMins() + "\n");
                lnInRangeEntriesWriter.write("  Appointment Type: " + entry.getAppointmentType() + "\n");
                lnInRangeEntriesWriter.write("  Entry Type: " + entry.getEntryType() + "\n");
                lnInRangeEntriesWriter.write("\n");
            }
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (lnInRangeEntriesWriter != null) {
                lnInRangeEntriesWriter.close();
                lnInRangeEntriesWriter = null;
            }
        }
    }

    /**
     * Returns true if the Lotus Notes Item object is empty or null.
     * @param lnItem The object to inspect.
     */
    protected boolean isItemEmpty(Item lnItem) {
        try {
            if (lnItem == null || (lnItem != null && lnItem.getText().isEmpty())) return true;
        } catch (Exception ex) {
            return true;
        }
        return false;
    }

    protected StatusMessageCallback statusMessageCallback = null;

    String calendarViewName = "Google Calendar Sync";

    String username, password;

    String server, mailfile;

    boolean requiresAuth;

    boolean diagnosticMode = false;

    String notesVersion;

    Date minStartDate = null;

    Date maxEndDate = null;

    File lnFoundEntriesFile = null;

    BufferedWriter lnFoundEntriesWriter = null;

    final String lnFoundEntriesFilename = "LotusNotesFoundEntries.txt";

    String lnFoundEntriesFullFilename;

    File lnInRangeEntriesFile = null;

    BufferedWriter lnInRangeEntriesWriter = null;

    final String lnInRangeEntriesFilename = "LotusNotesInRangeEntries.txt";

    String lnInRangeEntriesFullFilename;
}
