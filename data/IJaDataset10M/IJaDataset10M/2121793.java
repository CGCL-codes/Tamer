package com.jgoodies.forms.tutorial.building;

import javax.swing.*;
import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;
import com.jgoodies.forms.debug.FormDebugUtils;
import com.jgoodies.forms.layout.FormLayout;

/**
 * Demonstrates how to find bugs in the layout using 
 * the {@link FormDebugPanel} and the {@link FormDebugUtils}.<p>
 * 
 * The example also demonstrates efficient panel building with 
 * the DefaultFormBuilder. The builder has been configured 
 * to use a leading indent column.
 *
 * @author Karsten Lentzsch
 * @version $Revision: 1.16 $
 */
public final class FormDebugExample {

    private JTextField fileNumberField;

    private JTextField rfqNumberField;

    private JTextField blNumberField;

    private JTextField mblNumberField;

    private JTextField customerKeyField;

    private JTextField customerAddressField;

    private JTextField shipperKeyField;

    private JTextField shipperAddressField;

    private JTextField consigneeKeyField;

    private JTextField consigneeAddressField;

    private JTextField departureCodeField;

    private JTextField departurePortField;

    private JTextField destinationCodeField;

    private JTextField destinationPortField;

    private JTextField deliveryDateField;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.jgoodies.looks.plastic.PlasticXPLookAndFeel");
        } catch (Exception e) {
        }
        JFrame frame = new JFrame();
        frame.setTitle("Forms Tutorial :: Debug a Form");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JComponent panel = new FormDebugExample().buildPanel();
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     *  Creates and intializes the UI components.
     */
    private void initComponents() {
        fileNumberField = new JTextField();
        rfqNumberField = new JTextField();
        blNumberField = new JTextField();
        mblNumberField = new JTextField();
        customerKeyField = new JTextField();
        customerAddressField = new JTextField();
        customerAddressField.setEditable(false);
        shipperKeyField = new JTextField();
        shipperAddressField = new JTextField();
        shipperAddressField.setEditable(false);
        consigneeKeyField = new JTextField();
        consigneeAddressField = new JTextField();
        consigneeAddressField.setEditable(false);
        departureCodeField = new JTextField();
        departurePortField = new JTextField();
        departurePortField.setEditable(false);
        destinationCodeField = new JTextField();
        destinationPortField = new JTextField();
        destinationPortField.setEditable(false);
        deliveryDateField = new JTextField();
    }

    /**
     * Builds the panel.
     * 
     * @return the built panel
     */
    public JComponent buildPanel() {
        initComponents();
        FormLayout layout = new FormLayout("12dlu, pref, 3dlu, max(45dlu;min), 2dlu, min, 2dlu, min, 2dlu, min");
        layout.setColumnGroups(new int[][] { { 4, 6, 8, 10 } });
        DefaultFormBuilder builder = new DefaultFormBuilder(layout, new FormDebugPanel());
        builder.setDefaultDialogBorder();
        builder.setLeadingColumnOffset(1);
        builder.appendSeparator("General");
        builder.append("File Number", fileNumberField, 7);
        builder.append("RFQ Number", rfqNumberField, 7);
        builder.append("BL/MBL", blNumberField, mblNumberField);
        builder.nextLine();
        builder.appendSeparator("Addresses");
        builder.append("Customer", customerKeyField, customerAddressField, 5);
        builder.append("Shipper", shipperKeyField, shipperAddressField, 5);
        builder.append("Consignee", consigneeKeyField, consigneeAddressField, 5);
        builder.appendSeparator("Transport");
        builder.append("Departure", departureCodeField, departurePortField, 5);
        builder.append("Destination", destinationCodeField, destinationPortField, 5);
        builder.append("Delivery Date", deliveryDateField);
        builder.nextLine();
        FormDebugUtils.dumpAll(builder.getPanel());
        return builder.getPanel();
    }
}
