package de.moonflower.jfritz.callerlist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import de.moonflower.jfritz.JFritz;
import de.moonflower.jfritz.Main;
import de.moonflower.jfritz.callerlist.filter.CallFilter;
import de.moonflower.jfritz.callerlist.filter.DateFilter;
import de.moonflower.jfritz.exceptions.InvalidFirmwareException;
import de.moonflower.jfritz.exceptions.WrongPasswordException;
import de.moonflower.jfritz.phonebook.PhoneBook;
import de.moonflower.jfritz.phonebook.PhoneBookListener;
import de.moonflower.jfritz.struct.Call;
import de.moonflower.jfritz.struct.CallType;
import de.moonflower.jfritz.struct.IProgressListener;
import de.moonflower.jfritz.struct.Person;
import de.moonflower.jfritz.struct.PhoneNumber;
import de.moonflower.jfritz.utils.CopyFile;
import de.moonflower.jfritz.utils.Debug;
import de.moonflower.jfritz.utils.JFritzUtils;
import de.moonflower.jfritz.utils.reverselookup.LookupObserver;
import de.moonflower.jfritz.utils.reverselookup.ReverseLookup;

/**
 * This class manages the caller list.
 * 
 */
public class CallerList extends AbstractTableModel implements LookupObserver, PhoneBookListener {

    private static final long serialVersionUID = 1;

    private static final String CALLS_DTD_URI = "http://jfritz.moonflower.de/dtd/calls.dtd";

    private static final String CALLS_DTD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<!-- DTD for JFritz calls -->" + "<!ELEMENT calls (comment?,entry*)>" + "<!ELEMENT comment (#PCDATA)>" + "<!ELEMENT date (#PCDATA)>" + "<!ELEMENT caller (#PCDATA)>" + "<!ELEMENT port (#PCDATA)>" + "<!ELEMENT route (#PCDATA)>" + "<!ELEMENT duration (#PCDATA)>" + "<!ELEMENT comment (#PCDATA)>" + "<!ELEMENT entry (date,caller?,port?,route?,duration?,comment?)>" + "<!ATTLIST entry calltype (call_in|call_in_failed|call_out) #REQUIRED>";

    private static final String PATTERN_CSV = "(\\||;)";

    private static final String EXPORT_CSV_FORMAT_JFRITZ = "\"CallType\";\"Date\";\"Time\";\"Number\";\"Route\";\"" + "Port\";\"Duration\";\"Name\";\"Address\";\"City\";\"CallByCall\";\"Comment\"";

    private static final String EXPORT_CSV_FORMAT_FRITZBOX = "Typ;Datum;Rufnummer;Nebenstelle;Eigene Rufnummer;Dauer";

    private static final String EXPORT_CSV_FORMAT_FRITZBOX_PUSHSERVICE = "Typ; Datum; Rufnummer; Nebenstelle; Eigene Rufnummer; Dauer";

    private static final String EXPORT_CSV_FORMAT_PUSHSERVICE_NEW = "Typ; Datum; Name; Rufnummer; Nebenstelle; Eigene Rufnummer; Dauer";

    private static final String EXPORT_CSV_FORMAT_FRITZBOX_NEWFIRMWARE = "Typ;Datum;Name;Rufnummer;Nebenstelle;Eigene Rufnummer;Dauer";

    private static final String EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH = "Typ;Date;Number;Extension;Outgoing Caller ID;Duration";

    private static final String EXPORT_CSV_FORMAT_PUSHSERVICE_ENGLISH = "Type; Date; Number; Extension; Local Number; Duration";

    private static final String EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH_NEW = "Typ;Date;Name;Number;Extension;Outgoing Caller ID;Duration";

    private static final String EXPORT_CSV_FORMAT_FRITZBOX_EN_140426 = "Type;Date;Name;Number;Extension;Outgoing Caller ID;Duration";

    private Vector<Call> filteredCallerData;

    private Vector<Call> unfilteredCallerData;

    private Vector<Call> newCalls;

    private int sortColumn;

    private Vector<CallFilter> filters;

    private Vector<CallerListListener> callListListeners;

    private boolean sortDirection = false;

    private PhoneBook phonebook;

    private Vector<IProgressListener> progressListeners;

    private NumberCallMultiHashMap hashMap;

    private boolean initStage = true;

    /**
	 * CallerList Constructor new contrustor, using binary sizes
	 * NOTE:filteredCallerData = unfilteredCallerData is forbidden!! use
	 * filteredCallerData = unfilteredCallerData.clone() instead
	 * 
	 * @author Brian Jensen
	 * 
	 */
    public CallerList() {
        unfilteredCallerData = new Vector<Call>(256);
        filteredCallerData = new Vector<Call>();
        filters = new Vector<CallFilter>();
        newCalls = new Vector<Call>(32);
        callListListeners = new Vector<CallerListListener>();
        progressListeners = new Vector<IProgressListener>();
        hashMap = new NumberCallMultiHashMap();
        sortColumn = 1;
    }

    /**CallerListListeners are used to passively catch changes to the 
	 * data in the call list
	 * 
	 * @param l the listener to be added
	 */
    public synchronized void addListener(final CallerListListener listener) {
        callListListeners.add(listener);
    }

    /**
	 * CallerListListeners are used to passively catch changes to the
	 * data in the call list
	 * 
	 * @param l the listener to be removed
	 */
    public synchronized void removeListener(final CallerListListener listener) {
        callListListeners.remove(listener);
    }

    /**
	 * 
	 * @return Unfiltered Vector of Calls
	 */
    public Vector<Call> getUnfilteredCallVector() {
        return unfilteredCallerData;
    }

    /**
	 * 
	 * @return Filtered Vector of Calls
	 */
    public Vector<Call> getFilteredCallVector() {
        return filteredCallerData;
    }

    /**
	 * Is used for the clickability!
	 */
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        boolean isEditable;
        final String columnName = getRealColumnName(columnIndex);
        if (columnName.equals(CallerTable.COLUMN_COMMENT)) {
            isEditable = true;
        } else if (columnName.equals(CallerTable.COLUMN_NUMBER)) {
            isEditable = true;
        } else {
            isEditable = false;
        }
        return isEditable;
    }

    /**
	 * @param columnIndex
	 * @return class of column
	 */
    @SuppressWarnings("unchecked")
    public Class getColumnClass(final int columnIndex) {
        Class result;
        final Object o = getValueAt(0, columnIndex);
        if (o == null) {
            result = Object.class;
        } else {
            result = o.getClass();
        }
        return result;
    }

    /**
	 * Saves caller list to xml file.
	 * 
	 * @param filename
	 *            Filename to save to
	 * @param wholeCallerList
	 *            Save whole caller list or only selected entries
	 */
    public synchronized void saveToXMLFile(final String filename, final boolean wholeCallerList) {
        Debug.always("Saving to file " + filename);
        try {
            BufferedWriter pw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF8"));
            pw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            pw.newLine();
            pw.write("<calls>");
            pw.newLine();
            pw.write("<comment>Calls for " + Main.PROGRAM_NAME + " v" + Main.PROGRAM_VERSION + "</comment>");
            pw.newLine();
            int rows[] = null;
            if (JFritz.getJframe() != null) {
                rows = JFritz.getJframe().getCallerTable().getSelectedRows();
            }
            if (!wholeCallerList && (rows != null) && (rows.length > 0)) {
                Call currentCall;
                for (int i = 0; i < rows.length; i++) {
                    currentCall = filteredCallerData.elementAt(rows[i]);
                    pw.write(currentCall.toXML());
                    pw.newLine();
                }
            } else if (wholeCallerList) {
                Enumeration<Call> en = unfilteredCallerData.elements();
                Call call;
                while (en.hasMoreElements()) {
                    call = en.nextElement();
                    pw.write(call.toXML());
                    pw.newLine();
                }
            } else {
                Enumeration<Call> en = filteredCallerData.elements();
                Call call;
                while (en.hasMoreElements()) {
                    call = en.nextElement();
                    pw.write(call.toXML());
                    pw.newLine();
                }
            }
            pw.write("</calls>");
            pw.close();
        } catch (UnsupportedEncodingException e) {
            Debug.error("UTF-8 not supported");
        } catch (FileNotFoundException e) {
            Debug.error("Could not write " + filename + "!");
        } catch (IOException e) {
            Debug.error("IOException " + filename);
        }
    }

    /**
	 * Saves callerlist to csv file
	 * 
	 * @param filename
	 *            Filename to save to
	 * @param wholeCallerList
	 *            Save whole caller list or only selected entries
	 */
    public synchronized void saveToCSVFile(String filename, boolean wholeCallerList) {
        Debug.always("Saving to csv file " + filename);
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(filename);
            PrintWriter pw = new PrintWriter(fos);
            pw.println("\"CallType\";\"Date\";\"Time\";\"Number\";\"Route\";\"Port\";\"Duration\";\"Name\";\"Address\";\"City\";\"CallByCall\";\"Comment\"");
            int rows[] = null;
            if (JFritz.getJframe() != null) {
                rows = JFritz.getJframe().getCallerTable().getSelectedRows();
            }
            if (!wholeCallerList && (rows != null) && (rows.length > 0)) {
                Call currentCall;
                for (int i = 0; i < rows.length; i++) {
                    currentCall = filteredCallerData.elementAt(rows[i]);
                    pw.println(currentCall.toCSV());
                }
            } else if (wholeCallerList) {
                Enumeration<Call> en = unfilteredCallerData.elements();
                Call call;
                while (en.hasMoreElements()) {
                    call = en.nextElement();
                    pw.println(call.toCSV());
                }
            } else {
                Enumeration<Call> en = filteredCallerData.elements();
                Call call;
                while (en.hasMoreElements()) {
                    call = en.nextElement();
                    pw.println(call.toCSV());
                }
            }
            pw.close();
        } catch (FileNotFoundException e) {
            Debug.error("Could not write " + filename + "!");
        }
    }

    /**
	 * Loads calls from xml file
	 * 
	 * @param filename
	 */
    public synchronized void loadFromXMLFile(String filename) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();
            XMLReader reader = parser.getXMLReader();
            reader.setErrorHandler(new ErrorHandler() {

                public void error(SAXParseException x) throws SAXException {
                    throw x;
                }

                public void fatalError(SAXParseException x) throws SAXException {
                    throw x;
                }

                public void warning(SAXParseException x) throws SAXException {
                    throw x;
                }
            });
            reader.setEntityResolver(new EntityResolver() {

                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                    if (systemId.equals(CALLS_DTD_URI) || systemId.equals("calls.dtd")) {
                        InputSource is;
                        is = new InputSource(new StringReader(CALLS_DTD));
                        is.setSystemId(CALLS_DTD_URI);
                        return is;
                    }
                    throw new SAXException("Invalid system identifier: " + systemId);
                }
            });
            reader.setContentHandler(new CallFileXMLHandler(this));
            reader.parse(new InputSource(new FileInputStream(filename)));
            fireUpdateCallVector();
            initStage = false;
        } catch (ParserConfigurationException e) {
            Debug.error("Error with ParserConfiguration!");
        } catch (SAXException e) {
            Debug.error("Error on parsing " + filename + "!");
            if (e.getLocalizedMessage().startsWith("Relative URI") || e.getLocalizedMessage().startsWith("Invalid system identifier")) {
                Debug.error(e.toString());
                Debug.errDlg("STRUKTUR�NDERUNG!\n\nBitte in der Datei jfritz.calls.xml\n " + "die Zeichenkette \"calls.dtd\" durch\n \"" + CALLS_DTD_URI + "\"\n ersetzen!");
            }
        } catch (IOException e) {
            Debug.error("Could not read " + filename + "!");
        }
        JFritz.getPhonebook().addListener(this);
    }

    /**
	 * Removes all duplicate whitespaces from inputStr
	 * 
	 * @param inputStr
	 * @return outputStr
	 */
    public static synchronized String removeDuplicateWhitespace(String inputStr) {
        Pattern p = Pattern.compile("\\s+");
        Matcher matcher = p.matcher(inputStr);
        return matcher.replaceAll(" ");
    }

    /**
	 * Adds an entry to the call list this function calls contains(Call newCall)
	 * to test if the given call is contained in the list the function then adds
	 * the entry to newCalls if appropriate
	 * 
	 * Note: After all import processes make sure to call fireUpdateCallVector()
	 * 
	 * 
	 * @author Brian Jensen
	 */
    private synchronized boolean addEntry(Call call) {
        if (contains(call)) {
            return false;
        }
        newCalls.add(call);
        if (call.getPhoneNumber() != null) {
            hashMap.addCall(call.getPhoneNumber(), call);
        }
        return true;
    }

    /**
	 * Adds a vector of new calls to the list, used by network code to
	 * import calls en masse
	 * 
	 * @author brian
	 * 
	 * @param newCalls to be added to the call list
	 */
    public synchronized void addEntries(Vector<Call> newCalls) {
        int newEntries = 0;
        for (Call call : newCalls) {
            if (addEntry(call)) {
                newEntries++;
            }
        }
        if ((!initStage) && (newEntries > 0)) {
            fireUpdateCallVector();
            update();
            saveToXMLFile(Main.SAVE_DIR + JFritz.CALLS_FILE, true);
            String msg;
            if (newEntries == 1) {
                msg = Main.getMessage("imported_call");
            } else {
                msg = Main.getMessage("imported_calls").replaceAll("%N", Integer.toString(newEntries));
            }
            if (JFritzUtils.parseBoolean(Main.getProperty("option.notifyOnCalls"))) {
                JFritz.infoMsg(msg);
            }
            if (JFritzUtils.parseBoolean(Main.getProperty("option.createBackupAfterFetch"))) {
                doBackup();
            }
        }
    }

    /**
	 * Updates call data based upon an external data source
	 * 
	 * @param oldCall original call
	 * @param newCall new call containing changed data
	 */
    public synchronized void updateEntry(Call oldCall, Call newCall) {
        int index = unfilteredCallerData.indexOf(oldCall);
        if (index >= 0) {
            unfilteredCallerData.setElementAt(newCall, index);
            for (CallerListListener listener : callListListeners) listener.callsUpdated(oldCall, newCall);
            update();
            saveToXMLFile(Main.SAVE_DIR + JFritz.CALLS_FILE, true);
        }
    }

    /**
	 * Removes a vector of calls, as dictated by an external data source
	 * 
	 * @author brian
	 * 
	 * @param removeCalls calls to be removed
	 */
    public synchronized void removeEntries(Vector<Call> removeCalls) {
        unfilteredCallerData.removeAll(removeCalls);
        for (Call c : removeCalls) {
            hashMap.deleteCall(c.getPhoneNumber(), c);
        }
        for (CallerListListener listener : callListListeners) {
            listener.callsRemoved(removeCalls);
        }
        update();
        saveToXMLFile(Main.SAVE_DIR + JFritz.CALLS_FILE, true);
    }

    /**
	 * This function tests if the given call  is
	 * contained in the call list
	 * 
	 * This new method is using a binary search algorithm, that means
	 * unfilteredCallerData has to be sorted ascending by date or it won't work
	 * 
	 * 
	 * @author Brian Jensen
	 * 
	 */
    public synchronized boolean contains(Call newCall) {
        int left, right, middle;
        left = 0;
        right = unfilteredCallerData.size() - 1;
        Call c;
        while (left <= right) {
            middle = ((right - left) / 2) + left;
            if (unfilteredCallerData.isEmpty()) {
                return false;
            }
            c = unfilteredCallerData.elementAt(middle);
            int Compare = newCall.getCalldate().compareTo(c.getCalldate());
            if (Compare > 0) {
                right = middle - 1;
            } else if (Compare < 0) {
                left = middle + 1;
            } else {
                if (c.equals(newCall)) {
                    return true;
                } else {
                    int tmpMiddle = middle - 1;
                    if (tmpMiddle >= 0) {
                        c = unfilteredCallerData.elementAt(tmpMiddle);
                        while (c.getCalldate().equals(newCall.getCalldate())) {
                            if (c.equals(newCall)) {
                                return true;
                            }
                            if (tmpMiddle > 0) {
                                c = unfilteredCallerData.elementAt(--tmpMiddle);
                            } else {
                                break;
                            }
                        }
                    }
                    tmpMiddle = middle + 1;
                    if (tmpMiddle < unfilteredCallerData.size()) {
                        c = unfilteredCallerData.elementAt(middle + 1);
                        while (c.getCalldate().equals(newCall.getCalldate())) {
                            if (c.equals(newCall)) {
                                return true;
                            }
                            if (tmpMiddle < (unfilteredCallerData.size() - 1)) {
                                c = unfilteredCallerData.elementAt(++tmpMiddle);
                            } else {
                                break;
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return false;
    }

    /**
	 * This method synchronizes the main call vector with with the recently
	 * added calls per addEntry(Call call)
	 * 
	 * NOTE: This method must be called after any calls have been added but
	 * should not be called until done importing all calls
	 * 
	 * @author Brian Jensen
	 * 
	 */
    private synchronized void fireUpdateCallVector() {
        unfilteredCallerData.addAll(newCalls);
        for (CallerListListener l : callListListeners) l.callsAdded((Vector<Call>) newCalls.clone());
        newCalls.clear();
        sortAllUnfilteredRows();
    }

    /**
	 * Retrieves data from FRITZ!Box
	 * 
	 * @throws WrongPasswordException
	 * @throws IOException
	 */
    public void getNewCalls() throws WrongPasswordException, InvalidFirmwareException, IOException {
        getNewCalls(false);
    }

    /**
	 * Retrieves data from FRITZ!Box Function calls
	 * JFritzUtils.retrieveCSVList(...) which reads the HTML page from the box
	 * then reads the csv-file in and passes it on to
	 * CallerList.importFromCSVFile(BufferedReader br) which then parses all the
	 * entries makes backups and deletes entries from the box as appropriate *
	 * 
	 * @author Brian Jensen
	 * 
	 * @param deleteFritzBoxCallerList
	 *            true indicates that fritzbox callerlist should be deleted
	 *            without considering number of entries or config
	 * @throws WrongPasswordException
	 * @throws IOException
	 */
    public void getNewCalls(boolean deleteFritzBoxCallerList) throws WrongPasswordException, InvalidFirmwareException, IOException {
        if (JFritz.getFritzBox().checkValidFirmware()) {
            Debug.info("box.address: " + JFritz.getFritzBox().getAddress());
            Debug.info("box.port: " + JFritz.getFritzBox().getPort());
            Debug.debug("box.password: " + JFritz.getFritzBox().getPassword());
            Debug.info("box.firmware: " + JFritz.getFritzBox().getFirmware().getFirmwareVersion() + " " + JFritz.getFritzBox().getFirmware().getLanguage());
            boolean newEntries = JFritz.getFritzBox().retrieveCSVList();
            if (!JFritz.isShutdownInvoked()) {
                if ((newEntries && Main.getProperty("option.deleteAfterFetch").equals("true")) || deleteFritzBoxCallerList) {
                    JFritz.getFritzBox().clearListOnFritzBox();
                }
            }
            if (newEntries && JFritzUtils.parseBoolean(Main.getProperty("option.createBackupAfterFetch"))) {
                doBackup();
            }
        }
        update();
    }

    /**
	 * returns number of rows in CallerList
	 * 
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
    public int getRowCount() {
        return filteredCallerData.size();
    }

    /**
	 * returns number of columns of a call
	 * 
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
    public int getColumnCount() {
        return 10;
    }

    /**
	 * @param rowIndex
	 * @param columnIndex
	 * @return the value at a specific position
	 */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Call call = filteredCallerData.get(rowIndex);
        String columnName = getRealColumnName(columnIndex);
        if (columnName.equals(CallerTable.COLUMN_TYPE)) {
            return call.getCalltype();
        } else if (columnName.equals(CallerTable.COLUMN_DATE)) {
            return call.getCalldate();
        } else if (columnName.equals(CallerTable.COLUMN_CALL_BY_CALL)) {
            if (call.getPhoneNumber() != null) {
                return call.getPhoneNumber().getCallByCall();
            } else {
                return null;
            }
        } else if (columnName.equals(CallerTable.COLUMN_NUMBER)) {
            return call.getPhoneNumber();
        } else if (columnName.equals(CallerTable.COLUMN_PARTICIPANT)) {
            return JFritz.getPhonebook().findPerson(call);
        } else if (columnName.equals(CallerTable.COLUMN_PORT)) {
            return call.getPort();
        } else if (columnName.equals(CallerTable.COLUMN_ROUTE)) {
            if (call.getRoute().startsWith("SIP")) {
                return JFritz.getSIPProviderTableModel().getSipProvider(call.getRoute(), call.getRoute());
            }
            return call.getRoute();
        } else if (columnName.equals(CallerTable.COLUMN_DURATION)) {
            return Integer.toString(call.getDuration());
        } else if (columnName.equals(CallerTable.COLUMN_COMMENT)) {
            return call.getComment();
        } else if (columnName.equals(CallerTable.COLUMN_PICTURE)) {
            Person p = JFritz.getPhonebook().findPerson(call);
            if (p != null) return p.getScaledPicture(); else return new ImageIcon("");
        }
        return null;
    }

    /**
	 * Sets a value to a specific position
	 */
    public void setValueAt(Object object, int rowIndex, int columnIndex) {
        String columnName = getRealColumnName(columnIndex);
        if (columnName.equals(CallerTable.COLUMN_COMMENT)) {
            setComment((String) object, rowIndex);
        }
        fireTableCellUpdated(rowIndex, columnIndex);
    }

    public synchronized void setComment(String comment, int rowIndex) {
        Call updated = filteredCallerData.get(rowIndex);
        Call original = updated.clone();
        updated.setComment(comment);
        for (CallerListListener listener : callListListeners) listener.callsUpdated(original, updated);
    }

    /**
	 * Sort table model rows by a specific column and direction
	 * 
	 * @param col
	 *            Index of column to be sorted by
	 * @param asc
	 *            Order of sorting
	 */
    public void sortAllFilteredRowsBy(int col, boolean asc) {
        Collections.sort(filteredCallerData, new ColumnSorter<Call>(col, asc));
        fireTableDataChanged();
        fireTableStructureChanged();
    }

    /**
	 * Sort table model rows by a specific column. The direction is determined
	 * automatically.
	 * 
	 * @param col
	 *            Index of column to be sorted by
	 */
    public void sortAllFilteredRowsBy(int col) {
        if ((sortColumn == col) && (sortDirection == false)) {
            sortDirection = true;
        } else {
            sortColumn = col;
            sortDirection = false;
        }
        sortAllFilteredRowsBy(sortColumn, sortDirection);
    }

    public void sortAllUnfilteredRows() {
        Debug.debug("Sorting unfiltered data");
        int indexOfDate = -1;
        String columnName = "";
        for (int i = 0; i < getColumnCount(); i++) {
            columnName = getRealColumnName(i);
            if (columnName.equals(CallerTable.COLUMN_DATE)) {
                indexOfDate = i;
            }
        }
        Collections.sort(unfilteredCallerData, new ColumnSorter<Call>(indexOfDate, false));
        Collections.sort(filteredCallerData, new ColumnSorter<Call>(sortColumn, sortDirection));
        fireTableStructureChanged();
    }

    /**
	 * This comparator is used to sort vectors of data
	 */
    public class ColumnSorter<T extends Call> implements Comparator<Call> {

        int columnIndex;

        boolean ascending;

        ColumnSorter(int columnIndex, boolean ascending) {
            this.columnIndex = columnIndex;
            this.ascending = ascending;
        }

        public int compare2(Object a, Object b) {
            if (!(a instanceof Call) || !(b instanceof Call)) {
                return 0;
            }
            Call call1 = (Call) a;
            Call call2 = (Call) b;
            return compare(call1, call2);
        }

        public int compare(Call call1, Call call2) {
            Object o1 = null, o2 = null;
            String columnName = getRealColumnName(columnIndex);
            if (columnName.equals(CallerTable.COLUMN_TYPE)) {
                o1 = call1.getCalltype().toString();
                o2 = call2.getCalltype().toString();
            } else if (columnName.equals(CallerTable.COLUMN_DATE)) {
                o1 = call1.getCalldate();
                o2 = call2.getCalldate();
            } else if (columnName.equals(CallerTable.COLUMN_CALL_BY_CALL)) {
                if (call1.getPhoneNumber() != null) {
                    o1 = call1.getPhoneNumber().getCallByCall();
                } else {
                    o1 = null;
                }
                if (call2.getPhoneNumber() != null) {
                    o2 = call2.getPhoneNumber().getCallByCall();
                } else {
                    o2 = null;
                }
            } else if (columnName.equals(CallerTable.COLUMN_NUMBER)) {
                if (call1.getPhoneNumber() != null) {
                    o1 = call1.getPhoneNumber().getIntNumber();
                } else {
                    o1 = null;
                }
                if (call2.getPhoneNumber() != null) {
                    o2 = call2.getPhoneNumber().getIntNumber();
                } else {
                    o2 = null;
                }
            } else if (columnName.equals(CallerTable.COLUMN_PARTICIPANT)) {
                Person p1 = JFritz.getPhonebook().findPerson(call1);
                Person p2 = JFritz.getPhonebook().findPerson(call2);
                if (p1 != null) {
                    o1 = p1.getFullname().toUpperCase();
                } else {
                    o1 = null;
                }
                if (p2 != null) {
                    o2 = p2.getFullname().toUpperCase();
                } else {
                    o2 = null;
                }
            } else if (columnName.equals(CallerTable.COLUMN_PORT)) {
                o1 = call1.getPort();
                o2 = call2.getPort();
            } else if (columnName.equals(CallerTable.COLUMN_ROUTE)) {
                o1 = call1.getRoute();
                o2 = call2.getRoute();
            } else if (columnName.equals(CallerTable.COLUMN_DURATION)) {
                if (call1.getDuration() != 0) {
                    o1 = format(Integer.toString(call1.getDuration()), 10);
                } else {
                    o1 = null;
                }
                if (call2.getDuration() != 0) {
                    o2 = format(Integer.toString(call2.getDuration()), 10);
                } else {
                    o2 = null;
                }
            } else if (columnName.equals(CallerTable.COLUMN_COMMENT)) {
                o1 = call1.getComment().toUpperCase();
                o2 = call2.getComment().toUpperCase();
            } else {
                o1 = call1.getCalldate();
                o2 = call2.getCalldate();
            }
            if ((o1 instanceof String) && (((String) o1).trim().length() == 0)) {
                o1 = null;
            }
            if ((o2 instanceof String) && (((String) o2).trim().length() == 0)) {
                o2 = null;
            }
            if ((o1 == null) && (o2 == null)) {
                return 0;
            } else if (o1 == null) {
                return 1;
            } else if (o2 == null) {
                return -1;
            } else if (o1 instanceof Comparable) {
                if (ascending) {
                    return ((Comparable) o1).compareTo(o2);
                } else {
                    return ((Comparable) o2).compareTo(o1);
                }
            } else {
                if (ascending) {
                    return o1.toString().compareTo(o2.toString());
                } else {
                    return o2.toString().compareTo(o1.toString());
                }
            }
        }

        public String format(String s, int places) {
            int j = places - s.length();
            if (j > 0) {
                StringBuffer sb = null;
                sb = new StringBuffer(j);
                for (int k = 0; k < j; k++) {
                    sb.append(' ');
                }
                return sb.toString() + s;
            } else {
                return s;
            }
        }
    }

    /**
	 * @return Total duration of all (filtered) calls
	 */
    public int getTotalDuration() {
        Enumeration<Call> en = filteredCallerData.elements();
        int total = 0;
        Call call;
        while (en.hasMoreElements()) {
            call = en.nextElement();
            total += call.getDuration();
        }
        return total;
    }

    /**
	 * @return Total costs of all filtered calls
	 */
    public int getTotalCosts() {
        Enumeration<Call> en = filteredCallerData.elements();
        int total = 0;
        Call call;
        while (en.hasMoreElements()) {
            call = en.nextElement();
            if (call.getCost() > 0) {
                total += call.getCost();
            }
        }
        return total;
    }

    /**
	 * @param person
	 * @return Returns last call of person
	 */
    public Call findLastCall(Person person) {
        Vector<PhoneNumber> numbers = person.getNumbers();
        if (numbers.size() > 0) {
            Call result = new Call(new CallType(CallType.CALLIN_STR), new Date(0), new PhoneNumber("", false), "port", "route", 0);
            for (PhoneNumber num : numbers) {
                List<Call> l = hashMap.getCall(num);
                if (l != null) {
                    for (Call c : l) {
                        if (c.getCalldate().after(result.getCalldate())) {
                            result = c;
                        }
                    }
                }
            }
            return result;
        }
        return null;
    }

    public void clearList() {
        Debug.info("Clearing caller Table");
        unfilteredCallerData.clear();
        if ((JFritz.getJframe() != null) && (JFritz.getJframe().getCallerTable() != null)) {
            JFritz.getJframe().getCallerTable().clearSelection();
        }
        saveToXMLFile(Main.SAVE_DIR + JFritz.CALLS_FILE, true);
        fireTableDataChanged();
    }

    /**
	 * rows contain the rows of the <b>un</b>filteredCallerData wich will be
	 * removed from the filteredCallerData. Then fireTableChanged is called,
	 * wich will update the filteredCallerData
	 * 
	 * @param rows
	 *            of the filteredCallerData to be removed
	 */
    public synchronized void removeEntries(int[] rows) {
        Vector<Call> removedCalls = new Vector<Call>(rows.length);
        if (rows.length > 0) {
            Call call;
            Person p;
            for (int i = 0; i < rows.length; i++) {
                call = filteredCallerData.get(rows[i]);
                removedCalls.add(call);
                unfilteredCallerData.remove(call);
                hashMap.deleteCall(call.getPhoneNumber(), call);
            }
            for (CallerListListener l : callListListeners) {
                l.callsRemoved((Vector) removedCalls.clone());
            }
            saveToXMLFile(Main.SAVE_DIR + JFritz.CALLS_FILE, true);
            update();
            fireTableDataChanged();
        }
    }

    public void fireTableDataChanged() {
        super.fireTableDataChanged();
    }

    public String getRealColumnName(int columnIndex) {
        String columnName = "";
        if (JFritz.getJframe() != null) {
            Enumeration<TableColumn> en = JFritz.getJframe().getCallerTable().getTableHeader().getColumnModel().getColumns();
            TableColumn col;
            while (en.hasMoreElements()) {
                col = (TableColumn) en.nextElement();
                if (col.getModelIndex() == columnIndex) {
                    columnName = col.getIdentifier().toString();
                }
            }
        }
        return columnName;
    }

    private static void doBackup() {
        CopyFile backup = new CopyFile();
        backup.copy(Main.SAVE_DIR, "xml");
    }

    public Call getSelectedCall() {
        int rows[] = null;
        if (JFritz.getJframe() != null) {
            rows = JFritz.getJframe().getCallerTable().getSelectedRows();
        }
        if ((rows != null) && (rows.length == 1)) {
            return this.filteredCallerData.elementAt(rows[0]);
        } else {
            Debug.errDlg(Main.getMessage("error_choose_one_call"));
        }
        return null;
    }

    /**
	 * @author Brian Jensen
	 * 
	 * function reads the stream line by line using a buffered reader and using
	 * the appropriate parse function based on the structure
	 * 
	 * currently supported file types: JFritz's own export format:
	 * EXPORT_CSV_FOMAT_JFRITZ Exported files from the fritzbox's web interface:
	 * EXPORT_CSV_FORMAT_FRITZBOX Exported files from fritzbox's Push service
	 * EXPORT_CSV_FORMAT_FRITZBOX_PUSHSERVICE Exported files from the new
	 * fritzbox's new Firmware EXPORT_CSV_FORMAT_FRITZBOX_NEWFIRMWARE Exported
	 * files from the fritzbox's web interface:
	 * EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH (english firmware)
	 * 
	 * 
	 * function also has the ability to 'nicely' handle broken CSV lines
	 * 
	 * NOTE: As is standard the caller must close the input stream on exit!
	 * 
	 * @param filename
	 *            of the csv file to import from
	 */
    public synchronized boolean importFromCSVFile(BufferedReader br) {
        long t1, t2;
        t1 = System.currentTimeMillis();
        String line = "";
        boolean isJFritzExport = false;
        boolean isPushFile = false;
        boolean isNewFirmware = false;
        boolean isEnglishFirmware = false;
        boolean isNewEnglishFirmware = false;
        int newEntries = 0;
        try {
            String separator = PATTERN_CSV;
            line = br.readLine();
            Debug.debug("CSV-Header: " + line);
            if (line == null) {
                Debug.error("File empty");
                return false;
            } else {
                Pattern p = Pattern.compile("sep=(\\W{1})");
                Matcher matcher = p.matcher(line);
                if (matcher.find()) {
                    if (matcher.groupCount() == 1) {
                        separator = matcher.group(1);
                        Debug.debug("Separator: " + separator);
                        line = br.readLine();
                        Debug.debug("CSV-Header: " + line);
                    }
                }
            }
            for (IProgressListener listener : progressListeners) {
                listener.setMin(0);
                listener.setMax(400);
            }
            if (line.equals(EXPORT_CSV_FORMAT_JFRITZ) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX_PUSHSERVICE) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX_NEWFIRMWARE) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH) || line.equals(EXPORT_CSV_FORMAT_PUSHSERVICE_ENGLISH) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH_NEW) || line.equals(EXPORT_CSV_FORMAT_PUSHSERVICE_NEW) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX_EN_140426)) {
                if (line.equals(EXPORT_CSV_FORMAT_JFRITZ)) {
                    isJFritzExport = true;
                } else if (line.equals(EXPORT_CSV_FORMAT_FRITZBOX_PUSHSERVICE)) {
                    isPushFile = true;
                } else if (line.equals(EXPORT_CSV_FORMAT_FRITZBOX_NEWFIRMWARE) || line.equals(EXPORT_CSV_FORMAT_PUSHSERVICE_NEW)) {
                    isNewFirmware = true;
                } else if (line.equals(EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH) || line.equals(EXPORT_CSV_FORMAT_PUSHSERVICE_ENGLISH)) {
                    isEnglishFirmware = true;
                } else if (line.equals(EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH_NEW) || line.equals(EXPORT_CSV_FORMAT_FRITZBOX_EN_140426)) {
                    isNewEnglishFirmware = true;
                }
                int linesRead = 0;
                Call c;
                while (!JFritz.isShutdownInvoked() && null != (line = br.readLine())) {
                    linesRead++;
                    if (isJFritzExport) {
                        c = parseCallJFritzCSV(line, separator);
                    } else if (isNewFirmware) {
                        c = parseCallFritzboxNewCSV(line, separator);
                    } else if (isEnglishFirmware) {
                        c = parseCallFritzboxEnglishCSV(line, separator);
                    } else if (isNewEnglishFirmware) {
                        c = parseCallFritzboxNewEnglishCSV(line, separator);
                    } else {
                        c = parseCallFritzboxCSV(line, isPushFile, separator);
                    }
                    if (c == null) {
                        if (!line.equals("")) {
                            Debug.error("Broken entry: " + line);
                        }
                    } else if (addEntry(c)) {
                        newEntries++;
                    }
                    for (IProgressListener listener : progressListeners) {
                        listener.setProgress(linesRead);
                    }
                }
                for (IProgressListener listener : progressListeners) {
                    listener.finished();
                }
                Debug.debug(linesRead + " Lines read from csv file ");
                Debug.debug(newEntries + " New entries processed");
                fireUpdateCallVector();
                if (newEntries > 0) {
                    saveToXMLFile(Main.SAVE_DIR + JFritz.CALLS_FILE, true);
                    String msg;
                    if (newEntries == 1) {
                        msg = Main.getMessage("imported_call");
                    } else {
                        msg = Main.getMessage("imported_calls").replaceAll("%N", Integer.toString(newEntries));
                    }
                    if (Main.getProperty("option.notifyOnCalls").equals("true")) {
                        JFritz.infoMsg(msg);
                    }
                } else {
                }
            } else {
                Debug.error("Wrong file type or corrupted file");
            }
        } catch (FileNotFoundException e) {
            Debug.error("Could not read from File!");
        } catch (IOException e) {
            Debug.error("IO Exception reading csv file");
        }
        t2 = System.currentTimeMillis();
        Debug.always("Time used to import CSV-File: " + (t2 - t1) + "ms");
        if (newEntries > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @author Brian Jensen
	 * 
	 * function first splits the line into substrings, then strips the
	 * quotationmarks(do those have to be?) functions parses according to the
	 * format EXPORT_CSV_FORMAT_JFRITZ
	 * 
	 * 
	 * @param line
	 *            contains the line to be processed from a csv file
	 * @return returns a call object, or null if the csv line is invalid
	 */
    public Call parseCallJFritzCSV(String line, String separator) {
        String[] field = line.split(separator);
        Call call;
        CallType calltype;
        Date calldate;
        PhoneNumber number;
        if (field.length < 12) {
            if (field.length != 1) {
                Debug.error("Invalid CSV format, incorrect number of fields!");
            }
            return null;
        }
        for (int i = 0; i < 12; i++) {
            field[i] = field[i].substring(1, field[i].length() - 1);
        }
        if (field[0].equals("Incoming")) {
            calltype = new CallType("call_in");
        } else if (field[0].equals("Missed")) {
            calltype = new CallType("call_in_failed");
        } else if (field[0].equals("Outgoing")) {
            calltype = new CallType("call_out");
        } else {
            Debug.error("Invalid Call type in CSV entry!");
            return null;
        }
        if ((field[1] != null) && (field[2] != null)) {
            try {
                calldate = new SimpleDateFormat("dd.MM.yy HH:mm").parse(field[1] + " " + field[2]);
            } catch (ParseException e) {
                Debug.error("Invalid date format in csv entry!");
                return null;
            }
        } else {
            Debug.error("Invalid date format in csv entry!");
            return null;
        }
        if (field[5].equals("FON1")) {
            field[5] = "0";
        } else if (field[5].equals("FON2")) {
            field[5] = "1";
        } else if (field[5].equals("FON3")) {
            field[5] = "2";
        } else if (field[5].equals("Durchwahl")) {
            field[5] = "3";
        } else if (field[5].equals("ISDN")) {
            field[5] = "4";
        } else if (field[5].equals("DECT 1")) {
            field[5] = "10";
        } else if (field[5].equals("DECT 2")) {
            field[5] = "11";
        } else if (field[5].equals("DECT 3")) {
            field[5] = "12";
        } else if (field[5].equals("DECT 4")) {
            field[5] = "13";
        } else if (field[5].equals("DECT 5")) {
            field[5] = "14";
        } else if (field[5].equals("DECT 6")) {
            field[5] = "15";
        } else if (field[5].equals("DATA")) {
            field[5] = "36";
        }
        if (!field[3].equals("")) {
            number = new PhoneNumber(field[3], false);
            number.setCallByCall(field[10]);
        } else {
            number = null;
        }
        call = new Call(calltype, calldate, number, field[5], field[4], Integer.parseInt(field[6]));
        call.setComment(field[11]);
        return call;
    }

    /**
	 * @author Brian Jensen function parses a line of a csv file, that was
	 *         directly exported from the Fritzbox web interface, either
	 *         directly or through jfritz
	 * 
	 * function parses according to format: EXPORT_CSV_FORMAT_FRITZBOX and
	 * EXPORT_CSV_FORMAT_FRITZBOX_PUSHSERVICE
	 * 
	 * @param line
	 *            contains the line to be processed
	 * @return is call object, or null if the csv was invalid
	 */
    public Call parseCallFritzboxCSV(String line, boolean isPushFile, String separator) {
        String[] field = line.split(separator);
        Call call;
        CallType calltype;
        Date calldate;
        PhoneNumber number;
        if (field.length != 6) {
            if (field.length != 1) {
                Debug.error("Invalid CSV format, incorrect number of fields!");
            }
            return null;
        }
        if ((field[0].equals("1") && !isPushFile) || (field[0].equals("2") && isPushFile)) {
            calltype = new CallType("call_in");
        } else if ((field[0].equals("2") && !isPushFile) || (field[0].equals("3") && isPushFile)) {
            calltype = new CallType("call_in_failed");
        } else if ((field[0].equals("3") && !isPushFile) || (field[0].equals("1") && isPushFile)) {
            calltype = new CallType("call_out");
        } else {
            Debug.error("Invalid Call type in CSV entry!");
            return null;
        }
        if (field[1] != null) {
            try {
                calldate = new SimpleDateFormat("dd.MM.yy HH:mm").parse(field[1]);
            } catch (ParseException e) {
                Debug.error("Invalid date format in csv entry!");
                return null;
            }
        } else {
            Debug.error("Invalid date format in csv entry!");
            return null;
        }
        if (!field[2].equals("")) {
            number = new PhoneNumber(field[2], Main.getProperty("option.activateDialPrefix").toLowerCase().equals("true") && (calltype.toInt() == CallType.CALLOUT) && !field[4].startsWith("Internet"));
        } else {
            number = null;
        }
        String[] time = field[5].split(":");
        if (field[3].equals("FON 1")) {
            field[3] = "0";
        } else if (field[3].equals("FON 2")) {
            field[3] = "1";
        } else if (field[3].equals("FON 3")) {
            field[3] = "2";
        } else if (field[3].equals("Durchwahl")) {
            field[3] = "3";
        } else if (field[3].equals("FON S0")) {
            field[3] = "4";
        } else if (field[3].equals("DECT 1")) {
            field[3] = "10";
        } else if (field[3].equals("DECT 2")) {
            field[3] = "11";
        } else if (field[3].equals("DECT 3")) {
            field[3] = "12";
        } else if (field[3].equals("DECT 4")) {
            field[3] = "13";
        } else if (field[3].equals("DECT 5")) {
            field[3] = "14";
        } else if (field[3].equals("DECT 6")) {
            field[3] = "15";
        } else if (field[3].equals("DATA S0")) {
            field[3] = "36";
        }
        call = new Call(calltype, calldate, number, field[3], field[4], Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60);
        return call;
    }

    /**
	 * @author KCh function parses a line of a csv file, that was directly
	 *         exported from the Fritzbox web interface with BETA FW or with a
	 *         fritzbox with the new firmware >= XX.04.05
	 * 
	 * 
	 * @param line
	 *            contains the line to be processed
	 * @return is call object, or null if the csv was invalid
	 */
    public Call parseCallFritzboxNewCSV(String line, String separator) {
        String[] field = line.split(separator);
        Call call;
        CallType calltype;
        Date calldate;
        PhoneNumber number;
        if (field.length != 7) {
            if (field.length != 1) {
                Debug.error("Invalid CSV format, incorrect number fields!");
            }
            return null;
        }
        if ((field[0].equals("1"))) {
            calltype = new CallType("call_in");
        } else if ((field[0].equals("2"))) {
            calltype = new CallType("call_in_failed");
        } else if ((field[0].equals("3"))) {
            calltype = new CallType("call_out");
        } else {
            Debug.error("Invalid Call type in CSV entry!");
            return null;
        }
        if (field[1] != null) {
            try {
                calldate = new SimpleDateFormat("dd.MM.yy HH:mm").parse(field[1]);
            } catch (ParseException e) {
                Debug.error("Invalid date format in csv entry!");
                return null;
            }
        } else {
            Debug.error("Invalid date format in csv entry!");
            return null;
        }
        if (!field[3].equals("")) {
            number = new PhoneNumber(field[3], Main.getProperty("option.activateDialPrefix").toLowerCase().equals("true") && (calltype.toInt() == CallType.CALLOUT) && !field[5].startsWith("Internet"));
        } else {
            number = null;
        }
        String[] time = field[6].split(":");
        if (field[4].equals("FON 1")) {
            field[4] = "0";
        } else if (field[4].equals("FON 2")) {
            field[4] = "1";
        } else if (field[4].equals("FON 3")) {
            field[4] = "2";
        } else if (field[4].equals("Durchwahl")) {
            field[4] = "3";
        } else if (field[4].equals("DECT 1")) {
            field[4] = "10";
        } else if (field[4].equals("DECT 2")) {
            field[4] = "11";
        } else if (field[4].equals("DECT 3")) {
            field[4] = "12";
        } else if (field[4].equals("DECT 4")) {
            field[4] = "13";
        } else if (field[4].equals("DECT 5")) {
            field[4] = "14";
        } else if (field[4].equals("DECT 6")) {
            field[4] = "15";
        } else if (field[4].equals("FON S0")) {
            field[4] = "4";
        } else if (field[4].equals("DATA S0")) {
            field[4] = "36";
        }
        call = new Call(calltype, calldate, number, field[4], field[5], Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60);
        return call;
    }

    /**
	 * @author Brian Jensen function parses a line of a csv file, that was
	 *         directly exported from the Fritzbox web interface, either
	 *         directly or through jfritz
	 * 
	 * function parses according to format: EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH
	 * this is the format exported by boxes with english firmwar (unkown
	 * version)
	 * 
	 * Note: This function has yet to be tested!
	 * 
	 * @param line
	 *            contains the line to be processed
	 * @return is call object, or null if the csv was invalid
	 * 
	 */
    public Call parseCallFritzboxEnglishCSV(String line, String separator) {
        String[] field = line.split(separator);
        boolean isPushFile = false;
        Call call;
        CallType calltype;
        Date calldate;
        PhoneNumber number;
        if (field.length != 6) {
            if (field.length != 1) {
                Debug.error("Invalid CSV format, incorrect number of fields");
            }
            return null;
        }
        if ((field[0].equals("1") && !isPushFile) || (field[0].equals("2") && isPushFile)) {
            calltype = new CallType("call_in");
        } else if ((field[0].equals("2") && !isPushFile) || (field[0].equals("3") && isPushFile)) {
            calltype = new CallType("call_in_failed");
        } else if ((field[0].equals("3") && !isPushFile) || (field[0].equals("1") && isPushFile)) {
            calltype = new CallType("call_out");
        } else {
            Debug.error("Invalid Call type in CSV entry!");
            return null;
        }
        if (field[1] != null) {
            try {
                calldate = new SimpleDateFormat("dd.MM.yy HH:mm").parse(field[1]);
            } catch (ParseException e) {
                Debug.error("Invalid date format in csv entry!");
                return null;
            }
        } else {
            Debug.error("Invalid date format in csv entry!");
            return null;
        }
        if (!field[2].equals("")) {
            number = new PhoneNumber(field[2], Main.getProperty("option.activateDialPrefix").toLowerCase().equals("true") && (calltype.toInt() == CallType.CALLOUT) && !field[4].startsWith("Internet"));
        } else {
            number = null;
        }
        String[] time = field[5].split(":");
        if (field[3].equals("FON 1")) {
            field[3] = "0";
        } else if (field[3].equals("FON 2")) {
            field[3] = "1";
        } else if (field[3].equals("FON 3")) {
            field[3] = "2";
        } else if (field[3].equals("Durchwahl")) {
            field[3] = "3";
        } else if (field[3].equals("FON S0")) {
            field[3] = "4";
        } else if (field[3].equals("DECT 1")) {
            field[3] = "10";
        } else if (field[3].equals("DECT 2")) {
            field[3] = "11";
        } else if (field[3].equals("DECT 3")) {
            field[3] = "12";
        } else if (field[3].equals("DECT 4")) {
            field[3] = "13";
        } else if (field[3].equals("DECT 5")) {
            field[3] = "14";
        } else if (field[3].equals("DECT 6")) {
            field[3] = "15";
        } else if (field[3].equals("DATA S0")) {
            field[3] = "36";
        }
        call = new Call(calltype, calldate, number, field[3], field[4], Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60);
        return call;
    }

    /**
	 * @author Brian Jensen 
	 * 
	 * function parses a line of a csv file, that was
	 *         directly exported from the Fritzbox web interface, either
	 *         directly or through jfritz
	 * 
	 * function parses according to format:
	 * EXPORT_CSV_FORMAT_FRITZBOX_ENGLISH_NEW this is the format exported by
	 * boxes with english firmware (xx.04.20)
	 * 
	 * Note: This function has yet to be tested!
	 * 
	 * @param line
	 *            contains the line to be processed
	 * @return is call object, or null if the csv was invalid
	 * 
	 */
    public Call parseCallFritzboxNewEnglishCSV(String line, String separator) {
        String[] field = line.split(separator);
        boolean isPushFile = false;
        Call call;
        CallType calltype;
        Date calldate;
        PhoneNumber number;
        if (field.length != 7) {
            if (field.length != 1) {
                Debug.error("Invalid CSV format, incorrect number of fields");
            }
            return null;
        }
        if ((field[0].equals("1") && !isPushFile) || (field[0].equals("2") && isPushFile)) {
            calltype = new CallType("call_in");
        } else if ((field[0].equals("2") && !isPushFile) || (field[0].equals("3") && isPushFile)) {
            calltype = new CallType("call_in_failed");
        } else if ((field[0].equals("3") && !isPushFile) || (field[0].equals("1") && isPushFile)) {
            calltype = new CallType("call_out");
        } else {
            Debug.error("Invalid Call type in CSV entry!");
            return null;
        }
        if (field[1] != null) {
            try {
                calldate = new SimpleDateFormat("dd.MM.yy HH:mm").parse(field[1]);
            } catch (ParseException e) {
                Debug.error("Invalid date format in csv entry!");
                return null;
            }
        } else {
            Debug.error("Invalid date format in csv entry!");
            return null;
        }
        if (!field[3].equals("")) {
            number = new PhoneNumber(field[3], Main.getProperty("option.activateDialPrefix").toLowerCase().equals("true") && (calltype.toInt() == CallType.CALLOUT) && !field[5].startsWith("Internet"));
        } else {
            number = null;
        }
        String[] time = field[6].split(":");
        if (time.length != 2) {
            time = field[6].split("\\.");
        }
        if (field[4].equals("FON 1")) {
            field[4] = "0";
        } else if (field[4].equals("FON 2")) {
            field[4] = "1";
        } else if (field[4].equals("FON 3")) {
            field[4] = "2";
        } else if (field[4].equals("Durchwahl")) {
            field[4] = "3";
        } else if (field[4].equals("FON S0")) {
            field[4] = "4";
        } else if (field[4].equals("DECT 1")) {
            field[4] = "10";
        } else if (field[4].equals("DECT 2")) {
            field[4] = "11";
        } else if (field[4].equals("DECT 3")) {
            field[4] = "12";
        } else if (field[4].equals("DECT 4")) {
            field[4] = "13";
        } else if (field[4].equals("DECT 5")) {
            field[4] = "14";
        } else if (field[4].equals("DECT 6")) {
            field[4] = "15";
        } else if (field[4].equals("DATA S0")) {
            field[4] = "36";
        }
        call = new Call(calltype, calldate, number, field[4], field[5], Integer.parseInt(time[0]) * 3600 + Integer.parseInt(time[1]) * 60);
        return call;
    }

    /**
	 * adds a Filter to sort out some calls
	 * 
	 * @param cf
	 *            the CallFilter which should be applied
	 * @param name
	 *            the name of the Filter
	 */
    public void addFilter(CallFilter cf) {
        filters.add(cf);
    }

    /**
	 * removes a Filter
	 * 
	 * @param name
	 *            the name of the Filter
	 * @return true if the filter was removed
	 */
    public boolean removeFilter(CallFilter cf) {
        boolean o = filters.remove(cf);
        return o;
    }

    /**
	 * removes a Filter
	 * 
	 */
    public void removeAllFilter() {
        filters.removeAllElements();
    }

    /**
	 * Filters the unfilteredData and writes it to filtered Data all added
	 * Filters are used
	 */
    public Vector<Call> filterData(Vector<Call> src) {
        Vector<Call> result = new Vector<Call>();
        Enumeration<Call> en = src.elements();
        Call call;
        CallFilter f;
        int i;
        while (en.hasMoreElements()) {
            call = en.nextElement();
            for (i = 0; i < filters.size(); i++) {
                f = filters.elementAt(i);
                if (!f.passFilter(call)) {
                    break;
                }
            }
            if (i == filters.size()) {
                result.add(call);
            }
        }
        return result;
    }

    /**
	 * get all Call by Call Providers from the selected rows if no row is
	 * selected return all cbc Providers
	 * 
	 * @return the providers
	 */
    public Vector<String> getCbCProviders(int[] rows) {
        Vector<String> callByCallProviders = new Vector<String>();
        Call call;
        for (int i = 0; i < rows.length; i++) {
            call = filteredCallerData.get(rows[i]);
            addIfCbCProvider(callByCallProviders, call);
        }
        return callByCallProviders;
    }

    private void addIfCbCProvider(Vector<String> callByCallProviders, Call call) {
        String provider = "";
        if (call.getPhoneNumber() != null) {
            provider = call.getPhoneNumber().getCallByCall();
            if (!provider.equals("")) {
                if (!callByCallProviders.contains(provider)) {
                    callByCallProviders.add(provider);
                }
            }
        }
    }

    /**
	 * 
	 * @return all CallByCallProviders
	 */
    public Vector<String> getCbCProviders() {
        Vector<String> callByCallProviders = new Vector<String>();
        Call call;
        for (int i = 0; i < unfilteredCallerData.size(); i++) {
            call = unfilteredCallerData.get(i);
            addIfCbCProvider(callByCallProviders, call);
        }
        return callByCallProviders;
    }

    /**
	 * returns all selected Providers
	 * 
	 * @return the providers
	 */
    public Vector<String> getSelectedProviders(int[] rows) {
        Vector<String> selectedProviders = new Vector<String>();
        Call call;
        for (int i = 0; i < rows.length; i++) {
            call = filteredCallerData.get(rows[i]);
            if (!call.getRoute().equals("")) {
                if (!selectedProviders.contains(call.getRoute())) {
                    selectedProviders.add(call.getRoute());
                }
            }
        }
        return selectedProviders;
    }

    /**
	 * 
	 * @return all SipProviders of the callertable
	 */
    public Vector<String> getAllSipProviders() {
        Vector<String> sipProviders = new Vector<String>();
        Call call;
        for (int i = 0; i < filteredCallerData.size(); i++) {
            call = filteredCallerData.get(i);
            if (!call.getRoute().equals("")) {
                if (call.getRouteType() == Call.ROUTE_SIP) {
                    if (!sipProviders.contains(call.getRoute())) {
                        sipProviders.add(call.getRoute());
                    }
                }
            }
        }
        return sipProviders;
    }

    public void update() {
        filteredCallerData = filterData(unfilteredCallerData);
        sortAllFilteredRowsBy(sortColumn, sortDirection);
        fireTableDataChanged();
    }

    /**
	 * looks up either all filtered/displayed calls or all calls
	 * 
	 * @param filteredOnly
	 *            if false it will lookup all calls
	 * @param searchAlsoDummyEntries
	 *            if true, it will also lookup dummy entries
	 * @return
	 */
    public void reverseLookup(boolean filteredOnly, boolean searchAlsoForDummyEntries) {
        JFritz.getJframe().selectLookupButton(true);
        JFritz.getJframe().setLookupBusy(true);
        Vector<PhoneNumber> numbers = new Vector<PhoneNumber>();
        if (filteredOnly) {
            Call call;
            Person foundPerson;
            for (int i = 0; i < filteredCallerData.size(); i++) {
                call = filteredCallerData.get(i);
                if (call.getPhoneNumber() != null) {
                    foundPerson = phonebook.findPerson(call);
                    if ((foundPerson == null) || (searchAlsoForDummyEntries && foundPerson.isDummy()) && !numbers.contains(call.getPhoneNumber())) {
                        numbers.add(call.getPhoneNumber());
                    }
                }
            }
        } else {
            numbers = getAllUnknownEntries(searchAlsoForDummyEntries);
        }
        reverseLookup(numbers);
    }

    /**
	 * Returns all unknown entries
	 * @param searchAlsoForDummyEntries
	 *            if true, it will also lookup dummy entries
	 * @return all unknown entries            
	 */
    public Vector<PhoneNumber> getAllUnknownEntries(boolean searchAlsoForDummyEntries) {
        Vector<PhoneNumber> numbers = new Vector<PhoneNumber>();
        Call call;
        Person foundPerson;
        for (int i = 0; i < unfilteredCallerData.size(); i++) {
            call = unfilteredCallerData.get(i);
            if (call.getPhoneNumber() != null) {
                foundPerson = phonebook.findPerson(call);
                if ((foundPerson == null || (searchAlsoForDummyEntries && foundPerson.isDummy())) && !numbers.contains(call.getPhoneNumber())) {
                    numbers.add(call.getPhoneNumber());
                }
            }
        }
        return numbers;
    }

    /**
	 * does reverse lookup (find the name and address for a given phone number
	 * 
	 * @param rows
	 *            the rows, wich are selected for reverse lookup
	 */
    public void doReverseLookup(int[] rows) {
        Vector<PhoneNumber> numbers = new Vector<PhoneNumber>();
        Call call;
        for (int i = 0; i < rows.length; i++) {
            call = filteredCallerData.get(rows[i]);
            if (call.getPhoneNumber() != null) {
                numbers.add(call.getPhoneNumber());
            }
        }
        reverseLookup(numbers);
    }

    /**
	 * Does a reverse lookup for all numbers in vector "numbers"
	 * 
	 * @param numbers,
	 *            a vector of numbers to do reverse lookup on
	 */
    public void reverseLookup(Vector<PhoneNumber> numbers) {
        JFritz.getJframe().selectLookupButton(true);
        JFritz.getJframe().setLookupBusy(true);
        ReverseLookup.lookup(numbers, this, false);
    }

    /**
	 * for the LookupObserver
	 */
    public void personsFound(Vector<Person> persons) {
        if (persons != null && persons.size() > 0) {
            phonebook.addEntries(persons);
            update();
        }
        JFritz.getJframe().selectLookupButton(false);
        JFritz.getJframe().setLookupBusy(false);
    }

    /**
	 * for the LookupObserver
	 */
    public void percentOfLookupDone(float f) {
    }

    /**
	 * for the LookupObserver
	 */
    public void saveFoundEntries(Vector<Person> persons) {
        if (persons != null) {
            phonebook.addEntries(persons);
            update();
        }
    }

    public void setPhoneBook(PhoneBook phonebook) {
        this.phonebook = phonebook;
    }

    public PhoneBook getPhoneBook() {
        return phonebook;
    }

    public void stopLookup() {
        ReverseLookup.stopLookup();
    }

    /**
	 *  This function is used to get the date of the last
	 *  call in the list. Used by the network code to get updates
	 *  
	 *  @author brian
	 *
	 */
    public synchronized Date getLastCallDate() {
        if (unfilteredCallerData.size() > 0) return unfilteredCallerData.firstElement().getCalldate();
        return null;
    }

    /** This function is used by the network code to retrieve all calls
	 * newer than that of the timestamp
	 * 
	 * @author brian
	 * 
	 * @param timestamp of the last call received
	 * @return a vector of calls newer than the timestamp
	 */
    public synchronized Vector<Call> getNewerCalls(Date timestamp) {
        Vector<Call> newerCalls = new Vector<Call>();
        DateFilter dateFilter = new DateFilter(timestamp, new Date(System.currentTimeMillis()));
        for (Call call : unfilteredCallerData) {
            if (dateFilter.passFilter(call)) newerCalls.add(call);
        }
        return newerCalls;
    }

    public Vector<CallFilter> getCallFilters() {
        return filters;
    }

    /**
	 * this function is used for determing the call called in a pop up message
	 * 
	 * @param row number of call in table
	 * @return call object
	 */
    public Call getCallAt(int row) {
        return filteredCallerData.elementAt(row);
    }

    public void registerProgressListener(IProgressListener listener) {
        progressListeners.add(listener);
    }

    public void unregisterProgressListener(IProgressListener listener) {
        progressListeners.remove(listener);
    }

    public void contactUpdated(Person original, Person updated) {
        update();
    }

    public void contactsAdded(Vector<Person> newContacts) {
        update();
    }

    public void contactsRemoved(Vector<Person> removedContacts) {
        update();
    }
}
