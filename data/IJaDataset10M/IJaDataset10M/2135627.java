package jcd2.model;

import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.event.DocumentEvent;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import javax.swing.JComboBox;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import jcd2.Main;
import org.apache.xerces.dom.CoreDocumentImpl;
import org.apache.xerces.dom.DocumentImpl;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/**
 *  Cyclist is the basic actor; as such it keeps track of all performed training
 *  units. Training units are stored on a per year basis. Applications can get a
 *  visual overview including cyclist's name, max. Heart Rate and statistics as
 *  a visual proxy.
 *
 * @author     Manfred Crumbach
 * @created    28. Dezember 2001
 */
public class Cyclist extends java.beans.Beans implements java.awt.event.ActionListener, PropertyChangeListener, javax.swing.event.DocumentListener {

    public static final transient String INIT_NAME = "<your name>";

    public static final transient String INIT_YOB = "1978";

    public static final transient String DISTANCE_UNIT = "DistanceUnit";

    private static final long serialVersionUID = 5789815597531903954L;

    private static final transient String XSL_TO_XML = "/jcd2/resources/JCD2xml.xsl";

    private static final transient String UPDATE_UNIT = "Unit";

    private static final transient String UPDATE_INITIALISED = "INITIALISED";

    private static final transient String UPDATE_YEAR = "Year changed";

    private static final transient String UPDATE_DELETE = "deleteLogEntry";

    private static final transient String UPDATE_ADD = "addLogEntry";

    private static final transient String UPDATE_LOG_ENTRY = "LogEntry";

    private static final transient String UPDATE_FOCUS = "FocusLost";

    private static final transient String UPDATE_READ = "ReadXML";

    private final transient PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private transient int progressCounter = 0;

    private transient Map<Integer, List<LogEntry>> allEntries = new TreeMap<Integer, List<LogEntry>>();

    private transient java.util.Set<Integer> allYears = null;

    private transient Integer currentYear = null;

    private transient boolean notify = true;

    private transient boolean hasChanged = false;

    private transient Integer activitiesCount;

    private transient List<LogEntry> activities = null;

    private String name = INIT_NAME;

    private String yearOfBirth = INIT_YOB;

    private Map<Integer, List<LogEntry>> yearlyLists = null;

    private final PropertiesJCD properties = PropertiesJCD.getTheInstance();

    /**
     *  Constructor for the Cyclist object
     */
    public Cyclist() {
        initialise();
    }

    /**
     *  Sets the Unit attribute of the Cyclist object to either metric or
     *  non-metric units
     *
     * @param  metric  use metric units if true
     */
    public void setUnit(boolean metric) {
        Iterator it = null;
        Iterator itr = null;
        Integer year = null;
        Integer cy = null;
        LogEntry logEntry = null;
        notify = false;
        cy = currentYear;
        properties.setProperty(Cyclist.DISTANCE_UNIT, Boolean.toString(metric));
        for (it = allYears.iterator(); it.hasNext(); ) {
            year = (Integer) it.next();
            itr = setCurrentYear(year).iterator();
            while (itr.hasNext()) {
                logEntry = (LogEntry) itr.next();
                logEntry.setUnit(metric);
            }
        }
        setCurrentYear(cy);
        notify = true;
        updateCyclist(UPDATE_UNIT, true);
    }

    /**
     *  Sets the Name attribute of the Cyclist object
     *
     * @param  n  The new Name value
     */
    public void setName(String n) {
        name = n;
    }

    /**
     *  Sets the YearOfBirth attribute of the Cyclist object
     *
     * @param  n  The new YearOfBirth value
     */
    public void setYearOfBirth(String n) {
        yearOfBirth = n;
    }

    /**
     *  set the current year
     *
     * @param  d  specifies the selected year
     * @return    List of Activities performed in the specified year
     */
    public List<LogEntry> setCurrentYear(Date d) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d);
        return setCurrentYear(Integer.valueOf(cal.get(Calendar.YEAR)));
    }

    /**
     *  set the current year
     *
     * @param  i  specifies the selected year
     * @return    List of Activities performed in the specified year
     */
    public List<LogEntry> setCurrentYear(Integer i) {
        if (yearlyLists == null) {
            yearlyLists = new java.util.HashMap<Integer, List<LogEntry>>();
        }
        if (!i.equals(currentYear)) {
            currentYear = i;
            allYears.add(currentYear);
            activities = yearlyLists.get(currentYear);
            if (activities == null) {
                activities = new java.util.ArrayList<LogEntry>();
            }
            yearlyLists.put(currentYear, activities);
            allEntries.put(currentYear, activities);
            updateCyclist(UPDATE_YEAR, false);
        }
        return activities;
    }

    /**
     *  Gets the CurrentYear attribute of the Cyclist object
     *
     * @return    The CurrentYear value
     */
    public Integer getCurrentYear() {
        return currentYear;
    }

    /**
     *  Gets the Activities attribute of the Cyclist object
     *
     * @return    The Activities value
     */
    public List<LogEntry> getActivities() {
        return yearlyLists.get(currentYear);
    }

    /**
     *  Gets the Activities attribute of the Cyclist object
     *
     * @param     year   return activities done in this year only
     * @return    The Activities value
     */
    public List<LogEntry> getActivities(Integer year) {
        return yearlyLists.get(year);
    }

    /**
     *  Gets the Name attribute of the Cyclist object
     *
     * @return    The Name value
     */
    public String getName() {
        return name;
    }

    /**
     *  Gets the YearOfBirth attribute of the Cyclist object
     *
     * @return    The YearOfBirth value
     */
    public String getYearOfBirth() {
        return yearOfBirth;
    }

    /**
     *  Gets the MaxHeartRate attribute of the Cyclist object
     *
     * @return    The MaxHeartRate value
     */
    public Integer getMaxHeartRate() {
        final int max = 220;
        Integer result = null;
        if (yearOfBirth.length() > 0) {
            result = Integer.valueOf(yearOfBirth);
            result = Integer.valueOf(max - currentYear.intValue() + result);
        } else {
            result = Integer.valueOf(max);
        }
        return result;
    }

    /**
     *  Get all Seasons with a log entry
     *
     * @return    A map containing all seasons
     */
    public Map<Integer, List<LogEntry>> getAllLogEntries() {
        return allEntries;
    }

    /**
     *  Flag that shows if the state of Cyclist has changed
     *
     * @return    true or false depending on wether Cyclist was changed or not
     */
    public boolean hasChanged() {
        return hasChanged;
    }

    /**
     *  clear Cyclist in order to create or load a new Cyclist
     */
    public void reset() {
        java.util.Collection coll;
        Iterator it = null;
        List rs = null;
        coll = yearlyLists.values();
        it = coll.iterator();
        while (it.hasNext()) {
            rs = (List) it.next();
            rs.clear();
        }
        yearlyLists.clear();
        allYears.clear();
        initialise();
    }

    /**
     *  react to changes in visual proxy to select year
     *
     * @param  e  standard ActionEvent
     */
    public void actionPerformed(java.awt.event.ActionEvent e) {
        JComboBox cb = (JComboBox) e.getSource();
        Integer y = (Integer) cb.getSelectedItem();
        if ((y != null) && (!y.equals(currentYear))) {
            setCurrentYear(y);
        }
    }

    /**
     *  adds an activity to the list of performed activities
     *
     * @param  activity  the LogEntry to add
     * @return           the LogEntry that was added
     */
    @SuppressWarnings("unchecked")
    public LogEntry add(LogEntry logEntry) {
        ++activitiesCount;
        setCurrentYear(logEntry.getDate());
        activities.add(logEntry);
        Collections.sort(activities);
        allEntries.remove(logEntry.getSeason());
        allEntries.put(logEntry.getSeason(), activities);
        logEntry.addPropertyChangeListener(this);
        updateCyclist(UPDATE_ADD, this, logEntry, true);
        return logEntry;
    }

    /**
     *  Delete LogEntry from Cyclist's list of Activities
     *
     * @param  logEntry Entry that is to be deleted
     */
    public void delete(LogEntry logEntry) {
        Integer y;
        y = getCurrentYear();
        logEntry.removePropertyChangeListener(this);
        if (activities.remove(logEntry)) {
            --activitiesCount;
            updateCyclist(UPDATE_DELETE, this, logEntry, true);
        }
        allEntries.remove(currentYear);
        allEntries.put(currentYear, activities);
        setCurrentYear(y);
    }

    /**
     *  inform listeners of changes
     *
     * @param  property  Plain text to indicate which property was changed
     * @param  changed   flag wether Cyclist has changed (true) or only a
     *      redisplay is necessary (false)
     */
    void updateCyclist(String property, boolean changed) {
        updateCyclist(property, this, changed);
    }

    /**
     *  inform listeners of changes
     *
     * @param  property  Plain text to indicate which property was changed
     * @param  source    Object that has changed
     * @param  changed   flag wether Cyclist has changed (true) or only a
     *      redisplay is necessary (false)
     */
    void updateCyclist(String property, Object source, boolean changed) {
        updateCyclist(property, source, null, changed);
    }

    /**
     *  inform listeners of changes
     *
     * @param  property  Plain text to indicate which property was changed
     * @param  source    Object that has changed
     * @param  changed   flag wether Cyclist has changed (true) or only a
     *      redisplay is necessary (false)
     */
    void updateCyclist(String property, Object source, Object newValue, boolean changed) {
        PropertyChangeEvent pce = new PropertyChangeEvent(source, property, null, newValue);
        updateCyclist(property, pce, changed);
    }

    /**
     *  inform listeners of changes
     *
     * @param  property  Plain text to indicate which property was changed
     * @param  pce       PropertyChangeEvent to be distributed
     * @param  changed   flag wether Cyclist has changed (true) or only a
     *      redisplay is necessary (false)
     */
    void updateCyclist(String property, PropertyChangeEvent pce, boolean changed) {
        if (!hasChanged) {
            hasChanged = changed;
        }
        if (notify) {
            changeSupport.firePropertyChange(pce);
        }
    }

    /**
     *  Adds a feature to the PropertyChangeListener attribute of the Cyclist
     *  object
     *
     * @param  l  The feature to be added to the PropertyChangeListener attribute
     */
    public void addPropertyChangeListener(PropertyChangeListener l) {
        changeSupport.addPropertyChangeListener(l);
    }

    /**
     *  Removes a feature from the PropertyChangeListener attribute of the
     *  Cyclist object
     *
     * @param  l  the feature to be removed from the PropertyChangeListener
     *      attribute
     */
    public void removePropertyChangeListener(PropertyChangeListener l) {
        changeSupport.removePropertyChangeListener(l);
    }

    /**
     *  Cyclist himself listens to changes of all its Activities
     *
     * @param  pce  The PropertyChangeEvent indicating the changes
     */
    public void propertyChange(PropertyChangeEvent pce) {
        LogEntry logEntry = (LogEntry) pce.getSource();
        boolean doNotify = notify;
        notify = false;
        if (pce.getPropertyName().equals("DATE")) {
            delete(logEntry);
            add(logEntry);
        }
        notify = doNotify;
        updateCyclist(UPDATE_LOG_ENTRY, this, logEntry, true);
    }

    /**
     *  this method is called when name or year of birth were changed
     *  (document: changed)
     *
     * @param  de  the DocumentEvent describing the change
     * @see        javax.swing.event.DocumentListener
     */
    public void changedUpdate(DocumentEvent de) {
        updateCyclist(UPDATE_FOCUS, true);
    }

    /**
     *  this method is called when an insert in name or year of birth took place
     *  (document: insert)
     *
     * @param  de  the DocumentEvent describing the change
     * @see        javax.swing.event.DocumentListener
     */
    public void insertUpdate(DocumentEvent de) {
        updateCyclist(UPDATE_FOCUS, true);
    }

    /**
     *  this method is called when a character in name or year of birth was removed
     *  (document: remove)
     *
     * @param  de  the DocumentEvent describing the change
     * @see        javax.swing.event.DocumentListener
     */
    public void removeUpdate(DocumentEvent de) {
        updateCyclist(UPDATE_FOCUS, true);
    }

    /**
     *  write Cyclist's data to a file
     *
     * @param  sfilename        filename to store Cyclist
     * @param  styleSheet       stylesheet to be used
     * @exception  IOException  thrown on problems writing to the file
     */
    public void write(String sfilename) throws IOException, URISyntaxException {
        write(new FileOutputStream(sfilename), getXSL2XML());
    }

    /**
     *  write the cyclist's data out as a file. Depending on the Stylesheet
     *  this can be XML, HTML, ...
     *
     * @param  styleSheet       stylesheet to be used
     * @param  os               Description of Parameter
     * @exception  IOException  thrown on problems writing to the file
     */
    void write(final OutputStream os, final URI styleSheetURI) throws IOException {
        final CoreDocumentImpl doc;
        final Integer actualYear;
        if (styleSheetURI == null) {
            throw new IllegalArgumentException("styleSheet missing!");
        }
        doc = new DocumentImpl();
        actualYear = currentYear;
        notify = false;
        createDOM(doc, doc.createElement("JCycleData"));
        transform(doc, styleSheetURI, new StreamResult(os));
        notify = true;
        hasChanged = false;
        setCurrentYear(actualYear);
    }

    /**
     *  transform a DOM document using a XSL stylesheet
     *
     * @param  doc              DOM document
     * @param  xslURI           String to access XSL stylesheet via ResourceManager
     * @param  xslOutput        Result of the transformation
     * @exception  IOException  I/O operation failed
     * @see                     com.crumbach.tools.ResourceManager
     */
    void transform(CoreDocumentImpl doc, URI xslURI, StreamResult xslOutput) throws IOException {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(xslURI.toString()));
            transformer.transform(new DOMSource(doc), xslOutput);
        } catch (javax.xml.transform.TransformerConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (javax.xml.transform.TransformerException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     *  this method creates a Document Object Model (DOM) for the Cyclist object
     *
     * @param  doc   the DOM
     * @param  root  root node of the DOM
     */
    void createDOM(Document doc, Element root) {
        Element top = null;
        Element elem = null;
        Element elemData = null;
        org.w3c.dom.Text text = null;
        DateFormat datef = DateFormat.getDateTimeInstance();
        Iterator it = null;
        Iterator itr = null;
        Integer year;
        progressCounter = 0;
        updateStatus(0);
        if (root == null) {
            throw new IllegalArgumentException("root is null");
        } else {
            top = root;
        }
        doc.appendChild(top);
        top.setAttribute("modified", datef.format(new Date()));
        top.setAttribute(DISTANCE_UNIT, properties.getProperty(PropertiesJCD.UNIT_METRIC));
        elemData = doc.createElement("locale");
        elemData.setAttribute("country", Locale.getDefault().getCountry());
        elemData.setAttribute("language", Locale.getDefault().getLanguage());
        top.appendChild(elemData);
        elemData = doc.createElement("Cyclist");
        elemData.setAttribute("NumberOfActivities", activitiesCount.toString());
        top.appendChild(elemData);
        elem = doc.createElement("Name");
        text = doc.createTextNode(getName());
        elem.appendChild(text);
        elemData.appendChild(elem);
        elem = doc.createElement("YearOfBirth");
        text = doc.createTextNode(getYearOfBirth());
        elem.appendChild(text);
        elemData.appendChild(elem);
        it = allYears.iterator();
        while (it.hasNext()) {
            year = (Integer) it.next();
            elem = doc.createElement("Season");
            elem.setAttribute("Year", year.toString());
            elemData.appendChild(elem);
            setCurrentYear(year);
            itr = activities.iterator();
            while (itr.hasNext()) {
                updateStatus(++progressCounter);
                ((LogEntry) itr.next()).createDOM(doc, elem);
            }
        }
    }

    private void updateStatus(int i) {
        ApplicationContext _ctxt;
        Task task = null;
        _ctxt = Main.getApplication().getContext();
        TaskMonitor taskMon = _ctxt.getTaskMonitor();
        Iterator<Task> it = taskMon.getTasks().iterator();
        while (it.hasNext()) {
            task = it.next();
            task.firePropertyChange("progress", i - 1, i);
        }
    }

    /**
     *  initialisation of Cyclist object
     */
    private void initialise() {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        currentYear = Integer.valueOf(-1);
        if (allYears == null) {
            allYears = new java.util.TreeSet<Integer>();
        }
        if (name == null) {
            name = INIT_NAME;
        }
        if (yearOfBirth == null) {
            yearOfBirth = "1969";
        }
        activitiesCount = 0;
        this.setName(INIT_NAME);
        this.setYearOfBirth(INIT_YOB);
        activities = setCurrentYear(Integer.valueOf(cal.get(Calendar.YEAR)));
        updateCyclist(UPDATE_INITIALISED, false);
    }

    private URI getXSL2XML() throws URISyntaxException {
        return getClass().getResource(XSL_TO_XML).toURI();
    }

    /**
     * Prevent notification of Listeners on changes, e.g. during load
     */
    public void notifyPause() {
        notify = false;
    }

    /**
     * Resume notification of Listeners on changes
     */
    public void notifyResume(boolean changed) {
        notify = true;
        hasChanged = changed;
        updateCyclist(UPDATE_READ, true);
    }
}
