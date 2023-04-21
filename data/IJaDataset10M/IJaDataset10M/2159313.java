package net.sourceforge.buildprocess.webautodeploy.client;

import net.sourceforge.buildprocess.autodeploy.model.Application;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.TabPane;
import nextapp.echo2.extras.app.layout.TabPaneLayoutData;

/**
 * J2EE application window <code>WindowPane</code>
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class J2EEApplicationWindow extends WindowPane {

    private String applicationName;

    private String applicationServerName;

    private Application application = null;

    private EnvironmentJ2EEApplicationsTabPane parent;

    private J2EEApplicationGeneralTabPane j2eeApplicationGeneralTabPane;

    private J2EEApplicationArchivesTabPane j2eeApplicationArchivesTabPane;

    private J2EEApplicationContentManagersTabPane j2eeApplicationContentManagersTabPane;

    private J2EEApplicationConfigurationFilesTabPane j2eeApplicationConfigurationFilesTabPane;

    private J2EEApplicationDatabasesTabPane j2eeApplicationDatabasesTabPane;

    private ActionListener refreshButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            J2EEApplicationWindow.this.application = parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getApplication(applicationName);
            if (J2EEApplicationWindow.this.application == null) {
                J2EEApplicationWindow.this.application = new Application();
            }
            update();
        }
    };

    private ActionListener closeButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            J2EEApplicationWindow.this.userClose();
        }
    };

    private ActionListener deleteButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironmentWindow().getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getApplications().remove(application);
            parent.getEnvironmentWindow().getChangeEvents().add("Delete J2EE application (name [ " + application.getName() + " ] | uri [ " + application.getUri() + " ]) from the J2EE application server " + applicationServerName);
            parent.getEnvironmentWindow().setUpdated(true);
            parent.getEnvironmentWindow().updateJournalLogTabPane();
            parent.update();
            J2EEApplicationWindow.this.userClose();
        }
    };

    private ActionListener applyButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironmentWindow().getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            String applicationNameFieldValue = j2eeApplicationGeneralTabPane.getApplicationNameField().getText();
            int applicationActiveFieldIndex = j2eeApplicationGeneralTabPane.getApplicationActiveField().getSelectedIndex();
            int applicationBlockerFieldIndex = j2eeApplicationGeneralTabPane.getApplicationBlockerField().getSelectedIndex();
            String applicationUriFieldValue = j2eeApplicationGeneralTabPane.getApplicationUriField().getText();
            if (applicationNameFieldValue == null || applicationNameFieldValue.trim().length() < 1) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("J2EEApplicationWindow.Error"), Messages.getString("J2EEApplicationWindow.Error.Mandatory")));
                return;
            }
            if (applicationName == null || (applicationName != null && !applicationName.equals(applicationNameFieldValue))) {
                if (parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getApplication(applicationNameFieldValue) != null) {
                    parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("J2EEApplicationWindow.Error"), Messages.getString("J2EEApplicationWindow.Error.AlreadyExists")));
                    return;
                }
            }
            if (applicationName != null) {
                parent.getEnvironmentWindow().getChangeEvents().add("Change J2EE application (name [ " + application.getName() + " => " + applicationNameFieldValue + " ] | uri [ " + application.getUri() + " => " + applicationUriFieldValue + " ]) in the J2EE application server " + applicationServerName);
            }
            application.setName(applicationNameFieldValue);
            if (applicationActiveFieldIndex == 0) {
                application.setActive(true);
            } else {
                application.setActive(false);
            }
            if (applicationBlockerFieldIndex == 0) {
                application.setBlocker(true);
            } else {
                application.setBlocker(false);
            }
            application.setUri(applicationUriFieldValue);
            if (applicationName == null) {
                try {
                    parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).addApplication(application);
                    parent.getEnvironmentWindow().getChangeEvents().add("Add J2EE application (name [ " + application.getName() + " ] | uri [ " + application.getUri() + " ]) in the J2EE application server " + applicationServerName);
                } catch (Exception e) {
                    parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("J2EEApplicationWindow.Error"), Messages.getString("J2EEApplicationWindow.Error.AlreadyExists")));
                    return;
                }
            }
            setTitle(Messages.getString("J2EEApplicationWindow.Title") + " " + application.getName());
            setId("j2eeApplicationWindow_" + parent.getEnvironmentWindow().getEnvironmentName() + "_" + applicationServerName + "_" + application.getName());
            applicationName = application.getName();
            parent.getEnvironmentWindow().setUpdated(true);
            parent.getEnvironmentWindow().updateJournalLogTabPane();
            parent.update();
            update();
        }
    };

    private ActionListener copyButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            try {
                WebAutoDeployApplication.getApplication().setCopyComponent(application.clone());
            } catch (Exception e) {
                return;
            }
        }
    };

    private ActionListener pasteButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            Object copy = WebAutoDeployApplication.getApplication().getCopyComponent();
            if (copy == null || !(copy instanceof Application)) {
                return;
            }
            application = (Application) copy;
            applicationName = null;
            parent.update();
            update();
        }
    };

    /**
    * Create a new <code>J2EEApplicationWindow</code>
    * 
    * @param parent
    *           the parent <code>EnvironmentJ2EEApplicationsTabPane</code>
    * @param j2eeApplicationServerName
    *           the original J2EE application server name
    * @param j2eeApplicationName
    *           the original J2EE application name
    */
    public J2EEApplicationWindow(EnvironmentJ2EEApplicationsTabPane parent, String j2eeApplicationServerName, String j2eeApplicationName) {
        super();
        this.parent = parent;
        this.applicationServerName = j2eeApplicationServerName;
        this.applicationName = j2eeApplicationName;
        this.application = parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getApplication(applicationName);
        if (this.application == null) {
            this.application = new Application();
        }
        if (applicationName == null) {
            setTitle(Messages.getString("J2EEApplicationWindow.Title"));
        } else {
            setTitle(Messages.getString("J2EEApplicationWindow.Title") + " " + applicationName);
        }
        setId("j2eeApplicationWindow_" + parent.getEnvironmentWindow().getEnvironmentName() + "_" + applicationServerName + "_" + applicationName);
        setStyleName("J2EEApplicationWindow");
        setModal(false);
        setDefaultCloseOperation(WindowPane.DISPOSE_ON_CLOSE);
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        add(splitPane);
        Row controlRow = new Row();
        controlRow.setStyleName("ControlPane");
        splitPane.add(controlRow);
        Button refreshButton = new Button(Messages.getString("Window.Refresh"), Styles.ICON_24_REFRESH);
        refreshButton.setStyleName("ControlPane.Button");
        refreshButton.addActionListener(refreshButtonActionListener);
        controlRow.add(refreshButton);
        Button copyButton = new Button(Messages.getString("Window.Copy"), Styles.ICON_24_COPY);
        copyButton.setStyleName("ControlPane.Button");
        copyButton.addActionListener(copyButtonActionListener);
        controlRow.add(copyButton);
        if (parent.getEnvironmentWindow().getReadWriteAccess()) {
            Button pasteButton = new Button(Messages.getString("Window.Paste"), Styles.ICON_24_PASTE);
            pasteButton.setStyleName("ControlPane.Button");
            pasteButton.addActionListener(pasteButtonActionListener);
            controlRow.add(pasteButton);
            Button applyButton = new Button(Messages.getString("Window.Apply"), Styles.ICON_24_YES);
            applyButton.setStyleName("ControlPane.Button");
            applyButton.addActionListener(applyButtonActionListener);
            controlRow.add(applyButton);
            Button deleteButton = new Button(Messages.getString("Window.Delete"), Styles.ICON_24_NO);
            deleteButton.setStyleName("ControlPane.Button");
            deleteButton.addActionListener(deleteButtonActionListener);
            controlRow.add(deleteButton);
        }
        Button closeButton = new Button(Messages.getString("Window.Close"), Styles.ICON_24_EXIT);
        closeButton.setStyleName("ControlPane.Button");
        closeButton.addActionListener(closeButtonActionListener);
        controlRow.add(closeButton);
        TabPane tabPane = new TabPane();
        tabPane.setStyleName("EnvironmentWindow.TabPane");
        splitPane.add(tabPane);
        TabPaneLayoutData tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("J2EEApplicationWindow.Tab.General"));
        j2eeApplicationGeneralTabPane = new J2EEApplicationGeneralTabPane(this);
        j2eeApplicationGeneralTabPane.setLayoutData(tabLayoutData);
        tabPane.add(j2eeApplicationGeneralTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("J2EEApplicationWindow.Tab.Archives"));
        j2eeApplicationArchivesTabPane = new J2EEApplicationArchivesTabPane(this);
        j2eeApplicationArchivesTabPane.setLayoutData(tabLayoutData);
        tabPane.add(j2eeApplicationArchivesTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("J2EEApplicationWindow.Tab.ConfigurationFiles"));
        j2eeApplicationConfigurationFilesTabPane = new J2EEApplicationConfigurationFilesTabPane(this);
        j2eeApplicationConfigurationFilesTabPane.setLayoutData(tabLayoutData);
        tabPane.add(j2eeApplicationConfigurationFilesTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("J2EEApplicationWindow.Tab.Databases"));
        j2eeApplicationDatabasesTabPane = new J2EEApplicationDatabasesTabPane(this);
        j2eeApplicationDatabasesTabPane.setLayoutData(tabLayoutData);
        tabPane.add(j2eeApplicationDatabasesTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("J2EEApplicationWindow.Tab.ContentManagers"));
        j2eeApplicationContentManagersTabPane = new J2EEApplicationContentManagersTabPane(this);
        j2eeApplicationContentManagersTabPane.setLayoutData(tabLayoutData);
        tabPane.add(j2eeApplicationContentManagersTabPane);
    }

    /**
    * Get the J2EE application current object
    * 
    * @return the J2EE <code>Application</code> object
    */
    public Application getApplication() {
        return this.application;
    }

    /**
    * Get parent pane
    * 
    * @return the parent <code>EnvironmentJ2EEApplicationsTabPane</code>
    */
    public EnvironmentJ2EEApplicationsTabPane getParentPane() {
        return this.parent;
    }

    /**
    * Return the application server name
    * 
    * @return the <code>ApplicationServer</code> name
    */
    public String getApplicationServerName() {
        return this.applicationServerName;
    }

    /**
    * Return the application name
    * 
    * @return the <code>Application</code> name
    */
    public String getApplicationName() {
        return this.applicationName;
    }

    /**
    * Update the whole window
    */
    public void update() {
        j2eeApplicationGeneralTabPane.update();
        j2eeApplicationArchivesTabPane.update();
        j2eeApplicationContentManagersTabPane.update();
        j2eeApplicationConfigurationFilesTabPane.update();
        j2eeApplicationDatabasesTabPane.update();
    }
}
