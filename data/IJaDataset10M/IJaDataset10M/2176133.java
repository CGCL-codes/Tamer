package org.zurell.java.SortImagesGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author m0554
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SetPropertiesGUI extends JDialog {

    private String Version = SortImagesGUI.Version;

    private Container cp;

    private JLabel dbHostLabel, dbPortLabel, dbNameLabel, dbTableLabel, dbUserLabel, dbPasswordLabel, dbDumpLabel, defaultTargetLabel, dbUsageLabel;

    private JTextField dbHostTextField, dbPortTextField, dbNameTextField, dbTableTextField, dbUserTextField, dbPasswordTextField, defaultTargetTextField;

    private JButton abortButton, saveButton;

    private JPanel leftPanel, rightPanel, southPanel;

    private Properties dbProperties;

    private JCheckBox dbUsage, dbDump;

    public SetPropertiesGUI(Frame parent, Properties props) {
        super(parent, true);
        dbProperties = props;
        initComponents();
        pack();
    }

    /**
	 * 
	 */
    private void initComponents() {
        cp = this.getContentPane();
        cp.setLayout(new BorderLayout());
        leftPanel = new JPanel(new GridLayout(9, 1));
        rightPanel = new JPanel(new GridLayout(9, 1));
        southPanel = new JPanel(new FlowLayout());
        dbHostLabel = new JLabel("Database host");
        dbPortLabel = new JLabel("Database port");
        dbNameLabel = new JLabel("Database name");
        dbTableLabel = new JLabel("Table nam");
        dbUserLabel = new JLabel("Username");
        dbPasswordLabel = new JLabel("Password");
        defaultTargetLabel = new JLabel("Default Target Directory");
        dbUsageLabel = new JLabel("Use Database?");
        dbDumpLabel = new JLabel("Dump Database on every usage?");
        dbHostTextField = new JTextField(20);
        dbPortTextField = new JTextField(20);
        dbNameTextField = new JTextField(20);
        dbTableTextField = new JTextField(20);
        dbUserTextField = new JTextField(20);
        dbPasswordTextField = new JTextField(20);
        defaultTargetTextField = new JTextField(20);
        dbUsage = new JCheckBox("yes/no");
        dbDump = new JCheckBox("yes/no");
        dbHostTextField.setText(dbProperties.getProperty("DBHOST"));
        dbPortTextField.setText(dbProperties.getProperty("DBPORT"));
        dbNameTextField.setText(dbProperties.getProperty("DBNAME"));
        dbTableTextField.setText(dbProperties.getProperty("DBTABLE"));
        dbUserTextField.setText(dbProperties.getProperty("DBUSER"));
        dbPasswordTextField.setText(dbProperties.getProperty("DBPASSWD"));
        defaultTargetTextField.setText(dbProperties.getProperty("PHOTODIR"));
        String ndb = dbProperties.getProperty("USEDB");
        if (ndb != null && ndb.matches("TRUE")) {
            dbUsage.setSelected(true);
        } else {
            dbUsage.setSelected(false);
        }
        String ddb = dbProperties.getProperty("DUMPDB");
        if (ddb != null && ddb.matches("TRUE")) {
            dbDump.setSelected(true);
        } else {
            dbDump.setSelected(false);
        }
        leftPanel.add(dbHostLabel);
        rightPanel.add(dbHostTextField);
        leftPanel.add(dbPortLabel);
        rightPanel.add(dbPortTextField);
        leftPanel.add(dbNameLabel);
        rightPanel.add(dbNameTextField);
        leftPanel.add(dbTableLabel);
        rightPanel.add(dbTableTextField);
        leftPanel.add(dbUserLabel);
        rightPanel.add(dbUserTextField);
        leftPanel.add(dbPasswordLabel);
        rightPanel.add(dbPasswordTextField);
        leftPanel.add(defaultTargetLabel);
        rightPanel.add(defaultTargetTextField);
        leftPanel.add(dbUsageLabel);
        rightPanel.add(dbUsage);
        leftPanel.add(dbDumpLabel);
        rightPanel.add(dbDump);
        cp.add(leftPanel, BorderLayout.WEST);
        cp.add(rightPanel, BorderLayout.EAST);
        abortButton = new JButton("Abort");
        abortButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                abortButtonActionPerformed(e);
            }
        });
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                saveButtonActionPerformed(e);
            }
        });
        southPanel.add(abortButton);
        southPanel.add(saveButton);
        cp.add(southPanel, BorderLayout.SOUTH);
    }

    /**
	 * @param e
	 */
    protected void saveButtonActionPerformed(ActionEvent e) {
        dbProperties.setProperty("DBHOST", dbHostTextField.getText());
        dbProperties.setProperty("DBPORT", dbPortTextField.getText());
        dbProperties.setProperty("DBNAME", dbNameTextField.getText());
        dbProperties.setProperty("DBTABLE", dbTableTextField.getText());
        dbProperties.setProperty("DBUSER", dbUserTextField.getText());
        dbProperties.setProperty("DBPASSWD", dbPasswordTextField.getText());
        dbProperties.setProperty("PHOTODIR", defaultTargetTextField.getText());
        if (dbUsage.isSelected()) {
            dbProperties.setProperty("USEDB", "TRUE");
        } else {
            dbProperties.setProperty("USEDB", "FALSE");
        }
        if (dbDump.isSelected()) {
            dbProperties.setProperty("DUMPDB", "TRUE");
        } else {
            dbProperties.setProperty("DUMPDB", "FALSE");
        }
        try {
            dbProperties.store(new BufferedOutputStream(new FileOutputStream(dbProperties.getProperty("PROPERTIESFILE"))), "SortImagesGUI Properties File");
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.setVisible(false);
        try {
            this.finalize();
        } catch (Throwable e2) {
            e2.printStackTrace();
        }
    }

    /**
	 * @param e
	 */
    protected void abortButtonActionPerformed(ActionEvent e) {
        this.setVisible(false);
        try {
            this.finalize();
        } catch (Throwable e1) {
            e1.printStackTrace();
        }
    }

    /**
	 * @return Returns the dbProperties.
	 */
    public Properties getDbProperties() {
        return dbProperties;
    }
}
