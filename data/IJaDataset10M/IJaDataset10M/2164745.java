package lablog.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import lablog.gui.dialog.ChangePswdDialog;
import lablog.gui.dialog.PasswordDialog;
import lablog.gui.frame.AddDatasets;
import lablog.gui.frame.GrantManager;
import lablog.gui.frame.Journal;
import lablog.gui.frame.LoginWindow;
import lablog.gui.frame.Persons;
import lablog.gui.frame.RetrieveData;
import lablog.gui.frame.Subgroups;
import lablog.util.DBAccess;
import lablog.util.DBConnectionDetails;
import lablog.util.PropertiesManager;
import lablog.util.orm.DatabaseHelper;

/**
 * The Main class of the LabLog. Main extends JFrame and provides the Desktop pane,
 * the menu, and the buttons.
 *  
 * @author Jan Grewe
 *
 */
public class LablogMain extends JFrame {

    private static final long serialVersionUID = -8854367067930477722L;

    private static final double version = 0.9;

    private static LablogMain instance = null;

    private Container cp;

    private JPanel buttonsPanel;

    private JDesktopPane desktopPane;

    private JTextArea newsArea;

    private DBConnectionDetails connDetails;

    private Connection conn;

    private boolean connected = false;

    private String pswd;

    private String server, database, lastServer, lastDb;

    private JButton disconnectIconBtn, connectIconBtn;

    private PropertiesManager properties;

    private JInternalFrame[] frames;

    private LabDBMainActionListener al;

    public static LablogMain instance() {
        if (instance == null) instance = new LablogMain();
        return instance;
    }

    private LablogMain() {
        super();
        properties = PropertiesManager.instance();
        this.setTitle(properties.getProperty("Title"));
        Vector<Image> imageList = new Vector<Image>();
        imageList.add(new ImageIcon(properties.getResource("iconWindow16")).getImage());
        imageList.add(new ImageIcon(properties.getResource("iconWindow32")).getImage());
        imageList.add(new ImageIcon(properties.getResource("iconWindow48")).getImage());
        imageList.add(new ImageIcon(properties.getResource("iconWindow64")).getImage());
        this.setIconImages(imageList);
        al = new LabDBMainActionListener();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowListener() {

            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (connected) {
                    if (disconnectBtnPressed()) {
                        System.exit(NORMAL);
                    }
                } else {
                    System.exit(NORMAL);
                }
            }

            public void windowClosing(WindowEvent e) {
                if (connected) {
                    if (disconnectBtnPressed()) {
                        System.exit(NORMAL);
                    }
                } else {
                    System.exit(NORMAL);
                }
            }

            ;

            public void windowDeactivated(WindowEvent e) {
            }

            ;

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            public void windowIconified(WindowEvent e) {
            }

            ;

            @Override
            public void windowOpened(WindowEvent e) {
            }
        });
        setJMenuBar(initMenu());
        initGUI(1280, 800);
        loginBtnPressed();
    }

    public static void main(String[] args) {
        boolean success = false;
        try {
            for (LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(laf.getName())) {
                    UIManager.setLookAndFeel(laf.getClassName());
                    UIManager.put("Table.showGrid", true);
                    UIManager.put("TextField.font", new Font("SansSerif", Font.BOLD, 10));
                    UIManager.put("TitledBorder.font", new Font("SansSerif", Font.BOLD, 10));
                    success = true;
                }
            }
            if (!success) {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            }
        } catch (Exception exc) {
            System.err.println("Error loading L&F: " + exc);
        }
        LablogMain.instance();
    }

    /**
	 * layout the GUI. 
	 * @param width - int: the width of the frame in pixel
	 * @param height - int: the frame's height 
	 */
    private void initGUI(int width, int height) {
        setBounds(0, 0, width, height);
        setVisible(true);
        JButton personsIconBtn = new JButton();
        personsIconBtn.setIcon(new ImageIcon(properties.getResource("iconPeople")));
        personsIconBtn.setActionCommand("mainPersonsIconBtn");
        personsIconBtn.setToolTipText("View/Edit personel (cntrl-p)");
        personsIconBtn.addActionListener(al);
        personsIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton subgroupIconBtn = new JButton();
        subgroupIconBtn.setIcon(new ImageIcon(properties.getResource("iconGroups")));
        subgroupIconBtn.setActionCommand("mainGroupsIconBtn");
        subgroupIconBtn.setToolTipText("View/Edit subgroups (cntrl-g)");
        subgroupIconBtn.addActionListener(al);
        subgroupIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton grantsIconBtn = new JButton();
        grantsIconBtn.setIcon(new ImageIcon(properties.getResource("iconGrants")));
        grantsIconBtn.setActionCommand("grantsIconBtn");
        grantsIconBtn.setToolTipText("manage grant proposals");
        grantsIconBtn.addActionListener(al);
        grantsIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton projectsIconBtn = new JButton();
        projectsIconBtn.setIcon(new ImageIcon(properties.getResource("iconProjects")));
        projectsIconBtn.setActionCommand("mainProjectsIconBtn");
        projectsIconBtn.setToolTipText("View/Edit projects");
        projectsIconBtn.addActionListener(al);
        projectsIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton dataIconBtn = new JButton();
        dataIconBtn.setIcon(new ImageIcon(properties.getResource("iconData")));
        dataIconBtn.setActionCommand("mainDataIconBtn");
        dataIconBtn.setToolTipText("manage/search data");
        dataIconBtn.addActionListener(al);
        dataIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton documentationIconBtn = new JButton();
        documentationIconBtn.setIcon(new ImageIcon(properties.getResource("iconJournal")));
        documentationIconBtn.setActionCommand("mainJournalIconBtn");
        documentationIconBtn.setToolTipText("the journal: project documentation, diary, manage and retrieve data, etc. (cntrl-j)");
        documentationIconBtn.addActionListener(al);
        documentationIconBtn.setPreferredSize(new Dimension(50, 50));
        connectIconBtn = new JButton();
        connectIconBtn.setIcon(new ImageIcon(properties.getResource("iconConnect")));
        connectIconBtn.setActionCommand("mainConnectIconBtn");
        connectIconBtn.setToolTipText("connect to database (cntrl-c)");
        connectIconBtn.addActionListener(al);
        connectIconBtn.setPreferredSize(new Dimension(50, 50));
        disconnectIconBtn = new JButton();
        disconnectIconBtn.setIcon(new ImageIcon(properties.getResource("iconDisconnect")));
        disconnectIconBtn.setActionCommand("mainDisconnectIconBtn");
        disconnectIconBtn.setToolTipText("disconnect from database (alt-c)");
        disconnectIconBtn.addActionListener(al);
        disconnectIconBtn.setEnabled(false);
        disconnectIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton setupsIconBtn = new JButton();
        setupsIconBtn.setIcon(new ImageIcon(properties.getResource("iconSetups")));
        setupsIconBtn.setActionCommand("mainSetupsIconBtn");
        setupsIconBtn.setToolTipText("add new setup/ edit existing setups");
        setupsIconBtn.addActionListener(al);
        setupsIconBtn.setPreferredSize(new Dimension(50, 50));
        JButton hardwareIconBtn = new JButton();
        hardwareIconBtn.setIcon(new ImageIcon(properties.getResource("iconItems")));
        hardwareIconBtn.setActionCommand("mainHardwareIconBtn");
        hardwareIconBtn.setToolTipText("add hardware items to the lab's inventory");
        hardwareIconBtn.addActionListener(al);
        hardwareIconBtn.setPreferredSize(new Dimension(50, 50));
        buttonsPanel = new JPanel();
        buttonsPanel.setPreferredSize(new Dimension(width, 60));
        buttonsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        FlowLayout fl = new FlowLayout();
        fl.setAlignment(FlowLayout.LEFT);
        buttonsPanel.setLayout(fl);
        buttonsPanel.add(connectIconBtn);
        buttonsPanel.add(disconnectIconBtn);
        buttonsPanel.add(personsIconBtn);
        buttonsPanel.add(subgroupIconBtn);
        buttonsPanel.add(grantsIconBtn);
        buttonsPanel.add(documentationIconBtn);
        desktopPane = new MyDesktopPane(properties.getResource("iconDesktop"));
        desktopPane.setPreferredSize(new Dimension(width - 200, height - 50));
        desktopPane.setBorder(BorderFactory.createBevelBorder(2));
        desktopPane.setBackground(Color.LIGHT_GRAY);
        desktopPane.setLayout(null);
        desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
        newsArea = new JTextArea();
        newsArea.setAutoscrolls(true);
        newsArea.setLineWrap(true);
        newsArea.setWrapStyleWord(true);
        newsArea.setEditable(true);
        newsArea.setFont(new Font("SansSerif", Font.BOLD, 9));
        JScrollPane newsScrollPane = new JScrollPane(newsArea);
        newsScrollPane.setPreferredSize(new Dimension(width, 60));
        newsScrollPane.setBorder(BorderFactory.createBevelBorder(1));
        newsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(desktopPane, BorderLayout.CENTER);
        cp.add(newsScrollPane, BorderLayout.SOUTH);
        cp.add(buttonsPanel, BorderLayout.NORTH);
        cp.validate();
    }

    /**
	 * Creates the menu bar displayed on top of the frame.
	 * @return - JMenuBar: the menu bar
	 */
    protected JMenuBar initMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setFocusCycleRoot(true);
        menuBar.setFocusTraversalKeysEnabled(true);
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        JMenuItem fileQuitItem = new JMenuItem("quit");
        fileQuitItem.setMnemonic(KeyEvent.VK_Q);
        fileQuitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        fileQuitItem.setActionCommand("quit");
        fileQuitItem.addActionListener(al);
        JMenuItem fileConnectItem = new JMenuItem("connect");
        fileConnectItem.setToolTipText("connect to database");
        fileConnectItem.setMnemonic(KeyEvent.VK_C);
        fileConnectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        fileConnectItem.setActionCommand("connect");
        fileConnectItem.addActionListener(al);
        JMenuItem fileDisconnectItem = new JMenuItem("disconnect");
        fileDisconnectItem.setToolTipText("disconnect from database");
        fileDisconnectItem.setMnemonic(KeyEvent.VK_C);
        fileDisconnectItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        fileDisconnectItem.setActionCommand("disconnect");
        fileDisconnectItem.addActionListener(al);
        fileMenu.add(fileConnectItem);
        fileMenu.add(fileDisconnectItem);
        fileMenu.addSeparator();
        fileMenu.add(fileQuitItem);
        JMenu personsMenu = new JMenu("Persons");
        personsMenu.setMnemonic(KeyEvent.VK_P);
        JMenuItem personsRegistrationItem = new JMenuItem("add/view/edit");
        personsRegistrationItem.setToolTipText("view personal details, enter new persons, register users on the server");
        personsRegistrationItem.setMnemonic(KeyEvent.VK_P);
        personsRegistrationItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, ActionEvent.CTRL_MASK));
        personsRegistrationItem.setActionCommand("persons");
        personsRegistrationItem.addActionListener(al);
        JMenuItem personsPasswordItem = new JMenuItem("change password");
        personsPasswordItem.setToolTipText("change your password");
        personsPasswordItem.setActionCommand("passwordChange");
        personsPasswordItem.addActionListener(al);
        personsMenu.add(personsRegistrationItem);
        personsMenu.add(personsPasswordItem);
        JMenu groupsMenu = new JMenu("Groups");
        JMenuItem groupsMenuItem = new JMenuItem("add/view/edit");
        groupsMenuItem.setActionCommand("groups");
        groupsMenuItem.setToolTipText("create new subgroups, view existing groups, modify subgroup descriptions");
        groupsMenuItem.setMnemonic(KeyEvent.VK_G);
        groupsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, ActionEvent.CTRL_MASK));
        groupsMenuItem.addActionListener(al);
        groupsMenu.add(groupsMenuItem);
        JMenuItem journalItem = new JMenuItem("add/view/edit");
        journalItem.setToolTipText("add new documents to your lab-journal, view existing documents, modify documents");
        journalItem.setMnemonic(KeyEvent.VK_J);
        journalItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.CTRL_MASK));
        journalItem.setActionCommand("journal");
        journalItem.addActionListener(al);
        JMenu journalMenu = new JMenu("Journal");
        journalMenu.setMnemonic(KeyEvent.VK_J);
        journalMenu.add(journalItem);
        JMenuItem dataFinderMenuItem = new JMenuItem("retrieve data");
        dataFinderMenuItem.setToolTipText("open the dialog to retrieve data from the database");
        dataFinderMenuItem.setMnemonic(KeyEvent.VK_F);
        dataFinderMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.CTRL_MASK));
        dataFinderMenuItem.setActionCommand("dataFinder");
        dataFinderMenuItem.addActionListener(al);
        JMenu dataMenu = new JMenu("Data");
        dataMenu.add(dataFinderMenuItem);
        JMenuItem helpHelpItem = new JMenuItem("Help");
        helpHelpItem.setActionCommand("helpHelpItem");
        helpHelpItem.setToolTipText("create new subgroups, view existing groups, modify subgroup descriptions");
        helpHelpItem.setMnemonic(KeyEvent.VK_F1);
        helpHelpItem.setEnabled(false);
        helpHelpItem.addActionListener(al);
        JMenuItem helpPreferencesItem = new JMenuItem("Preferences");
        helpPreferencesItem.setActionCommand("helpPreferencesItem");
        helpPreferencesItem.setToolTipText("Set the preferences");
        helpPreferencesItem.addActionListener(al);
        JMenuItem helpAboutItem = new JMenuItem("About");
        helpAboutItem.setActionCommand("helpAboutItem");
        helpAboutItem.setToolTipText("about the LabLog");
        helpAboutItem.addActionListener(al);
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic(KeyEvent.VK_H);
        helpMenu.add(helpHelpItem);
        helpMenu.addSeparator();
        helpMenu.add(helpAboutItem);
        helpMenu.add(helpPreferencesItem);
        menuBar.add(fileMenu);
        menuBar.add(personsMenu);
        menuBar.add(groupsMenu);
        menuBar.add(journalMenu);
        menuBar.add(dataMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    public void setConnection(DBConnectionDetails cDetails) {
        if (cDetails != null) {
            connDetails = cDetails;
            if (connectToDatabase()) {
                DBAccess db = new DBAccess(this, conn);
                Object version = db.getColumnValue("database_version", "version", "version is not null", false);
                if (version == null) {
                    JOptionPane.showMessageDialog(this, "The database version is older than required by this version of LabLog.\n" + "Some things may not work. Either update your database or use an earlier version of LabLog." + "\n(http://lablog.sourceforge.net)", "Unsupported database version...", JOptionPane.WARNING_MESSAGE);
                } else if (Double.valueOf(version.toString()) < LablogMain.version) {
                    JOptionPane.showMessageDialog(this, "The database version is older than required by this version of LabLog.\n" + "Some things may not work. Either update your database or use an earlier version of LabLog." + "\n(http://lablog.sourceforge.net)", "Unsupported database version...", JOptionPane.WARNING_MESSAGE);
                } else if (Double.valueOf(version.toString()) > LablogMain.version) {
                    JOptionPane.showMessageDialog(this, "The database version is newer than supported by this version of LabLog.\n" + "Some things may not work. Get a newer version of LabLog.\n" + "(http://lablog.sourceforge.net)", "Unsupported database version...", JOptionPane.WARNING_MESSAGE);
                }
                properties.setProperty("lastDB", connDetails.getDbName());
                properties.setProperty("lastServer", connDetails.getServerURL());
                if (!properties.hasProperty("Server")) {
                    properties.setProperty("Server", connDetails.getServerURL());
                }
                if (!properties.hasProperty("Database")) {
                    properties.setProperty("Database", connDetails.getDbName());
                }
                if (!properties.getProperty("Server").contains(connDetails.getServerURL())) {
                    String temp = properties.getProperty("Server") + "," + connDetails.getServerURL();
                    properties.setProperty("Server", temp);
                }
                if (!properties.getProperty("Database").contains(connDetails.getDbName())) {
                    String temp = properties.getProperty("Database") + "," + connDetails.getDbName();
                    properties.setProperty("Database", temp);
                }
                properties.store();
            }
        } else connected = false;
    }

    public boolean connectToDatabase() {
        if (connDetails != null) {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));
            if (pswd == null) {
                PasswordDialog pswdDlg = new PasswordDialog(this);
                this.pswd = pswdDlg.getPassword();
                pswdDlg.dispose();
            }
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
                newsArea.append("driver registered\n");
            } catch (Exception ex) {
                writeNews("Exception: " + ex.getMessage());
                writeNews(ex.toString());
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return false;
            }
            try {
                DatabaseHelper.configure(connDetails.getServerURL(), Integer.parseInt(connDetails.getPort()), connDetails.getDbName(), connDetails.getUserName(), pswd);
                conn = DriverManager.getConnection("jdbc:mysql://" + connDetails.getServerURL() + "/" + connDetails.getDbName() + "?user=" + connDetails.getUserName() + "&password=" + pswd);
                newsArea.append("connection established\n");
                this.setTitle(properties.getProperty("Title") + " - connected to " + connDetails.getDbName() + "@" + connDetails.getServerURL());
                connected = true;
                pswd = null;
                this.connectIconBtn.setEnabled(false);
                this.disconnectIconBtn.setEnabled(true);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return true;
            } catch (SQLException ex) {
                writeNews("SQLException: " + ex.getMessage());
                writeNews("SQLState: " + ex.getSQLState());
                writeNews("VendorError: " + ex.getErrorCode());
                connDetails = null;
                pswd = null;
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                return false;
            }
        } else {
            loginBtnPressed();
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            return false;
        }
    }

    /**
	 * Disconnect from the database. Function handles disconnection and creates a warning message if 
	 * some windows are still open.
	 * @return {@link Boolean} true if operation succeeded, flase otherwise.
	 */
    public boolean disconnectBtnPressed() {
        if (connected) {
            if (desktopPane.getAllFrames().length != 0) {
                int n = JOptionPane.showConfirmDialog(this, "Some windows are still open. Disconnect anyway? (unsubmitted information is lost)", "Some windows are still open!", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                if (n == JOptionPane.NO_OPTION) return false;
            }
            try {
                DatabaseHelper.instance().diconnect();
                conn.close();
                newsArea.append("connection terminated\n");
                this.setTitle(properties.getProperty("Title"));
                disconnectIconBtn.setEnabled(false);
                connectIconBtn.setEnabled(true);
                connected = false;
                return true;
            } catch (SQLException ex) {
                this.writeNews("SQLException: " + ex.getMessage());
                this.writeNews("SQLState: " + ex.getSQLState());
                this.writeNews("VendorError: " + ex.getErrorCode());
            }
        }
        return true;
    }

    public void personsBtnPressed() {
        if (connected) {
            int index = isAlreadyOpened(Persons.class.toString());
            if (index == -1) {
                Persons personsIF = new Persons();
                desktopPane.add(personsIF);
                personsIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                personsIF.moveToFront();
                desktopPane.revalidate();
            } else {
                JInternalFrame[] tmp = desktopPane.getAllFrames();
                try {
                    tmp[index].setIcon(false);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                desktopPane.revalidate();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    public void loginBtnPressed() {
        try {
            server = properties.hasProperty("Server") ? properties.getProperty("Server") : "";
            database = properties.hasProperty("Database") ? properties.getProperty("Database") : "";
            lastServer = properties.hasProperty("lastServer") ? properties.getProperty("lastServer") : "";
            lastDb = properties.hasProperty("lastDB") ? properties.getProperty("lastDB") : null;
        } catch (Exception e) {
            server = "localhost";
            database = "";
        }
        if (!connected) {
            int index = isAlreadyOpened(LoginWindow.class.toString());
            if (index == -1) {
                LoginWindow loginIF = new LoginWindow(this, server, database, lastServer, lastDb);
                desktopPane.add(loginIF);
                loginIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                loginIF.moveToFront();
                desktopPane.revalidate();
                loginIF.setFocus();
            } else {
                try {
                    frames[index].setIcon(false);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private void grantsBtnPressed() {
        if (connected) {
            int index = isAlreadyOpened(GrantManager.class.toString());
            if (index == -1) {
                GrantManager grantManagerIF = new GrantManager(conn, desktopPane, this.getLocationOnScreen());
                desktopPane.add(grantManagerIF);
                grantManagerIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                grantManagerIF.moveToFront();
                desktopPane.revalidate();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    public void findDataBtnPressed() {
        if (connected) {
            int index = isAlreadyOpened(RetrieveData.class.toString());
            if (index == -1) {
                RetrieveData dataFinderIF = new RetrieveData(conn);
                desktopPane.add(dataFinderIF);
                dataFinderIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                dataFinderIF.moveToFront();
                desktopPane.revalidate();
            } else {
                try {
                    frames[index].setIcon(false);
                } catch (Exception e) {
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    /**
	 * Function handles pressing the Data button. 
	 */
    public void importDataBtnPressed() {
        if (connected) {
            int index = isAlreadyOpened(AddDatasets.class.toString());
            if (index == -1) {
                AddDatasets dataImportIF = new AddDatasets(conn);
                desktopPane.add(dataImportIF);
                dataImportIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                dataImportIF.moveToFront();
                desktopPane.revalidate();
            } else {
                try {
                    frames[index].setIcon(false);
                } catch (Exception e) {
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    /**
	 * Function handles pressing the subgroups button. 
	 */
    public void subgroupsBtnPressed() {
        if (connected) {
            int index = isAlreadyOpened(Subgroups.class.toString());
            if (index == -1) {
                Subgroups subgroupsIF = new Subgroups(conn);
                desktopPane.add(subgroupsIF);
                subgroupsIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                subgroupsIF.moveToFront();
                desktopPane.revalidate();
            } else {
                try {
                    frames[index].setIcon(false);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    /**
	 * Function handles pressing the Journal Button.
	 */
    public void journalBtnPressed() {
        if (connected) {
            int index = isAlreadyOpened(Journal.class.toString());
            if (index == -1) {
                setCursor(new Cursor(Cursor.WAIT_CURSOR));
                Journal documentationIF = new Journal(conn, desktopPane);
                desktopPane.add(documentationIF);
                documentationIF.setLocation((desktopPane.getComponentCount() - 1) * 30, (desktopPane.getComponentCount() - 1) * 30);
                documentationIF.moveToFront();
                desktopPane.revalidate();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            } else {
                frames[index].moveToFront();
                try {
                    frames[index].setIcon(false);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    /**
	 * Function handles the pressing of the change Password MenuItem.
	 */
    public void changePasswordItemPressed() {
        if (connected) {
            new ChangePswdDialog(conn);
        } else {
            JOptionPane.showMessageDialog(this, "Establish connection and try again!", "No connection to database!", JOptionPane.ERROR_MESSAGE);
            loginBtnPressed();
        }
    }

    /**
	 * Function checks whether a frame of a respective class is already opened. 
	 * @param className - String: the name of the class to check.
	 * @return - integer: the index of the first frame matching the requested class.
	 */
    private int isAlreadyOpened(String className) {
        int index = -1;
        frames = desktopPane.getAllFrames();
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].getClass().toString().equals(className)) {
                index = i;
                break;
            }
        }
        return index;
    }

    /**
	 * Writes the passed string to the main window's textArea.
	 * @param news - String: the message to be displayed
	 */
    public void writeNews(String news) {
        if (!news.endsWith(String.valueOf('\n'))) news = news + '\n';
        this.newsArea.append(news);
    }

    /**
	 * Show the about window.
	 *
	 */
    private void showAbout() {
        new AboutWindow(properties.getProperty("aboutText"), new ImageIcon(properties.getResource("iconWindow16")));
    }

    /**
	 * Show the preferences windows.
	 */
    private void showPreferences() {
        new PreferencesWindow();
    }

    private void closeWindow() {
        if (connected) {
            if (disconnectBtnPressed()) {
                System.exit(NORMAL);
            }
        } else {
            System.exit(NORMAL);
        }
    }

    @SuppressWarnings("serial")
    class MyDesktopPane extends JDesktopPane {

        Image img;

        public MyDesktopPane(URL imageURL) {
            try {
                img = javax.imageio.ImageIO.read(imageURL);
            } catch (Exception e) {
            }
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (img != null) g.drawImage(img, (int) (this.getWidth() - 750) / 2, (int) (this.getHeight() - 410) / 2, 750, 410, this); else g.drawString("Image not found", 50, 50);
        }
    }

    /**
	 * The ActionListener Subclass which merely filters the incoming ActionCommands and 
	 * distributes to the respective functions.
	 */
    class LabDBMainActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("dataFinder")) {
                findDataBtnPressed();
            } else if (e.getActionCommand().equals("dataImport")) {
                importDataBtnPressed();
            } else if (e.getActionCommand().equals("journal")) {
                journalBtnPressed();
            } else if (e.getActionCommand().equals("mainJournalIconBtn")) {
                journalBtnPressed();
            } else if (e.getActionCommand().equals("disconnect")) {
                disconnectBtnPressed();
            } else if (e.getActionCommand().equals("mainDisconnectIconBtn")) {
                disconnectBtnPressed();
            } else if (e.getActionCommand().equals("connect")) {
                connectToDatabase();
            } else if (e.getActionCommand().equals("mainConnectIconBtn")) {
                connectToDatabase();
            } else if (e.getActionCommand().equals("quit")) {
                closeWindow();
            } else if (e.getActionCommand().equals("persons")) {
                personsBtnPressed();
            } else if (e.getActionCommand().equals("mainPersonsIconBtn")) {
                personsBtnPressed();
            } else if (e.getActionCommand().equals("passwordChange")) {
                changePasswordItemPressed();
            } else if (e.getActionCommand().equals("mainGroupsIconBtn")) {
                subgroupsBtnPressed();
            } else if (e.getActionCommand().equals("groups")) {
                subgroupsBtnPressed();
            } else if (e.getActionCommand().equals("grantsIconBtn")) {
                grantsBtnPressed();
            } else if (e.getActionCommand().equals("helpAboutItem")) {
                showAbout();
            } else if (e.getActionCommand().equals("helpPreferencesItem")) {
                showPreferences();
            }
        }
    }

    @SuppressWarnings("serial")
    class AboutWindow extends JDialog implements ActionListener {

        private JDialog w;

        private String text;

        private JEditorPane aboutText;

        private JButton okBtn;

        public AboutWindow(String text, ImageIcon image) {
            w = new JDialog();
            w.setModal(true);
            w.setTitle("About...");
            w.setIconImage(image.getImage());
            this.text = text;
            w.setSize(320, 350);
            w.setLocation(500, 350);
            setGUI();
            w.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("closeBtn")) {
                closeBtnPressed();
            }
        }

        private void setGUI() {
            Container cp = w.getContentPane();
            cp.setLayout(new BorderLayout());
            aboutText = new JEditorPane();
            aboutText.setBackground(Color.white);
            aboutText.setContentType("text/html");
            aboutText.setFont(new Font("Serif", Font.PLAIN, 12));
            aboutText.setCaretPosition(0);
            aboutText.setEditable(false);
            aboutText.setFocusable(false);
            aboutText.setText(text);
            JScrollPane pane = new JScrollPane(aboutText);
            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.add(pane, BorderLayout.CENTER);
            okBtn = new JButton("close");
            okBtn.setToolTipText("close this window");
            okBtn.setActionCommand("closeBtn");
            okBtn.addActionListener(this);
            JPanel btnPanel = new JPanel(new GridLayout(1, 2, 2, 2));
            btnPanel.add(okBtn);
            cp.add(textPanel, BorderLayout.CENTER);
            cp.add(btnPanel, BorderLayout.SOUTH);
        }

        private void closeBtnPressed() {
            w.dispose();
        }
    }

    /**
	 * 
	 * Preferences Window subclass.
	 *
	 */
    @SuppressWarnings("serial")
    class PreferencesWindow extends JDialog implements ActionListener {

        private JDialog w;

        private PropertiesManager properties;

        private JButton okBtn, cancelBtn;

        private JTable odmlTable, iconTable, mappingTable;

        public int state = -1;

        public static final int OK = 0, CANCEL = -1;

        /**
		 * 
		 * @param primaryVoc
		 * @param secondaryVoc
		 * @param primaySchema
		 * @param secondarySchema
		 * @param image
		 */
        public PreferencesWindow() {
            w = new JDialog();
            w.setModal(true);
            w.setTitle("Preferences...");
            this.properties = PropertiesManager.instance();
            w.setSize(400, 350);
            w.setLocation(500, 350);
            setGUI();
            w.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equalsIgnoreCase("okBtn")) {
                okBtnPressed();
            } else if (e.getActionCommand().equalsIgnoreCase("cancelBtn")) {
                cancelBtnPressed();
            }
        }

        private void setGUI() {
            Container cp = w.getContentPane();
            cp.setLayout(new BorderLayout());
            iconTable = new JTable(new DefaultTableModel());
            DefaultTableModel iconModel = createModel("icon");
            iconTable.setModel(iconModel);
            iconTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            iconTable.getColumn("property").setMaxWidth(150);
            iconTable.getColumn("property").setWidth(150);
            odmlTable = new JTable(new DefaultTableModel());
            DefaultTableModel odmlModel = createModel("odml");
            odmlTable.setModel(odmlModel);
            odmlTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            odmlTable.getColumn("property").setMaxWidth(150);
            odmlTable.getColumn("property").setWidth(150);
            mappingTable = new JTable();
            DefaultTableModel m = createMappingTable();
            mappingTable.setModel(m);
            mappingTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            JTabbedPane tabPane = new JTabbedPane();
            tabPane.add("icons", new JScrollPane(iconTable));
            tabPane.setToolTipTextAt(0, "edit the icon references to use your own icons");
            tabPane.add("odML", new JScrollPane(odmlTable));
            tabPane.setToolTipTextAt(1, "definitions of odML locations etc");
            tabPane.add("odML mapping ", new JScrollPane(mappingTable));
            tabPane.setToolTipTextAt(2, "mapping of odML properties to database entries");
            okBtn = new JButton("ok");
            okBtn.setToolTipText("Accept changes and close window.");
            okBtn.setActionCommand("okBtn");
            okBtn.addActionListener(this);
            cancelBtn = new JButton("cancel");
            cancelBtn.setToolTipText("Discard changes and close window.");
            cancelBtn.setActionCommand("cancelBtn");
            cancelBtn.addActionListener(this);
            JPanel btnPanel = new JPanel(new GridLayout(1, 2, 2, 2));
            btnPanel.add(okBtn);
            btnPanel.add(cancelBtn);
            cp.add(tabPane, BorderLayout.CENTER);
            cp.add(btnPanel, BorderLayout.SOUTH);
        }

        private DefaultTableModel createMappingTable() {
            Object[] colNames = { "odML section", "odML property" };
            return new DefaultTableModel();
        }

        private MyTableModel createModel(String prefix) {
            MyTableModel m = new MyTableModel();
            Object[] colNames = { "property", "value" };
            m.setColumnIdentifiers(colNames);
            Object[] keys = properties.keySet().toArray();
            for (int i = 0; i < keys.length; i++) {
                if (keys[i].toString().startsWith(prefix)) {
                    Object[] temp = new Object[2];
                    temp[0] = keys[i];
                    temp[1] = properties.getProperty(keys[i].toString());
                    m.addRow(temp);
                }
            }
            return m;
        }

        /**
		 * 
		 */
        private void okBtnPressed() {
            MyTableModel m = (MyTableModel) odmlTable.getModel();
            for (int i = 0; i < m.getRowCount(); i++) {
                properties.setProperty(m.getValueAt(i, 0).toString(), m.getValueAt(i, 1).toString());
            }
            MyTableModel n = (MyTableModel) iconTable.getModel();
            for (int i = 0; i < n.getRowCount(); i++) {
                properties.setProperty(n.getValueAt(i, 0).toString(), n.getValueAt(i, 1).toString());
            }
            properties.finalize();
            w.setVisible(false);
        }

        /**
		 * 
		 */
        private void cancelBtnPressed() {
            state = CANCEL;
            w.setVisible(false);
        }

        private class MyTableModel extends DefaultTableModel {

            @Override
            public boolean isCellEditable(int row, int column) {
                return (column == 1);
            }
        }
    }
}
