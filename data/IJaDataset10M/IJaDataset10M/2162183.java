package net.sourceforge.buildprocess.webautodeploy.client;

import net.sourceforge.buildprocess.autodeploy.model.ConnectionPool;
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
 * JDBC connection pool window <code>WindowPane</code>
 * 
 * @author <a href="mailto:jb@nanthrax.net">Jean-Baptiste Onofr�</a>
 */
public class JDBCConnectionPoolWindow extends WindowPane {

    private String connectionPoolName;

    private String applicationServerName;

    private ConnectionPool connectionPool = null;

    private EnvironmentJDBCConnectionPoolsTabPane parent;

    private JDBCConnectionPoolGeneralTabPane jdbcConnectionPoolGeneralTabPane;

    private JDBCConnectionPoolDriverTabPane jdbcConnectionPoolDriverTabPane;

    private JDBCConnectionPoolDatabaseTabPane jdbcConnectionPoolDatabaseTabPane;

    private JDBCConnectionPoolCapacityTabPane jdbcConnectionPoolCapacityTabPane;

    private ActionListener closeButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            JDBCConnectionPoolWindow.this.userClose();
        }
    };

    private ActionListener refreshButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            connectionPool = parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getConnectionPool(connectionPoolName);
            if (connectionPool == null) {
                connectionPool = new ConnectionPool();
            }
            update();
        }
    };

    private ActionListener deleteButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironmentWindow().getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getConnectionPools().remove(connectionPool);
            parent.getEnvironmentWindow().getChangeEvents().add("Delete JDBC connection pool (name [ " + connectionPool.getName() + " ]) from the J2EE application server " + applicationServerName);
            parent.getEnvironmentWindow().setUpdated(true);
            parent.getEnvironmentWindow().update();
            JDBCConnectionPoolWindow.this.userClose();
        }
    };

    private ActionListener applyButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            if (!parent.getEnvironmentWindow().getEnvironment().getLock().equals(WebAutoDeployApplication.getApplication().getUserid())) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("EnvironmentWindow.Error"), Messages.getString("EnvironmentWindow.Error.Locked")));
                return;
            }
            String jdbcConnectionPoolNameFieldValue = jdbcConnectionPoolGeneralTabPane.getJDBCConnectionPoolNameField().getText();
            int jdbcConnectionPoolActiveFieldIndex = jdbcConnectionPoolGeneralTabPane.getJDBCConnectionPoolActiveField().getSelectedIndex();
            int jdbcConnectionPoolBlockerFieldIndex = jdbcConnectionPoolGeneralTabPane.getJDBCConnectionPoolBlockerField().getSelectedIndex();
            int jdbcConnectionPoolDriverFieldIndex = jdbcConnectionPoolDriverTabPane.getJDBCConnectionPoolDriverField().getSelectedIndex();
            int jdbcConnectionPoolHelperFieldIndex = jdbcConnectionPoolDriverTabPane.getJDBCConnectionPoolHelperField().getSelectedIndex();
            String jdbcConnectionPoolClasspathFieldValue = jdbcConnectionPoolDriverTabPane.getJDBCConnectionPoolClasspathField().getText();
            String jdbcConnectionPoolUrlFieldValue = jdbcConnectionPoolDatabaseTabPane.getJDBCConnectionPoolUrlField().getText();
            String jdbcConnectionPoolUserFieldValue = jdbcConnectionPoolDatabaseTabPane.getJDBCConnectionPoolUserField().getText();
            String jdbcConnectionPoolPasswordFieldValue = jdbcConnectionPoolDatabaseTabPane.getJDBCConnectionPoolPasswordField().getText();
            String jdbcConnectionPoolConfirmPasswordFieldValue = jdbcConnectionPoolDatabaseTabPane.getJDBCConnectionPoolConfirmPasswordField().getText();
            String jdbcConnectionPoolInitialFieldValue = jdbcConnectionPoolCapacityTabPane.getJDBCConnectionPoolInitialField().getText();
            String jdbcConnectionPoolMaximalFieldValue = jdbcConnectionPoolCapacityTabPane.getJDBCConnectionPoolMaximalField().getText();
            String jdbcConnectionPoolIncrementFieldValue = jdbcConnectionPoolCapacityTabPane.getJDBCConnectionPoolIncrementField().getText();
            if (jdbcConnectionPoolActiveFieldIndex < 0 || jdbcConnectionPoolDriverFieldIndex < 0 || jdbcConnectionPoolHelperFieldIndex < 0) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("JDBCConnectionPoolWindow.Error"), Messages.getString("JDBCConnectionPoolWindow.Error.NotSelected")));
                return;
            }
            if (jdbcConnectionPoolNameFieldValue == null || jdbcConnectionPoolNameFieldValue.trim().length() < 1 || jdbcConnectionPoolUrlFieldValue == null || jdbcConnectionPoolUrlFieldValue.trim().length() < 1 || jdbcConnectionPoolUserFieldValue == null || jdbcConnectionPoolUserFieldValue.trim().length() < 1) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("JDBCConnectionPoolWindow.Error"), Messages.getString("JDBCConnectionPoolWindow.Error.Mandatory")));
                return;
            }
            int jdbcConnectionPoolInitialFieldNumber;
            int jdbcConnectionPoolMaximalFieldNumber;
            int jdbcConnectionPoolIncrementFieldNumber;
            try {
                jdbcConnectionPoolInitialFieldNumber = new Integer(jdbcConnectionPoolInitialFieldValue).intValue();
                jdbcConnectionPoolMaximalFieldNumber = new Integer(jdbcConnectionPoolMaximalFieldValue).intValue();
                jdbcConnectionPoolIncrementFieldNumber = new Integer(jdbcConnectionPoolIncrementFieldValue).intValue();
            } catch (Exception e) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("JDBCConnectionPoolWindow.Error"), Messages.getString("JDBCConnectionPoolWindow.Error.NotNumber")));
                return;
            }
            if (!jdbcConnectionPoolPasswordFieldValue.equals(jdbcConnectionPoolConfirmPasswordFieldValue)) {
                parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("JDBCConnectionPoolWindow.Error"), Messages.getString("JDBCConnectionPoolWindow.Error.PasswordMatching")));
                return;
            }
            if (connectionPoolName == null || (connectionPoolName != null && !connectionPoolName.equals(jdbcConnectionPoolNameFieldValue))) {
                if (parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getConnectionPool(jdbcConnectionPoolNameFieldValue) != null) {
                    parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("JDBCConnectionPoolWindow.Error"), Messages.getString("JDBCConnectionPoolWindow.Error.AlreadyExists")));
                    return;
                }
            }
            if (connectionPoolName != null) {
                parent.getEnvironmentWindow().getChangeEvents().add("Change JDBC connection pool (name [ " + connectionPool.getName() + " => " + jdbcConnectionPoolNameFieldValue + " ] | url [ " + connectionPool.getUrl() + " => " + jdbcConnectionPoolUrlFieldValue + " ] | user [ " + connectionPool.getUser() + " => " + jdbcConnectionPoolUserFieldValue + " ]) in the J2EE application server " + applicationServerName);
            }
            connectionPool.setName(jdbcConnectionPoolNameFieldValue);
            if (jdbcConnectionPoolActiveFieldIndex == 0) {
                connectionPool.setActive(true);
            } else {
                connectionPool.setActive(false);
            }
            if (jdbcConnectionPoolBlockerFieldIndex == 0) {
                connectionPool.setBlocker(true);
            } else {
                connectionPool.setBlocker(false);
            }
            if (jdbcConnectionPoolDriverFieldIndex == 0) {
                connectionPool.setDriver("oracle.jdbc.driver.OracleDriver");
            }
            if (jdbcConnectionPoolDriverFieldIndex == 1) {
                connectionPool.setDriver("oracle.jdbc.xa.client.OracleXADataSource");
            }
            if (jdbcConnectionPoolDriverFieldIndex == 2) {
                connectionPool.setDriver("com.ibm.db2.jcc.DB2Driver");
            }
            if (jdbcConnectionPoolDriverFieldIndex == 3) {
                connectionPool.setDriver("com.mysql.jdbc.Driver");
            }
            if (jdbcConnectionPoolDriverFieldIndex == 4) {
                connectionPool.setDriver("org.postgresql.Driver");
            }
            if (jdbcConnectionPoolHelperFieldIndex == 0) {
                connectionPool.setHelperclass(null);
            }
            if (jdbcConnectionPoolHelperFieldIndex == 1) {
                connectionPool.setHelperclass("com.ibm.websphere.rsadapter.GenericDataStoreHelper");
            }
            if (jdbcConnectionPoolHelperFieldIndex == 2) {
                connectionPool.setHelperclass("com.ibm.websphere.rsadapter.OracleDataStoreHelper");
            }
            connectionPool.setClasspath(jdbcConnectionPoolClasspathFieldValue);
            connectionPool.setUrl(jdbcConnectionPoolUrlFieldValue);
            connectionPool.setUser(jdbcConnectionPoolUserFieldValue);
            connectionPool.setPassword(jdbcConnectionPoolPasswordFieldValue);
            connectionPool.setInitial(jdbcConnectionPoolInitialFieldNumber);
            connectionPool.setMaximal(jdbcConnectionPoolMaximalFieldNumber);
            connectionPool.setIncrement(jdbcConnectionPoolIncrementFieldNumber);
            if (connectionPoolName == null) {
                try {
                    parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).addConnectionPool(connectionPool);
                    parent.getEnvironmentWindow().getChangeEvents().add("Add JDBC connection pool (name [ " + jdbcConnectionPoolNameFieldValue + " ] | url [ " + jdbcConnectionPoolUrlFieldValue + " ] | user [ " + jdbcConnectionPoolUserFieldValue + " ]) in the J2EE application server " + applicationServerName);
                } catch (Exception e) {
                    parent.getEnvironmentWindow().getContentPane().add(new ErrorWindow(Messages.getString("JDBCConnectionPoolWindow.Error"), Messages.getString("JDBCConnectionPoolWindow.Error.AlreadyExists")));
                    return;
                }
            }
            setTitle(Messages.getString("JDBCConnectionPoolWindow.Title") + " " + connectionPool.getName());
            setId("jdbcConnectionPoolWindow_" + parent.getEnvironmentWindow().getEnvironmentName() + "_" + applicationServerName + "_" + connectionPool.getName());
            connectionPoolName = connectionPool.getName();
            parent.getEnvironmentWindow().setUpdated(true);
            update();
            parent.getEnvironmentWindow().update();
        }
    };

    private ActionListener copyButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            try {
                WebAutoDeployApplication.getApplication().setCopyComponent(connectionPool.clone());
            } catch (Exception e) {
                return;
            }
        }
    };

    private ActionListener pasteButtonActionListener = new ActionListener() {

        public void actionPerformed(ActionEvent event) {
            Object copy = WebAutoDeployApplication.getApplication().getCopyComponent();
            if (copy == null || !(copy instanceof ConnectionPool)) {
                return;
            }
            connectionPool = (ConnectionPool) copy;
            connectionPoolName = null;
            parent.update();
            update();
        }
    };

    /**
    * Create a new <code>WindowPane</code>
    * 
    * @param parent
    *           the <code>EnvironmentJDBCConnectionPoolsTabPane</code>
    * @param j2eeApplicationServerName
    *           the original J2EE application server name
    * @param jdbcConnectionPoolName
    *           the original JDBC connection pool name
    */
    public JDBCConnectionPoolWindow(EnvironmentJDBCConnectionPoolsTabPane parent, String j2eeApplicationServerName, String jdbcConnectionPoolName) {
        super();
        this.parent = parent;
        this.connectionPoolName = jdbcConnectionPoolName;
        this.applicationServerName = j2eeApplicationServerName;
        this.connectionPool = parent.getEnvironmentWindow().getEnvironment().getApplicationServers().getApplicationServer(applicationServerName).getConnectionPool(jdbcConnectionPoolName);
        if (this.connectionPool == null) {
            this.connectionPool = new ConnectionPool();
        }
        if (connectionPoolName == null) {
            setTitle(Messages.getString("JDBCConnectionPoolWindow.Title"));
        } else {
            setTitle(Messages.getString("JDBCConnectionPoolWindow.Title") + " " + connectionPoolName);
        }
        setId("jdbcConnectionPoolWindow_" + parent.getEnvironmentWindow().getEnvironmentName() + "_" + applicationServerName + "_" + connectionPoolName);
        setStyleName("JDBCConnectionPoolWindow");
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
        tabLayoutData.setTitle(Messages.getString("JDBCConnectionPoolWindow.General"));
        jdbcConnectionPoolGeneralTabPane = new JDBCConnectionPoolGeneralTabPane(this);
        jdbcConnectionPoolGeneralTabPane.setLayoutData(tabLayoutData);
        tabPane.add(jdbcConnectionPoolGeneralTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("JDBCConnectionPoolWindow.Driver"));
        jdbcConnectionPoolDriverTabPane = new JDBCConnectionPoolDriverTabPane(this);
        jdbcConnectionPoolDriverTabPane.setLayoutData(tabLayoutData);
        tabPane.add(jdbcConnectionPoolDriverTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("JDBCConnectionPoolWindow.Database"));
        jdbcConnectionPoolDatabaseTabPane = new JDBCConnectionPoolDatabaseTabPane(this);
        jdbcConnectionPoolDatabaseTabPane.setLayoutData(tabLayoutData);
        tabPane.add(jdbcConnectionPoolDatabaseTabPane);
        tabLayoutData = new TabPaneLayoutData();
        tabLayoutData.setTitle(Messages.getString("JDBCConnectionPoolWindow.Capacity"));
        jdbcConnectionPoolCapacityTabPane = new JDBCConnectionPoolCapacityTabPane(this);
        jdbcConnectionPoolCapacityTabPane.setLayoutData(tabLayoutData);
        tabPane.add(jdbcConnectionPoolCapacityTabPane);
        update();
    }

    /**
    * Update the window
    */
    public void update() {
        jdbcConnectionPoolGeneralTabPane.update();
        jdbcConnectionPoolDriverTabPane.update();
        jdbcConnectionPoolDatabaseTabPane.update();
        jdbcConnectionPoolCapacityTabPane.update();
    }

    /**
    * Get the <code>ConnectionPool</code>
    * 
    * @return the <code>ConnectionPool</code>
    */
    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    /**
    * Get the <code>ConnectionPool</code> name
    * 
    * @return the <code>ConnectionPool</code> name
    */
    public String getConnectionPoolName() {
        return this.connectionPoolName;
    }
}
