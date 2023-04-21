package com.streamsicle.gui;

import javax.swing.*;
import java.io.*;
import java.util.*;
import java.net.*;
import java.awt.Frame;
import com.streamsicle.util.JDirectoryChooser;
import org.apache.log4j.Category;

/**
 * Class responsible for running the configuration wizard.
 *
 * @author John Watkinson
 */
public class ConfigurationWizard {

    public static final int NUMBER_OF_STEPS = 4;

    public static final int STEP_DIRECTORY = 0;

    public static final int STEP_HOSTNAME = 1;

    public static final int STEP_MESSAGE = 2;

    public static final int STEP_PASSWORD = 3;

    public static final int STEP_COMPLETE = 4;

    public static final String DEFAULT_MESSAGE = "STREAMSICLE - - - take a lick - - -";

    public static final String[] STEP_TITLES = { "Choose MP3 Directory", "Select Server Name", "Create Outgoing Message", "Choose Admin Password", "Configuration Complete" };

    public static final String[] STEP_DESCRIPTIONS = { "Click \"Next...\" to select the top-level directory that contains your music files. All MP3 files in this directory and in subdirectories of this directory will be available to your listeners.", "Choose the hostname of your machine that users will use to connect to your server. If in doubt, just use the default setting. This is the hostname that users will use to connect your server.", "Choose the outgoing message of the server. This will appear in your listeners' media players.", "Choose the admin password. It is not a high-security password, but is used to carry out administrative tasks on your server.", "Configuration is complete." };

    private static Category log = Category.getInstance(ConfigurationWizard.class);

    /**
    * Indicates whether this is the first time the configuration wizard has
    * been run.
    */
    private boolean firstTime;

    /**
    * The owner of the wizard if there is one.
    */
    private Frame owner = null;

    /**
    * The directory with MP3 files.
    */
    private File dir;

    /**
    * The hostname of the server.
    */
    private String host;

    /**
    * The outgoing message of the server.
    */
    private String message;

    /**
    * The admin password of the server.
    */
    private String password;

    /**
    * The location of the config file.
    */
    private String configFile;

    /**
    * Prepares a configuration wizard to be run for the first time.
    */
    public ConfigurationWizard(String configFile) {
        this.configFile = configFile;
        firstTime = true;
        dir = null;
        host = null;
        message = null;
    }

    /**
    * Prepares a configuration to be run for an additional time.
    */
    public ConfigurationWizard(String configFile, Frame owner, File dir, String host, String message, String password) {
        this.configFile = configFile;
        this.owner = owner;
        firstTime = false;
        this.dir = dir;
        this.host = host;
        this.message = message;
        this.password = password;
    }

    public boolean run() throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (firstTime) {
            JOptionPane.showMessageDialog(null, "Welcome to Streamsicle! The following wizard will help you configure your Streamsicle server.", "Configure Streamsicle", JOptionPane.INFORMATION_MESSAGE);
        }
        for (int i = 0; i < NUMBER_OF_STEPS; i++) {
            boolean success = true;
            switch(i) {
                case STEP_DIRECTORY:
                    {
                        WizardDialog stepDialog = new WizardDialog(owner, STEP_TITLES[i], STEP_DESCRIPTIONS[i]) {

                            public void setData() {
                                JDirectoryChooser chooser;
                                if (dir != null) {
                                    chooser = new JDirectoryChooser(owner, "Choose Directory Containing MP3s", dir);
                                } else {
                                    chooser = new JDirectoryChooser(owner, "Choose Directory Containing MP3s");
                                }
                                data = chooser.getDirectory();
                            }
                        };
                        dir = (File) stepDialog.getData();
                        if (dir == null) {
                            log.warn("Directory specified was null.");
                            success = false;
                        }
                    }
                    break;
                case STEP_HOSTNAME:
                    {
                        InetAddress localhost = null;
                        try {
                            localhost = InetAddress.getLocalHost();
                        } catch (UnknownHostException e) {
                            e.printStackTrace();
                        }
                        InetAddress[] addies = InetAddress.getAllByName(localhost.getHostName());
                        Vector hostList = new Vector();
                        for (int j = 0; j < addies.length; j++) {
                            String hostname = addies[j].getHostName();
                            String address = addies[j].getHostAddress();
                            for (int k = 0; k < hostList.size(); k++) {
                                String h = (String) hostList.elementAt(k);
                                if (h.equals(host)) {
                                    hostname = null;
                                } else if (h.equals(address)) {
                                    address = null;
                                }
                            }
                            if (hostname != null) {
                                hostList.addElement(hostname);
                            }
                            if (address != null) {
                                hostList.addElement(address);
                            }
                        }
                        final JComboBox hosts = new JComboBox(hostList);
                        hosts.setEditable(true);
                        hosts.setSelectedItem(hostList.elementAt(0));
                        WizardDialog hostDialog = new WizardDialog(owner, STEP_TITLES[i], STEP_DESCRIPTIONS[i]) {

                            private JComboBox hostBox = hosts;

                            public void setData() {
                                data = hostBox.getSelectedItem();
                            }
                        };
                        hostDialog.setContent(hosts);
                        host = (String) hostDialog.getData();
                        if (host == null) {
                            log.warn("Host specified was null.");
                            success = false;
                        }
                    }
                    break;
                case STEP_MESSAGE:
                    {
                        final JTextField textField;
                        if (message == null) {
                            textField = new JTextField(DEFAULT_MESSAGE);
                        } else {
                            textField = new JTextField(message);
                        }
                        WizardDialog messageDialog = new WizardDialog(owner, STEP_TITLES[i], STEP_DESCRIPTIONS[i]) {

                            private JTextField messageField = textField;

                            public void setData() {
                                data = messageField.getText();
                            }
                        };
                        messageDialog.setContent(textField);
                        message = (String) messageDialog.getData();
                        if (message == null) {
                            log.warn("Outgoing server message was null.");
                            success = false;
                        }
                    }
                    break;
                case STEP_PASSWORD:
                    {
                        final JTextField passField;
                        if (password == null) {
                            passField = new JTextField(20);
                        } else {
                            passField = new JTextField(password, 20);
                        }
                        WizardDialog passwordDialog = new WizardDialog(owner, STEP_TITLES[i], STEP_DESCRIPTIONS[i]) {

                            private JTextField messageField = passField;

                            public void setData() {
                                data = messageField.getText();
                            }
                        };
                        passwordDialog.setContent(passField);
                        password = (String) passwordDialog.getData();
                        if (password == null) {
                            log.warn("Password was null.");
                            success = false;
                        }
                    }
                    break;
                case STEP_COMPLETE:
                    {
                        WizardDialog completeDialog = new WizardDialog(owner, STEP_TITLES[i], STEP_DESCRIPTIONS[i]);
                        JLabel finalLabel;
                        if (firstTime) {
                            finalLabel = new JLabel("Enjoy Streamsicle!");
                        } else {
                            finalLabel = new JLabel("The server will now reload its index of MP3s.");
                        }
                        completeDialog.setContent(finalLabel);
                        Object result = completeDialog.getData();
                        if (result == null) {
                            log.warn("Concluding dialog somehow failed!");
                            success = false;
                        }
                    }
                    break;
                default:
                    throw new Exception("Wizard error!");
            }
            if (!success) {
                log.info("Cancelled");
                if (firstTime) {
                    JOptionPane.showMessageDialog(null, "Streamsicle will shut down-- you must configure the server before you can run it.", "Configuration Cancelled", JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                } else {
                    return false;
                }
            }
        }
        log.info("Result: ");
        log.info("  Directory: " + dir);
        log.info("  Hostname: " + host);
        log.info("  Message: " + message);
        log.info("  Password: " + password);
        Properties props = new Properties();
        try {
            FileInputStream propsFile = new FileInputStream(configFile);
            props.load(propsFile);
            propsFile.close();
            props.put("streamsicle.playdir", "" + dir);
            props.put("streamsicle.host", host);
            props.put("streamsicle.description", message);
            props.put("streamsicle.adminPassword", password);
            FileOutputStream propsOut = new FileOutputStream(configFile);
            props.save(propsOut, "Automatically generated by Streamsicle at " + new Date());
            propsOut.close();
        } catch (IOException e) {
            log.error("Problem messing with the config file: " + e);
        }
        return true;
    }

    public static void main(String[] args) {
        String s = new String("");
        try {
            new ConfigurationWizard("test.conf").run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
