package proper.gui.core.panel;

import proper.database.ColumnLister;
import proper.database.Connector;
import proper.database.DatabaseLister;
import proper.database.TableLister;
import proper.gui.core.event.ConnectorChangeEvent;
import proper.gui.core.event.ConnectorChangeListener;
import proper.gui.core.event.SizeChangeEvent;
import proper.gui.core.event.SizeChangeListener;
import proper.gui.core.io.ImageLoader;
import proper.util.ProperVector;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
* This Panel represents an easy way to connect to a database.<br>
* It supports PropertyChangeListeners for the names <code>database</code>
* and <code>table</code>, if other components should react to changes.<br>
* The two buttons "Connect" and "Select" also fire Property Changes, where
* the <code>propertyName</code> of the Event contains the action command of
* the button.
*
*
* @see         #ACTION_CONNECT
* @see         #ACTION_SELECTDATABASE
* @see         #ACTION_SELECTTABLE
* @author      FracPete
* @version $Revision: 1.4 $
*/
public class DatabasePanel extends ProperPanel {

    /** the action of the Connect-Button */
    public static final String ACTION_CONNECT = "connect";

    /** the action of the Select-Button (Database) */
    public static final String ACTION_SELECTDATABASE = "select_database";

    /** the action of the Select-Button (Table) */
    public static final String ACTION_SELECTTABLE = "select_table";

    /** the database property */
    public static final String PROPERTY_DATABASE = "database";

    /** the table property */
    public static final String PROPERTY_TABLE = "table";

    /** the column property */
    public static final String PROPERTY_COLUMN = "column";

    private JButton buttonConnect;

    private JButton buttonSelectDatabase;

    private JButton buttonSelectTable;

    private JLabel labelDriver;

    private JLabel labelUrl;

    private JLabel labelUser;

    private JLabel labelPassword;

    private JLabel labelDatabases;

    private JLabel labelTables;

    private JLabel labelExcludes;

    private JLabel labelColumns;

    private JLabel labelInitialDatabase;

    private JTextField textDriver;

    private JTextField textUrl;

    private JTextField textUser;

    private JTextField textPassword;

    private JTextField textExcludes;

    private JTextField textInitialDatabase;

    private JComboBox comboDatabases;

    private JComboBox comboTables;

    private JComboBox comboColumns;

    private JPanel panelDriver;

    private JPanel panelConnect;

    private JPanel panelDatabases;

    private JPanel panelTables;

    private JPanel panelColumns;

    private String driver;

    private String url;

    private String user;

    private String password;

    private String excludes;

    private Connector conn;

    private boolean showDriver;

    private boolean showConnect;

    private boolean showTables;

    private boolean showColumns;

    private String database;

    private String table;

    private String column;

    private String preferredDatabase;

    private String preferredTable;

    private String preferredColumn;

    private String initialDatabase;

    private HashSet sizeListeners;

    private HashSet connectorListeners;

    /**
   * initializes the panel
   */
    public DatabasePanel() {
        super();
        initialize();
        createPanel();
    }

    /**
   * here we initialize all the member variables
   */
    protected void initialize() {
        showDriver = true;
        showConnect = true;
        showTables = false;
        showColumns = false;
        driver = Connector.MYSQL_DRIVER;
        url = Connector.MYSQL_URL;
        user = Connector.MYSQL_USER;
        password = Connector.MYSQL_PASSWORD;
        excludes = "_keys,_file*,_identifier_";
        password = "";
        preferredDatabase = "";
        preferredTable = "";
        preferredColumn = "";
        initialDatabase = "test";
        database = "";
        table = "";
        column = "";
        sizeListeners = new HashSet();
        connectorListeners = new HashSet();
        try {
            conn = new Connector();
        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }
    }

    /**
   * here the actual creating of components happens
   */
    protected void createPanel() {
        setLayout(new GridLayout(0, 1));
        labelDriver = new JLabel("Driver");
        textDriver = new JTextField(20);
        panelDriver = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelDriver.add(labelDriver);
        panelDriver.add(textDriver);
        add(panelDriver);
        labelUrl = new JLabel("URL");
        textUrl = new JTextField(20);
        labelInitialDatabase = new JLabel("InitialDatabase");
        textInitialDatabase = new JTextField(7);
        labelUser = new JLabel("User");
        textUser = new JTextField(7);
        labelPassword = new JLabel("Password");
        textPassword = new JTextField(7);
        buttonConnect = new JButton("Connect", ImageLoader.getImageIcon("network.gif"));
        buttonConnect.setActionCommand(ACTION_CONNECT);
        panelConnect = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelConnect.add(labelUrl);
        panelConnect.add(textUrl);
        panelConnect.add(labelInitialDatabase);
        panelConnect.add(textInitialDatabase);
        panelConnect.add(labelUser);
        panelConnect.add(textUser);
        panelConnect.add(labelPassword);
        panelConnect.add(textPassword);
        panelConnect.add(buttonConnect);
        add(panelConnect);
        labelDatabases = new JLabel("Databases");
        comboDatabases = new JComboBox();
        comboDatabases.addItemListener(this);
        buttonSelectDatabase = new JButton("Select", ImageLoader.getImageIcon("open.gif"));
        buttonSelectDatabase.setActionCommand(ACTION_SELECTDATABASE);
        labelTables = new JLabel("Tables");
        comboTables = new JComboBox();
        comboTables.addItemListener(this);
        labelExcludes = new JLabel("Exclude Pattern");
        textExcludes = new JTextField(10);
        buttonSelectTable = new JButton("Select", ImageLoader.getImageIcon("open.gif"));
        buttonSelectTable.setActionCommand(ACTION_SELECTTABLE);
        labelColumns = new JLabel("Columns");
        comboColumns = new JComboBox();
        comboColumns.addItemListener(this);
        panelDatabases = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelDatabases.add(labelDatabases);
        panelDatabases.add(comboDatabases);
        panelDatabases.add(buttonSelectDatabase);
        add(panelDatabases);
        panelTables = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelTables.add(labelExcludes);
        panelTables.add(textExcludes);
        panelTables.add(labelTables);
        panelTables.add(comboTables);
        panelTables.add(buttonSelectTable);
        add(panelTables);
        panelColumns = new JPanel(new FlowLayout(FlowLayout.LEADING));
        panelColumns.add(labelColumns);
        panelColumns.add(comboColumns);
        add(panelColumns);
        addActionListener(this);
        setEnabledState("");
        setValues();
    }

    /**
   * Invalidates the container.
   */
    public void invalidate() {
        int rows;
        rows = 1;
        if (getShowDriver()) rows++;
        if (getShowConnect()) rows++;
        if (getShowTables()) rows++;
        if (getShowColumns()) rows++;
        ((GridLayout) getLayout()).setRows(rows);
        super.invalidate();
    }

    /**
   * sets the connector object to use (instead of instantiating one)
   */
    public void setConnector(Connector conn) {
        this.conn = conn;
        if (conn != null) {
            driver = conn.getDriver();
            url = conn.getUrl();
            database = conn.getDatabase();
            user = conn.getUser();
            password = conn.getPassword();
            try {
                if (conn.isConnected()) {
                    setShowConnect(false);
                    comboDatabases.setEnabled(true);
                    buttonSelectDatabase.setEnabled(true);
                    setValues();
                    listDatabases();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            driver = "";
            url = "";
            database = "";
            user = "";
            password = "";
            setShowConnect(true);
            setEnabledState("");
        }
    }

    /**
   * returns the connector instance being used
   */
    public Connector getConnector() {
        return conn;
    }

    /**
   * sets the driver to use for connecting
   */
    public void setDriver(String driver) {
        textDriver.setText(driver);
    }

    /**
   * returns the driver for connecting
   */
    public String getDriver() {
        return textDriver.getText();
    }

    /**
   * sets the connection URL
   */
    public void setUrl(String url) {
        textUrl.setText(url);
    }

    /**
   * returns the connection URL
   */
    public String getUrl() {
        return textUrl.getText();
    }

    /**
   * sets the InitialDatabase for connecting
   */
    public void setInitialDatabase(String initialDatabase) {
        textInitialDatabase.setText(initialDatabase);
    }

    /**
   * returns the InitialDatabase for connecting
   */
    public String getInitialDatabase() {
        return textInitialDatabase.getText();
    }

    /**
   * sets the user to use for connecting to the DB
   */
    public void setUser(String user) {
        textUser.setText(user);
    }

    /**
   * returns the user used for connecting to the DB
   */
    public String getUser() {
        return textUser.getText();
    }

    /**
   * sets the password of the user
   */
    public void setPassword(String password) {
        textPassword.setText(password);
    }

    /**
   * returns the password of the user
   */
    public String getPassword() {
        return textPassword.getText();
    }

    /**
   * sets the patterns for excluding tables
   */
    public void setExcludes(String excludes) {
        textExcludes.setText(excludes);
    }

    /**
   * returns the patterns for excluding tables
   */
    public String getExcludes() {
        return textExcludes.getText();
    }

    /**
   * sets whether the driver should be displayed or not
   */
    public void setShowDriver(boolean show) {
        showDriver = show;
        remove(panelDriver);
        if (showDriver) add(panelDriver, 0);
        invalidate();
    }

    /**
   * returns whether the connect-part is displayed
   */
    public boolean getShowDriver() {
        return showDriver;
    }

    /**
   * sets whether the connect-part should be displayed or not
   */
    public void setShowConnect(boolean show) {
        showConnect = show;
        remove(panelConnect);
        if (showConnect) {
            if (getShowDriver()) add(panelConnect, 1); else add(panelConnect, 0);
        }
        invalidate();
    }

    /**
   * returns whether the driver is displayed
   */
    public boolean getShowConnect() {
        return showConnect;
    }

    /**
   * sets whether the tables should be displayed or not
   */
    public void setShowTables(boolean show) {
        showTables = show;
        remove(panelTables);
        remove(panelColumns);
        if (showTables) add(panelTables);
        buttonSelectDatabase.setVisible(showTables);
        invalidate();
    }

    /**
   * returns whether the tables are displayed
   */
    public boolean getShowTables() {
        return showTables;
    }

    /**
   * sets whether the columns should be displayed or not
   */
    public void setShowColumns(boolean show) {
        showColumns = show;
        remove(panelColumns);
        if (showColumns) add(panelColumns);
        buttonSelectTable.setVisible(showColumns);
        invalidate();
    }

    /**
   * returns whether the columns are displayed
   */
    public boolean getShowColumns() {
        return showColumns;
    }

    /**
   * sets the database
   */
    public void setDatabase(String database) {
        this.database = database;
        setSelectedItem(comboDatabases, database);
    }

    /**
   * returns the database
   */
    public String getDatabase() {
        return database;
    }

    /**
   * sets the preferred database, which is preselected after connecting
   * (if it can be found)
   */
    public void setPreferredDatabase(String preferredDatabase) {
        this.preferredDatabase = preferredDatabase;
    }

    /**
   * returns the preferred database
   */
    public String getPreferredDatabase() {
        return preferredDatabase;
    }

    /**
   * returns all available databases (if connected!)
   */
    public Vector getDatabases() {
        Vector result;
        int i;
        result = new ProperVector();
        for (i = 0; i < comboDatabases.getItemCount(); i++) result.add(comboDatabases.getItemAt(i));
        return result;
    }

    /**
   * sets the table
   */
    public void setTable(String table) {
        this.table = table;
        setSelectedItem(comboTables, table);
    }

    /**
   * returns the table
   */
    public String getTable() {
        return table;
    }

    /**
   * sets the preferred table, which is preselected after connecting
   * (if it can be found)
   */
    public void setPreferredTable(String preferredTable) {
        this.preferredTable = preferredTable;
    }

    /**
   * returns the preferred table
   */
    public String getPreferredTable() {
        return preferredTable;
    }

    /**
   * returns all available tables (if connected!)
   */
    public Vector getTables() {
        Vector result;
        int i;
        result = new ProperVector();
        for (i = 0; i < comboTables.getItemCount(); i++) result.add(comboTables.getItemAt(i));
        return result;
    }

    /**
   * sets the column
   */
    public void setColumn(String column) {
        this.column = column;
        setSelectedItem(comboColumns, column);
    }

    /**
   * returns the column
   */
    public String getColumn() {
        return column;
    }

    /**
   * sets the preferred column, which is preselected after connecting
   * (if it can be found)
   */
    public void setPreferredColumn(String preferredColumn) {
        this.preferredColumn = preferredColumn;
    }

    /**
   * returns the preferred column
   */
    public String getPreferredColumn() {
        return preferredColumn;
    }

    /**
   * transfers the member variables to the GUI
   */
    protected void setValues() {
        setShowDriver(showDriver);
        setShowTables(showTables);
        setShowColumns(showColumns);
        textDriver.setText(driver);
        textUrl.setText(url);
        textInitialDatabase.setText(initialDatabase);
        textUser.setText(user);
        textPassword.setText(password);
        textExcludes.setText(excludes);
        if ((database != null) && (!database.equals(""))) setSelectedItem(comboDatabases, database); else if (!preferredDatabase.equals("")) setSelectedItem(comboDatabases, preferredDatabase);
        if (!table.equals("")) setSelectedItem(comboTables, table); else if (!preferredTable.equals("")) setSelectedItem(comboTables, preferredTable);
        if (!column.equals("")) setSelectedItem(comboColumns, column); else if (!preferredColumn.equals("")) setSelectedItem(comboColumns, preferredColumn);
    }

    /**
   * sets the enabled state for the components, depending on the action
   */
    protected void setEnabledState(String action) {
        if (action.equals("")) {
            comboDatabases.setEnabled(false);
            buttonSelectDatabase.setEnabled(false);
            textExcludes.setEnabled(false);
            comboTables.setEnabled(false);
            comboColumns.setEnabled(false);
        } else if (action.equals(ACTION_CONNECT)) {
            comboDatabases.setEnabled(true);
            buttonSelectDatabase.setEnabled(true);
            textExcludes.setEnabled(true);
            comboTables.setEnabled(false);
            comboColumns.setEnabled(false);
        } else if (action.equals(ACTION_SELECTDATABASE)) {
            comboDatabases.setEnabled(true);
            buttonSelectDatabase.setEnabled(true);
            textExcludes.setEnabled(true);
            comboTables.setEnabled(true);
            comboColumns.setEnabled(false);
        } else if (action.equals(ACTION_SELECTTABLE)) {
            comboDatabases.setEnabled(true);
            buttonSelectDatabase.setEnabled(true);
            textExcludes.setEnabled(true);
            comboTables.setEnabled(true);
            comboColumns.setEnabled(true);
        }
    }

    /**
   * fills the combobox with the databases
   */
    private void listDatabases() {
        DatabaseLister lister;
        Vector list;
        int i;
        comboDatabases.removeAllItems();
        if (getUrl().startsWith("jdbc:odbc:")) return;
        if (conn != null) {
            try {
                lister = new DatabaseLister(conn);
                list = lister.getList();
                for (i = 0; i < list.size(); i++) comboDatabases.addItem(list.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * fills the combobox with the tables
   */
    private void listTables() {
        TableLister lister;
        Vector list;
        int i;
        comboTables.removeAllItems();
        if (conn != null) {
            try {
                lister = new TableLister(conn);
                lister.setExcludes(textExcludes.getText());
                lister.setSort(true);
                list = lister.getList();
                for (i = 0; i < list.size(); i++) comboTables.addItem(list.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * fills the combobox with the columns
   */
    private void listColumns() {
        ColumnLister lister;
        Vector list;
        int i;
        comboColumns.removeAllItems();
        if (conn != null) {
            try {
                lister = new ColumnLister(conn);
                lister.setTable(getTable());
                lister.setSort(true);
                list = lister.getList();
                for (i = 0; i < list.size(); i++) comboColumns.addItem(list.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
   * connects to the server and shows the databases
   */
    private void connect() {
        comboDatabases.removeAllItems();
        comboTables.removeAllItems();
        comboColumns.removeAllItems();
        try {
            if (conn == null) conn = new Connector();
            conn.connect(textDriver.getText(), textUrl.getText(), textInitialDatabase.getText(), textUser.getText(), textPassword.getText());
            listDatabases();
            setEnabledState(ACTION_CONNECT);
            setSelectedItem(comboDatabases, getPreferredDatabase());
            notifySizeListeners();
            notifyConnectorListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * shows the tables from the current database
   */
    private void selectDatabase() {
        comboTables.removeAllItems();
        comboColumns.removeAllItems();
        try {
            if (conn == null) conn = new Connector();
            conn.connect(textDriver.getText(), textUrl.getText(), getDatabase(), textUser.getText(), textPassword.getText());
            listTables();
            setEnabledState(ACTION_SELECTDATABASE);
            setSelectedItem(comboTables, getPreferredTable());
            notifySizeListeners();
            notifyConnectorListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * shows the columns from the current table
   */
    private void selectTable() {
        comboColumns.removeAllItems();
        try {
            listColumns();
            setEnabledState(ACTION_SELECTTABLE);
            setSelectedItem(comboColumns, getPreferredColumn());
            notifySizeListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
   * invoked when an action occurs
   */
    public void actionPerformed(ActionEvent e) {
        String action;
        action = e.getActionCommand();
        if (action.equals(ACTION_CONNECT)) {
            connect();
            firePropertyChange(action, "", "true");
        } else if (action.equals(ACTION_SELECTDATABASE)) {
            selectDatabase();
            firePropertyChange(action, "", "true");
        } else if (action.equals(ACTION_SELECTTABLE)) {
            selectTable();
            firePropertyChange(action, "", "true");
        }
    }

    /**
   * Invoked when an item has been selected or deselected by the user.
   */
    public void itemStateChanged(ItemEvent e) {
        String tmp;
        if (e.getSource() == comboDatabases) {
            if (comboDatabases.getSelectedIndex() > -1) {
                tmp = database;
                database = comboDatabases.getSelectedItem().toString();
                firePropertyChange(PROPERTY_DATABASE, tmp, database);
            }
        } else if (e.getSource() == comboTables) {
            if (comboTables.getSelectedIndex() > -1) {
                tmp = table;
                table = comboTables.getSelectedItem().toString();
                firePropertyChange(PROPERTY_TABLE, tmp, table);
            }
        } else if (e.getSource() == comboColumns) {
            if (comboTables.getSelectedIndex() > -1) {
                tmp = column;
                column = comboColumns.getSelectedItem().toString();
                firePropertyChange(PROPERTY_COLUMN, tmp, column);
            }
        }
    }

    /**
   * removes the given listener to the buttons
   */
    public void removeActionListener(ActionListener listener) {
        buttonConnect.removeActionListener(listener);
        buttonSelectDatabase.removeActionListener(listener);
        buttonSelectTable.removeActionListener(listener);
    }

    /**
   * adds the given listener to the buttons
   */
    public void addActionListener(ActionListener listener) {
        removeActionListener(listener);
        buttonConnect.addActionListener(listener);
        buttonSelectDatabase.addActionListener(listener);
        buttonSelectTable.addActionListener(listener);
    }

    /**
   * removes the keylistener for the buttons
   */
    public void removeKeyListener(KeyListener listener) {
        textDriver.removeKeyListener(listener);
        textUrl.removeKeyListener(listener);
        textInitialDatabase.removeKeyListener(listener);
        textUser.removeKeyListener(listener);
        textPassword.removeKeyListener(listener);
        textExcludes.removeKeyListener(listener);
    }

    /**
   * adds the keylistener for the buttons
   */
    public void addKeyListener(KeyListener listener) {
        removeKeyListener(listener);
        textDriver.addKeyListener(listener);
        textUrl.addKeyListener(listener);
        textInitialDatabase.addKeyListener(listener);
        textUser.addKeyListener(listener);
        textPassword.addKeyListener(listener);
        textExcludes.addKeyListener(listener);
    }

    /**
   * notifies all the size listeners about the size change
   */
    private void notifySizeListeners() {
        Iterator iter;
        Dimension size;
        SizeChangeEvent event;
        size = new Dimension(getPreferredSize());
        event = new SizeChangeEvent(this, size);
        iter = sizeListeners.iterator();
        while (iter.hasNext()) ((SizeChangeListener) iter.next()).sizeChanged(event);
    }

    /**
   * removes the given listener for size changes
   */
    public void removeSizeChangeListener(SizeChangeListener listener) {
        sizeListeners.remove(listener);
    }

    /**
   * adds the keylistener for the buttons
   */
    public void addSizeChangeListener(SizeChangeListener listener) {
        sizeListeners.add(listener);
    }

    /**
   * notifies all the size listeners about the size change
   */
    private void notifyConnectorListeners() {
        Iterator iter;
        ConnectorChangeEvent event;
        event = new ConnectorChangeEvent(this, conn);
        iter = connectorListeners.iterator();
        while (iter.hasNext()) ((ConnectorChangeListener) iter.next()).connectorChanged(event);
    }

    /**
   * removes the given listener for connector changes
   */
    public void removeConnectorChangeListener(ConnectorChangeListener listener) {
        connectorListeners.remove(listener);
    }

    /**
   * adds the keylistener for the buttons
   */
    public void addConnectorChangeListener(ConnectorChangeListener listener) {
        connectorListeners.add(listener);
    }
}
