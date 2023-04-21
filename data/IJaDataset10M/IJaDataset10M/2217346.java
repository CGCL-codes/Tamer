package com.funambol.syncclient.google;

import com.funambol.framework.logging.FunambolLogger;
import com.funambol.framework.logging.FunambolLoggerFactory;
import com.funambol.syncclient.spds.SyncException;
import com.funambol.syncclient.google.panels.SyncSetPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Hashtable;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import com.funambol.syncclient.common.DeviceTools;
import com.funambol.syncclient.common.logging.Logger;
import com.funambol.syncclient.spdm.DMException;
import com.funambol.syncclient.spdm.ManagementNode;
import com.funambol.syncclient.spdm.SimpleDeviceManager;
import com.funambol.syncclient.spds.SyncManager;
import com.funambol.syncclient.spds.event.SyncEvent;
import com.funambol.syncclient.spds.event.SyncItemEvent;
import com.funambol.syncclient.spds.event.SyncItemListener;
import com.funambol.syncclient.spds.event.SyncListener;
import com.funambol.syncclient.spds.event.SyncSourceEvent;
import com.funambol.syncclient.spds.event.SyncSourceListener;
import com.funambol.syncclient.spds.event.SyncStatusEvent;
import com.funambol.syncclient.spds.event.SyncStatusListener;
import com.funambol.syncclient.spds.event.SyncTransportEvent;
import com.funambol.syncclient.spds.event.SyncTransportListener;
import com.funambol.syncclient.google.panels.CommunicationSetPanel;
import com.funambol.syncclient.google.utils.Constants;
import com.funambol.syncclient.google.utils.Language;

/**
 * The main window for the google Client GUI.
 *
 * @version $Id: MainWindow.java,v 1.0 2007/06/20 16:19:47 Paulo F. Exp $
 */
public class MainWindow extends JFrame implements ActionListener, Constants, SyncItemListener, SyncListener, SyncSourceListener, SyncStatusListener, SyncTransportListener {

    private ManagementNode rootNode = null;

    /**
     * DM values from DM_VALUE_PATH
     */
    private Hashtable syncmlValues = null;

    /**
     * DM values from DM_VALUE_CONTACT_PATH
     */
    private Hashtable xmlContactValues = null;

    /**
     *DM values for DM_VALUE_CALENDAR_PATH
     */
    private Hashtable xmlCalendarValues = null;

    /**
     * If true there was a SyncError event into synchronization process, false
     * otherwise
     */
    private boolean syncError = true;

    /**
     * The root directory
     */
    private String rootDirectory = null;

    /**
     * The source directory
     */
    private String sourceDirectory = "";

    private JLabel jlStatus;

    private JButton btSync;

    private MenuBar menuBar;

    protected FunambolLogger log = FunambolLoggerFactory.getLogger("funambol.google");

    public static final String PROP_CHARSET = "spds.charset";

    public static final String PROP_WD = "wd";

    public static final String VALUE_UTF8 = "UTF8";

    public static final String ROOT_DIRECTORY = "config";

    public static final String DM_VALUE_PATH = "spds/syncml";

    public static final String DM_VALUE_CONTACT_PATH = "spds/sources/contact";

    public static final String DM_VALUE_CALENDAR_PATH = "spds/sources/calendar";

    public static void main(String[] args) throws Exception {
        MainWindow mw = new MainWindow();
        mw.setVisible(true);
    }

    /**
     *
     * Creates an instance of MainWindow
     *
     */
    public MainWindow() {
        Language.init();
        createAndShowMainGUI();
        Properties props = System.getProperties();
        rootDirectory = System.getProperty(PROP_WD);
        if (System.getProperty(SimpleDeviceManager.PROP_DM_DIR_BASE) == null) {
            props.put(SimpleDeviceManager.PROP_DM_DIR_BASE, buildPath(rootDirectory, ROOT_DIRECTORY));
            System.setProperties(props);
        }
        props = System.getProperties();
        props.put(PROP_CHARSET, VALUE_UTF8);
        System.setProperties(props);
        sourceDirectory = buildPath(rootDirectory, sourceDirectory);
        rootNode = SimpleDeviceManager.getDeviceManager().getManagementTree();
        checkDeviceID();
        loadConfiguration();
    }

    /**
     * Create and show the main GUI.
     */
    public void createAndShowMainGUI() {
        getContentPane().setLayout(new BorderLayout(1, 1));
        setTitle(Language.getMessage(Language.LABEL_TITLE_MAINWINDOW));
        setIconImage(Toolkit.getDefaultToolkit().getImage(FRAME_ICONNAME));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(280, 362);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(new Color(236, 233, 216));
        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Color.WHITE);
        getContentPane().add(mainPanel, BorderLayout.CENTER);
        JLabel lab = new JLabel(new ImageIcon(FRAME_LOGONAME));
        lab.setBounds(72, 30, 128, 128);
        mainPanel.add(lab);
        btSync = new JButton(Language.getMessage(Language.BT_SYNC));
        btSync.setFont(new Font("Microsoft Sans Serif", 0, 13));
        btSync.setBounds(81, 233, 112, 24);
        btSync.setBackground(Color.WHITE);
        btSync.addActionListener(this);
        mainPanel.add(btSync);
        mainPanel.setBorder(BorderFactory.createRaisedBevelBorder());
        jlStatus = new JLabel();
        jlStatus.setFont(font);
        jlStatus.setPreferredSize(new Dimension(260, 25));
        getContentPane().add(jlStatus, BorderLayout.SOUTH);
        menuBar = new MenuBar(this);
        setJMenuBar(menuBar);
    }

    /**
     *Runs the synchronization inside a thread
     */
    public void runSynchronization() {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        setStatusMessage(Language.getMessage(Language.STATUS_SYNC_BEGIN));
        new Thread(new Runnable() {

            public void run() {
                btSync.setEnabled(false);
                menuBar.setEnableSyncMenuItem(false);
                synchronize();
                btSync.setEnabled(true);
                menuBar.setEnableSyncMenuItem(true);
            }
        }).start();
    }

    /**
     * Start the synchronization process based on the configuration parameters
     */
    private void synchronize() {
        try {
            System.setProperty(SimpleDeviceManager.PROP_DM_DIR_BASE, "config");
            SyncManager syncManager = SyncManager.getSyncManager("");
            syncManager.addSyncItemListener(this);
            syncManager.addSyncListener(this);
            syncManager.addSyncSourceListener(this);
            syncManager.addSyncStatusListener(this);
            syncManager.addSyncTransportListener(this);
            syncManager.sync();
        } catch (SyncException ex) {
            if (log.isInfoEnabled()) {
                log.info("An error occurred during synchronization:" + ex.getMessage(), ex);
            }
            setErrorMessage("An error occurred during synchronization.");
        } catch (DMException ex) {
            if (log.isInfoEnabled()) {
                log.info("An error occurred during synchronization:" + ex.getMessage(), ex);
            }
            setErrorMessage("An error occurred during synchronization.");
        } catch (Exception ex) {
            if (log.isInfoEnabled()) {
                log.info("Unknown error:" + ex.getMessage(), ex);
            }
        }
        if (syncError) {
            setStatusMessage(Language.getMessage(Language.ERROR_SYNC));
        }
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    /**
     * Show the message for the status of synchronization process
     *
     * @param msg the message to show
     */
    private void setErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    /**
     * Show the message for the status of synchronization process
     *
     * @param msg the message to show
     */
    private void setStatusMessage(String msg) {
        jlStatus.setText(msg);
        jlStatus.setAutoscrolls(true);
        jlStatus.setHorizontalAlignment(SwingConstants.CENTER);
    }

    public void actionPerformed(ActionEvent e) {
        if (checkSyncSettings()) {
            if (checkServerLoginSettings()) {
                if (checkGoogleLoginSettings()) runSynchronization(); else {
                    CommunicationSetPanel c = new CommunicationSetPanel(this, true);
                    c.setVisible(true);
                }
            } else {
                CommunicationSetPanel c = new CommunicationSetPanel(this, true);
                c.setVisible(true);
            }
        } else {
            SyncSetPanel c = new SyncSetPanel(this, true);
            c.setVisible(true);
        }
    }

    /**
     * Fired to notify that an item addition has been received
     *
     * @param syncItemEvent the SyncItemEvent event
     */
    public void itemAddedByServer(SyncItemEvent syncItemEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCITEM_ADDSERVER) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncItemEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncItemEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired to notify that an item deletion has been received
     *
     * @param syncItemEvent the SyncItemEvent event
     */
    public void itemDeletedByServer(SyncItemEvent syncItemEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCITEM_DELETESERVER) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncItemEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncItemEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired to notify that an item update has been received.
     *
     * @param syncItemEvent the SyncItemEvent event
     */
    public void itemUpdatedByServer(SyncItemEvent syncItemEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCITEM_UPDATESERVER) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncItemEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncItemEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired to notify that an item addition has been sent.
     *
     * @param syncItemEvent the SyncItemEvent event
     */
    public void itemAddedByClient(SyncItemEvent syncItemEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCITEM_ADDCLIENT) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncItemEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncItemEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired to notify that an item deletion has been sent.
     *
     * @param syncItemEvent the SyncItemEvent event
     */
    public void itemDeletedByClient(SyncItemEvent syncItemEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCITEM_DELETECLIENT) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncItemEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncItemEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired to notify that an item update has been sent.
     *
     * @param syncItemEvent the SyncItemEvent event
     */
    public void itemUpdatedByClient(SyncItemEvent syncItemEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCITEM_UPDATECLIENT) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncItemEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncItemEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired when the synchronization process start.
     *
     * @param syncEvent the synchronization event
     */
    public void syncBegin(SyncEvent syncEvent) {
        syncError = false;
        if (Logger.isLoggable(Logger.INFO)) {
            Logger.info(syncEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNC_BEGIN));
        }
    }

    /**
     * Fired when the synchronization process end.
     *
     * @param syncEvent the synchronization event
     */
    public void syncEnd(SyncEvent syncEvent) {
        if (Logger.isLoggable(Logger.INFO)) {
            Logger.info(syncEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNC_END));
        }
        setStatusMessage(Language.getMessage(Language.STATUS_SYNC_END));
    }

    /**
     * Fired to notify that the initialization package was correctly set and
     * precessed.
     *
     * @param syncEvent the synchronization event
     */
    public void sendInitialization(SyncEvent syncEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(syncEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNC_SENDINIT));
        }
    }

    /**
     * Fired to notify that the modifications package was correctly set and
     * processed
     *
     * @param syncEvent the synchronization event
     */
    public void sendModification(SyncEvent syncEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(syncEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNC_SENDMOD));
        }
    }

    /**
     * Fired to notify that the final package was correctly set and processed.
     *
     * @param syncEvent the synchronization event
     */
    public void sendFinalization(SyncEvent syncEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(syncEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNC_SENDFINAL));
        }
    }

    /**
     * Fired to notify that the engine encountered a not blocking error
     *
     * @param syncEvent the synchronization event
     */
    public void syncError(SyncEvent syncEvent) {
        syncError = true;
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(syncEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNC_ERROR) + " - " + Language.getMessage(Language.LOG_SYNCERROR_MSG) + " " + syncEvent.getMessage() + " - " + Language.getMessage(Language.LOG_SYNCCAUSE_MSG) + " " + syncEvent.getCause().getMessage());
        }
    }

    /**
     * Fired to notify the beginning of the synchronization of a SyncSource
     *
     * @param syncSourceEvent the SyncSource event
     */
    public void syncBegin(SyncSourceEvent syncSourceEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(syncSourceEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNCSOURCE_BEGIN) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncSourceEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SYNCMODE) + " " + syncSourceEvent.getSyncMode());
        }
    }

    /**
     * Fired to notify the end of the synchronization of a SyncSource
     *
     * @param syncSourceEvent the SyncSource event
     */
    public void syncEnd(SyncSourceEvent syncSourceEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(syncSourceEvent.getDate() + ", " + Language.getMessage(Language.LOG_SYNCSOURCE_END) + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncSourceEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SYNCMODE) + " " + syncSourceEvent.getSyncMode());
        }
    }

    public void statusReceived(SyncStatusEvent syncStatusEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCSTATUS_RECEIVED) + " - " + Language.getMessage(Language.LOG_SYNCSTATUS_CMD) + " " + syncStatusEvent.getCommand() + " - " + Language.getMessage(Language.LOG_SYNCSTATUS_STATUS) + " " + syncStatusEvent.getStatusCode() + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncStatusEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncStatusEvent.getItemKey().getKeyAsString());
        }
    }

    public void statusToSend(SyncStatusEvent syncStatusEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCSTATUS_SEND) + " - " + Language.getMessage(Language.LOG_SYNCSTATUS_CMD) + " " + syncStatusEvent.getCommand() + " - " + Language.getMessage(Language.LOG_SYNCSTATUS_STATUS) + " " + syncStatusEvent.getStatusCode() + " - " + Language.getMessage(Language.LOG_SYNCSOURCE_SOURCEURI) + " " + syncStatusEvent.getSourceUri() + " - " + Language.getMessage(Language.LOG_SYNCITEM_KEY) + " " + syncStatusEvent.getItemKey().getKeyAsString());
        }
    }

    /**
     * Fired to notify that the engine started to send data to the server
     *
     * @param syncTransportEvent the SyncTransportEvent event
     */
    public void sendDataBegin(SyncTransportEvent syncTransportEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCTRANSPORT_SENDDATABEGIN) + " " + syncTransportEvent.getData());
        }
    }

    /**
     * Fired to notify that the engine has sent all data to the server
     *
     * @param syncTransportEvent the SyncTransportEvent event
     */
    public void sendDataEnd(SyncTransportEvent syncTransportEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCTRANSPORT_SENDDATAEND) + " " + syncTransportEvent.getData());
        }
    }

    /**
     * Fired to notify that the engine started to receive data from the server
     *
     * @param syncTransportEvent the SyncTransportEvent event
     */
    public void receiveDataBegin(SyncTransportEvent syncTransportEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCTRANSPORT_RECEIVEDATABEGIN) + " " + syncTransportEvent.getData());
        }
    }

    /**
     * Fired to notify that the engine is receiving data from the server
     *
     * @param syncTransportEvent the SyncTransportEvent event
     */
    public void dataReceived(SyncTransportEvent syncTransportEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCTRANSPORT_RECEIVEDATA) + " " + syncTransportEvent.getData());
        }
    }

    /**
     * Fired to notify that the engine has received all data from the server
     *
     * @param syncTransportEvent the SyncTransportEvent event
     */
    public void receiveDataEnd(SyncTransportEvent syncTransportEvent) {
        if (Logger.isLoggable(Logger.DEBUG)) {
            Logger.debug(Language.getMessage(Language.LOG_SYNCTRANSPORT_RECEIVEDATAEND) + " " + syncTransportEvent.getData());
        }
    }

    /**
     * Closes the window and stops the application
     */
    public void exit() {
        setVisible(false);
        dispose();
        if (Logger.isLoggable(Logger.INFO)) {
            Logger.info(Language.getMessage(Language.LOGGING_STOPPED));
        }
        System.exit(0);
    }

    /**
     * Check if there is a valid username and password for syncmlserver
     *
     * @return true if there is a valid login , otherwise false
     */
    private boolean checkServerLoginSettings() {
        String usernameSyncml = (String) syncmlValues.get(PARAM_USERNAME);
        String passwordSyncml = (String) syncmlValues.get(PARAM_PASSWORD);
        if ((usernameSyncml.length() < 1) && (passwordSyncml.length() < 1)) {
            return false;
        }
        return true;
    }

    /**
     * MODIFIED
     * Check if there is least one source to synchronize
     *
     * @return true if there is least one source to sync, otherwise false
     */
    private boolean checkSyncSettings() {
        String contactSM = (String) xmlContactValues.get(PARAM_SYNCMODE);
        String calendarSM = (String) xmlCalendarValues.get(PARAM_SYNCMODE);
        String serverUrl = (String) syncmlValues.get(PARAM_SYNCMLURL);
        if (contactSM.equalsIgnoreCase(SYNC_NONE) || (serverUrl.length() < 1) && calendarSM.equalsIgnoreCase(SYNC_NONE)) {
            return false;
        }
        return true;
    }

    /**
     * Check if google is set to sync, and if it is then check is the user or password is blank
     *
     * @return true if the username and password have lenght >0 sync, otherwise false
     */
    private boolean checkGoogleLoginSettings() {
        String contactUsername = (String) xmlContactValues.get(PARAM_GOOGLE_USERNAME);
        String contactPassword = (String) xmlContactValues.get(PARAM_GOOGLE_PASSWORD);
        String calendarUsername = (String) xmlCalendarValues.get(PARAM_GOOGLE_USERNAME);
        String calendarPassword = (String) xmlCalendarValues.get(PARAM_GOOGLE_PASSWORD);
        String calendarSM = (String) xmlCalendarValues.get(PARAM_SYNCMODE);
        String contactSM = (String) xmlContactValues.get(PARAM_SYNCMODE);
        if (!calendarSM.equalsIgnoreCase(SYNC_NONE)) {
            if ((calendarUsername.length() < 1) || (calendarPassword.length() < 1)) {
                return false;
            }
        }
        if (!contactSM.equalsIgnoreCase(SYNC_NONE)) {
            if ((contactUsername.length() < 1) || (contactPassword.length() < 1)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the device id is not null or empty. If so, a new device id will
     * be created and stored in the dm tree
     */
    public void checkDeviceID() {
        String deviceID = null;
        try {
            deviceID = (String) rootNode.getNodeValue(DM_VALUE_PATH, PARAM_DEVICEID);
            if (deviceID == null || deviceID.length() == 0) {
                deviceID = DeviceTools.createDeviceID(PREFIX_DEVICE_ID, DEFAULT_DEVICE_ID);
                rootNode.setValue(DM_VALUE_PATH, PARAM_DEVICEID, deviceID);
            }
        } catch (DMException e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_DEVICE_MANAGER));
                Logger.error(e.getMessage());
            }
        }
    }

    /**
     * Loads DM configuration
     */
    public void loadConfiguration() {
        try {
            syncmlValues = rootNode.getNodeValues(DM_VALUE_PATH);
            xmlContactValues = rootNode.getNodeValues(DM_VALUE_CONTACT_PATH);
            xmlCalendarValues = rootNode.getNodeValues(DM_VALUE_CALENDAR_PATH);
            String logLevel = (String) syncmlValues.get(PARAM_LOGLEVEL);
            if (logLevel.equals(LOG_INFO)) {
                Logger.setLevel(Logger.INFO);
            } else if (logLevel.equals(LOG_DEBUG)) {
                Logger.setLevel(Logger.DEBUG);
            }
            Logger.setDefaultLogFile();
        } catch (DMException e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_DEVICE_MANAGER));
                Logger.error(e.getMessage());
            }
        }
    }

    /**
     * Saves the specified configuration parameters
     * by passing them to the DeviceManager
     *
     * @param values a list of calendar configuration values
     */
    public void writeCalendarSyncSettings(Hashtable values) {
    }

    /**
     * Saves the specified configuration parameters
     * by passing them to the DeviceManager
     *
     * @param values a list of configuration values
     */
    public void writeSyncSettings(Hashtable values) {
        try {
            xmlContactValues.put(PARAM_SYNCMODE, values.get(PARAM_SYNCCONTACT));
            xmlCalendarValues.put(PARAM_SYNCMODE, values.get(PARAM_SYNCCALENDAR));
            syncmlValues.put(PARAM_LOGLEVEL, values.get(PARAM_LOGLEVEL));
            rootNode.setValue(DM_VALUE_PATH, PARAM_LOGLEVEL, values.get(PARAM_LOGLEVEL));
            rootNode.setValue(DM_VALUE_CONTACT_PATH, PARAM_SYNCMODE, values.get(PARAM_SYNCCONTACT));
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_SYNCMODE, values.get(PARAM_SYNCCALENDAR));
        } catch (DMException e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_DEVICE_MANAGER));
                Logger.error(e.getMessage());
            }
        } catch (Exception e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_WRITE_SYNCSET));
                Logger.error(e.getMessage());
            }
        }
    }

    /**
     * Saves the specified configuration parameters
     * by passing them to the DeviceManager
     *
     * @param values a list of configuration values
     */
    public void writeRemoteSettings(Hashtable values) {
        try {
            xmlContactValues.put(PARAM_SYNCSOURCEURI, values.get(PARAM_SOURCEURICONTACT));
            xmlCalendarValues.put(PARAM_SYNCSOURCEURI, values.get(PARAM_SOURCEURICALENDAR));
            rootNode.setValue(DM_VALUE_CONTACT_PATH, PARAM_SYNCSOURCEURI, values.get(PARAM_SOURCEURICONTACT));
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_SYNCSOURCEURI, values.get(PARAM_SOURCEURICALENDAR));
        } catch (DMException e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_DEVICE_MANAGER));
                Logger.error(e.getMessage());
            }
        } catch (Exception e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_WRITE_REMOTESET));
                Logger.error(e.getMessage());
            }
        }
    }

    /**
     * MODIFIED
     * Saves the specified Google interval to calendar synchronization
     * by passing them to the DeviceManager
     *
     * @param values a list of configuration values
     */
    public void writeGoogleCalendarInterval(Hashtable calendarValues) {
        xmlCalendarValues.put(PARAM_GOOGLE_CALENDAR_SINCE, calendarValues.get(PARAM_GOOGLE_CALENDAR_SINCE));
        xmlCalendarValues.put(PARAM_GOOGLE_CALENDAR_UNTIL, calendarValues.get(PARAM_GOOGLE_CALENDAR_UNTIL));
        try {
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_GOOGLE_CALENDAR_SINCE, calendarValues.get(PARAM_GOOGLE_CALENDAR_SINCE));
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_GOOGLE_CALENDAR_UNTIL, calendarValues.get(PARAM_GOOGLE_CALENDAR_UNTIL));
        } catch (DMException ex) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_WRITE_COMMUNICATIONSET));
                Logger.error(ex.getMessage());
            }
        }
    }

    /**
     * Saves the specified Yahoo configuration parameters
     * by passing them to the DeviceManager
     *
     * @param values a list of configuration values
     */
    public void writeGoogleSettings(Hashtable contactValues, Hashtable calendarValues) {
        if (calendarValues.get(PARAM_GOOGLE_LASTMODIFIED) == null) System.out.print("OLA MUNDO!!!!!");
        try {
            xmlContactValues.put(PARAM_GOOGLE_USERNAME, contactValues.get(PARAM_GOOGLE_USERNAME));
            xmlContactValues.put(PARAM_GOOGLE_PASSWORD, contactValues.get(PARAM_GOOGLE_PASSWORD));
            if (contactValues.get(PARAM_GOOGLE_LASTMODIFIED) != null) xmlContactValues.put(PARAM_GOOGLE_LASTMODIFIED, contactValues.get(PARAM_GOOGLE_LASTMODIFIED));
            xmlCalendarValues.put(PARAM_GOOGLE_USERNAME, calendarValues.get(PARAM_GOOGLE_USERNAME));
            xmlCalendarValues.put(PARAM_GOOGLE_PASSWORD, calendarValues.get(PARAM_GOOGLE_PASSWORD));
            if (calendarValues.get(PARAM_GOOGLE_LASTMODIFIED) != null) xmlCalendarValues.put(PARAM_GOOGLE_LASTMODIFIED, calendarValues.get(PARAM_GOOGLE_LASTMODIFIED));
            rootNode.setValue(DM_VALUE_CONTACT_PATH, PARAM_GOOGLE_USERNAME, contactValues.get(PARAM_GOOGLE_USERNAME));
            rootNode.setValue(DM_VALUE_CONTACT_PATH, PARAM_GOOGLE_PASSWORD, contactValues.get(PARAM_GOOGLE_PASSWORD));
            rootNode.setValue(DM_VALUE_CONTACT_PATH, PARAM_GOOGLE_LASTMODIFIED, contactValues.get(PARAM_GOOGLE_LASTMODIFIED));
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_GOOGLE_USERNAME, calendarValues.get(PARAM_GOOGLE_USERNAME));
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_GOOGLE_PASSWORD, calendarValues.get(PARAM_GOOGLE_PASSWORD));
            rootNode.setValue(DM_VALUE_CALENDAR_PATH, PARAM_GOOGLE_LASTMODIFIED, calendarValues.get(PARAM_GOOGLE_LASTMODIFIED));
        } catch (DMException ex) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_WRITE_COMMUNICATIONSET));
                Logger.error(ex.getMessage());
            }
        }
    }

    /**
     * Saves the specified configuration parameters
     * by passing them to the DeviceManager
     *
     * @param values a list of configuration values
     */
    public void writeCommunicationSettings(Hashtable values) {
        try {
            syncmlValues.put(PARAM_SYNCMLURL, values.get(PARAM_SYNCMLURL));
            syncmlValues.put(PARAM_USERNAME, values.get(PARAM_USERNAME));
            syncmlValues.put(PARAM_PASSWORD, values.get(PARAM_PASSWORD));
            syncmlValues.put(PARAM_DEVICEID, values.get(PARAM_DEVICEID));
            rootNode.setValue(DM_VALUE_PATH, PARAM_TARGETLOCALURI, values.get(PARAM_SYNCMLURL));
            rootNode.setValue(DM_VALUE_PATH, PARAM_SYNCMLURL, values.get(PARAM_SYNCMLURL));
            rootNode.setValue(DM_VALUE_PATH, PARAM_USERNAME, values.get(PARAM_USERNAME));
            rootNode.setValue(DM_VALUE_PATH, PARAM_PASSWORD, values.get(PARAM_PASSWORD));
            rootNode.setValue(DM_VALUE_PATH, PARAM_DEVICEID, values.get(PARAM_DEVICEID));
        } catch (DMException e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_DEVICE_MANAGER));
                Logger.error(e.getMessage());
            }
        } catch (Exception e) {
            if (Logger.isLoggable(Logger.ERROR)) {
                Logger.error(Language.getMessage(Language.ERROR_WRITE_COMMUNICATIONSET));
                Logger.error(e.getMessage());
            }
        }
        loadConfiguration();
        checkDeviceID();
    }

    public File getFileLog() {
        File f = new File(syncmlValues.get(PARAM_LOG_FILE).toString());
        if (f.exists()) {
            return f;
        }
        return null;
    }

    /**
     * Returns a string representing the path of the specified dir
     * concatenating it with the specified source directory
     *
     * @param sourceDirectory the source directory
     * @param dir the directory
     * @return the directory's path
     */
    private String buildPath(String sourceDirectory, String dir) {
        return sourceDirectory + "/" + dir;
    }

    public Hashtable getSyncmlValues() {
        return this.syncmlValues;
    }

    public Hashtable getXmlContactValues() {
        return this.xmlContactValues;
    }

    public Hashtable getXmlCalendarValues() {
        return this.xmlCalendarValues;
    }
}
