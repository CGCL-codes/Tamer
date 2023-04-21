package ceirinhashls.gui;

import ceirinhashls.CheckHubConnection;
import ceirinhashls.DCHub;
import ceirinhashls.MultiHubServer;
import ceirinhashls.PublicHubList;
import ceirinhashls.SaveHublist;
import ceirinhashls.UploadHubList;
import ceirinhashls.util.Settings;
import ceirinhashls.gui.*;
import javax.swing.*;
import com.maxmind.geoip.*;

/**
 *
 * @author  botelhodaniel
 */
public class MainFrame extends javax.swing.JFrame {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    private UIManager.LookAndFeelInfo lookInfo[] = UIManager.getInstalledLookAndFeels();

    private static int metal = 0;

    private static int motif = 1;

    private static int windows = 2;

    private int lnfChoosed;

    private int lnfIndex = 0;

    private UploadHubListFrame upHubListFrame;

    private PublicHubList hublist;

    private TableSorter tableSorter;

    private StatusPanel statusPanel;

    private Thread multiHubServerThread;

    private MultiHubServer multiHubServer;

    private DCTableModel dctm;

    private Schedule timer;

    private ScheduleDialog scheduleDialog;

    private Thread timerThread;

    private OpenDialog openDialog;

    private LookupService lookupService;

    /** Creates new form MainFrame */
    public MainFrame() {
        hublist = new PublicHubList();
        try {
            lookupService = new LookupService(getAppPath() + java.io.File.separator + "GeoIP.dat");
            Settings.loadSettings(getAppPath() + java.io.File.separator + "Settings.xml", hublist);
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                Settings.setDefault();
                Settings.saveSettings(getAppPath() + java.io.File.separator + "Settings.xml", hublist.iterator());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(getAppPath());
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        openDialog = new OpenDialog(this);
        upHubListFrame = new UploadHubListFrame(this);
        scheduleDialog = new ScheduleDialog(this);
        timer = new Schedule(this);
        initComponents();
        setTimerEnable(Settings.SCHEDULED);
        statusHandle();
        try {
            System.out.println("Vai come�ar o multiHubServer");
            multiHubServer = new MultiHubServer(hublist, Settings.PORT);
            System.out.println("Ja come�ou o multiHubServer");
            addTableSorter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getAppPath() {
        return System.getProperty("user.dir");
    }

    public void setTimerEnable(boolean bool) {
        if (timer.isAlive()) {
            if (bool) jLabel2.setText(StringDef.SCHEDULE_RUNNING); else {
                timer.end();
                jLabel2.setText(StringDef.SCHEDULE_IDLE);
            }
            return;
        }
        if (bool) {
            timer.start();
            jLabel2.setText(StringDef.SCHEDULE_RUNNING);
            return;
        }
        jLabel2.setText(StringDef.SCHEDULE_IDLE);
    }

    public void timeReached() {
        if (Settings.PING_ALL) {
            pingList(hublist.iterator());
        }
        System.out.println(new java.util.Date() + " :: Guarda ficheiro");
        if (Settings.LOCAL_FILE) new SaveHublist(hublistToIterator(), Settings.LOCAL_FILE_PATH);
        if (Settings.WEB_SERVER) {
            try {
                java.io.File f = java.io.File.createTempFile("temp", (Settings.REMOTE_FILE_PATH.toLowerCase().endsWith(".xml")) ? ".xml" : ".config");
                new SaveHublist(hublistToIterator(), f);
                new UploadHubList(Settings.FTP_SERVER_ADDRESS, Settings.FTP_SERVER_USER, Settings.FTP_SERVER_PASSWORD, Settings.REMOTE_FILE_PATH, f.getAbsolutePath());
                System.out.println(f.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public java.util.Iterator hublistToIterator() {
        if (Settings.USE_TABLE_COMPARATOR) return hublist.iterator(DCTableModel.getComparator()); else return hublist.iterator();
    }

    public PublicHubList getPublicHubList() {
        return hublist;
    }

    private void statusHandle() {
        statusPanel = new StatusPanel(jToggleButton1.isSelected());
        jPanel2.add(statusPanel);
    }

    private void doTable() {
        jTable1.setModel(tableSorter);
        tableSorter.setTableHeader(jTable1.getTableHeader());
        jScrollPane1.setViewportView(jTable1);
    }

    private void addTableSorter() throws Exception {
        dctm = new DCTableModel(hublist);
        tableSorter = new TableSorter(dctm);
        jTable1 = new javax.swing.JTable(tableSorter);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                userTableMouseClicked(evt);
            }
        });
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tableSorter.setTableHeader(jTable1.getTableHeader());
        jScrollPane1.setViewportView(jTable1);
    }

    private void userTableMouseClicked(java.awt.event.MouseEvent evt) {
        if (SwingUtilities.isRightMouseButton(evt)) {
            int row = jTable1.getSelectedRow();
            System.out.println("Selected Row:" + row + " Address:" + jTable1.getValueAt(row, 1));
            DCHub hub = hublist.get(jTable1.getValueAt(row, 1) + "");
            pingList(hub);
        }
    }

    private void updateTable() {
        dctm.fireTableDataChanged();
        jScrollPane1.setViewportView(jTable1);
        jLabel1.setText(StringDef.TOTAL_HUB_COUNT + jTable1.getRowCount());
    }

    private void pingList(DCHub hub) {
        jLabel3.setText("check hub:" + hub.getName() + "(" + hub.getAddress() + ")");
        new Thread(new CheckHubConnection(hub)).start();
    }

    private void pingList(java.util.Iterator it) {
        jLabel3.setText("checking hubs...");
        while (it.hasNext()) {
            DCHub hub = (DCHub) it.next();
            jLabel3.setText("check hub:" + hub.getName() + "(" + hub.getAddress() + ")");
            new Thread(new CheckHubConnection(hub)).start();
        }
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        setTitle(StringDef.APP_NAME);
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout(10, 10));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(StringDef.HUBS));
        jPanel1.setAlignmentX(0.2F);
        jPanel1.setAlignmentY(0.2F);
        jPanel3.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setPreferredSize(new java.awt.Dimension(452, 202));
        jPanel3.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jPanel1.add(jPanel3, java.awt.BorderLayout.CENTER);
        jPanel4.setLayout(new java.awt.GridLayout(1, 0));
        jPanel5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        jPanel4.add(jPanel5);
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));
        jLabel1.setText(StringDef.TOTAL_HUB_COUNT + "0");
        jPanel6.add(jLabel1);
        jPanel4.add(jPanel6);
        jPanel1.add(jPanel4, java.awt.BorderLayout.SOUTH);
        jPanel7.setAlignmentX(0.0F);
        jPanel7.setAlignmentY(0.0F);
        jToggleButton1.setText(StringDef.START_SERVER);
        jToggleButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel7.add(jToggleButton1);
        jButton2.setText(StringDef.REFRESH);
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton2);
        jButton3.setText(StringDef.ABOUT);
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel7.add(jButton3);
        jPanel1.add(jPanel7, java.awt.BorderLayout.NORTH);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        jPanel2.add(jLabel3);
        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setPreferredSize(new java.awt.Dimension(2, 10));
        jPanel2.add(jSeparator2);
        jPanel2.add(jLabel2);
        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);
        jMenu1.setText("Menu");
        jMenuItem1.setText(StringDef.SAVE);
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);
        jMenuItem3.setText(StringDef.OPEN);
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);
        jMenu1.add(jSeparator1);
        jMenuItem2.setText(StringDef.EXIT);
        jMenu1.add(jMenuItem2);
        jMenuBar1.add(jMenu1);
        jMenu2.setText(StringDef.TOOLS);
        jMenuItem4.setText(StringDef.SCHEDULE);
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);
        jMenuBar1.add(jMenu2);
        setJMenuBar(jMenuBar1);
        pack();
    }

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {
        openDialog.setVisible(true);
    }

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {
        scheduleDialog.setVisible(true);
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        if (Settings.PING_ALL) {
            pingList(hublist.iterator());
        }
    }

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        upHubListFrame.setVisible(true);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        updateTable();
        Runtime.getRuntime().gc();
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (jToggleButton1.isSelected()) {
                multiHubServer.setListening(true);
                System.out.println("ligou!");
                multiHubServerThread = new Thread(multiHubServer);
                multiHubServerThread.start();
            } else {
                System.out.println("desligou!");
                multiHubServer.closeServer();
            }
            statusPanel.setConnected(jToggleButton1.isSelected());
            statusPanel.updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        try {
            Settings.saveSettings(getAppPath() + java.io.File.separator + "Settings.xml", hublist.iterator());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        new MainFrame().setVisible(true);
    }

    private javax.swing.JTable jTable1;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JMenu jMenu1;

    private javax.swing.JMenu jMenu2;

    private javax.swing.JMenuBar jMenuBar1;

    private javax.swing.JMenuItem jMenuItem1;

    private javax.swing.JMenuItem jMenuItem2;

    private javax.swing.JMenuItem jMenuItem3;

    private javax.swing.JMenuItem jMenuItem4;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JSeparator jSeparator1;

    private javax.swing.JSeparator jSeparator2;

    private javax.swing.JToggleButton jToggleButton1;
}
