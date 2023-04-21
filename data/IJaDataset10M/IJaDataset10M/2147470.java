package net.sf.warpcore.cms.webfrontend.gui;

import net.sf.warpcore.cms.webfrontend.*;
import net.sf.warpcore.cms.webfrontend.git.*;
import net.sf.warpcore.cms.webfrontend.gui.configuration.*;
import net.sf.warpcore.cms.webfrontend.gui.event.*;
import net.sf.warpcore.cms.webfrontend.gui.window.*;
import net.sf.wedgetarian.util.*;
import net.sf.wedgetarian.*;
import net.sf.wedgetarian.event.*;
import javax.naming.*;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;
import java.rmi.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.ejb.FinderException;
import net.sf.warpcore.ejb.UniquePK;
import org.apache.log4j.Category;
import org.apache.log4j.NDC;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import java.io.*;
import net.sf.warpcore.cms.value.*;

public class Cockpit extends Application {

    Git myGit = null;

    private static Category category = Category.getInstance(Cockpit.class.getName());

    private boolean criticalExit = false;

    private Grid cockpitGrid = new Grid("CockpitGrid");

    private DialogContainer dialogContainer = new DialogContainer();

    private static MessageBundle messageBundle = new MessageBundle("/net/sf/warpcore/cms/webfrontend/gui/CockpitMessages");

    public MessageBundle getSharedMessageBundle() {
        return messageBundle;
    }

    private static CockpitConfiguration cockpitConfiguration = new CockpitConfiguration();

    private RepositoryKit repositoryKit;

    private EditorFactory editorFactory;

    public MimetypeKit mimetypeKit;

    private Context context;

    private String cmsUserName;

    private boolean cmsUserAdmin;

    private Vector userGroupNames;

    private Date loginDate;

    private static final int SUPER_USER = 20;

    private static final int DEFAULT_USER = 0;

    private int userStatus = DEFAULT_USER;

    private WindowManager windowManager;

    private WindowDisplay windowDisplay;

    private WindowManagerCockpitDisplay windowManagerCockpitDisplay;

    /** Konfiguration merken **/
    private ConfigurationCreator configurationCreator;

    private Configuration configuration;

    /** Hier die Subapplikationen **/
    private Navigator navigator;

    private Window navigatorWindow;

    private EditorApplication editorApplication;

    private Window editorApplicationWindow;

    public Cockpit() {
        if (cockpitConfiguration.getLoggingConfigurationType().toUpperCase().equals("XML")) {
            DOMConfigurator.configure(cockpitConfiguration.getLoggingConfigurationPath());
        } else {
            PropertyConfigurator.configure(cockpitConfiguration.getLoggingConfigurationPath());
        }
        windowManager = new WindowManager();
        windowDisplay = new WindowDisplay(windowManager);
        if (category.isDebugEnabled()) category.debug("######### Starting Cockpit #########");
        setLocale(Locale.GERMAN);
        setRootWedget(cockpitGrid);
        setDefaultDialogContainer(dialogContainer);
        cockpitGrid.add(dialogContainer, "dialogContainer");
        LoginDialog loginDialog = new LoginDialog(true);
        showDialog(loginDialog);
        loginDialog.addDialogListener(new DialogListener() {

            private boolean alreadyClosed = false;

            public void dialogClosed(DialogEvent event) {
                if (!alreadyClosed) {
                    alreadyClosed = true;
                    String language = (String) ((LoginDialog) event.getSource()).getLanguage();
                    if (language != null) {
                        if (language.equals("Deutsch")) {
                            setLocale(Locale.GERMAN);
                        } else if (language.equals("English")) {
                            setLocale(Locale.ENGLISH);
                        }
                    }
                    loginDate = new Date();
                    init();
                } else {
                    try {
                        Cockpit.this.cockpitGrid.render(Cockpit.this.getRenderContext());
                    } catch (net.sf.wedgetarian.ui.UIException exception) {
                        if (category.isEnabledFor(Priority.ERROR)) category.error(exception);
                    }
                }
            }
        });
    }

    public String getServletUri() {
        return "/cms/cockpit";
    }

    /** Just call this Method once !!! */
    private void init() {
        mimetypeKit = new MimetypeKit((Application) this);
        repositoryKit = new RepositoryKit(this);
        editorFactory = new EditorFactory(repositoryKit, this);
        if (!criticalExit) {
            getUserGroupNames();
            userStatus = SUPER_USER;
            if (userStatus == SUPER_USER) {
                configurationCreator = (ConfigurationCreator) (new DummyUserConfigurationCreator(true));
            } else {
                configurationCreator = (ConfigurationCreator) (new DummyUserConfigurationCreator(false));
            }
            configuration = configurationCreator.createCockpitConfiguration();
            int entries = configuration.getSize();
            for (int i = 0; i < entries; i++) {
                String key = configuration.getKey(i);
                if (key.equals("navigator")) {
                    NavigatorCreator navCreator = new NavigatorCreator();
                    navCreator.create((Configuration) configuration.getValue(i));
                    navigator = navCreator.getNavigator();
                    navigator.addNavigatorListener(new NavigatorListener() {

                        public void openEditor(NavigatorEvent event) {
                            Cockpit.this.openEditorForNode(event.getNode());
                        }

                        public void showEditor(NavigatorEvent event) {
                            Cockpit.this.windowManager.setActiveWindow(editorApplicationWindow);
                        }
                    });
                    navigatorWindow = new Window(navigator, false, "Navigator", "Navigator");
                    windowManager.registerWindow(navigatorWindow);
                }
            }
            windowManagerCockpitDisplay = new WindowManagerCockpitDisplay(windowManager);
            windowManager.setActiveWindow(navigatorWindow);
            cockpitGrid.add(windowManagerCockpitDisplay, "windowManagerCockpitDisplay");
            cockpitGrid.add(windowDisplay, "windowDisplay");
        }
    }

    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String ubC = "not logged in";
        ubC = getCmsUserName();
        NDC.push(ubC);
        super.processRequest(request, response);
        NDC.pop();
    }

    /**
     * Returns a toolkit for the RepositoryObjects the client will use.
     */
    public RepositoryKit getRepositoryKit() {
        return repositoryKit;
    }

    /**
     * Returns the CokpitConfigurationObject. This is ment for some ClientSettings
     * they may differ in several Projects or Versions.
     */
    public CockpitConfiguration getCockpitConfiguration() {
        return cockpitConfiguration;
    }

    /**
     * Returns a Vector of Strings, where the User is in.
     * Or an emprty Vector if something goes wrong.
     */
    public Vector getUserGroupNames() {
        Vector v = new Vector();
        v.addElement("Admins");
        return v;
    }

    /**
     * Returns the authentificated User.
     */
    public String getCmsUser() {
        return "admin";
    }

    /**
     * Returns the authentificated UserName.
     */
    public String getCmsUserName() {
        return "admin";
    }

    public boolean isCmsUserAdmin() {
        return true;
    }

    /**
     * Returns the CockpitStatus of the current user.
     */
    public int getUserStatus() {
        return userStatus;
    }

    /**
     * Returns the Date when the User has logged in.
     */
    public Date getLoginDate() {
        return loginDate;
    }

    /**
     * Return a valid context for the Application you can do a lookup with or it throws
     * a NamingException if failed.
     *
     * @return The context for lookups.
     */
    public Context getContext() throws NamingException {
        if (context == null) {
            context = new InitialContext();
        }
        return context;
    }

    /**
     * Return the Git object for this session.
     *
     * @return The Git object.
     */
    public Git getGit() {
        if (myGit == null) {
            myGit = Git.getCurrent(getHttpSession());
            myGit.setUser(getCmsUserName());
            GitElement gitGlobal = myGit.getGlobalElement();
            gitGlobal.addAttribute("CMSUSER", getCmsUser());
        }
        return myGit;
    }

    /** Private internal known EditorTypes */
    private static final int UNKNOWN_EDITOR = 0;

    private static final int ATOMIX_EDITOR = 1;

    private static final int DELIVERY_EDITOR = 2;

    /**
     * Opens an Editor for a <code>DOMPerignonTreeNode</code>
     * @param node The Node that represents the object to edit
     */
    public void openEditorForNode(DOMPerignonTreeNode node) {
        Object editor = editorFactory.buildEditor(node);
        int type = node.getType();
        int editorType = UNKNOWN_EDITOR;
        if (editor != null) {
            switch(type) {
                case MimetypeKit.FILE:
                case MimetypeKit.INTERNAL:
                    editorType = ATOMIX_EDITOR;
            }
        }
        String titel = node.getName();
        String longTitel = node.getRepositoryGroupName() + ":" + node.getPath();
        newEditorWindow(node.hasRight("accessmethod_files_write"), editor, editorType, titel, longTitel, mimetypeKit.getImageFor(type));
    }

    /**
     * Opens a new EditorWindow in the EditorSubapplication
     *
     * @param editor     The Editor which should be opened in a window. Has to be a Wedget.
     * @param editorType A known EditorType
     * @param titel      The title of the window
     * @param longTitel  The long title of the window
     * @param icon       The Image that should be displayed for the window
     */
    private void newEditorWindow(boolean canSave, Object editor, int editorType, String titel, String longTitel, Image icon) {
        try {
            Wedget editorWedget = (Wedget) editor;
            EditorApplication editorApplication;
            if (editorType != UNKNOWN_EDITOR) {
                editorApplication = getEditorApplication();
                editorApplication.newEditorWindow(canSave, editorWedget, titel, longTitel, icon);
                windowManager.setActiveWindow(editorApplicationWindow);
            } else {
                showDialog(new ErrorDialog(getMessage("ERROR_COCKPIT_no_editor"), null));
            }
        } catch (ClassCastException exception) {
            if (category.isDebugEnabled()) category.debug("That Editor is not suitable for this application !");
            if (category.isEnabledFor(Priority.ERROR)) category.error(exception);
        }
    }

    /**
     * Constructs the Editor(Sub)Application if not done and shows the window.
     * This also adds a Listener to hide it, when receiving the signal.
     */
    private EditorApplication getEditorApplication() {
        if (editorApplication == null) {
            int entries = configuration.getSize();
            if (category.isDebugEnabled()) category.debug("" + entries);
            for (int i = 0; i < entries; i++) {
                String key = configuration.getKey(i);
                if (category.isDebugEnabled()) category.debug(key);
                if (key.equals("editor")) {
                    editorApplication = new EditorApplication();
                    navigator.setEditorWindowManager(editorApplication.getWindowManager());
                    editorApplication.addEditorApplicationListener(new EditorApplicationListener() {

                        public void closeEditorApplication(ActionEvent event) {
                            editorApplicationWindow.close();
                            windowManager.setActiveWindow(navigatorWindow);
                        }
                    });
                }
            }
        }
        if (editorApplicationWindow == null) {
            editorApplicationWindow = new Window(editorApplication, true, "Editor", "Editor");
        }
        if (!windowManager.isWindowRegistered(editorApplicationWindow)) {
            windowManager.registerWindow(editorApplicationWindow);
        }
        return editorApplication;
    }

    public WindowManager getWindowManager() {
        return Cockpit.this.windowManager;
    }

    public Navigator getNavigator() {
        return Cockpit.this.navigator;
    }

    /** Critical Exit - Used for startup failure with CMS-Core - Could be used in other context as well **/
    public void criticalExit(Exception exception) {
        criticalExit = true;
        ErrorDialog errorDialog = new ErrorDialog(getMessage("ERROR_COCKPIT_critical_exit"), exception);
        errorDialog.addDialogListener(new DialogListener() {

            public void dialogClosed(DialogEvent event) {
                Grid cockpitLogoutGrid = new Grid("CockpitLogoutGrid");
                cockpitLogoutGrid.add(new Label("<meta http-equiv=\"refresh\" content=\"5; URL=" + getServletUri() + "\">"), "back_meta_refresh");
                cockpitLogoutGrid.add(new Label(new Image("classpath:///net/sf/warpcore/cms/webfrontend/gui/resources/cockpit/logo.gif")), "logo");
                cockpitLogoutGrid.add(new Label("<a href=\"" + getServletUri() + "\">" + getMessage("logout_back_link") + "</a>"), "back_link");
                setRootWedget(cockpitLogoutGrid);
                close();
            }
        });
        showDialog(errorDialog);
    }
}
