package org.wcb.autohome;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import javax.swing.BoxLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import org.wcb.autohome.util.Item;
import org.wcb.autohome.util.DeviceIDRenderer;
import org.wcb.autohome.interfaces.X10DeviceConstants;
import org.wcb.autohome.interfaces.IX10Events;
import org.wcb.autohome.interfaces.I18nConstants;
import org.wcb.autohome.interfaces.IX10Module;
import org.wcb.autohome.implementations.X10Events;
import org.wcb.autohome.implementations.X10Module;
import org.wcb.common.JTimeDialog;

/**
 * * Project: Alice X10 Home Automation<BR>
 * Filename:  $Id: EventsDetailPanel.java,v 1.26 2004/07/22 03:06:49 wbogaardt Exp $<BR>
 * Abstract: Used to display detail information on the events panel and run the
 *           events daemon
 *
 * $Log: EventsDetailPanel.java,v $
 * Revision 1.26  2004/07/22 03:06:49  wbogaardt
 * removed deprecated method calls.
 *
 * Revision 1.25  2004/02/28 06:06:28  wbogaardt
 * *** empty log message ***
 *
 * Revision 1.24  2004/02/06 23:27:12  wbogaardt
 * added clock label to internationalization, refactored menu bar from home center panel, fixed display of JTimeDialog so that it looks a bit better.
 *
 * Revision 1.23  2004/02/06 20:06:15  wbogaardt
 * replaced ampm drop boxes with time buttons which launch a time panel move menu items around on main panel
 *
 * Revision 1.22  2004/02/05 14:31:50  wbogaardt
 * modified to use time slider
 *
 * Revision 1.21  2004/02/01 19:31:52  wbogaardt
 * removed form layout references
 *
 * Revision 1.20  2004/01/19 17:54:16  wbogaardt
 * initial fixed monitor
 *
 * Revision 1.19  2004/01/17 07:21:15  wbogaardt
 * added serialization to run events and allow monitoring of these events to the file system to reload later
 *
 * Revision 1.18  2004/01/16 19:50:14  wbogaardt
 * refactored, fixed long standing bug with updating macro panels, add error notification to user
 * for improper device codes
 *
 * Revision 1.17  2004/01/16 00:53:34  wbogaardt
 * Fixed a very obscure bug with the Macro Panel that it didn't added new
 * x10 devices to the drop down of available x10 device for the macro. Modified Macro triggers to change the events
 * to integer verses strings cleaner this way.
 *
 * Revision 1.16  2004/01/15 21:05:17  wbogaardt
 * major revamp of Modules and interfaces changes overall structure of how information is stored
 *
 * Revision 1.15  2003/12/30 21:20:16  wbogaardt
 * added new file saving dialog to actually save files in .x10 extension.
 *
 * Revision 1.14  2003/12/30 18:47:40  wbogaardt
 * made labels so they are internationlized and fixed layout of trigger panel
 *
 * Revision 1.13  2003/12/22 20:51:29  wbogaardt
 * refactored name assignments and formatted code for readability.
 *
 * Revision 1.12  2003/12/20 21:32:38  wbogaardt
 * enabled new file menu option and added functionality
 *
 * Revision 1.10  2003/12/20 06:16:00  wbogaardt
 * moved most buttons text to i18n internationalization.
 *
 * Revision 1.9  2003/12/16 22:08:33  wbogaardt
 * refactored events daemon handeling
 *
 * Revision 1.8  2003/12/12 23:17:33  wbogaardt
 * javadoc comments refactored methods so they are more descriptive
 *
 * Revision 1.7  2003/12/11 23:10:07  wbogaardt
 * cleaned up exception handeling and logging of system.out messages
 *
 * Revision 1.6  2003/10/10 22:35:33  wbogaardt
 * fixed parsing of date information for am and pm values.
 *
 * Revision 1.5  2003/10/10 21:39:01  wbogaardt
 * modified macro triggers to use calendar in stead of strings
 *
 * Revision 1.4  2003/10/10 18:39:09  wbogaardt
 * changed date time information from a string to a calendar object
 *
 *
 *@author wbogaardt
 *@version 1.15
 */
public class EventsDetailPanel extends JPanel implements SwingConstants, X10DeviceConstants {

    private JButton jbAdd, jbUpdate, jbDelete;

    private JButton jbTime;

    private JTimeDialog dialog;

    private JButton okButton = new JButton("OK");

    private JToggleButton jtbExecute;

    private JToggleButton jtbSunday, jtbMonday, jtbTuesday, jtbWednesday, jtbThursday, jtbFriday, jtbSaturday;

    private JTextField descriptionTf, startTf;

    private JComboBox jcbStatus, jcbLightModuleAction, jcbInstalledModules;

    private JSlider jsLightController;

    private EventsPanel stepDad;

    private ImageIcon imgIconDisableDate = AutoHomeAdminSession.DAYOFF;

    private ImageIcon imgIconEnableDate = AutoHomeAdminSession.DAYON;

    private String[] sEventsArray = { "Off", "ON", "LightControl", "All Lights Off", "All Lights ON", "All Off" };

    private IX10Events x10Events = null;

    private static final SimpleDateFormat DATE_FORMAT_HMMA = new SimpleDateFormat("h:mm a");

    /**
     * Creates the Events detail panel with a reference
     * to the Main Events Panel.
     * @param modPanel The main events panel with the table selection model.
     */
    public EventsDetailPanel(EventsPanel modPanel) {
        stepDad = modPanel;
        setupComponents();
        setupListeners();
    }

    /**
     * Setup componets on this panel
     */
    private void setupComponents() {
        setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.DETAIL_LABEL)));
        jsLightController = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
        jsLightController.setPaintLabels(true);
        jsLightController.setMajorTickSpacing(20);
        jcbLightModuleAction = new JComboBox();
        jcbLightModuleAction.addItem("Dim");
        jcbLightModuleAction.addItem("Brighten");
        jsLightController.setEnabled(false);
        jcbLightModuleAction.setEnabled(false);
        startTf = new JTextField(6);
        startTf.setEditable(false);
        jbTime = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.CLOCK_LABEL));
        jtbSunday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.SUN_BUTTON), imgIconDisableDate);
        setupButton(jtbSunday);
        jtbMonday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.MON_BUTTON), imgIconDisableDate);
        setupButton(jtbMonday);
        jtbTuesday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.TUE_BUTTON), imgIconDisableDate);
        setupButton(jtbTuesday);
        jtbWednesday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.WED_BUTTON), imgIconDisableDate);
        setupButton(jtbWednesday);
        jtbThursday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.THU_BUTTON), imgIconDisableDate);
        setupButton(jtbThursday);
        jtbFriday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.FRI_BUTTON), imgIconDisableDate);
        setupButton(jtbFriday);
        jtbSaturday = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.SAT_BUTTON), imgIconDisableDate);
        setupButton(jtbSaturday);
        jbAdd = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.ADD_BUTTON));
        jbAdd.setToolTipText("Used to add new events");
        jbUpdate = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.UPDATE_BUTTON));
        jbUpdate.setToolTipText("Update a currently selected event");
        jbDelete = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.DELETE_BUTTON));
        jbDelete.setToolTipText("Delete a selected event");
        jtbExecute = new JToggleButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.RUN_BUTTON), imgIconDisableDate);
        jtbExecute.setToolTipText("Begin executing events");
        jtbExecute.setSelected(AutoHomeAdminSession.getInstance().isEventDaemonRunning());
        setupButton(jtbExecute);
        jcbStatus = new JComboBox(sEventsArray);
        descriptionTf = new JTextField(15);
        jcbInstalledModules = new JComboBox();
        updateDeviceID();
        JPanel execPanel = new JPanel();
        execPanel.setLayout(new BoxLayout(execPanel, BoxLayout.Y_AXIS));
        execPanel.add(jtbExecute);
        add(BorderLayout.NORTH, this.createTopPanel());
        add(BorderLayout.WEST, this.createWeeksPanel());
        add(BorderLayout.EAST, execPanel);
        add(BorderLayout.SOUTH, this.createLightControllerPanel());
    }

    /**
     * This panel creates the top description, add, delete, and update buttons
     * as well as the time for the event and the modules.
     * @return Completed panel of description details.
     */
    private JPanel createTopPanel() {
        java.awt.GridBagConstraints gridBagConstraints;
        JPanel jPanel2 = new javax.swing.JPanel();
        jPanel2.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        jPanel2.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.INSTALLED_MODULES_LABEL)), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jcbInstalledModules, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        jPanel2.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.DESCRIPTION_LABEL)), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel2.add(descriptionTf, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jbAdd, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.TIME_LABEL)), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(startTf, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jbTime, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.ACTION_LABEL)), gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(jcbStatus, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jbUpdate, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jbDelete, gridBagConstraints);
        return jPanel2;
    }

    /**
     * This panel displays the toggle buttons for the various days of the week.
     * @return Days of week panel
     */
    private JPanel createWeeksPanel() {
        JPanel jpDaysOfWeek = new JPanel();
        jpDaysOfWeek.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.WEEKDAY_LABEL)));
        jpDaysOfWeek.add(jtbSunday);
        jpDaysOfWeek.add(jtbMonday);
        jpDaysOfWeek.add(jtbTuesday);
        jpDaysOfWeek.add(jtbWednesday);
        jpDaysOfWeek.add(jtbThursday);
        jpDaysOfWeek.add(jtbFriday);
        jpDaysOfWeek.add(jtbSaturday);
        return jpDaysOfWeek;
    }

    /**
     * This creates the south panel which displays the light
     * dim and brighten slider as well as other actions for light based modules.
     * @return The light slider panel
     */
    private JPanel createLightControllerPanel() {
        JPanel lightPanel = new JPanel();
        lightPanel.setLayout(new BoxLayout(lightPanel, BoxLayout.X_AXIS));
        lightPanel.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.LIGHT_LABEL)));
        lightPanel.add(jcbLightModuleAction);
        lightPanel.add(jsLightController);
        return lightPanel;
    }

    /**
     * Pass in a toggle button and set its position
     * text postion to center and top location.
     * @param jtb the passed in toggle button to set information.
     */
    private void setupButton(JToggleButton jtb) {
        jtb.setSelectedIcon(imgIconEnableDate);
        jtb.setVerticalTextPosition(TOP);
        jtb.setHorizontalTextPosition(CENTER);
    }

    /**
     *Updates the drop down combo box with the list of available X10 modules
     *added in the ModulePanel.  We wanted this information to updated to subsequent
     *panels if we made changes to X10 devices so that they either become available
     *or unavailable for either this panel or others.
     */
    public void updateDeviceID() {
        jcbInstalledModules = AutoHomeAdminSession.getInstance().setRenderedModules(jcbInstalledModules);
        jcbInstalledModules.setRenderer(new DeviceIDRenderer());
    }

    /**
     * Updates this view with the selected row data
     * in this case it is a vector. The information
     * is appropriatly distributed to fill in the UI
     * detail information for the user.
     * @param detail The X10Events object interface to set components on panel to display its values.
     */
    public void updateView(IX10Events detail) {
        x10Events = detail;
        IX10Module iEventModule = x10Events.getModule();
        clearAllViews();
        if (iEventModule.getType() == LAMP_MODULE_ON) {
            jsLightController.setEnabled(true);
            jcbLightModuleAction.setEnabled(true);
            setupLightComponets(detail.getAction());
        }
        if (iEventModule.getType() == APPLIANCE_MODULE_ON) {
            jsLightController.setEnabled(false);
            jcbLightModuleAction.setEnabled(false);
            jsLightController.setValue(0);
        }
        for (int i = 0; i < jcbInstalledModules.getItemCount(); i++) {
            if (((jcbInstalledModules.getItemAt(i)).toString()).startsWith(iEventModule.getFullDeviceCode())) {
                jcbInstalledModules.setSelectedIndex(i);
            }
        }
        descriptionTf.setText(detail.getDescription());
        if (detail.getAction().equalsIgnoreCase(sEventsArray[1])) {
            jcbStatus.setSelectedIndex(1);
            jsLightController.setValue(100);
            jcbLightModuleAction.setSelectedIndex(0);
        }
        if (detail.getAction().equalsIgnoreCase(sEventsArray[0])) {
            jcbStatus.setSelectedIndex(0);
        }
        if (detail.getAction().equalsIgnoreCase(sEventsArray[3])) {
            jcbStatus.setSelectedIndex(3);
        }
        if (detail.getAction().equalsIgnoreCase(sEventsArray[4])) {
            jcbStatus.setSelectedIndex(4);
        }
        if (detail.getAction().equalsIgnoreCase(sEventsArray[5])) {
            jcbStatus.setSelectedIndex(5);
        }
        setupDaysofWeek(detail);
        startTf.setText(DATE_FORMAT_HMMA.format(detail.getTime().getTime()));
    }

    /**
     * If the text indicates its a light then set
     * the light slider bar to the value indicated.
     */
    private void setupLightComponets(String lightText) {
        if (lightText.startsWith("-")) {
            jcbLightModuleAction.setSelectedIndex(0);
        }
        if (lightText.startsWith("+")) {
            jcbLightModuleAction.setSelectedIndex(1);
        }
        try {
            jsLightController.setValue(Integer.parseInt(lightText.substring(1)));
        } catch (NumberFormatException err) {
            jsLightController.setValue(50);
        }
        jcbStatus.setSelectedIndex(2);
    }

    private void setupListeners() {
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Object src = evt.getSource();
                if (src == jbAdd) {
                    if (checkIsLightDevice()) {
                        try {
                            stepDad.addNewRow(saveDetailData());
                            clearAllViews();
                        } catch (Exception err) {
                        }
                    }
                }
                if (src == jbUpdate) {
                    if (checkIsLightDevice()) {
                        try {
                            stepDad.updateRow(saveDetailData());
                        } catch (Exception err) {
                        }
                    }
                }
                if (src == jbDelete) {
                    stepDad.deleteRow();
                    clearAllViews();
                }
                if (src == jtbExecute) {
                    if (AutoHomeAdminSession.getInstance().isX10GatwayConnected()) {
                        if (jtbExecute.isSelected()) {
                            if (!AutoHomeAdminSession.getInstance().isEventDaemonRunning()) {
                                stepDad.loadData();
                                AutoHomeAdminSession.getInstance().runEventsDaemon();
                            }
                        }
                        if (!jtbExecute.isSelected()) {
                            if (AutoHomeAdminSession.getInstance().isEventDaemonRunning()) {
                                AutoHomeAdminSession.getInstance().stopEventsDaemon();
                            }
                        }
                    } else {
                        showErrorMessage("ALICE is not currently connected\nto the X10 interface.");
                        jtbExecute.setSelected(false);
                    }
                }
                if (src == jbTime) {
                    GregorianCalendar time = new GregorianCalendar();
                    if (startTf.getText() != null) {
                        try {
                            time.setTime(DATE_FORMAT_HMMA.parse(startTf.getText()));
                        } catch (ParseException pe) {
                        }
                    }
                    dialog = new JTimeDialog(null, time, okButton);
                    dialog.setLocationRelativeTo(EventsDetailPanel.this);
                    dialog.setVisible(true);
                }
                if (src == jcbStatus) {
                    lightControllerCheck();
                }
                if (src == okButton) {
                    startTf.setText(dialog.getTime());
                    dialog.dispose();
                }
            }
        };
        okButton.addActionListener(al);
        jbAdd.addActionListener(al);
        jbUpdate.addActionListener(al);
        jbDelete.addActionListener(al);
        jtbExecute.addActionListener(al);
        jcbStatus.addActionListener(al);
        jbTime.addActionListener(al);
    }

    /**
     * Set all the toggle buttons to be unselected
     */
    private void clearAllViews() {
        jcbStatus.setSelectedIndex(0);
        descriptionTf.setText("");
        jtbSunday.setSelected(false);
        jtbMonday.setSelected(false);
        jtbTuesday.setSelected(false);
        jtbWednesday.setSelected(false);
        jtbThursday.setSelected(false);
        jtbFriday.setSelected(false);
        jtbSaturday.setSelected(false);
        jtbExecute.setSelected(AutoHomeAdminSession.getInstance().isEventDaemonRunning());
    }

    /**
     * Toggle the day of week button down
     * based on the string passed.
     * example string is --TWTF-
     */
    private void setupDaysofWeek(IX10Events x10evt) {
        jtbSunday.setSelected(x10evt.getSunday());
        jtbMonday.setSelected(x10evt.getMonday());
        jtbTuesday.setSelected(x10evt.getTuesday());
        jtbWednesday.setSelected(x10evt.getWednesday());
        jtbThursday.setSelected(x10evt.getThursday());
        jtbFriday.setSelected(x10evt.getFriday());
        jtbSaturday.setSelected(x10evt.getSaturday());
    }

    /**
     * Enable or disable the light slider based on
     * the module type selected.
     */
    private void lightControllerCheck() {
        Item selectItem = (Item) jcbInstalledModules.getSelectedItem();
        if (selectItem.getType() == LAMP_MODULE_ON) {
            jsLightController.setEnabled(true);
            jcbLightModuleAction.setEnabled(true);
        }
        if (selectItem.getType() == APPLIANCE_MODULE_ON) {
            jsLightController.setEnabled(false);
            jcbLightModuleAction.setEnabled(false);
            jsLightController.setValue(0);
        }
    }

    /**
     * If a Toggle button had be selected then
     * set its value to a day of the week otherwise
     * put in a - to indicate that week was not selected.
     * @param x10evt event Object to set the date values.
     */
    private void saveDaysOfWeek(X10Events x10evt) {
        x10evt.setSunday(jtbSunday.isSelected());
        x10evt.setMonday(jtbMonday.isSelected());
        x10evt.setTuesday(jtbTuesday.isSelected());
        x10evt.setWednesday(jtbWednesday.isSelected());
        x10evt.setThursday(jtbThursday.isSelected());
        x10evt.setFriday(jtbFriday.isSelected());
        x10evt.setSaturday(jtbSaturday.isSelected());
    }

    /**
     * With the information that the user entered
     * create a new X10Events object and pass its
     * interface.
     * @return the X10Events object with all values set.
     */
    private IX10Events saveDetailData() {
        X10Events returnEvents = new X10Events();
        Item type = (Item) jcbInstalledModules.getSelectedItem();
        try {
            int i = Integer.parseInt((type.toString()).substring(1));
            returnEvents.setDeviceModule(new X10Module(type.toString().charAt(0), i, "", "Event", type.getType()));
        } catch (NumberFormatException err) {
            returnEvents.setDeviceModule(new X10Module(type.toString().charAt(0), 1, "", "Event", type.getType()));
        }
        returnEvents.setDescription(descriptionTf.getText());
        if (jcbStatus.getSelectedIndex() == 0) {
            returnEvents.setAction(sEventsArray[0]);
        }
        if (jcbStatus.getSelectedIndex() == 1) {
            returnEvents.setAction(sEventsArray[1]);
        }
        if (jcbLightModuleAction.getSelectedIndex() == 0 && jcbStatus.getSelectedIndex() == 2) {
            returnEvents.setAction("-" + jsLightController.getValue());
        }
        if (jcbLightModuleAction.getSelectedIndex() == 1 && jcbStatus.getSelectedIndex() == 2) {
            returnEvents.setAction("+" + jsLightController.getValue());
        }
        if (jcbStatus.getSelectedIndex() == 3) {
            returnEvents.setAction(sEventsArray[3]);
        }
        if (jcbStatus.getSelectedIndex() == 4) {
            returnEvents.setAction(sEventsArray[4]);
        }
        if (jcbStatus.getSelectedIndex() == 5) {
            returnEvents.setAction(sEventsArray[5]);
        }
        saveDaysOfWeek(returnEvents);
        try {
            Calendar timestamp = new GregorianCalendar();
            timestamp.setTime(DATE_FORMAT_HMMA.parse(startTf.getText()));
            returnEvents.setTime(timestamp);
        } catch (ParseException pe) {
            returnEvents.setTime(new GregorianCalendar());
        }
        return returnEvents;
    }

    /**
     * Do a check on the item selected and if its an appliance
     * module selected and they try to use light event commands
     * then warn the user of this issue.
     * @return false if it is not a light module.
     */
    private boolean checkIsLightDevice() {
        Item item = (Item) jcbInstalledModules.getSelectedItem();
        if (item.getType() == APPLIANCE_MODULE_ON && (jcbStatus.getSelectedIndex() == 3 || jcbStatus.getSelectedIndex() == 4)) {
            JOptionPane.showMessageDialog(null, "For appliance modules you cannot select\nlight action events.", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void showErrorMessage(String errorString) {
        JOptionPane.showMessageDialog(null, errorString, "Failed Information", JOptionPane.ERROR_MESSAGE);
    }
}
