package org.wcb.autohome;

import org.wcb.autohome.interfaces.X10DeviceConstants;
import org.wcb.autohome.interfaces.MessageInterface;
import org.wcb.autohome.interfaces.I18nConstants;
import org.wcb.autohome.exceptions.HomeException;
import org.wcb.autohome.implementations.SerialPortBean;
import org.wcb.common.UIEditorDialog;
import org.wcb.common.GuiLib;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Copyright (C) 1999  Walter Bogaardt
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *
 * Project: Alice X10 Home Automation
 *
 *  GUI element that holds the user changable elements for connection
 *  configuration.
 *
 * Date: Oct 9, 2003
 * Time: 5:22:14 PM
 *
 *  $Log: SerialConfigurationPanel.java,v $
 *  Revision 1.16  2004/07/22 02:38:37  wbogaardt
 *  fixed serial panel configuration launch.
 *
 *  Revision 1.15  2004/06/09 18:52:34  wbogaardt
 *  changed enum to enumr to avoid java keyword issue
 *
 *  Revision 1.14  2004/02/03 21:02:24  wbogaardt
 *  moved DeviceFactory away from rmi creation and simplified interface between gateway
 *
 *  Revision 1.13  2004/02/03 00:35:06  wbogaardt
 *  reogranized files so that serial port settings are maintained in a bean to interface with config file
 *
 *  Revision 1.12  2004/02/01 20:03:20  wbogaardt
 *  removed reference to the Form layout and added javadoc comments to privat methods
 *
 *  Revision 1.11  2003/12/31 01:09:37  wbogaardt
 *  removed dead string reference to jhome.prop
 *
 *  Revision 1.10  2003/12/30 18:47:40  wbogaardt
 *  made labels so they are internationlized and fixed layout of trigger panel
 *
 *  Revision 1.9  2003/12/22 20:51:29  wbogaardt
 *  refactored name assignments and formatted code for readability.
 *
 *  Revision 1.8  2003/12/20 20:13:01  wbogaardt
 *  modified formating and some names for labels
 *
 *  Revision 1.7  2003/12/20 06:16:00  wbogaardt
 *  moved most buttons text to i18n internationalization.
 *
 *  Revision 1.6  2003/12/18 00:02:50  wbogaardt
 *  more javadoc comments
 *
 *  Revision 1.5  2003/12/17 21:27:06  wbogaardt
 *  Improved error traping of CM11A send command to all house
 *
 *  Revision 1.4  2003/12/12 23:17:33  wbogaardt
 *  javadoc comments refactored methods so they are more descriptive
 *
 *  Revision 1.3  2003/12/11 23:10:07  wbogaardt
 *  cleaned up exception handeling and logging of system.out messages
 *
 *  Revision 1.2  2003/10/11 05:06:52  wbogaardt
 *  modified connect and disconnect button to be one button for simplification of ui
 *
 *  Revision 1.1  2003/10/10 00:50:43  wbogaardt
 *  decoupled the ConfigurationPanel to SerialConfigurationPanel
 *
 */
public class SerialConfigurationPanel extends JPanel implements X10DeviceConstants {

    private JComboBox interfaceCombo;

    private JButton jbPortConnection;

    private JButton jbConfigure;

    private String[] sDeviceTypeArray = { "CM11A", "CM17A" };

    private SerialPanel spSerialPanel;

    private MessageInterface msgInterface;

    /**
     * Creates and initilizes the configuration panel. The initial settings
     * are from the parameters object.
     * @param panel the panel instance where this will sit.
     */
    public SerialConfigurationPanel(SerialPanel panel) {
        msgInterface = AutoHomeAdminSession.getInstance().getMessageInterface();
        this.spSerialPanel = panel;
        jbConfigure = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.CONFIGURE_BUTTON));
        if (!AutoHomeAdminSession.getInstance().isX10GatwayConnected()) {
            jbPortConnection = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.CONNECT_BUTTON));
        } else {
            jbPortConnection = new JButton(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.DISCONNECT_BUTTON));
        }
        interfaceCombo = new JComboBox(sDeviceTypeArray);
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.SERIAL_PORT_TITLE_LABEL)));
        JPanel configPanel = new JPanel();
        configPanel.add(jbConfigure);
        configPanel.add(new JLabel(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.INTERFACE_TYPE_LABEL)));
        configPanel.add(interfaceCombo);
        configPanel.add(jbPortConnection);
        this.setupListeners();
        this.add(configPanel);
    }

    /**
     * mutator to set the combo box for port settings
     * to the value previously saved by the user
     */
    public void setPortValue() {
        if (AutoHomeAdminSession.getInstance().getX10GatewayType() == CM11A_TRANSMITTER) {
            interfaceCombo.setSelectedIndex(0);
            spSerialPanel.timePanelEnable(true);
        }
        if (AutoHomeAdminSession.getInstance().getX10GatewayType() == CM17A_TRANSMITTER) {
            interfaceCombo.setSelectedIndex(1);
            spSerialPanel.timePanelEnable(false);
        }
    }

    /**
     * Get the currently select interface name from the JComboBox
     * @return the integer value of the interface type.
     */
    public int getInterfaceType() {
        if (((String) interfaceCombo.getSelectedItem()).startsWith("CM11A")) {
            return CM11A_TRANSMITTER;
        } else {
            return CM17A_TRANSMITTER;
        }
    }

    /**
     * This returns and instance of this panel so that outside components
     * can have reference to this instance.
     * @return The instance of this panel.
     */
    private SerialConfigurationPanel getPanel() {
        return this;
    }

    /**
     * This setsup the action listeners to the buttons and drop downs
     * so that the serial port can be enabled based on the user's action.
     */
    private void setupListeners() {
        ActionListener al = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Object src = evt.getSource();
                if (src == jbPortConnection) {
                    if (AutoHomeAdminSession.getInstance().isX10GatwayConnected()) {
                        try {
                            printMessage("Closing port");
                            AutoHomeAdminSession.getInstance().closeSerialPort();
                            jbPortConnection.setText(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.CONNECT_BUTTON));
                        } catch (HomeException err) {
                            printMessage("Failed");
                            jbPortConnection.setText(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.CONNECT_BUTTON));
                            printMessage(err.toString());
                        }
                    } else {
                        if (connectToSerialPort()) {
                            spSerialPanel.setPort();
                            jbPortConnection.setText(AutoHomeAdminSession.getInstance().getI18n().getString(I18nConstants.DISCONNECT_BUTTON));
                        }
                    }
                }
                if (src == interfaceCombo) {
                    if (interfaceCombo.getSelectedIndex() == 0) {
                        spSerialPanel.timePanelEnable(true);
                    } else if (interfaceCombo.getSelectedIndex() == 1) {
                        spSerialPanel.timePanelEnable(false);
                    }
                }
                if (src == jbConfigure) {
                    Frame fr = GuiLib.getFrameForComponent(getPanel());
                    UIEditorDialog editor;
                    if ((fr != null) && (fr instanceof JFrame)) {
                        JComponent rootC = ((JFrame) fr).getRootPane();
                        editor = new UIEditorDialog(rootC);
                        editor.setTitle("A.L.I.C.E Global Settings");
                    } else {
                        new UIEditorDialog();
                    }
                    editor = new UIEditorDialog();
                    editor.setLocationRelativeTo(SerialConfigurationPanel.this);
                    editor.setVisible(true);
                }
            }
        };
        jbPortConnection.addActionListener(al);
        jbConfigure.addActionListener(al);
        interfaceCombo.addActionListener(al);
    }

    /**
     * Connects the session object to the serial port.
     *
     * @return true indicates success.
     */
    private boolean connectToSerialPort() {
        SerialPortBean serialBean = AutoHomeAdminSession.getInstance().getSerialPort();
        if (serialBean.getPort() != null) {
            try {
                AutoHomeAdminSession.getInstance().connectPortToDevice(this.getInterfaceType());
            } catch (HomeException err) {
                JOptionPane.showMessageDialog(null, "There appears to be an issue with \n" + "your X10 interface. Check your port \n" + "settings in the Global Options Menu.", "Missing Port ID", JOptionPane.WARNING_MESSAGE);
                return false;
            }
        } else {
            JOptionPane.showMessageDialog(null, "There appears to be an issue with \n" + "your X10 interface. Check your port \n" + "settings in the Global Options Menu.", "Missing Port ID", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
      * sends text information to the application statatus bar
      * @param info the string to send to interface.
      */
    private void printMessage(String info) {
        if (msgInterface != null) msgInterface.printMessage(info);
    }
}
