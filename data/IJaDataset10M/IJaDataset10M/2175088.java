package org.hyperimage.client.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.activation.MimetypesFileTypeMap;
import javax.imageio.ImageIO;
import javax.media.jai.PlanarImage;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import org.hyperimage.client.HIRuntime;
import org.hyperimage.client.HIWebServiceManager;
import org.hyperimage.client.Messages;
import org.hyperimage.client.components.AccountSettings;
import org.hyperimage.client.components.GenericMetadataEditor;
import org.hyperimage.client.components.GroupBrowser;
import org.hyperimage.client.components.HIComponent;
import org.hyperimage.client.components.HIComponentFrame;
import org.hyperimage.client.components.LayerEditor;
import org.hyperimage.client.components.LightTableEditor;
import org.hyperimage.client.components.ObjectEditor;
import org.hyperimage.client.components.PreferenceManager;
import org.hyperimage.client.components.ProjectSettings;
import org.hyperimage.client.components.ProjectUsersManager;
import org.hyperimage.client.components.RepositoryImport;
import org.hyperimage.client.components.SearchModule;
import org.hyperimage.client.components.TemplateEditor;
import org.hyperimage.client.components.HIComponent.HIMessageTypes;
import org.hyperimage.client.exception.HIWebServiceException;
import org.hyperimage.client.xmlimportexport.PeTALExporter;
import org.hyperimage.client.gui.dialogs.AboutDialog;
import org.hyperimage.client.gui.dialogs.ExportProjectDialog;
import org.hyperimage.client.gui.dialogs.LoginDialog;
import org.hyperimage.client.gui.dialogs.ProjectChooser;
import org.hyperimage.client.gui.dialogs.XMLImportProjectDialog;
import org.hyperimage.client.util.MetadataHelper;
import org.hyperimage.client.ws.HIMaintenanceModeException_Exception;
import org.hyperimage.client.ws.HiBase;
import org.hyperimage.client.ws.HiBaseTypes;
import org.hyperimage.client.ws.HiFlexMetadataSet;
import org.hyperimage.client.ws.HiFlexMetadataTemplate;
import org.hyperimage.client.ws.HiGroup;
import org.hyperimage.client.ws.HiImageSizes;
import org.hyperimage.client.ws.HiLightTable;
import org.hyperimage.client.ws.HiObject;
import org.hyperimage.client.ws.HiProject;
import org.hyperimage.client.ws.HiQuickInfo;
import org.hyperimage.client.ws.HiRoles;
import org.hyperimage.client.ws.HiText;
import org.hyperimage.client.ws.HiView;
import org.hyperimage.client.ws.Hiurl;
import org.hyperimage.client.xmlimportexport.LegacyHIPeTALExporter;
import org.hyperimage.client.xmlimportexport.PeTALImporter;
import org.w3c.dom.Document;

/**
 * 
 * Class: HIClientGUI
 * Package: org.hyperimage.client.gui
 * @author Jens-Martin Loebel
 *
 * Provides the HyperImage Client GUI
 */
public class HIClientGUI extends JFrame implements WindowListener, ActionListener, InternalFrameListener, HierarchyListener, ComponentListener {

    private static final long serialVersionUID = -4501582186237562342L;

    private static final String APPNAME = "HyperImage Editor";

    private HIWebServiceManager manager;

    HiProject project;

    private JMenu debugMenu;

    private HIComponentFrame frontFrame = null;

    private ProjectChooser projectChooser;

    private LoginDialog loginDialog;

    private ExportProjectDialog exportDialog;

    private XMLImportProjectDialog xmlImportDialog;

    private HIWebServiceException lastWSError = null;

    Vector<HIComponentFrame> components;

    private boolean serviceActivity = false;

    private boolean displayingError = false;

    Cursor waitCursor;

    public JDesktopPane mdiPane = new JDesktopPane();

    public ProgressInfoGlassPane infoGlassPane = new ProgressInfoGlassPane();

    private JMenuItem aboutMenuItem;

    private JMenuItem administrateProjectPrefsMenuItem;

    private JMenuItem administrateProjectUsersMenuItem;

    private JMenuItem changeProjectMenuItem;

    private JMenuItem changeUserMenuItem;

    private JMenuItem contentMenuItem;

    private JMenuItem editUserMenuItem;

    private JMenuItem exitMenuItem;

    private JMenu fileMenu;

    private JMenuItem exportMenuItem;

    private JMenuItem xmlImportMenuItem;

    private JMenu guiLanguageMenu;

    private JMenuBar guiMenuBar;

    private JMenu helpMenu;

    private JMenuItem feedbackItem;

    private JMenuItem newGroupBrowserMenuItem;

    private JMenuItem searchMenuItem;

    private JMenuItem importMenuItem;

    private JMenuItem nextWindowItem;

    private JMenuItem prevWindowItem;

    private JMenu projectMenu;

    private JMenuItem projectSettingsMenuItem;

    private JMenuItem projectTemplatesMenuItem;

    private JMenuItem toggleMetadataViewMenuItem;

    private JMenu toolsMenu;

    private JMenuItem visitWebsiteMenuItem;

    private JMenu windowMenu;

    private JSeparator windowSeparator1;

    private JSeparator windowSeparator2;

    private JMenu popupToolsMenu;

    private JMenuItem popupNewGroupBrowserMenuItem;

    private JMenuItem popupSearchMenuItem;

    private JMenuItem popupImportMenuItem;

    public HIClientGUI() {
        this.manager = HIRuntime.getManager();
        HIRuntime.setGui(this);
        components = new Vector<HIComponentFrame>();
        mdiPane.setBackground(new Color(0x41, 0x69, 0xAA));
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(this);
    }

    public LoginDialog getLoginDialog() {
        return loginDialog;
    }

    public boolean handleLogin() {
        boolean loginSelected;
        HIRuntime.getManager().logout();
        this.setTitle(APPNAME + " " + HIRuntime.getClientVersion());
        loginDialog.setInfoLabel(Messages.getString("HIClientGUI.0"), Color.blue);
        loginSelected = loginDialog.promptLogin(loginDialog.getUserName());
        if (!loginSelected) return false;
        try {
            while (!HIRuntime.getManager().login(loginDialog.getUserName(), loginDialog.getPassword())) {
                loginDialog.setInfoLabel(Messages.getString("HIClientGUI.1"), Color.red);
                loginSelected = loginDialog.promptLogin(loginDialog.getUserName());
                if (!loginSelected) return false;
            }
        } catch (HIWebServiceException e) {
            System.out.println("SERVER SIDE SERVICE EXCEPTION:");
            e.printStackTrace();
            if (e.getCause() instanceof HIMaintenanceModeException_Exception) {
                displayMaintenanceDialog(e);
                return handleLogin();
            } else HIRuntime.displayFatalErrorAndExit("WebService Initialization Failed!\n\nReason:" + e.getMessage() + "\n\n" + e.getClass());
            return false;
        }
        return true;
    }

    public void tryLogoutAndExit() {
        if (deregisterAllComponents()) {
            System.out.print("Logout: ");
            System.out.println(HIRuntime.getManager().logout());
            this.setVisible(false);
            this.dispose();
            System.exit(0);
        }
    }

    public void chooseProject() {
        this.setTitle(APPNAME + " " + HIRuntime.getClientVersion());
        List<HiProject> projects = null;
        try {
            projects = HIRuntime.getManager().getProjects();
        } catch (HIWebServiceException e) {
            HIRuntime.displayFatalErrorAndExit("Could not get user projects!");
        }
        if (projects.size() == 0) {
            HIRuntime.displayFatalErrorAndExit("User does not belong to any projects!");
        }
        project = projectChooser.selectProject(projects);
        if (project == null) if (HIRuntime.getManager().getProject() == null) {
            tryLogoutAndExit();
            System.exit(1);
        } else {
            setMenuState();
            return;
        }
        try {
            manager.setProject(project);
            updateProjectTitle();
            setMenuState();
        } catch (HIWebServiceException wse) {
            reportError(wse, null);
            return;
        }
    }

    public void updateProjectTitle() {
        this.project = HIRuntime.getManager().getProject();
        String projectTitle = MetadataHelper.findValue(project, project.getDefaultLanguage().getLanguageId());
        if (projectTitle == null || projectTitle.length() == 0) projectTitle = "Project P" + project.getId();
        this.setTitle(APPNAME + " " + HIRuntime.getClientVersion() + " - " + projectTitle);
    }

    public void triggerProjectUpdate() {
        sendMessage(HIMessageTypes.PREFERENCE_MODIFIED, null, null);
        sendMessage(HIMessageTypes.LANGUAGE_ADDED, null, null);
        sendMessage(HIMessageTypes.DEFAULT_LANGUAGE_CHANGED, null, null);
        sendMessage(HIMessageTypes.GROUP_SORTORDER_CHANGED, null, null);
        updateProjectTitle();
    }

    public void handleXMLImport() {
        if (!checkAdminAbility(false)) return;
        boolean projectIsEmpty = false;
        try {
            if (HIRuntime.getManager().getGroups().size() == 0) {
                if (HIRuntime.getManager().getGroupContents(HIRuntime.getManager().getImportGroup()).size() == 0 && HIRuntime.getManager().getGroupContents(HIRuntime.getManager().getTrashGroup()).size() == 0) projectIsEmpty = true;
            }
        } catch (HIWebServiceException wse) {
            HIRuntime.getGui().reportError(wse, null);
            return;
        }
        if (!projectIsEmpty) {
            displayInfoDialog(Messages.getString("HIClientGUI.159"), Messages.getString("HIClientGUI.160") + "\n\n" + Messages.getString("HIClientGUI.161"));
            return;
        }
        if (xmlImportDialog.displayImportDialog()) {
        } else {
            displayInfoDialog(Messages.getString("HIClientGUI.150"), Messages.getString("HIClientGUI.151"));
            return;
        }
        File importFile = xmlImportDialog.getImportXMLFile();
        if (importFile == null || !importFile.isFile() || !importFile.exists() || !importFile.canRead()) {
            displayInfoDialog(Messages.getString("HIClientGUI.152"), Messages.getString("HIClientGUI.153"));
            return;
        }
        PeTALImporter importer = new PeTALImporter();
        if (!importer.loadAndValidatePeTAL(importFile)) {
            displayInfoDialog(Messages.getString("HIClientGUI.154"), Messages.getString("HIClientGUI.155"));
            return;
        }
        if (importer.getPeTALVersion() != null && importer.getPeTALVersion().compareTo("2.0") != 0) {
            displayInfoDialog(Messages.getString("HIClientGUI.156"), Messages.getString("HIClientGUI.157") + "\n\n" + Messages.getString("HIClientGUI.158"));
            return;
        }
        importer.importXMLToProject();
    }

    public void handleExport() {
        if (!checkEditAbility(false)) return;
        if (exportDialog.displayExportDialog()) {
            if (exportDialog.getExportDir() == null) return;
            if (!exportDialog.getExportDir().canWrite()) {
                displayInfoDialog(Messages.getString("HIClientGUI.8"), Messages.getString("HIClientGUI.9"));
                return;
            }
            final File exportDir = new File(exportDialog.getExportDir().getAbsolutePath() + File.separatorChar + "PeTAL");
            if (!exportDir.exists()) exportDir.mkdir();
            startIndicatingServiceActivity(true);
            setMessage(Messages.getString("HIClientGUI.5"));
            new Thread() {

                public void run() {
                    Document xml;
                    if (exportDialog.isLegacyMode()) xml = LegacyHIPeTALExporter.getProjectToPeTALXML(HIRuntime.getManager().getProject()); else xml = PeTALExporter.getProjectToPeTALXML(HIRuntime.getManager().getProject());
                    if (xml != null) {
                        File outputFile = new File(exportDir.getAbsolutePath() + File.separatorChar + "project.xml");
                        try {
                            outputFile.createNewFile();
                        } catch (IOException ioe) {
                            return;
                        }
                        if (outputFile.canWrite() == false) return;
                        if (exportDialog.isLegacyMode()) LegacyHIPeTALExporter.serializeXMLDocumentToFile(xml, outputFile); else PeTALExporter.serializeXMLDocumentToFile(xml, outputFile);
                    }
                    if (exportDialog.isExportingBinaries()) {
                        FileOutputStream binWriter;
                        File binaryDir = new File(exportDir.getAbsolutePath() + File.separatorChar + "img");
                        if (!binaryDir.exists()) binaryDir.mkdir();
                        int progress = -1;
                        float percentage = 0;
                        Vector<HiView> projectViews;
                        if (exportDialog.isLegacyMode()) projectViews = LegacyHIPeTALExporter.projectViews; else projectViews = PeTALExporter.projectViews;
                        if (projectViews.size() > 0) percentage = 100f / (float) projectViews.size();
                        for (int index = 0; index < projectViews.size(); index++) {
                            if (progress != (int) (percentage * (float) index)) {
                                progress = (int) (percentage * (float) index);
                                setProgress(progress);
                            }
                            HiView view = projectViews.get(index);
                            setMessage(Messages.getString("HIClientGUI.6") + " " + (index + 1) + " " + Messages.getString("HIClientGUI.11") + " " + projectViews.size());
                            if (view.getFilename() != null && view.getFilename().length() > 0) view.setMimeType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType((view.getFilename())));
                            if (view.getMimeType() == null) view.setMimeType("");
                            try {
                                byte[] bitstream;
                                if (exportDialog.isLegacyMode()) {
                                    bitstream = HIRuntime.getManager().getImageAsBitstream(view, HiImageSizes.HI_FULL);
                                    if (bitstream != null) {
                                        binWriter = new FileOutputStream(binaryDir.getAbsolutePath() + File.separatorChar + "V" + view.getId() + ".jpg");
                                        binWriter.write(bitstream);
                                        binWriter.close();
                                    } else if (!view.getMimeType().startsWith("image/")) HIRuntime.getGui().displayInfoDialog(Messages.getString("HIClientGUI.12"), Messages.getString("HIClientGUI.13") + " V" + view.getId() + " " + Messages.getString("HIClientGUI.16") + "\n\n" + Messages.getString("HIClientGUI.18")); else System.out.println("View not exported because the image is broken or mime detection failed" + " (" + view.getMimeType() + ") - " + view.getFilename() + "!");
                                    bitstream = HIRuntime.getManager().getImageAsBitstream(view, HiImageSizes.HI_PREVIEW);
                                    if (bitstream != null) {
                                        binWriter = new FileOutputStream(binaryDir.getAbsolutePath() + File.separatorChar + "V" + view.getId() + "_prev.jpg");
                                        binWriter.write(bitstream);
                                        binWriter.close();
                                    }
                                    bitstream = HIRuntime.getManager().getImageAsBitstream(view, HiImageSizes.HI_THUMBNAIL);
                                    if (bitstream != null) {
                                        binWriter = new FileOutputStream(binaryDir.getAbsolutePath() + File.separatorChar + "V" + view.getId() + "_thumb.jpg");
                                        binWriter.write(bitstream);
                                        binWriter.close();
                                    }
                                } else {
                                    File destFile = new File(binaryDir.getAbsolutePath() + File.separatorChar + "V" + view.getId() + ".original");
                                    boolean exportBinary = true;
                                    if (destFile.exists() && destFile.canRead()) {
                                        bitstream = HIRuntime.getBytesFromFile(destFile);
                                        String hash = HIRuntime.getMD5HashString(bitstream);
                                        if (hash.compareTo(view.getHash()) == 0) exportBinary = false;
                                        bitstream = null;
                                    }
                                    if (exportBinary) {
                                        bitstream = HIRuntime.getManager().getImageAsBitstream(view, HiImageSizes.HI_ORIGINAL);
                                        if (bitstream != null) {
                                            binWriter = new FileOutputStream(binaryDir.getAbsolutePath() + File.separatorChar + "V" + view.getId() + ".original");
                                            binWriter.write(bitstream);
                                            binWriter.close();
                                        } else {
                                            HIRuntime.getGui().displayInfoDialog(Messages.getString("HIClientGUI.12"), Messages.getString("HIClientGUI.13") + " V" + view.getId() + " " + Messages.getString("HIClientGUI.16") + "\n\n" + Messages.getString("HIClientGUI.18"));
                                            System.out.println("View not exported because the image is broken or mime detection failed" + " (" + view.getMimeType() + ") - " + view.getFilename() + "!");
                                        }
                                    }
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                                HIRuntime.getGui().displayInfoDialog(Messages.getString("HIClientGUI.23"), Messages.getString("HIClientGUI.24"));
                                break;
                            } catch (IOException e) {
                                e.printStackTrace();
                                HIRuntime.getGui().displayInfoDialog(Messages.getString("HIClientGUI.25"), Messages.getString("HIClientGUI.26"));
                                break;
                            } catch (HIWebServiceException wse) {
                                HIRuntime.getGui().reportError(wse, null);
                                break;
                            }
                        }
                    }
                    stopIndicatingServiceActivity();
                    String ppTool = exportDialog.getSelectedPPTool();
                    if (ppTool != null) {
                        try {
                            Runtime.getRuntime().exec("javaws " + ppTool);
                        } catch (IOException e) {
                            displayInfoDialog(Messages.getString("HIClientGUI.17"), Messages.getString("HIClientGUI.19"));
                        }
                    }
                }
            }.start();
        } else displayInfoDialog(Messages.getString("HIClientGUI.27"), Messages.getString("HIClientGUI.28"));
    }

    public void createAndShowGUI() {
        for (Locale guiLang : HIRuntime.supportedLanguages) if (Locale.getDefault().getLanguage().compareTo(guiLang.getLanguage()) == 0) HIRuntime.setGUILanguage(guiLang);
        projectChooser = new ProjectChooser(this);
        loginDialog = new LoginDialog(this);
        exportDialog = new ExportProjectDialog(this);
        xmlImportDialog = new XMLImportProjectDialog(this);
        initMenus();
        this.setTitle(APPNAME + " " + HIRuntime.getClientVersion());
        this.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        this.setMinimumSize(new Dimension(200, 460));
        this.setContentPane(mdiPane);
        this.setGlassPane(infoGlassPane);
        infoGlassPane.setVisible(false);
        this.setVisible(true);
        Toolkit tk = java.awt.Toolkit.getDefaultToolkit();
        waitCursor = null;
        try {
            waitCursor = tk.createCustomCursor(ImageIO.read(getClass().getResource("/resources/cursors/wait-cursor.png")), new Point(16, 16), "cross-Add");
        } catch (Exception e) {
            waitCursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        }
        infoGlassPane.setCursor(waitCursor);
        mdiPane.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger() && !e.isConsumed()) {
                    e.consume();
                    popupToolsMenu.getPopupMenu().show(mdiPane, e.getX() + 10, e.getY());
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger() && !e.isConsumed()) {
                    e.consume();
                    popupToolsMenu.getPopupMenu().show(mdiPane, e.getX() + 10, e.getY());
                }
            }
        });
        if (!handleLogin()) tryLogoutAndExit();
        chooseProject();
        showDefaultProjectLayout();
    }

    private void initMenus() {
        guiMenuBar = new JMenuBar();
        fileMenu = new JMenu();
        exportMenuItem = new JMenuItem();
        xmlImportMenuItem = new JMenuItem();
        guiLanguageMenu = new JMenu();
        editUserMenuItem = new JMenuItem();
        changeProjectMenuItem = new JMenuItem();
        changeUserMenuItem = new JMenuItem();
        exitMenuItem = new JMenuItem();
        projectMenu = new JMenu();
        administrateProjectPrefsMenuItem = new JMenuItem();
        projectSettingsMenuItem = new JMenuItem();
        projectTemplatesMenuItem = new JMenuItem();
        administrateProjectUsersMenuItem = new JMenuItem();
        toolsMenu = new JMenu();
        newGroupBrowserMenuItem = new JMenuItem();
        searchMenuItem = new JMenuItem();
        importMenuItem = new JMenuItem();
        windowMenu = new JMenu();
        nextWindowItem = new JMenuItem();
        prevWindowItem = new JMenuItem();
        windowSeparator1 = new JSeparator();
        toggleMetadataViewMenuItem = new JMenuItem();
        windowSeparator2 = new JSeparator();
        helpMenu = new JMenu();
        aboutMenuItem = new JMenuItem();
        contentMenuItem = new JMenuItem();
        visitWebsiteMenuItem = new JMenuItem();
        editUserMenuItem.setActionCommand("accountSettings");
        fileMenu.add(editUserMenuItem);
        fileMenu.add(new JSeparator());
        fileMenu.add(guiLanguageMenu);
        fileMenu.add(new JSeparator());
        changeUserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, HIRuntime.getModifierKey() + ActionEvent.SHIFT_MASK));
        changeUserMenuItem.setActionCommand("changeUser");
        fileMenu.add(changeUserMenuItem);
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, HIRuntime.getModifierKey()));
        exitMenuItem.setActionCommand("exit");
        fileMenu.add(exitMenuItem);
        guiMenuBar.add(fileMenu);
        exportMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/export-menu.png")));
        exportMenuItem.setActionCommand("exportProject");
        exportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, HIRuntime.getModifierKey()));
        projectMenu.add(exportMenuItem);
        projectMenu.add(new JSeparator());
        administrateProjectPrefsMenuItem.setActionCommand("librarySettings");
        projectMenu.add(administrateProjectPrefsMenuItem);
        projectMenu.add(new JSeparator());
        projectSettingsMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/preferences-menu.png")));
        projectSettingsMenuItem.setActionCommand("projectSettings");
        projectSettingsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, HIRuntime.getModifierKey()));
        projectMenu.add(projectSettingsMenuItem);
        projectTemplatesMenuItem.setActionCommand("templateEditor");
        projectMenu.add(projectTemplatesMenuItem);
        administrateProjectUsersMenuItem.setActionCommand("manageProjectUsers");
        projectMenu.add(administrateProjectUsersMenuItem);
        xmlImportMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/import-menu.png")));
        xmlImportMenuItem.setActionCommand("xmlImportProject");
        xmlImportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, HIRuntime.getModifierKey()));
        projectMenu.add(xmlImportMenuItem);
        projectMenu.add(new JSeparator());
        changeProjectMenuItem.setActionCommand("changeProject");
        projectMenu.add(changeProjectMenuItem);
        guiMenuBar.add(projectMenu);
        newGroupBrowserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, HIRuntime.getModifierKey()));
        newGroupBrowserMenuItem.setActionCommand("newBrowser");
        toolsMenu.add(newGroupBrowserMenuItem);
        searchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, HIRuntime.getModifierKey()));
        searchMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/search-menu.png")));
        searchMenuItem.setActionCommand("newSearch");
        toolsMenu.add(searchMenuItem);
        toolsMenu.add(new JSeparator());
        importMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, HIRuntime.getModifierKey()));
        importMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/import-menu.png")));
        importMenuItem.setActionCommand("repositoryImport");
        toolsMenu.add(importMenuItem);
        guiMenuBar.add(toolsMenu);
        nextWindowItem.setActionCommand("nextWindow");
        nextWindowItem.setEnabled(false);
        nextWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_GREATER, HIRuntime.getModifierKey()));
        windowMenu.add(nextWindowItem);
        prevWindowItem.setActionCommand("previousWindow");
        prevWindowItem.setEnabled(false);
        prevWindowItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_LESS, HIRuntime.getModifierKey()));
        windowMenu.add(prevWindowItem);
        windowMenu.add(windowSeparator1);
        toggleMetadataViewMenuItem.setActionCommand("toggleMetadata");
        toggleMetadataViewMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, HIRuntime.getModifierKey()));
        toggleMetadataViewMenuItem.setEnabled(false);
        windowMenu.add(toggleMetadataViewMenuItem);
        windowMenu.add(windowSeparator2);
        guiMenuBar.add(windowMenu);
        aboutMenuItem.setActionCommand("about");
        helpMenu.add(aboutMenuItem);
        helpMenu.add(new JSeparator());
        contentMenuItem.setActionCommand("notImplemented");
        helpMenu.add(contentMenuItem);
        visitWebsiteMenuItem.setActionCommand("visitWebsite");
        helpMenu.add(visitWebsiteMenuItem);
        feedbackItem = new JMenuItem();
        feedbackItem.setActionCommand("FEEDBACK");
        feedbackItem.addActionListener(this);
        guiMenuBar.add(helpMenu);
        setJMenuBar(guiMenuBar);
        debugMenu = new JMenu("Debug");
        JMenuItem debugItem = new JMenuItem("GTK Look and Feel");
        debugItem.setActionCommand("GTK");
        debugItem.addActionListener(this);
        debugMenu.add(debugItem);
        debugItem = new JMenuItem("System Look and Feel");
        debugItem.setActionCommand("SYSTEM");
        debugItem.addActionListener(this);
        debugMenu.add(debugItem);
        debugMenu.add(new JSeparator());
        debugItem = new JMenuItem("Perform WS Tests...");
        debugItem.setActionCommand("WS_TEST");
        debugItem.addActionListener(this);
        debugMenu.add(debugItem);
        if (HIRuntime.getManager().getServerURL().indexOf("localhost") >= 0) guiMenuBar.add(debugMenu);
        popupToolsMenu = new JMenu();
        popupNewGroupBrowserMenuItem = new JMenuItem();
        popupNewGroupBrowserMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, HIRuntime.getModifierKey()));
        popupNewGroupBrowserMenuItem.setActionCommand("newBrowser");
        popupToolsMenu.add(popupNewGroupBrowserMenuItem);
        popupSearchMenuItem = new JMenuItem();
        popupSearchMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, HIRuntime.getModifierKey()));
        popupSearchMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/search-menu.png")));
        popupSearchMenuItem.setActionCommand("notImplemented");
        popupToolsMenu.add(popupSearchMenuItem);
        popupToolsMenu.add(new JSeparator());
        popupImportMenuItem = new JMenuItem();
        popupImportMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, HIRuntime.getModifierKey()));
        popupImportMenuItem.setIcon(new ImageIcon(getClass().getResource("/resources/icons/import-menu.png")));
        popupImportMenuItem.setActionCommand("notImplemented");
        popupToolsMenu.add(popupImportMenuItem);
        aboutMenuItem.addActionListener(this);
        editUserMenuItem.addActionListener(this);
        changeUserMenuItem.addActionListener(this);
        changeProjectMenuItem.addActionListener(this);
        exitMenuItem.addActionListener(this);
        newGroupBrowserMenuItem.addActionListener(this);
        searchMenuItem.addActionListener(this);
        importMenuItem.addActionListener(this);
        exportMenuItem.addActionListener(this);
        xmlImportMenuItem.addActionListener(this);
        projectSettingsMenuItem.addActionListener(this);
        projectTemplatesMenuItem.addActionListener(this);
        administrateProjectUsersMenuItem.addActionListener(this);
        administrateProjectPrefsMenuItem.addActionListener(this);
        prevWindowItem.addActionListener(this);
        nextWindowItem.addActionListener(this);
        toggleMetadataViewMenuItem.addActionListener(this);
        contentMenuItem.addActionListener(this);
        visitWebsiteMenuItem.addActionListener(this);
        popupNewGroupBrowserMenuItem.addActionListener(this);
        popupSearchMenuItem.addActionListener(this);
        popupImportMenuItem.addActionListener(this);
        for (int i = 0; i < HIRuntime.supportedLanguages.length; i++) {
            JCheckBoxMenuItem langItem = new JCheckBoxMenuItem();
            langItem.setActionCommand("setLang_" + i);
            langItem.addActionListener(this);
            guiLanguageMenu.add(langItem);
        }
        setMenuText();
    }

    /**
	 * (Re)sets menu / menu item titles in current GUI language. This will be called after a language change
	 */
    private void setMenuText() {
        fileMenu.setText(Messages.getString("HIClientGUI.64"));
        editUserMenuItem.setText(Messages.getString("HIClientGUI.65"));
        guiLanguageMenu.setText(Messages.getString("HIClientGUI.66"));
        changeUserMenuItem.setText(Messages.getString("HIClientGUI.67"));
        exitMenuItem.setText(Messages.getString("HIClientGUI.68"));
        projectMenu.setText(Messages.getString("HIClientGUI.69"));
        exportMenuItem.setText(Messages.getString("HIClientGUI.70"));
        xmlImportMenuItem.setText(Messages.getString("HIClientGUI.93"));
        administrateProjectPrefsMenuItem.setText(Messages.getString("HIClientGUI.71"));
        projectSettingsMenuItem.setText(Messages.getString("HIClientGUI.72"));
        projectTemplatesMenuItem.setText(Messages.getString("HIClientGUI.73"));
        administrateProjectUsersMenuItem.setText(Messages.getString("HIClientGUI.74"));
        changeProjectMenuItem.setText(Messages.getString("HIClientGUI.75"));
        toolsMenu.setText(Messages.getString("HIClientGUI.76"));
        newGroupBrowserMenuItem.setText(Messages.getString("HIClientGUI.77"));
        searchMenuItem.setText(Messages.getString("HIClientGUI.78"));
        importMenuItem.setText(Messages.getString("HIClientGUI.79"));
        windowMenu.setText(Messages.getString("HIClientGUI.80"));
        nextWindowItem.setText(Messages.getString("HIClientGUI.81"));
        prevWindowItem.setText(Messages.getString("HIClientGUI.82"));
        toggleMetadataViewMenuItem.setText(Messages.getString("HIClientGUI.83"));
        helpMenu.setText(Messages.getString("HIClientGUI.84"));
        aboutMenuItem.setText(Messages.getString("HIClientGUI.85"));
        contentMenuItem.setText(Messages.getString("HIClientGUI.86"));
        visitWebsiteMenuItem.setText(Messages.getString("HIClientGUI.87"));
        feedbackItem.setText(Messages.getString("HIClientGUI.88"));
        popupNewGroupBrowserMenuItem.setText(newGroupBrowserMenuItem.getText());
        popupSearchMenuItem.setText(searchMenuItem.getText());
        popupImportMenuItem.setText(importMenuItem.getText());
        for (int i = 0; i < HIRuntime.supportedLanguages.length; i++) {
            Locale guiLang = HIRuntime.supportedLanguages[i];
            JCheckBoxMenuItem langItem = (JCheckBoxMenuItem) guiLanguageMenu.getMenuComponent(i);
            langItem.setText(guiLang.getDisplayLanguage());
            langItem.setSelected(guiLang.getLanguage().compareTo(HIRuntime.getGUILanguage().getLanguage()) == 0 ? true : false);
        }
    }

    /**
	 * Sets the state of all GUI menus after a change occurred (e.g. a window was closed or got focus)
	 */
    private void setMenuState() {
        if (frontFrame != null) {
            toggleMetadataViewMenuItem.setEnabled(frontFrame.hasMetadataView());
            if (frontFrame.isMetadataVisible()) toggleMetadataViewMenuItem.setText(Messages.getString("HIClientGUI.89")); else toggleMetadataViewMenuItem.setText(Messages.getString("HIClientGUI.90"));
        } else toggleMetadataViewMenuItem.setEnabled(false);
        boolean hasWindows = false;
        if (components.size() > 0) hasWindows = true;
        nextWindowItem.setEnabled(hasWindows);
        prevWindowItem.setEnabled(hasWindows);
        exportMenuItem.setEnabled(checkEditAbility(true));
        xmlImportMenuItem.setEnabled(checkAdminAbility(true));
        administrateProjectUsersMenuItem.setEnabled(checkAdminAbility(true));
        projectSettingsMenuItem.setEnabled(checkAdminAbility(true));
        projectTemplatesMenuItem.setEnabled(checkAdminAbility(true));
        administrateProjectPrefsMenuItem.setEnabled(checkEditAbility(true));
    }

    private HIComponentFrame getFrontMostFrame() {
        return frontFrame;
    }

    public void displayNotImplementedDialog() {
        displayInfoDialog(Messages.getString("HIClientGUI.91"), Messages.getString("HIClientGUI.92"));
    }

    private void showDefaultProjectLayout() {
        registerComponent(new GroupBrowser());
    }

    @SuppressWarnings("unchecked")
    public int getComponentTypeCount(Class componentClass) {
        int count = 0;
        for (HIComponentFrame frame : components) if (frame.getHIComponent().getClass() == componentClass) count = count + 1;
        return count;
    }

    public HIComponentFrame getEditingComponentForElement(HiQuickInfo info) {
        if (info.getContentType() == HiBaseTypes.HI_OBJECT) {
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof ObjectEditor) if (frame.getHIComponent().getBaseElement().getId() == info.getBaseID()) return frame;
        }
        if (info.getContentType() == HiBaseTypes.HI_VIEW || info.getContentType() == HiBaseTypes.HI_INSCRIPTION) {
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof ObjectEditor) if (frame.getHIComponent().getBaseElement().getId() == info.getRelatedID()) return frame;
        }
        if (info.getContentType() == HiBaseTypes.HI_LAYER) {
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof LayerEditor) if (frame.getHIComponent().getBaseElement().getId() == info.getRelatedID()) return frame;
        }
        if (info.getContentType() == HiBaseTypes.HI_GROUP) {
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof GroupBrowser) if (frame.getHIComponent().getBaseElement().getId() == info.getBaseID()) return frame;
        }
        if (info.getContentType() == HiBaseTypes.HI_TEXT || info.getContentType() == HiBaseTypes.HIURL) {
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof GenericMetadataEditor) if (frame.getHIComponent().getBaseElement().getId() == info.getBaseID()) return frame;
        }
        if (info.getContentType() == HiBaseTypes.HI_LIGHT_TABLE) {
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof LightTableEditor) if (frame.getHIComponent().getBaseElement().getId() == info.getBaseID()) return frame;
        }
        return null;
    }

    public boolean focusComponent(HIComponentFrame frame) {
        if (frame == frontFrame) return true;
        frame.moveToFront();
        try {
            frame.setSelected(true);
            frontFrame = frame;
            setMenuState();
        } catch (PropertyVetoException e1) {
            return false;
        }
        return true;
    }

    public boolean focusComponent(HIComponent component) {
        for (HIComponentFrame frame : components) if (frame.getHIComponent() == component) return focusComponent(frame);
        return false;
    }

    public void updateComponentTitle(HIComponent sender) {
        for (HIComponentFrame frame : components) if (frame.getHIComponent() == sender) frame.updateTitle();
    }

    public void registerComponent(final HIComponent component) {
        SwingUtilities.invokeLater(new Runnable() {

            @SuppressWarnings("serial")
            public void run() {
                HIComponentFrame componentFrame = null;
                for (HIComponentFrame regFrame : components) if (regFrame.getHIComponent() == component) componentFrame = regFrame;
                if (componentFrame != null) {
                    focusComponent(componentFrame);
                    return;
                }
                componentFrame = new HIComponentFrame(component);
                componentFrame.setFocusable(true);
                mdiPane.add(componentFrame, JLayeredPane.DEFAULT_LAYER);
                componentFrame.addInternalFrameListener(HIRuntime.getGui());
                componentFrame.addHierarchyListener(HIRuntime.getGui());
                componentFrame.addComponentListener(HIRuntime.getGui());
                componentFrame.getHIComponent().getWindowMenuItem().addActionListener(HIRuntime.getGui());
                if (component instanceof LayerEditor || component instanceof GroupBrowser) {
                    componentFrame.setSize(HIRuntime.getGui().getContentPane().getSize().width, componentFrame.getSize().height);
                    if (component instanceof GroupBrowser) {
                        componentFrame.setSize(HIRuntime.getGui().getContentPane().getSize().width, componentFrame.getSize().height);
                        componentFrame.setLocation(0, 0 + ((1 + getComponentTypeCount(GroupBrowser.class) * 50)));
                    }
                    componentFrame.setMetadataVisible(true);
                } else componentFrame.setLocation((HIRuntime.getGui().getContentPane().getSize().width / 2) - (componentFrame.getSize().width / 2) + ((getComponentTypeCount(component.getClass()) * 30)), (HIRuntime.getGui().getContentPane().getSize().height / 2) - (componentFrame.getSize().height / 2) + ((getComponentTypeCount(component.getClass()) * 30)));
                componentFrame.setVisible(true);
                focusComponent(componentFrame);
                if (System.getProperty("os.name").toLowerCase().indexOf("mac") != -1) {
                    componentFrame.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.META_MASK), "closeOnMac");
                    componentFrame.getActionMap().put("closeOnMac", new AbstractAction() {

                        public void actionPerformed(ActionEvent e) {
                            ((HIComponentFrame) e.getSource()).doDefaultCloseAction();
                        }
                    });
                }
                components.add(componentFrame);
                windowMenu.add(componentFrame.getHIComponent().getWindowMenuItem());
                for (int i = 0; i < components.size(); i++) components.get(i).getHIComponent().getWindowMenuItem().setActionCommand("showComponent_" + i);
                frontFrame = componentFrame;
                setMenuState();
            }
        });
    }

    public boolean deregisterComponent(HIComponentFrame componentFrame, boolean force) {
        focusComponent(componentFrame);
        setMenuState();
        if (!force) {
            if (!componentFrame.getHIComponent().requestClose()) return false;
        }
        componentFrame.removeInternalFrameListener(this);
        componentFrame.removeHierarchyListener(this);
        componentFrame.getHIComponent().getWindowMenuItem().removeActionListener(this);
        componentFrame.removeComponentListener(HIRuntime.getGui());
        if (components.size() > 1) {
            int index = components.indexOf(componentFrame);
            index = index - 1;
            if (index < 0) index = components.size() - 1;
            focusComponent(components.get(index));
        }
        componentFrame.setVisible(false);
        components.remove(componentFrame);
        componentFrame.dispose();
        setMenuState();
        windowMenu.remove(componentFrame.getHIComponent().getWindowMenuItem());
        for (int i = 0; i < components.size(); i++) components.get(i).getHIComponent().getWindowMenuItem().setActionCommand("showComponent_" + i);
        return true;
    }

    public boolean deregisterComponent(HIComponent component, boolean force) {
        HIComponentFrame compFrame = null;
        for (HIComponentFrame frame : components) if (frame.getHIComponent() == component) compFrame = frame;
        if (compFrame != null) return deregisterComponent(compFrame, force);
        return false;
    }

    /**
	 * Tries to close all active components and their GUI windows, prompting the user if there are unsaved changes
	 * @return <code>true</code> if all active component windows have been registered; 
	 * <code>false</code> otherwise, e.g. the user canceled the operation
	 */
    public boolean deregisterAllComponents() {
        while (components.size() > 0) if (!deregisterComponent(components.get(components.size() - 1), false)) {
            displayInfoDialog(APPNAME, Messages.getString("HIClientGUI.100"));
            return false;
        }
        return true;
    }

    /**
	 * Asks to user to save all open / edited elements without actually closing the GUI component frames
	 * @return <code>true</code> if all changed elements have been saved; 
	 * <code>false</code> otherwise, e.g. the user canceled the operation
	 */
    public boolean saveAllOpenWork() {
        for (HIComponentFrame frame : components) if (!frame.getHIComponent().requestClose()) return false;
        return true;
    }

    /**
	 * called by components to notify GUI that an element has been moved to the trash
	 * The GUI will check if element is currently being edited by another component and force-close that component
	 * 
	 * @param baseID id of item in trash
	 */
    public void notifyItemSentToTrash(long baseID) {
        Vector<HIComponentFrame> componentsToDelete = new Vector<HIComponentFrame>();
        for (HIComponentFrame frame : components) if (frame.getHIComponent().getBaseElement() != null && frame.getHIComponent().getBaseElement().getId() == baseID) componentsToDelete.addElement(frame);
        for (HIComponentFrame frame : componentsToDelete) deregisterComponent(frame, true);
    }

    /**
	 * propagates changes of a base element (group, object, layer, etc.) to all other GUI components / frames
	 * so all frames and views are in sync and display the updated data
	 * This method should be called by a HIComponent after a "save" operation.
	 * @param message the type of update (add, delete, change, ...) specified by HIComponent.HIMessageTypes enum
	 * @param base HI Editor element that was updated
	 * @param sender component that updated this element
	 */
    public void sendMessage(HIMessageTypes message, HiBase base, HIComponent sender) {
        for (HIComponentFrame frame : components) if (frame.getHIComponent() != sender) frame.getHIComponent().receiveMessage(message, base);
    }

    public void openContentEditor(long baseID, HIComponent sender) {
        HiQuickInfo info = null;
        try {
            info = HIRuntime.getManager().getBaseQuickInfo(baseID);
            if (info != null) openContentEditor(info, sender);
        } catch (HIWebServiceException wse) {
            reportError(wse, sender);
            return;
        }
    }

    public void openContentEditor(final HiQuickInfo content, HIComponent sender) {
        HIComponentFrame editingFrame = getEditingComponentForElement(content);
        if (editingFrame != null) {
            focusComponent(editingFrame);
            if (content.getContentType() == HiBaseTypes.HI_VIEW || content.getContentType() == HiBaseTypes.HI_INSCRIPTION) ((ObjectEditor) editingFrame.getHIComponent()).displayContentByID(content.getBaseID());
            if (content.getContentType() == HiBaseTypes.HI_LAYER) ((LayerEditor) editingFrame.getHIComponent()).displayLayerByID(content.getBaseID());
            return;
        }
        new Thread() {

            public void run() {
                startIndicatingServiceActivity();
                GenericMetadataEditor editor = null;
                if (content.getContentType() == HiBaseTypes.HI_TEXT) {
                    try {
                        editor = new GenericMetadataEditor((HiText) manager.getBaseElement(content.getBaseID()));
                        registerComponent(editor);
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else if (content.getContentType() == HiBaseTypes.HI_VIEW || content.getContentType() == HiBaseTypes.HI_INSCRIPTION) {
                    try {
                        HiObject object = (HiObject) manager.getBaseElement(content.getRelatedID());
                        ObjectEditor objEditor = new ObjectEditor(object, content.getBaseID());
                        registerComponent(objEditor);
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else if (content.getContentType() == HiBaseTypes.HI_LAYER) {
                    try {
                        HiView view = (HiView) manager.getBaseElement(content.getRelatedID());
                        openLayerEditor(view, content.getBaseID());
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else if (content.getContentType() == HiBaseTypes.HIURL) {
                    try {
                        editor = new GenericMetadataEditor((Hiurl) manager.getBaseElement(content.getBaseID()));
                        registerComponent(editor);
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else if (content.getContentType() == HiBaseTypes.HI_LIGHT_TABLE) {
                    try {
                        HiLightTable lightTable = (HiLightTable) manager.getBaseElement(content.getBaseID());
                        LightTableEditor ltEditor = new LightTableEditor(lightTable);
                        registerComponent(ltEditor);
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else if (content.getContentType() == HiBaseTypes.HI_OBJECT) {
                    try {
                        HiObject object = (HiObject) manager.getBaseElement(content.getBaseID());
                        ObjectEditor objEditor = new ObjectEditor(object);
                        registerComponent(objEditor);
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else if (content.getContentType() == HiBaseTypes.HI_GROUP) {
                    try {
                        HiGroup group = (HiGroup) manager.getBaseElement(content.getBaseID());
                        GroupBrowser browser = new GroupBrowser(group);
                        registerComponent(browser);
                    } catch (HIWebServiceException wse) {
                        reportError(wse, null);
                        return;
                    }
                } else JOptionPane.showMessageDialog(HIRuntime.getGui(), "GUI ERROR: Don´t know how to handle " + content.getContentType() + " (" + content.getBaseID() + ") yet!");
                stopIndicatingServiceActivity();
            }
        }.start();
    }

    public void openLayerEditor(HiView view) {
        openLayerEditor(view, -1);
    }

    private void openLayerEditor(final HiView view, final long layerID) {
        System.gc();
        long neededMem = (view.getWidth() * view.getHeight() * 3);
        if ((Runtime.getRuntime().freeMemory() - neededMem) < HIRuntime.MINIMUM_FREE_MEMORY) {
            boolean choice = displayUserYesNoDialog(Messages.getString("HIClientGUI.104"), Messages.getString("HIClientGUI.105") + "\n" + Messages.getString("HIClientGUI.107") + " (" + view.getWidth() + "*" + view.getHeight() + " px) " + Messages.getString("HIClientGUI.111") + "\n\n" + Messages.getString("HIClientGUI.113"));
            if (choice) {
                displayNotImplementedDialog();
                return;
            } else return;
        }
        new Thread() {

            public void run() {
                startIndicatingServiceActivity();
                PlanarImage image;
                try {
                    image = HIRuntime.getManager().getImage(view.getId(), HiImageSizes.HI_FULL, true);
                    stopIndicatingServiceActivity();
                    if (image == null) {
                        displayInfoDialog(Messages.getString("HIClientGUI.114"), Messages.getString("HIClientGUI.115"));
                        return;
                    }
                    if (layerID > 0) registerComponent(new LayerEditor(view, image, layerID)); else registerComponent(new LayerEditor(view, image));
                    System.gc();
                } catch (HIWebServiceException wse) {
                    reportError(wse, null);
                    return;
                }
            }
        }.start();
    }

    public void setGUIEnabled(boolean enabled) {
        fileMenu.setEnabled(enabled);
        projectMenu.setEnabled(enabled);
        toolsMenu.setEnabled(enabled);
        windowMenu.setEnabled(enabled);
        helpMenu.setEnabled(enabled);
    }

    public void startIndicatingServiceActivity() {
        startIndicatingServiceActivity(false);
    }

    public void startIndicatingServiceActivity(boolean showImmediately) {
        if (serviceActivity) return;
        serviceActivity = true;
        mdiPane.setCursor(waitCursor);
        HIRuntime.getGui().setGUIEnabled(false);
        setMessage(Messages.getString("HIClientGUI.20"));
        if (showImmediately) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    if (serviceActivity) infoGlassPane.setVisible(true);
                }
            });
        } else {
            new Thread() {

                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        return;
                    }
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            if (serviceActivity) infoGlassPane.setVisible(true);
                        }
                    });
                }
            }.start();
        }
    }

    public void setProgress(int progress) {
        infoGlassPane.setProgress(progress);
    }

    public void setMessage(String message) {
        infoGlassPane.setMessage(message);
    }

    public void stopIndicatingServiceActivity() {
        if (!serviceActivity) return;
        mdiPane.setCursor(Cursor.getDefaultCursor());
        serviceActivity = false;
        HIRuntime.getGui().setGUIEnabled(true);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                infoGlassPane.setVisible(false);
            }
        });
    }

    public int displayUserChoiceDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
    }

    public boolean displayUserYesNoDialog(String title, String message) {
        int answer = JOptionPane.showConfirmDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer == JOptionPane.YES_OPTION) return true;
        return false;
    }

    public void displayInfoDialog(String title, String message) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public void displayReconnectDialog() {
        if (HIRuntime.getManager().getState() != HIWebServiceManager.WSStates.RECONNECT) {
            HIRuntime.getManager().setReconnect();
            int answer = JOptionPane.showConfirmDialog(this, Messages.getString("HIClientGUI.116"), Messages.getString("HIClientGUI.117"), JOptionPane.OK_CANCEL_OPTION);
            if (answer == JOptionPane.OK_OPTION) {
                try {
                    if (HIRuntime.getManager().reconnect() == false) if (deregisterAllComponents()) if (!handleLogin()) tryLogoutAndExit(); else {
                        chooseProject();
                        showDefaultProjectLayout();
                    } else System.exit(1);
                } catch (HIWebServiceException wse) {
                    reportError(wse, null);
                    if (deregisterAllComponents()) if (!handleLogin()) tryLogoutAndExit(); else {
                        chooseProject();
                        showDefaultProjectLayout();
                    } else System.exit(1);
                }
            } else {
                if (deregisterAllComponents()) if (!handleLogin()) tryLogoutAndExit(); else {
                    chooseProject();
                    showDefaultProjectLayout();
                } else System.exit(1);
            }
        }
    }

    public void displayMaintenanceDialog(HIWebServiceException wse) {
        HIRuntime.getGui().displayInfoDialog(Messages.getString("HIClientGUI.118"), "<html>" + Messages.getString("HIClientGUI.120") + "<br><br>" + "<b>" + Messages.getString("HIClientGUI.123") + "</b><br>" + wse.getCause().getMessage() + "</html>");
    }

    public void clearLastWSError() {
        lastWSError = null;
    }

    public HIWebServiceException getLastWSError() {
        return lastWSError;
    }

    public void reportError(HIWebServiceException wse, HIComponent sender) {
        serviceActivity = true;
        stopIndicatingServiceActivity();
        lastWSError = wse;
        if (!displayingError) {
            if (wse.getErrorType() == HIWebServiceException.HIerrorTypes.RECONNECT) displayReconnectDialog(); else if (wse.getErrorType() == HIWebServiceException.HIerrorTypes.MAINTENANCE) {
                displayMaintenanceDialog(wse);
                displayingError = false;
                while (components.size() > 0) deregisterComponent(components.get(0), true);
                if (!handleLogin()) {
                    tryLogoutAndExit();
                    System.exit(1);
                } else {
                    chooseProject();
                    showDefaultProjectLayout();
                }
            } else {
                wse.getCause().printStackTrace();
                JOptionPane.showMessageDialog(this, "Web Service Error: " + wse.getCause().getClass() + "\n\nReason: " + wse.getCause().getMessage() + "\n");
            }
        }
        displayingError = false;
    }

    public boolean checkEditAbility(boolean silent) {
        if (HIRuntime.getManager().getCurrentRole() == HiRoles.GUEST) {
            if (!silent) JOptionPane.showMessageDialog(HIRuntime.getGui(), Messages.getString("HIClientGUI.128"), Messages.getString("HIClientGUI.129"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public boolean checkAdminAbility(boolean silent) {
        if (HIRuntime.getManager().getCurrentRole() != HiRoles.ADMIN) {
            if (!silent) JOptionPane.showMessageDialog(HIRuntime.getGui(), Messages.getString("HIClientGUI.130"), Messages.getString("HIClientGUI.131"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("notImplemented")) displayNotImplementedDialog();
        if (e.getActionCommand().equalsIgnoreCase("about")) new AboutDialog(this).setVisible(true);
        if (e.getActionCommand().equalsIgnoreCase("accountSettings")) {
            if (getComponentTypeCount(AccountSettings.class) > 0) {
                for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof AccountSettings) focusComponent(frame);
            } else {
                registerComponent(new AccountSettings(HIRuntime.getManager().getCurrentUser()));
            }
        }
        if (e.getActionCommand().startsWith("setLang_")) {
            int index = Integer.parseInt(e.getActionCommand().substring(8));
            HIRuntime.setGUILanguage(HIRuntime.supportedLanguages[index]);
            setMenuText();
            for (HIComponentFrame frame : components) frame.getHIComponent().updateLanguage();
            setMenuState();
        }
        if (e.getActionCommand().equalsIgnoreCase("exit")) if (displayUserYesNoDialog(Messages.getString("HIClientGUI.2"), Messages.getString("HIClientGUI.138"))) tryLogoutAndExit();
        if (e.getActionCommand().equalsIgnoreCase("changeUser")) if (displayUserYesNoDialog(Messages.getString("HIClientGUI.140"), Messages.getString("HIClientGUI.141"))) if (deregisterAllComponents()) if (!handleLogin()) tryLogoutAndExit(); else {
            chooseProject();
            showDefaultProjectLayout();
        }
        if (e.getActionCommand().equalsIgnoreCase("xmlImportProject")) handleXMLImport();
        if (e.getActionCommand().equalsIgnoreCase("exportProject")) handleExport();
        if (e.getActionCommand().equalsIgnoreCase("projectSettings")) {
            if (getComponentTypeCount(ProjectSettings.class) > 0) {
                for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof ProjectSettings) focusComponent(frame);
            } else {
                registerComponent(new ProjectSettings());
            }
        }
        if (e.getActionCommand().equalsIgnoreCase("templateEditor")) {
            if (getComponentTypeCount(TemplateEditor.class) > 0) {
                for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof TemplateEditor) focusComponent(frame);
            } else {
                if (getComponentTypeCount(ObjectEditor.class) > 0) {
                    boolean closeObjectEditors = displayUserYesNoDialog(Messages.getString("HIClientGUI.200"), Messages.getString("HIClientGUI.201") + "\n\n" + Messages.getString("HIClientGUI.202"));
                    if (closeObjectEditors) {
                        Vector<HIComponentFrame> objectEditorFrames = new Vector<HIComponentFrame>();
                        for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof ObjectEditor) objectEditorFrames.addElement(frame);
                        while (objectEditorFrames.size() > 0) if (!deregisterComponent(objectEditorFrames.get(0), false)) return; else objectEditorFrames.remove(0);
                    } else return;
                }
                registerComponent(new TemplateEditor());
            }
        }
        if (e.getActionCommand().equalsIgnoreCase("librarySettings")) {
            if (getComponentTypeCount(PreferenceManager.class) > 0) {
                for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof PreferenceManager) focusComponent(frame);
            } else {
                registerComponent(new PreferenceManager());
            }
        }
        if (e.getActionCommand().equalsIgnoreCase("manageProjectUsers")) {
            HIComponentFrame userManagerFrame = null;
            for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof ProjectUsersManager) userManagerFrame = frame;
            if (userManagerFrame == null) registerComponent(new ProjectUsersManager()); else focusComponent(userManagerFrame);
        }
        if (e.getActionCommand().equalsIgnoreCase("changeProject")) if (displayUserYesNoDialog(Messages.getString("HIClientGUI.3"), Messages.getString("HIClientGUI.147"))) if (deregisterAllComponents()) {
            chooseProject();
            showDefaultProjectLayout();
        }
        if (e.getActionCommand().equalsIgnoreCase("newBrowser")) registerComponent(new GroupBrowser());
        if (e.getActionCommand().equalsIgnoreCase("newSearch")) registerComponent(new SearchModule());
        if (e.getActionCommand().equalsIgnoreCase("repositoryImport")) {
            if (getComponentTypeCount(RepositoryImport.class) > 0) {
                for (HIComponentFrame frame : components) if (frame.getHIComponent() instanceof RepositoryImport) focusComponent(frame);
            } else {
                registerComponent(new RepositoryImport());
            }
        }
        if (e.getActionCommand().equalsIgnoreCase("nextWindow")) if (getFrontMostFrame() != null && components.size() > 0) {
            int index = components.indexOf(getFrontMostFrame());
            index = index + 1;
            if (index >= components.size()) index = 0;
            HIComponentFrame frame = components.get(index);
            focusComponent(frame);
        }
        if (e.getActionCommand().equalsIgnoreCase("previousWindow")) if (getFrontMostFrame() != null && components.size() > 0) {
            int index = components.indexOf(getFrontMostFrame());
            index = index - 1;
            if (index < 0) index = components.size() - 1;
            HIComponentFrame frame = components.get(index);
            focusComponent(frame);
        }
        if (e.getActionCommand().equalsIgnoreCase("toggleMetadata")) {
            if (getFrontMostFrame() != null) {
                getFrontMostFrame().setMetadataVisible(!getFrontMostFrame().isMetadataVisible());
                setMenuState();
            }
        }
        if (e.getActionCommand().startsWith("showComponent_")) {
            int index = Integer.parseInt(e.getActionCommand().substring(14));
            HIComponentFrame frame = components.get(index);
            focusComponent(frame);
            setMenuState();
        }
        if (e.getActionCommand().equalsIgnoreCase("visitWebsite")) {
            try {
                Desktop.getDesktop().browse(new URI("http://hyperimage.sourceforge.net/"));
            } catch (Exception e1) {
                displayInfoDialog(Messages.getString("HIClientGUI.10"), Messages.getString("HIClientGUI.14") + "\n\n" + Messages.getString("HIClientGUI.21") + "\n" + "http://hyperimage.sourceforge.net/");
            }
        }
        if (e.getActionCommand().equalsIgnoreCase("WS_TEST")) {
            System.out.println("starting tests...");
            startIndicatingServiceActivity();
            try {
                HiFlexMetadataTemplate template = null;
                for (HiFlexMetadataTemplate temp : HIRuntime.getManager().getProject().getTemplates()) {
                    if (temp.getNamespacePrefix().startsWith("dc")) template = temp;
                }
                if (template != null) {
                    System.out.println("removing...");
                    HiFlexMetadataSet set = null;
                    for (HiFlexMetadataSet tempSet : template.getEntries()) if (tempSet.getTagname().equalsIgnoreCase("media")) set = tempSet;
                    if (set != null) HIRuntime.getManager().removeSetFromTemplate(template, set);
                }
            } catch (HIWebServiceException wse) {
                reportError(wse, null);
            }
            stopIndicatingServiceActivity();
            System.out.println("finished tests");
        }
        if (e.getActionCommand().equalsIgnoreCase("GTK")) {
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (UnsupportedLookAndFeelException e1) {
                e1.printStackTrace();
            }
            SwingUtilities.updateComponentTreeUI(this);
        } else if (e.getActionCommand().equalsIgnoreCase("SYSTEM")) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                SwingUtilities.updateComponentTreeUI(this);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (UnsupportedLookAndFeelException e1) {
                e1.printStackTrace();
            }
        }
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        if (displayUserYesNoDialog(Messages.getString("HIClientGUI.4"), Messages.getString("HIClientGUI.163"))) tryLogoutAndExit();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void internalFrameActivated(InternalFrameEvent e) {
    }

    public void internalFrameClosed(InternalFrameEvent e) {
    }

    public void internalFrameClosing(InternalFrameEvent e) {
        HIComponentFrame compFrame = (HIComponentFrame) e.getSource();
        if (!deregisterComponent(compFrame, false)) return;
    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    public void internalFrameIconified(InternalFrameEvent e) {
    }

    public void internalFrameOpened(InternalFrameEvent e) {
    }

    public void hierarchyChanged(HierarchyEvent e) {
        HIComponentFrame frame = (HIComponentFrame) e.getSource();
        frontFrame = frame;
        setMenuState();
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }

    @Override
    public void componentMoved(ComponentEvent e) {
        if (e.getComponent().getBounds().y < 0) e.getComponent().setBounds(e.getComponent().getBounds().x, 0, e.getComponent().getBounds().width, e.getComponent().getBounds().height);
    }

    @Override
    public void componentResized(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }
}
