package org.tigr.cloe.view.gui.login;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import org.tigr.cloe.controller.Controller;
import org.tigr.cloe.controller.login.LoginInfo;
import org.tigr.cloe.controller.login.LoginListener;
import org.tigr.cloe.controller.login.LoginModel;
import org.tigr.cloe.model.facade.datastoreFacade.authentication.IUserCredentials;
import org.tigr.cloe.model.facade.datastoreFacade.authentication.TDBUserCredentials;
import org.tigr.cloe.model.facade.datastoreFacade.authentication.UserCredentials;
import org.tigr.cloe.model.facade.datastoreFacade.datastore.IDataStore;
import org.tigr.common.Application;
import org.tigr.common.CommonUtils;
import org.tigr.seq.log.*;
import org.tigr.seq.seqdata.edit.AssemblyEditUtil;
import org.tigr.seq.display.*;

/**
 *
 * A fairly standard Sybase login window.
 *
 * <p>
 * Copyright &copy; 2001 The Institute for Genomic Research (TIGR).
 * <p>
 * All rights reserved.
 * 
 * <pre>
 * $RCSfile: TDBLoginWindow.java,v $
 * $Revision: 1.55 $
 * $Date: 2006/01/31 18:50:39 $
 * $Author: dkatzel $
 * </pre>
 * 
 *
 * @author Miguel Covarrubias
 * @version 1.0
 */
@SuppressWarnings("serial")
public class LoginWindow extends JFrame implements LoginListener {

    /**
     * Handler for cancel event if specified as the CANCEL_HANDLER
     * attribute in the obtainIdentification hash. */
    protected ActionListener cancelHandler;

    /** Various GUI components that we need to keep references to. */
    protected JTextField usernameField;

    /**
     * Describe variable <code>passwordField</code> here.
     *
     *
     */
    protected JPasswordField passwordField;

    /**
     * Describe variable <code>serverCombo</code> here.
     *
     *
     */
    protected JComboBox serverCombo;

    /**
     * Text field to type in project name, this will replace projectCombo
     * which got too annoying with 530 projects to find anything.
     */
    protected JTextField projectField;

    /**
     * Describe variable <code>serverDescriptionLabel</code> here.
     *
     *
     */
    protected JLabel serverDescriptionLabel;

    /**
     * Describe constant <code>WINDOW_SIZE</code> here.
     *
     *
     */
    private static final Dimension WINDOW_SIZE = new Dimension(255, 330);

    /**
     * The login button.
     *
     *
     */
    protected JButton loginButton;

    private List<IDataStore> dataStores;

    public LoginWindow(List<IDataStore> datastores) {
        AppUtil.setApplicationLAF();
        this.dataStores = datastores;
        this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                if (LoginWindow.this.cancelHandler != null) {
                    ActionEvent ae = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ResourceUtil.getResource(LoginWindow.class, "text.simulated_action"));
                    LoginWindow.this.cancelHandler.actionPerformed(ae);
                }
            }
        });
        Controller.getInstance().getLoginModel().addModelListener(this);
        this.setTitle(Application.getApplicationName() + " Log In");
        this.cancelHandler = new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                Application.exit(1);
            }
        };
        this.build();
    }

    /**
     * Internal method for actually laying out the login window.
     */
    protected void build() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 10, 3, 0);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weighty = 0.0;
        JPanel loginPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        JPanel mainPanel = new JPanel(new BorderLayout(5, 5));
        JPanel innerPanel = new JPanel(new BorderLayout(15, 15));
        this.getContentPane().setLayout(new BorderLayout(15, 15));
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 0.0;
        JLabel usernameLabel = new JLabel("Login:");
        loginPanel.add(usernameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 1;
        c.weightx = 1.0;
        this.usernameField = new JTextField();
        this.usernameField.setName("usernameField");
        this.usernameField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent fe) {
                LoginWindow.this.usernameField.selectAll();
            }

            public void focusLost(FocusEvent fe) {
            }
        });
        loginPanel.add(this.usernameField, c);
        KeyStroke enter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0);
        Keymap keymap = this.usernameField.getKeymap();
        keymap.removeKeyStrokeBinding(enter);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        loginPanel.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 0.0;
        JLabel passwordLabel = new JLabel("Password:");
        loginPanel.add(passwordLabel, c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 1.0;
        this.passwordField = new JPasswordField();
        this.passwordField.setEchoChar('*');
        this.passwordField.setName("passwordField");
        keymap = this.passwordField.getKeymap();
        keymap.removeKeyStrokeBinding(enter);
        this.passwordField.addFocusListener(new FocusListener() {

            public void focusGained(FocusEvent fe) {
                LoginWindow.this.passwordField.selectAll();
            }

            public void focusLost(FocusEvent fe) {
            }
        });
        loginPanel.add(this.passwordField, c);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 2;
        loginPanel.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 1;
        c.weightx = 0.0;
        JLabel serverLabel = new JLabel("Data Store:");
        loginPanel.add(serverLabel, c);
        this.serverCombo = new JComboBox(dataStores.toArray());
        if (dataStores.size() > 0) {
            this.serverDescriptionLabel = new JLabel(dataStores.get(0).getDescription());
            this.serverCombo.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    IDataStore dataStore = (IDataStore) serverCombo.getSelectedItem();
                    serverDescriptionLabel.setText(dataStore.getDescription());
                }
            });
        } else {
            this.serverDescriptionLabel = new JLabel("No Data Stores available");
        }
        c.gridx = 1;
        c.gridy = 4;
        c.gridwidth = 1;
        c.weightx = 1.0;
        loginPanel.add(this.serverCombo, c);
        c.gridx = 0;
        c.gridy = 5;
        c.gridwidth = 2;
        loginPanel.add(new JPanel(), c);
        c.gridx = 0;
        c.gridy = 6;
        c.gridwidth = 1;
        c.weightx = 0.0;
        JLabel projectLabel = new JLabel("Project:");
        loginPanel.add(projectLabel, c);
        projectField = new JTextField();
        projectField.setName("projectField");
        c.gridx = 1;
        c.gridy = 6;
        c.gridwidth = 1;
        c.weightx = 1.0;
        loginPanel.add(this.projectField, c);
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                String username = LoginWindow.this.usernameField.getText().trim();
                if (username.equals("")) {
                    Controller.getInstance().getView().displayErrorMessage("Invalid Login", "An empty login is invalid.  Please enter a valid login");
                    return;
                }
                String password = new String(LoginWindow.this.passwordField.getPassword()).trim();
                if (password.equals("")) {
                    Controller.getInstance().getView().displayErrorMessage("Invalid Login", "An empty password is invalid.  Please enter a valid password.");
                    return;
                }
                String project = projectField.getText().trim();
                if (project == null || project.equals("")) {
                    Controller.getInstance().getView().displayErrorMessage("Invalid Login", "No Project Selected");
                    return;
                }
                IDataStore dataStore = (IDataStore) LoginWindow.this.serverCombo.getSelectedItem();
                IUserCredentials userCredentials = new TDBUserCredentials(username, password, project);
                Controller.getInstance().loginToDataStore(new LoginInfo(dataStore, userCredentials));
            }
        };
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(al);
        loginButton.setActionCommand("Login");
        this.loginButton = loginButton;
        this.getRootPane().setDefaultButton(loginButton);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                if (LoginWindow.this.cancelHandler != null) {
                    LoginWindow.this.cancelHandler.actionPerformed(ae);
                }
            }
        });
        buttonPanel.add(cancelButton);
        buttonPanel.add(loginButton);
        innerPanel.add("Center", loginPanel);
        innerPanel.add("South", buttonPanel);
        Border border = BorderFactory.createEmptyBorder(20, 10, 0, 10);
        innerPanel.setBorder(border);
        mainPanel.add("Center", innerPanel);
        JPanel messagesPanel = new JPanel(new GridBagLayout());
        c.insets = new Insets(0, 10, 3, 0);
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0;
        messagesPanel.add(this.serverDescriptionLabel, c);
        ResourceBundle bundle = ResourceBundle.getBundle("org/tigr/seq/cloe/resources/build_ant");
        String cloe_build = bundle.getString("build.tag");
        c.gridy = 1;
        messagesPanel.add(new JLabel(cloe_build), c);
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.0;
        messagesPanel.add(new JPanel(), c);
        this.getContentPane().add("South", messagesPanel);
        this.getContentPane().add("Center", mainPanel);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screendim = tk.getScreenSize();
        this.setSize(LoginWindow.WINDOW_SIZE);
        this.setResizable(false);
        Dimension mydim = this.getSize();
        this.setLocation(screendim.width / 2 - mydim.width / 2, screendim.height / 2 - mydim.height / 2);
        readSaveLoginData();
    }

    private void readSaveLoginData() {
        InputStream inStream = Application.getPreferencesInputStream();
        if (inStream != null) {
            Properties props = new Properties();
            try {
                props.load(inStream);
                this.usernameField.setText(props.getProperty("user"));
                this.projectField.setText(props.getProperty("project"));
                String dataStoreName = props.getProperty("server");
                ComboBoxModel comboBoxModel = this.serverCombo.getModel();
                for (int i = 0; i < comboBoxModel.getSize(); i++) {
                    IDataStore datastore = (IDataStore) comboBoxModel.getElementAt(i);
                    if (datastore.getName().equals(dataStoreName)) {
                        comboBoxModel.setSelectedItem(datastore);
                        break;
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * public method saveUserCredentials: save the TDBUserCredentials to a property file, which
     * is application specific.
     */
    public void saveUserCredentials() {
        OutputStream outStream = Application.getPreferencesOutputStream();
        if (outStream != null) {
            TDBUserCredentials tdbCredentials = (TDBUserCredentials) UserCredentials.getUserCredentials();
            String username = tdbCredentials.getUserName();
            String project = tdbCredentials.getProjectName();
            String dataStoreName = Application.getDatastore().getName();
            PrintStream output = new PrintStream(outStream);
            output.println("#Saved User Credentials");
            Date date = Calendar.getInstance().getTime();
            output.println("#" + date);
            output.println("user=" + username);
            output.println("project=" + project);
            output.println("server=" + dataStoreName);
            output.close();
        }
    }

    public void modelChanged(LoginModel model) {
        Boolean loggedIn = model.loginSucceeded.getValue();
        if (loggedIn != null && loggedIn == true) {
            System.out.println("min overlap for project = " + AssemblyEditUtil.MINIMUM_OVERLAP);
            saveUserCredentials();
            Controller.getInstance().getLoginModel().removeModelListener(this);
            LoginWindow.this.dispose();
        } else {
            Exception e = model.getLoginException();
            if (e != null) {
                Controller.getInstance().getView().displayErrorMessage("Invalid Log-in", CommonUtils.getRootCause(e).getMessage());
            }
        }
    }
}
