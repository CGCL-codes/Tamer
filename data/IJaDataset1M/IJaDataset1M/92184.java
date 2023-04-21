package lngs;

import lngs.util.StatusMessageCallback;
import lngs.util.ProxyManager;
import lngs.lotus.LotusNotesManager;
import lngs.lotus.LotusNotesCalendarEntry;
import lngs.google.GoogleManager;
import com.google.gdata.data.calendar.CalendarEventEntry;
import java.io.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.URL;
import java.awt.Cursor;
import java.awt.TrayIcon;
import java.awt.SystemTray;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.MenuItem;
import java.awt.AWTException;
import java.awt.event.*;
import java.math.BigDecimal;
import javax.swing.*;
import lngs.util.ConfigurationManager;

public class MainGUI extends javax.swing.JFrame implements StatusMessageCallback {

    public static void main(String args[]) throws Exception {
        if (args.length == 0) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    lngs.MainGUI gui = new MainGUI();
                    gui.setupSystemTray();
                    gui.setLocationRelativeTo(null);
                    gui.jButton_Synchronize.requestFocus();
                }
            });
        } else if (args[0].equals("-silent")) {
            new MainGUI().runCommandLine();
            System.exit(exitCode);
        } else {
            System.out.println("Usage: mainGUI <options>\n\tIf no options are specified, then the application starts in GUI mode.\n\t-silent  Performs synchronization with existing settings in non-GUI mode.");
            System.exit(EXIT_INVALID_PARM);
        }
    }

    public MainGUI() {
        preInitComponents();
        initComponents();
        URL urlIcon = getClass().getResource(iconAppPath);
        if (urlIcon == null) {
            System.out.println("The program icon could not be found at this resource path: " + iconAppPath);
            System.exit(EXIT_MISSING_RESOURCE);
        }
        iconApp = new ImageIcon(urlIcon);
        setIconImage(iconApp.getImage());
        syncScheduleListener = new SyncScheduleListener();
        syncTimer = new javax.swing.Timer(600000, syncScheduleListener);
        syncTimer.setCoalesce(true);
        syncTimer.setRepeats(true);
        syncTimer.stop();
        proxy = new ProxyManager();
        config = new ConfigurationManager();
        config.readConfig();
        loadSettings();
        validate();
        validateSettings();
        setDateRange();
        helpFilename = getClass().getResource(getClass().getSimpleName() + ".class").getPath();
        int slashIdx;
        int jarIdx = helpFilename.lastIndexOf(".jar!");
        if (jarIdx == -1) slashIdx = helpFilename.lastIndexOf("/"); else slashIdx = helpFilename.lastIndexOf("/", jarIdx);
        helpFilename = helpFilename.substring(0, slashIdx + 1) + "HelpFile.html";
        if (!config.getApplicationVersion().equals(appVersion)) {
            setVisible(true);
        }
    }

    private void preInitComponents() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            Logger.getLogger(MainGUI.class.getName()).log(Level.SEVERE, "There was an error setting the window's look-and-feel.", ex);
        }
    }

    protected void setupSystemTray() {
        TrayIcon trayIcon = null;
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            int t;
            t = tray.getTrayIconSize().height;
            t = tray.getTrayIconSize().width;
            Image image = iconApp.getImage();
            PopupMenu popup = new PopupMenu();
            ActionListener listenerOpen = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(NORMAL);
                }
            };
            MenuItem defaultItem = new MenuItem("Open");
            defaultItem.addActionListener(listenerOpen);
            popup.add(defaultItem);
            ActionListener listenerSync = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(NORMAL);
                    jButton_SynchronizeActionPerformed(null);
                }
            };
            MenuItem syncItem = new MenuItem("Open and Sync");
            syncItem.addActionListener(listenerSync);
            popup.add(syncItem);
            ActionListener listenerExit = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    formWindowClosed(null);
                }
            };
            MenuItem exitItem = new MenuItem("Exit");
            exitItem.addActionListener(listenerExit);
            popup.add(exitItem);
            trayIcon = new TrayIcon(image, "Lotus Notes Google Sync", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addActionListener(listenerOpen);
            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        } else {
        }
    }

    /**
     * Runs the synchronization in silent mode using existing configuration settings.
     */
    public void runCommandLine() {
        try {
            this.setVisible(false);
            isSilentMode = true;
            doSync();
        } catch (Exception ex) {
            exitCode = EXIT_EXCEPTION;
            System.out.println("\nGeneral problem: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Perform synchronization independent of GUI or non-GUI mode.
     */
    public void doSync() throws Exception {
        long startTime = 0L;
        DateFormat dfShort = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat tfDefault = DateFormat.getTimeInstance();
        try {
            if (!jButton_Synchronize.isEnabled()) {
                return;
            }
            startTime = System.currentTimeMillis();
            proxy.deactivateNow();
            statusClear();
            setDateRange();
            String strNow = dfShort.format(new Date()) + " " + tfDefault.format(new Date());
            if (config.getSyncOnStartup()) statusAppendLine("Automatic sync-on-startup is enabled. Starting sync - " + strNow); else statusAppendLine("Starting sync - " + strNow);
            statusAppendLineDiag("Application Version: " + appVersion);
            statusAppendLineDiag("OS: " + System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"));
            statusAppendLineDiag("Java: " + System.getProperty("java.version") + " " + System.getProperty("java.vendor"));
            statusAppendLineDiag("Java Home: " + System.getProperty("java.home"));
            statusAppendLineDiag("Java Classpath: " + System.getProperty("java.class.path"));
            statusAppendLineDiag("Java Library Path: " + System.getProperty("java.library.path"));
            statusAppendLineDiag("Local Server: " + jCheckBox_LotusNotesServerIsLocal.isSelected());
            statusAppendLineDiag("Destination Calendar: " + jTextField_DestinationCalendarName.getText());
            statusAppendLineDiag("Use Proxy: " + jCheckBox_enableProxy.isSelected());
            statusAppendLineDiag("Use SSL: " + jCheckBox_GoogleSSL.isSelected());
            statusAppendLineDiag("Sync All Subjects To Value: " + jCheckBox_SyncAllSubjectsToValue.isSelected());
            statusAppendLineDiag("Sync Location and Room: " + jCheckBox_SyncLocationAndRoom.isSelected());
            statusAppendLineDiag("Sync Description: " + jCheckBox_SyncDescription.isSelected());
            statusAppendLineDiag("Sync Alarms: " + jCheckBox_SyncAlarms.isSelected());
            statusAppendLineDiag("Sync Days In Past: " + jTextField_SyncDaysInPast.getText());
            statusAppendLineDiag("Sync Days In Future: " + jTextField_SyncDaysInFuture.getText());
            statusAppendLine("Date range: " + dfShort.format(minStartDate) + " thru " + dfShort.format(maxEndDate) + " (-" + syncDaysInPast + " to +" + syncDaysInFuture + " days)");
            LotusNotesManager lotusNotesService = new LotusNotesManager();
            lotusNotesService.setStatusMessageCallback(this);
            lotusNotesService.setRequiresAuth(true);
            lotusNotesService.setCredentials(jTextField_LotusNotesUsername.getText(), new String(jPasswordField_LotusNotesPassword.getPassword()));
            String lnServer = jTextField_LotusNotesServer.getText();
            if (jCheckBox_LotusNotesServerIsLocal.isSelected()) lnServer = "";
            lotusNotesService.setServer(lnServer);
            lotusNotesService.setMailFile(jTextField_LotusNotesMailFile.getText());
            lotusNotesService.setMinStartDate(minStartDate);
            lotusNotesService.setMaxEndDate(maxEndDate);
            lotusNotesService.setDiagnosticMode(jCheckBox_DiagnosticMode.isSelected());
            ArrayList<LotusNotesCalendarEntry> lotusCalEntries = lotusNotesService.getCalendarEntries();
            statusAppendLine(lotusCalEntries.size() + " Lotus entries found within date range");
            statusAppendLineDiag("Lotus Version: " + lotusNotesService.getNotesVersion());
            if (jCheckBox_enableProxy.isSelected()) {
                if (!jTextField_proxyUsername.getText().isEmpty()) {
                    proxy.enableProxyAuthentication(true);
                    proxy.setProxyUser(jTextField_proxyUsername.getText());
                    proxy.setProxyPassword(new String(jPasswordField_proxyPassword.getPassword()));
                }
                proxy.activateNow();
            }
            boolean GoogleConnectUsingSSL = jCheckBox_GoogleSSL.isSelected();
            GoogleManager googleService = new GoogleManager();
            googleService.setStatusMessageCallback(this);
            googleService.setUsername(jTextField_GoogleUsername.getText());
            googleService.setPassword(new String(jPasswordField_GooglePassword.getPassword()));
            googleService.setCalendarName(jTextField_DestinationCalendarName.getText());
            googleService.setUseSSL(GoogleConnectUsingSSL);
            googleService.setDiagnosticMode(jCheckBox_DiagnosticMode.isSelected());
            googleService.setSyncDescription(jCheckBox_SyncDescription.isSelected());
            googleService.setSyncAlarms(jCheckBox_SyncAlarms.isSelected());
            googleService.setSyncWhere(jCheckBox_SyncLocationAndRoom.isSelected());
            googleService.setSyncAllSubjectsToValue(jCheckBox_SyncAllSubjectsToValue.isSelected());
            googleService.setSyncAllSubjectsToThisValue(jTextField_SyncAllSubjectsToThisValue.getText());
            googleService.setSyncMeetingAttendees(jCheckBox_SyncMeetingAttendees.isSelected());
            googleService.setMinStartDate(minStartDate);
            googleService.setMaxEndDate(maxEndDate);
            googleService.Connect();
            ArrayList<CalendarEventEntry> googleCalEntries = googleService.getCalendarEntries();
            statusAppendLine(googleCalEntries.size() + " Google entries found within date range");
            statusAppendStart("Comparing Lotus Notes and Google calendar entries");
            googleService.compareCalendarEntries(lotusCalEntries, googleCalEntries);
            statusAppendFinished();
            statusAppendLine(lotusCalEntries.size() + " Google entries to create. " + googleCalEntries.size() + " entries to delete.");
            if (googleCalEntries.size() > 0) {
                statusAppendStart("Deleting old Google calendar entries");
                int deleteCount = googleService.deleteCalendarEntries(googleCalEntries);
                statusAppendFinished();
                statusAppendLine(deleteCount + " Google entries deleted");
            }
            if (lotusCalEntries.size() > 0) {
                statusAppendStart("Creating new Google calendar entries");
                int createdCount = 0;
                createdCount = googleService.createCalendarEntries(lotusCalEntries);
                statusAppendFinished();
                statusAppendLine(createdCount + " Google entries created");
            }
        } catch (Exception ex) {
            statusAppendException("There was an error synchronizing.", ex);
        } finally {
            long elapsedMillis = System.currentTimeMillis() - startTime;
            BigDecimal elapsedSecs = new BigDecimal(elapsedMillis / 1000.0).setScale(1, BigDecimal.ROUND_HALF_UP);
            statusAppendLine("Finished sync (" + elapsedSecs + " s total) - " + dfShort.format(new Date()) + " " + tfDefault.format(new Date()));
        }
    }

    protected void showNewVersionMessage() {
        lngs.NewVersionDialog nvd = new NewVersionDialog(this, false);
        nvd.SetAppVersion(appVersion);
        nvd.SetHelpFilename(helpFilename);
        nvd.setVisible(true);
        config.setApplicationVersion(appVersion);
        try {
            saveSettings();
        } catch (Exception ex) {
            statusAppendException("There was an error saving settings.", ex);
        }
    }

    class SyncSwingWorker extends SwingWorker<Void, Void> {

        @Override
        protected Void doInBackground() {
            try {
                syncTimer.stop();
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                doSync();
            } catch (Exception ex) {
                statusAppendException("There was an error synchronizing the calendars.", ex);
            } finally {
                setSyncScheduleDelay();
                setCursor(Cursor.getDefaultCursor());
            }
            return null;
        }
    }

    protected List<Integer> syncMinOffsetsList = new ArrayList<Integer>();

    protected javax.swing.Timer syncTimer;

    public SyncScheduleListener syncScheduleListener;

    public class SyncScheduleListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent evt) {
            new SyncSwingWorker().execute();
        }
    }

    ;

    protected void setSyncScheduleDelay() {
        syncTimer.stop();
        if (syncMinOffsetsList.size() == 0 || !jCheckBox_SyncAtMinOffsets.isSelected()) {
            jLabel_NextSyncTime.setVisible(false);
            return;
        }
        Calendar cal = Calendar.getInstance();
        int currMin = cal.get(Calendar.MINUTE);
        double currMins = currMin + (cal.get(Calendar.SECOND) / 60.0);
        int delayMsecs = -1;
        for (Integer offsetMins : syncMinOffsetsList) {
            if (offsetMins > currMins) {
                delayMsecs = (int) (((double) offsetMins - currMins) * 60000.0);
                cal.set(Calendar.MINUTE, offsetMins);
                break;
            }
        }
        if (delayMsecs == -1) {
            delayMsecs = (int) ((60.0 - currMins + (double) syncMinOffsetsList.get(0)) * 60000.0);
            cal.add(Calendar.HOUR, 1);
            cal.set(Calendar.MINUTE, syncMinOffsetsList.get(0));
        }
        if (delayMsecs < 500) delayMsecs = 500;
        syncTimer.setInitialDelay(delayMsecs);
        syncTimer.setDelay(delayMsecs);
        syncTimer.start();
        DateFormat dfShort = DateFormat.getTimeInstance(DateFormat.SHORT);
        jLabel_NextSyncTime.setText("Next sync: " + dfShort.format(cal.getTime()));
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jButton_Cancel = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jButton_Synchronize = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea_Status = new javax.swing.JTextArea();
        jLabel_NextSyncTime = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jCheckBox_SyncOnStart = new javax.swing.JCheckBox();
        jCheckBox_DiagnosticMode = new javax.swing.JCheckBox();
        jLabel17 = new javax.swing.JLabel();
        jCheckBox_SyncDescription = new javax.swing.JCheckBox();
        jCheckBox_SyncAlarms = new javax.swing.JCheckBox();
        jLabel20 = new javax.swing.JLabel();
        jTextField_DestinationCalendarName = new javax.swing.JTextField();
        jCheckBox_SyncMeetingAttendees = new javax.swing.JCheckBox();
        jLabel21 = new javax.swing.JLabel();
        jTextField_SyncDaysInPast = new javax.swing.JFormattedTextField();
        jTextField_SyncDaysInFuture = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jTextField_SyncMinOffsets = new javax.swing.JTextField();
        jCheckBox_SyncAtMinOffsets = new javax.swing.JCheckBox();
        jCheckBox_SyncAllSubjectsToValue = new javax.swing.JCheckBox();
        jCheckBox_SyncLocationAndRoom = new javax.swing.JCheckBox();
        jTextField_SyncAllSubjectsToThisValue = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jCheckBox_GoogleSSL = new javax.swing.JCheckBox();
        jPasswordField_GooglePassword = new javax.swing.JPasswordField();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField_GoogleUsername = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jPasswordField_LotusNotesPassword = new javax.swing.JPasswordField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jTextField_LotusNotesUsername = new javax.swing.JTextField();
        jTextField_LotusNotesMailFile = new javax.swing.JTextField();
        jTextField_LotusNotesServer = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jCheckBox_enableProxy = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jTextField_proxyIP = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTextField_proxyPort = new javax.swing.JTextField();
        jCheckBox_LotusNotesServerIsLocal = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();
        jTextField_proxyUsername = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPasswordField_GooglePassword1 = new javax.swing.JPasswordField();
        jPasswordField_proxyPassword = new javax.swing.JPasswordField();
        jLabel16 = new javax.swing.JLabel();
        jButton_Help = new javax.swing.JButton();
        jButton_Minimize = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Lotus Notes to Google Calendar Synchronizer (LNGS)");
        setBackground(new java.awt.Color(254, 254, 254));
        setMaximizedBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }

            public void windowIconified(java.awt.event.WindowEvent evt) {
                formWindowIconified(evt);
            }

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jButton_Cancel.setMnemonic('x');
        jButton_Cancel.setText("Exit");
        jButton_Cancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_CancelActionPerformed(evt);
            }
        });
        jTabbedPane1.setMaximumSize(new java.awt.Dimension(105, 88));
        jLabel10.setText("Define the Settings then click Synchronize.");
        jButton_Synchronize.setMnemonic('y');
        jButton_Synchronize.setText("Synchronize");
        jButton_Synchronize.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SynchronizeActionPerformed(evt);
            }
        });
        jTextArea_Status.setColumns(20);
        jTextArea_Status.setEditable(false);
        jTextArea_Status.setRows(5);
        jTextArea_Status.setText("Status messages display here.\n");
        jScrollPane1.setViewportView(jTextArea_Status);
        jLabel_NextSyncTime.setText("Next sync");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 476, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup().add(jLabel10, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 215, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 42, Short.MAX_VALUE).add(jLabel_NextSyncTime, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 110, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(18, 18, 18).add(jButton_Synchronize))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton_Synchronize).add(jLabel10).add(jLabel_NextSyncTime)).add(18, 18, 18).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE).addContainerGap()));
        jTabbedPane1.addTab("Perform Sync", jPanel2);
        jLabel11.setForeground(new java.awt.Color(51, 51, 255));
        jLabel11.setText("General Settings");
        jCheckBox_SyncOnStart.setText("Sync On Startup");
        jCheckBox_SyncOnStart.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncOnStart.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncOnStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jCheckBox_DiagnosticMode.setText("Diagnostic Mode");
        jCheckBox_DiagnosticMode.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_DiagnosticMode.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_DiagnosticMode.setPreferredSize(new java.awt.Dimension(100, 23));
        jLabel17.setForeground(new java.awt.Color(51, 51, 255));
        jLabel17.setText("Data To Sync");
        jCheckBox_SyncDescription.setText("Descriptions");
        jCheckBox_SyncDescription.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncDescription.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncDescription.setPreferredSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAlarms.setText("Alarms Become Google Reminders");
        jCheckBox_SyncAlarms.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAlarms.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncAlarms.setPreferredSize(new java.awt.Dimension(100, 23));
        jLabel20.setText("Destination Calendar Name");
        jTextField_DestinationCalendarName.setToolTipText("The calendar name is case sensitive, i.e. \"my cal\" is different then \"My Cal\".");
        jTextField_DestinationCalendarName.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_DestinationCalendarNameFocusLost(evt);
            }
        });
        jCheckBox_SyncMeetingAttendees.setText("Attendees are Listed at Top of Description");
        jCheckBox_SyncMeetingAttendees.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncMeetingAttendees.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncMeetingAttendees.setPreferredSize(new java.awt.Dimension(100, 23));
        jLabel21.setForeground(new java.awt.Color(51, 51, 255));
        jLabel21.setText("Sync Date Range");
        jTextField_SyncDaysInPast.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        jTextField_SyncDaysInPast.setText("7");
        jTextField_SyncDaysInPast.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_SyncDaysInPastFocusLost(evt);
            }
        });
        jTextField_SyncDaysInFuture.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("###0"))));
        jTextField_SyncDaysInFuture.setText("60");
        jTextField_SyncDaysInFuture.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_SyncDaysInFutureFocusLost(evt);
            }
        });
        jLabel22.setText("Days in the Past");
        jLabel23.setText("Days in the Future");
        jLabel24.setForeground(new java.awt.Color(51, 51, 255));
        jLabel24.setText("Sync Schedule");
        jTextField_SyncMinOffsets.setToolTipText("The calendar name is case sensitive, i.e. \"my cal\" is different then \"My Cal\".");
        jTextField_SyncMinOffsets.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_SyncMinOffsetsFocusLost(evt);
            }
        });
        jCheckBox_SyncAtMinOffsets.setText("Sync at These Minute Offsets");
        jCheckBox_SyncAtMinOffsets.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAtMinOffsets.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncAtMinOffsets.setPreferredSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAtMinOffsets.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox_SyncAtMinOffsetsItemStateChanged(evt);
            }
        });
        jCheckBox_SyncAllSubjectsToValue.setText("For Privacy, Set All Subjects to");
        jCheckBox_SyncAllSubjectsToValue.setToolTipText("When checked, all subjects created in Google Calendar will be set to the specified value.");
        jCheckBox_SyncAllSubjectsToValue.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAllSubjectsToValue.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncAllSubjectsToValue.setPreferredSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncAllSubjectsToValue.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox_SyncAllSubjectsToValueItemStateChanged(evt);
            }
        });
        jCheckBox_SyncLocationAndRoom.setText("Location and Room");
        jCheckBox_SyncLocationAndRoom.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_SyncLocationAndRoom.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_SyncLocationAndRoom.setPreferredSize(new java.awt.Dimension(100, 23));
        jTextField_SyncAllSubjectsToThisValue.setToolTipText("The calendar name is case sensitive, i.e. \"my cal\" is different then \"My Cal\".");
        jTextField_SyncAllSubjectsToThisValue.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent evt) {
                jTextField_SyncAllSubjectsToThisValueFocusLost(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().add(28, 28, 28).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jLabel22, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE).add(jLabel23)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jTextField_SyncDaysInFuture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_SyncDaysInPast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 39, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().add(10, 10, 10).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jCheckBox_SyncLocationAndRoom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCheckBox_SyncDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCheckBox_SyncMeetingAttendees, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 274, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCheckBox_SyncAlarms, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 253, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel3Layout.createSequentialGroup().add(jCheckBox_SyncAllSubjectsToValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 188, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField_SyncAllSubjectsToThisValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 264, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))).add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 105, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel3Layout.createSequentialGroup().add(10, 10, 10).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jPanel3Layout.createSequentialGroup().add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jCheckBox_DiagnosticMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel20, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jTextField_DestinationCalendarName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 312, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel3Layout.createSequentialGroup().add(jCheckBox_SyncAtMinOffsets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jTextField_SyncMinOffsets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 270, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(jCheckBox_SyncOnStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 179, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))).addContainerGap(20, Short.MAX_VALUE)));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel3Layout.createSequentialGroup().addContainerGap().add(jLabel11, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jTextField_DestinationCalendarName, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel20)).add(3, 3, 3).add(jCheckBox_DiagnosticMode, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jLabel24, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox_SyncOnStart, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jTextField_SyncMinOffsets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCheckBox_SyncAtMinOffsets, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(9, 9, 9).add(jLabel21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(4, 4, 4).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel22).add(jTextField_SyncDaysInPast, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel23).add(jTextField_SyncDaysInFuture, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jLabel17, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox_SyncLocationAndRoom, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox_SyncDescription, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox_SyncMeetingAttendees, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jCheckBox_SyncAlarms, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jCheckBox_SyncAllSubjectsToValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_SyncAllSubjectsToThisValue, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(64, Short.MAX_VALUE)));
        jTabbedPane1.addTab("Sync Settings", jPanel3);
        jPanel1.setAutoscrolls(true);
        jCheckBox_GoogleSSL.setSelected(true);
        jCheckBox_GoogleSSL.setText("Connect to Google Using SSL/TLS (recommended)");
        jLabel4.setText("Password");
        jLabel3.setText("Email Address");
        jTextField_GoogleUsername.setText("user@google.com");
        jLabel5.setForeground(new java.awt.Color(51, 51, 255));
        jLabel5.setText("Google Settings");
        jLabel13.setText("Password");
        jLabel14.setText("Server");
        jLabel15.setText("Mail File");
        jLabel12.setText("Username");
        jTextField_LotusNotesServer.setToolTipText("");
        jLabel6.setForeground(new java.awt.Color(51, 51, 255));
        jLabel6.setText("Lotus Notes Settings");
        jLabel9.setForeground(new java.awt.Color(51, 51, 255));
        jLabel9.setText("Network Settings");
        jCheckBox_enableProxy.setText("Use Proxy Server to Reach the Internet");
        jCheckBox_enableProxy.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox_enableProxyStateChanged(evt);
            }
        });
        jLabel8.setText("Server IP or Name");
        jLabel8.setPreferredSize(new java.awt.Dimension(97, 14));
        jTextField_proxyIP.setEnabled(false);
        jLabel7.setText("Port Number");
        jLabel7.setPreferredSize(new java.awt.Dimension(97, 14));
        jTextField_proxyPort.setEnabled(false);
        jCheckBox_LotusNotesServerIsLocal.setText("Local Server");
        jCheckBox_LotusNotesServerIsLocal.setMaximumSize(new java.awt.Dimension(100, 23));
        jCheckBox_LotusNotesServerIsLocal.setMinimumSize(new java.awt.Dimension(40, 23));
        jCheckBox_LotusNotesServerIsLocal.setPreferredSize(new java.awt.Dimension(100, 23));
        jCheckBox_LotusNotesServerIsLocal.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBox_LotusNotesServerIsLocalItemStateChanged(evt);
            }
        });
        jLabel18.setText("Username (optional)");
        jTextField_proxyUsername.setEnabled(false);
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel19.setText("Password (optional)");
        jLabel19.setPreferredSize(new java.awt.Dimension(97, 14));
        jPasswordField_proxyPassword.setEnabled(false);
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 120, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1Layout.createSequentialGroup().add(10, 10, 10).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(jLabel15, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel12, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel14, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 69, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(70, 70, 70).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_LotusNotesUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, jPasswordField_LotusNotesPassword).add(org.jdesktop.layout.GroupLayout.LEADING, jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 388, Short.MAX_VALUE)).add(473, 473, 473))).add(jLabel13, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1Layout.createSequentialGroup().add(10, 10, 10).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 76, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1Layout.createSequentialGroup().add(70, 70, 70).add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 329, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))).add(jPanel1Layout.createSequentialGroup().add(80, 80, 80).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().add(jTextField_LotusNotesServer).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jCheckBox_LotusNotesServerIsLocal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 92, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jTextField_LotusNotesMailFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 388, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 117, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel1Layout.createSequentialGroup().add(10, 10, 10).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jCheckBox_enableProxy).add(jPanel1Layout.createSequentialGroup().add(21, 21, 21).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1Layout.createSequentialGroup().add(jLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPasswordField_proxyPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel18)).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jTextField_proxyUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 143, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 40, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(66, 66, 66))))).add(jCheckBox_GoogleSSL, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 453, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(839, Short.MAX_VALUE).add(jPasswordField_GooglePassword1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 104, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(8, 8, 8))));
        jPanel1Layout.linkSize(new java.awt.Component[] { jLabel12, jLabel13, jLabel14, jLabel15, jLabel3 }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        jPanel1Layout.linkSize(new java.awt.Component[] { jPasswordField_LotusNotesPassword, jTextField_LotusNotesMailFile }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        jPanel1Layout.linkSize(new java.awt.Component[] { jPasswordField_GooglePassword, jTextField_GoogleUsername }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jLabel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel14).add(jTextField_LotusNotesServer, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jCheckBox_LotusNotesServerIsLocal, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel15).add(jTextField_LotusNotesMailFile, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel12).add(jTextField_LotusNotesUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel13).add(jPasswordField_LotusNotesPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(jLabel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_GoogleUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPasswordField_GooglePassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(18, 18, 18).add(jLabel9, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jCheckBox_GoogleSSL).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(jCheckBox_enableProxy).add(1, 1, 1).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel8, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_proxyIP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jTextField_proxyPort, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel18).add(jTextField_proxyUsername, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel19, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPasswordField_proxyPassword, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(31, Short.MAX_VALUE)).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(213, 213, 213).add(jPasswordField_GooglePassword1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(214, Short.MAX_VALUE))));
        jTabbedPane1.addTab("Connection Settings", jPanel1);
        jLabel16.setForeground(new java.awt.Color(51, 51, 255));
        jLabel16.setText("This tool synchronizes your Lotus Notes calendar to your Google calendar.");
        jButton_Help.setMnemonic('H');
        jButton_Help.setText("Help");
        jButton_Help.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_HelpActionPerformed(evt);
            }
        });
        jButton_Minimize.setMnemonic('m');
        jButton_Minimize.setText("Minimize");
        jButton_Minimize.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_MinimizeActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(layout.createSequentialGroup().add(jTabbedPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 501, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()).add(jLabel16, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 416, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(layout.createSequentialGroup().add(10, 10, 10).add(jButton_Cancel).add(146, 146, 146).add(jButton_Minimize).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jButton_Help).add(23, 23, 23)))));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(jLabel16).add(15, 15, 15).add(jTabbedPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton_Cancel).add(jButton_Minimize).add(jButton_Help)).addContainerGap()));
        pack();
    }

    private void jButton_CancelActionPerformed(java.awt.event.ActionEvent evt) {
        formWindowClosed(null);
    }

    private void jButton_SynchronizeActionPerformed(java.awt.event.ActionEvent evt) {
        new SyncSwingWorker().execute();
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        this.setTitle(this.getTitle() + " v" + appVersion);
        if (!config.getApplicationVersion().equals(appVersion)) {
            showNewVersionMessage();
        }
        if (config.getSyncOnStartup() && jButton_Synchronize.isEnabled()) {
            new SyncSwingWorker().execute();
        }
    }

    private void formWindowClosed(java.awt.event.WindowEvent evt) {
        try {
            if (saveSettingsOnExit) saveSettings();
        } catch (Exception ex) {
            statusAppendException("There was an error saving settings.", ex);
            saveSettingsOnExit = false;
            return;
        }
        System.exit(EXIT_SUCCESS);
    }

    private void jCheckBox_enableProxyStateChanged(javax.swing.event.ChangeEvent evt) {
        jTextField_proxyIP.setEnabled(jCheckBox_enableProxy.isSelected());
        jTextField_proxyPort.setEnabled(jCheckBox_enableProxy.isSelected());
        jTextField_proxyUsername.setEnabled(jCheckBox_enableProxy.isSelected());
        jPasswordField_proxyPassword.setEnabled(jCheckBox_enableProxy.isSelected());
        validateSettings();
    }

    private void jTextField_DestinationCalendarNameFocusLost(java.awt.event.FocusEvent evt) {
        jTextField_DestinationCalendarName.setText(jTextField_DestinationCalendarName.getText().trim());
    }

    private void jButton_HelpActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(helpFilename));
        } catch (Exception ex) {
            statusAppendException("There was a problem opening the help file.", ex);
        }
    }

    private void jTextField_SyncDaysInPastFocusLost(java.awt.event.FocusEvent evt) {
        setDateRange();
    }

    private void jTextField_SyncDaysInFutureFocusLost(java.awt.event.FocusEvent evt) {
        setDateRange();
    }

    private void jTextField_SyncMinOffsetsFocusLost(java.awt.event.FocusEvent evt) {
        validateSettings();
    }

    private void jTextField_SyncAllSubjectsToThisValueFocusLost(java.awt.event.FocusEvent evt) {
    }

    private void jCheckBox_SyncAtMinOffsetsItemStateChanged(java.awt.event.ItemEvent evt) {
        jTextField_SyncMinOffsets.setEnabled(jCheckBox_SyncAtMinOffsets.isSelected());
        jLabel_NextSyncTime.setVisible(jCheckBox_SyncAtMinOffsets.isSelected());
        validateSettings();
        if (!jCheckBox_SyncAtMinOffsets.isSelected()) syncTimer.stop();
    }

    private void jCheckBox_SyncAllSubjectsToValueItemStateChanged(java.awt.event.ItemEvent evt) {
        jTextField_SyncAllSubjectsToThisValue.setEnabled(jCheckBox_SyncAllSubjectsToValue.isSelected());
    }

    private void jCheckBox_LotusNotesServerIsLocalItemStateChanged(java.awt.event.ItemEvent evt) {
        jTextField_LotusNotesServer.setEnabled(!jCheckBox_LotusNotesServerIsLocal.isSelected());
    }

    private void formWindowIconified(java.awt.event.WindowEvent evt) {
        setVisible(false);
    }

    private void jButton_MinimizeActionPerformed(java.awt.event.ActionEvent evt) {
        formWindowIconified(null);
    }

    private void validateSettings() {
        try {
            jButton_Synchronize.setEnabled(true);
            if (jCheckBox_enableProxy.isSelected()) {
                if (jTextField_proxyIP.getText().isEmpty()) {
                    throw new Exception("The Proxy Server IP/Name cannot be blank.");
                }
                if (jTextField_proxyPort.getText().isEmpty()) {
                    throw new Exception("The Proxy Port cannot be blank.");
                }
            }
            if (jCheckBox_SyncAtMinOffsets.isSelected()) {
                validateSyncMinOffsets();
            }
            statusClear();
            statusAppendLine("Configuration settings were successfully validated.");
        } catch (Exception ex) {
            statusClear();
            statusAppendLine("ERROR: A configuration setting is invalid. Syncing is disabled until the problem is resolved.");
            statusAppendLine(ex.getMessage());
            jButton_Synchronize.setEnabled(false);
        }
    }

    private void validateSyncMinOffsets() throws Exception {
        String[] syncOffsets = jTextField_SyncMinOffsets.getText().split(",");
        if (syncOffsets.length == 0 || (syncOffsets.length == 1 && syncOffsets[0].trim().isEmpty())) throw new Exception("The Sync Min Offsets list is empty.  Specify at least one value.");
        syncMinOffsetsList.clear();
        for (String strOffset : syncOffsets) {
            try {
                strOffset = strOffset.trim();
                int intOffset = Integer.parseInt(strOffset);
                if (intOffset >= 0 && intOffset <= 59) {
                    syncMinOffsetsList.add(intOffset);
                } else {
                    throw new Exception("In the Sync Min Offsets list, the offset value of '" + strOffset + "' is not between 0 and 59.");
                }
            } catch (NumberFormatException ex) {
                throw new Exception("In the Sync Min Offsets list, the offset value of '" + strOffset + "' is not a valid integer.");
            }
        }
        Collections.sort(syncMinOffsetsList);
        setSyncScheduleDelay();
    }

    @SuppressWarnings("static-access")
    private void saveSettings() throws Exception {
        config.setLotusNotesServer(jTextField_LotusNotesServer.getText());
        config.setLotusNotesServerIsLocal(jCheckBox_LotusNotesServerIsLocal.isSelected());
        config.setLotusNotesMailFile(jTextField_LotusNotesMailFile.getText());
        config.setLotusNotesUsername(jTextField_LotusNotesUsername.getText());
        config.setLotusNotesPassword(new String(jPasswordField_LotusNotesPassword.getPassword()));
        config.setGoogleUserName(jTextField_GoogleUsername.getText());
        config.setGooglePassword(new String(jPasswordField_GooglePassword.getPassword()));
        config.setGoogleEnableProxy(jCheckBox_enableProxy.isSelected());
        config.setGoogleProxyPort(jTextField_proxyPort.getText());
        config.setGoogleProxyIP(jTextField_proxyIP.getText());
        config.setGoogleProxyUsername(jTextField_proxyUsername.getText());
        config.setGoogleProxyPassword(new String(jPasswordField_proxyPassword.getPassword()));
        config.setGoogleCalendarName(jTextField_DestinationCalendarName.getText());
        config.setGoogleUseSSL(jCheckBox_GoogleSSL.isSelected());
        config.setSyncOnStartup(jCheckBox_SyncOnStart.isSelected());
        config.setSyncAtMinOffsets(jCheckBox_SyncAtMinOffsets.isSelected());
        config.setSyncMinOffsets(jTextField_SyncMinOffsets.getText());
        config.setSyncAllSubjectsToValue(jCheckBox_SyncAllSubjectsToValue.isSelected());
        config.setSyncAllSubjectsToThisValue(jTextField_SyncAllSubjectsToThisValue.getText());
        config.setDiagnosticMode(jCheckBox_DiagnosticMode.isSelected());
        config.setSyncDescription(jCheckBox_SyncDescription.isSelected());
        config.setSyncLocationAndRoom(jCheckBox_SyncLocationAndRoom.isSelected());
        config.setSyncAlarms(jCheckBox_SyncAlarms.isSelected());
        config.setSyncDaysInFuture(Integer.parseInt(jTextField_SyncDaysInFuture.getText()));
        config.setSyncDaysInPast(Integer.parseInt(jTextField_SyncDaysInPast.getText()));
        config.setSyncMeetingAttendees(jCheckBox_SyncMeetingAttendees.isSelected());
        config.writeConfig();
    }

    private void loadSettings() {
        try {
            jTextField_LotusNotesServer.setText(config.getLotusNotesServer());
            jCheckBox_LotusNotesServerIsLocal.setSelected(config.getLotusNotesServerIsLocal());
            jCheckBox_LotusNotesServerIsLocalItemStateChanged(null);
            jTextField_LotusNotesMailFile.setText(config.getLotusNotesMailFile());
            jTextField_LotusNotesUsername.setText(config.getLotusNotesUsername());
            String s = config.getLotusNotesPassword();
            jPasswordField_LotusNotesPassword.setText(config.getLotusNotesPassword());
            jTextField_GoogleUsername.setText(config.getGoogleUserName());
            jPasswordField_GooglePassword.setText(config.getGooglePassword());
            jCheckBox_GoogleSSL.setSelected(config.getGoogleUseSSL());
            jCheckBox_enableProxy.setSelected(config.getGoogleEnableProxy());
            jTextField_proxyIP.setText(config.getGoogleProxyIP());
            jTextField_proxyPort.setText(config.getGoogleProxyPort());
            jTextField_proxyUsername.setText(config.getGoogleProxyUsername());
            jPasswordField_proxyPassword.setText(config.getGoogleProxyPassword());
            jTextField_DestinationCalendarName.setText(config.getGoogleCalendarName());
            jCheckBox_SyncOnStart.setSelected(config.getSyncOnStartup());
            jTextField_SyncMinOffsets.setText(config.getSyncMinOffsets());
            jCheckBox_SyncAtMinOffsetsItemStateChanged(null);
            jCheckBox_SyncAtMinOffsets.setSelected(config.getSyncAtMinOffsets());
            jCheckBox_SyncAllSubjectsToValue.setSelected(config.getSyncAllSubjectsToValue());
            jCheckBox_SyncAllSubjectsToValueItemStateChanged(null);
            jTextField_SyncAllSubjectsToThisValue.setText(config.getSyncAllSubjectsToThisValue());
            jCheckBox_DiagnosticMode.setSelected(config.getDiagnosticMode());
            jCheckBox_SyncDescription.setSelected(config.getSyncDescription());
            jCheckBox_SyncLocationAndRoom.setSelected(config.getSyncLocationAndRoom());
            jCheckBox_SyncAlarms.setSelected(config.getSyncAlarms());
            jTextField_SyncDaysInFuture.setText(Integer.toString(config.getSyncDaysInFuture()));
            jTextField_SyncDaysInPast.setText(Integer.toString(config.getSyncDaysInPast()));
            jCheckBox_SyncMeetingAttendees.setSelected(config.getSyncMeetingAttendees());
            proxy.setProxyHost(config.getGoogleProxyIP());
            proxy.setProxyPort(config.getGoogleProxyPort());
            proxy.setEnabled(config.getGoogleEnableProxy());
        } catch (Exception ex) {
            statusAppendException("Unable to read settings from the config file.", ex);
        }
    }

    protected void setDateRange() {
        Calendar now = Calendar.getInstance();
        syncDaysInPast = 0;
        if (!jTextField_SyncDaysInPast.getText().isEmpty()) syncDaysInPast = Integer.parseInt(jTextField_SyncDaysInPast.getText());
        now.add(Calendar.DATE, syncDaysInPast * -1);
        now.set(Calendar.HOUR_OF_DAY, 0);
        now.set(Calendar.MINUTE, 0);
        now.set(Calendar.SECOND, 0);
        minStartDate = now.getTime();
        now = Calendar.getInstance();
        syncDaysInFuture = 0;
        if (!jTextField_SyncDaysInFuture.getText().isEmpty()) syncDaysInFuture = Integer.parseInt(jTextField_SyncDaysInFuture.getText());
        now.add(Calendar.DATE, syncDaysInFuture);
        now.set(Calendar.HOUR_OF_DAY, 23);
        now.set(Calendar.MINUTE, 59);
        now.set(Calendar.SECOND, 59);
        maxEndDate = now.getTime();
    }

    /**
     * Clears the status text area.
     */
    protected void statusClear() {
        if (!isSilentMode) {
            jTextArea_Status.setText("");
        }
    }

    /**
     * Adds a line to the status area.
     * @param text - The text to add.
     */
    @Override
    public void statusAppendLine(String text) {
        if (isSilentMode) System.out.println(text); else {
            jTextArea_Status.append(text + "\n");
            jTextArea_Status.setCaretPosition(jTextArea_Status.getDocument().getLength());
        }
    }

    /**
     * Adds a line to the status area in diagnostic format.
     * @param text - The text to add.
     */
    @Override
    public void statusAppendLineDiag(String text) {
        if (jCheckBox_DiagnosticMode.isSelected()) statusAppendLine("   " + text);
    }

    /**
     * Adds text to the status area (without inserting a newline).
     * @param text - The text to add.
     */
    @Override
    public void statusAppend(String text) {
        if (isSilentMode) System.out.print(text); else {
            jTextArea_Status.append(text);
            jTextArea_Status.setCaretPosition(jTextArea_Status.getDocument().getLength());
        }
    }

    /**
     * Adds a line to the status area and starts a timer.
     * @param text - The text to add.
     */
    @Override
    public void statusAppendStart(String text) {
        statusStartTime = System.currentTimeMillis();
        if (jCheckBox_DiagnosticMode.isSelected()) {
            statusStartMsg = text;
            statusAppendLine(text);
        } else statusAppend(text);
    }

    /**
     * Writes the elapsed time (started with statusAppendStart()) to the status area.
     */
    @Override
    public void statusAppendFinished() {
        long elapsedMillis = System.currentTimeMillis() - statusStartTime;
        BigDecimal elapsedSecs = new BigDecimal(elapsedMillis / 1000.0).setScale(1, BigDecimal.ROUND_HALF_UP);
        if (jCheckBox_DiagnosticMode.isSelected()) statusAppendLine(statusStartMsg + " (done in " + elapsedSecs.toString() + " s)"); else statusAppendLine(" (" + elapsedSecs.toString() + " s)");
    }

    /**
     * Adds a line to the status area followed by the stack trace of an exception.
     * @param text - The text to add.
     * @param ex - Exception to display the stack trace of.
     */
    @Override
    public void statusAppendException(String text, Exception ex) {
        statusAppendLine("\n\n=== ERROR ===");
        statusAppendLine(text);
        StringWriter sw = new StringWriter();
        ex.printStackTrace(new PrintWriter(sw));
        statusAppendLine(sw.toString());
    }

    final String iconAppPath = "/images/lngs-icon.png";

    ImageIcon iconApp;

    ProxyManager proxy;

    ConfigurationManager config;

    private boolean isUrlValid = false;

    long statusStartTime = 0;

    String statusStartMsg;

    final String appVersion = "2.1";

    private boolean isSilentMode = false;

    private boolean saveSettingsOnExit = true;

    private String helpFilename = "(unknown)";

    Date minStartDate = null;

    Date maxEndDate = null;

    int syncDaysInPast = 0;

    int syncDaysInFuture = 0;

    static final int EXIT_SUCCESS = 0;

    static final int EXIT_INVALID_PARM = 1;

    static final int EXIT_EXCEPTION = 2;

    static final int EXIT_MISSING_RESOURCE = 3;

    static int exitCode = EXIT_SUCCESS;

    private javax.swing.JButton jButton_Cancel;

    private javax.swing.JButton jButton_Help;

    private javax.swing.JButton jButton_Minimize;

    private javax.swing.JButton jButton_Synchronize;

    private javax.swing.JCheckBox jCheckBox_DiagnosticMode;

    private javax.swing.JCheckBox jCheckBox_GoogleSSL;

    private javax.swing.JCheckBox jCheckBox_LotusNotesServerIsLocal;

    private javax.swing.JCheckBox jCheckBox_SyncAlarms;

    private javax.swing.JCheckBox jCheckBox_SyncAllSubjectsToValue;

    private javax.swing.JCheckBox jCheckBox_SyncAtMinOffsets;

    private javax.swing.JCheckBox jCheckBox_SyncDescription;

    private javax.swing.JCheckBox jCheckBox_SyncLocationAndRoom;

    private javax.swing.JCheckBox jCheckBox_SyncMeetingAttendees;

    private javax.swing.JCheckBox jCheckBox_SyncOnStart;

    private javax.swing.JCheckBox jCheckBox_enableProxy;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel19;

    private javax.swing.JLabel jLabel20;

    private javax.swing.JLabel jLabel21;

    private javax.swing.JLabel jLabel22;

    private javax.swing.JLabel jLabel23;

    private javax.swing.JLabel jLabel24;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JLabel jLabel_NextSyncTime;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPasswordField jPasswordField_GooglePassword;

    private javax.swing.JPasswordField jPasswordField_GooglePassword1;

    private javax.swing.JPasswordField jPasswordField_LotusNotesPassword;

    private javax.swing.JPasswordField jPasswordField_proxyPassword;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTabbedPane jTabbedPane1;

    private javax.swing.JTextArea jTextArea_Status;

    private javax.swing.JTextField jTextField_DestinationCalendarName;

    private javax.swing.JTextField jTextField_GoogleUsername;

    private javax.swing.JTextField jTextField_LotusNotesMailFile;

    private javax.swing.JTextField jTextField_LotusNotesServer;

    private javax.swing.JTextField jTextField_LotusNotesUsername;

    private javax.swing.JTextField jTextField_SyncAllSubjectsToThisValue;

    private javax.swing.JFormattedTextField jTextField_SyncDaysInFuture;

    private javax.swing.JFormattedTextField jTextField_SyncDaysInPast;

    private javax.swing.JTextField jTextField_SyncMinOffsets;

    private javax.swing.JTextField jTextField_proxyIP;

    private javax.swing.JTextField jTextField_proxyPort;

    private javax.swing.JTextField jTextField_proxyUsername;
}
