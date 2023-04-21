package net.sourceforge.buildprocess.webautodeploy.client;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.sourceforge.buildprocess.autodeploy.model.AutoDeploy;
import net.sourceforge.buildprocess.autodeploy.model.Environment;
import net.sourceforge.buildprocess.autodeploy.model.log.Event;
import net.sourceforge.buildprocess.autodeploy.model.log.Journal;
import net.sourceforge.buildprocess.webautodeploy.configuration.WebAutoDeployConfigurationLoader;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SplitPane;
import nextapp.echo2.app.WindowPane;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.extras.app.TabPane;
import nextapp.echo2.extras.app.layout.TabPaneLayoutData;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Environment window <code>WindowPane</code>
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class EnvironmentWindow extends WindowPane {

    private static final Log log = LogFactory.getLog(EnvironmentWindow.class);

    private String environmentName;

    private Environment environment;

    private boolean readWriteAccess = false;

    private boolean updateOnlyAccess = false;

    private LinkedList changeEvents;

    private boolean updated = false;

    private Button lockButton;

    private EnvironmentsAccordionPane parent;

    private EnvironmentGeneralTabPane environmentGeneralTabPane;

    private EnvironmentSecurityTabPane environmentSecurityTabPane;

    private EnvironmentJ2EEApplicationServersTabPane environmentJ2EEApplicationServersTabPane;

    private EnvironmentJDBCConnectionPoolsTabPane environmentJDBCConnectionPoolsTabPane;

    private EnvironmentJDBCDataSourcesTabPane environmentJDBCDataSourcesTabPane;

    private EnvironmentJMSConnectionFactoriesTabPane environmentJMSConnectionFactoriesTabPane;

    private EnvironmentJMSServersTabPane environmentJMSServersTabPane;

    private EnvironmentJNDINameSpaceBindingsTabPane environmentJNDINameSpaceBindingsTabPane;

    private EnvironmentSharedLibrariesTabPane environmentSharedLibrariesTabPane;

    private EnvironmentJ2EEApplicationsTabPane environmentJ2EEApplicationsTabPane;

    private EnvironmentExternalSoftwaresTabPane environmentExternalSoftwaresTabPane;

    private EnvironmentJournalLogTabPane environmentJournalLogTabPane;

    private EnvironmentNotifiersTabPane environmentNotifiersTabPane;

    private EnvironmentPublishersTabPane environmentPublishersTabPane;

    private EnvironmentActionsTabPane environmentActionsTabPane;

    private EnvironmentControlerTabPane environmentControlerTabPane;

    private EnvironmentCheckerTabPane environmentCheckerTabPane;

    private ActionListener closeButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (environmentName == null || (!readWriteAccess && !updateOnlyAccess)) {
                EnvironmentWindow.this.getParent().remove(EnvironmentWindow.this);
                return;
            }
            if (getUpdated()) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.NotSave"), Messages.getString("EnvironmentWindow.Error.NotSave.Help")));
                return;
            }
            AutoDeploy autoDeploy = null;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            Environment current = autoDeploy.getEnvironment(environmentName);
            if (current == null) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.NotSave"), Messages.getString("EnvironmentWindow.Error.NotSave.Help")));
                return;
            }
            if (current.getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                current.setLock("");
                try {
                    autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
                } catch (Exception e) {
                    getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                    return;
                }
            }
            EnvironmentWindow.this.getParent().remove(EnvironmentWindow.this);
        }
    };

    private ActionListener toggleLockButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (getUpdated()) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.NotSave"), Messages.getString("EnvironmentWindow.Error.NotSave.Help")));
                return;
            }
            AutoDeploy autoDeploy;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            Environment current = autoDeploy.getEnvironment(environmentName);
            if (current == null) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.NotSave"), Messages.getString("EnvironmentWindow.Error.NotSave.Help")));
                return;
            }
            if (current.getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                current.setLock("");
                environment = current;
                EnvironmentWindow.this.setTitle(Messages.getString("EnvironmentWindow.Title") + " " + environmentName);
                try {
                    autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
                } catch (Exception e) {
                    getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                    return;
                }
                update();
                return;
            }
            if (current.getLock() == null || current.getLock().trim().length() < 1) {
                current.setLock(WebAutoDeployApplication.getApplication().getUserid());
                environment = current;
                EnvironmentWindow.this.setTitle(Messages.getString("EnvironmentWindow.Title") + " " + environmentName + " (" + Messages.getString("EnvironmentWindow.Title.Lock") + " " + environment.getLock() + ")");
                try {
                    autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
                } catch (Exception e) {
                    getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                    return;
                }
                update();
                return;
            }
        }
    };

    private ActionListener refreshButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            AutoDeploy autoDeploy = null;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            environment = autoDeploy.getEnvironment(environmentName);
            if (environment == null) {
                environment = new Environment();
                environment.setLock(WebAutoDeployApplication.getApplication().getUserid());
            }
            setUpdated(false);
            changeEvents = new LinkedList();
            update();
        }
    };

    private ActionListener saveButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (!environment.getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            String environmentNameFieldValue = environmentGeneralTabPane.getEnvironmentNameField().getText();
            String environmentGroupFieldValue = environmentGeneralTabPane.getEnvironmentGroupField().getText();
            String environmentAgentFieldValue = (String) environmentGeneralTabPane.getEnvironmentAgentField().getSelectedItem();
            int environmentAutoUpdateFieldIndex = environmentGeneralTabPane.getEnvironmentAutoUpdateField().getSelectedIndex();
            String environmentNotesAreaValue = environmentGeneralTabPane.getEnvironmentNotesArea().getText();
            String environmentWeblinksAreaValue = environmentGeneralTabPane.getEnvironmentWeblinksArea().getText();
            int j2eeApplicationServersTopologyFieldIndex = environmentJ2EEApplicationServersTabPane.getJ2EEApplicationServersTopologyField().getSelectedIndex();
            String notifierCountDownFieldValue = environmentNotifiersTabPane.getCountDownField().getText();
            if (environmentNameFieldValue == null || environmentNameFieldValue.trim().length() < 1 || environmentGroupFieldValue == null || environmentGroupFieldValue.trim().length() < 1 || environmentAgentFieldValue == null || environmentAgentFieldValue.trim().length() < 1 || notifierCountDownFieldValue == null || notifierCountDownFieldValue.trim().length() < 1) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Mandatory")));
                return;
            }
            int notifierCountDownInt;
            try {
                notifierCountDownInt = new Integer(notifierCountDownFieldValue).intValue();
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.NotifierCountDownNotInt")));
                return;
            }
            Journal journal = null;
            try {
                journal = WebAutoDeployConfigurationLoader.loadEnvironmentJournal(environmentName);
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.Journal"), e.getMessage()));
                return;
            }
            AutoDeploy autoDeploy = null;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            if (environmentName == null || (environmentName != null && !environmentName.equals(environmentNameFieldValue))) {
                if (autoDeploy.getEnvironment(environmentNameFieldValue) != null) {
                    getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.AlreadyExists")));
                    return;
                }
            }
            if (environmentName != null) {
                getChangeEvents().add("Change environment (name [ " + environment.getName() + " => " + environmentNameFieldValue + " ] | group [ " + environment.getGroup() + " ] | agent [ " + environment.getAgent() + " => " + environmentAgentFieldValue + " ])");
            }
            environment.setName(environmentNameFieldValue);
            environment.setGroup(environmentGroupFieldValue);
            environment.setAgent(environmentAgentFieldValue);
            if (environmentAutoUpdateFieldIndex == 0) {
                environment.setAutoupdate(true);
            } else {
                environment.setAutoupdate(false);
            }
            environment.setNotes(environmentNotesAreaValue);
            environment.setWeblinks(environmentWeblinksAreaValue);
            if (j2eeApplicationServersTopologyFieldIndex == 0) {
                environment.getApplicationServers().setCluster(false);
            } else {
                environment.getApplicationServers().setCluster(true);
            }
            environment.getNotifiers().setCountdown(notifierCountDownInt);
            Environment toupdate = autoDeploy.getEnvironment(environmentName);
            if (toupdate == null || environmentName == null) {
                try {
                    autoDeploy.addEnvironment(environment);
                } catch (Exception e) {
                    getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.AlreadyExists")));
                    return;
                }
            } else {
                int index = autoDeploy.getEnvironments().indexOf(toupdate);
                autoDeploy.getEnvironments().set(index, environment);
            }
            try {
                autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                return;
            }
            if (environment.getLock() == null || environment.getLock().trim().length() < 1) {
                setTitle(Messages.getString("EnvironmentWindow.Title") + " " + environment.getName());
            } else {
                setTitle(Messages.getString("EnvironmentWindow.Title") + " " + environment.getName() + " (" + Messages.getString("EnvironmentWindow.Title.Lock") + " " + environment.getLock() + ")");
            }
            setId("environmentWindow_" + environment.getName());
            environmentName = environment.getName();
            for (Iterator eventIterator = getChangeEvents().iterator(); eventIterator.hasNext(); ) {
                String eventMessage = (String) eventIterator.next();
                Event journalEvent = new Event();
                journalEvent.setDate(((FastDateFormat) DateFormatUtils.ISO_DATETIME_FORMAT).format(new Date()));
                journalEvent.setSeverity("INFO");
                journalEvent.setAuthor(WebAutoDeployApplication.getApplication().getUserid());
                journalEvent.setContent(eventMessage);
                journal.addEvent(journalEvent);
            }
            try {
                journal.writeXMLFile(WebAutoDeployConfigurationLoader.getEnvironmentJournalLogFile(environmentName));
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.Journal"), e.getMessage()));
                return;
            }
            setUpdated(false);
            changeEvents = new LinkedList();
            parent.update();
            update();
        }
    };

    private ActionListener copyButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            try {
                WebAutoDeployApplication.getApplication().setCopyComponent(environment.clone());
            } catch (Exception e) {
                return;
            }
        }
    };

    private ActionListener pasteButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            Object copy = WebAutoDeployApplication.getApplication().getCopyComponent();
            if (copy == null || !(copy instanceof Environment)) {
                return;
            }
            environment = (Environment) copy;
            environment.setLock(WebAutoDeployApplication.getApplication().getUserid());
            environmentName = null;
            update();
        }
    };

    private ActionListener deleteButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (!environment.getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            AutoDeploy autoDeploy = null;
            try {
                autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
                return;
            }
            Environment delete = autoDeploy.getEnvironment(environmentName);
            autoDeploy.getEnvironments().remove(delete);
            try {
                autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
            } catch (Exception e) {
                getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                return;
            }
            parent.update();
            EnvironmentWindow.this.getParent().remove(EnvironmentWindow.this);
        }
    };

    /**
    * Create a new <code>EnvironmentWindow</code>
    * 
    * @param environment
    *           the environment to consider
    * @param environmentName
    *           the environment name
    */
    public EnvironmentWindow(EnvironmentsAccordionPane parent, String environmentName) {
        super();
        this.parent = parent;
        this.environmentName = environmentName;
        this.changeEvents = new LinkedList();
        this.updated = false;
        AutoDeploy autoDeploy = null;
        try {
            autoDeploy = WebAutoDeployConfigurationLoader.loadAutoDeploy();
        } catch (Exception e) {
            parent.getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Parsing"), e.getMessage()));
            return;
        }
        this.environment = autoDeploy.getEnvironment(environmentName);
        if (this.environment == null) {
            this.environment = new Environment();
            this.environment.setLock(WebAutoDeployApplication.getApplication().getUserid());
        }
        if (environmentName != null) {
            if (!autoDeploy.getSecurity().checkEnvironmentUserAccess(this.environment, WebAutoDeployApplication.getApplication().getUserid(), "ro") && !autoDeploy.getSecurity().checkEnvironmentUserAccess(this.environment, WebAutoDeployApplication.getApplication().getUserid(), "rw") && !autoDeploy.getSecurity().checkEnvironmentUserAccess(this.environment, WebAutoDeployApplication.getApplication().getUserid(), "uo")) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error.AccessDenied"), Messages.getString("EnvironmentWindow.Error.AccessMember")));
                return;
            }
            if (autoDeploy.getSecurity().checkEnvironmentUserAccess(this.environment, WebAutoDeployApplication.getApplication().getUserid(), "rw")) {
                this.readWriteAccess = true;
            }
            if (autoDeploy.getSecurity().checkEnvironmentUserAccess(this.environment, WebAutoDeployApplication.getApplication().getUserid(), "uo")) {
                this.updateOnlyAccess = true;
            }
        } else {
            this.readWriteAccess = true;
        }
        if ((this.readWriteAccess || this.updateOnlyAccess) && (this.environment.getLock() == null || this.environment.getLock().trim().length() < 1)) {
            this.environment.setLock(WebAutoDeployApplication.getApplication().getUserid());
            if (environmentName != null) {
                try {
                    autoDeploy.writeXMLFile(WebAutoDeployConfigurationLoader.getAutoDeployConfigurationFile());
                } catch (Exception e) {
                    parent.getContentPane().add(new ErrorWindow(Messages.getString("ErrorWindow.AutoDeployError.Writing"), e.getMessage()));
                    return;
                }
            }
        }
        if (environmentName == null) {
            setTitle(Messages.getString("EnvironmentWindow.Title"));
        } else {
            if (environment.getLock() == null || environment.getLock().trim().length() < 1) {
                setTitle(Messages.getString("EnvironmentWindow.Title") + " " + environmentName);
            } else {
                setTitle(Messages.getString("EnvironmentWindow.Title") + " " + environmentName + " (" + Messages.getString("EnvironmentWindow.Title.Lock") + " " + this.environment.getLock() + ")");
            }
        }
        setId("environmentWindow_" + environmentName);
        setStyleName("EnvironmentWindow");
        setModal(false);
        setDefaultCloseOperation(WindowPane.DO_NOTHING_ON_CLOSE);
        SplitPane splitPane = new SplitPane(SplitPane.ORIENTATION_VERTICAL_BOTTOM_TOP, new Extent(32));
        this.add(splitPane);
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
        if (this.readWriteAccess) {
            Button pasteButton = new Button(Messages.getString("Window.Paste"), Styles.ICON_24_PASTE);
            pasteButton.setStyleName("ControlPane.Button");
            pasteButton.addActionListener(pasteButtonActionListener);
            controlRow.add(pasteButton);
            Button saveButton = new Button(Messages.getString("Window.Save"), Styles.ICON_24_YES);
            saveButton.setStyleName("ControlPane.Button");
            saveButton.addActionListener(saveButtonActionListener);
            controlRow.add(saveButton);
            Button deleteButton = new Button(Messages.getString("Window.Delete"), Styles.ICON_24_NO);
            deleteButton.setStyleName("ControlPane.Button");
            deleteButton.addActionListener(deleteButtonActionListener);
            controlRow.add(deleteButton);
        }
        if (this.readWriteAccess || this.updateOnlyAccess) {
            lockButton = new Button(Styles.ICON_24_LOCK);
            lockButton.addActionListener(toggleLockButtonActionListener);
            updateLockButton();
            lockButton.setStyleName("ControlPane.Button");
            controlRow.add(lockButton);
        }
        Button closeButton = new Button(Messages.getString("Window.Close"), Styles.ICON_24_EXIT);
        closeButton.setStyleName("ControlPane.Button");
        closeButton.addActionListener(closeButtonActionListener);
        controlRow.add(closeButton);
        TabPane tabPane = new TabPane();
        tabPane.setStyleName("EnvironmentWindow.TabPane");
        splitPane.add(tabPane);
        TabPaneLayoutData tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.General"));
        environmentGeneralTabPane = new EnvironmentGeneralTabPane(this);
        environmentGeneralTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentGeneralTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Security"));
        environmentSecurityTabPane = new EnvironmentSecurityTabPane(this);
        environmentSecurityTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentSecurityTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.J2EEApplicationServers"));
        environmentJ2EEApplicationServersTabPane = new EnvironmentJ2EEApplicationServersTabPane(this);
        environmentJ2EEApplicationServersTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentJ2EEApplicationServersTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.J2EEResources"));
        TabPane ressourcePane = new TabPane();
        ressourcePane.setStyleName("EnvironmentWindow.TabPane");
        ressourcePane.setLayoutData(tabLayoutData);
        tabPane.add(ressourcePane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.JDBCConnectionPools"));
        environmentJDBCConnectionPoolsTabPane = new EnvironmentJDBCConnectionPoolsTabPane(this);
        environmentJDBCConnectionPoolsTabPane.setLayoutData(tabLayoutData);
        ressourcePane.add(environmentJDBCConnectionPoolsTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.JDBCDataSources"));
        environmentJDBCDataSourcesTabPane = new EnvironmentJDBCDataSourcesTabPane(this);
        environmentJDBCDataSourcesTabPane.setLayoutData(tabLayoutData);
        ressourcePane.add(environmentJDBCDataSourcesTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.JMSConnectionFactories"));
        environmentJMSConnectionFactoriesTabPane = new EnvironmentJMSConnectionFactoriesTabPane(this);
        environmentJMSConnectionFactoriesTabPane.setLayoutData(tabLayoutData);
        ressourcePane.add(environmentJMSConnectionFactoriesTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.JMSServers"));
        environmentJMSServersTabPane = new EnvironmentJMSServersTabPane(this);
        environmentJMSServersTabPane.setLayoutData(tabLayoutData);
        ressourcePane.add(environmentJMSServersTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.JNDINameSpaceBindings"));
        environmentJNDINameSpaceBindingsTabPane = new EnvironmentJNDINameSpaceBindingsTabPane(this);
        environmentJNDINameSpaceBindingsTabPane.setLayoutData(tabLayoutData);
        ressourcePane.add(environmentJNDINameSpaceBindingsTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.SharedLibraries"));
        environmentSharedLibrariesTabPane = new EnvironmentSharedLibrariesTabPane(this);
        environmentSharedLibrariesTabPane.setLayoutData(tabLayoutData);
        ressourcePane.add(environmentSharedLibrariesTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.J2EEApplications"));
        environmentJ2EEApplicationsTabPane = new EnvironmentJ2EEApplicationsTabPane(this);
        environmentJ2EEApplicationsTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentJ2EEApplicationsTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.ExternalSoftwares"));
        environmentExternalSoftwaresTabPane = new EnvironmentExternalSoftwaresTabPane(this);
        environmentExternalSoftwaresTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentExternalSoftwaresTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.JournalLog"));
        environmentJournalLogTabPane = new EnvironmentJournalLogTabPane(this);
        environmentJournalLogTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentJournalLogTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Notifiers"));
        environmentNotifiersTabPane = new EnvironmentNotifiersTabPane(this);
        environmentNotifiersTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentNotifiersTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Publishers"));
        environmentPublishersTabPane = new EnvironmentPublishersTabPane(this);
        environmentPublishersTabPane.setLayoutData(tabLayoutData);
        tabPane.add(environmentPublishersTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Actions"));
        TabPane actionPane = new TabPane();
        actionPane.setStyleName("EnvironmentWindow.TabPane");
        actionPane.setLayoutData(tabLayoutData);
        tabPane.add(actionPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Updater"));
        environmentActionsTabPane = new EnvironmentActionsTabPane(this);
        environmentActionsTabPane.setLayoutData(tabLayoutData);
        actionPane.add(environmentActionsTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Controller"));
        environmentControlerTabPane = new EnvironmentControlerTabPane(this);
        environmentControlerTabPane.setLayoutData(tabLayoutData);
        actionPane.add(environmentControlerTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("EnvironmentWindow.Tab.Checker"));
        environmentCheckerTabPane = new EnvironmentCheckerTabPane(this);
        environmentCheckerTabPane.setLayoutData(tabLayoutData);
        actionPane.add(environmentCheckerTabPane);
    }

    /**
    * Get the environment linked with the window
    * 
    * @return the <code>Environment</code> linked with the window
    */
    public Environment getEnvironment() {
        return this.environment;
    }

    /**
    * Get the environment name
    * 
    * @return the current <code>Environment</code> name
    */
    public String getEnvironmentName() {
        return this.environmentName;
    }

    /**
    * Get the parent content pane
    * 
    * @return the parent <code>ContentPane</code>
    */
    public ContentPane getContentPane() {
        return parent.getContentPane();
    }

    /**
    * Get the change events list
    * 
    * @return the change events list
    */
    public List getChangeEvents() {
        return this.changeEvents;
    }

    /**
    * Get the updated flag
    * 
    * @return the updated flag
    */
    public boolean getUpdated() {
        return this.updated;
    }

    /**
    * Set the updated flag
    * 
    * @param the
    *           new updated flag value
    */
    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    /**
    * Get the read write access flag
    * 
    * @return the read write access flag
    */
    public boolean getReadWriteAccess() {
        return this.readWriteAccess;
    }

    /**
    * Get the update only access flag
    * 
    * @return the update only access flag
    */
    public boolean getUpdateOnlyAccess() {
        return this.updateOnlyAccess;
    }

    /**
    * Update the lock button (display lock or unlock depending of the state)
    */
    public void updateLockButton() {
        if (environmentName != null && environment.getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
            lockButton.setText(Messages.getString("EnvironmentWindow.UnlockButton"));
        } else {
            lockButton.setText(Messages.getString("EnvironmentWindow.LockButton"));
        }
    }

    /**
    * Update the complete <code>EnvironmentWindow</code> with all children tab
    */
    public void update() {
        environmentGeneralTabPane.update();
        environmentSecurityTabPane.update();
        environmentJ2EEApplicationServersTabPane.update();
        environmentJDBCConnectionPoolsTabPane.update();
        environmentJDBCDataSourcesTabPane.update();
        environmentJMSConnectionFactoriesTabPane.update();
        environmentJMSServersTabPane.update();
        environmentJNDINameSpaceBindingsTabPane.update();
        environmentSharedLibrariesTabPane.update();
        environmentJ2EEApplicationsTabPane.update();
        environmentCheckerTabPane.update();
        environmentExternalSoftwaresTabPane.update();
        environmentJournalLogTabPane.update();
        environmentNotifiersTabPane.update();
        environmentPublishersTabPane.update();
        environmentActionsTabPane.update();
        environmentControlerTabPane.update();
        this.updateLockButton();
    }

    /**
    * Only update the <code>EnvironmentJournalLogTabPane</code>
    */
    public void updateJournalLogTabPane() {
        environmentJournalLogTabPane.update();
    }

    /**
    * Return the parent accordion pane
    */
    public EnvironmentsAccordionPane getAccordionPane() {
        return parent;
    }
}
