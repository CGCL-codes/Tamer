package ArianneEditor;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import com.borland.dx.sql.dataset.*;
import java.sql.*;
import java.awt.Dimension;
import java.util.logging.Level;
import ArianneUtil.LogHandler;

public class TabularShapeDialog extends JDialog {

    JPanel panel1 = new JPanel();

    BorderLayout borderLayout1 = new BorderLayout();

    JPanel mainPanel = new JPanel();

    JLabel textLabel = new JLabel();

    JTextField fieldTextValue = new JTextField();

    JButton okButton = new JButton();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    JLabel hostLabel = new JLabel();

    JTextField hostTextField = new JTextField();

    JLabel dbNameLabel = new JLabel();

    JTextField dbTextField = new JTextField();

    JLabel portLabel = new JLabel();

    JTextField portTextField = new JTextField();

    JLabel userLabel = new JLabel();

    JTextField userTextField = new JTextField();

    JLabel passwordLabel = new JLabel();

    JPasswordField passwordField = new JPasswordField();

    JLabel sqlLabel = new JLabel();

    JTextField sqlTextField = new JTextField();

    JLabel refreshPeriodLabel = new JLabel();

    JTextField refreshPeriodTextField = new JTextField();

    JLabel chooseConnectionLabel = new JLabel();

    JComboBox chooseConnectionCombo = new JComboBox();

    private String hostName;

    private int connectionPort = -1;

    private String dbName;

    private String connectString;

    private String userName;

    private String password;

    private String sqlQuery;

    private int refreshPeriod = 0;

    private File file = null;

    private String jDataStoreName;

    private EditorDrawingPanel fatherPanel = null;

    private boolean logEnabled = false;

    Database localDb = new Database();

    ResultSet rp;

    JButton sqlTextAreaButton = new JButton();

    public TabularShapeDialog(EditorDrawingPanel p, Database db, String title, boolean modal, boolean le) {
        super(p.getFatherFrame(), title, modal);
        logEnabled = le;
        fatherPanel = p;
        localDb = db;
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
        }
    }

    public TabularShapeDialog() {
        this(null, null, "", false, false);
    }

    public boolean isLoggingEnabled() {
        return logEnabled;
    }

    public void init(String hName, int cPort, String dbN, String cString, String uName, String pwd, String sqlQ, int rPeriod) {
        hostName = hName;
        connectionPort = cPort;
        dbName = dbN;
        connectString = cString;
        userName = uName;
        password = pwd;
        sqlQuery = sqlQ;
        refreshPeriod = rPeriod;
        hostTextField.setText(this.hostName);
        dbTextField.setText(this.dbName);
        portTextField.setText("" + this.connectionPort);
        userTextField.setText(this.userName);
        sqlTextField.setText("" + this.sqlQuery);
        refreshPeriodTextField.setText("" + this.refreshPeriod);
        String imageName = fatherPanel.getImgName();
        try {
            String qp = "SELECT * FROM DBCONNECTION WHERE IMAGE_NAME ='" + imageName + "'";
            Statement sp = localDb.getJdbcConnection().createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rp = sp.executeQuery(qp);
            while (rp.next()) {
                chooseConnectionCombo.addItem(rp.getString("NAME"));
            }
            try {
                rp.beforeFirst();
                while (rp.next()) {
                    if (rp.getString("HOST").equalsIgnoreCase(hostName) && rp.getInt("PORT") == connectionPort && rp.getString("DBNAME").equalsIgnoreCase(dbName) && rp.getString("USERNAME").equalsIgnoreCase(userName) && rp.getString("PASSWORD").equalsIgnoreCase(password)) {
                        chooseConnectionCombo.setSelectedItem(rp.getString("NAME"));
                        break;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
            }
        } catch (java.sql.SQLException sqlex) {
            sqlex.printStackTrace();
            LogHandler.log(sqlex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
        } catch (Exception ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
        }
        chooseConnectionCombo.addActionListener(new TabularShapeDialog_chooseConnectionCombo_actionAdapter(this));
    }

    private void jbInit() throws Exception {
        panel1.setLayout(borderLayout1);
        mainPanel.setBorder(BorderFactory.createEtchedBorder());
        mainPanel.setLayout(gridBagLayout1);
        textLabel.setText("Text value:");
        fieldTextValue.setMaximumSize(new Dimension(1200, 100));
        fieldTextValue.setMinimumSize(new Dimension(250, 20));
        fieldTextValue.setPreferredSize(new Dimension(250, 20));
        fieldTextValue.setMargin(new Insets(1, 5, 2, 4));
        fieldTextValue.setText("");
        okButton.setText("OK");
        okButton.addActionListener(new TabularShapeDialog_okButton_actionAdapter(this));
        hostLabel.setText("Host:");
        hostTextField.setMaximumSize(new Dimension(1200, 100));
        hostTextField.setMinimumSize(new Dimension(250, 20));
        hostTextField.setPreferredSize(new Dimension(250, 20));
        hostTextField.setText("localhost");
        dbNameLabel.setText("DB Name:");
        dbTextField.setMaximumSize(new Dimension(1200, 100));
        dbTextField.setMinimumSize(new Dimension(250, 20));
        dbTextField.setPreferredSize(new Dimension(250, 20));
        dbTextField.setText("ped");
        portLabel.setText("Port:");
        portTextField.setMaximumSize(new Dimension(1200, 100));
        portTextField.setMinimumSize(new Dimension(250, 20));
        portTextField.setOpaque(true);
        portTextField.setPreferredSize(new Dimension(250, 20));
        portTextField.setText("3306");
        userLabel.setText("User name:");
        passwordLabel.setText("Password:");
        passwordField.setMaximumSize(new Dimension(1200, 100));
        passwordField.setMinimumSize(new Dimension(250, 20));
        passwordField.setPreferredSize(new Dimension(250, 20));
        passwordField.setText("");
        userTextField.setMinimumSize(new Dimension(250, 20));
        userTextField.setPreferredSize(new Dimension(250, 20));
        userTextField.setText("root");
        sqlLabel.setText("Sql query:");
        refreshPeriodLabel.setText("Refresh period:");
        refreshPeriodTextField.setMaximumSize(new Dimension(1200, 100));
        refreshPeriodTextField.setMinimumSize(new Dimension(250, 20));
        refreshPeriodTextField.setPreferredSize(new Dimension(250, 20));
        sqlTextField.setMinimumSize(new Dimension(250, 20));
        sqlTextField.setPreferredSize(new Dimension(250, 20));
        chooseConnectionLabel.setText("Choose Connection:");
        chooseConnectionCombo.setMaximumSize(new Dimension(1200, 100));
        chooseConnectionCombo.setMinimumSize(new Dimension(250, 20));
        chooseConnectionCombo.setPreferredSize(new Dimension(250, 20));
        sqlTextAreaButton.setMaximumSize(new Dimension(20, 21));
        sqlTextAreaButton.setMinimumSize(new Dimension(20, 21));
        sqlTextAreaButton.setPreferredSize(new Dimension(20, 21));
        sqlTextAreaButton.setText("...");
        sqlTextAreaButton.addActionListener(new TabularShapeDialog_sqlTextAreaButton_actionAdapter(this));
        getContentPane().add(panel1);
        panel1.add(mainPanel, BorderLayout.CENTER);
        mainPanel.add(textLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(fieldTextValue, new GridBagConstraints(2, 1, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(okButton, new GridBagConstraints(0, 10, 3, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(hostLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(hostTextField, new GridBagConstraints(2, 2, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(dbNameLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(dbTextField, new GridBagConstraints(2, 4, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(portLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(portTextField, new GridBagConstraints(2, 3, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(userLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(userTextField, new GridBagConstraints(2, 5, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(passwordLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(passwordField, new GridBagConstraints(2, 6, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(sqlLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(refreshPeriodLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(refreshPeriodTextField, new GridBagConstraints(2, 8, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(chooseConnectionLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(chooseConnectionCombo, new GridBagConstraints(2, 9, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
        mainPanel.add(sqlTextAreaButton, new GridBagConstraints(3, 7, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 0, 0, 0), 0, 0));
        mainPanel.add(sqlTextField, new GridBagConstraints(2, 7, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 0, 0), 0, 0));
    }

    public String getTextValue() {
        return fieldTextValue.getText();
    }

    public String getHostTextValue() {
        return hostTextField.getText();
    }

    public int getPortTextValue() {
        return Integer.parseInt(portTextField.getText());
    }

    public String getDbTextValue() {
        return dbTextField.getText();
    }

    public String getUserTextValue() {
        return userTextField.getText();
    }

    public String getPasswordTextValue() {
        return new String(passwordField.getPassword());
    }

    public String getsqlTextValue() {
        return sqlTextField.getText();
    }

    public int getRefreshPeriod() {
        return Integer.parseInt(refreshPeriodTextField.getText());
    }

    void okButton_actionPerformed(ActionEvent e) {
        this.dispose();
    }

    void chooseConnectionCombo_actionPerformed(ActionEvent e) {
        try {
            rp.beforeFirst();
            while (rp.next()) {
                if (((String) chooseConnectionCombo.getSelectedItem()).equals(rp.getString("NAME"))) {
                    hostTextField.setText(rp.getString("HOST"));
                    portTextField.setText(rp.getString("PORT"));
                    dbTextField.setText(rp.getString("DBNAME"));
                    userTextField.setText(rp.getString("USERNAME"));
                    passwordField.setText(rp.getString("PASSWORD"));
                    break;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            LogHandler.log(ex.getMessage(), Level.INFO, "LOG_MSG", isLoggingEnabled());
        }
    }

    public void sqlTextAreaButton_actionPerformed(ActionEvent e) {
        SQLTextAreaDialog sqld = new SQLTextAreaDialog(null, "SQL Query", true, isLoggingEnabled());
        sqld.init(sqlTextField.getText());
        sqld.setVisible(true);
        sqlTextField.setText(sqld.getSqlQuery());
    }
}

class TabularShapeDialog_sqlTextAreaButton_actionAdapter implements ActionListener {

    private TabularShapeDialog adaptee;

    TabularShapeDialog_sqlTextAreaButton_actionAdapter(TabularShapeDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.sqlTextAreaButton_actionPerformed(e);
    }
}

class TabularShapeDialog_okButton_actionAdapter implements java.awt.event.ActionListener {

    TabularShapeDialog adaptee;

    TabularShapeDialog_okButton_actionAdapter(TabularShapeDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.okButton_actionPerformed(e);
    }
}

class TabularShapeDialog_chooseConnectionCombo_actionAdapter implements java.awt.event.ActionListener {

    TabularShapeDialog adaptee;

    TabularShapeDialog_chooseConnectionCombo_actionAdapter(TabularShapeDialog adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.chooseConnectionCombo_actionPerformed(e);
    }
}
