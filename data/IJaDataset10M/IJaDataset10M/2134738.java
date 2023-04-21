package systeminformationmonitor;

import java.awt.Rectangle;
import java.awt.CardLayout;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.DefaultSingleSelectionModel;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;
import javax.swing.table.TableRowSorter;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.skin.SubstanceGeminiLookAndFeel;
import org.jvnet.substance.api.skin.SubstanceGraphiteAquaLookAndFeel;
import org.jvnet.substance.api.skin.SubstanceMagellanLookAndFeel;
import org.jvnet.substance.skin.SubstanceAutumnLookAndFeel;
import org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel;
import org.jvnet.substance.skin.SubstanceBusinessLookAndFeel;
import org.jvnet.substance.skin.SubstanceChallengerDeepLookAndFeel;
import org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel;
import org.jvnet.substance.skin.SubstanceCremeLookAndFeel;
import org.jvnet.substance.skin.SubstanceDustCoffeeLookAndFeel;
import org.jvnet.substance.skin.SubstanceDustLookAndFeel;
import org.jvnet.substance.skin.SubstanceEmeraldDuskLookAndFeel;
import org.jvnet.substance.skin.SubstanceMagmaLookAndFeel;
import org.jvnet.substance.skin.SubstanceMistAquaLookAndFeel;
import org.jvnet.substance.skin.SubstanceMistSilverLookAndFeel;
import org.jvnet.substance.skin.SubstanceModerateLookAndFeel;
import org.jvnet.substance.skin.SubstanceNebulaBrickWallLookAndFeel;
import org.jvnet.substance.skin.SubstanceNebulaLookAndFeel;
import org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel;
import org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel;
import org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel;
import org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel;
import org.jvnet.substance.skin.SubstanceRavenLookAndFeel;
import org.jvnet.substance.skin.SubstanceSaharaLookAndFeel;
import org.jvnet.substance.skin.SubstanceTwilightLookAndFeel;
import systeminformationmonitor.swing.HelpDialog;
import systeminformationmonitor.swing.JavaEnvDialog;
import systeminformationmonitor.swing.ShapeCellRenderer;
import systeminformationmonitor.swing.SystemEnvDialog;
import systeminformationmonitor.swing.charts.JChartFactory;
import systeminformationmonitor.swing.model.ConnectionTableModel;
import systeminformationmonitor.swing.model.IOTableModel;
import systeminformationmonitor.swing.model.NetInfoComboBoxModel;
import systeminformationmonitor.swing.model.ProcessTableModel;
import systeminformationmonitor.swing.util.CalcDialogCenter;
import systeminformationmonitor.swing.util.CalcDialogCenter.XYCoord;
import systeminformationmonitor.system.*;
import systeminformationmonitor.system.object.JavaInformationObject;
import systeminformationmonitor.system.object.NetInfoObject;
import systeminformationmonitor.system.object.NetInterfaceObject;

/**
 * The application's main frame.
 */
public class SystemInformationMonitorView extends FrameView {

    public SystemInformationMonitorView(SingleFrameApplication app) {
        super(app);
        initComponents();
        ResourceMap resourceMap = getResourceMap();
        getFrame().setIconImage(resourceMap.getImageIcon("ProgramIcon").getImage());
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            @Override
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
        updateUITask = new GlobalMonitorUpdater(NORMAL_SPEED);
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = SystemInformationMonitorApp.getApplication().getMainFrame();
            aboutBox = new SystemInformationMonitorAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        SystemInformationMonitorApp.getApplication().show(aboutBox);
    }

    @Action
    public void setAlwaysOnTop() {
        JFrame myFrame = getFrame();
        myFrame.setAlwaysOnTop(alwaysOnTopCheckBoxMenuItem.isSelected());
    }

    /**
     * Change the update speed to high
     */
    @Action
    public void changeSpeedHigh() {
        SystemInformationGather.getInstance().setTime(HIGH_SPEED);
    }

    /**
     * Change the update speed to normal
     */
    @Action
    public void changeSpeedNormal() {
        SystemInformationGather.getInstance().setTime(NORMAL_SPEED);
    }

    /**
     * Change the update speed to slow
     */
    @Action
    public void changeSpeedLow() {
        SystemInformationGather.getInstance().setTime(LOW_SPEED);
    }

    @Action
    public void changeSpeedPause() {
        SystemInformationGather.getInstance().setTime(-1);
    }

    @Action
    public Task changeSkin() {
        return new ChangeSkinTask(getApplication());
    }

    /**
     * @return the tcpJPanel
     */
    public javax.swing.JPanel getTcpJPanel() {
        return tcpJPanel;
    }

    /**
     * @return the connectionJPanel
     */
    public javax.swing.JPanel getConnectionJPanel() {
        return connectionJPanel;
    }

    private class ChangeSkinTask extends org.jdesktop.application.Task<Object, Void> {

        ChangeSkinTask(org.jdesktop.application.Application app) {
            super(app);
            JFrame.setDefaultLookAndFeelDecorated(true);
        }

        @Override
        protected Object doInBackground() {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            if (businessSkinMenuRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceBusinessLookAndFeel());
                            } else if (businessBlueSteelRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
                            } else if (businessBlackSteelSkinRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
                            } else if (cremeRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceCremeLookAndFeel());
                            } else if (cremeCoffeeRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceCremeCoffeeLookAndFeel());
                            } else if (saharaRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceSaharaLookAndFeel());
                            } else if (moderateRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceModerateLookAndFeel());
                            } else if (officeSilver2007RadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceOfficeSilver2007LookAndFeel());
                            } else if (officeBlue2007RadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceOfficeBlue2007LookAndFeel());
                            } else if (nebulaRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceNebulaLookAndFeel());
                            } else if (nebulaBrickWallRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceNebulaBrickWallLookAndFeel());
                            } else if (autumnRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceAutumnLookAndFeel());
                            } else if (mistSilverRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceMistSilverLookAndFeel());
                            } else if (mistAquaRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceMistAquaLookAndFeel());
                            } else if (dustRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceDustLookAndFeel());
                            } else if (dustCoffeeRadioButtonMenuItem1.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceDustCoffeeLookAndFeel());
                            } else if (geminiRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceGeminiLookAndFeel());
                            }
                            if (twilightRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceTwilightLookAndFeel());
                            } else if (magellanRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceMagellanLookAndFeel());
                            } else if (ravenGraphiteRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceRavenGraphiteLookAndFeel());
                            } else if (ravenGraphiteGlassRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceRavenGraphiteGlassLookAndFeel());
                            } else if (graphiteAquaRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceGraphiteAquaLookAndFeel());
                            } else if (ravenRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceRavenLookAndFeel());
                            } else if (magmaRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceMagmaLookAndFeel());
                            } else if (challengerDeepRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceChallengerDeepLookAndFeel());
                            } else if (emeraldDuskRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceEmeraldDuskLookAndFeel());
                            }
                            if (systemDefaultSkinRadioButtonMenuItem.isSelected()) {
                                UIManager.setLookAndFeel(new SubstanceBusinessBlueSteelLookAndFeel());
                            }
                            getFrame().setVisible(false);
                            SwingUtilities.updateComponentTreeUI(getComponent());
                            SwingUtilities.updateComponentTreeUI(statusPanel);
                            getFrame().setVisible(true);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemInformationMonitorView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(SystemInformationMonitorView.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        protected void succeeded(Object result) {
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        mainPanel = new javax.swing.JPanel();
        systemInfoMonitorTabbedPane = new javax.swing.JTabbedPane();
        processesPanel = new javax.swing.JPanel();
        processTableScrollPane = new javax.swing.JScrollPane();
        processTable = new javax.swing.JTable();
        networkAdapterPanel = new javax.swing.JPanel();
        networkAdapterLabel = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        adaperNameLabel = new javax.swing.JLabel();
        adapterNameTextField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        adapterIPAddressTextField = new javax.swing.JTextField();
        adapterMACAddressTextField = new javax.swing.JTextField();
        adapterHostNameTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        adapterVirtualCheckBox = new javax.swing.JCheckBox();
        adapterEnabledCheckBox = new javax.swing.JCheckBox();
        adapterLoopbackCheckBox = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        networkGateway = new javax.swing.JTextField();
        networkDomainName = new javax.swing.JTextField();
        networkPrimaryDNS = new javax.swing.JTextField();
        networkSecondaryDNS = new javax.swing.JTextField();
        cpuPanel = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        AllCpujPanel = JChartFactory.createAllCpuChart();
        MultipleCpujPanel = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        CPUStaticInformationPanel = new javax.swing.JPanel();
        CpuModeljLabel = new javax.swing.JLabel();
        CpuModeljTextField = new javax.swing.JTextField();
        CpuSocketsjLabel = new javax.swing.JLabel();
        CpuSocketsjTextField = new javax.swing.JTextField();
        CpuCoresPerSocketjLabel = new javax.swing.JLabel();
        CpuCoresPerSocketjTextField = new javax.swing.JTextField();
        CpuVendorjLabel = new javax.swing.JLabel();
        CpuVendorjTextField = new javax.swing.JTextField();
        CpuCachejLabel = new javax.swing.JLabel();
        CpuCachejTextField = new javax.swing.JTextField();
        CpuMhzjLabel = new javax.swing.JLabel();
        CpuMhzjTextField = new javax.swing.JTextField();
        CPULiveInformationPanel = new systeminformationmonitor.CpuLiveInformation();
        systemPanel = new javax.swing.JPanel();
        OSPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        OSDescriptionTextField = new javax.swing.JTextField();
        OSNameTextField = new javax.swing.JTextField();
        OSArchitectureTextField = new javax.swing.JTextField();
        OSVendorVersionTextField = new javax.swing.JTextField();
        OSVendorTextField = new javax.swing.JTextField();
        OSDataModelTextField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        OSCPUEndianTextField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        totalMemoryProgressBar = new javax.swing.JProgressBar();
        usedMemoryProgressBar = new javax.swing.JProgressBar();
        freeMemoryProgressBar = new javax.swing.JProgressBar();
        memoryPieChartPanel = JChartFactory.createMemoryPieChart();
        jPanel5 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        systemUptimeTextField = new javax.swing.JTextField();
        ioPanel = new javax.swing.JPanel();
        jSplitPane2 = new javax.swing.JSplitPane();
        IOInformationPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        IOChart = new javax.swing.JPanel();
        jSplitPane3 = new javax.swing.JSplitPane();
        ioChartsPanel = new javax.swing.JPanel();
        ioWritesChartPanel = new javax.swing.JPanel();
        writesChartPanel = JChartFactory.createIOWriteChart();
        readAccessChartPanel = new javax.swing.JPanel();
        readsChartPanel = JChartFactory.createIOReadChart();
        hardDriveSpaceChartPanel = JChartFactory.createIOBarChart();
        networkConnectionPanel = new javax.swing.JPanel();
        jSplitPane4 = new javax.swing.JSplitPane();
        ConnectionInformationGraphPanel = new javax.swing.JPanel();
        tcpJPanel = JChartFactory.createSegmentsChart();
        connectionJPanel = JChartFactory.createConnectionsChart();
        TCPConnectionPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        TCPConnectionTable = new javax.swing.JTable();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        optionMenu = new javax.swing.JMenu();
        alwaysOnTopCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        viewMenu = new javax.swing.JMenu();
        updateSpeedMenu = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem4 = new javax.swing.JRadioButtonMenuItem();
        cpuJMenu = new javax.swing.JMenu();
        CPUSingleGraphButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        CPUMultiGraphRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        skinMenu = new javax.swing.JMenu();
        lightSkinjMenu = new javax.swing.JMenu();
        businessSkinMenuRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        businessBlueSteelRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        businessBlackSteelSkinRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        cremeRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        cremeCoffeeRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        saharaRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        moderateRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        officeSilver2007RadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        officeBlue2007RadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        nebulaRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        nebulaBrickWallRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        autumnRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        mistSilverRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        mistAquaRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        dustRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        dustCoffeeRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        geminiRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        darkSkinJMenu = new javax.swing.JMenu();
        twilightRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        magellanRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        ravenGraphiteRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        ravenGraphiteGlassRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        graphiteAquaRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        ravenRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        magmaRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        challengerDeepRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        emeraldDuskRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        systemDefaultSkinRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        refreshNowjMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        helpMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        skinButtonGroup = new javax.swing.ButtonGroup();
        speedSettingbuttonGroup = new javax.swing.ButtonGroup();
        CPUGraphButtonGroup = new javax.swing.ButtonGroup();
        mainPanel.setFocusable(false);
        mainPanel.setName("mainPanel");
        mainPanel.setLayout(new java.awt.BorderLayout());
        systemInfoMonitorTabbedPane.setModel(new DefaultSingleSelectionModel());
        systemInfoMonitorTabbedPane.setDoubleBuffered(true);
        systemInfoMonitorTabbedPane.setFocusable(false);
        systemInfoMonitorTabbedPane.setName("systemInfoMonitorTabbedPane");
        processesPanel.setName("processesPanel");
        processTableScrollPane.setName("processTableScrollPane");
        processesTableModel = systeminformationmonitor.swing.model.ProcessTableModel.getInstance();
        processTable.setModel(processesTableModel);
        processTable.setRowSorter(new TableRowSorter<ProcessTableModel>(processesTableModel));
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(systeminformationmonitor.SystemInformationMonitorApp.class).getContext().getResourceMap(SystemInformationMonitorView.class);
        processTable.setToolTipText(resourceMap.getString("processTable.toolTipText"));
        processTable.setDoubleBuffered(true);
        processTable.setName("processTable");
        processTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        processTableScrollPane.setViewportView(processTable);
        javax.swing.GroupLayout processesPanelLayout = new javax.swing.GroupLayout(processesPanel);
        processesPanel.setLayout(processesPanelLayout);
        processesPanelLayout.setHorizontalGroup(processesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(processesPanelLayout.createSequentialGroup().addContainerGap().addComponent(processTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 613, Short.MAX_VALUE).addContainerGap()));
        processesPanelLayout.setVerticalGroup(processesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(processesPanelLayout.createSequentialGroup().addContainerGap().addComponent(processTableScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 461, Short.MAX_VALUE).addContainerGap()));
        systemInfoMonitorTabbedPane.addTab(resourceMap.getString("processesPanel.TabConstraints.tabTitle"), resourceMap.getIcon("processesPanel.TabConstraints.tabIcon"), processesPanel, resourceMap.getString("processesPanel.TabConstraints.tabToolTip"));
        networkAdapterPanel.setName("networkAdapterPanel");
        networkAdapterLabel.setText(resourceMap.getString("networkAdapterLabel.text"));
        networkAdapterLabel.setName("networkAdapterLabel");
        netInfoComboModel = NetInfoComboBoxModel.getInstance();
        jComboBox1.setModel(netInfoComboModel);
        jComboBox1.setName("jComboBox1");
        if (jComboBox1.getModel().getSize() > 0) {
            jComboBox1.setSelectedIndex(0);
        }
        jComboBox1.addActionListener(new NetworkInterfaceComboBoxActionListener());
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel3.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel3.setName("jPanel3");
        adaperNameLabel.setText(resourceMap.getString("adaperNameLabel.text"));
        adaperNameLabel.setName("adaperNameLabel");
        adapterNameTextField.setEditable(false);
        adapterNameTextField.setName("adapterNameTextField");
        jLabel2.setText(resourceMap.getString("jLabel2.text"));
        jLabel2.setName("jLabel2");
        jLabel3.setText(resourceMap.getString("jLabel3.text"));
        jLabel3.setName("jLabel3");
        jLabel4.setText(resourceMap.getString("jLabel4.text"));
        jLabel4.setName("jLabel4");
        adapterIPAddressTextField.setEditable(false);
        adapterIPAddressTextField.setText(resourceMap.getString("adapterIPAddressTextField.text"));
        adapterIPAddressTextField.setName("adapterIPAddressTextField");
        adapterMACAddressTextField.setEditable(false);
        adapterMACAddressTextField.setText(resourceMap.getString("adapterMACAddressTextField.text"));
        adapterMACAddressTextField.setName("adapterMACAddressTextField");
        adapterHostNameTextField.setEditable(false);
        adapterHostNameTextField.setText(resourceMap.getString("adapterHostNameTextField.text"));
        adapterHostNameTextField.setName("adapterHostNameTextField");
        jLabel5.setText(resourceMap.getString("jLabel5.text"));
        jLabel5.setName("jLabel5");
        adapterVirtualCheckBox.setText(resourceMap.getString("adapterVirtualCheckBox.text"));
        adapterVirtualCheckBox.setEnabled(false);
        adapterVirtualCheckBox.setName("adapterVirtualCheckBox");
        adapterEnabledCheckBox.setText(resourceMap.getString("adapterEnabledCheckBox.text"));
        adapterEnabledCheckBox.setEnabled(false);
        adapterEnabledCheckBox.setName("adapterEnabledCheckBox");
        adapterLoopbackCheckBox.setText(resourceMap.getString("adapterLoopbackCheckBox.text"));
        adapterLoopbackCheckBox.setEnabled(false);
        adapterLoopbackCheckBox.setName("adapterLoopbackCheckBox");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(adaperNameLabel).addComponent(jLabel2).addComponent(jLabel3).addComponent(jLabel4).addComponent(jLabel5)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(adapterLoopbackCheckBox).addComponent(adapterEnabledCheckBox).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(adapterVirtualCheckBox).addComponent(adapterHostNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE).addComponent(adapterMACAddressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE).addComponent(adapterIPAddressTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE).addComponent(adapterNameTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE))).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(adaperNameLabel).addGap(12, 12, 12).addComponent(jLabel2).addGap(12, 12, 12).addComponent(jLabel3).addGap(12, 12, 12).addComponent(jLabel4).addGap(12, 12, 12).addComponent(jLabel5)).addGroup(jPanel3Layout.createSequentialGroup().addComponent(adapterNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(adapterIPAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(adapterMACAddressTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(adapterHostNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(adapterVirtualCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(adapterEnabledCheckBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(adapterLoopbackCheckBox))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel4.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel4.setName("jPanel4");
        jLabel6.setText(resourceMap.getString("jLabel6.text"));
        jLabel6.setName("jLabel6");
        jLabel7.setText(resourceMap.getString("jLabel7.text"));
        jLabel7.setName("jLabel7");
        jLabel8.setText(resourceMap.getString("jLabel8.text"));
        jLabel8.setName("jLabel8");
        jLabel9.setText(resourceMap.getString("jLabel9.text"));
        jLabel9.setName("jLabel9");
        networkGateway.setEditable(false);
        networkGateway.setText(resourceMap.getString("networkGateway.text"));
        networkGateway.setName("networkGateway");
        networkDomainName.setEditable(false);
        networkDomainName.setText(resourceMap.getString("networkDomainName.text"));
        networkDomainName.setName("networkDomainName");
        networkPrimaryDNS.setEditable(false);
        networkPrimaryDNS.setText(resourceMap.getString("networkPrimaryDNS.text"));
        networkPrimaryDNS.setName("networkPrimaryDNS");
        networkSecondaryDNS.setEditable(false);
        networkSecondaryDNS.setText(resourceMap.getString("networkSecondaryDNS.text"));
        networkSecondaryDNS.setName("networkSecondaryDNS");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel7).addComponent(jLabel8).addComponent(jLabel9)).addGap(27, 27, 27).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(networkDomainName, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addComponent(networkGateway, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addComponent(networkPrimaryDNS, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addComponent(networkSecondaryDNS, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)).addContainerGap()));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(networkGateway, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel6)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel7).addComponent(networkDomainName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8).addComponent(networkPrimaryDNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addComponent(networkSecondaryDNS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(78, Short.MAX_VALUE)));
        javax.swing.GroupLayout networkAdapterPanelLayout = new javax.swing.GroupLayout(networkAdapterPanel);
        networkAdapterPanel.setLayout(networkAdapterPanelLayout);
        networkAdapterPanelLayout.setHorizontalGroup(networkAdapterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(networkAdapterPanelLayout.createSequentialGroup().addContainerGap().addGroup(networkAdapterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(networkAdapterPanelLayout.createSequentialGroup().addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(networkAdapterPanelLayout.createSequentialGroup().addComponent(networkAdapterLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox1, 0, 523, Short.MAX_VALUE))).addContainerGap()));
        networkAdapterPanelLayout.setVerticalGroup(networkAdapterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(networkAdapterPanelLayout.createSequentialGroup().addContainerGap().addGroup(networkAdapterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(networkAdapterLabel).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(networkAdapterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(243, Short.MAX_VALUE)));
        systemInfoMonitorTabbedPane.addTab(resourceMap.getString("networkAdapterPanel.TabConstraints.tabTitle"), resourceMap.getIcon("networkAdapterPanel.TabConstraints.tabIcon"), networkAdapterPanel, resourceMap.getString("networkAdapterPanel.TabConstraints.tabToolTip"));
        cpuPanel.setFocusable(false);
        cpuPanel.setName("jPanel3");
        cpuPanel.setLayout(new java.awt.BorderLayout());
        jPanel8.setFocusable(false);
        jPanel8.setMinimumSize(new java.awt.Dimension(633, 130));
        jPanel8.setName("jPanel8");
        jPanel8.setPreferredSize(new java.awt.Dimension(633, 130));
        jPanel8.setLayout(new java.awt.CardLayout());
        AllCpujPanel.setFocusable(false);
        AllCpujPanel.setName("AllCpujPanel");
        AllCpujPanel.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                flipCpuGraph(evt);
            }
        });
        AllCpujPanel.setLayout(new java.awt.BorderLayout());
        jPanel8.add(AllCpujPanel, "Multiple");
        MultipleCpujPanel.setFocusable(false);
        MultipleCpujPanel.setName("MultipleCpujPanel");
        MultipleCpujPanel.setPreferredSize(new java.awt.Dimension(633, 130));
        MultipleCpujPanel.setLayout(new java.awt.GridLayout(1, 2, 4, 0));
        jPanel8.add(MultipleCpujPanel, "Single");
        for (int i = 0; i < CpuInformation.getCpuCores() * CpuInformation.getCpuCoresPerSocket(); i++) {
            JPanel tempGraphPanel = JChartFactory.createCpuChart(i);
            tempGraphPanel.setPreferredSize(new java.awt.Dimension(100, 130));
            tempGraphPanel.addMouseListener(new java.awt.event.MouseAdapter() {

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    flipCpuGraph(evt);
                }
            });
            tempGraphPanel.setLayout(new java.awt.BorderLayout());
            MultipleCpujPanel.add(tempGraphPanel);
        }
        cpuPanel.add(jPanel8, java.awt.BorderLayout.CENTER);
        jPanel7.setName("jPanel7");
        jPanel7.setPreferredSize(new java.awt.Dimension(663, 320));
        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setDividerSize(3);
        jSplitPane1.setResizeWeight(0.25);
        jSplitPane1.setName("jSplitPane1");
        CPUStaticInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("CPUStaticInformationPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        CPUStaticInformationPanel.setMinimumSize(new java.awt.Dimension(300, 37));
        CPUStaticInformationPanel.setName("CPUStaticInformationPanel");
        CPUStaticInformationPanel.setPreferredSize(new java.awt.Dimension(200, 240));
        CpuModeljLabel.setText(resourceMap.getString("CpuModeljLabel.text"));
        CpuModeljLabel.setName("CpuModeljLabel");
        CpuModeljTextField.setEditable(false);
        CpuModeljTextField.setText(CpuInformation.getCpuModel());
        CpuModeljTextField.setName("CpuModeljTextField");
        CpuSocketsjLabel.setText(resourceMap.getString("CpuSocketsjLabel.text"));
        CpuSocketsjLabel.setName("CpuSocketsjLabel");
        CpuSocketsjTextField.setEditable(false);
        CpuSocketsjTextField.setText(new Integer(CpuInformation.getCpuCores()).toString());
        CpuSocketsjTextField.setName("CpuSocketsjTextField");
        CpuCoresPerSocketjLabel.setText(resourceMap.getString("CpuCoresPerSocketjLabel.text"));
        CpuCoresPerSocketjLabel.setName("CpuCoresPerSocketjLabel");
        CpuCoresPerSocketjTextField.setEditable(false);
        CpuCoresPerSocketjTextField.setText(new Integer(CpuInformation.getCpuCoresPerSocket()).toString());
        CpuCoresPerSocketjTextField.setName("CpuCoresPerSocketjTextField");
        CpuVendorjLabel.setText(resourceMap.getString("CpuVendorjLabel.text"));
        CpuVendorjLabel.setName("CpuVendorjLabel");
        CpuVendorjTextField.setEditable(false);
        CpuVendorjTextField.setText(CpuInformation.getCpuVendor());
        CpuVendorjTextField.setName("CpuVendorjTextField");
        CpuCachejLabel.setText(resourceMap.getString("CpuCachejLabel.text"));
        CpuCachejLabel.setName("CpuCachejLabel");
        CpuCachejTextField.setEditable(false);
        CpuCachejTextField.setText(CpuInformation.getCpuCache());
        CpuCachejTextField.setName("CpuCachejTextField");
        CpuMhzjLabel.setText(resourceMap.getString("CpuMhzjLabel.text"));
        CpuMhzjLabel.setName("CpuMhzjLabel");
        CpuMhzjTextField.setEditable(false);
        CpuMhzjTextField.setText(CpuInformation.getCpuMhz());
        CpuMhzjTextField.setName("CpuMhzjTextField");
        javax.swing.GroupLayout CPUStaticInformationPanelLayout = new javax.swing.GroupLayout(CPUStaticInformationPanel);
        CPUStaticInformationPanel.setLayout(CPUStaticInformationPanelLayout);
        CPUStaticInformationPanelLayout.setHorizontalGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(CPUStaticInformationPanelLayout.createSequentialGroup().addContainerGap().addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(CpuVendorjLabel).addGroup(CPUStaticInformationPanelLayout.createSequentialGroup().addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(CpuCoresPerSocketjLabel).addComponent(CpuModeljLabel).addComponent(CpuSocketsjLabel).addComponent(CpuCachejLabel).addComponent(CpuMhzjLabel)).addGap(14, 14, 14).addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(CpuCachejTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE).addComponent(CpuModeljTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE).addComponent(CpuMhzjTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE).addComponent(CpuSocketsjTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE).addComponent(CpuCoresPerSocketjTextField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE).addComponent(CpuVendorjTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE)))).addContainerGap()));
        CPUStaticInformationPanelLayout.setVerticalGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(CPUStaticInformationPanelLayout.createSequentialGroup().addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(CpuVendorjLabel).addComponent(CpuVendorjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(CpuModeljLabel).addComponent(CpuModeljTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(CpuMhzjLabel).addComponent(CpuMhzjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(CpuSocketsjLabel).addComponent(CpuSocketsjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(CpuCoresPerSocketjLabel).addComponent(CpuCoresPerSocketjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(CPUStaticInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(CpuCachejLabel).addComponent(CpuCachejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(141, Short.MAX_VALUE)));
        jSplitPane1.setLeftComponent(CPUStaticInformationPanel);
        CPUStaticInformationPanel.getAccessibleContext().setAccessibleName(resourceMap.getString("jPanel6.AccessibleContext.accessibleName"));
        CPULiveInformationPanel.setMinimumSize(new java.awt.Dimension(300, 257));
        CPULiveInformationPanel.setName("CPULiveInformationPanel");
        jSplitPane1.setRightComponent(CPULiveInformationPanel);
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 633, Short.MAX_VALUE).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 320, Short.MAX_VALUE).addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 320, Short.MAX_VALUE)));
        cpuPanel.add(jPanel7, java.awt.BorderLayout.SOUTH);
        systemInfoMonitorTabbedPane.addTab(resourceMap.getString("jPanel3.TabConstraints.tabTitle"), resourceMap.getIcon("jPanel3.TabConstraints.tabIcon"), cpuPanel, resourceMap.getString("jPanel3.TabConstraints.tabToolTip"));
        systemPanel.setName("systemPanel");
        OSPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("OSPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        OSPanel.setName("OSPanel");
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        jLabel10.setText(resourceMap.getString("jLabel10.text"));
        jLabel10.setName("jLabel10");
        jLabel11.setText(resourceMap.getString("jLabel11.text"));
        jLabel11.setName("jLabel11");
        jLabel12.setText(resourceMap.getString("jLabel12.text"));
        jLabel12.setName("jLabel12");
        jLabel13.setText(resourceMap.getString("jLabel13.text"));
        jLabel13.setName("jLabel13");
        jLabel14.setText(resourceMap.getString("jLabel14.text"));
        jLabel14.setName("jLabel14");
        jSeparator1.setName("jSeparator1");
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(systeminformationmonitor.SystemInformationMonitorApp.class).getContext().getActionMap(SystemInformationMonitorView.class, this);
        jButton1.setAction(actionMap.get("showSystemInformationEnvDialog"));
        jButton1.setText(resourceMap.getString("jButton1.text"));
        jButton1.setName("jButton1");
        jButton2.setAction(actionMap.get("showJavaInformationDialog"));
        jButton2.setText(resourceMap.getString("jButton2.text"));
        jButton2.setName("jButton2");
        OSDescriptionTextField.setEditable(false);
        OSDescriptionTextField.setText(resourceMap.getString("OSDescriptionTextField.text"));
        OSDescriptionTextField.setName("OSDescriptionTextField");
        OSNameTextField.setEditable(false);
        OSNameTextField.setText(resourceMap.getString("OSNameTextField.text"));
        OSNameTextField.setName("OSNameTextField");
        OSArchitectureTextField.setEditable(false);
        OSArchitectureTextField.setText(resourceMap.getString("OSArchitectureTextField.text"));
        OSArchitectureTextField.setName("OSArchitectureTextField");
        OSVendorVersionTextField.setEditable(false);
        OSVendorVersionTextField.setText(resourceMap.getString("OSVendorVersionTextField.text"));
        OSVendorVersionTextField.setName("OSVendorVersionTextField");
        OSVendorTextField.setEditable(false);
        OSVendorTextField.setText(resourceMap.getString("OSVendorTextField.text"));
        OSVendorTextField.setName("OSVendorTextField");
        OSDataModelTextField.setEditable(false);
        OSDataModelTextField.setText(resourceMap.getString("OSDataModelTextField.text"));
        OSDataModelTextField.setName("OSDataModelTextField");
        jLabel16.setText(resourceMap.getString("jLabel16.text"));
        jLabel16.setName("jLabel16");
        OSCPUEndianTextField.setEditable(false);
        OSCPUEndianTextField.setText(resourceMap.getString("OSCPUEndianTextField.text"));
        OSCPUEndianTextField.setName("OSCPUEndianTextField");
        javax.swing.GroupLayout OSPanelLayout = new javax.swing.GroupLayout(OSPanel);
        OSPanel.setLayout(OSPanelLayout);
        OSPanelLayout.setHorizontalGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(OSPanelLayout.createSequentialGroup().addContainerGap().addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(OSPanelLayout.createSequentialGroup().addComponent(jButton1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2)).addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 404, Short.MAX_VALUE).addGroup(OSPanelLayout.createSequentialGroup().addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel10).addComponent(jLabel11).addComponent(jLabel13).addComponent(jLabel12).addComponent(jLabel14).addComponent(jLabel16)).addGap(24, 24, 24).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addComponent(OSDescriptionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addComponent(OSArchitectureTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addComponent(OSVendorVersionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addComponent(OSVendorTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addComponent(OSDataModelTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE).addComponent(OSCPUEndianTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE)))).addContainerGap()));
        OSPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { jButton1, jButton2 });
        OSPanelLayout.setVerticalGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(OSPanelLayout.createSequentialGroup().addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSDescriptionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel10)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSArchitectureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel11)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSVendorTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel12)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSVendorVersionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel13)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSDataModelTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel14)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(OSCPUEndianTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel16)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE).addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(OSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(jButton2)).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel2.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel2.setName("jPanel2");
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Memory Information", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel9.setName("jPanel6");
        jLabel17.setText(resourceMap.getString("jLabel17.text"));
        jLabel17.setName("jLabel17");
        jLabel18.setText(resourceMap.getString("jLabel18.text"));
        jLabel18.setName("jLabel18");
        jLabel19.setText(resourceMap.getString("jLabel19.text"));
        jLabel19.setName("jLabel19");
        totalMemoryProgressBar.setDoubleBuffered(true);
        totalMemoryProgressBar.setName("totalMemoryProgressBar");
        totalMemoryProgressBar.setString(resourceMap.getString("totalMemoryProgressBar.string"));
        totalMemoryProgressBar.setStringPainted(true);
        usedMemoryProgressBar.setDoubleBuffered(true);
        usedMemoryProgressBar.setName("usedMemoryProgressBar");
        usedMemoryProgressBar.setString(resourceMap.getString("usedMemoryProgressBar.string"));
        usedMemoryProgressBar.setStringPainted(true);
        freeMemoryProgressBar.setDoubleBuffered(true);
        freeMemoryProgressBar.setName("freeMemoryProgressBar");
        freeMemoryProgressBar.setString(resourceMap.getString("freeMemoryProgressBar.string"));
        freeMemoryProgressBar.setStringPainted(true);
        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addContainerGap().addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel19).addComponent(jLabel18).addComponent(jLabel17)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(totalMemoryProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE).addComponent(usedMemoryProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE).addComponent(freeMemoryProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)).addContainerGap()));
        jPanel9Layout.setVerticalGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel9Layout.createSequentialGroup().addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel17).addComponent(totalMemoryProgressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE).addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel18).addComponent(usedMemoryProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(9, 9, 9).addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel19).addComponent(freeMemoryProgressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(14, 14, 14)));
        memoryPieChartPanel.setName("memoryPieChartPanel");
        javax.swing.GroupLayout memoryPieChartPanelLayout = new javax.swing.GroupLayout(memoryPieChartPanel);
        memoryPieChartPanel.setLayout(memoryPieChartPanelLayout);
        memoryPieChartPanelLayout.setHorizontalGroup(memoryPieChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 139, Short.MAX_VALUE));
        memoryPieChartPanelLayout.setVerticalGroup(memoryPieChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 318, Short.MAX_VALUE));
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(memoryPieChartPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addComponent(memoryPieChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("jPanel5.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        jPanel5.setName("jPanel5");
        jLabel15.setText(resourceMap.getString("jLabel15.text"));
        jLabel15.setName("jLabel15");
        systemUptimeTextField.setEditable(false);
        systemUptimeTextField.setText(resourceMap.getString("systemUptimeTextField.text"));
        systemUptimeTextField.setToolTipText(resourceMap.getString("systemUptimeTextField.toolTipText"));
        systemUptimeTextField.setName("systemUptimeTextField");
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addContainerGap().addComponent(jLabel15).addGap(18, 18, 18).addComponent(systemUptimeTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE).addContainerGap()));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel15).addComponent(systemUptimeTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(138, Short.MAX_VALUE)));
        javax.swing.GroupLayout systemPanelLayout = new javax.swing.GroupLayout(systemPanel);
        systemPanel.setLayout(systemPanelLayout);
        systemPanelLayout.setHorizontalGroup(systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(systemPanelLayout.createSequentialGroup().addContainerGap().addGroup(systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(OSPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        systemPanelLayout.setVerticalGroup(systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, systemPanelLayout.createSequentialGroup().addGroup(systemPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(systemPanelLayout.createSequentialGroup().addComponent(OSPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addContainerGap()));
        systemInfoMonitorTabbedPane.addTab(resourceMap.getString("systemPanel.TabConstraints.tabTitle"), resourceMap.getIcon("systemPanel.TabConstraints.tabIcon"), systemPanel, resourceMap.getString("systemPanel.TabConstraints.tabToolTip"));
        ioPanel.setName("ioPanel");
        ioPanel.setPreferredSize(new java.awt.Dimension(848, 477));
        ioPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane2.setDividerLocation(350);
        jSplitPane2.setDividerSize(3);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setResizeWeight(1.0);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(3, 477));
        jSplitPane2.setName("jSplitPane2");
        jSplitPane2.setPreferredSize(new java.awt.Dimension(848, 707));
        IOInformationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("IOInformationPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        IOInformationPanel.setMinimumSize(new java.awt.Dimension(0, 150));
        IOInformationPanel.setName("IOInformationPanel");
        IOInformationPanel.setPreferredSize(new java.awt.Dimension(846, 50));
        jScrollPane1.setName("jScrollPane1");
        jTable1.setModel(IOTableModel.getInstance());
        jTable1.setRowSorter(new TableRowSorter<IOTableModel>(IOTableModel.getInstance()));
        jTable1.setDoubleBuffered(true);
        jTable1.setName("jTable1");
        jTable1.setDefaultRenderer(Rectangle.class, new ShapeCellRenderer());
        jScrollPane1.setViewportView(jTable1);
        javax.swing.GroupLayout IOInformationPanelLayout = new javax.swing.GroupLayout(IOInformationPanel);
        IOInformationPanel.setLayout(IOInformationPanelLayout);
        IOInformationPanelLayout.setHorizontalGroup(IOInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(IOInformationPanelLayout.createSequentialGroup().addContainerGap().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 599, Short.MAX_VALUE).addContainerGap()));
        IOInformationPanelLayout.setVerticalGroup(IOInformationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(IOInformationPanelLayout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 91, Short.MAX_VALUE).addContainerGap()));
        jSplitPane2.setBottomComponent(IOInformationPanel);
        IOChart.setName("IOChart");
        IOChart.setPreferredSize(new java.awt.Dimension(846, 200));
        IOChart.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setDividerLocation(250);
        jSplitPane3.setDividerSize(4);
        jSplitPane3.setMinimumSize(new java.awt.Dimension(270, 300));
        jSplitPane3.setName("jSplitPane3");
        ioChartsPanel.setMinimumSize(new java.awt.Dimension(376, 58));
        ioChartsPanel.setName("ioChartsPanel");
        ioChartsPanel.setLayout(new java.awt.GridLayout(2, 1));
        ioWritesChartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("ioWritesChartPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        ioWritesChartPanel.setFocusable(false);
        ioWritesChartPanel.setName("ioWritesChartPanel");
        ioWritesChartPanel.setLayout(new java.awt.BorderLayout());
        writesChartPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        writesChartPanel.setName("writesChartPanel");
        writesChartPanel.setLayout(new java.awt.BorderLayout());
        ioWritesChartPanel.add(writesChartPanel, java.awt.BorderLayout.CENTER);
        ioChartsPanel.add(ioWritesChartPanel);
        readAccessChartPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("readAccessChartPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        readAccessChartPanel.setName("readAccessChartPanel");
        readAccessChartPanel.setLayout(new java.awt.BorderLayout());
        readsChartPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        readsChartPanel.setName("readsChartPanel");
        javax.swing.GroupLayout readsChartPanelLayout = new javax.swing.GroupLayout(readsChartPanel);
        readsChartPanel.setLayout(readsChartPanelLayout);
        readsChartPanelLayout.setHorizontalGroup(readsChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 362, Short.MAX_VALUE));
        readsChartPanelLayout.setVerticalGroup(readsChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 144, Short.MAX_VALUE));
        readAccessChartPanel.add(readsChartPanel, java.awt.BorderLayout.CENTER);
        ioChartsPanel.add(readAccessChartPanel);
        jSplitPane3.setRightComponent(ioChartsPanel);
        hardDriveSpaceChartPanel.setMinimumSize(new java.awt.Dimension(250, 0));
        hardDriveSpaceChartPanel.setName("hardDriveSpaceChartPanel");
        hardDriveSpaceChartPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane3.setLeftComponent(hardDriveSpaceChartPanel);
        IOChart.add(jSplitPane3, java.awt.BorderLayout.CENTER);
        jSplitPane2.setTopComponent(IOChart);
        ioPanel.add(jSplitPane2, java.awt.BorderLayout.CENTER);
        systemInfoMonitorTabbedPane.addTab(resourceMap.getString("ioPanel.TabConstraints.tabTitle"), resourceMap.getIcon("ioPanel.TabConstraints.tabIcon"), ioPanel, resourceMap.getString("ioPanel.TabConstraints.tabToolTip"));
        networkConnectionPanel.setName("networkConnectionPanel");
        networkConnectionPanel.setLayout(new java.awt.BorderLayout());
        jSplitPane4.setDividerLocation(300);
        jSplitPane4.setDividerSize(4);
        jSplitPane4.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane4.setResizeWeight(0.5);
        jSplitPane4.setName("jSplitPane4");
        ConnectionInformationGraphPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("ConnectionInformationGraphPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        ConnectionInformationGraphPanel.setMinimumSize(new java.awt.Dimension(12, 350));
        ConnectionInformationGraphPanel.setName("ConnectionInformationGraphPanel");
        ConnectionInformationGraphPanel.setLayout(new java.awt.GridLayout(2, 0));
        tcpJPanel.setName("tcpJPanel");
        tcpJPanel.setLayout(new java.awt.BorderLayout());
        ConnectionInformationGraphPanel.add(tcpJPanel);
        connectionJPanel.setName("connectionJPanel");
        connectionJPanel.setLayout(new java.awt.BorderLayout());
        ConnectionInformationGraphPanel.add(connectionJPanel);
        jSplitPane4.setTopComponent(ConnectionInformationGraphPanel);
        TCPConnectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, resourceMap.getString("TCPConnectionPanel.border.title"), javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION));
        TCPConnectionPanel.setMinimumSize(new java.awt.Dimension(0, 100));
        TCPConnectionPanel.setName("TCPConnectionPanel");
        TCPConnectionPanel.setPreferredSize(new java.awt.Dimension(100, 100));
        jScrollPane2.setName("jScrollPane2");
        TCPConnectionTable.setModel(ConnectionTableModel.getInstance());
        TCPConnectionTable.setRowSorter(new TableRowSorter<ConnectionTableModel>(ConnectionTableModel.getInstance()));
        TCPConnectionTable.setDoubleBuffered(true);
        TCPConnectionTable.setName("TCPConnectionTable");
        jScrollPane2.setViewportView(TCPConnectionTable);
        javax.swing.GroupLayout TCPConnectionPanelLayout = new javax.swing.GroupLayout(TCPConnectionPanel);
        TCPConnectionPanel.setLayout(TCPConnectionPanelLayout);
        TCPConnectionPanelLayout.setHorizontalGroup(TCPConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE));
        TCPConnectionPanelLayout.setVerticalGroup(TCPConnectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE));
        jSplitPane4.setBottomComponent(TCPConnectionPanel);
        networkConnectionPanel.add(jSplitPane4, java.awt.BorderLayout.CENTER);
        systemInfoMonitorTabbedPane.addTab(resourceMap.getString("networkConnectionPanel.TabConstraints.tabTitle"), resourceMap.getIcon("networkConnectionPanel.TabConstraints.tabIcon"), networkConnectionPanel, resourceMap.getString("networkConnectionPanel.TabConstraints.tabToolTip"));
        mainPanel.add(systemInfoMonitorTabbedPane, java.awt.BorderLayout.CENTER);
        menuBar.setName("menuBar");
        fileMenu.setText(resourceMap.getString("fileMenu.text"));
        fileMenu.setName("fileMenu");
        exitMenuItem.setAction(actionMap.get("quit"));
        exitMenuItem.setIcon(resourceMap.getIcon("exitMenuItem.icon"));
        exitMenuItem.setName("exitMenuItem");
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        optionMenu.setText(resourceMap.getString("optionMenu.text"));
        optionMenu.setName("optionMenu");
        alwaysOnTopCheckBoxMenuItem.setAction(actionMap.get("setAlwaysOnTop"));
        alwaysOnTopCheckBoxMenuItem.setText(resourceMap.getString("alwaysOnTopCheckBoxMenuItem.text"));
        alwaysOnTopCheckBoxMenuItem.setToolTipText(resourceMap.getString("alwaysOnTopCheckBoxMenuItem.toolTipText"));
        alwaysOnTopCheckBoxMenuItem.setIcon(resourceMap.getIcon("alwaysOnTopCheckBoxMenuItem.icon"));
        alwaysOnTopCheckBoxMenuItem.setName("alwaysOnTopCheckBoxMenuItem");
        optionMenu.add(alwaysOnTopCheckBoxMenuItem);
        menuBar.add(optionMenu);
        viewMenu.setText(resourceMap.getString("viewMenu.text"));
        viewMenu.setName("viewMenu");
        updateSpeedMenu.setIcon(resourceMap.getIcon("updateSpeedMenu.icon"));
        updateSpeedMenu.setText(resourceMap.getString("updateSpeedMenu.text"));
        updateSpeedMenu.setToolTipText(resourceMap.getString("updateSpeedMenu.toolTipText"));
        updateSpeedMenu.setName("updateSpeedMenu");
        jRadioButtonMenuItem1.setAction(actionMap.get("changeSpeedHigh"));
        speedSettingbuttonGroup.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setText(resourceMap.getString("jRadioButtonMenuItem1.text"));
        jRadioButtonMenuItem1.setName("jRadioButtonMenuItem1");
        updateSpeedMenu.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem2.setAction(actionMap.get("changeSpeedNormal"));
        speedSettingbuttonGroup.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText(resourceMap.getString("jRadioButtonMenuItem2.text"));
        jRadioButtonMenuItem2.setName("jRadioButtonMenuItem2");
        updateSpeedMenu.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem3.setAction(actionMap.get("changeSpeedLow"));
        speedSettingbuttonGroup.add(jRadioButtonMenuItem3);
        jRadioButtonMenuItem3.setText(resourceMap.getString("jRadioButtonMenuItem3.text"));
        jRadioButtonMenuItem3.setName("jRadioButtonMenuItem3");
        updateSpeedMenu.add(jRadioButtonMenuItem3);
        jRadioButtonMenuItem4.setAction(actionMap.get("changeSpeedPause"));
        speedSettingbuttonGroup.add(jRadioButtonMenuItem4);
        jRadioButtonMenuItem4.setText(resourceMap.getString("jRadioButtonMenuItem4.text"));
        jRadioButtonMenuItem4.setName("jRadioButtonMenuItem4");
        updateSpeedMenu.add(jRadioButtonMenuItem4);
        viewMenu.add(updateSpeedMenu);
        cpuJMenu.setAction(actionMap.get("SingleGraphActionPerformed"));
        cpuJMenu.setIcon(resourceMap.getIcon("cpuJMenu.icon"));
        cpuJMenu.setText(resourceMap.getString("cpuJMenu.text"));
        cpuJMenu.setToolTipText(resourceMap.getString("cpuJMenu.toolTipText"));
        cpuJMenu.setName("cpuJMenu");
        CPUSingleGraphButtonMenuItem.setAction(actionMap.get("SingleGraphActionPerformed"));
        CPUGraphButtonGroup.add(CPUSingleGraphButtonMenuItem);
        CPUSingleGraphButtonMenuItem.setSelected(true);
        CPUSingleGraphButtonMenuItem.setText(resourceMap.getString("CPUSingleGraphButtonMenuItem.text"));
        CPUSingleGraphButtonMenuItem.setName("CPUSingleGraphButtonMenuItem");
        cpuJMenu.add(CPUSingleGraphButtonMenuItem);
        CPUMultiGraphRadioButtonMenuItem.setAction(actionMap.get("MultiGraphActionPerformed"));
        CPUGraphButtonGroup.add(CPUMultiGraphRadioButtonMenuItem);
        CPUMultiGraphRadioButtonMenuItem.setText(resourceMap.getString("CPUMultiGraphRadioButtonMenuItem.text"));
        CPUMultiGraphRadioButtonMenuItem.setName("CPUMultiGraphRadioButtonMenuItem");
        cpuJMenu.add(CPUMultiGraphRadioButtonMenuItem);
        viewMenu.add(cpuJMenu);
        skinMenu.setIcon(resourceMap.getIcon("skinMenu.icon"));
        skinMenu.setText(resourceMap.getString("skinMenu.text"));
        skinMenu.setToolTipText(resourceMap.getString("skinMenu.toolTipText"));
        skinMenu.setName("skinMenu");
        lightSkinjMenu.setText(resourceMap.getString("lightSkinjMenu.text"));
        lightSkinjMenu.setName("lightSkinjMenu");
        businessSkinMenuRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(businessSkinMenuRadioButtonMenuItem);
        businessSkinMenuRadioButtonMenuItem.setText(resourceMap.getString("businessSkinMenuRadioButtonMenuItem.text"));
        businessSkinMenuRadioButtonMenuItem.setToolTipText(resourceMap.getString("businessSkinMenuRadioButtonMenuItem.toolTipText"));
        businessSkinMenuRadioButtonMenuItem.setName("businessSkinMenuRadioButtonMenuItem");
        lightSkinjMenu.add(businessSkinMenuRadioButtonMenuItem);
        businessBlueSteelRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(businessBlueSteelRadioButtonMenuItem);
        businessBlueSteelRadioButtonMenuItem.setText(resourceMap.getString("businessBlueSteelRadioButtonMenuItem.text"));
        businessBlueSteelRadioButtonMenuItem.setToolTipText(resourceMap.getString("businessBlueSteelRadioButtonMenuItem.toolTipText"));
        businessBlueSteelRadioButtonMenuItem.setName("businessBlueSteelRadioButtonMenuItem");
        lightSkinjMenu.add(businessBlueSteelRadioButtonMenuItem);
        businessBlackSteelSkinRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(businessBlackSteelSkinRadioButtonMenuItem);
        businessBlackSteelSkinRadioButtonMenuItem.setSelected(true);
        businessBlackSteelSkinRadioButtonMenuItem.setText(resourceMap.getString("businessBlackSteelSkinRadioButtonMenuItem.text"));
        businessBlackSteelSkinRadioButtonMenuItem.setToolTipText(resourceMap.getString("businessBlackSteelSkinRadioButtonMenuItem.toolTipText"));
        businessBlackSteelSkinRadioButtonMenuItem.setName("businessBlackSteelSkinRadioButtonMenuItem");
        lightSkinjMenu.add(businessBlackSteelSkinRadioButtonMenuItem);
        cremeRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(cremeRadioButtonMenuItem);
        cremeRadioButtonMenuItem.setText(resourceMap.getString("cremeRadioButtonMenuItem.text"));
        cremeRadioButtonMenuItem.setToolTipText(resourceMap.getString("cremeRadioButtonMenuItem.toolTipText"));
        cremeRadioButtonMenuItem.setName("cremeRadioButtonMenuItem");
        lightSkinjMenu.add(cremeRadioButtonMenuItem);
        cremeCoffeeRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(cremeCoffeeRadioButtonMenuItem);
        cremeCoffeeRadioButtonMenuItem.setText(resourceMap.getString("cremeCoffeeRadioButtonMenuItem.text"));
        cremeCoffeeRadioButtonMenuItem.setToolTipText(resourceMap.getString("cremeCoffeeRadioButtonMenuItem.toolTipText"));
        cremeCoffeeRadioButtonMenuItem.setName("cremeCoffeeRadioButtonMenuItem");
        lightSkinjMenu.add(cremeCoffeeRadioButtonMenuItem);
        saharaRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(saharaRadioButtonMenuItem);
        saharaRadioButtonMenuItem.setText(resourceMap.getString("saharaRadioButtonMenuItem.text"));
        saharaRadioButtonMenuItem.setToolTipText(resourceMap.getString("saharaRadioButtonMenuItem.toolTipText"));
        saharaRadioButtonMenuItem.setName("saharaRadioButtonMenuItem");
        lightSkinjMenu.add(saharaRadioButtonMenuItem);
        moderateRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(moderateRadioButtonMenuItem);
        moderateRadioButtonMenuItem.setText(resourceMap.getString("moderateRadioButtonMenuItem.text"));
        moderateRadioButtonMenuItem.setToolTipText(resourceMap.getString("moderateRadioButtonMenuItem.toolTipText"));
        moderateRadioButtonMenuItem.setName("moderateRadioButtonMenuItem");
        lightSkinjMenu.add(moderateRadioButtonMenuItem);
        officeSilver2007RadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(officeSilver2007RadioButtonMenuItem);
        officeSilver2007RadioButtonMenuItem.setText(resourceMap.getString("officeSilver2007RadioButtonMenuItem.text"));
        officeSilver2007RadioButtonMenuItem.setToolTipText(resourceMap.getString("officeSilver2007RadioButtonMenuItem.toolTipText"));
        officeSilver2007RadioButtonMenuItem.setName("officeSilver2007RadioButtonMenuItem");
        lightSkinjMenu.add(officeSilver2007RadioButtonMenuItem);
        officeBlue2007RadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(officeBlue2007RadioButtonMenuItem);
        officeBlue2007RadioButtonMenuItem.setText(resourceMap.getString("officeBlue2007RadioButtonMenuItem.text"));
        officeBlue2007RadioButtonMenuItem.setToolTipText(resourceMap.getString("officeBlue2007RadioButtonMenuItem.toolTipText"));
        officeBlue2007RadioButtonMenuItem.setName("officeBlue2007RadioButtonMenuItem");
        lightSkinjMenu.add(officeBlue2007RadioButtonMenuItem);
        nebulaRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(nebulaRadioButtonMenuItem);
        nebulaRadioButtonMenuItem.setText(resourceMap.getString("nebulaRadioButtonMenuItem.text"));
        nebulaRadioButtonMenuItem.setToolTipText(resourceMap.getString("nebulaRadioButtonMenuItem.toolTipText"));
        nebulaRadioButtonMenuItem.setName("nebulaRadioButtonMenuItem");
        lightSkinjMenu.add(nebulaRadioButtonMenuItem);
        nebulaBrickWallRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(nebulaBrickWallRadioButtonMenuItem);
        nebulaBrickWallRadioButtonMenuItem.setText(resourceMap.getString("nebulaBrickWallRadioButtonMenuItem.text"));
        nebulaBrickWallRadioButtonMenuItem.setToolTipText(resourceMap.getString("nebulaBrickWallRadioButtonMenuItem.toolTipText"));
        nebulaBrickWallRadioButtonMenuItem.setName("nebulaBrickWallRadioButtonMenuItem");
        lightSkinjMenu.add(nebulaBrickWallRadioButtonMenuItem);
        autumnRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(autumnRadioButtonMenuItem);
        autumnRadioButtonMenuItem.setText(resourceMap.getString("autumnRadioButtonMenuItem.text"));
        autumnRadioButtonMenuItem.setToolTipText(resourceMap.getString("autumnRadioButtonMenuItem.toolTipText"));
        autumnRadioButtonMenuItem.setName("autumnRadioButtonMenuItem");
        lightSkinjMenu.add(autumnRadioButtonMenuItem);
        mistSilverRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(mistSilverRadioButtonMenuItem);
        mistSilverRadioButtonMenuItem.setText(resourceMap.getString("mistSilverRadioButtonMenuItem.text"));
        mistSilverRadioButtonMenuItem.setToolTipText(resourceMap.getString("mistSilverRadioButtonMenuItem.toolTipText"));
        mistSilverRadioButtonMenuItem.setName("mistSilverRadioButtonMenuItem");
        lightSkinjMenu.add(mistSilverRadioButtonMenuItem);
        mistAquaRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(mistAquaRadioButtonMenuItem);
        mistAquaRadioButtonMenuItem.setText(resourceMap.getString("mistAquaRadioButtonMenuItem.text"));
        mistAquaRadioButtonMenuItem.setToolTipText(resourceMap.getString("mistAquaRadioButtonMenuItem.toolTipText"));
        mistAquaRadioButtonMenuItem.setName("mistAquaRadioButtonMenuItem");
        lightSkinjMenu.add(mistAquaRadioButtonMenuItem);
        dustRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(dustRadioButtonMenuItem);
        dustRadioButtonMenuItem.setText(resourceMap.getString("dustRadioButtonMenuItem.text"));
        dustRadioButtonMenuItem.setToolTipText(resourceMap.getString("dustRadioButtonMenuItem.toolTipText"));
        dustRadioButtonMenuItem.setName("dustRadioButtonMenuItem");
        lightSkinjMenu.add(dustRadioButtonMenuItem);
        dustCoffeeRadioButtonMenuItem1.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(dustCoffeeRadioButtonMenuItem1);
        dustCoffeeRadioButtonMenuItem1.setText(resourceMap.getString("dustCoffeeRadioButtonMenuItem1.text"));
        dustCoffeeRadioButtonMenuItem1.setToolTipText(resourceMap.getString("dustCoffeeRadioButtonMenuItem1.toolTipText"));
        dustCoffeeRadioButtonMenuItem1.setName("dustCoffeeRadioButtonMenuItem1");
        lightSkinjMenu.add(dustCoffeeRadioButtonMenuItem1);
        geminiRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(geminiRadioButtonMenuItem);
        geminiRadioButtonMenuItem.setText(resourceMap.getString("geminiRadioButtonMenuItem.text"));
        geminiRadioButtonMenuItem.setToolTipText(resourceMap.getString("geminiRadioButtonMenuItem.toolTipText"));
        geminiRadioButtonMenuItem.setName("geminiRadioButtonMenuItem");
        lightSkinjMenu.add(geminiRadioButtonMenuItem);
        skinMenu.add(lightSkinjMenu);
        darkSkinJMenu.setText(resourceMap.getString("darkSkinJMenu.text"));
        darkSkinJMenu.setName("darkSkinJMenu");
        twilightRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(twilightRadioButtonMenuItem);
        twilightRadioButtonMenuItem.setText(resourceMap.getString("twilightRadioButtonMenuItem.text"));
        twilightRadioButtonMenuItem.setToolTipText(resourceMap.getString("twilightRadioButtonMenuItem.toolTipText"));
        twilightRadioButtonMenuItem.setName("twilightRadioButtonMenuItem");
        darkSkinJMenu.add(twilightRadioButtonMenuItem);
        magellanRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(magellanRadioButtonMenuItem);
        magellanRadioButtonMenuItem.setText(resourceMap.getString("magellanRadioButtonMenuItem.text"));
        magellanRadioButtonMenuItem.setToolTipText(resourceMap.getString("magellanRadioButtonMenuItem.toolTipText"));
        magellanRadioButtonMenuItem.setName("magellanRadioButtonMenuItem");
        darkSkinJMenu.add(magellanRadioButtonMenuItem);
        ravenGraphiteRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(ravenGraphiteRadioButtonMenuItem);
        ravenGraphiteRadioButtonMenuItem.setText(resourceMap.getString("ravenGraphiteRadioButtonMenuItem.text"));
        ravenGraphiteRadioButtonMenuItem.setToolTipText(resourceMap.getString("ravenGraphiteRadioButtonMenuItem.toolTipText"));
        ravenGraphiteRadioButtonMenuItem.setName("ravenGraphiteRadioButtonMenuItem");
        darkSkinJMenu.add(ravenGraphiteRadioButtonMenuItem);
        ravenGraphiteGlassRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(ravenGraphiteGlassRadioButtonMenuItem);
        ravenGraphiteGlassRadioButtonMenuItem.setText(resourceMap.getString("ravenGraphiteGlassRadioButtonMenuItem.text"));
        ravenGraphiteGlassRadioButtonMenuItem.setToolTipText(resourceMap.getString("ravenGraphiteGlassRadioButtonMenuItem.toolTipText"));
        ravenGraphiteGlassRadioButtonMenuItem.setName("ravenGraphiteGlassRadioButtonMenuItem");
        darkSkinJMenu.add(ravenGraphiteGlassRadioButtonMenuItem);
        graphiteAquaRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(graphiteAquaRadioButtonMenuItem);
        graphiteAquaRadioButtonMenuItem.setText(resourceMap.getString("graphiteAquaRadioButtonMenuItem.text"));
        graphiteAquaRadioButtonMenuItem.setToolTipText(resourceMap.getString("graphiteAquaRadioButtonMenuItem.toolTipText"));
        graphiteAquaRadioButtonMenuItem.setName("graphiteAquaRadioButtonMenuItem");
        darkSkinJMenu.add(graphiteAquaRadioButtonMenuItem);
        ravenRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(ravenRadioButtonMenuItem);
        ravenRadioButtonMenuItem.setText(resourceMap.getString("ravenRadioButtonMenuItem.text"));
        ravenRadioButtonMenuItem.setToolTipText(resourceMap.getString("ravenRadioButtonMenuItem.toolTipText"));
        ravenRadioButtonMenuItem.setName("ravenRadioButtonMenuItem");
        darkSkinJMenu.add(ravenRadioButtonMenuItem);
        magmaRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(magmaRadioButtonMenuItem);
        magmaRadioButtonMenuItem.setText(resourceMap.getString("magmaRadioButtonMenuItem.text"));
        magmaRadioButtonMenuItem.setToolTipText(resourceMap.getString("magmaRadioButtonMenuItem.toolTipText"));
        magmaRadioButtonMenuItem.setName("magmaRadioButtonMenuItem");
        darkSkinJMenu.add(magmaRadioButtonMenuItem);
        challengerDeepRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(challengerDeepRadioButtonMenuItem);
        challengerDeepRadioButtonMenuItem.setText(resourceMap.getString("challengerDeepRadioButtonMenuItem.text"));
        challengerDeepRadioButtonMenuItem.setToolTipText(resourceMap.getString("challengerDeepRadioButtonMenuItem.toolTipText"));
        challengerDeepRadioButtonMenuItem.setName("challengerDeepRadioButtonMenuItem");
        darkSkinJMenu.add(challengerDeepRadioButtonMenuItem);
        emeraldDuskRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(emeraldDuskRadioButtonMenuItem);
        emeraldDuskRadioButtonMenuItem.setText(resourceMap.getString("emeraldDuskRadioButtonMenuItem.text"));
        emeraldDuskRadioButtonMenuItem.setToolTipText(resourceMap.getString("emeraldDuskRadioButtonMenuItem.toolTipText"));
        emeraldDuskRadioButtonMenuItem.setName("emeraldDuskRadioButtonMenuItem");
        darkSkinJMenu.add(emeraldDuskRadioButtonMenuItem);
        skinMenu.add(darkSkinJMenu);
        systemDefaultSkinRadioButtonMenuItem.setAction(actionMap.get("changeSkin"));
        skinButtonGroup.add(systemDefaultSkinRadioButtonMenuItem);
        systemDefaultSkinRadioButtonMenuItem.setText(resourceMap.getString("systemDefaultSkinRadioButtonMenuItem.text"));
        systemDefaultSkinRadioButtonMenuItem.setToolTipText(resourceMap.getString("systemDefaultSkinRadioButtonMenuItem.toolTipText"));
        systemDefaultSkinRadioButtonMenuItem.setName("systemDefaultSkinRadioButtonMenuItem");
        skinMenu.add(systemDefaultSkinRadioButtonMenuItem);
        viewMenu.add(skinMenu);
        refreshNowjMenuItem.setAction(actionMap.get("refreshNow"));
        refreshNowjMenuItem.setIcon(resourceMap.getIcon("refreshNowjMenuItem.icon"));
        refreshNowjMenuItem.setText(resourceMap.getString("refreshNowjMenuItem.text"));
        refreshNowjMenuItem.setToolTipText(resourceMap.getString("refreshNowjMenuItem.toolTipText"));
        refreshNowjMenuItem.setName("refreshNowjMenuItem");
        viewMenu.add(refreshNowjMenuItem);
        menuBar.add(viewMenu);
        helpMenu.setText(resourceMap.getString("helpMenu.text"));
        helpMenu.setName("helpMenu");
        helpMenuItem.setAction(actionMap.get("helpDialog"));
        helpMenuItem.setIcon(resourceMap.getIcon("helpMenuItem.icon"));
        helpMenuItem.setText(resourceMap.getString("helpMenuItem.text"));
        helpMenuItem.setToolTipText(resourceMap.getString("helpMenuItem.toolTipText"));
        helpMenuItem.setName("helpMenuItem");
        helpMenu.add(helpMenuItem);
        aboutMenuItem.setAction(actionMap.get("showAboutBox"));
        aboutMenuItem.setIcon(resourceMap.getIcon("aboutMenuItem.icon"));
        aboutMenuItem.setName("aboutMenuItem");
        helpMenu.add(aboutMenuItem);
        menuBar.add(helpMenu);
        statusPanel.setName("statusPanel");
        statusPanel.setPreferredSize(new java.awt.Dimension(638, 30));
        statusPanelSeparator.setName("statusPanelSeparator");
        statusMessageLabel.setName("statusMessageLabel");
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel");
        progressBar.setName("progressBar");
        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE).addGroup(statusPanelLayout.createSequentialGroup().addContainerGap().addComponent(statusMessageLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 468, Short.MAX_VALUE).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(statusAnimationLabel).addContainerGap()));
        statusPanelLayout.setVerticalGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(statusPanelLayout.createSequentialGroup().addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE).addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(statusMessageLabel).addComponent(statusAnimationLabel).addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(3, 3, 3)));
        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }

    private boolean singleGraph = true;

    private void flipCpuGraph(java.awt.event.MouseEvent evt) {
        CardLayout layout = (CardLayout) jPanel8.getLayout();
        layout.next(jPanel8);
        singleGraph = !singleGraph;
        CPUSingleGraphButtonMenuItem.setSelected(singleGraph);
        CPUMultiGraphRadioButtonMenuItem.setSelected(!singleGraph);
    }

    @Action
    public void SingleGraphActionPerformed() {
        CardLayout layout = (CardLayout) jPanel8.getLayout();
        layout.first(jPanel8);
        singleGraph = true;
    }

    @Action
    public void MultiGraphActionPerformed() {
        CardLayout layout = (CardLayout) jPanel8.getLayout();
        layout.last(jPanel8);
        singleGraph = false;
    }

    private javax.swing.JPanel AllCpujPanel;

    private javax.swing.ButtonGroup CPUGraphButtonGroup;

    private systeminformationmonitor.CpuLiveInformation CPULiveInformationPanel;

    private javax.swing.JRadioButtonMenuItem CPUMultiGraphRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem CPUSingleGraphButtonMenuItem;

    private javax.swing.JPanel CPUStaticInformationPanel;

    private javax.swing.JPanel ConnectionInformationGraphPanel;

    private javax.swing.JLabel CpuCachejLabel;

    private javax.swing.JTextField CpuCachejTextField;

    private javax.swing.JLabel CpuCoresPerSocketjLabel;

    private javax.swing.JTextField CpuCoresPerSocketjTextField;

    private javax.swing.JLabel CpuMhzjLabel;

    private javax.swing.JTextField CpuMhzjTextField;

    private javax.swing.JLabel CpuModeljLabel;

    private javax.swing.JTextField CpuModeljTextField;

    private javax.swing.JLabel CpuSocketsjLabel;

    private javax.swing.JTextField CpuSocketsjTextField;

    private javax.swing.JLabel CpuVendorjLabel;

    private javax.swing.JTextField CpuVendorjTextField;

    private javax.swing.JPanel IOChart;

    private javax.swing.JPanel IOInformationPanel;

    private javax.swing.JPanel MultipleCpujPanel;

    private javax.swing.JTextField OSArchitectureTextField;

    private javax.swing.JTextField OSCPUEndianTextField;

    private javax.swing.JTextField OSDataModelTextField;

    private javax.swing.JTextField OSDescriptionTextField;

    private javax.swing.JTextField OSNameTextField;

    private javax.swing.JPanel OSPanel;

    private javax.swing.JTextField OSVendorTextField;

    private javax.swing.JTextField OSVendorVersionTextField;

    private javax.swing.JPanel TCPConnectionPanel;

    private javax.swing.JTable TCPConnectionTable;

    private javax.swing.JLabel adaperNameLabel;

    private javax.swing.JCheckBox adapterEnabledCheckBox;

    private javax.swing.JTextField adapterHostNameTextField;

    private javax.swing.JTextField adapterIPAddressTextField;

    private javax.swing.JCheckBox adapterLoopbackCheckBox;

    private javax.swing.JTextField adapterMACAddressTextField;

    private javax.swing.JTextField adapterNameTextField;

    private javax.swing.JCheckBox adapterVirtualCheckBox;

    private javax.swing.JCheckBoxMenuItem alwaysOnTopCheckBoxMenuItem;

    private javax.swing.JRadioButtonMenuItem autumnRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem businessBlackSteelSkinRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem businessBlueSteelRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem businessSkinMenuRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem challengerDeepRadioButtonMenuItem;

    private javax.swing.JPanel connectionJPanel;

    private javax.swing.JMenu cpuJMenu;

    private javax.swing.JPanel cpuPanel;

    private javax.swing.JRadioButtonMenuItem cremeCoffeeRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem cremeRadioButtonMenuItem;

    private javax.swing.JMenu darkSkinJMenu;

    private javax.swing.JRadioButtonMenuItem dustCoffeeRadioButtonMenuItem1;

    private javax.swing.JRadioButtonMenuItem dustRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem emeraldDuskRadioButtonMenuItem;

    private javax.swing.JProgressBar freeMemoryProgressBar;

    private javax.swing.JRadioButtonMenuItem geminiRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem graphiteAquaRadioButtonMenuItem;

    private javax.swing.JPanel hardDriveSpaceChartPanel;

    private javax.swing.JMenuItem helpMenuItem;

    private javax.swing.JPanel ioChartsPanel;

    private javax.swing.JPanel ioPanel;

    private javax.swing.JPanel ioWritesChartPanel;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JLabel jLabel1;

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

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JPanel jPanel9;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;

    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JSplitPane jSplitPane2;

    private javax.swing.JSplitPane jSplitPane3;

    private javax.swing.JSplitPane jSplitPane4;

    private javax.swing.JTable jTable1;

    private javax.swing.JMenu lightSkinjMenu;

    private javax.swing.JRadioButtonMenuItem magellanRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem magmaRadioButtonMenuItem;

    private javax.swing.JPanel mainPanel;

    private javax.swing.JPanel memoryPieChartPanel;

    private javax.swing.JMenuBar menuBar;

    private javax.swing.JRadioButtonMenuItem mistAquaRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem mistSilverRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem moderateRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem nebulaBrickWallRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem nebulaRadioButtonMenuItem;

    private javax.swing.JLabel networkAdapterLabel;

    private javax.swing.JPanel networkAdapterPanel;

    private javax.swing.JPanel networkConnectionPanel;

    private javax.swing.JTextField networkDomainName;

    private javax.swing.JTextField networkGateway;

    private javax.swing.JTextField networkPrimaryDNS;

    private javax.swing.JTextField networkSecondaryDNS;

    private javax.swing.JRadioButtonMenuItem officeBlue2007RadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem officeSilver2007RadioButtonMenuItem;

    private javax.swing.JMenu optionMenu;

    private javax.swing.JTable processTable;

    private javax.swing.JScrollPane processTableScrollPane;

    private javax.swing.JPanel processesPanel;

    private javax.swing.JProgressBar progressBar;

    private javax.swing.JRadioButtonMenuItem ravenGraphiteGlassRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem ravenGraphiteRadioButtonMenuItem;

    private javax.swing.JRadioButtonMenuItem ravenRadioButtonMenuItem;

    private javax.swing.JPanel readAccessChartPanel;

    private javax.swing.JPanel readsChartPanel;

    private javax.swing.JMenuItem refreshNowjMenuItem;

    private javax.swing.JRadioButtonMenuItem saharaRadioButtonMenuItem;

    private javax.swing.ButtonGroup skinButtonGroup;

    private javax.swing.JMenu skinMenu;

    private javax.swing.ButtonGroup speedSettingbuttonGroup;

    private javax.swing.JLabel statusAnimationLabel;

    private javax.swing.JLabel statusMessageLabel;

    private javax.swing.JPanel statusPanel;

    private javax.swing.JRadioButtonMenuItem systemDefaultSkinRadioButtonMenuItem;

    private javax.swing.JTabbedPane systemInfoMonitorTabbedPane;

    private javax.swing.JPanel systemPanel;

    private javax.swing.JTextField systemUptimeTextField;

    private javax.swing.JPanel tcpJPanel;

    private javax.swing.JProgressBar totalMemoryProgressBar;

    private javax.swing.JRadioButtonMenuItem twilightRadioButtonMenuItem;

    private javax.swing.JMenu updateSpeedMenu;

    private javax.swing.JProgressBar usedMemoryProgressBar;

    private javax.swing.JMenu viewMenu;

    private javax.swing.JPanel writesChartPanel;

    private final Timer messageTimer;

    private final Timer busyIconTimer;

    private final Icon idleIcon;

    private final Icon[] busyIcons = new Icon[15];

    private int busyIconIndex = 0;

    private JDialog aboutBox;

    private ProcessTableModel processesTableModel;

    private NetInfoComboBoxModel netInfoComboModel;

    private Timer updateUITaskTimer;

    private static GlobalMonitorUpdater updateUITask;

    public static final int NORMAL_SPEED = 1000;

    public static final int LOW_SPEED = 4000;

    public static final int HIGH_SPEED = 500;

    /**
     * @return the memoryPieChartPanel
     */
    public javax.swing.JPanel getMemoryPieChartPanel() {
        return memoryPieChartPanel;
    }

    /**
     * @return the hardDriveSpaceChartPanel
     */
    public javax.swing.JPanel getHardDriveSpaceChartPanel() {
        return hardDriveSpaceChartPanel;
    }

    /**
     * Actiona listener for the Network Interface Combo Box
     */
    public class NetworkInterfaceComboBoxActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox) e.getSource();
            String networkName = (String) cb.getSelectedItem();
            (new UpdateNetworkInformation(networkName)).execute();
        }
    }

    /**
     * Action Listener to trigger system update
     */
    public class GlobalMonitorUpdater implements ActionListener {

        private SystemInformationGather sysGather;

        public GlobalMonitorUpdater(int time) {
            sysGather = SystemInformationGather.getInstance();
            sysGather.setTime(time);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            sysGather.refresh();
        }
    }

    /**
     * UpdateNetworkInformation class update all network related UI
     */
    public class UpdateNetworkInformation extends SwingWorker {

        private NetInterfaceObject netInterface;

        private NetInfoObject netInfoObj;

        private String networkName;

        public UpdateNetworkInformation(String networkName) {
            this.networkName = networkName;
            this.netInterface = new NetInterfaceObject();
            this.netInfoObj = new NetInfoObject();
        }

        @Override
        protected Object doInBackground() throws Exception {
            netInterface = NetInterface.getNetInfoObject(networkName);
            netInfoObj = NetInformation.getNetInfo();
            return null;
        }

        @Override
        protected void done() {
            adapterNameTextField.setText(netInterface.getName());
            adapterIPAddressTextField.setText(netInterface.getIpAddress());
            adapterMACAddressTextField.setText(netInterface.getHardwareAddress());
            adapterHostNameTextField.setText(netInterface.getHostName());
            adapterLoopbackCheckBox.setSelected(netInterface.isIsLoopback());
            adapterVirtualCheckBox.setSelected(netInterface.isIsVirtual());
            adapterEnabledCheckBox.setSelected(netInterface.isIsUp());
            networkGateway.setText(netInfoObj.getDefaultGateway());
            networkDomainName.setText(netInfoObj.getDomainName());
            networkPrimaryDNS.setText(netInfoObj.getPrimaryDns());
            networkSecondaryDNS.setText(netInfoObj.getSecondaryDns());
        }
    }

    /**
     * Force the program to refresh the UI
     * @return Task
     */
    @Action
    public Task refreshNow() {
        return new RefreshNowTask(getApplication());
    }

    private class RefreshNowTask extends org.jdesktop.application.Task<Object, Void> {

        RefreshNowTask(org.jdesktop.application.Application app) {
            super(app);
        }

        @Override
        protected Object doInBackground() {
            SystemInformationGather.getInstance().refresh();
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        SwingUtilities.updateComponentTreeUI(getComponent());
                        SwingUtilities.updateComponentTreeUI(statusPanel);
                    }
                });
            } catch (InterruptedException ex) {
                Logger.getLogger(SystemInformationMonitorView.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(SystemInformationMonitorView.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        protected void succeeded(Object result) {
        }
    }

    public void setUpTimeTextField(String timeString) {
        this.systemUptimeTextField.setText(timeString);
    }

    public void setOSArchitectureTextField(String OSArchitectureText) {
        this.OSArchitectureTextField.setText(OSArchitectureText);
    }

    public void setOSDataModelTextField(String OSDataModelText) {
        this.OSDataModelTextField.setText(OSDataModelText);
    }

    public void setOSDescriptionTextField(String OSDescriptionText) {
        this.OSDescriptionTextField.setText(OSDescriptionText);
    }

    public void setOSNameTextField(String OSNameText) {
        this.OSNameTextField.setText(OSNameText);
    }

    public void setOSVendorTextField(String OSVendorText) {
        this.OSVendorTextField.setText(OSVendorText);
    }

    public void setOSVendorVersionTextField(String OSVendorVersionText) {
        this.OSVendorVersionTextField.setText(OSVendorVersionText);
    }

    public void setOSCPUEndianTextField(String OSCPUEndianText) {
        this.OSCPUEndianTextField.setText(OSCPUEndianText);
    }

    public void setUsedMemoryProgressBar(int size, String title) {
        this.usedMemoryProgressBar.setValue(size);
        this.usedMemoryProgressBar.setString(title);
        this.usedMemoryProgressBar.setStringPainted(true);
    }

    public void setFreeMemoryProgressBar(int size, String title) {
        this.freeMemoryProgressBar.setValue(size);
        this.freeMemoryProgressBar.setString(title);
        this.freeMemoryProgressBar.setStringPainted(true);
    }

    public void setTotalMemoryProgressBar(int size, String title) {
        this.totalMemoryProgressBar.setValue(size);
        this.totalMemoryProgressBar.setString(title);
        this.totalMemoryProgressBar.setStringPainted(true);
    }

    @Action
    public void showJavaInformationDialog() {
        final JavaEnvDialog javaInformationDialog = new JavaEnvDialog(getFrame(), true);
        javaInformationDialog.addWindowListener(new java.awt.event.WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                JavaInformationObject javaOjb = JavaInformation.getJavaInformation();
                javaInformationDialog.setJavaHomeTextField(javaOjb.getJavaHome());
                javaInformationDialog.setJavaVMVendorTextField(javaOjb.getJvmVendor());
                javaInformationDialog.setJavaVersionTextField(javaOjb.getJvmVersion());
            }
        });
        XYCoord xyp = CalcDialogCenter.getDialogCenterLocation(getFrame(), javaInformationDialog);
        javaInformationDialog.setLocation(xyp.getX(), xyp.getY());
        javaInformationDialog.setVisible(true);
    }

    @Action
    public void showSystemInformationEnvDialog() {
        final SystemEnvDialog sysInformationDialog = new SystemEnvDialog(getFrame(), true);
        XYCoord xyp = CalcDialogCenter.getDialogCenterLocation(getFrame(), sysInformationDialog);
        sysInformationDialog.setLocation(xyp.getX(), xyp.getY());
        sysInformationDialog.setVisible(true);
    }

    @Action
    public void helpDialog() {
        final HelpDialog helpDialog = new HelpDialog(getFrame(), true);
        XYCoord xyp = CalcDialogCenter.getDialogCenterLocation(getFrame(), helpDialog);
        helpDialog.setLocation(xyp.getX(), xyp.getY());
        helpDialog.setVisible(true);
    }
}
