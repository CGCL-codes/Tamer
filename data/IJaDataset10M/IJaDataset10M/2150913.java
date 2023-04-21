package com.ezsoft.ezpersistence;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;

/**
 * <p>Title:        Persistence</p>
 * <p>  Description:</p>
 * <p> Copyright:    Copyright (c) 2001</p>
 * <p>  Company:      http://www.ez-softinc.com</p>
 * @author Michael Lee
 * @version 0.9
 */
public class PersistenceAdminDataSource extends JDialog {

    JPanel panel1 = new JPanel();

    JTextField userName = new JTextField();

    JLabel userNameLabel = new JLabel();

    JPasswordField password = new JPasswordField();

    JLabel passwordLabel = new JLabel();

    JLabel jndiLabel = new JLabel();

    JTextField jndiName = new JTextField();

    JTextField initialContextURL = new JTextField();

    JLabel contextURLLabel = new JLabel();

    JPanel driverManagerPanel = new JPanel();

    TitledBorder titledBorder1;

    Border border1;

    TitledBorder titledBorder2;

    JLabel logoPic = new JLabel();

    JButton finishedButton = new JButton();

    JButton backButton = new JButton();

    JButton closeButton = new JButton();

    JButton helpButton = new JButton();

    public PersistenceAdminDataSource(Frame frame, String title, boolean modal) {
        super(frame, title, modal);
        try {
            jbInit();
            pack();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PersistenceAdminDataSource() {
        this(null, "", false);
    }

    void jbInit() throws Exception {
        titledBorder1 = new TitledBorder("");
        border1 = BorderFactory.createEtchedBorder(Color.white, new Color(134, 134, 134));
        titledBorder2 = new TitledBorder(border1, "Driver Manager");
        panel1.setLayout(null);
        userName.setBounds(new Rectangle(16, 143, 189, 21));
        userName.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                userName_actionPerformed(e);
            }
        });
        userNameLabel.setText("User Name");
        userNameLabel.setBounds(new Rectangle(16, 127, 73, 17));
        password.setBounds(new Rectangle(15, 191, 112, 21));
        passwordLabel.setText("Password");
        passwordLabel.setBounds(new Rectangle(18, 174, 65, 17));
        jndiLabel.setText("JNDI Name");
        jndiLabel.setBounds(new Rectangle(14, 22, 73, 17));
        jndiName.setText("jdbc/poolName");
        jndiName.setBounds(new Rectangle(15, 40, 191, 21));
        initialContextURL.setBounds(new Rectangle(15, 88, 189, 21));
        contextURLLabel.setText("Initial Context URL");
        contextURLLabel.setBounds(new Rectangle(13, 66, 109, 17));
        driverManagerPanel.setBorder(titledBorder2);
        driverManagerPanel.setBounds(new Rectangle(156, 13, 234, 232));
        driverManagerPanel.setLayout(null);
        logoPic.setBounds(new Rectangle(20, 31, 126, 218));
        finishedButton.setText("Finished");
        finishedButton.setBounds(new Rectangle(198, 256, 82, 27));
        finishedButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                finishedButton_actionPerformed(e);
            }
        });
        backButton.setText("Back");
        backButton.setBounds(new Rectangle(117, 256, 81, 27));
        backButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                backButton_actionPerformed(e);
            }
        });
        closeButton.setText("Close");
        closeButton.setBounds(new Rectangle(301, 257, 82, 27));
        closeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                closeButton_actionPerformed(e);
            }
        });
        helpButton.setText("Help");
        helpButton.setBounds(new Rectangle(17, 256, 79, 27));
        helpButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                helpButton_actionPerformed(e);
            }
        });
        getContentPane().add(panel1);
        panel1.add(driverManagerPanel, null);
        driverManagerPanel.add(jndiLabel, null);
        driverManagerPanel.add(jndiName, null);
        driverManagerPanel.add(contextURLLabel, null);
        driverManagerPanel.add(initialContextURL, null);
        driverManagerPanel.add(password, null);
        driverManagerPanel.add(passwordLabel, null);
        driverManagerPanel.add(userName, null);
        driverManagerPanel.add(userNameLabel, null);
        panel1.add(logoPic, null);
        panel1.add(backButton, null);
        panel1.add(finishedButton, null);
        panel1.add(closeButton, null);
        panel1.add(helpButton, null);
    }

    void userName_actionPerformed(ActionEvent e) {
    }

    void closeButton_actionPerformed(ActionEvent e) {
    }

    void finishedButton_actionPerformed(ActionEvent e) {
    }

    void backButton_actionPerformed(ActionEvent e) {
    }

    void helpButton_actionPerformed(ActionEvent e) {
    }
}
