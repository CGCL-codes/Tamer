package hoipolloi;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.*;
import java.util.*;
import java.text.SimpleDateFormat;
import com.pagosoft.plaf.*;
import com.pagosoft.plaf.themes.*;
import javax.swing.event.*;

/**
 * Draws the main application interface.
 *
 * @author  Brandon Buck
 * @author  Brandon Tanner
 * @version 0.18b (Dec 29, 2008)
 * @since   November 10, 2006
 */
public class MainMenu extends JFrame implements ActionListener, KeyEventDispatcher {

    private JPanel contentPane;

    private JPanel profileListPane;

    private JPanel listPane;

    private JPanel filterListPanel;

    private WatermarkPanel splitPanel;

    private MainMenu THIS;

    /** The Properties File */
    private Properties propFile;

    /** Contact Info Table */
    private JTable ctnTable;

    /** The current person being viewed or edited */
    private Person currentPerson;

    /** The Version of Hoi Polloi */
    private String hpVersion;

    private static final PgsTheme GRAY = ThemeFactory.createTheme("Gray", new Color(0x7997D1), new Color(0xABABAB), Color.GRAY);

    private static final PgsTheme YELLOW = ThemeFactory.createTheme("Yellow", new Color(0xCCAA53), new Color(0xABABAB), Color.BLACK);

    private static final PgsTheme GOLD = ThemeFactory.createTheme("Gold", new Color(0xFFDB29));

    private static final VistaTheme DEFAULT = new VistaTheme();

    private static final SilverTheme SILVER = new SilverTheme();

    private static final CharcoalTheme CHARCOAL = new CharcoalTheme();

    private static final RubyTheme RUBY = new RubyTheme();

    private static final DarudeTheme DARUDE = new DarudeTheme();

    private JRadioButtonMenuItem grayTheme;

    private JRadioButtonMenuItem yellowTheme;

    private JRadioButtonMenuItem rubyTheme;

    private JRadioButtonMenuItem goldTheme;

    private JRadioButtonMenuItem defaultTheme;

    private JRadioButtonMenuItem silverTheme;

    private JRadioButtonMenuItem charcoalTheme;

    private JRadioButtonMenuItem darudeTheme;

    private JRadioButtonMenuItem metalTheme;

    private JRadioButtonMenuItem oceanTheme;

    private JButton quickAddButton;

    private JButton addIndButton;

    private JButton addFamButton;

    private JButton addBusButton;

    private JButton cancelButton;

    private JButton btnAddContact;

    private JButton btnDelContact;

    private JButton btnDelProfile;

    private JButton btnUpdateProfile;

    private JScrollPane filterListScrollPane;

    private TrayIcon hpTrayIcon;

    private boolean editing = false;

    /** Creates a new instance of MainMenu */
    public MainMenu() {
        THIS = this;
        this.currentPerson = null;
        ImageIcon icon = new ImageIcon(getClass().getClassLoader().getResource("hoipolloi.png"));
        this.setTitle("Hoi Polloi");
        this.setIconImage(icon.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addWindowListener(new HPWindowListener(this));
        this.setJMenuBar(this.getHPMenuBar());
        this.setSize(1080, 600);
        this.setHPLayout();
        this.initializeButtons();
        this.getContentPane().add(splitPanel);
        this.setUpTrayIcon();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
        loadPropertyFile();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((int) (screenSize.getWidth() / 2) - (getWidth() / 2), (int) (screenSize.getHeight() / 2) - (getHeight() / 2));
        showPeopleList(DBHPInterface.getListOfPeopleByLastName());
        int temp = DBHPInterface.getBirthdaysThisMonth().size();
        if (temp > 0) {
            if (temp > 1) this.sysTrayMessage("Birthday Notice", "You have " + temp + " contacts with Birthdays this Month!", TrayIcon.MessageType.INFO); else this.sysTrayMessage("Birthday Notice", "You have 1 contact with a Birthday this Month!", TrayIcon.MessageType.INFO);
        }
        this.setVisible(true);
    }

    /** Creates and sets up the SystemTray Icon if the function is supported.
     * Creates the System tray icon adding in any necessary parts to it provided
     * that SystemTray.isSupported() returns true. If System Tray functionality
     * is not supported then it sets hpIconTray to null.
     *
     * ** NOTE **
     * MainMenu features two helper methods to set the TrayIcon tooltip and to
     * display messages, they both test to make sure the SystemTray is supported
     * and check to see if hpTrayIcon is not null. Use them instead of accessing
     * hpTrayIcon directly.
     */
    private void setUpTrayIcon() {
        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();
            Image sysTrayIcon = this.getIconImage();
            Dimension sysTrayIconSize = tray.getTrayIconSize();
            sysTrayIcon = sysTrayIcon.getScaledInstance((int) (sysTrayIconSize.getWidth()), (int) (sysTrayIconSize.getHeight()), Image.SCALE_AREA_AVERAGING);
            this.hpTrayIcon = new TrayIcon(sysTrayIcon);
            this.hpTrayIcon.addMouseListener(new TrayMouseListener(this));
            this.hpTrayIcon.setPopupMenu(this.getSysTrayMenu());
            try {
                tray.add(this.hpTrayIcon);
            } catch (Exception e) {
                Debug.print("Error with SystemTray functionality, exiting app.");
                System.exit(0);
            }
        } else hpTrayIcon = null;
    }

    /** Calls the TrayIcon displayMessage() method and passes in the given fields.
     * This method adds a buffer between MainMenu and it's TrayIcon so that the
     * coder doesn't to bother with double checking to make sure the System Tray
     * is supported on the OS or that the hpTrayIcon was even initialized.
     * @param caption The caption to give the displayed message.
     * @param message The message to included on the displayed box.
     * @param type The type of message to display (changes icon)
     */
    public void sysTrayMessage(String caption, String message, TrayIcon.MessageType type) {
        if (SystemTray.isSupported() && hpTrayIcon != null) {
            hpTrayIcon.displayMessage(caption, message, type);
        }
    }

    /** Calls the TrayIcon setToolTip() method and passes in the given field.
     * This method adds a buffer between MainMenu and it's TrayIcon so that the
     * coder doesn't to bother with double checking to make sure the System Tray
     * is supported on the OS or that the hpTrayIcon was even initialized.
     * @param tooltip The new tooltip to add to the TrayIcon
     */
    public void sysTrayTooltip(String tooltip) {
        if (SystemTray.isSupported() && hpTrayIcon != null) {
            hpTrayIcon.setToolTip(tooltip);
        }
    }

    /** Creates the PopupMenu used by the TrayIcon and adds all the MenuItems to it.
     * Creates the PopupMenu for the TrayIcon and sets up all the menu items for
     * it, then returns the completed menu.
     * @return Creates the pop up menu for the TrayIcon to use.
     */
    public PopupMenu getSysTrayMenu() {
        PopupMenu popupMenu = new PopupMenu();
        MenuItem quickAddItem = new MenuItem("Quick Add");
        quickAddItem.addActionListener(new MenuAction("Quick Add", this));
        MenuItem addIndItem = new MenuItem("Add Person");
        addIndItem.addActionListener(new MenuAction("Add Individual", this));
        MenuItem addFamItem = new MenuItem("Add Family");
        addFamItem.addActionListener(new MenuAction("Add Family", this));
        MenuItem addCorpItem = new MenuItem("Add Business");
        addCorpItem.addActionListener(new MenuAction("Add Business", this));
        MenuItem searchItem = new MenuItem("Search");
        searchItem.addActionListener(new MenuAction("Search", this));
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.addActionListener(new MenuAction("About", this));
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(new MenuAction("Exit", this));
        popupMenu.add(quickAddItem);
        popupMenu.addSeparator();
        popupMenu.add(addIndItem);
        popupMenu.add(addFamItem);
        popupMenu.add(addCorpItem);
        popupMenu.addSeparator();
        popupMenu.add(searchItem);
        popupMenu.add(aboutItem);
        popupMenu.addSeparator();
        popupMenu.add(exitItem);
        return popupMenu;
    }

    /** Initializes all the buttons and adds action listeners to them.
     * Initializes the buttons used by Hoi Polloi and then adds the proper
     * actions listener to them.
     */
    private void initializeButtons() {
        addIndButton = new JButton("Add Individual");
        addIndButton.addActionListener(this);
        addFamButton = new JButton("Add Family");
        addFamButton.addActionListener(this);
        addBusButton = new JButton("Add Business");
        addBusButton.addActionListener(this);
        quickAddButton = new JButton("Add");
        quickAddButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
    }

    /** Creates the panels and lays them out properly for all the visual items in Hoi Polli
     * Creates the WatermarkPanel and adds the Content and sort panes in their
     * proper place. Also creates the split pane for the sorted list and filter
     * tree.
     */
    private void setHPLayout() {
        contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.BLACK));
        filterListScrollPane = this.getFilterListTree();
        filterListPanel = new JPanel();
        filterListPanel.setLayout(new GridLayout(1, 1));
        filterListPanel.add(filterListScrollPane);
        filterListPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
        listPane = new JPanel();
        listPane.setLayout(new GridLayout(1, 1));
        listPane.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.BLACK));
        profileListPane = new JPanel();
        profileListPane.setLayout(new GridLayout(2, 1));
        profileListPane.add(filterListPanel);
        profileListPane.add(listPane);
        profileListPane.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 3, Color.BLACK));
        splitPanel = new WatermarkPanel(JSplitPane.HORIZONTAL_SPLIT, profileListPane, contentPane, "wtlogo.png");
        splitPanel.setDividerSize(1);
        splitPanel.setDividerLocation(180);
        splitPanel.setOpaque(false);
        splitPanel.setOneTouchExpandable(false);
    }

    /** Gets the menu bar for Hoi Polloi and returns it.
     * Builds the menu bar for Hoi Polloi using all the helper methods laid out
     * to return each menu.
     * @return The JMenuBar for the Hoi Polloi Application
     */
    private JMenuBar getHPMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(getFileMenu());
        menuBar.add(getEditMenu());
        menuBar.add(getSearchMenu());
        menuBar.add(getInfoMenu());
        menuBar.add(Box.createHorizontalGlue());
        menuBar.add(getHelpMenu());
        return menuBar;
    }

    /** Builds this menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     * @return The completed JMenu for the JMenuBar
     */
    private JMenu getHelpMenu() {
        ImageIcon helpIcon = new ImageIcon(getClass().getClassLoader().getResource("help.png"));
        ImageIcon infoIcon = new ImageIcon(getClass().getClassLoader().getResource("information.png"));
        JMenu helpMenu = new JMenu("Help");
        helpMenu.setMnemonic('H');
        JMenuItem aboutItem = new JMenuItem(new MenuAction("About", this));
        aboutItem.setIcon(infoIcon);
        aboutItem.setMnemonic('A');
        JMenuItem helpItem = new JMenuItem(new MenuAction("Help", this));
        helpItem.setIcon(helpIcon);
        helpItem.setMnemonic('H');
        helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpMenu.add(aboutItem);
        helpMenu.addSeparator();
        helpMenu.add(helpItem);
        return helpMenu;
    }

    /** 
     * Builds the Info menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     *
     * @return The completed Info menu for the JMenuBar
     */
    private JMenu getInfoMenu() {
        ImageIcon reportIcon = new ImageIcon(getClass().getClassLoader().getResource("report.png"));
        ImageIcon timeIcon = new ImageIcon(getClass().getClassLoader().getResource("time.png"));
        JMenu infoMenu = new JMenu("Info");
        infoMenu.setMnemonic('I');
        JMenuItem statItem = new JMenuItem(new MenuAction("Statistics", this));
        statItem.setIcon(reportIcon);
        statItem.setMnemonic('S');
        JMenuItem timeItem = new JMenuItem(new MenuAction("World Clocks", this));
        timeItem.setIcon(timeIcon);
        timeItem.setMnemonic('W');
        infoMenu.add(statItem);
        infoMenu.add(timeItem);
        return infoMenu;
    }

    /** Builds this menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     * @return The completed JMenu for the JMenuBar
     */
    private JMenu getSearchMenu() {
        ImageIcon searchIcon = new ImageIcon(getClass().getClassLoader().getResource("magnifier.png"));
        JMenu searchMenu = new JMenu("Search");
        searchMenu.setMnemonic('S');
        JMenuItem searchItem = new JMenuItem(new MenuAction("Search", this));
        searchItem.setIcon(searchIcon);
        searchItem.setMnemonic('e');
        searchItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK));
        JMenuItem activeSearchItem = new JMenuItem(new MenuAction("Active Search", this));
        activeSearchItem.setIcon(searchIcon);
        activeSearchItem.setMnemonic('A');
        activeSearchItem.setAccelerator(KeyStroke.getKeyStroke('F', InputEvent.CTRL_DOWN_MASK + InputEvent.ALT_DOWN_MASK));
        searchMenu.add(searchItem);
        searchMenu.add(activeSearchItem);
        return searchMenu;
    }

    /** Builds this menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     * @return The completed JMenu for the JMenuBar
     */
    private JMenu getEditMenu() {
        ImageIcon settingsIcon = new ImageIcon(getClass().getClassLoader().getResource("settings.png"));
        ImageIcon editProfileIcon = new ImageIcon(getClass().getClassLoader().getResource("user_edit.png"));
        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic('E');
        JMenuItem editProfileItem = new JMenuItem(new MenuAction("Edit Profile", this));
        editProfileItem.setIcon(editProfileIcon);
        editProfileItem.setMnemonic('d');
        editProfileItem.setAccelerator(KeyStroke.getKeyStroke('D', InputEvent.CTRL_DOWN_MASK));
        JMenuItem settingsItem = new JMenuItem(new MenuAction("Settings", this));
        settingsItem.setIcon(settingsIcon);
        settingsItem.setMnemonic('S');
        editMenu.add(editProfileItem);
        editMenu.addSeparator();
        editMenu.add(settingsItem);
        editMenu.add(getThemeMenu());
        return editMenu;
    }

    /** Builds this menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     * @return The completed JMenu for the JMenuBar
     */
    private JMenu getThemeMenu() {
        ImageIcon changeThemeIcon = new ImageIcon(getClass().getClassLoader().getResource("color_wheel.png"));
        JMenu themeMenu = new JMenu("Themes");
        themeMenu.setMnemonic('T');
        themeMenu.setIcon(changeThemeIcon);
        JMenuItem dummyItem = new JMenuItem("Choose A Theme...");
        dummyItem.setFont(new Font(themeMenu.getFont().getFamily(), Font.ITALIC, themeMenu.getFont().getSize()));
        dummyItem.setEnabled(false);
        setThemeButtonGroup();
        themeMenu.add(dummyItem);
        themeMenu.addSeparator();
        themeMenu.add(defaultTheme);
        themeMenu.add(metalTheme);
        themeMenu.add(oceanTheme);
        themeMenu.add(charcoalTheme);
        themeMenu.add(goldTheme);
        themeMenu.add(silverTheme);
        themeMenu.add(rubyTheme);
        themeMenu.add(yellowTheme);
        themeMenu.add(grayTheme);
        themeMenu.add(darudeTheme);
        return themeMenu;
    }

    /** Initializes the theme variables and then adds them to a ButtonGroup
     * Initializes the Theme radio button menu items and then adds them into
     * a button group so that only one can be selected at a time.
     */
    private void setThemeButtonGroup() {
        defaultTheme = new JRadioButtonMenuItem("Default");
        defaultTheme.addActionListener(this);
        rubyTheme = new JRadioButtonMenuItem("Ruby");
        rubyTheme.addActionListener(this);
        yellowTheme = new JRadioButtonMenuItem("Yellow");
        yellowTheme.addActionListener(this);
        grayTheme = new JRadioButtonMenuItem("Gray");
        grayTheme.addActionListener(this);
        goldTheme = new JRadioButtonMenuItem("Gold");
        goldTheme.addActionListener(this);
        silverTheme = new JRadioButtonMenuItem("Silver");
        silverTheme.addActionListener(this);
        charcoalTheme = new JRadioButtonMenuItem("Charcoal");
        charcoalTheme.addActionListener(this);
        darudeTheme = new JRadioButtonMenuItem("Darude");
        darudeTheme.addActionListener(this);
        metalTheme = new JRadioButtonMenuItem("Metal");
        metalTheme.addActionListener(this);
        oceanTheme = new JRadioButtonMenuItem("Ocean");
        oceanTheme.addActionListener(this);
        ButtonGroup themeGroup = new ButtonGroup();
        themeGroup.add(defaultTheme);
        themeGroup.add(metalTheme);
        themeGroup.add(oceanTheme);
        themeGroup.add(rubyTheme);
        themeGroup.add(yellowTheme);
        themeGroup.add(grayTheme);
        themeGroup.add(goldTheme);
        themeGroup.add(silverTheme);
        themeGroup.add(charcoalTheme);
        themeGroup.add(darudeTheme);
    }

    /** Builds this menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     * @return The completed JMenu for the JMenuBar
     */
    private JMenu getFileMenu() {
        ImageIcon backupIcon = new ImageIcon(getClass().getClassLoader().getResource("dbbackup.png"));
        ImageIcon restoreIcon = new ImageIcon(getClass().getClassLoader().getResource("dbrestore.png"));
        ImageIcon clearIcon = new ImageIcon(getClass().getClassLoader().getResource("bomb.png"));
        ImageIcon exitIcon = new ImageIcon(getClass().getClassLoader().getResource("exit.png"));
        ImageIcon importIcon = new ImageIcon(getClass().getClassLoader().getResource("import.png"));
        ImageIcon exportIcon = new ImageIcon(getClass().getClassLoader().getResource("export.png"));
        ImageIcon syncIcon = new ImageIcon(getClass().getClassLoader().getResource("sync.png"));
        ImageIcon printIcon = new ImageIcon(getClass().getClassLoader().getResource("printer.png"));
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        JMenuItem backupItem = new JMenuItem(new MenuAction("Backup", this));
        backupItem.setIcon(backupIcon);
        backupItem.setMnemonic('B');
        backupItem.setAccelerator(KeyStroke.getKeyStroke('B', InputEvent.CTRL_DOWN_MASK));
        JMenuItem restoreItem = new JMenuItem(new MenuAction("Restore", this));
        restoreItem.setIcon(restoreIcon);
        restoreItem.setMnemonic('R');
        restoreItem.setAccelerator(KeyStroke.getKeyStroke('R', InputEvent.CTRL_DOWN_MASK));
        JMenuItem clearItem = new JMenuItem(new MenuAction("Clear", this));
        clearItem.setIcon(clearIcon);
        clearItem.setMnemonic('C');
        clearItem.setAccelerator(KeyStroke.getKeyStroke('C', InputEvent.CTRL_DOWN_MASK));
        JMenuItem importItem = new JMenuItem(new MenuAction("Import", this));
        importItem.setIcon(importIcon);
        importItem.setMnemonic('I');
        importItem.setAccelerator(KeyStroke.getKeyStroke('I', InputEvent.CTRL_DOWN_MASK));
        JMenuItem exportItem = new JMenuItem(new MenuAction("Export", this));
        exportItem.setIcon(exportIcon);
        exportItem.setMnemonic('E');
        exportItem.setAccelerator(KeyStroke.getKeyStroke('E', InputEvent.CTRL_DOWN_MASK));
        JMenuItem syncItem = new JMenuItem(new MenuAction("Synchronize", this));
        syncItem.setIcon(syncIcon);
        syncItem.setMnemonic('S');
        syncItem.setAccelerator(KeyStroke.getKeyStroke('S', InputEvent.CTRL_DOWN_MASK));
        JMenuItem printItem = new JMenuItem(new MenuAction("Print", this));
        printItem.setIcon(printIcon);
        printItem.setMnemonic('P');
        printItem.setAccelerator(KeyStroke.getKeyStroke('P', InputEvent.CTRL_DOWN_MASK));
        JMenuItem exitItem = new JMenuItem(new MenuAction("Exit", this));
        exitItem.setIcon(exitIcon);
        exitItem.setMnemonic('x');
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4, InputEvent.ALT_DOWN_MASK));
        fileMenu.add(getNewMenu());
        fileMenu.addSeparator();
        fileMenu.add(clearItem);
        fileMenu.addSeparator();
        fileMenu.add(backupItem);
        fileMenu.add(restoreItem);
        fileMenu.addSeparator();
        fileMenu.add(importItem);
        fileMenu.add(exportItem);
        fileMenu.add(syncItem);
        fileMenu.add(printItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        return fileMenu;
    }

    /** Pulls in a new HPSearchPanel to perform an active "as entered" text search.
     * Uses an active search, changing the search results as you type or make
     * changes by checking/unchecking the "Search in" boxes.
     *
     * Performs the same search as the Search Window, but uses mini profiles
     * and performs it's task in the main window instead of moving the user to
     * another window.
     */
    public void showActiveSearch() {
        this.removeAllComponents();
        contentPane.add(new HPSearchPanel(this));
        this.updateWindow();
    }

    /** Builds this menu and then returns the completed object.
     * Loads the images used by the this menu, creates and adds all menu items
     * and sub menus that belong to this menu.
     * @return The completed JMenu for the JMenuBar
     */
    private JMenu getNewMenu() {
        ImageIcon newIcon = new ImageIcon(getClass().getClassLoader().getResource("add.png"));
        ImageIcon addIndIcon = new ImageIcon(getClass().getClassLoader().getResource("user_add.png"));
        ImageIcon addFamIcon = new ImageIcon(getClass().getClassLoader().getResource("family.png"));
        ImageIcon addCorpIcon = new ImageIcon(getClass().getClassLoader().getResource("business.png"));
        ImageIcon quickAddIcon = new ImageIcon(getClass().getClassLoader().getResource("hourglass.png"));
        JMenuItem quickAddItem = new JMenuItem(new MenuAction("Quick Add", this));
        quickAddItem.setIcon(quickAddIcon);
        quickAddItem.setMnemonic('Q');
        quickAddItem.setAccelerator(KeyStroke.getKeyStroke('Q', InputEvent.CTRL_DOWN_MASK));
        JMenuItem addIndItem = new JMenuItem(new MenuAction("Add Individual", this));
        addIndItem.setIcon(addIndIcon);
        addIndItem.setMnemonic('d');
        addIndItem.setAccelerator(KeyStroke.getKeyStroke('I', KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        JMenuItem addFamItem = new JMenuItem(new MenuAction("Add Family", this));
        addFamItem.setIcon(addFamIcon);
        addFamItem.setMnemonic('a');
        addFamItem.setAccelerator(KeyStroke.getKeyStroke('F', KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        JMenuItem addCorpItem = new JMenuItem(new MenuAction("Add Business", this));
        addCorpItem.setIcon(addCorpIcon);
        addCorpItem.setMnemonic('u');
        addCorpItem.setAccelerator(KeyStroke.getKeyStroke('B', KeyEvent.CTRL_DOWN_MASK + KeyEvent.SHIFT_DOWN_MASK));
        JMenu newMenu = new JMenu("New");
        newMenu.setIcon(newIcon);
        newMenu.setMnemonic('N');
        newMenu.add(quickAddItem);
        newMenu.addSeparator();
        newMenu.add(addIndItem);
        newMenu.add(addFamItem);
        newMenu.add(addCorpItem);
        return newMenu;
    }

    /**
     * Loads the Hoi Polloi properties file.
     *
     * The filename is hp.properties
     */
    private void loadPropertyFile() {
        try {
            propFile = new Properties();
            propFile.load(new java.io.FileInputStream(new java.io.File("hp.properties")));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading property file!\nShutting down program.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
        setTheme(propFile.getProperty("theme"));
        if (propFile.getProperty("version") == null) {
            propFile.setProperty("version", "0.004A");
            this.hpVersion = "0.004A";
        } else {
            this.hpVersion = propFile.getProperty("version");
        }
        if (!propFile.getProperty("lastprofile").equals("-1")) {
            try {
                showProfile(new Person(Integer.parseInt(propFile.getProperty("lastprofile"))));
            } catch (Exception e) {
                Debug.print("Error: Unable to load last viewed profile");
                Debug.print(e.getMessage());
            }
        }
    }

    /**
     * Saves the Hoi Polloi properties file.
     *
     * The filename is hp.properties
     */
    public void savePropertyFile() {
        try {
            propFile.store(new java.io.FileOutputStream(new java.io.File("hp.properties")), "Property File for Hoi Polloi");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error saving property file, if you changed\n" + "settings then they have not been saved.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets the theme for Hoi Polloi.
     *
     * @param theme The name of the theme to set.
     */
    private void setTheme(String theme) {
        if ("default".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(DEFAULT);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            defaultTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("gray".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(GRAY);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            grayTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("yellow".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(YELLOW);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            yellowTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("ruby".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(RUBY);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            rubyTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("darude".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(DARUDE);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            darudeTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("silver".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(SILVER);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            silverTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("gold".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(GOLD);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            goldTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("charcoal".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(CHARCOAL);
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            charcoalTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("metal".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(new javax.swing.plaf.metal.DefaultMetalTheme());
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            metalTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        } else if ("ocean".equalsIgnoreCase(theme)) {
            PlafOptions.setCurrentTheme(new javax.swing.plaf.metal.OceanTheme());
            PlafOptions.setAsLookAndFeel();
            PlafOptions.updateAllUIs();
            oceanTheme.setSelected(true);
            Debug.print("Set theme: " + theme.toUpperCase());
        }
    }

    /**
     * Formats a date from YYYY-MM-DD into Month day, Year.
     *
     * Eg. Takes in 1979-12-12 and spits out Dec 12, 1979.
     *
     * @param dateString The date string as YYYY-MM-DD
     * @return The date formatted by Month day, Year
     */
    private String convertDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        java.sql.Date dateObject = java.sql.Date.valueOf(dateString);
        return format.format(dateObject);
    }

    /** Need to Java Doc this, and what is the difference between updateFilteredList */
    public void updateFilterTree() {
        this.filterListPanel.removeAll();
        this.filterListPanel.add(this.getFilterListTree());
        this.updateWindow();
    }

    /**
     * Sets the state of the editing flag so the program knows if it's being
     * edited.
     * @param state The new state for the editing flag.
     */
    public void setEditing(boolean state) {
        this.editing = state;
    }

    /**
     * A test approach for editing a profile.
     *
     * Probably won't use this approach, someone make a better idea.
     *
     * @param person The Person's Profile to Edit.
     */
    public void editProfile(Person person) {
        this.setEditing(true);
        removeAllComponents();
        String prefix = person.getPrefix();
        String firstName = person.getFirstName();
        String middleName = person.getMiddleName();
        String lastName = person.getLastName();
        String suffix = person.getSuffix();
        String nickName = person.getNickName();
        String eyeColor = person.getEyeColor();
        String hairColor = person.getHairColor();
        String height = person.getHeight();
        String weight = person.getWeight();
        String dob = person.getBirthday();
        String maidenName = person.getMaidenName();
        String gender = person.getGender();
        KeyValue demonym = person.getDemonym();
        Font boldInfoFont = new Font("Arial", Font.BOLD, 12);
        String desc = person.getDescription();
        if (desc == null || desc.isEmpty()) desc = "You have not entered a description for this person yet!";
        String fileName = person.getPhotoFileName();
        if (fileName == null || fileName.equals("")) fileName = "pictures/unknown.jpg";
        String lastUpdate = person.getLastUpdate();
        if (lastUpdate == null || lastUpdate.equals("") || lastUpdate.equals("0000-00-00")) lastUpdate = "Never"; else lastUpdate = convertDate(lastUpdate);
        JLabel prefixLabel = new JLabel("Prefix:");
        JLabel firstLabel = new JLabel("First:");
        JLabel middleLabel = new JLabel("Middle:");
        JLabel lastLabel = new JLabel("Last:");
        JLabel suffixLabel = new JLabel("Suffix:");
        prefixLabel.setFont(boldInfoFont);
        firstLabel.setFont(boldInfoFont);
        middleLabel.setFont(boldInfoFont);
        lastLabel.setFont(boldInfoFont);
        suffixLabel.setFont(boldInfoFont);
        final JTextField psnPrefixBox = new JTextField(prefix);
        final JTextField psnFirstNameBox = new JTextField(firstName);
        final JTextField psnMiddleNameBox = new JTextField(middleName);
        final JTextField psnLastNameBox = new JTextField(lastName);
        final JTextField psnSuffixBox = new JTextField(suffix);
        psnPrefixBox.setColumns(3);
        psnFirstNameBox.setColumns(5);
        psnMiddleNameBox.setColumns(5);
        psnLastNameBox.setColumns(5);
        psnSuffixBox.setColumns(3);
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(prefixLabel);
        namePanel.add(psnPrefixBox);
        namePanel.add(firstLabel);
        namePanel.add(psnFirstNameBox);
        namePanel.add(middleLabel);
        namePanel.add(psnMiddleNameBox);
        namePanel.add(lastLabel);
        namePanel.add(psnLastNameBox);
        namePanel.add(suffixLabel);
        namePanel.add(psnSuffixBox);
        btnUpdateProfile = new JButton("Save");
        btnDelProfile = new JButton("Delete");
        namePanel.add(btnUpdateProfile);
        namePanel.add(btnDelProfile);
        JLabel nickNameLabel = new JLabel("Nick Name:");
        nickNameLabel.setFont(boldInfoFont);
        final JTextField nickNameTextField = new JTextField(nickName);
        nickNameTextField.setColumns(5);
        JPanel nickNamePanel = new JPanel();
        nickNamePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        nickNamePanel.add(nickNameLabel);
        nickNamePanel.add(nickNameTextField);
        JPanel nickPanel = new JPanel();
        nickPanel.setLayout(new BorderLayout());
        nickPanel.add(nickNamePanel, BorderLayout.SOUTH);
        JLabel picLabel = new JLabel();
        picLabel.setIcon(new ImageIcon(fileName));
        picLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new BorderLayout());
        picPanel.add(picLabel, BorderLayout.EAST);
        final JTextArea descArea = new JTextArea();
        descArea.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        descArea.setText(desc);
        descArea.setBackground(Color.WHITE);
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEnabled(true);
        descArea.setCaretPosition(desc.length());
        JScrollPane descScroller = new JScrollPane(descArea);
        descScroller.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        JLabel eyeLabel = new JLabel("Eye Color:");
        eyeLabel.setFont(boldInfoFont);
        final JTextField personEyeLabel = new JTextField(eyeColor);
        personEyeLabel.setColumns(5);
        JPanel eyePanel = new JPanel();
        eyePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        eyePanel.add(eyeLabel);
        eyePanel.add(personEyeLabel);
        JLabel hairLabel = new JLabel("Hair Color:");
        hairLabel.setFont(boldInfoFont);
        final JTextField personHairLabel = new JTextField(hairColor);
        personHairLabel.setColumns(5);
        JPanel hairPanel = new JPanel();
        hairPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        hairPanel.add(hairLabel);
        hairPanel.add(personHairLabel);
        JLabel heightLabel = new JLabel("Height:");
        heightLabel.setFont(boldInfoFont);
        final JTextField personHeightLabel = new JTextField(height);
        personHeightLabel.setColumns(5);
        JPanel heightPanel = new JPanel();
        heightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        heightPanel.add(heightLabel);
        heightPanel.add(personHeightLabel);
        JLabel weightLabel = new JLabel("Weight:");
        weightLabel.setFont(boldInfoFont);
        final JTextField personWeightLabel = new JTextField(weight);
        personWeightLabel.setColumns(5);
        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        weightPanel.add(weightLabel);
        weightPanel.add(personWeightLabel);
        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setFont(boldInfoFont);
        final JComboBox monthBox = new JComboBox();
        monthBox.setFont(new Font("Arial", Font.PLAIN, 11));
        final JComboBox dayBox = new JComboBox();
        dayBox.setFont(new Font("Arial", Font.PLAIN, 11));
        final JComboBox yearBox = new JComboBox();
        yearBox.setFont(new Font("Arial", Font.PLAIN, 11));
        String months[] = { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" };
        int daysInMonth[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        Calendar cal = new GregorianCalendar();
        int yearNow = cal.get(Calendar.YEAR);
        for (int x = 1; x < 10; x++) dayBox.addItem("0" + x);
        for (int i = 0; i < 12; i++) monthBox.addItem(months[i]);
        for (int j = 10; j <= 31; j++) dayBox.addItem(j);
        for (int k = yearNow; k >= yearNow - 100; k--) yearBox.addItem(k);
        if (dob.matches("[0-9]{4}-[0-9]{2}-[0-9]{2}")) {
            int bYear = Integer.parseInt(dob.substring(0, 4));
            int bMonth = Integer.parseInt(dob.substring(5, 7));
            int bDay = Integer.parseInt(dob.substring(8, 10));
            if (bDay < 10) {
                dayBox.setSelectedItem("0" + bDay);
            } else {
                dayBox.setSelectedItem(bDay);
            }
            yearBox.setSelectedItem(bYear);
            monthBox.setSelectedIndex(bMonth - 1);
            Debug.print("EP:REGEX: " + bYear + "-" + bMonth + "-" + bDay);
        }
        JPanel dobPanel = new JPanel();
        dobPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        dobPanel.add(dobLabel);
        dobPanel.add(monthBox);
        dobPanel.add(dayBox);
        dobPanel.add(yearBox);
        JLabel maidenLabel = new JLabel("Maiden Name:");
        maidenLabel.setFont(boldInfoFont);
        final JTextField personMaidenLabel = new JTextField(maidenName);
        personMaidenLabel.setColumns(5);
        JPanel maidenPanel = new JPanel();
        maidenPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        maidenPanel.add(maidenLabel);
        maidenPanel.add(personMaidenLabel);
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(boldInfoFont);
        final JComboBox personGenderLabel = new JComboBox();
        personGenderLabel.addItem("Unknown");
        personGenderLabel.addItem("Male");
        personGenderLabel.addItem("Female");
        if (gender.equals("Male")) personGenderLabel.setSelectedItem("Male"); else if (gender.equals("Female")) personGenderLabel.setSelectedItem("Female"); else personGenderLabel.setSelectedItem("Unknown");
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        genderPanel.add(genderLabel);
        genderPanel.add(personGenderLabel);
        JLabel demonymLabel = new JLabel("Demonym:");
        demonymLabel.setFont(boldInfoFont);
        final JComboBox demonymBox = new JComboBox();
        ArrayList<KeyValue> demonyms = DBHPInterface.getListOfDemonyms();
        for (KeyValue dem : demonyms) {
            String demstr = dem.getValue();
            demonymBox.addItem(demstr);
        }
        if (!demonym.getValue().equals("")) {
            demonymBox.setSelectedItem(demonym.getValue());
        }
        JPanel demonymPanel = new JPanel();
        demonymPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        demonymPanel.add(demonymLabel);
        demonymPanel.add(demonymBox);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(8, 1));
        infoPanel.add(eyePanel);
        infoPanel.add(hairPanel);
        infoPanel.add(heightPanel);
        infoPanel.add(weightPanel);
        infoPanel.add(dobPanel);
        if (!person.getGender().equals("Male")) infoPanel.add(maidenPanel);
        infoPanel.add(genderPanel);
        infoPanel.add(demonymPanel);
        personGenderLabel.addActionListener(new GenderComboBoxActionListener(this, infoPanel, maidenPanel));
        picPanel.add(infoPanel, BorderLayout.WEST);
        Font bottomFont = new Font("Arial", Font.PLAIN, 10);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        JLabel lastUpdateLabel = new JLabel("  Last Udated: " + lastUpdate + "  ");
        lastUpdateLabel.setFont(bottomFont);
        lastUpdateLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
        bottomPanel.add(this.getCategoryPanel(), BorderLayout.WEST);
        bottomPanel.add(lastUpdateLabel, BorderLayout.EAST);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(namePanel, BorderLayout.WEST);
        topPanel.add(nickPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.add(picPanel, BorderLayout.NORTH);
        eastPanel.add(descScroller, BorderLayout.CENTER);
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        JPanel ctnPanel = new JPanel();
        ctnPanel.setLayout(new BorderLayout());
        ctnPanel.setBorder(BorderFactory.createTitledBorder("Contact Info"));
        ArrayList contacts = person.getContacts();
        final DefaultTableModel model = new DefaultTableModel();
        ctnTable = new JTable(model);
        model.addColumn("Contact Type");
        model.addColumn("Contact Name");
        ctnTable.setPreferredScrollableViewportSize(new Dimension(150, 150));
        ctnTable.setGridColor(Color.LIGHT_GRAY);
        for (int i = 0; i < contacts.size(); i++) {
            Contact ctn = (Contact) contacts.get(i);
            model.addRow(new Object[] { ctn.getContactType(), ctn.getContact() });
        }
        ColumnResizer.adjustColumnPreferredWidths(ctnTable);
        JScrollPane ctnScrollPane = new JScrollPane(ctnTable);
        ctnPanel.add(ctnScrollPane, BorderLayout.NORTH);
        ctnTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    String type = (String) target.getValueAt(row, 0);
                    String data = (String) target.getValueAt(row, 1);
                    try {
                        if (type.equals("URL")) {
                            BrowserLauncher.openURL(data);
                        } else if (type.equals("Skype")) {
                            BrowserLauncher.openURL("skype:" + data + "?chat");
                        } else if (type.equals("Home Phone") || type.equals("Work Phone") || type.equals("Cell Phone") || type.equals("Perm Phone") || type.equals("Pager")) {
                            BrowserLauncher.openURL("callto://" + data);
                        } else if (type.equals("Primary Email") || type.equals("Extra Email")) {
                            BrowserLauncher.openURL("mailto:" + data);
                        } else if (type.equals("AIM")) {
                            BrowserLauncher.openURL("aim:goim?screenname=" + data);
                        } else if (type.equals("YIM")) {
                            BrowserLauncher.openURL("ymsgr:sendim?" + data);
                        } else if (type.equals("ICQ")) {
                            BrowserLauncher.openURL("http://web.icq.com/whitepages/message_me?uin=" + data + "&action=message");
                        } else if (type.equals("MSN")) {
                            BrowserLauncher.openURL("msnim:chat?contact=" + data);
                        }
                    } catch (Exception ex) {
                        Debug.print(ex.getMessage());
                    }
                }
            }
        });
        JPanel newContactPanel = new JPanel();
        newContactPanel.setLayout(new FlowLayout());
        final JComboBox ctnTypeComboBox = new JComboBox();
        ctnTypeComboBox.setRenderer(new HPCellRenderer());
        ArrayList ctnTypes = Contact.getContactTypes();
        for (Object value : ctnTypes) {
            ctnTypeComboBox.addItem(value);
        }
        final JTextField ctnText = new JTextField(10);
        final Person noob = person;
        btnAddContact = new JButton("Add");
        btnAddContact.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ctnText != null && ctnText.getText().length() != 0) {
                    int ctnTypeID = ((KeyValue) ctnTypeComboBox.getSelectedItem()).getKey();
                    noob.addContact(ctnTypeID, ctnText.getText());
                    noob.saveToDatabase();
                    model.addRow(new Object[] { ((KeyValue) ctnTypeComboBox.getSelectedItem()).getValue(), ctnText.getText() });
                    ctnText.setText(null);
                    ctnTypeComboBox.setSelectedIndex(0);
                }
            }
        });
        btnDelContact = new JButton("-");
        btnDelContact.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = ctnTable.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(THIS, "Please selecte a contact to remove.");
                } else {
                    String ctnType = (String) model.getValueAt(row, 0);
                    String ctnData = (String) model.getValueAt(row, 1);
                    noob.removeContact(ctnType, ctnData);
                    noob.saveToDatabase();
                    Debug.print("Removing " + ctnType + ": " + ctnData + " for Person #" + noob.getPersonID());
                    model.removeRow(row);
                }
            }
        });
        ctnText.addKeyListener(new KeyListener() {

            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                    if (ctnText != null) {
                        int ctnTypeID = ((KeyValue) ctnTypeComboBox.getSelectedItem()).getKey();
                        noob.addContact(ctnTypeID, ctnText.getText());
                        noob.saveToDatabase();
                        model.addRow(new Object[] { ((KeyValue) ctnTypeComboBox.getSelectedItem()).getValue(), ctnText.getText() });
                        ctnText.setText(null);
                        ctnTypeComboBox.setSelectedIndex(0);
                    }
                }
            }

            public void keyReleased(KeyEvent evt) {
            }

            public void keyTyped(KeyEvent evt) {
            }
        });
        newContactPanel.add(ctnTypeComboBox);
        newContactPanel.add(ctnText);
        newContactPanel.add(btnAddContact);
        newContactPanel.add(btnDelContact);
        ctnPanel.add(newContactPanel, BorderLayout.SOUTH);
        westPanel.add(ctnPanel, BorderLayout.NORTH);
        westPanel.add(getAddressPane(person));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(eastPanel, BorderLayout.EAST);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        contentPane.add(westPanel, BorderLayout.WEST);
        final Person p = person;
        btnUpdateProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (!personGenderLabel.getSelectedItem().toString().equals("Unknown")) {
                    p.setPrefix(psnPrefixBox.getText());
                    p.setFirstName(psnFirstNameBox.getText());
                    p.setMiddleName(psnMiddleNameBox.getText());
                    p.setLastName(psnLastNameBox.getText());
                    p.setSuffix(psnSuffixBox.getText());
                    p.setNickName(nickNameTextField.getText());
                    p.setEyeColor(personEyeLabel.getText());
                    p.setHairColor(personHairLabel.getText());
                    p.setHeight(personHeightLabel.getText());
                    p.setWeight(personWeightLabel.getText());
                    p.setBirthday(yearBox.getSelectedItem().toString() + "-" + DBHPInterface.getMonthNumber(monthBox.getSelectedItem().toString()) + "-" + dayBox.getSelectedItem().toString());
                    p.setMaidenName(personMaidenLabel.getText());
                    p.setGender(personGenderLabel.getSelectedItem().toString());
                    KeyValue d = new KeyValue();
                    d.setKey(DBHPInterface.getIDOfDemonym((String) demonymBox.getSelectedItem()));
                    d.setValue((String) demonymBox.getSelectedItem());
                    p.setDemonym(d);
                    p.setDescription(descArea.getText());
                    p.saveToDatabase();
                    updateFilteredList();
                    THIS.setEditing(false);
                    showProfile(p);
                } else JOptionPane.showMessageDialog(THIS, "Is this person a Male or Female?", "Select a Gender!", JOptionPane.QUESTION_MESSAGE);
            }
        });
        btnDelProfile.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ImageIcon deathIcon = new ImageIcon(getClass().getClassLoader().getResource("death.gif"));
                int response = JOptionPane.showConfirmDialog(THIS, "Are you sure you want to purge this profile?", "Purge Profile", 0, 0, deathIcon);
                if (response == 0) {
                    if (Death.purgePerson(p)) {
                        currentPerson = null;
                        propFile.setProperty("lastprofile", "-1");
                        savePropertyFile();
                        clearWindow();
                        showPeopleList(DBHPInterface.getListOfPeopleByLastName());
                        THIS.setEditing(false);
                    } else {
                        Debug.print("Failed to Purge this Person");
                    }
                }
            }
        });
        updateWindow();
    }

    /**
     * Gets the current person.
     *
     * @return The Current Person being Viewed.
     */
    public Person getCurrentPerson() {
        return currentPerson;
    }

    /**
     * Gets the filter list JTree.
     *
     * @return The Filter List JTree.
     */
    public JScrollPane getFilterListTree() {
        DefaultMutableTreeNode top = new DefaultMutableTreeNode("Filter the Hoi Polloi by ...");
        DefaultMutableTreeNode subNode0 = new DefaultMutableTreeNode("Everyone");
        DefaultMutableTreeNode subNode1 = new DefaultMutableTreeNode("Category");
        DefaultMutableTreeNode subNode2 = new DefaultMutableTreeNode("District");
        DefaultMutableTreeNode subNode3 = new DefaultMutableTreeNode("Cities");
        DefaultMutableTreeNode subNode4 = new DefaultMutableTreeNode("Countries");
        DefaultMutableTreeNode subNode5 = new DefaultMutableTreeNode("Recent");
        DefaultMutableTreeNode subNode6 = new DefaultMutableTreeNode("Birthdays");
        DefaultMutableTreeNode subNode7 = new DefaultMutableTreeNode("Search");
        ArrayList<KeyValue> categories = DBHPInterface.getListOfCategories();
        for (KeyValue c : categories) {
            subNode1.add(new DefaultMutableTreeNode(c));
        }
        ArrayList districts = DBHPInterface.getListOfDistricts();
        for (Object c : districts) {
            subNode2.add(new DefaultMutableTreeNode(c));
        }
        ArrayList cities = DBHPInterface.getCitiesPeopleAreIn();
        for (Object o : cities) {
            subNode3.add(new DefaultMutableTreeNode(o));
        }
        ArrayList countries = DBHPInterface.getCountriesPeopleAreIn();
        for (Object o : countries) {
            subNode4.add(new DefaultMutableTreeNode(o));
        }
        subNode5.add(new DefaultMutableTreeNode(new KeyValue(25, "Most Recent 25")));
        subNode5.add(new DefaultMutableTreeNode(new KeyValue(50, "Most Recent 50")));
        subNode5.add(new DefaultMutableTreeNode(new KeyValue(100, "Most Recent 100")));
        subNode6.add(new DefaultMutableTreeNode("Last Month"));
        subNode6.add(new DefaultMutableTreeNode("This Month"));
        subNode6.add(new DefaultMutableTreeNode("Next Month"));
        top.add(subNode0);
        top.add(subNode1);
        top.add(subNode2);
        top.add(subNode3);
        top.add(subNode4);
        top.add(subNode5);
        top.add(subNode6);
        top.add(subNode7);
        JTree tree = new JTree(top);
        tree.addTreeSelectionListener(new FilterTreeSelectionListener());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.addKeyListener(new FilterTreeKeyListener());
        JScrollPane treeView = new JScrollPane(tree);
        return treeView;
    }

    /**
     * Updates the filtered list to reflect changes in the database.
     */
    public void updateFilteredList() {
        JTree filterTree = (JTree) ((JViewport) filterListScrollPane.getComponent(0)).getComponent(0);
        setFilteredInfo(filterTree);
    }

    /** Sets the contents of the Filter list based on filter parameters from the given tree
     * Using the already selected filter settings from the filter tree, or by
     * default, showing everyone if no valid sort is selected.
     * @param tree The tree to retrieve the filter parameters from.
     */
    public void setFilteredInfo(JTree tree) {
        TreePath selectionPath = tree.getSelectionPath();
        DefaultMutableTreeNode parentNode;
        if (selectionPath != null && selectionPath.getParentPath() != null) {
            parentNode = (DefaultMutableTreeNode) (selectionPath.getParentPath().getLastPathComponent());
            if (parentNode.toString().equals("Category")) {
                DefaultMutableTreeNode catNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
                KeyValue catValue = (KeyValue) (catNode.getUserObject());
                showPeopleList(DBHPInterface.getPeopleInCategory(catValue.getKey()));
            } else if (parentNode.toString().equals("District")) {
                DefaultMutableTreeNode filterNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
                showPeopleList(DBHPInterface.getPeopleInDistrict(filterNode.toString()));
            } else if (parentNode.toString().equals("Cities")) {
                DefaultMutableTreeNode filterNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
                showPeopleList(DBHPInterface.getPeopleInCity(filterNode.toString()));
            } else if (parentNode.toString().equals("Countries")) {
                DefaultMutableTreeNode filterNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
                KeyValue countryValue = (KeyValue) (filterNode.getUserObject());
                showPeopleList(DBHPInterface.getPeopleInCountry(countryValue.getKey()));
            } else if (parentNode.toString().equals("Recent")) {
                DefaultMutableTreeNode filterNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
                KeyValue recentValue = (KeyValue) (filterNode.getUserObject());
                showPeopleList(DBHPInterface.getMostRecentlyAdded(recentValue.getKey()));
            } else if (parentNode.toString().equals("Birthdays")) {
                DefaultMutableTreeNode filterNode = (DefaultMutableTreeNode) (selectionPath.getLastPathComponent());
                if (filterNode.toString().equals("Last Month")) showPeopleList(DBHPInterface.getBirthdaysPrevMonth()); else if (filterNode.toString().equals("This Month")) showPeopleList(DBHPInterface.getBirthdaysThisMonth()); else if (filterNode.toString().equals("Next Month")) showPeopleList(DBHPInterface.getBirthdaysNextMonth());
            } else if (selectionPath.getLastPathComponent().toString().equals("Search")) {
            } else showPeopleList(DBHPInterface.getListOfPeopleByLastName());
        } else showPeopleList(DBHPInterface.getListOfPeopleByLastName());
    }

    /**
     * Displays a Family's Profile.
     *
     * @param family The Family whose profile to display.
     */
    public void showProfile(Family family) {
    }

    /**
     * Displays a Business's Profile.
     *
     * @param business The Business whose profile to display.
     */
    public void showProfile(Business business) {
    }

    /**
     * Display a Person's Profile.
     *
     * @param person The Person whose profile to display.
     */
    public void showProfile(Person person) {
        this.currentPerson = person;
        StringBuffer trayTooltip = new StringBuffer();
        trayTooltip.append("Viewing profile for ");
        trayTooltip.append(person.getLastName());
        trayTooltip.append(", ");
        trayTooltip.append(person.getFirstName());
        trayTooltip.append(".");
        this.sysTrayTooltip(trayTooltip.toString());
        removeAllComponents();
        Debug.print("Getting information on person with ID #: " + person.getPersonID());
        propFile.setProperty("lastprofile", person.getPersonID() + "");
        String prefix = person.getPrefix() + " ";
        if (prefix.equals("null ") || prefix.equals(" ")) prefix = "";
        String firstName = person.getFirstName();
        if (firstName == null || firstName.equals("")) firstName = "";
        String middleName = " " + person.getMiddleName();
        if (middleName.equals(" null") || middleName.equals(" ")) middleName = "";
        String lastName = " " + person.getLastName();
        if (lastName.equals(" null") || lastName.equals(" ")) lastName = "";
        String suffix = " " + person.getSuffix();
        if (suffix.equals(" null") || suffix.equals(" ")) suffix = "";
        String name = prefix + firstName + middleName + lastName + suffix;
        if (name == null || name.length() == 0) name = "Unknown Person";
        String nickName = "\"" + person.getNickName() + "\"";
        if (nickName.equals("\"null\"") || nickName.equals("\"\"")) nickName = "";
        String eyeColor = person.getEyeColor();
        if (eyeColor == null || eyeColor.equals("")) eyeColor = "Unknown";
        String hairColor = person.getHairColor();
        if (hairColor == null || hairColor.equals("")) hairColor = "Unknown";
        String height = person.getHeight();
        if (height == null || height.equals("")) height = "Unknown";
        String weight = person.getWeight();
        if (weight == null || weight.equals("")) weight = "Unknown";
        String dob = person.getBirthday();
        if (dob == null || dob.equals("") || dob.equals("0000-00-00")) dob = "Unknown"; else dob = convertDate(dob) + " (" + person.getCurrentAge() + ")";
        String maidenName = person.getMaidenName();
        if (maidenName == null || maidenName.equals("")) maidenName = "Unknown";
        String gender = person.getGender();
        if (gender == null || gender.equals("")) gender = "Unknown";
        String demonym = person.getDemonymText();
        if (demonym == null || demonym.equals("")) demonym = "Unknown";
        String desc = person.getDescription();
        if (desc == null || desc.equals("")) desc = "You have not entered a description for this person yet!";
        String fileName = person.getPhotoFileName();
        Debug.print(fileName);
        if (fileName == null || fileName.equals("")) fileName = "pictures/unknown.jpg";
        String lastUpdate = person.getLastUpdate();
        if (lastUpdate == null || lastUpdate.equals("") || lastUpdate.equals("0000-00-00")) lastUpdate = "Never"; else lastUpdate = convertDate(lastUpdate);
        Debug.print("Done getting info for this person:");
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 30));
        JPanel namePanel = new JPanel();
        namePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        namePanel.add(nameLabel);
        JLabel nickLabel = new JLabel(nickName);
        nickLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        nickLabel.setForeground(Color.GRAY);
        JPanel nickLabelPanel = new JPanel();
        nickLabelPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        nickLabelPanel.add(nickLabel);
        JPanel nickPanel = new JPanel();
        nickPanel.setLayout(new BorderLayout());
        nickPanel.add(nickLabelPanel, BorderLayout.SOUTH);
        JLabel picLabel = new JLabel();
        try {
            picLabel.setIcon(new ImageIcon(javax.imageio.ImageIO.read(new java.io.File(fileName))));
        } catch (Exception exc) {
        }
        picLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 1, 0, Color.BLACK));
        JPanel picPanel = new JPanel();
        picPanel.setLayout(new BorderLayout());
        picPanel.add(picLabel, BorderLayout.EAST);
        picLabel.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    HPPictureEditor.showHPPictureEditor(THIS, currentPerson.getPersonID());
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        JTextArea descArea = new JTextArea();
        descArea.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        descArea.setText(desc);
        descArea.setBackground(contentPane.getBackground());
        descArea.setWrapStyleWord(true);
        descArea.setLineWrap(true);
        descArea.setEnabled(false);
        descArea.setDisabledTextColor(Color.BLACK);
        descArea.setCaretPosition(0);
        JScrollPane descScroller = new JScrollPane(descArea);
        descScroller.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK));
        Font infoFont = new Font("Arial", Font.PLAIN, 12);
        Font boldInfoFont = new Font("Arial", Font.BOLD, 12);
        JLabel eyeLabel = new JLabel("Eye Color:");
        eyeLabel.setFont(boldInfoFont);
        JLabel personEyeLabel = new JLabel(eyeColor);
        personEyeLabel.setFont(infoFont);
        JPanel eyePanel = new JPanel();
        eyePanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        eyePanel.add(eyeLabel);
        eyePanel.add(personEyeLabel);
        JLabel hairLabel = new JLabel("Hair Color:");
        hairLabel.setFont(boldInfoFont);
        JLabel personHairLabel = new JLabel(hairColor);
        personHairLabel.setFont(infoFont);
        JPanel hairPanel = new JPanel();
        hairPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        hairPanel.add(hairLabel);
        hairPanel.add(personHairLabel);
        JLabel heightLabel = new JLabel("Height:");
        heightLabel.setFont(boldInfoFont);
        JLabel personHeightLabel = new JLabel(height);
        personHeightLabel.setFont(infoFont);
        JPanel heightPanel = new JPanel();
        heightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        heightPanel.add(heightLabel);
        heightPanel.add(personHeightLabel);
        JLabel weightLabel = new JLabel("Weight:");
        weightLabel.setFont(boldInfoFont);
        JLabel personWeightLabel = new JLabel(weight);
        personWeightLabel.setFont(infoFont);
        JPanel weightPanel = new JPanel();
        weightPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        weightPanel.add(weightLabel);
        weightPanel.add(personWeightLabel);
        JLabel dobLabel = new JLabel("DOB:");
        dobLabel.setFont(boldInfoFont);
        JLabel personDobLabel = new JLabel(dob);
        personDobLabel.setFont(infoFont);
        personDobLabel.setToolTipText(Birthday.getETAString(person.getBirthday()));
        JPanel dobPanel = new JPanel();
        dobPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        dobPanel.add(dobLabel);
        dobPanel.add(personDobLabel);
        JLabel maidenLabel = new JLabel("Maiden Name:");
        maidenLabel.setFont(boldInfoFont);
        JLabel personMaidenLabel = new JLabel(maidenName);
        personMaidenLabel.setFont(infoFont);
        JPanel maidenPanel = new JPanel();
        maidenPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        maidenPanel.add(maidenLabel);
        maidenPanel.add(personMaidenLabel);
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(boldInfoFont);
        JLabel personGenderLabel = new JLabel(gender);
        personGenderLabel.setFont(infoFont);
        JPanel genderPanel = new JPanel();
        genderPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        genderPanel.add(genderLabel);
        genderPanel.add(personGenderLabel);
        JLabel demonymLabel = new JLabel("Demonym:");
        demonymLabel.setFont(boldInfoFont);
        JLabel personDemonymLabel = new JLabel(demonym);
        personDemonymLabel.setFont(infoFont);
        JPanel demonymPanel = new JPanel();
        demonymPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        demonymPanel.add(demonymLabel);
        demonymPanel.add(personDemonymLabel);
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(8, 1));
        infoPanel.add(eyePanel);
        infoPanel.add(hairPanel);
        infoPanel.add(heightPanel);
        infoPanel.add(weightPanel);
        infoPanel.add(dobPanel);
        if (!gender.equals("Male")) infoPanel.add(maidenPanel);
        infoPanel.add(genderPanel);
        infoPanel.add(demonymPanel);
        picPanel.add(infoPanel, BorderLayout.WEST);
        Font bottomFont = new Font("Arial", Font.PLAIN, 10);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
        JLabel lastUpdateLabel = new JLabel("  Last Udated: " + lastUpdate + "  ");
        lastUpdateLabel.setFont(bottomFont);
        lastUpdateLabel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, Color.GRAY));
        bottomPanel.add(this.getCategoryPanel(), BorderLayout.WEST);
        bottomPanel.add(lastUpdateLabel, BorderLayout.EAST);
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.add(namePanel, BorderLayout.WEST);
        topPanel.add(nickPanel, BorderLayout.EAST);
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.BLACK));
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JPanel eastPanel = new JPanel();
        eastPanel.setLayout(new BorderLayout());
        eastPanel.add(picPanel, BorderLayout.NORTH);
        eastPanel.add(descScroller, BorderLayout.CENTER);
        JPanel westPanel = new JPanel();
        westPanel.setLayout(new BorderLayout());
        JPanel ctnPanel = new JPanel();
        ctnPanel.setLayout(new BorderLayout());
        ctnPanel.setBorder(BorderFactory.createTitledBorder("Contact Info"));
        ArrayList contacts = person.getContacts();
        final DefaultTableModel model = new DefaultTableModel();
        ctnTable = new JTable(model);
        model.addColumn("Contact Type");
        model.addColumn("Contact Name");
        ctnTable.setPreferredScrollableViewportSize(new Dimension(350, 200));
        ctnTable.setGridColor(Color.LIGHT_GRAY);
        for (int i = 0; i < contacts.size(); i++) {
            Contact ctn = (Contact) contacts.get(i);
            model.addRow(new Object[] { ctn.getContactType(), ctn.getContact() });
        }
        ColumnResizer.adjustColumnPreferredWidths(ctnTable);
        JScrollPane ctnScrollPane = new JScrollPane(ctnTable);
        ctnPanel.add(ctnScrollPane, BorderLayout.NORTH);
        ctnTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1 && e.getButton() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    String type = (String) target.getValueAt(row, 0);
                    String data = (String) target.getValueAt(row, 1);
                    try {
                        if (type.equals("URL")) {
                            BrowserLauncher.openURL(data);
                        } else if (type.equals("Skype")) {
                            BrowserLauncher.openURL("skype:" + data + "?chat");
                        } else if (type.equals("Home Phone") || type.equals("Work Phone") || type.equals("Cell Phone") || type.equals("Perm Phone") || type.equals("Pager")) {
                            BrowserLauncher.openURL("callto://" + data);
                        } else if (type.equals("Primary Email") || type.equals("Extra Email")) {
                            BrowserLauncher.openURL("mailto:" + data);
                        } else if (type.equals("AIM")) {
                            BrowserLauncher.openURL("aim:goim?screenname=" + data);
                        } else if (type.equals("YIM")) {
                            BrowserLauncher.openURL("ymsgr:sendim?" + data);
                        } else if (type.equals("ICQ")) {
                            BrowserLauncher.openURL("http://web.icq.com/whitepages/message_me?uin=" + data + "&action=message");
                        } else if (type.equals("MSN")) {
                            BrowserLauncher.openURL("msnim:chat?contact=" + data);
                        }
                    } catch (Exception ex) {
                        Debug.print(ex.getMessage());
                    }
                }
            }
        });
        westPanel.add(ctnPanel, BorderLayout.NORTH);
        westPanel.add(getAddressPane(person));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(topPanel, BorderLayout.NORTH);
        contentPane.add(eastPanel, BorderLayout.EAST);
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        contentPane.add(westPanel, BorderLayout.WEST);
        updateWindow();
    }

    /**
     * Shows a JList of People on the Left Side of the Application.
     *
     * @param peeps The People to Put in the JLIst
     */
    public void showPeopleList(ArrayList peeps) {
        Object[] people = peeps.toArray();
        PeopleList list = new PeopleList(this);
        list.setListData(people);
        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(220, getContentPane().getHeight()));
        this.listPane.removeAll();
        this.listPane.add(listScroller);
        this.updateWindow();
    }

    /** Clears the main application window screen and shows the HP logo in the center.*/
    public void removeAllComponents() {
        contentPane.removeAll();
    }

    /** Causes MainMenu and all subcomponents to repaint() */
    public void updateWindow() {
        contentPane.updateUI();
    }

    /**
     * Gets an AddressPane showing addresses for a given person.
     *
     * @param person The Target Person
     * @return An AddressPane showing Addresses for the Target Person
     */
    public AddressPane getAddressPane(Person person) {
        AddressPane addressPane = new AddressPane(this, person);
        ArrayList<Address> addressList = person.getAddresses();
        if (addressList != null && !addressList.isEmpty()) {
            for (Address address : addressList) {
                String addressType = address.getAddressType().getValue();
                AddressPanel addressPanel = new AddressPanel(address);
                addressPane.addTab(addressType, addressPanel);
            }
        } else {
            JPanel noAddressPanel = new JPanel(new BorderLayout());
            noAddressPanel.add(new JLabel("     Right Click in this area to Manage Addresses"), BorderLayout.CENTER);
            addressPane.addTab("Address", noAddressPanel);
        }
        return addressPane;
    }

    /**
     * Clears and updates the main application window.
     */
    public void clearWindow() {
        this.currentPerson = null;
        removeAllComponents();
        updateWindow();
    }

    /** The Profile Quick-add Feature */
    public void quickAdd() {
        this.currentPerson = null;
        removeAllComponents();
        contentPane.setLayout(new BorderLayout());
        JPanel addPanel = new JPanel();
        addPanel.setLayout(new BorderLayout());
        JPanel comboPanel = new JPanel();
        JPanel quickAddInfo = new JPanel();
        JPanel buttonPanel = new JPanel();
        JComboBox categoryBox = new JComboBox();
        categoryBox.addItem("Select Category");
        categoryBox.addItem("New Category");
        categoryBox.addItem("----------------");
        JTextField firstNameField = new JTextField(15);
        firstNameField.addFocusListener(new TextCompFocusListener());
        JTextField lastNameField = new JTextField(15);
        lastNameField.addFocusListener(new TextCompFocusListener());
        JTextField newCatField = new JTextField(15);
        newCatField.addFocusListener(new TextCompFocusListener());
        newCatField.setText("New Category Name");
        ArrayList tempCatList = DBHPInterface.getListOfCategories();
        if (!(tempCatList == null) || !(tempCatList.isEmpty())) {
            for (Object value : tempCatList) {
                categoryBox.addItem(((KeyValue) value).getValue());
            }
        }
        JTextArea descArea = new JTextArea(5, 20);
        final JPanel INFO_PANEL_REF = quickAddInfo;
        final JPanel BUTTON_PANEL_REF = buttonPanel;
        final JTextField FIRST_NAME_REF = firstNameField;
        final JTextField LAST_NAME_REF = lastNameField;
        final JTextField NEWCAT_TEXT_REF = newCatField;
        final JComboBox CAT_COMBO_REF = categoryBox;
        final JTextArea DESC_REF = descArea;
        JComboBox comboBox = new JComboBox();
        comboBox.addItem("Select Profile Type");
        comboBox.addItem("--------------------");
        comboBox.addItem("Individual");
        comboBox.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                JPanel inputPanel = INFO_PANEL_REF;
                JPanel buttonPanel = BUTTON_PANEL_REF;
                String selected = (String) (e.getItem());
                if ("Individual".equals(selected)) {
                    inputPanel.removeAll();
                    buttonPanel.removeAll();
                    JTextField firstName = FIRST_NAME_REF;
                    JTextField lastName = LAST_NAME_REF;
                    JTextArea descArea = DESC_REF;
                    JComboBox catCombo = CAT_COMBO_REF;
                    catCombo.addItemListener(new ItemListener() {

                        public void itemStateChanged(ItemEvent e) {
                            if (((String) (e.getItem())).equals("New Category")) {
                                NEWCAT_TEXT_REF.setVisible(true);
                            } else {
                                NEWCAT_TEXT_REF.setVisible(false);
                            }
                        }
                    });
                    JTextField newCatField = NEWCAT_TEXT_REF;
                    JButton addButton = new JButton("Add Profile");
                    addButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            boolean canAdd = true;
                            String firstName = FIRST_NAME_REF.getText().trim();
                            String lastName = LAST_NAME_REF.getText().trim();
                            String catAdd = (String) (CAT_COMBO_REF.getSelectedItem());
                            String newCatName = NEWCAT_TEXT_REF.getText().trim();
                            String description = DESC_REF.getText().trim();
                            if (firstName == null || firstName.equals("")) canAdd = false;
                            if (lastName == null || lastName.equals("")) canAdd = false;
                            if (catAdd.equals("Select Category") || catAdd.equals("----------------")) canAdd = false;
                            if (catAdd.equals("New Category") && (newCatName == null || newCatName.equals(""))) canAdd = false;
                            if (canAdd) {
                                Person newPerson = new Person();
                                KeyValue category;
                                int catID = -1;
                                if (catAdd.equals("New Category")) {
                                    catID = DBHPInterface.addNewCategoryToDB(newCatName);
                                } else {
                                    catID = DBHPInterface.getIDOfCategory(catAdd);
                                }
                                newPerson.setFirstName(firstName);
                                newPerson.setLastName(lastName);
                                newPerson.setDescription(description);
                                newPerson.setDemonym(new KeyValue(193, "Unknown"));
                                newPerson.setBirthday("1970-01-01");
                                boolean success = newPerson.saveToDatabase();
                                newPerson.addCategory(catID);
                                if (success) {
                                    THIS.filterListScrollPane = THIS.getFilterListTree();
                                    THIS.filterListPanel.removeAll();
                                    THIS.filterListPanel.add(THIS.filterListScrollPane);
                                    THIS.updateWindow();
                                    showProfile(newPerson);
                                    showPeopleList(DBHPInterface.getListOfPeopleByLastName());
                                } else {
                                    JOptionPane.showMessageDialog(THIS, "There was a problem saving this person to the database, please try again.", "Failure", JOptionPane.ERROR_MESSAGE);
                                }
                            } else JOptionPane.showMessageDialog(THIS, "The First Name, Last Name, and Category must have valid entries!", "Cannot Add", JOptionPane.ERROR_MESSAGE);
                        }
                    });
                    JButton clearButton = new JButton("Clear Form");
                    clearButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            FIRST_NAME_REF.setText("");
                            LAST_NAME_REF.setText("");
                            DESC_REF.setText("");
                            CAT_COMBO_REF.setSelectedIndex(0);
                            NEWCAT_TEXT_REF.setText("New Category Name");
                        }
                    });
                    JButton exitButton = new JButton("Exit Quick-Add");
                    exitButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            THIS.clearWindow();
                        }
                    });
                    JPanel newCatPanel = new JPanel();
                    newCatPanel.setLayout(new VerticalFlowLayout());
                    newCatPanel.add(catCombo);
                    newCatPanel.add(newCatField);
                    JPanel firstNamePanel = new JPanel();
                    firstNamePanel.add(new JLabel("First Name:"));
                    firstNamePanel.add(firstName);
                    JPanel lastNamePanel = new JPanel();
                    lastNamePanel.add(new JLabel("Last Name:"));
                    lastNamePanel.add(lastName);
                    JPanel namePanel = new JPanel();
                    namePanel.setLayout(new VerticalFlowLayout());
                    namePanel.add(firstNamePanel);
                    namePanel.add(lastNamePanel);
                    JPanel descPanel = new JPanel();
                    descPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.BLACK), "Description:"));
                    JScrollPane descScroller = new JScrollPane(descArea);
                    descPanel.add(descScroller);
                    inputPanel.setLayout(new VerticalFlowLayout());
                    inputPanel.add(namePanel);
                    inputPanel.add(newCatPanel);
                    inputPanel.add(descPanel);
                    buttonPanel.add(addButton);
                    buttonPanel.add(clearButton);
                    buttonPanel.add(exitButton);
                    updateWindow();
                } else if ("Family".equals(selected)) {
                } else if ("Business".equals(selected)) {
                }
            }
        });
        comboPanel.add(comboBox);
        addPanel.add(comboPanel, BorderLayout.NORTH);
        addPanel.add(quickAddInfo, BorderLayout.CENTER);
        addPanel.add(buttonPanel, BorderLayout.SOUTH);
        contentPane.add(addPanel, BorderLayout.NORTH);
        updateWindow();
    }

    /** Handles all the theme change actions. */
    public void actionPerformed(ActionEvent evt) {
        boolean button = true;
        Component source = null;
        try {
            source = (JButton) (evt.getSource());
        } catch (Exception e) {
            button = false;
        }
        if (button) {
            if (source == cancelButton) {
                contentPane.removeAll();
                this.setVisible(true);
            }
        }
        if (defaultTheme.isSelected()) {
            setTheme("DEFAULT");
            propFile.setProperty("theme", "default");
        } else if (goldTheme.isSelected()) {
            setTheme("GOLD");
            propFile.setProperty("theme", "gold");
        } else if (silverTheme.isSelected()) {
            setTheme("SILVER");
            propFile.setProperty("theme", "silver");
        } else if (rubyTheme.isSelected()) {
            setTheme("RUBY");
            propFile.setProperty("theme", "ruby");
        } else if (darudeTheme.isSelected()) {
            setTheme("DARUDE");
            propFile.setProperty("theme", "darude");
        } else if (yellowTheme.isSelected()) {
            setTheme("YELLOW");
            propFile.setProperty("theme", "yellow");
        } else if (grayTheme.isSelected()) {
            setTheme("GRAY");
            propFile.setProperty("theme", "gray");
        } else if (charcoalTheme.isSelected()) {
            setTheme("CHARCOAL");
            propFile.setProperty("theme", "charcoal");
        } else if (metalTheme.isSelected()) {
            setTheme("METAL");
            propFile.setProperty("theme", "metal");
        } else if (oceanTheme.isSelected()) {
            setTheme("OCEAN");
            propFile.setProperty("theme", "ocean");
        }
    }

    /**
     * Dispatchs the key event for browing profiles with the left and right arrow keys.
     * 
     * @param e The KeyEvent.
     * @return  True if there is a current person shown and not editing profile.
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getID() == KeyEvent.KEY_RELEASED) {
            if (this.currentPerson != null && !this.editing) {
                String personName = currentPerson.getLastName() + ", " + currentPerson.getFirstName();
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    ArrayList<KeyValue> listOfPeople = DBHPInterface.getListOfPeopleByLastName();
                    int nextPerson = listOfPeople.indexOf(new KeyValue(0, personName)) + 1;
                    if (nextPerson >= listOfPeople.size()) nextPerson = 0;
                    try {
                        showProfile(new Person(listOfPeople.get(nextPerson).getKey()));
                    } catch (Exception exc) {
                    }
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    ArrayList<KeyValue> listOfPeople = DBHPInterface.getListOfPeopleByLastName();
                    int previousPerson = listOfPeople.indexOf(new KeyValue(0, personName)) - 1;
                    if (previousPerson < 0) previousPerson = listOfPeople.size() - 1;
                    try {
                        showProfile(new Person(listOfPeople.get(previousPerson).getKey()));
                    } catch (Exception exc) {
                    }
                }
                return true;
            }
            return false;
        }
        return false;
    }

    /**
     * Gets the current version of Hoi Polloi.
     *
     * @return The Hoi Polloi Version.
     */
    public String getVersion() {
        return hpVersion;
    }

    /**
     * Gets the Category JPanel to show at the bottom of showProfile/editProfile.
     *
     * @return The JPanel with Categories.
     */
    private JPanel getCategoryPanel() {
        JPanel categoryPanel = new JPanel();
        categoryPanel.add(new JLabel("Categories: "));
        int numCategoriesIn = this.currentPerson.getCategories().size();
        if (numCategoriesIn < 1) {
            categoryPanel.add(new JLabel("This person isn't in any categories yet."));
        } else {
            int counter = 1;
            for (Object cat : this.currentPerson.getCategories()) {
                String category = ((KeyValue) cat).getValue();
                int cid = ((KeyValue) cat).getKey();
                CategoryLabel cl = new CategoryLabel(category, this, this.currentPerson, cid);
                cl.setFont(new Font("Arial", Font.PLAIN, 10));
                categoryPanel.add(cl);
                if (counter < numCategoriesIn) {
                    categoryPanel.add(new JLabel(", "));
                }
                counter++;
            }
        }
        return categoryPanel;
    }

    /**
     * Not sure exactly, subclass for something. Brandon should javadoc this.
     */
    class FilterTreeSelectionListener implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent e) {
            JTree source = (JTree) e.getSource();
            setFilteredInfo(source);
        }
    }
}
