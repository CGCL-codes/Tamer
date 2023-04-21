package com.explosion.datastream;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import com.explosion.datastream.gui.ConnectPanel;
import com.explosion.expf.Application;
import com.explosion.expf.ExpActionListener;
import com.explosion.expf.ExpFrame;
import com.explosion.expf.ExpInternalFrame;
import com.explosion.expfmodules.rdbmsconn.RdbmsConnConstants;
import com.explosion.expfmodules.rdbmsconn.RdbmsConnModuleManager;
import com.explosion.expfmodules.rdbmsconn.connect.ConnectionManager;
import com.explosion.expfmodules.wizard.Wizard;
import com.explosion.expfmodules.wizard.standard.load.WizardDefinitionLoader;
import com.explosion.expfmodules.wizard.standard.view.WizardBasePanel;
import com.explosion.utilities.GeneralUtils;
import com.explosion.utilities.preferences.groups.PreferenceGroup;
import com.explosion.utilities.preferences.groups.PreferenceGroupManager;
import com.explosion.utilities.preferences.groups.dialogs.PreferenceGroupEditDialog;
import com.explosion.utilities.preferences.groups.dialogs.PreferenceGroupNameEditDialog;
import com.explosion.utilities.preferences.groups.dialogs.PreferenceGroupsEditorDialog;

/**
 * @author Stephen Cowx
 *  
 */
public class EXQLListener implements ExpActionListener {

    private static Logger log = LogManager.getLogger(EXQLListener.class);

    private HashMap map;

    private PreferenceGroupsEditorDialog connectionsDialog;

    private PreferenceGroupsEditorDialog driversDialog;

    /**
     * Constructor for EXQLListener.
     */
    public EXQLListener() {
        map = new HashMap();
        map.put(EXQLConstants.MENU_CONNECTIONS, EXQLConstants.MENU_CONNECTIONS);
        map.put(EXQLConstants.MENU_DRIVERS, EXQLConstants.MENU_DRIVERS);
        map.put(EXQLConstants.MENU_NEW_DRIVER, EXQLConstants.MENU_NEW_DRIVER);
        map.put(EXQLConstants.MENU_NEW_CONNECTION, EXQLConstants.MENU_NEW_CONNECTION);
        map.put(EXQLConstants.MENU_OPEN_CONNECTION, EXQLConstants.MENU_OPEN_CONNECTION);
    }

    /**
     * @see package
     *      com.explosion.expf.Interfaces.ExpActionListener#getListensFor()
     */
    public Map getListensFor() {
        return map;
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        try {
            log.debug("Handling event " + e.getActionCommand());
            if (e.getActionCommand().equals(EXQLConstants.MENU_CONNECTIONS)) {
                log.debug("Connections");
                connectionsDialog = new PreferenceGroupsEditorDialog(Application.getApplicationFrame(), "Connection definitions", true, RdbmsConnModuleManager.instance().getConnectionDescriptorManager());
                JButton testButton = new JButton("Test");
                testButton.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        testConnection();
                    }
                });
                connectionsDialog.addButton(testButton, true);
                JButton connectButton = new JButton("Connect");
                connectButton.addActionListener(new java.awt.event.ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                        try {
                            connect(connectionsDialog.getSelectedValue());
                            connectionsDialog.setVisible(false);
                        } catch (Exception ex) {
                            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while connecting to database.");
                        }
                    }
                });
                connectionsDialog.addButton(connectButton, true);
                connectionsDialog.getPersistentObjectList().addMouseListener(new java.awt.event.MouseAdapter() {

                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() == 2) {
                            try {
                                connect(connectionsDialog.getSelectedValue());
                                connectionsDialog.setVisible(false);
                            } catch (Exception ex) {
                                com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while connecting to database.");
                            }
                        }
                    }
                });
                connectionsDialog.setSize(RdbmsConnConstants.driversDialogDimension);
                GeneralUtils.centreWindowInParent(connectionsDialog);
                connectionsDialog.setVisible(true);
                EXQLModuleManager.instance().rebuildOpenMenu();
            } else if (e.getActionCommand().equals(EXQLConstants.MENU_DRIVERS)) {
                log.debug("Drivers");
                driversDialog = new PreferenceGroupsEditorDialog(Application.getApplicationFrame(), "JDBC Drivers", true, RdbmsConnModuleManager.instance().getDriverDescriptorManager());
                driversDialog.setSize(RdbmsConnConstants.driversDialogDimension);
                GeneralUtils.centreWindowInParent(driversDialog);
                driversDialog.setVisible(true);
            } else if (e.getActionCommand().equals(EXQLConstants.MENU_NEW_DRIVER)) {
                log.debug("New Driver");
                Frame frame = Application.getApplicationFrame();
                WizardDefinitionLoader loader = new WizardDefinitionLoader();
                InputStream inputStream = loader.getClass().getClassLoader().getResourceAsStream("com/explosion/expfmodules/rdbmsconn/connectwizard/NewDriverWizard.xml");
                Wizard wizard = loader.getWizard(new InputStreamReader(inputStream));
                wizard.start();
                JDialog dialog = new JDialog(frame, true);
                WizardBasePanel view = new WizardBasePanel(wizard, dialog);
                dialog.getContentPane().setLayout(new BorderLayout());
                dialog.getContentPane().add(view, BorderLayout.CENTER);
                dialog.setSize(new Dimension(600, 400));
                dialog.setTitle(wizard.getName());
                GeneralUtils.centreWindowInParent(dialog);
                dialog.setVisible(true);
            } else if (e.getActionCommand().equals(EXQLConstants.MENU_NEW_CONNECTION)) {
                log.debug("New connection");
                Frame frame = Application.getApplicationFrame();
                WizardDefinitionLoader loader = new WizardDefinitionLoader();
                InputStream inputStream = loader.getClass().getClassLoader().getResourceAsStream("com/explosion/expfmodules/rdbmsconn/connectwizard/DatabaseConnectionWizard.xml");
                Wizard wizard = loader.getWizard(new InputStreamReader(inputStream));
                wizard.start();
                JDialog dialog = new JDialog(frame, true);
                WizardBasePanel view = new WizardBasePanel(wizard, dialog);
                dialog.getContentPane().setLayout(new BorderLayout());
                dialog.getContentPane().add(view, BorderLayout.CENTER);
                dialog.setSize(new Dimension(600, 400));
                dialog.setTitle(wizard.getName());
                GeneralUtils.centreWindowInParent(dialog);
                dialog.setVisible(true);
                EXQLModuleManager.instance().rebuildOpenMenu();
            } else if (e.getActionCommand().equals(EXQLConstants.MENU_OPEN_CONNECTION)) {
                log.debug("Open connection");
                try {
                    String menuText = ((JMenuItem) e.getSource()).getText();
                    int indexOfDot = menuText.indexOf(".");
                    connect(menuText.substring(indexOfDot + 1).trim());
                } catch (Exception ex) {
                    com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while connecting to database.");
                }
            }
        } catch (Throwable ex) {
            log.debug("Exception");
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while handling event.");
        }
        log.debug("Done");
    }

    private void newPersistentDescriptor(PreferenceGroupManager objectManager, String title) throws Exception {
        Frame frame = Application.getApplicationFrame();
        PreferenceGroupNameEditDialog nameDialog = new PreferenceGroupNameEditDialog(frame, title, null, objectManager);
        nameDialog.setSize(PreferenceGroupNameEditDialog.nameEditorDimension);
        GeneralUtils.centreWindowInParent(nameDialog);
        nameDialog.setVisible(true);
        if (nameDialog.getObject() != null) {
            PreferenceGroupEditDialog editDialog = new PreferenceGroupEditDialog(frame, nameDialog.getObject().getIdentifier(), true);
            editDialog.loadPreferences(nameDialog.getObject());
            editDialog.setSize(PreferenceGroupEditDialog.editorDimension);
            try {
                GeneralUtils.centreWindowInParent(editDialog);
            } catch (Exception ex) {
            }
            editDialog.setVisible(true);
        }
    }

    private void newConnection(PreferenceGroupManager objectManager, String title) throws Exception {
        Frame frame = Application.getApplicationFrame();
        PreferenceGroupNameEditDialog nameDialog = new PreferenceGroupNameEditDialog(frame, title, null, objectManager);
        nameDialog.setSize(PreferenceGroupNameEditDialog.nameEditorDimension);
        GeneralUtils.centreWindowInParent(nameDialog);
        nameDialog.setVisible(true);
        if (nameDialog.getObject() != null) {
            final PreferenceGroupEditDialog editDialog = new PreferenceGroupEditDialog(frame, nameDialog.getObject().getIdentifier(), true);
            editDialog.loadPreferences(nameDialog.getObject());
            JButton connectButton = editDialog.getOKButton();
            connectButton.setText("Connect");
            connectButton.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        connect(editDialog.getPersistentDescriptor().getIdentifier());
                    } catch (Exception ex) {
                        com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(ex, "Exception caught while connecting to database.");
                    }
                }
            });
            editDialog.setSize(PreferenceGroupEditDialog.editorDimension);
            try {
                GeneralUtils.centreWindowInParent(editDialog);
            } catch (Exception ex) {
            }
            editDialog.setVisible(true);
        }
    }

    void testConnection() {
        PreferenceGroup descriptor = RdbmsConnModuleManager.instance().getConnectionDescriptorManager().getGroup(connectionsDialog.getSelectedValue());
        try {
            if (descriptor == null) throw new Exception("Connection '" + connectionsDialog.getSelectedValue() + "' not defined.");
            int connectionKey = ConnectionManager.getInstance().connect(descriptor, RdbmsConnModuleManager.instance().getDriverDescriptorManager());
            JOptionPane.showMessageDialog(Application.getApplicationFrame(), "Connected to '" + connectionsDialog.getSelectedValue() + "' successfully.");
            try {
                ConnectionManager.getInstance().getConnection(connectionKey).close();
            } catch (Exception e) {
            }
        } catch (Exception e) {
            com.explosion.utilities.exception.ExceptionManagerFactory.getExceptionManager().manageException(e, "Test for connection '" + connectionsDialog.getSelectedValue() + "' failed.");
        }
    }

    /**
     * Connects to a database
     * 
     * @param connectionDescriptorName
     * @throws Exception
     */
    void connect(String connectionDescriptorName) throws Exception {
        PreferenceGroup descriptor = RdbmsConnModuleManager.instance().getConnectionDescriptorManager().getGroup(connectionDescriptorName);
        if (descriptor == null) throw new Exception("Connection '" + connectionDescriptorName + "' not defined.");
        int heightOfIt = 90;
        int widthOfIt = 320;
        ConnectPanel connectPanel = new ConnectPanel();
        ExpInternalFrame frame = ((ExpFrame) Application.getApplicationFrame()).createPaletteFrame(connectPanel, new Dimension(widthOfIt, heightOfIt), "Connecting to " + connectionDescriptorName, false);
        connectPanel.connect(descriptor);
    }
}
