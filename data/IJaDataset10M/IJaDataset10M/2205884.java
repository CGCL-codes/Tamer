package jstudio.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import jstudio.JStudio;
import jstudio.db.DatabaseInterface;
import jstudio.db.HibernateDB;
import jstudio.util.Configuration;
import jstudio.util.Language;

/**
 * User defined database connection parameters
 * @author Matteo
 *
 */
public class DBDialog extends JDialog implements ActionListener {

    private static final long serialVersionUID = 1L;

    public static void main(String args[]) {
        DBDialog dialog = new DBDialog(new JFrame(), new HibernateDB("jdbc:mysql", "com.mysql.jdbc.Driver"));
        Configuration.setGlobalConfiguration(new Configuration());
        dialog.showDialog(Configuration.getGlobalConfiguration());
        System.exit(0);
    }

    private boolean accept;

    private JTextField jdbcField, driverField, protocolField, hostField, nameField, userField, passwordField;

    private JButton acceptButton, testButton, cancelButton;

    private DatabaseInterface database;

    public DBDialog(JFrame parent, DatabaseInterface database) {
        super(parent);
        this.setTitle("Database Connection Configuration");
        this.database = database;
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Language.string("Connection details"), TitledBorder.LEFT, TitledBorder.CENTER));
        jdbcField = createField(16);
        driverField = createField(16);
        protocolField = createField(16);
        hostField = createField(16);
        nameField = createField(16);
        userField = createField(16);
        passwordField = createField(16);
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridy = 0;
        gc.insets = new Insets(4, 4, 4, 4);
        addInput(panel, gc, "JDBC:", jdbcField);
        addInput(panel, gc, "Driver:", driverField);
        addInput(panel, gc, "Protocol:", protocolField);
        addInput(panel, gc, "Source:", hostField);
        addInput(panel, gc, "Database name:", nameField);
        addInput(panel, gc, "Username:", userField);
        addInput(panel, gc, "Password:", passwordField);
        this.getContentPane().add(panel, BorderLayout.CENTER);
        JPanel bPanel = new JPanel();
        bPanel.setLayout(new GridBagLayout());
        acceptButton = createButton(Language.string("Ok"));
        testButton = createButton(Language.string("Test"));
        cancelButton = createButton(Language.string("Cancel"));
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1.0f;
        gc.fill = GridBagConstraints.HORIZONTAL;
        bPanel.add(acceptButton, gc);
        gc.gridx++;
        bPanel.add(testButton, gc);
        gc.gridx++;
        bPanel.add(cancelButton, gc);
        this.getContentPane().add(bPanel, BorderLayout.SOUTH);
        this.setModal(true);
        this.pack();
        this.setLocationRelativeTo(parent);
    }

    public boolean showDialog(Configuration c) {
        accept = false;
        setJDBC(c.getProperty(DatabaseInterface.KEY_JDBC, DatabaseInterface.DEF_JDBC));
        setDriver(c.getProperty(DatabaseInterface.KEY_DRIVER, DatabaseInterface.DEF_DRIVER));
        setProtocol(c.getProperty(DatabaseInterface.KEY_PROTOCOL, DatabaseInterface.DEF_PROTOCOL));
        setHost(c.getProperty(DatabaseInterface.KEY_HOST, DatabaseInterface.DEF_HOST));
        setDBName(c.getProperty(DatabaseInterface.KEY_NAME, DatabaseInterface.DEF_NAME));
        setUser(c.getProperty(DatabaseInterface.KEY_USER, DatabaseInterface.DEF_USER));
        setPassword(c.getProperty(DatabaseInterface.KEY_PASS, DatabaseInterface.DEF_PASS));
        setVisible(true);
        if (accept) {
            c.setProperty(DatabaseInterface.KEY_JDBC, getJDBC());
            c.setProperty(DatabaseInterface.KEY_DRIVER, getDriver());
            c.setProperty(DatabaseInterface.KEY_PROTOCOL, getProtocol());
            c.setProperty(DatabaseInterface.KEY_HOST, getHost());
            c.setProperty(DatabaseInterface.KEY_NAME, getDBName());
            c.setProperty(DatabaseInterface.KEY_USER, getUser());
            c.setProperty(DatabaseInterface.KEY_PASS, getPassword());
        }
        return accept;
    }

    /**
	 * Retrieve the database interface currently used within the dialog
	 * Database can be invalid or null
	 * @return
	 */
    public DatabaseInterface getDatabase() {
        return database;
    }

    public String getJDBC() {
        return jdbcField.getText();
    }

    public String getDriver() {
        return driverField.getText();
    }

    public String getProtocol() {
        return protocolField.getText();
    }

    public String getHost() {
        return hostField.getText();
    }

    public String getDBName() {
        return nameField.getText();
    }

    public String getUser() {
        return userField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }

    public void setJDBC(String t) {
        jdbcField.setText(t);
    }

    public void setDriver(String t) {
        driverField.setText(t);
    }

    public void setProtocol(String t) {
        protocolField.setText(t);
    }

    public void setHost(String t) {
        hostField.setText(t);
    }

    public void setDBName(String t) {
        nameField.setText(t);
    }

    public void setUser(String t) {
        userField.setText(t);
    }

    public void setPassword(String t) {
        passwordField.setText(t);
    }

    private JTextField createField(final int size) {
        assert (size > 0);
        JTextField textField = new JTextField();
        textField.setColumns(size);
        return textField;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.addActionListener(this);
        return button;
    }

    private void addInput(JPanel panel, GridBagConstraints gc, String title, JTextField field) {
        gc.gridx = 0;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.weightx = 0f;
        JLabel label = new JLabel(Language.string(title));
        label.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        label.setHorizontalAlignment(JLabel.RIGHT);
        panel.add(label, gc);
        gc.gridx++;
        gc.weightx = 1.0f;
        panel.add(field, gc);
        gc.gridy++;
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == acceptButton) {
            accept = true;
            this.dispose();
            synchronized (this) {
                this.notify();
            }
        } else if (src == cancelButton) {
            accept = false;
            this.dispose();
            synchronized (this) {
                this.notify();
            }
        } else if (src == testButton) {
            try {
                database = JStudio.getDatabaseInterface(getJDBC(), getProtocol(), getDriver());
                database.connect(getHost(), getDBName(), getUser(), getPassword());
                if (database.isConnected()) {
                    JOptionPane.showMessageDialog(this, Language.string("Connection established"), Language.string("Connection test"), JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new Exception(Language.string("Unknown error"));
                }
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(this, Language.string("Connection error") + ": " + e1.getLocalizedMessage(), Language.string("Connection test"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
