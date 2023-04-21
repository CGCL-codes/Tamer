package net.sourceforge.buildprocess.webautodeploy.client;

import java.util.Iterator;
import net.sourceforge.buildprocess.autodeploy.model.ApplicationServer;
import net.sourceforge.buildprocess.autodeploy.model.JMSConnectionFactory;
import nextapp.echo2.app.Button;
import nextapp.echo2.app.Column;
import nextapp.echo2.app.ContentPane;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import nextapp.echo2.app.list.DefaultListModel;

/**
 * Environment JMS connection factories tab <code>ContentPane</code>
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class EnvironmentJMSConnectionFactoriesTabPane extends ContentPane {

    /**
    * 
    */
    private static final long serialVersionUID = -4402395850972367884L;

    private EnvironmentWindow parent;

    private SelectField scopeSelectField;

    private Label jmsConnectionFactoriesHeader;

    private Grid jmsConnectionFactoriesGrid;

    private boolean newJMSConnectionFactoryActive = true;

    private boolean newJMSConnectionFactoryBlocker = false;

    private TextField newJMSConnectionFactoryNameField;

    private ActionListener scopeSelectFieldActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 5997242849050279798L;

        public void actionPerformed(ActionEvent event) {
            update();
        }
    };

    private ActionListener editButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = -8237890917119143988L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            String jmsConnectionFactoryName = event.getActionCommand();
            TextField jmsConnectionFactoryNameField = (TextField) EnvironmentJMSConnectionFactoriesTabPane.this.getComponent("jmsConnectionFactoryNameField_" + parent.getEnvironmentName() + "_" + (String) scopeSelectField.getSelectedItem() + "_" + jmsConnectionFactoryName);
            String jmsConnectionFactoryNameFieldValue = jmsConnectionFactoryNameField.getText();
            if (jmsConnectionFactoryNameFieldValue == null || jmsConnectionFactoryNameFieldValue.trim().length() < 1) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.Mandatory")));
                return;
            }
            if (!jmsConnectionFactoryName.equals(jmsConnectionFactoryNameFieldValue)) {
                if (parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactory(jmsConnectionFactoryNameFieldValue) != null) {
                    parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.AlreadyExists")));
                    return;
                }
            }
            JMSConnectionFactory jmsConnectionFactory = parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactory(jmsConnectionFactoryName);
            if (jmsConnectionFactory == null) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.NotFound")));
                return;
            }
            parent.getChangeEvents().add("Change JMS connection factory (name [ " + jmsConnectionFactory.getName() + " => " + jmsConnectionFactoryNameFieldValue + " ]) in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            jmsConnectionFactory.setName(jmsConnectionFactoryNameFieldValue);
            parent.setUpdated(true);
            parent.updateJournalLogTabPane();
            update();
        }
    };

    private ActionListener createButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = -3907388876592786103L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            String newJMSConnectionFactoryNameFieldValue = newJMSConnectionFactoryNameField.getText();
            if (newJMSConnectionFactoryNameFieldValue == null || newJMSConnectionFactoryNameFieldValue.trim().length() < 1) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.Mandatory")));
                return;
            }
            JMSConnectionFactory jmsConnectionFactory = new JMSConnectionFactory();
            jmsConnectionFactory.setName(newJMSConnectionFactoryNameFieldValue);
            jmsConnectionFactory.setActive(newJMSConnectionFactoryActive);
            try {
                parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).addJMSConnectionFactory(jmsConnectionFactory);
            } catch (Exception e) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.AlreadyExists")));
                return;
            }
            parent.getChangeEvents().add("Create JMS connection factory (name [ " + jmsConnectionFactory.getName() + " ]) in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            parent.setUpdated(true);
            parent.updateJournalLogTabPane();
            update();
        }
    };

    private ActionListener deleteButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 4938133733803558210L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            String jmsConnectionFactoryName = event.getActionCommand();
            JMSConnectionFactory jmsConnectionFactory = parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactory(jmsConnectionFactoryName);
            if (jmsConnectionFactory == null) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.NotFound")));
                return;
            }
            parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactories().remove(jmsConnectionFactory);
            parent.getChangeEvents().add("Delete JMS connection factory (name [ " + jmsConnectionFactory.getName() + " ]) in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            parent.setUpdated(true);
            parent.updateJournalLogTabPane();
            update();
        }
    };

    private ActionListener toggleActiveButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = -7756657087511742452L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            JMSConnectionFactory jmsConnectionFactory = parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactory(event.getActionCommand());
            if (jmsConnectionFactory == null) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.NotFound")));
                return;
            }
            if (jmsConnectionFactory.getActive()) {
                jmsConnectionFactory.setActive(false);
                parent.getChangeEvents().add("Inactive JMS connection factory (name [ " + jmsConnectionFactory.getName() + " ] in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            } else {
                jmsConnectionFactory.setActive(true);
                parent.getChangeEvents().add("Active JMS connection factory (name [ " + jmsConnectionFactory.getName() + " ] in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            }
            parent.setUpdated(true);
            parent.updateJournalLogTabPane();
            update();
        }
    };

    private ActionListener toggleBlockerButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = 1044574595915789379L;

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            JMSConnectionFactory jmsConnectionFactory = parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactory(event.getActionCommand());
            if (jmsConnectionFactory == null) {
                parent.getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error"), Messages.getString("EnvironmentWindow.TabPane.JMSCF.Error.NotFound")));
                return;
            }
            if (jmsConnectionFactory.getBlocker()) {
                jmsConnectionFactory.setBlocker(false);
                parent.getChangeEvents().add("Not update blocker JMS connection factory (name [ " + jmsConnectionFactory.getName() + " ] in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            } else {
                jmsConnectionFactory.setBlocker(true);
                parent.getChangeEvents().add("Update blocker JMS connection factory (name [ " + jmsConnectionFactory.getName() + " ] in the J2EE application server " + (String) scopeSelectField.getSelectedItem());
            }
            parent.setUpdated(true);
            parent.updateJournalLogTabPane();
            update();
        }
    };

    private ActionListener newToggleActiveButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 1044574595915789379L;

        public void actionPerformed(ActionEvent event) {
            if (newJMSConnectionFactoryActive) {
                newJMSConnectionFactoryActive = false;
            } else {
                newJMSConnectionFactoryActive = true;
            }
            update();
        }
    };

    private ActionListener newToggleBlockerButtonActionListener = new ActionListener() {

        /**
       * Generated Serial Version UID
       */
        private static final long serialVersionUID = 3224212644204469748L;

        public void actionPerformed(ActionEvent event) {
            if (newJMSConnectionFactoryBlocker) {
                newJMSConnectionFactoryBlocker = false;
            } else {
                newJMSConnectionFactoryBlocker = true;
            }
            update();
        }
    };

    private ActionListener copyButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = -3833785372386382157L;

        public void actionPerformed(ActionEvent event) {
            JMSConnectionFactory jmsConnectionFactory = parent.getEnvironment().getApplicationServers().getApplicationServer((String) scopeSelectField.getSelectedItem()).getJMSConnectionFactory(event.getActionCommand());
            if (jmsConnectionFactory == null) {
                return;
            }
            try {
                WebAutoDeployApplication.getApplication().setCopyComponent(jmsConnectionFactory.clone());
            } catch (Exception e) {
                return;
            }
        }
    };

    private ActionListener pasteButtonActionListener = new ActionListener() {

        /**
       * 
       */
        private static final long serialVersionUID = 3224212644204469748L;

        public void actionPerformed(ActionEvent event) {
            Object copy = WebAutoDeployApplication.getApplication().getCopyComponent();
            if (copy == null || !(copy instanceof JMSConnectionFactory)) {
                return;
            }
            newJMSConnectionFactoryNameField.setText(((JMSConnectionFactory) copy).getName());
        }
    };

    /**
    * Create a new <code>EnvironmentJMSConnectionFactoriesTabPane</code>
    * 
    * @param parent
    *           the parent <code>EnvironmentWindow</code>
    */
    public EnvironmentJMSConnectionFactoriesTabPane(EnvironmentWindow parent) {
        super();
        setStyleName("EnvironmentWindow.TabPane");
        this.parent = parent;
        Column columnLayout = new Column();
        columnLayout.setStyleName("EnvironmentWindow.TabPane.ColumnLayout");
        add(columnLayout);
        Label scopeHeader = new Label(Messages.getString("EnvironmentWindow.Tab.Scope"));
        scopeHeader.setStyleName("ColumnHeader");
        columnLayout.add(scopeHeader);
        Grid layoutGrid = new Grid(2);
        layoutGrid.setStyleName("EnvironmentWindow.TabPane.GridLayout");
        columnLayout.add(layoutGrid);
        Label applicationServerScope = new Label(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Scope"));
        applicationServerScope.setStyleName("Default");
        layoutGrid.add(applicationServerScope);
        scopeSelectField = new SelectField();
        scopeSelectField.addActionListener(scopeSelectFieldActionListener);
        scopeSelectField.setStyleName("Default");
        layoutGrid.add(scopeSelectField);
        DefaultListModel scopeListModel = (DefaultListModel) scopeSelectField.getModel();
        scopeListModel.removeAll();
        for (Iterator applicationServerIterator = parent.getEnvironment().getApplicationServers().getApplicationServers().iterator(); applicationServerIterator.hasNext(); ) {
            ApplicationServer applicationServer = (ApplicationServer) applicationServerIterator.next();
            scopeListModel.add(applicationServer.getName());
        }
        if (scopeListModel.size() > 0) {
            scopeSelectField.setSelectedIndex(0);
        }
        jmsConnectionFactoriesHeader = new Label(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Header"));
        jmsConnectionFactoriesHeader.setStyleName("ColumnHeader");
        columnLayout.add(jmsConnectionFactoriesHeader);
        jmsConnectionFactoriesGrid = new Grid(4);
        jmsConnectionFactoriesGrid.setStyleName("EnvironmentWindow.TabPane.BorderGrid");
        columnLayout.add(jmsConnectionFactoriesGrid);
        update();
    }

    /**
    * Update the pane
    */
    public void update() {
        String applicationServerName = (String) scopeSelectField.getSelectedItem();
        DefaultListModel scopeListModel = (DefaultListModel) scopeSelectField.getModel();
        scopeListModel.removeAll();
        int index = 0;
        int found = -1;
        for (Iterator applicationServerIterator = parent.getEnvironment().getApplicationServers().getApplicationServers().iterator(); applicationServerIterator.hasNext(); ) {
            ApplicationServer applicationServer = (ApplicationServer) applicationServerIterator.next();
            scopeListModel.add(applicationServer.getName());
            if (applicationServer.getName().equals(applicationServerName)) {
                found = index;
            }
            index++;
        }
        jmsConnectionFactoriesGrid.removeAll();
        if (scopeListModel.size() < 1) {
            return;
        }
        if (found == -1) {
            scopeSelectField.setSelectedIndex(0);
        } else {
            scopeSelectField.setSelectedIndex(found);
        }
        applicationServerName = (String) scopeSelectField.getSelectedItem();
        Label jmsConnectionFactoryActive = new Label(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Active"));
        jmsConnectionFactoryActive.setStyleName("Header");
        jmsConnectionFactoryActive.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Active.Help"));
        jmsConnectionFactoriesGrid.add(jmsConnectionFactoryActive);
        Label jmsConnectionFactoryBlocker = new Label(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Blocker"));
        jmsConnectionFactoryBlocker.setStyleName("Header");
        jmsConnectionFactoryBlocker.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Blocker.Help"));
        jmsConnectionFactoriesGrid.add(jmsConnectionFactoryBlocker);
        Label jmsConnectionFactoryName = new Label(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Name"));
        jmsConnectionFactoryName.setStyleName("Header");
        jmsConnectionFactoriesGrid.add(jmsConnectionFactoryName);
        Label blankHeader = new Label(" ");
        blankHeader.setStyleName("Header");
        jmsConnectionFactoriesGrid.add(blankHeader);
        jmsConnectionFactoriesHeader.setText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.Header") + " " + applicationServerName);
        for (Iterator jmsConnectionFactoryIterator = parent.getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getJMSConnectionFactories().iterator(); jmsConnectionFactoryIterator.hasNext(); ) {
            JMSConnectionFactory jmsConnectionFactory = (JMSConnectionFactory) jmsConnectionFactoryIterator.next();
            Button activeButton;
            if (jmsConnectionFactory.getActive()) {
                activeButton = new Button(Styles.ICON_24_ACTIVE);
                activeButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.ActiveButton.Help"));
            } else {
                activeButton = new Button(Styles.ICON_24_INACTIVE);
                activeButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.InactiveButton.Help"));
            }
            if (parent.getReadWriteAccess()) {
                activeButton.setActionCommand(jmsConnectionFactory.getName());
                activeButton.addActionListener(toggleActiveButtonActionListener);
            }
            activeButton.setStyleName("Default");
            jmsConnectionFactoriesGrid.add(activeButton);
            Button blockerButton;
            if (jmsConnectionFactory.getBlocker()) {
                blockerButton = new Button(Styles.ICON_24_INACTIVE);
                blockerButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.BlockerButton.Help"));
            } else {
                blockerButton = new Button(Styles.ICON_24_ACTIVE);
                blockerButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.NotBlockerButton.Help"));
            }
            blockerButton.setStyleName("Default");
            blockerButton.addActionListener(toggleBlockerButtonActionListener);
            jmsConnectionFactoriesGrid.add(blockerButton);
            TextField connectionFactoryNameField = new TextField();
            connectionFactoryNameField.setId("jmsConnectionFactoryNameField_" + parent.getEnvironmentName() + "_" + applicationServerName + "_" + jmsConnectionFactory.getName());
            connectionFactoryNameField.setStyleName("Default");
            connectionFactoryNameField.setText(jmsConnectionFactory.getName());
            jmsConnectionFactoriesGrid.add(connectionFactoryNameField);
            Row actionRow = new Row();
            jmsConnectionFactoriesGrid.add(actionRow);
            Button copyButton = new Button(Styles.ICON_24_COPY);
            copyButton.setStyleName("ControlPane.Button");
            copyButton.setToolTipText(Messages.getString("Window.Copy"));
            copyButton.setActionCommand(jmsConnectionFactory.getName());
            copyButton.addActionListener(copyButtonActionListener);
            actionRow.add(copyButton);
            if (parent.getReadWriteAccess()) {
                Button deleteButton = new Button(Styles.ICON_24_NO);
                deleteButton.setStyleName("ControlPane.Button");
                deleteButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.DeleteButton.Help"));
                deleteButton.setActionCommand(jmsConnectionFactory.getName());
                deleteButton.addActionListener(deleteButtonActionListener);
                actionRow.add(deleteButton);
                Button editButton = new Button(Styles.ICON_24_YES);
                editButton.setStyleName("ControlPane.Button");
                editButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.EditButton.Help"));
                editButton.setActionCommand(jmsConnectionFactory.getName());
                editButton.addActionListener(editButtonActionListener);
                actionRow.add(editButton);
            }
        }
        if (parent.getReadWriteAccess()) {
            Button activeButton;
            if (newJMSConnectionFactoryActive) {
                activeButton = new Button(Styles.ICON_24_ACTIVE);
                activeButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.ActiveButton.Help"));
            } else {
                activeButton = new Button(Styles.ICON_24_INACTIVE);
                activeButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.InactiveButton.Help"));
            }
            activeButton.setStyleName("Default");
            activeButton.addActionListener(newToggleActiveButtonActionListener);
            jmsConnectionFactoriesGrid.add(activeButton);
            Button blockerButton;
            if (newJMSConnectionFactoryBlocker) {
                blockerButton = new Button(Styles.ICON_24_INACTIVE);
                blockerButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.BlockerButton.Help"));
            } else {
                blockerButton = new Button(Styles.ICON_24_ACTIVE);
                blockerButton.setToolTipText(Messages.getString("EnvironmentWindow.TabPane.JMSCF.NotBlockerButton.Help"));
            }
            blockerButton.setStyleName("Default");
            blockerButton.addActionListener(newToggleBlockerButtonActionListener);
            jmsConnectionFactoriesGrid.add(blockerButton);
            newJMSConnectionFactoryNameField = new TextField();
            newJMSConnectionFactoryNameField.setStyleName("Default");
            jmsConnectionFactoriesGrid.add(newJMSConnectionFactoryNameField);
            Row actionRow = new Row();
            jmsConnectionFactoriesGrid.add(actionRow);
            Button pasteButton = new Button(Styles.ICON_24_PASTE);
            pasteButton.setStyleName("ControlPane.Button");
            pasteButton.setToolTipText(Messages.getString("Window.Paste"));
            pasteButton.addActionListener(pasteButtonActionListener);
            actionRow.add(pasteButton);
            Button createButton = new Button(Styles.ICON_24_CREATE);
            createButton.setStyleName("ControlPane.Button");
            createButton.addActionListener(createButtonActionListener);
            actionRow.add(createButton);
        }
    }
}
