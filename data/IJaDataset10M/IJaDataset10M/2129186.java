package edu.biik.controller;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.swing.UIManager;
import edu.biik.database.SnortDAO;
import edu.biik.datatransforms.DataFilterer;
import edu.biik.datatransforms.StaticDataStore;
import edu.biik.entities.AlertSummary;
import edu.biik.entities.InitialData;
import edu.biik.entities.Sensor;
import edu.biik.framework.AttackClassType;
import edu.biik.framework.BiikVisualizationFrame;
import edu.biik.framework.Event;
import edu.biik.framework.LinkedVisualization;
import edu.biik.framework.LoadingDialogBox;
import edu.biik.gui.BiikMainWindow;
import edu.biik.gui.InitializationDialog;
import edu.biik.visualizations.authenticationzoomin.AuthenticationZoominFrame;
import edu.biik.visualizations.destzoomin.DestZoomInFrame;
import edu.biik.visualizations.doszoomin.DosZoominFrame;
import edu.biik.visualizations.main.MainVisualizationFrame;
import edu.biik.visualizations.misczoomin.MiscZoominFrame;
import edu.biik.visualizations.protocolportactivityzoomin.ProtocolPortActivityZoominFrame;
import edu.biik.visualizations.recon.ReconZoominFrame;
import edu.biik.visualizations.sourcezoomin.SourceZoomInFrame;
import edu.biik.visualizations.suspiciouszoomin.SuspiciousZoominFrame;
import edu.biik.visualizations.timeslicehistogram.TimeSliceHistogramPanel;
import edu.biik.visualizations.webzoomin.WebZoomInFrame;
import edu.biik.visualizations.shellcodezoomin.ShellcodeZoomInFrame;

;

public class ApplicationController {

    private Date startDate = null;

    private Date endDate = null;

    private static ApplicationController controller = null;

    private InitializationDialog initDialog = null;

    private BiikMainWindow mainWindow = null;

    private SnortDAO snortDAO = null;

    private Collection<Event> eventList = null;

    private Properties dataSourceProperties = null;

    private ArrayList<LinkedVisualization> linkedVisualizations;

    private BiikVisualizationFrame biikMainVizWindow;

    private Set<Event> currentlyActiveEvents = null;

    private Sensor activeSensor = null;

    private SortedSet<Event> currentlyLoadedEvents = null;

    private Set<Event> currentlyHiddenEvents = null;

    private HashMap<Integer, Event> eventsMap = null;

    private double granularityMultiplier = 1.0;

    public boolean debugMode = true;

    private AlertSummary alertSummary = null;

    private SuspiciousZoominFrame suspiciousZoominFrame;

    private SourceZoomInFrame sourceZoomInFrame;

    private DestZoomInFrame destZoomInFrame;

    private ReconZoominFrame reconZoominFrame;

    private DosZoominFrame dosZoominFrame;

    private AuthenticationZoominFrame authenticationZoominFrame;

    private MiscZoominFrame miscZoominFrame;

    private ShellcodeZoomInFrame shellcodeZoominFrame;

    private WebZoomInFrame webZoominFrame;

    private ProtocolPortActivityZoominFrame protocolPortActivityZoominFrame;

    private TimeSliceHistogramPanel thPanel;

    private ApplicationController() {
        initDialog = new InitializationDialog();
        currentlyActiveEvents = new HashSet<Event>();
        currentlyLoadedEvents = new TreeSet<Event>();
        currentlyHiddenEvents = new HashSet<Event>();
        eventsMap = new HashMap<Integer, Event>();
    }

    public void startApplication() {
        if (debugMode) {
            long start = System.currentTimeMillis();
            snortDAO = SnortDAO.createLocalSnortDAO();
            startDate = snortDAO.getEvent(1, 1).getTimestamp();
            endDate = snortDAO.getEvent(1, 2544).getTimestamp();
            eventList = snortDAO.getEventListForTimeSlice(startDate, endDate);
            loadAllEvents();
            showMainApplication();
            return;
        }
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        initDialog.setLocation((int) screenSize.getWidth() / 2 - initDialog.getWidth() / 2, (int) screenSize.getHeight() / 2 - initDialog.getHeight() / 2);
        initDialog.setVisible(true);
    }

    public static synchronized ApplicationController getInstance() {
        if (controller == null) {
            controller = new ApplicationController();
        }
        return controller;
    }

    public void connectToDB(String ipAddress, String userName, String password, String schema) throws Exception {
        snortDAO = SnortDAO.createSnortDAO(ipAddress, userName, password, schema);
        if (snortDAO == null) {
            throw new Exception("Unable to connect to database. Check your username/password.");
        }
    }

    private void loadAllEvents() {
        for (Event e : eventList) {
            currentlyLoadedEvents.add(e);
            eventsMap.put(e.getCaseID(), e);
        }
    }

    public void filterLoadedEvents(Set<Integer> setOfFileredIDs) {
        for (Integer i : setOfFileredIDs) {
            currentlyLoadedEvents.remove(i);
        }
    }

    public InitialData getInitialData() {
        return snortDAO.getInitialData();
    }

    public InitialData getInitialData(Sensor sensor) {
        return snortDAO.getInitialData(sensor);
    }

    public int getNumOfEventsInRange(Date startTime, Date endTime) {
        return snortDAO.getEventCount(startTime, endTime);
    }

    public void retrieveEvents(Date startTime, Date endTime) {
        this.startDate = startTime;
        this.endDate = endTime;
        eventList = snortDAO.getEventListForTimeSlice(startTime, endTime);
        loadAllEvents();
    }

    public void retrieveEventsWithSensor(Date startTime, Date endTime) {
        this.startDate = startTime;
        this.endDate = endTime;
        eventList = snortDAO.getEventListForTimeSlice(startTime, endTime, activeSensor);
        loadAllEvents();
    }

    public void showMainApplication() {
        initDialog.setVisible(false);
        mainWindow = new BiikMainWindow();
        retrieveAlertSummary();
        displayHistogram();
        mainWindow.setVisible(true);
        mainWindow.initializeAlertSummaryPanel(alertSummary);
    }

    private void displayHistogram() {
        linkedVisualizations = new ArrayList<LinkedVisualization>();
        thPanel = new TimeSliceHistogramPanel(linkedVisualizations);
        thPanel.setEventList(currentlyLoadedEvents);
        thPanel.processData();
        mainWindow.displayHistogram(thPanel);
    }

    public void updateHistogram() {
        thPanel.refresh();
        thPanel.refreshWithDataChanges();
        System.out.println("Refreshing.");
    }

    public void launchMainVisualization() {
        biikMainVizWindow = new MainVisualizationFrame("Main Visualization", linkedVisualizations);
        biikMainVizWindow.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                biikMainVizWindow.processData();
                loadBox.setVisible(false);
                biikMainVizWindow.refresh();
                biikMainVizWindow.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    public void clearActiveEvents() {
        currentlyActiveEvents.clear();
    }

    public void addToActiveEvents(Event e) {
        currentlyActiveEvents.add(e);
    }

    public void updateEventsTable() {
        mainWindow.populateTable(new ArrayList<Event>(currentlyActiveEvents));
    }

    public void refreshAllVisualizations() {
        for (LinkedVisualization vizFrame : linkedVisualizations) {
            if (vizFrame.isVisible()) {
                vizFrame.refresh();
            }
        }
    }

    public void refreshAllVisualizationsWithDataChanges() {
        for (LinkedVisualization linkedViz : linkedVisualizations) {
            if (linkedViz.isVisible()) {
                linkedViz.updateLoadedEvents(currentlyLoadedEvents);
                linkedViz.refreshWithDataChanges();
            }
        }
    }

    public void launchSourceIPZoomInVisualization(String sourceIP) {
        sourceZoomInFrame = new SourceZoomInFrame(sourceIP, linkedVisualizations);
        sourceZoomInFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                sourceZoomInFrame.processData();
                loadBox.setVisible(false);
                sourceZoomInFrame.refresh();
                sourceZoomInFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    public boolean isEventCurrentlyActive(Event e) {
        return currentlyActiveEvents.contains(e);
    }

    public void launchDestIPZoomInVisualization(String destIP) {
        Set<Event> filteredEvents = new HashSet<Event>();
        for (Event e : currentlyLoadedEvents) {
            if (e.getIpheader().getDestinationIP().equals(destIP)) {
                filteredEvents.add(e);
            }
        }
        destZoomInFrame = new DestZoomInFrame(destIP, linkedVisualizations);
        destZoomInFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                destZoomInFrame.processData();
                loadBox.setVisible(false);
                destZoomInFrame.refresh();
                destZoomInFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    public void hideSomeEvents() {
        for (int i = 75; i < 500; i++) {
            Event event = eventsMap.get(i);
            if (event != null) {
                currentlyHiddenEvents.add(event);
            }
        }
        currentlyLoadedEvents.removeAll(currentlyHiddenEvents);
        refreshAllVisualizationsWithDataChanges();
    }

    public boolean isEventCurrentlyHidden(Event e) {
        return currentlyHiddenEvents.contains(e);
    }

    public void launchAttackClassVisualization(String attackClass) {
        AttackClassType attackClassType = StaticDataStore.getInstance().determineAttackClassType(attackClass);
        switch(attackClassType) {
            case AUTHENTICATION:
                {
                    launchAuthenticationZoomin();
                    break;
                }
            case WEB_APP:
                {
                    launchWebZoomin();
                    break;
                }
            case DOS:
                {
                    launchDosZoomin();
                    break;
                }
            case RECON:
                {
                    launchReconZoomin();
                    break;
                }
            case SUSPICIOUS_ITEMS:
                {
                    launchSuspiciousZoomin();
                    break;
                }
            case PROTOCOL_PORT:
                {
                    launchProtocolPortActivityZoomin();
                    break;
                }
            case SHELLCODE:
                {
                    launchShellcodeZoomin();
                    break;
                }
            default:
                launchMiscZoomin();
        }
    }

    private void launchAuthenticationZoomin() {
        authenticationZoominFrame = new AuthenticationZoominFrame("Authentication Zoom-in", linkedVisualizations);
        authenticationZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                authenticationZoominFrame.processData();
                loadBox.setVisible(false);
                authenticationZoominFrame.refresh();
                authenticationZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    private void launchReconZoomin() {
        reconZoominFrame = new ReconZoominFrame("Reconnaissance Zoom-in", linkedVisualizations);
        reconZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                reconZoominFrame.processData();
                loadBox.setVisible(false);
                reconZoominFrame.refresh();
                reconZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    private void launchDosZoomin() {
        dosZoominFrame = new DosZoominFrame("Denial of Service Zoom-in", linkedVisualizations);
        dosZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                dosZoominFrame.processData();
                loadBox.setVisible(false);
                dosZoominFrame.refresh();
                dosZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    private void launchSuspiciousZoomin() {
        suspiciousZoominFrame = new SuspiciousZoominFrame("Suspicious Items Zoom-in", linkedVisualizations);
        suspiciousZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable suspiciousVizProcess = new Runnable() {

            public void run() {
                suspiciousZoominFrame.processData();
                loadBox.setVisible(false);
                suspiciousZoominFrame.refresh();
                suspiciousZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(suspiciousVizProcess);
    }

    private void launchWebZoomin() {
        webZoominFrame = new WebZoomInFrame("Web Application Attack Zoom-in", linkedVisualizations);
        webZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable webVizProcess = new Runnable() {

            public void run() {
                webZoominFrame.processData();
                loadBox.setVisible(false);
                webZoominFrame.refresh();
                webZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(webVizProcess);
    }

    private void launchMiscZoomin() {
        miscZoominFrame = new MiscZoominFrame("Misc Attack Zoom-in", linkedVisualizations);
        miscZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                miscZoominFrame.processData();
                loadBox.setVisible(false);
                miscZoominFrame.refresh();
                miscZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    private void launchShellcodeZoomin() {
        shellcodeZoominFrame = new ShellcodeZoomInFrame("Shellcode Attack Zoom-in", linkedVisualizations);
        shellcodeZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                shellcodeZoominFrame.processData();
                loadBox.setVisible(false);
                shellcodeZoominFrame.refresh();
                shellcodeZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    private void launchProtocolPortActivityZoomin() {
        protocolPortActivityZoominFrame = new ProtocolPortActivityZoominFrame("Protocol & Port Activity Zoom-in", linkedVisualizations);
        protocolPortActivityZoominFrame.setEventList(currentlyLoadedEvents);
        final LoadingDialogBox loadBox = new LoadingDialogBox();
        Runnable mainVizProcess = new Runnable() {

            public void run() {
                protocolPortActivityZoominFrame.processData();
                loadBox.setVisible(false);
                protocolPortActivityZoominFrame.refresh();
                protocolPortActivityZoominFrame.showVisualization();
            }
        };
        loadBox.runProcessAndShow(mainVizProcess);
    }

    public Event getEvent(int eventID) {
        return eventsMap.get(eventID);
    }

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        ApplicationController controller = ApplicationController.getInstance();
        controller.startApplication();
    }

    public Set<Event> getCurrentlyActiveEvents() {
        return currentlyActiveEvents;
    }

    public Set<Event> getCurrentlyHiddenEvents() {
        return currentlyHiddenEvents;
    }

    public void updateVisualizationsBasedOnFilters() {
        final LoadingDialogBox diagBox = new LoadingDialogBox();
        Runnable refreshThread = new Runnable() {

            public void run() {
                DataFilterer.updateLoadedEventsBasedOnFilters(eventList, currentlyLoadedEvents);
                refreshAllVisualizationsWithDataChanges();
                diagBox.setVisible(false);
            }
        };
        diagBox.runProcessAndShow(refreshThread);
    }

    private void retrieveAlertSummary() {
        alertSummary = new AlertSummary();
        alertSummary.setDestCount(snortDAO.getDestCount(startDate, endDate));
        alertSummary.setEarliestTimestamp(snortDAO.getEarliestTimestampInRange(startDate, endDate));
        alertSummary.setLatestTimestamp(snortDAO.getLatestTimestampInRange(startDate, endDate));
        alertSummary.setNumOfEvents(snortDAO.getEventCount(startDate, endDate));
        alertSummary.setSourceCount(snortDAO.getSourceCount(startDate, endDate));
        alertSummary.setTopAttackersList(snortDAO.getTopAttackers(startDate, endDate));
        alertSummary.setTopSignaturesList(snortDAO.getTopSignatures(startDate, endDate));
        alertSummary.setTotalAttacksByProtocol(snortDAO.getTotalAttacksByProtocol(startDate, endDate));
        alertSummary.setTotalAttacksBySeverityList(snortDAO.getTotalAttacksBySeverity(startDate, endDate));
        alertSummary.setTotalAttacksBySigClass(snortDAO.getTotalAttacksBySigClass(startDate, endDate));
    }

    public void closeApplication() {
        mainWindow.setVisible(false);
        System.exit(0);
    }

    public void setActiveSensor(Sensor activeSensor) {
        this.activeSensor = activeSensor;
    }

    public Sensor getActiveSensor() {
        return activeSensor;
    }

    public List<Sensor> getAvailableSensors() throws Exception {
        return snortDAO.getAvailableSensors();
    }

    public double getGranularityMultiplier() {
        return granularityMultiplier;
    }

    public void setGranularityMultiplier(double granularityMultiplier) {
        this.granularityMultiplier = granularityMultiplier;
    }
}
