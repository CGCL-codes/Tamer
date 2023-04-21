package com.arsenal.sip.client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.Enumeration;
import java.util.Properties;
import java.io.File;
import java.util.Hashtable;
import com.arsenal.client.Client;
import com.arsenal.client.ConnectionWindow;
import com.arsenal.client.observer.*;
import com.arsenal.user.*;
import com.arsenal.log.Log;
import com.arsenal.skin.*;
import com.arsenal.util.Util;

public class UserAgentWindow extends JFrame implements LogoutObserver, LoginObserver, SkinChangeObserver {

    private JPanel topPanel = new JPanel();

    private JPanel bottomPanel = new JPanel();

    private JLabel userlabel = new JLabel(" Sip User: ");

    private Choice userChoice = new Choice();

    private JButton submitButton = new JButton("Call");

    private JButton cancelButton = new JButton("Cancel");

    public String getUser() {
        return userChoice.getSelectedItem().trim();
    }

    private static UserAgentWindow instance = new UserAgentWindow();

    public static UserAgentWindow getInstance() {
        if (instance == null) {
            instance = new UserAgentWindow();
        }
        return instance;
    }

    public UserAgentWindow() {
        super("Arsenal UserAgent Window");
        Util.setDefaultLookAndFeel();
        setIconImage(new ImageIcon("." + File.separator + "images" + File.separator + "arsenal" + File.separator + "ico.gif").getImage());
        setSize(new Dimension(400, 100));
        topPanel.setBackground(Color.white);
        bottomPanel.setBackground(Color.white);
        bottomPanel.add(submitButton);
        bottomPanel.add(cancelButton);
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        topPanel.setLayout(gridbag);
        JLabel[] labels = { userlabel };
        setUpForm(labels, gridbag, topPanel, userChoice, bottomPanel);
        JSplitPane jsplitpane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, bottomPanel);
        jsplitpane.setDividerSize(0);
        getContentPane().add(jsplitpane);
        submitButton.addActionListener(new CallButtonListener());
        cancelButton.addActionListener(new CancelButtonListener());
        registerLogoutListener(this);
        registerLoginListener(this);
        registerSkinChangeListener(this);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension size = getSize();
        screenSize.height = screenSize.height / 2;
        screenSize.width = screenSize.width / 2;
        size.height = size.height / 2;
        size.width = size.width / 2;
        int y = screenSize.height - size.height;
        int x = screenSize.width - size.width;
        setLocation(x, y);
    }

    private void setUpForm(JLabel[] label, GridBagLayout gridbag, Container container, Choice userChoice, JPanel buttonPane) {
        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.EAST;
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        gridbag.setConstraints(userlabel, c);
        container.add(userlabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        gridbag.setConstraints(userChoice, c);
        container.add(userChoice);
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.fill = GridBagConstraints.NONE;
        c.weightx = 0.0;
        JLabel blankLabel = new JLabel();
        gridbag.setConstraints(blankLabel, c);
        container.add(blankLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 1.0;
        gridbag.setConstraints(buttonPane, c);
        container.add(buttonPane);
    }

    public void addUserToSelect(String name) {
        Log.debug(this, "addUserToSelect: " + name);
        if (name != null) userChoice.add(name);
    }

    public void removeUserFromSelect(String name) {
        Log.debug(this, "removeUserFromSelect: " + name);
        if (name != null) {
            try {
                userChoice.remove(name);
            } catch (Exception e) {
            }
        }
    }

    private void disableAll() {
        userChoice.setEnabled(false);
    }

    private void enableAll() {
        userChoice.setEnabled(true);
    }

    public void addAddressToSipDialBox() {
        SipClientHandler.getInstance().addAddressToSipDialBox(userChoice.getSelectedItem());
    }

    /*******************************************************************************
   *
   * observers
   *
   *******************************************************************************/
    public void doLogoutAction() {
        userChoice.removeAll();
        disableAll();
        hide();
    }

    public void registerLogoutListener(LogoutObserver logoutObserver) {
        Client.getInstance().registerLogoutObserver(logoutObserver);
    }

    public void doLoginAction() {
        enableAll();
    }

    public void registerLoginListener(LoginObserver loginObserver) {
        Client.getInstance().registerLoginObserver(loginObserver);
    }

    public void doSkinChangeAction(Object object) {
        SkinBean bean = (SkinBean) object;
        topPanel.setBackground(bean.getBackgroundColor());
        topPanel.setForeground(bean.getForegroundColor());
        bottomPanel.setBackground(bean.getBackgroundColor());
        bottomPanel.setForeground(bean.getForegroundColor());
    }

    public void registerSkinChangeListener(SkinChangeObserver skinchangeObserver) {
        Client.getInstance().registerSkinChangeObserver(skinchangeObserver);
    }
}
