package com.pallas.unicore.client;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InvalidClassException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JWindow;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import org.unicore.User;
import com.pallas.unicore.client.controls.GenericFileFilter;
import com.pallas.unicore.client.controls.SimpleToolBar;
import com.pallas.unicore.client.dialogs.ConfigureSitesDialog;
import com.pallas.unicore.client.dialogs.GenericDialog;
import com.pallas.unicore.client.dialogs.PasswordDialog;
import com.pallas.unicore.client.dialogs.SetIdentityDialog;
import com.pallas.unicore.client.dialogs.UserDefaultsDialog;
import com.pallas.unicore.client.editor.KeyStoreEditor;
import com.pallas.unicore.client.explorer.SelectorDialog;
import com.pallas.unicore.client.menus.RecentFileMenu;
import com.pallas.unicore.client.panels.ImagePanel;
import com.pallas.unicore.client.panels.JPAPanel;
import com.pallas.unicore.client.panels.StatusPanel;
import com.pallas.unicore.client.themes.LookAndFeelManager;
import com.pallas.unicore.client.trees.ContainerNode;
import com.pallas.unicore.client.trees.JMCTree;
import com.pallas.unicore.client.trees.JPATree;
import com.pallas.unicore.client.trees.PanelNode;
import com.pallas.unicore.client.trees.JPATree.AddPluginAction;
import com.pallas.unicore.client.util.ClientPluginManager;
import com.pallas.unicore.client.util.ExtensionPlugable;
import com.pallas.unicore.client.util.PluginObjectInputStream;
import com.pallas.unicore.client.util.UnicorePlugable;
import com.pallas.unicore.client.xml.XMLObjectIO;
import com.pallas.unicore.client.xml.XMLObjectReader;
import com.pallas.unicore.client.xml.XMLObjectWriter;
import com.pallas.unicore.connection.Connection;
import com.pallas.unicore.container.JobContainer;
import com.pallas.unicore.extensions.Usite;
import com.pallas.unicore.requests.GetResources;
import com.pallas.unicore.requests.GetUsites;
import com.pallas.unicore.requests.GetVsites;
import com.pallas.unicore.requests.InitRandom;
import com.pallas.unicore.requests.SiteDetector;
import com.pallas.unicore.resourcemanager.ResourceManager;
import com.pallas.unicore.threadpool.IObserver;
import com.pallas.unicore.utility.DeleteAll;
import com.pallas.unicore.utility.UrlEncoder;
import com.pallas.unicore.utility.UserMessages;
import com.pallas.unicore.utility.Version;

/**
 * Main class for UNICORE Client Application.
 * 
 * @author Thomas Kentemich
 * @author Ralf Ratering
 * @version $Id: Client.java,v 1.9 2005/04/20 08:08:54 bschuller Exp $
 */
public class Client extends JFrame implements IObserver {

    static ResourceBundle res = ResourceBundle.getBundle("com.pallas.unicore.client.ResourceStrings");

    private static String clientVersion = res.getString("UNICORE") + Version.dotVersion();

    private static String CONFIGURATION_DIR_KEY = "com.pallas.unicore.configpath";

    private static String TESTGRID_TRUE_FALSE = "testgrid";

    private static boolean testGrid = false;

    private static SiteDetector detector = new SiteDetector();

    private static String helpSetName = res.getString("HELPSET_PATH");

    private static Logger logger = Logger.getLogger("com.pallas.unicore.client");

    private static String NO_GUI_KEY = "-nogui";

    private static boolean noGui = false;

    private static Vector pluginExtensionItems = new Vector();

    private static Vector pluginHelpSets = new Vector();

    private static ClientPluginManager pluginManager;

    private static Vector pluginSettingsItems = new Vector();

    static final int TIMING = (Integer.getInteger("TIMING") == null) ? 0 : Integer.getInteger("TIMING").intValue();

    private static String unicoreDir;

    private JMenuItem abortJobItem, holdJobItem, resumeJobItem;

    private ExitAction exitAction = new ExitAction();

    private JMenu extensionMenu;

    private HelpBroker hb;

    private HelpSet hs;

    private SimpleToolBar jmcToolBar;

    private JPanel jmcToolBarPanel;

    private JMCTree jmcTree;

    private JMenu jpaMenu, settingsMenu;

    /**
	* JPA "add" menu constants: 
	* used to define in which part of the "add" menu plugin entries go
	*/
    public static final String MENU_JOB = "job";

    public static final String MENU_TASK = "task";

    public static final String MENU_CONTROL = "control";

    public static final String MENU_FILEOPERATION = "fileoperation";

    public static final String MENU_OTHER = "other";

    private SimpleToolBar jpaToolBar;

    private JPanel jpaToolBarPanel;

    private JPATree jpaTree;

    private String keystoreName;

    private javax.swing.filechooser.FileFilter lastFileFilter;

    private JPanel leftPanel;

    private JMenuItem loadPluginsItem, userDefaultsItem, certificateItem, setIdentityItem, configureSitesItem, clearCacheItem;

    private ImagePanel logoPanel = new ImagePanel(ResourceManager.getImage(ResourceManager.UNICORE), false);

    private JMenuBar menuBar;

    private MenuListener menuListener = new MenuListener();

    private boolean noHelpSet = false;

    private OfflineAction offlineAction = new OfflineAction();

    private JCheckBoxMenuItem offlineItem;

    private Vector pluginJPAComponents = new Vector();

    private Vector pluginJMCComponents = new Vector();

    private Vector pluginJMCPopupComponents = new Vector();

    private RecentFileListener recentFileListener = new RecentFileListener();

    private RecentFileMenu recentFileMenu;

    private JMenuItem tutorItem, referenceItem, aboutPluginItem, aboutItem;

    private JSplitPane splitPane;

    private StatusPanel statusPanel = new StatusPanel();

    private JSplitPane treeSplitPane, mainSplitPane;

    private UserDefaults userDefaults;

    private GenericFileFilter xmlFilter = new GenericFileFilter(".xml"), ajoFilter = new GenericFileFilter(".ajo");

    /**
	 * Get a reference to the static plugin manager
	 * 
	 * @return the one and only ClientPluginManager
	 */
    public static final ClientPluginManager getPluginManager() {
        return pluginManager;
    }

    /**
	 * Check if we are running in commandline mode
	 * 
	 * @return The RunningMode value
	 */
    public static final boolean getRunningMode() {
        return noGui;
    }

    /**
	 * Get config directory (default .unicore)
	 * 
	 * @return path to config directory
	 */
    public static final String getUnicoreDir() {
        return unicoreDir;
    }

    /**
	 * Main class for UNICORE Client
	 * 
	 * @param args
	 *            Command line arguments
	 */
    public static void main(String args[]) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals(NO_GUI_KEY)) {
                    logger.info("Running in commandline mode");
                    noGui = true;
                }
            }
            if (System.getProperty(CONFIGURATION_DIR_KEY) != null) {
                unicoreDir = System.getProperty(CONFIGURATION_DIR_KEY);
                logger.info("Using configuration path: " + unicoreDir);
            }
            if (System.getProperty(TESTGRID_TRUE_FALSE) != null) {
                if (System.getProperty(TESTGRID_TRUE_FALSE).equals("true")) {
                    testGrid = true;
                }
            }
            if (unicoreDir == null) {
                String userhome = System.getProperty("user.home");
                String username = System.getProperty("user.name");
                unicoreDir = userhome + File.separator + ".unicore";
            }
            ResourceManager.loadImages();
            if (!Client.getRunningMode()) {
                Image splashImage = ResourceManager.getImage(ResourceManager.UNICORE);
                ImagePanel splashPanel = new ImagePanel(splashImage, false);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                int logoXsize = splashImage.getWidth(splashPanel) + 50;
                int logoYsize = splashImage.getHeight(splashPanel) + 50;
                int splashXpos = ((int) screenSize.getWidth() - logoXsize) / 2;
                int splashYpos = ((int) screenSize.getHeight() - logoYsize) / 2;
                JWindow splash = new JWindow();
                splash.setLocation(splashXpos, splashYpos);
                splash.getContentPane().add(splashPanel, BorderLayout.CENTER);
                splash.pack();
                splash.show();
                Client client = new Client();
                client.setIconImage(ResourceManager.getImage(ResourceManager.UNICOREICON));
                UserDefaults userDefaults = ResourceManager.getUserDefaults();
                client.setSize(userDefaults.getWindowSize());
                client.setLocation(userDefaults.getWindowPos());
                splash.dispose();
                client.show();
                client.setDividerLocations();
                client.init();
            } else {
                Client client = new Client();
                CommandLineClient clClient = new CommandLineClient(args);
                System.exit(0);
            }
            long end = System.currentTimeMillis();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected exception occured. Terminating Client...", e);
            System.exit(-1);
        }
    }

    /**
	 * Constructor builds gui for UNICORE Client and initializes ResourceManager
	 * and PluginManager
	 */
    public Client() {
        super("UNICORE Client");
        Locale.setDefault(Locale.US);
        String jreVersion = System.getProperty("java.version");
        if (jreVersion.compareTo("1.4.0") < 0) {
            UserMessages.warning("You are using Java Runtime Environment version: " + jreVersion + "\nThe UNICORE Client requires at least version 1.4.0 to work properly.");
        }
        ResourceManager.init(this);
        if (pluginManager == null) {
            pluginManager = new ClientPluginManager();
        }
        userDefaults = ResourceManager.getUserDefaults();
        new InitRandom().start();
        if (!noGui) {
            setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent evt) {
                    exitInstance();
                }
            });
            System.setProperty("sun.awt.exception.handler", "com.pallas.unicore.client.UncaughtExceptionHandler");
            initHelpSystem();
            jpaTree = new JPATree(this);
            jmcTree = new JMCTree(this);
            buildMenuBar();
            buildToolBars();
            buildControls();
            updateLookAndFeel();
            updateTitle();
            pack();
        }
    }

    /**
	 * Add a panel to the data area, which is the one on the right side next to
	 * jpa and jmc tree.
	 * 
	 * @param panel
	 *            JPanel to be added.
	 */
    public final void addToDataArea(JPanel panel) {
        if (panel.equals(mainSplitPane.getRightComponent())) {
            return;
        }
        int dividerLocation = mainSplitPane.getDividerLocation();
        Component component = mainSplitPane.getRightComponent();
        if (component != null) {
            mainSplitPane.remove(component);
        }
        mainSplitPane.setRightComponent(panel);
        mainSplitPane.setDividerLocation(dividerLocation);
    }

    /**
	 * Empty the data area on the right.
	 */
    public final void clearDataArea() {
        int dividerLocation = mainSplitPane.getDividerLocation();
        Component component = mainSplitPane.getRightComponent();
        if (component != null) {
            mainSplitPane.remove(component);
        }
        mainSplitPane.setRightComponent(logoPanel);
        mainSplitPane.setDividerLocation(dividerLocation);
    }

    /**
	 * Return the JMC tree
	 * 
	 * @return The JMCTree
	 */
    public final JMCTree getJMCTree() {
        return this.jmcTree;
    }

    /**
	 * Return the JPA tree
	 * 
	 * @return The JPATree
	 */
    public final JPATree getJPATree() {
        return this.jpaTree;
    }

    /**
	 * Return the status panel
	 * 
	 * @return The status panel
	 */
    public StatusPanel getStatusPanel() {
        return this.statusPanel;
    }

    /**
	 * Load plugin via PluginManager
	 */
    public synchronized void loadPlugins() {
        if (ResourceManager.getKeystoreManager() == null || !ResourceManager.getKeystoreManager().hasOpenKeystore()) {
            return;
        }
        pluginManager.stopPlugins();
        pluginManager.scanPlugins(userDefaults.getPluginDirectory());
        pluginManager.startPlugins(this);
        jpaTree.buildPopupMenu();
        updatePluginMenus();
    }

    /**
	 * Update resource cache
	 *  
	 */
    public synchronized void loadResources() {
        SwingUtilities.invokeLater(detector);
        jmcTree.getRoot().setState(PanelNode.STATE_UPDATING);
        jmcTree.updateNode(jmcTree.getRoot());
    }

    /**
	 * Stop all resource update threads
	 */
    public void stopLoadResources() {
        detector.stopThreads();
    }

    /**
	 * Update currently selected JPAPanel. This method may be used by plugins
	 * that changed the panel's settings and want to update it.
	 */
    public final void updateDataArea() {
        if (mainSplitPane.getRightComponent() instanceof JPAPanel) {
            JPAPanel panel = (JPAPanel) mainSplitPane.getRightComponent();
            panel.updateValues();
            panel.repaint();
        }
    }

    /**
	 * Callbacks for resource chache update threads
	 * 
	 * @param theObservable
	 *            request that returned
	 * @param changeCode
	 *            object informing about change
	 */
    public void observableUpdate(Object theObservable, Object changeCode) {
        if (theObservable instanceof GetUsites) {
            setStatusMessage("Updating UNICORE sites in Job Preparation and Job Monitor");
            jmcTree.updateUsites(ResourceManager.getUsites());
            jpaTree.updateUsites();
        } else if (theObservable instanceof GetVsites) {
            GetVsites getVsites = (GetVsites) theObservable;
            Usite usite = getVsites.getUsite();
            jmcTree.updateVsites(usite, ResourceManager.getVsites(usite));
        } else if (theObservable instanceof GetResources) {
            GetResources getResources = (GetResources) theObservable;
            jmcTree.updateVsiteIcon(getResources.getVsite());
        } else if (theObservable instanceof SiteDetector) {
            Integer nrOfThreads = (Integer) changeCode;
            String message = "";
            if (nrOfThreads.intValue() == 0) {
                message = "Resource update finished.";
                ResourceManager.writeCacheToDisk();
                jmcTree.getRoot().setUpToDate();
                jmcTree.updateNode(jmcTree.getRoot());
                jmcTree.enableDefaultActions();
                statusPanel.setProgressValue(5);
                statusPanel.stopProgressBar();
                logger.info(ResourceManager.getResourceCache().toString());
            } else {
                message = "Updating resources - running requests: " + nrOfThreads.intValue();
            }
            jmcTree.setRequestInfo(message);
            setStatusMessage(message);
        }
    }

    /**
	 * Display a file chooser, open the selected job and add it as a new branch
	 * into the jpa tree.
	 */
    public void openJob() {
        File file = getSelectedFileFromJPA();
        if (file == null) {
            file = new File(userDefaults.getJobDirectory());
        }
        SelectorDialog chooser = ResourceManager.getSelectorDialog(file, null);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        setFileFilters(chooser);
        chooser.setTitle("UNICORE: Open Job");
        int returnVal = chooser.showDialog();
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile();
            lastFileFilter = chooser.getSelectedFileFilter();
            checkOpenJob(file.getAbsolutePath());
            userDefaults.setJobDirectory(chooser.getSelectedFile().getParent());
            recentFileMenu.addRecentFile(file.getAbsolutePath());
        }
    }

    /**
	 * Save the currently selected job in the jpa tree to file. If no filename
	 * has been selected prviously, call saveJobAs().
	 */
    public void saveJob() {
        jpaTree.updateCurrentJob(false);
        JobContainer job = jpaTree.getCurrentTopLevelJob();
        if (job == null) {
            return;
        }
        if (job.getFilename() != null) {
            writeJobToFile(job);
            writeJobToFileXML(job);
            setStatusMessage(job + " saved as: " + job.getFilename());
        } else {
            saveJobAs();
        }
    }

    /**
	 * Popup a file chooser dialog and write currently selected job to chosen
	 * filename.
	 */
    public void saveJobAs() {
        JobContainer job = jpaTree.getCurrentTopLevelJob();
        if (job == null) {
            return;
        }
        jpaTree.updateCurrentJob(false);
        File file = getSelectedFileFromJPA();
        if (file == null) {
            file = new File(userDefaults.getJobDirectory());
        }
        SelectorDialog chooser = ResourceManager.getSelectorDialog(file, null);
        setFileFilters(chooser);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setTitle("UNICORE: Save job <" + job + ">");
        int returnVal = chooser.showDialog();
        try {
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filename = chooser.getSelectedFile().getCanonicalPath();
                lastFileFilter = chooser.getSelectedFileFilter();
                if (!filename.endsWith(".ajo") && !filename.endsWith(".xml")) {
                    if (lastFileFilter == this.xmlFilter) {
                        filename = addOrReplaceEnding(filename, "xml");
                    } else {
                        filename = addOrReplaceEnding(filename, "ajo");
                    }
                }
                job.setFilename(filename);
                writeJobToFile(job);
                userDefaults.setJobDirectory(chooser.getSelectedFile().getParent());
                writeJobToFileXML(job);
                if (userDefaults.getJobStorageFormat().equals(UserDefaults.XML_FORMAT)) {
                    job.setFilename(addOrReplaceEnding(job.getFilename(), "xml"));
                } else {
                    job.setFilename(addOrReplaceEnding(job.getFilename(), "ajo"));
                }
                setStatusMessage(job + " saved as: " + job.getFilename());
            }
        } catch (IOException ioe) {
            UserMessages.error(res.getString("SAVE_JOB_FAILED"), ioe.getMessage());
            logger.log(Level.SEVERE, "", ioe);
        }
    }

    /**
	 * Edit an existing keystore. If no keystore exists build a new empty one
	 * and edit it.
	 */
    private void editKeystore() {
        if (checkKeystoreExists()) {
            final KeyStoreEditor keystoreEditor = new KeyStoreEditor();
            keystoreEditor.addPropertyChangeListener(new KeystoreChangeListener());
            try {
                JDialog dialog = new JDialog(this, "Keystore Editor", true);
                dialog.getContentPane().add(keystoreEditor);
                dialog.addWindowListener(new WindowAdapter() {

                    public void windowClosing(WindowEvent ev) {
                        keystoreEditor.exitKeystore();
                    }
                });
                dialog.setSize(600, 300);
                dialog.setLocationRelativeTo(this);
                dialog.show();
                dialog.dispose();
                if (ResourceManager.isKeystoreAvailable()) {
                    this.setStatusMessage("Keystore available");
                } else {
                    this.setStatusMessage("No Keystore available.");
                }
            } catch (Throwable t) {
                logger.log(Level.SEVERE, "Uncaught exception in keystore editor.", t);
            }
        }
    }

    /**
	 * Exit the client instance. First we look for unsaved jobs, then we make
	 * another security prompt if we really want to close the instance.
	 */
    private void exitInstance() {
        Vector unsavedJobs = jpaTree.getUnsavedJobs();
        if (unsavedJobs.size() > 0) {
            String message = res.getString("UNSAVED_JOBS");
            for (int i = 0; i < unsavedJobs.size(); i++) {
                message += " - " + unsavedJobs.elementAt(i) + "\n";
            }
            message += res.getString("CLOSE_ANYWAY");
            if (JOptionPane.showConfirmDialog(this.getContentPane(), message, res.getString("UNSAVED_JOBS_TITLE"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                stopAndGetOut();
            }
        } else if (JOptionPane.showConfirmDialog(this.getContentPane(), res.getString("EXIT_CONFIRM"), res.getString("EXIT_TITLE"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            stopAndGetOut();
        }
    }

    /**
	 * Generate a message with info about loaded plugins
	 * 
	 * @return message string
	 */
    private String genPluginInfo() {
        String msg = "";
        Vector plugins = pluginManager.getPluginClasses();
        if (plugins != null) {
            for (int i = 0; i < plugins.size(); i++) {
                UnicorePlugable plugable = (UnicorePlugable) plugins.elementAt(i);
                if (plugable.getPluginInfo() != null) msg += "-" + plugable.getPluginInfo() + "\n\n";
            }
        }
        return msg;
    }

    private File getSelectedFileFromJPA() {
        JobContainer job = jpaTree.getCurrentTopLevelJob();
        if (job == null) {
            return null;
        }
        String filename = job.getFilename();
        if (filename == null) {
            return null;
        }
        return new File(filename);
    }

    private void init() {
        detector.addObserver(this);
        setStatusMessage(clientVersion);
        statusPanel.setProgressMaxValue(6);
        statusPanel.setProgressString(res.getString("SEARCHING_KEYSTORE"));
        keystoreName = userDefaults.getKeyStoreName();
        if (!ResourceManager.isKeystoreAvailable()) {
            setStatusMessage("No Keystore available.");
            if (!testGrid) {
                if (JOptionPane.showConfirmDialog(this, "You do not have a keystore installed, yet. Create initial keystore ?", "UNICORE: Build initial keystore?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    processBuildKeystore(true, true);
                } else {
                    statusPanel.setProgressString("");
                    setStatusMessage("No keystore available.");
                }
            } else {
                if (JOptionPane.showConfirmDialog(this, "You do not have a keystore installed, yet." + "\nThe UNICORE client can generate an initial keystore containing" + "\ncertificates that authorize you to access the UNICORE Testgrid." + "\nCreate initial keystore ?", "UNICORE: Build initial keystore?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    processBuildKeystore(false, true);
                } else {
                    statusPanel.setProgressString("");
                    setStatusMessage("No keystore available.");
                }
            }
        }
        if (ResourceManager.isKeystoreAvailable()) {
            processOpenKeystore();
        }
    }

    /**
	 * Initialize the java help system
	 */
    private void initHelpSystem() {
        if (!isJavaHelpAvailable()) {
            return;
        }
        try {
            ClassLoader loader = Client.class.getClassLoader();
            URL tmpURL = loader.getResource(helpSetName);
            if (tmpURL == null) {
                throw new Exception(res.getString("HELPSET_NOT_FOUND"));
            } else {
                URL hsURL = new URL(UrlEncoder.decodeURL(tmpURL.toString()));
                hs = new HelpSet(loader, hsURL);
            }
        } catch (Exception ee) {
            UserMessages.warning(res.getString("HELPSET_NOT_OPENED") + helpSetName + ".", ee.getMessage());
            setStatusMessage("HELPSET_NOT_FOUND" + helpSetName);
            noHelpSet = true;
            return;
        }
        hb = hs.createHelpBroker();
    }

    /**
	 * Helper method initializes menu items
	 */
    private void initMenuItem(JMenuItem item) {
        item.setEnabled(false);
        item.addActionListener(menuListener);
    }

    /**
	 * Helper method initializing menu item including a CTRL-Shortcut
	 */
    private void initMenuItem(JMenuItem item, int keyCode) {
        item.setAccelerator(KeyStroke.getKeyStroke(keyCode, ActionEvent.CTRL_MASK));
        initMenuItem(item);
    }

    /**
	 * Helper method initializes toolbar buttons
	 */
    private void initToolbarButton(JButton button) {
        button.setEnabled(false);
        button.addActionListener(menuListener);
    }

    /**
	 * Check if we have java help available in the current run time environment
	 * 
	 * @return true if java help is available
	 */
    private final boolean isJavaHelpAvailable() {
        try {
            Class cls = Class.forName("javax.help.HelpSet");
        } catch (ClassNotFoundException cnfe) {
            return false;
        }
        return true;
    }

    /**
	 * Load a job from file and insert it into the JPA tree as a new branch.
	 */
    private void loadJob(final String filename, final int index) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        setStatusMessage(res.getString("LOADING JOB"));
        statusPanel.startProgressBar();
        statusPanel.setProgressString(res.getString("LOADING JOB"));
        Thread loadThread = new Thread() {

            public void run() {
                final JobContainer jc;
                final JobContainer jcXMLbinary;
                String name = filename;
                if (filename.endsWith(".xml")) {
                    jc = readJobFromFileXML(filename);
                    jcXMLbinary = readJobFromFileXMLbinary(filename);
                    name = addOrReplaceEnding(filename, "ajo");
                    if (jcXMLbinary != null) {
                        jcXMLbinary.setFilename(name);
                        try {
                            jcXMLbinary.checkContents();
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "", e);
                        }
                        recursiveSetUser(jcXMLbinary);
                    }
                } else {
                    jc = readJobFromFile(filename);
                    jcXMLbinary = null;
                }
                if (jc == null) {
                    statusPanel.stopProgressBar();
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    return;
                }
                jc.setFilename(filename);
                recursiveSetUser(jc);
                statusPanel.setProgressString(res.getString("CONTACTING_SITES"));
                setStatusMessage(res.getString("CONTACTING_SITES_EXPLICIT"));
                Runnable runner = new Runnable() {

                    public void run() {
                        if (jcXMLbinary != null) {
                            jpaTree.addJobGroupAt(index, jcXMLbinary, true);
                        }
                        jpaTree.addJobGroupAt(index, jc, true);
                        Date now = new Date();
                        jc.setModifiedTime(now);
                        jc.setSavedTime(now);
                        if (jcXMLbinary != null) {
                            jcXMLbinary.setModifiedTime(now);
                            jcXMLbinary.setSavedTime(now);
                        }
                    }
                };
                SwingUtilities.invokeLater(runner);
                statusPanel.stopProgressBar();
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        };
        loadThread.start();
    }

    /**
	 * Ask for keystore password and open it. For loading a ResourceManager
	 * routine will be used, because that class also manages the keystore
	 * accesses.
	 * 
	 * @return true if keystore was successfully opened
	 */
    private boolean openKeystore() {
        if (ResourceManager.getKeystoreManager() != null && ResourceManager.getKeystoreManager().hasOpenKeystore()) {
            return true;
        }
        PasswordDialog pwdDlg = new PasswordDialog(this, res.getString("PASSWORD_TITLE"));
        int count = 3;
        boolean opened = false;
        while (count > 0 && !opened) {
            switch(count) {
                case 3:
                    pwdDlg.setStatusMessage(res.getString("ENTER_KEYSTORE_PASSWORD") + keystoreName);
                    break;
                case 2:
                    pwdDlg.setStatusMessage(res.getString("WRONG_PASSWORD_2_TRIES"));
                    break;
                case 1:
                    pwdDlg.setStatusMessage(res.getString("WRONG_PASSWORD_LAST_TRY"));
                    break;
            }
            int value = pwdDlg.showDialog();
            if (value == GenericDialog.CANCEL_OPTION) {
                return opened;
            }
            count--;
            char[] keystorePassword = pwdDlg.getPassword();
            if (keystorePassword == null) {
                return false;
            }
            try {
                opened = ResourceManager.loadKeystore(keystoreName, keystorePassword);
            } catch (Exception e) {
                logger.info("Could not unlock keystore, because Exception occured: " + e.getMessage());
                opened = false;
                break;
            }
        }
        return opened;
    }

    private boolean processBuildKeystore(boolean createEmptyKeystore, boolean createAtFirst) {
        if (createEmptyKeystore) {
            statusPanel.setProgressString("Building empty keystore...");
        } else {
            statusPanel.setProgressString("Contacting certificate service...");
        }
        try {
            statusPanel.setProgressValue(1);
            statusPanel.startWaiting();
            buildInitialKeystore(createEmptyKeystore);
            statusPanel.stopWaiting();
            setStatusMessage("New Keystore");
        } catch (Exception e) {
            if (!createEmptyKeystore) {
                logger.log(Level.SEVERE, "Error in communication with Certificate Service.", e);
                UserMessages.warning(res.getString("CERT_SERVICE_FAILED"));
            } else {
                String retry;
                if (createAtFirst) {
                    retry = res.getString("CREATE_NEW_KEYSTORE");
                } else {
                    retry = res.getString("TRY_AGAIN");
                }
                logger.log(Level.SEVERE, "Building an empty keystore failed. ", e);
                UserMessages.warning(res.getString("BUILDING_EMPTY_KEYSTORE_FAILED") + retry);
            }
            resetStatusPanel();
            jmcTree.getRoot().setUpToDate();
            return false;
        }
        resetStatusPanel();
        jmcTree.getRoot().setUpToDate();
        return true;
    }

    private boolean processOpenKeystore() {
        statusPanel.setProgressValue(2);
        statusPanel.setProgressString(res.getString("OPENING_KEYSTORE"));
        if (openKeystore()) {
            ResourceManager.initUsers();
            statusPanel.setProgressValue(3);
            statusPanel.setProgressString(res.getString("LOADING_PLUGINS"));
            Runnable runner = new Runnable() {

                public void run() {
                    loadPlugins();
                    statusPanel.setProgressValue(4);
                    statusPanel.setProgressString(res.getString("ENABLING_MENUS"));
                    getJPATree().selectRootNode();
                    offlineItem.setSelected(ResourceManager.isOffline());
                    loadPluginsItem.setEnabled(true);
                    if (ResourceManager.isOffline()) {
                        jmcTree.getRoot().setUpToDate();
                        jmcTree.updateNode(jmcTree.getRoot());
                        jmcTree.enableDefaultActions();
                        resetStatusPanel();
                        statusPanel.setConnected(false);
                        return;
                    } else {
                        statusPanel.setProgressValue(5);
                        statusPanel.setProgressString("Updating resource cache.");
                        loadResources();
                        statusPanel.setConnected(true);
                    }
                    setStatusMessage(clientVersion);
                }
            };
            SwingUtilities.invokeLater(runner);
        } else {
            UserMessages.info(res.getString("KEYSTORE_NOT_OPEN"));
            setStatusMessage("No Keystore has been openend.");
            resetStatusPanel();
            return false;
        }
        resetStatusPanel();
        return true;
    }

    /**
	 * Read a job from file.
	 * 
	 * @param name
	 *            filename
	 * @return new JobContainer
	 */
    private JobContainer readJobFromFile(String name) {
        byte[] in = null;
        long tstart = System.currentTimeMillis();
        long timing = tstart;
        try {
            File file = new File(name);
            FileInputStream f = new FileInputStream(file);
            DataInputStream dis = new DataInputStream(new BufferedInputStream(f));
            in = new byte[f.available()];
            dis.readFully(in);
            dis.close();
            f.close();
        } catch (IOException e) {
            UserMessages.error(res.getString("COULD_NOT_LOAD_FILE"), e.getMessage());
            return null;
        }
        long tstop = System.currentTimeMillis();
        if (TIMING > 1) {
            logger.info("TIMING[2]: Reading from file " + name + " to byte array in " + (tstop - tstart) + " milliseconds");
        }
        tstart = tstop;
        Object o = null;
        if (in != null) {
            PluginObjectInputStream ois = null;
            try {
                ois = new PluginObjectInputStream(new ByteArrayInputStream(in), pluginManager);
                o = ois.readObject();
                ois.close();
            } catch (ClassNotFoundException cnfe) {
                UserMessages.error(res.getString("PLUGIN_NOT_AVAILABLE"), cnfe.getMessage());
                return null;
            } catch (InvalidClassException cnfe) {
                logger.log(Level.SEVERE, "", cnfe);
                UserMessages.error(res.getString("INCOMPATIBLE_JOB"));
                return null;
            } catch (Exception e) {
                logger.log(Level.SEVERE, "", e);
                UserMessages.error("Error reading object " + o + " from UNICORE job " + name, e.getMessage());
                return null;
            }
            if (o instanceof com.pallas.unicore.container.JobContainer) {
                JobContainer jc = (JobContainer) o;
                tstop = System.currentTimeMillis();
                if (TIMING > 1) {
                    logger.info("TIMING[2]: Java Object Deserialization from byte array in " + (tstop - tstart) + " milliseconds");
                }
                timing = tstop - timing;
                if (TIMING > 0) {
                    logger.info("TIMING[1]: Reading job in binary format from file " + name + " in " + timing + " milliseconds");
                }
                return jc;
            }
        }
        return null;
    }

    /**
	 * Read a job from file in XML format.
	 * 
	 * @param filename
	 *            filename
	 * @return new JobContainer
	 */
    private JobContainer readJobFromFileXML(String filename) {
        if (!filename.endsWith(".xml")) {
            return null;
        }
        Object o = null;
        try {
            long timing = System.currentTimeMillis();
            XMLObjectReader din = new XMLObjectReader(new File(filename));
            o = din.readObjectXML();
            timing = System.currentTimeMillis() - timing;
            if (TIMING > 0) {
                logger.info("TIMING[1]: Reading job in XML format from file " + filename + " in " + timing + " milliseconds");
            }
        } catch (FileNotFoundException fnfe) {
            UserMessages.error(res.getString("COULD_NOT_LOAD_FILE"), fnfe.getMessage());
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while loading file " + filename, e);
            return null;
        }
        if (o instanceof com.pallas.unicore.container.JobContainer) {
            JobContainer jc = (JobContainer) o;
            if (XMLObjectIO.DEBUG > 1) {
                jc.setName(jc.getName() + "_XML");
            }
            return jc;
        } else {
            return null;
        }
    }

    /**
	 * Read a job from file in XML format.
	 * 
	 * @param filename
	 *            filename
	 * @return new JobContainer
	 */
    private JobContainer readJobFromFileXMLbinary(String filename) {
        if (!filename.endsWith(".xml")) {
            return null;
        }
        Object o = null;
        try {
            XMLObjectReader din = new XMLObjectReader(new File(filename));
            o = din.readObjectXMLbinary();
        } catch (FileNotFoundException fnfe) {
            UserMessages.error(res.getString("COULD_NOT_LOAD_FILE"), fnfe.getMessage());
            logger.log(Level.SEVERE, "", fnfe);
            return null;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            return null;
        }
        if (o instanceof com.pallas.unicore.container.JobContainer) {
            JobContainer jc = (JobContainer) o;
            jc.setName(jc.getName() + "_XMLbinary");
            return jc;
        } else {
            return null;
        }
    }

    /**
	 * Recursively fill in the user email address into a job group and its sub
	 * job groups.
	 * 
	 * @param jc
	 *            job container
	 */
    private void recursiveSetUser(JobContainer jc) {
        UserDefaults defaults = ResourceManager.getUserDefaults();
        String defaultUserEmail = defaults.getUserEmail();
        String defaultXlogin = defaults.getXlogin();
        String defaultProject = defaults.getProject();
        String userEmail;
        String xlogin;
        String project;
        User jcUser = jc.getUser();
        X509Certificate jcCert = (jcUser == null) ? null : jcUser.getCertificate();
        User defaultUser = null;
        try {
            defaultUser = ResourceManager.getUser(jc.getVsite());
        } catch (Exception e) {
            logger.log(Level.WARNING, "Probably harmless exception while setting user", e);
        }
        if (defaultUser == null) {
            defaultUser = ResourceManager.getUser();
            logger.info("Setting user to" + defaultUser);
            return;
        }
        if (defaultUser == null) {
            UserMessages.warning("Cannot set user for job " + jc + "\nReason: No default user available in keystore.");
            return;
        }
        X509Certificate currentCert = defaultUser.getCertificate();
        String message = "";
        if (currentCert != null && currentCert.equals(jcCert)) {
            userEmail = jcUser.getEmailAddress();
            xlogin = jcUser.getXlogin();
            project = jcUser.getProject();
            if (userEmail == null || userEmail.equals("")) {
                userEmail = defaultUserEmail;
                if (!defaultUserEmail.equals("")) {
                    message = message + "\n" + res.getString("SET_EMAIL_DEFAULT") + defaultUserEmail;
                }
            }
            if (xlogin == null || xlogin.equals("")) {
                xlogin = defaultXlogin;
                if (!defaultXlogin.equals("")) {
                    message = message + "\n" + res.getString("SET_XLOGIN_DEFAULT") + defaultXlogin;
                }
            }
            if (project == null || project.equals("")) {
                project = defaultProject;
                if (!defaultProject.equals("")) {
                    message = message + "\n" + res.getString("SET_PROJECT_DEFAULT") + defaultProject;
                }
            }
        } else {
            userEmail = defaultUser.getEmailAddress();
            xlogin = defaultUser.getXlogin();
            project = defaultUser.getProject();
            if (jcUser != null && jcUser.getCertificate() != null) {
                message = "Job " + jc + " contains unavailable identity with certificate:\n" + jcUser.getCertificate().getSubjectDN() + "\nJob certificate will now be set to current identity:\n" + defaultUser.getCertificate().getSubjectDN();
            } else {
                logger.info("Job " + jc + " contains invalid user <" + jcUser + ">.\n" + "Setting user to " + defaultUser.getCertificate().getSubjectDN());
            }
        }
        if (!message.equals("")) {
            UserMessages.info(message);
        }
        User newUser = null;
        try {
            newUser = new User(defaultUser.getCertificateChain(), userEmail);
        } catch (java.security.cert.CertificateException ce) {
            UserMessages.warning("Could not set user certificate.", ce.getMessage());
            return;
        }
        newUser.setXlogin(xlogin);
        newUser.setProject(project);
        jc.setUser(newUser);
        Vector subJobs = jc.getSubJobs();
        for (int i = 0; i < subJobs.size(); i++) {
            recursiveSetUser((JobContainer) subJobs.elementAt(i));
        }
    }

    /**
	 * compare filenames ignoring the .xml or .ajo extensions
	 */
    private String removeSuffixAJOorXML(String filename) {
        if (filename.endsWith(".ajo") || filename.endsWith(".xml")) {
            return filename.substring(0, filename.length() - 4);
        } else {
            return filename;
        }
    }

    private void resetStatusPanel() {
        statusPanel.stopWaiting();
        statusPanel.stopProgressBar();
        statusPanel.setProgressValue(0);
        statusPanel.setProgressString("");
    }

    /**
	 * Set split pane dividers of main GUI to their default locations.
	 */
    private void setDividerLocations() {
        treeSplitPane.setDividerLocation(0.5);
        mainSplitPane.setDividerLocation(0.28);
    }

    private void setFileFilters(SelectorDialog chooser) {
        if (lastFileFilter == null) {
            if (userDefaults.getJobStorageFormat().equals("xml")) {
                lastFileFilter = xmlFilter;
            } else {
                lastFileFilter = ajoFilter;
            }
        }
        chooser.addFileFilter(ajoFilter);
        chooser.addFileFilter(xmlFilter);
        chooser.setSelectedFileFilter(lastFileFilter);
    }

    /**
	 * Set the offline mode in Client
	 * 
	 * @param offline
	 *            true if client should go offline
	 */
    public void setOfflineMode(boolean offline) {
        ResourceManager.setOfflineMode(offline);
        statusPanel.setConnected(false);
        jpaTree.enableDefaultActions();
        jmcTree.enableDefaultActions();
        if (!offline) {
            statusPanel.setConnected(true);
            loadResources();
        }
        updateTitle();
    }

    /**
	 * Set client status bar to message string
	 * 
	 * @param message
	 *            String to be displayed
	 */
    public final synchronized void setStatusMessage(String message) {
        String identity = "no identity";
        if (userDefaults != null) {
            identity = userDefaults.getDefaultIdentity();
        }
        statusPanel.setStatusMessage(message);
        logger.log(Level.FINER, message);
        statusPanel.setIdentity(identity);
        repaint();
    }

    /**
	 * Popup about dialog
	 */
    private void showAboutInfo() {
        JOptionPane.showMessageDialog(this, genMessage(), res.getString("ABOUT"), JOptionPane.INFORMATION_MESSAGE);
    }

    /**
	 * Popup plugin info dialog
	 */
    private void showPluginInfo() {
        final JDialog infoDialog = new JDialog(this, res.getString("PLUGIN_TITLE"), true);
        JTextArea textArea = new JTextArea(genPluginInfo());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new TitledBorder(new EmptyBorder(10, 10, 10, 10), res.getString("LOADED_PLUGINS")));
        JButton closeButton = new JButton(res.getString("CLOSE"));
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                infoDialog.dispose();
            }
        });
        infoDialog.getContentPane().add(scrollPane, BorderLayout.CENTER);
        infoDialog.getContentPane().add(closeButton, BorderLayout.SOUTH);
        infoDialog.setSize(400, 300);
        infoDialog.setLocationRelativeTo(this);
        infoDialog.show();
    }

    private void showSetIdentityDialog() {
        SetIdentityDialog dialog = new SetIdentityDialog(this);
        dialog.show();
    }

    private void showConfigureSitesDialog() {
        ConfigureSitesDialog dialog = new ConfigureSitesDialog(this);
        dialog.show();
    }

    /**
	 * Popup user defaults dialog
	 */
    private void showUserDefaultsDialog() {
        UserDefaultsDialog dialog = new UserDefaultsDialog(this);
        dialog.addPropertyChangeListener(new ProxyEnabledListener());
        dialog.show();
    }

    /**
	 * Stop everything and exit client
	 */
    private final void stopAndGetOut() {
        Vector plugins = pluginManager.getPluginClasses();
        if (plugins != null) {
            for (int i = 0; i < plugins.size(); i++) {
                UnicorePlugable plugable = (UnicorePlugable) plugins.elementAt(i);
                plugable.stopPlugin();
            }
        }
        String dir = Connection.getSessionDir();
        if (dir != null) {
            File sessionDir = new File(dir);
            try {
                DeleteAll.deleteAll(sessionDir);
            } catch (IOException e) {
                UserMessages.warning("Could not delete temporary session directory: " + dir);
                logger.log(Level.SEVERE, "Could not delete session dir:" + dir, e);
            }
        }
        userDefaults.setWindowSize(getSize());
        userDefaults.setWindowPos(getLocation());
        userDefaults.setStartOffline(ResourceManager.isOffline());
        userDefaults.writeToFile();
        System.exit(0);
    }

    private void updateJPAAddMenu() {
        int index = 0;
        jpaMenu.removeAll();
        jpaMenu.add(jpaTree.getAddSubJobAction());
        jpaMenu.addSeparator();
        jpaMenu.addSeparator();
        jpaMenu.add(jpaTree.getAddDoNAction());
        jpaMenu.add(jpaTree.getAddDoRepeatAction());
        jpaMenu.add(jpaTree.getAddHoldJobAction());
        jpaMenu.add(jpaTree.getAddIfThenElseAction());
        jpaMenu.addSeparator();
        jpaMenu.add(jpaTree.getAddImportAction());
        jpaMenu.add(jpaTree.getAddExportAction());
        jpaMenu.add(jpaTree.getAddTransferAction());
        jpaMenu.add(jpaTree.getAddFileOperationAction());
        jpaMenu.addSeparator();
        jpaMenu.add(jpaTree.getRemoveAction());
        Vector pluginActions = jpaTree.getAddPluginActions();
        String entryPoint = null;
        int job = 0, task = 0, control = 0, file = 0;
        for (int i = 0; i < pluginActions.size(); i++) {
            entryPoint = ((AddPluginAction) pluginActions.elementAt(i)).getMenuEntryPoint();
            if (MENU_JOB.equals(entryPoint)) {
                index = 1 + job;
                job++;
            } else if (MENU_CONTROL.equals(entryPoint)) {
                index = 7 + job + task;
                control++;
            } else if (MENU_FILEOPERATION.equals(entryPoint)) {
                index = 12 + job + task + control;
                file++;
            } else if (MENU_OTHER.equals(entryPoint)) {
                index = 14 + job + task + control + file;
            } else {
                index = 2 + job + task;
                task++;
                entryPoint = MENU_TASK + " by default";
            }
            logger.finest("Inserting plugin " + ((AbstractAction) pluginActions.elementAt(i)).getValue(javax.swing.Action.NAME) + " into <" + entryPoint + ">");
            jpaMenu.insert((AbstractAction) pluginActions.elementAt(i), index);
        }
        jpaTree.enableDefaultActions();
    }

    private void updateJPAToolbar() {
        for (int i = 0; i < pluginJPAComponents.size(); i++) {
            jpaToolBar.remove((Component) pluginJPAComponents.elementAt(i));
        }
        pluginJPAComponents.clear();
        Vector plugins = pluginManager.getPluginClasses();
        for (int i = 0; i < plugins.size(); i++) {
            UnicorePlugable plugable = (UnicorePlugable) plugins.elementAt(i);
            if (plugable instanceof ExtensionPlugable) {
                Component component = ((ExtensionPlugable) plugable).getJPAToolBarComponent();
                if (component != null) {
                    jpaToolBar.add(component);
                    pluginJPAComponents.add(component);
                }
            }
        }
    }

    private void updateJMCActions() {
        JPopupMenu popup = jmcTree.getPopupMenu();
        for (int i = 0; i < pluginJMCComponents.size(); i++) {
            jmcToolBar.remove((Component) pluginJMCComponents.elementAt(i));
        }
        for (int i = 0; i < pluginJMCPopupComponents.size(); i++) {
            popup.remove((Component) pluginJMCPopupComponents.elementAt(i));
        }
        pluginJMCComponents.clear();
        pluginJMCPopupComponents.clear();
        Vector plugins = pluginManager.getPluginClasses();
        boolean first = true;
        for (int i = 0; i < plugins.size(); i++) {
            UnicorePlugable plugable = (UnicorePlugable) plugins.elementAt(i);
            if (plugable instanceof ExtensionPlugable) {
                Component component = ((ExtensionPlugable) plugable).getJMCToolBarComponent();
                if (component != null) {
                    jmcToolBar.add(component);
                    pluginJMCComponents.add(component);
                }
                JMenuItem mi = ((ExtensionPlugable) plugable).getJMCPopupMenuItem();
                if (mi != null) {
                    if (first) {
                        popup.add(new JSeparator());
                        first = false;
                    }
                    popup.add(mi);
                    pluginJMCPopupComponents.add(mi);
                }
            }
        }
    }

    /**
	 * Set Client GUI to a new Look and Feel
	 */
    public final void updateLookAndFeel() {
        String plaf = userDefaults.getLookAndFeel();
        if (plaf == null) {
            return;
        }
        if (plaf.equals(LookAndFeelManager.getCrossPlatformPlaf())) {
            DefaultMetalTheme theme = userDefaults.getTheme();
            if (theme != null) {
                MetalLookAndFeel.setCurrentTheme(theme);
            }
        }
        if (!LookAndFeelManager.isAvailableLookAndFeel(plaf)) {
            plaf = LookAndFeelManager.getCrossPlatformPlaf();
        }
        try {
            UIManager.setLookAndFeel(plaf);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Could not set look and feel to " + plaf, e);
        }
    }

    /**
	 * add plugin action items and remove old entries
	 */
    private void updatePluginMenus() {
        updateJPAAddMenu();
        updateJPAToolbar();
        updateJMCActions();
        for (int i = 0; i < pluginHelpSets.size(); i++) {
            hs.remove((HelpSet) pluginHelpSets.elementAt(i));
        }
        pluginHelpSets.clear();
        for (int i = 0; i < pluginSettingsItems.size(); i++) {
            settingsMenu.remove((JMenuItem) pluginSettingsItems.elementAt(i));
        }
        pluginSettingsItems.clear();
        for (int i = 0; i < pluginExtensionItems.size(); i++) {
            extensionMenu.remove((JMenuItem) pluginExtensionItems.elementAt(i));
        }
        pluginExtensionItems.clear();
        Vector plugins = pluginManager.getPluginClasses();
        Vector invalidPlugins = new Vector();
        for (int i = 0; i < plugins.size(); i++) {
            UnicorePlugable plugable = (UnicorePlugable) plugins.elementAt(i);
            try {
                HelpSet helpSet = plugable.getHelpSet();
                if (helpSet != null) {
                    hs.add(helpSet);
                    pluginHelpSets.add(helpSet);
                }
            } catch (Throwable e) {
                invalidPlugins.add(plugable);
                UserMessages.error("Disactivating invalid plugin\n" + plugable.getPluginInfo() + "\nProbably wrong version.", e.getMessage());
                logger.log(Level.SEVERE, "", e);
            }
        }
        for (int i = 0; i < invalidPlugins.size(); i++) {
            UnicorePlugable plugable = (UnicorePlugable) invalidPlugins.elementAt(i);
            plugable.stopPlugin();
            plugins.remove(plugable);
        }
        extensionMenu.removeAll();
        for (int i = 0; i < plugins.size(); i++) {
            UnicorePlugable plugable = (UnicorePlugable) plugins.elementAt(i);
            JMenuItem item = plugable.getSettingsItem();
            if (item != null) {
                settingsMenu.add(item);
                pluginSettingsItems.add(item);
            }
            if (plugable instanceof ExtensionPlugable) {
                JMenuItem customMenu = ((ExtensionPlugable) plugable).getCustomMenu();
                if (customMenu != null) {
                    extensionMenu.add(customMenu);
                    pluginExtensionItems.add(customMenu);
                }
            }
        }
    }

    /**
	 * Add or replace filename ending
	 * 
	 * @param oldName
	 *            original filename
	 * @param newEnding
	 *            New file ending excluding "."
	 * @return modified filename
	 */
    private String addOrReplaceEnding(String oldName, String newEnding) {
        String name = oldName;
        if (name.endsWith(newEnding)) {
            return name;
        }
        name = removeSuffixAJOorXML(oldName) + "." + newEnding;
        return name;
    }

    private void updateTitle() {
        String title = "UNICORE Client";
        if (ResourceManager.isOffline()) {
            title += " <Offline Mode>";
        }
        setTitle(title);
    }

    /**
	 * Write a job to file.
	 * 
	 * @param jc
	 *            JobContainer that will be written to file
	 * @return true if writing succeeded
	 */
    private boolean writeJobToFile(JobContainer jc) {
        if (jc == null || jc.getFilename() == null) {
            return false;
        }
        boolean succeeded = false;
        String jobName = jc.getFilename();
        String name = addOrReplaceEnding(jobName, "ajo");
        jc.setFilename(name);
        succeeded = ResourceManager.writeObjectToFile(jc, name);
        if (succeeded) {
            jc.setSavedTime(new Date());
            if (jobName.equals(name)) {
                recentFileMenu.addRecentFile(name);
            }
            setStatusMessage(jc + " saved as: " + jc.getFilename());
        }
        if (!jobName.equals(name)) {
            jc.setFilename(jobName);
        }
        return succeeded;
    }

    /**
	 * Write a job to file in XML format.
	 * 
	 * @param jc
	 *            JobContainer that will be written to file
	 * @return true if writing succeeded
	 */
    private boolean writeJobToFileXML(JobContainer jc) {
        if (jc == null || jc.getFilename() == null) {
            return false;
        }
        boolean succeeded = false;
        String jobName = jc.getFilename();
        String name = addOrReplaceEnding(jobName, "xml");
        jc.setFilename(name);
        try {
            long timing = System.currentTimeMillis();
            XMLObjectWriter dout = new XMLObjectWriter(new File(name));
            dout.writeObjectXML(jc);
            timing = System.currentTimeMillis() - timing;
            if (TIMING > 0) {
                logger.info("TIMING[1]: Writing job in XML format to file " + name + " in " + timing + " milliseconds");
            }
            jc.setSavedTime(new Date());
            if (jobName.equals(name)) {
                recentFileMenu.addRecentFile(name);
            }
            setStatusMessage(jc + " saved as: " + jc.getFilename());
            succeeded = true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "", e);
            succeeded = false;
        }
        if (!jobName.equals(name)) {
            jc.setFilename(jobName);
        }
        return succeeded;
    }

    /**
	 * Build jpa and jmc tree and split panes
	 */
    private void buildControls() {
        xmlFilter.setDescription(res.getString("UNICORE_JOBS_XML"));
        ajoFilter.setDescription(res.getString("UNICORE_JOBS_BINARY"));
        JScrollPane jpaScrollPane = new JScrollPane(jpaTree);
        JPanel jpaPanel = new JPanel(new BorderLayout());
        jpaPanel.add(jpaToolBarPanel, BorderLayout.NORTH);
        jpaPanel.add(jpaScrollPane, BorderLayout.CENTER);
        JScrollPane jmcScrollPane = new JScrollPane(jmcTree);
        JPanel jmcPanel = new JPanel(new BorderLayout());
        jmcPanel.add(jmcToolBarPanel, BorderLayout.NORTH);
        jmcPanel.add(jmcScrollPane, BorderLayout.CENTER);
        treeSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, jpaPanel, jmcPanel);
        treeSplitPane.setBorder(BorderFactory.createEtchedBorder());
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeSplitPane, logoPanel);
        getContentPane().add(mainSplitPane, BorderLayout.CENTER);
        getContentPane().add(statusPanel, BorderLayout.SOUTH);
    }

    private void buildInitialKeystore(boolean empty) throws Exception {
        logger.info("Building initial keystore...");
        userDefaults.setKeyStoreName(userDefaults.getUnicoreDir() + File.separator + "keystore");
        String keystoreName = userDefaults.getKeyStoreName();
        if (empty) {
            ResourceManager.buildEmptyKeystore(keystoreName);
        } else {
            ResourceManager.buildTestCertKeystore(keystoreName);
        }
    }

    /**
	 * Build the main menu bar
	 */
    private void buildMenuBar() {
        JMenu fileMenu = new JMenu(res.getString("FILE"));
        fileMenu.setMnemonic('F');
        JMenuItem newItem = new JMenuItem(jpaTree.getNewJobAction());
        newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        fileMenu.add(newItem);
        JMenuItem openItem = new JMenuItem(jpaTree.getOpenAction());
        openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
        fileMenu.add(openItem);
        recentFileMenu = new RecentFileMenu("Recent Files", userDefaults.getUnicoreDir() + File.separator + res.getString("RECENT_FILES_FILE"), new RecentFileListener());
        recentFileMenu.setMnemonic('R');
        fileMenu.add(recentFileMenu);
        JMenuItem saveItem = new JMenuItem(jpaTree.getSaveAction());
        saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileMenu.add(saveItem);
        fileMenu.add(jpaTree.getSaveAsAction());
        fileMenu.addSeparator();
        JMenuItem submitItem = new JMenuItem(jpaTree.getSubmitAction());
        fileMenu.add(submitItem);
        fileMenu.addSeparator();
        offlineItem = new JCheckBoxMenuItem(offlineAction);
        fileMenu.add(offlineItem);
        fileMenu.addSeparator();
        fileMenu.add(exitAction);
        jpaMenu = new JMenu(res.getString("JOB_PREPARATION"));
        jpaMenu.setMnemonic('P');
        updateJPAAddMenu();
        JMenu jmcMenu = new JMenu(res.getString("JOB_MONITORING"));
        jmcMenu.setMnemonic('M');
        jmcMenu.add(jmcTree.getRefreshAction());
        jmcMenu.add(jmcTree.getStopRefreshAction());
        jmcMenu.add(jmcTree.getFetchOutputAction());
        jmcMenu.addSeparator();
        jmcMenu.add(jmcTree.getShowDependenciesAction());
        jmcMenu.add(jmcTree.getShowResourcesAction());
        jmcMenu.addSeparator();
        jmcMenu.add(jmcTree.getAbortAction());
        jmcMenu.add(jmcTree.getRemoveAction());
        jmcMenu.add(jmcTree.getHoldAction());
        jmcMenu.add(jmcTree.getResumeAction());
        settingsMenu = new JMenu(res.getString("SETTINGS"));
        settingsMenu.setMnemonic('S');
        configureSitesItem = new JMenuItem("Configure sites...");
        initMenuItem(configureSitesItem);
        configureSitesItem.setEnabled(true);
        settingsMenu.add(configureSitesItem);
        certificateItem = new JMenuItem(res.getString("CERTIFICATES"), 'K');
        initMenuItem(certificateItem);
        certificateItem.setEnabled(true);
        settingsMenu.add(certificateItem);
        userDefaultsItem = new JMenuItem(res.getString("USER_DEFAULTS"), 'U');
        initMenuItem(userDefaultsItem);
        userDefaultsItem.setEnabled(true);
        settingsMenu.add(userDefaultsItem);
        setIdentityItem = new JMenuItem(res.getString("SET_IDENTITY"), 'I');
        initMenuItem(setIdentityItem);
        setIdentityItem.setEnabled(true);
        settingsMenu.add(setIdentityItem);
        loadPluginsItem = new JMenuItem(res.getString("RELOAD_PLUGINS"), 'R');
        initMenuItem(loadPluginsItem);
        loadPluginsItem.setEnabled(true);
        settingsMenu.add(loadPluginsItem);
        clearCacheItem = new JMenuItem(res.getString("CLEAR_CACHE"), 'C');
        initMenuItem(clearCacheItem);
        clearCacheItem.setEnabled(true);
        settingsMenu.add(clearCacheItem);
        settingsMenu.addSeparator();
        extensionMenu = new JMenu(res.getString("EXTENSIONS"));
        extensionMenu.setMnemonic('X');
        JMenu helpMenu = new JMenu(res.getString("HELP"));
        helpMenu.setMnemonic('H');
        if (isJavaHelpAvailable()) {
            referenceItem = new JMenuItem(res.getString("USER_GUIDE"), 'U');
            initMenuItem(referenceItem);
            if (!noHelpSet) {
                referenceItem.addActionListener(new CSH.DisplayHelpFromSource(hb));
            }
            referenceItem.setEnabled(true);
            helpMenu.add(referenceItem);
            tutorItem = new JMenuItem(res.getString("TUTOR"), 'T');
            initMenuItem(tutorItem);
        }
        aboutPluginItem = new JMenuItem(res.getString("PLUGIN_INFO"), 'P');
        initMenuItem(aboutPluginItem);
        aboutPluginItem.setEnabled(true);
        helpMenu.add(aboutPluginItem);
        aboutItem = new JMenuItem(res.getString("ABOUT"), 'A');
        initMenuItem(aboutItem);
        aboutItem.setEnabled(true);
        helpMenu.add(aboutItem);
        JMenuItem gcItem = new JMenuItem("Free unused memory");
        gcItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.gc();
            }
        });
        helpMenu.add(gcItem);
        if (menuBar != null) {
            remove(menuBar);
        }
        menuBar = new JMenuBar();
        menuBar.add(fileMenu);
        menuBar.add(jpaMenu);
        menuBar.add(jmcMenu);
        menuBar.add(settingsMenu);
        menuBar.add(extensionMenu);
        menuBar.add(helpMenu);
        setJMenuBar(menuBar);
    }

    /**
	 * Build jpa and jmc toolbars
	 */
    private void buildToolBars() {
        JButton submitButton = new JButton(jpaTree.getSubmitAction());
        submitButton.setToolTipText(res.getString("SUBMIT_JOB_TIP"));
        submitButton.setText("");
        JButton checkButton = new JButton(jpaTree.getCheckAction());
        checkButton.setToolTipText(res.getString("CHECK_JOB_TIP"));
        checkButton.setText("");
        JButton removeButton = new JButton(jpaTree.getRemoveAction());
        removeButton.setToolTipText(res.getString("REMOVE_NODE_TIP"));
        removeButton.setText("");
        JButton newButton = new JButton(jpaTree.getNewJobAction());
        newButton.setToolTipText("Create a new job");
        newButton.setText("");
        JButton sortButton = new JButton(jpaTree.getSortAction());
        sortButton.setToolTipText(res.getString("SORT_JOB_TIP"));
        sortButton.setText("");
        jpaToolBar = new SimpleToolBar(submitButton, checkButton, newButton);
        jpaToolBar.add(sortButton);
        SimpleToolBar jpaToolBarRemove = new SimpleToolBar(removeButton);
        jpaToolBarPanel = new JPanel(new BorderLayout());
        jpaToolBarPanel.add(jpaToolBar, BorderLayout.WEST);
        jpaToolBarPanel.add(jpaToolBarRemove, BorderLayout.EAST);
        JButton fetchOutputButton = new JButton(jmcTree.getFetchOutputAction());
        fetchOutputButton.setToolTipText(res.getString("FETCH_OUTPUT_TIP"));
        fetchOutputButton.setText("");
        JButton refreshButton = new JButton(jmcTree.getRefreshAction());
        refreshButton.setToolTipText(res.getString("REFRESH_TIP"));
        refreshButton.setText("");
        JButton stopRefreshButton = new JButton(jmcTree.getStopRefreshAction());
        stopRefreshButton.setToolTipText("Interrupt current refresh action");
        stopRefreshButton.setText("");
        JButton jmcRemoveButton = new JButton(jmcTree.getRemoveAction());
        jmcRemoveButton.setToolTipText(res.getString("REMOVE_TIP"));
        jmcRemoveButton.setText("");
        jmcToolBar = new SimpleToolBar(refreshButton, stopRefreshButton, fetchOutputButton);
        jmcToolBar.addSeparator();
        SimpleToolBar jmcToolBarRemove = new SimpleToolBar(jmcRemoveButton);
        jmcToolBarPanel = new JPanel(new BorderLayout());
        jmcToolBarPanel.add(jmcToolBar, BorderLayout.WEST);
        jmcToolBarPanel.add(jmcToolBarRemove, BorderLayout.EAST);
    }

    private boolean checkKeystoreExists() {
        if (ResourceManager.getKeystoreManager() != null && ResourceManager.getKeystoreManager().hasOpenKeystore() && ResourceManager.isKeystoreAvailable()) {
            return true;
        }
        setStatusMessage(clientVersion);
        statusPanel.setProgressMaxValue(6);
        statusPanel.setProgressString(res.getString("SEARCHING_KEYSTORE"));
        keystoreName = userDefaults.getKeyStoreName();
        if (!ResourceManager.isKeystoreAvailable()) {
            return this.processBuildKeystore(true, false);
        }
        if (JOptionPane.showConfirmDialog(this, "You did not unlock your keystore, yet.\n" + "There exists a keystore at \n" + keystoreName + "\nDo you want to use this keystore?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
            return this.processOpenKeystore();
        }
        if (JOptionPane.showConfirmDialog(this, "Do you want to build a new empty keystore?\n" + "CAUTION: Your old keystore\n" + keystoreName + " will be overwritten.", "", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
            return this.processBuildKeystore(true, false);
        }
        setStatusMessage("No Keystore available.");
        return false;
    }

    /**
	 * Check, whether the Job is already open which the user want to load.
	 * Ignores whether the job was loaded in XML or binary format.
	 * 
	 * @param filename
	 *            Job to be loaded
	 */
    private void checkOpenJob(String filename) {
        setCursor(new Cursor(Cursor.WAIT_CURSOR));
        Object jpaTreeRoot = jpaTree.getModel().getRoot();
        ContainerNode node = null;
        boolean open = false;
        int index = 0;
        String filenameSuffixFree = removeSuffixAJOorXML(filename);
        String nodeFilename = null;
        for (int i = 0; i < jpaTree.getModel().getChildCount(jpaTreeRoot); i++) {
            node = (ContainerNode) (jpaTree.getModel().getChild(jpaTreeRoot, i));
            nodeFilename = ((JobContainer) node.getActionContainer()).getFilename();
            if (nodeFilename == null) {
                continue;
            }
            if (filenameSuffixFree.equals(removeSuffixAJOorXML(nodeFilename))) {
                index = i;
                open = true;
                break;
            }
        }
        if (open) {
            int reloadOption = JOptionPane.YES_OPTION;
            int copyOption = JOptionPane.NO_OPTION;
            int cancelOption = JOptionPane.CANCEL_OPTION;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            Object[] options = { "Reload", "Copy", "Cancel" };
            String message = nodeFilename + " is already open. \n" + "You can only reload or copy this job.";
            if (!filename.equals(nodeFilename)) {
                message += "\n\nATTENTION: reloading means overwriting with " + filename;
            }
            String title = "Try to open Job " + filename;
            int status = JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
            if (status == reloadOption) {
                jpaTree.remove(node);
                this.loadJob(filename, index);
                return;
            }
            if (status == copyOption) {
                jpaTree.copyNode(node, true);
                jpaTree.paste((PanelNode) jpaTreeRoot);
                return;
            }
        } else {
            this.loadJob(filename, index);
        }
    }

    /**
	 * Generate message with info about client version
	 * 
	 * @return message string
	 */
    private static String genMessage() {
        String msg = "UNICORE Client\n\n" + res.getString("CURRENT_VERSION") + com.pallas.unicore.utility.Version.getCurrentVersion(false) + "\n" + res.getString("CURRENT_BUILD") + com.pallas.unicore.utility.Version.getBuildNumber() + "\n" + res.getString("BUILD_DATE") + com.pallas.unicore.utility.Version.getBuildDate() + "\n" + res.getString("AJO_VERSION") + Package.getPackage("org.unicore").getSpecificationVersion() + "\n" + res.getString("AJO_BUILD") + Package.getPackage("org.unicore").getImplementationVersion() + "\n" + res.getString("IMPLEMENTATION");
        msg += "\nJava Version: " + System.getProperty("java.version");
        msg += "\nJava VM: " + System.getProperty("java.vm.name");
        msg += "\nJava VM vendor: " + System.getProperty("java.vm.vendor");
        return msg;
    }

    class ExitAction extends AbstractAction {

        public ExitAction() {
            super("Exit");
        }

        public void actionPerformed(ActionEvent e) {
            exitInstance();
        }
    }

    private class KeystoreChangeListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals(KeyStoreEditor.IDENTITY_CHANGED_EVENT)) {
                logger.info("Updating default identity.");
                try {
                    ResourceManager.updateDefaultIdentity();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not set default identity.", e);
                }
                statusPanel.setIdentity(ResourceManager.getUserDefaults().getDefaultIdentity());
                Enumeration jobNodes = jpaTree.getJobs();
                while (jobNodes.hasMoreElements()) {
                    PanelNode jobNode = (PanelNode) jobNodes.nextElement();
                    JobContainer jc = (JobContainer) jobNode.getUserObject();
                    recursiveSetUser(jc);
                }
            } else if (evt.getPropertyName().equals(KeyStoreEditor.CACHE_OUTDATED_EVENT)) {
                setStatusMessage("Updating resource cache...");
                loadResources();
                setStatusMessage("Ok.");
            } else if (evt.getPropertyName().equals(KeyStoreEditor.PLUGINS_OUTDATED_EVENT)) {
                setStatusMessage("Reloading plugins...");
                loadPlugins();
            }
        }
    }

    private class MenuListener implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            Object source = event.getSource();
            if (source == loadPluginsItem) {
                loadPlugins();
            } else if (source == clearCacheItem) {
                ResourceManager.clearResourceCache();
                Runnable runner = new Runnable() {

                    public void run() {
                        statusPanel.setProgressString("Updating resource cache.");
                        loadResources();
                        setStatusMessage(clientVersion);
                    }
                };
                SwingUtilities.invokeLater(runner);
            } else if (source == certificateItem) {
                editKeystore();
            } else if (source == userDefaultsItem) {
                showUserDefaultsDialog();
            } else if (source == setIdentityItem) {
                try {
                    showSetIdentityDialog();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "SetIdentityDialog cannot be loaded", e);
                }
            } else if (source == configureSitesItem) {
                try {
                    showConfigureSitesDialog();
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "confiureSitesDialog cannot be loaded", e);
                }
            } else if (source == aboutPluginItem) {
                showPluginInfo();
            } else if (source == aboutItem) {
                showAboutInfo();
            }
        }
    }

    class OfflineAction extends AbstractAction {

        public OfflineAction() {
            super("Offline Mode");
        }

        public void actionPerformed(ActionEvent e) {
            setOfflineMode(offlineItem.isSelected());
        }
    }

    private class ProxyEnabledListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getPropertyName().equals("PROXY_ENABLED")) {
                setStatusMessage("Updating resource cache...");
                loadResources();
            }
        }
    }

    private class RecentFileListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem) (e.getSource());
            String filename = mi.getActionCommand();
            checkOpenJob(filename);
            recentFileMenu.addRecentFile(filename);
        }
    }
}
