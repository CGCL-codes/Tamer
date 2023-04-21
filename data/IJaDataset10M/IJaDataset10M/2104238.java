package net.sourceforge.jdbcexplorer;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DriverManagerConnectionPanel extends AbstractConnectionPanel {

    public static final String CONNECTION_TYPE = "driver_manager";

    public static final String DRIVER_PROP = "driver";

    public static final String URL_PROP = "url";

    public static final String USER_PROP = "user";

    public static final String PASSWORD_PROP = "password";

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(new DriverManagerConnectionPanel());
        f.pack();
        f.show();
    }

    public DriverManagerConnectionPanel() {
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints gc = new GridBagConstraints();
        setLayout(gridbag);
        _driverText = new JTextField("sun.jdbc.odbc.JdbcOdbcDriver", 15);
        _driverText.setToolTipText("Database Driver");
        _urlText = new JTextField("jdbc:odbc:DATABASENAME", 15);
        _urlText.setToolTipText("Database Name");
        _userText = new JTextField(15);
        _userText.setToolTipText("Username");
        _passText = new JPasswordField(15);
        _passText.setToolTipText("Password");
        JLabel conLabel = new JLabel("Driver / URL: ");
        JLabel userLabel = new JLabel("User / Password: ");
        gc.insets = new Insets(2, 2, 2, 2);
        gc.fill = GridBagConstraints.NONE;
        gc.anchor = GridBagConstraints.EAST;
        gc.weighty = 0;
        gc.weightx = 0;
        gc.gridx = 0;
        gc.gridy = 0;
        gridbag.setConstraints(conLabel, gc);
        add(conLabel);
        gc.gridy = 1;
        gridbag.setConstraints(userLabel, gc);
        add(userLabel);
        gc.weightx = 1;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = 1;
        gc.gridy = 0;
        gridbag.setConstraints(_driverText, gc);
        add(_driverText);
        gc.gridy = 1;
        gridbag.setConstraints(_userText, gc);
        add(_userText);
        gc.gridx = 2;
        gc.gridy = 0;
        gridbag.setConstraints(_urlText, gc);
        add(_urlText);
        gc.gridy = 1;
        gridbag.setConstraints(_passText, gc);
        add(_passText);
    }

    public Properties getConnectionProperties() {
        Properties props = new Properties();
        props.setProperty(CONNECTION_TYPE_PROP, CONNECTION_TYPE);
        props.setProperty(DRIVER_PROP, _driverText.getText());
        props.setProperty(URL_PROP, _urlText.getText());
        props.setProperty(USER_PROP, _userText.getText());
        props.setProperty(PASSWORD_PROP, new String(_passText.getPassword()));
        return props;
    }

    public void setConnectionProperties(Properties props) {
        _driverText.setText(props.getProperty(DRIVER_PROP));
        _urlText.setText(props.getProperty(URL_PROP));
        _userText.setText(props.getProperty(USER_PROP));
        _passText.setText(props.getProperty(PASSWORD_PROP));
    }

    public Connection getConnection() throws SQLException {
        try {
            Class.forName(_driverText.getText()).newInstance();
            return DriverManager.getConnection(_urlText.getText(), _userText.getText(), new String(_passText.getPassword()));
        } catch (ClassNotFoundException e) {
            throw new SQLException("No driver found for " + _driverText.getText());
        } catch (InstantiationException e) {
            throw new SQLException("Driver " + _driverText.getText() + " could not be instantiated.");
        } catch (IllegalAccessException e) {
            throw new SQLException("Driver " + _driverText.getText() + " could not be instantiated: " + e.getMessage());
        }
    }

    public JTextField _driverText;

    public JTextField _urlText;

    public JTextField _userText;

    public JPasswordField _passText;
}
