package com.sptci.rwt.webui;

import nextapp.echo2.app.Column;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Grid;
import nextapp.echo2.app.Label;
import nextapp.echo2.app.PasswordField;
import nextapp.echo2.app.Row;
import nextapp.echo2.app.SelectField;
import nextapp.echo2.app.TextField;
import nextapp.echo2.app.event.ActionEvent;
import nextapp.echo2.app.event.ActionListener;
import consultas.echo2consultas.LiveTextField;
import echopointng.PopUp;
import com.sptci.echo2.Configuration;
import com.sptci.echo2.Dimensions;
import com.sptci.echo2.Utilities;
import com.sptci.echo2.WindowPane;
import com.sptci.echo2.style.Extent;

/**
 * The dialogue used to initiate a new JDBC {@link java.sql.Connection}.
 * Also used to save/edit pre-configured connections.
 *
 * <p>&copy; Copyright 2007 Sans Pareil Technologies, Inc.</p>
 * @author Rakesh Vidyadharan 2007-09-30
 * @version $Id: ConnectionDialogue.java 2 2007-10-19 21:06:36Z rakesh.vidyadharan $
 */
public class ConnectionDialogue extends WindowPane {

    /** The main controller for the application. */
    private final MainController controller;

    /** The container used to display all the components. */
    private final Column column = new Column();

    /** The component used to display configured database engines. */
    private SelectField databaseType;

    /** The component used to specify the hostname of the database server. */
    private TextField host;

    /** The port on wich the database listens for connections. */
    private LiveTextField port;

    /** The name of the database to connect to. */
    private TextField database;

    /** The component used to enter the user name to login as. */
    private TextField userName;

    /** The component used to enter the password to logon with. */
    private PasswordField password;

    /**
   * Create a new instance blank of the view.
   *
   * @param controller The {@link #controller} to use.
   */
    public ConnectionDialogue(final MainController controller) {
        super();
        this.controller = controller;
        setMaximizable(false);
        setMinimizable(false);
        initComponents();
        add(column);
    }

    /**
   * Initialises all the view components.
   *
   * @see com.sptci.echo2.Utilities#createTextField( String, String, String, Object )
   * @see #createDatabaseType
   * @see #createPort
   * @see #createButtons
   */
    private void initComponents() {
        Grid grid = new Grid();
        grid.add(Utilities.createLabel(getClass().getName(), "databaseType", "Title.Label"));
        grid.add(createDatabaseType());
        grid.add(Utilities.createLabel(getClass().getName(), "host", "Title.Label"));
        grid.add(Utilities.createTextField(getClass().getName(), "host", this));
        grid.add(Utilities.createLabel(getClass().getName(), "port", "Title.Label"));
        grid.add(createPort());
        grid.add(Utilities.createLabel(getClass().getName(), "database", "Title.Label"));
        grid.add(Utilities.createTextField(getClass().getName(), "database", this));
        grid.add(Utilities.createLabel(getClass().getName(), "userName", "Title.Label"));
        grid.add(Utilities.createTextField(getClass().getName(), "userName", this));
        grid.add(Utilities.createLabel(getClass().getName(), "password", "Title.Label"));
        grid.add(Utilities.createTextField(getClass().getName(), "password", new ConnectListener(controller), this));
        column.add(grid);
        column.add(createButtons());
    }

    /**
   * Initialise the {@link #databaseType} field with the localised values.
   *
   * @return The initialised {@link #databaseType} component.
   */
    protected Component createDatabaseType() {
        String[] values = Configuration.getString(this, "databaseTypes").split(",");
        for (int i = 0; i < values.length; ++i) {
            values[i] = values[i].trim();
        }
        databaseType = new SelectField(values);
        databaseType.setWidth(Extent.getInstance(Dimensions.getInt(this, "databaseType.width")));
        databaseType.addActionListener(new DatabaseTypeListener());
        return databaseType;
    }

    /**
   * Initialise the {@link #port} field with the localised values.
   *
   * @return The initialised {@link #port} component.
   */
    protected Component createPort() {
        port = new LiveTextField();
        port.setRegexp("[1-9][0-9]*");
        return port;
    }

    /**
   * Create the component used to display the control butons for the
   * dialogue.
   *
   * @see #createSave
   * @return The container component with the buttons.
   */
    protected Component createButtons() {
        Row row = new Row();
        row.add(Utilities.createButton(getClass().getName(), "connect", "Accept.Button", new ConnectListener(controller)));
        row.add(Utilities.createButton(getClass().getName(), "cancel", "Cancel.Button", new CancelListener()));
        row.add(createSave());
        return row;
    }

    /**
   * Create the {@link echopointng.PopUp} component used to display the
   * component used to capture user input on the name to assign to save
   * the JDBC connection parameters represented by this component.
   *
   * @return The component used to capture user input.
   */
    protected Component createSave() {
        PopUp popup = new PopUp();
        popup.setTarget(Utilities.createLabel(getClass().getName(), "save", "Save.Label"));
        popup.setPopUp(new SaveConnectionComponent(controller));
        return popup;
    }

    /**
   * Return the database engine that was selected.
   *
   * @return The database type selected.
   */
    protected String getDatabaseType() {
        return (String) databaseType.getSelectedItem();
    }

    /**
   * Sets the selected item in {@link #databaseType} to the value specified.
   *
   * @param value The name to set as selected.
   */
    protected void setDatabaseType(final String value) {
        databaseType.setSelectedItem(value);
    }

    /**
   * Return the hostname of the database server as entered in the view.
   *
   * @return The hastname of the server to connect to.
   */
    protected String getHost() {
        return host.getText();
    }

    /**
   * Sets the value displayed in the {@link #host} component.
   *
   * @param value The value to display.
   */
    protected void setHost(final String value) {
        host.setText(value);
    }

    /**
   * Return the port on which the database server listens for connections.
   *
   * @return The port number.
   */
    protected int getPort() {
        return ((port.getText().length() == 0) ? 0 : Integer.parseInt(port.getText()));
    }

    /**
   * Sets the value displayed in {@link #port} component.
   *
   * @param value The value to display.
   */
    protected void setPort(final int value) {
        port.setText(String.valueOf(value));
    }

    /**
   * Return the name of the database (instance) to connect to.
   *
   * @return The name of the database to connect to.
   */
    protected String getDatabase() {
        return database.getText();
    }

    /**
   * Sets the value displayed in the {@link #database} component.
   *
   * @param value The value to display.
   */
    protected void setDatabase(final String value) {
        database.setText(value);
    }

    /**
   * Return the user name to logon to the database server as.
   *
   * @return The user name as entered.
   */
    protected String getUserName() {
        return userName.getText();
    }

    /**
   * Sets the value displayed in the {@link #userName} component.
   *
   * @param value The value to display.
   */
    protected void setUserName(final String value) {
        userName.setText(value);
    }

    /**
   * Return the password to use to login to the database server.
   *
   * @return The password as entered.
   */
    protected String getPassword() {
        return password.getText();
    }

    /**
   * Sets the value displayed in the {@link #password} component.
   *
   * @param value The value to display.
   */
    protected void setPassword(final String value) {
        password.setText(value);
    }

    /**
   * The {@link nextapp.echo2.app.event.ActionListener} that updates
   * {@link #port} depending upon the selections made in {@link
   * #databaseType}.
   */
    protected class DatabaseTypeListener implements ActionListener {

        /**
     * The action listener implementation.  Updates the port value to
     * the default value for the selected database type.
     *
     * @param event The event that was triggered.
     */
        public void actionPerformed(final ActionEvent event) {
            port.setText(Configuration.getString(ConnectionDialogue.this, databaseType.getSelectedItem().toString() + ".port"));
        }
    }

    /**
   * The {@link nextapp.echo2.app.event.ActionListener} that cancels
   * (closes) this connection dialogue.
   */
    protected class CancelListener implements ActionListener {

        /**
     * The action listener implementation.  Closes this dialogue.
     *
     * @param event The event that was triggered.
     */
        public void actionPerformed(final ActionEvent event) {
            ConnectionDialogue.this.userClose();
        }
    }
}
