package core;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Dialog to choose the Device, Driver, BankNumber and PatchNumber of
 * a Patch. Only Devices, Drivers, Bank- and PatchNumbers are choosable,
 * which are supporting the Patch.
 * Is used for Reassign..., Store... and SendTo... a patch.
 * @author  Torsten Tittmann
 * @version $Id: DevDrvPatchSelector.java 1182 2011-12-04 22:07:24Z chriswareham $
 */
public class DevDrvPatchSelector extends JDialog {

    /** The last index in driver Combo Box. */
    private int driverNum;

    private int patchNum;

    private int bankNum;

    protected IPatch p;

    private byte[] sysex;

    private String patchString;

    private JLabel myLabel;

    private JComboBox deviceComboBox;

    protected JComboBox driverComboBox;

    protected JComboBox bankComboBox;

    protected JComboBox patchNumComboBox;

    public DevDrvPatchSelector(IPatch patch, String wintitle, String action) {
        super(PatchEdit.getInstance(), wintitle, true);
        p = patch;
        sysex = patch.getByteArray();
        patchString = patch.getPatchHeader();
        initDialog(action, false);
    }

    public DevDrvPatchSelector(IPatch patch, int banknum, int patchnum, String wintitle, String action) {
        super(PatchEdit.getInstance(), wintitle, true);
        p = patch;
        sysex = patch.getByteArray();
        patchString = patch.getPatchHeader();
        this.patchNum = patchnum;
        this.bankNum = banknum;
        initDialog(action, true);
    }

    private void initDialog(String action, boolean hasBPComboBox) {
        JPanel dialogPanel = new JPanel(new BorderLayout(5, 5));
        myLabel = new JLabel("Please select a Location to \"" + action + "\".", JLabel.CENTER);
        dialogPanel.add(myLabel, BorderLayout.NORTH);
        deviceComboBox = new JComboBox();
        deviceComboBox.addActionListener(new DeviceActionListener());
        driverComboBox = new JComboBox();
        if (hasBPComboBox) {
            driverComboBox.addActionListener(new DriverActionListener());
            bankComboBox = new JComboBox();
            patchNumComboBox = new JComboBox();
        }
        int nDriver = 0;
        for (int i = 0; i < AppConfig.deviceCount(); i++) {
            Device device = AppConfig.getDevice(i);
            boolean newDevice = true;
            for (int j = 0, m = 0; j < device.driverCount(); j++) {
                IDriver driver = device.getDriver(j);
                if ((driver.isSingleDriver() || driver.isBankDriver()) && (driver.supportsPatch(patchString, sysex))) {
                    if (newDevice) {
                        deviceComboBox.addItem(device);
                        newDevice = false;
                    }
                    if (p.getDriver() == driver) {
                        driverNum = m;
                        deviceComboBox.setSelectedIndex(deviceComboBox.getItemCount() - 1);
                    }
                    nDriver++;
                    m++;
                }
            }
        }
        deviceComboBox.setEnabled(deviceComboBox.getItemCount() > 1);
        JPanel labelPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        labelPanel.add(new JLabel("Device:", JLabel.LEFT));
        labelPanel.add(new JLabel("Driver:", JLabel.LEFT));
        if (hasBPComboBox) {
            labelPanel.add(new JLabel("Bank:", JLabel.LEFT));
            labelPanel.add(new JLabel("Patch:", JLabel.LEFT));
        }
        JPanel fieldPanel = new JPanel(new GridLayout(0, 1));
        fieldPanel.add(deviceComboBox);
        fieldPanel.add(driverComboBox);
        if (hasBPComboBox) {
            fieldPanel.add(bankComboBox);
            fieldPanel.add(patchNumComboBox);
        }
        JPanel comboPanel = new JPanel(new BorderLayout());
        comboPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        comboPanel.add(labelPanel, BorderLayout.CENTER);
        comboPanel.add(fieldPanel, BorderLayout.EAST);
        dialogPanel.add(comboPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton doit = new JButton(action);
        doit.addActionListener(new DoitActionListener());
        buttonPanel.add(doit);
        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                dispose();
            }
        });
        buttonPanel.add(cancel);
        getRootPane().setDefaultButton(doit);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
        getContentPane().add(dialogPanel);
        pack();
        Utility.centerWindow(this);
        if (nDriver > 0) {
            setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Oops, No driver was found, which support this patch! Nothing will happen", "Error while \"" + action + "\" a patch", JOptionPane.WARNING_MESSAGE);
            dispose();
        }
    }

    protected void doit() {
    }

    /**
     * Makes the actual work after pressing the 'Store' button
     */
    private class DoitActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            doit();
        }
    }

    /**
     * Repopulate the Driver ComboBox with valid drivers after a Device change
     */
    private class DeviceActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            driverComboBox.removeAllItems();
            Device device = (Device) deviceComboBox.getSelectedItem();
            int nDriver = 0;
            for (int i = 0; i < device.driverCount(); i++) {
                IDriver driver = device.getDriver(i);
                if ((driver.isSingleDriver() || driver.isBankDriver()) && driver.supportsPatch(patchString, sysex)) {
                    driverComboBox.addItem(driver);
                    nDriver++;
                }
            }
            driverComboBox.setSelectedIndex(Math.min(driverNum, nDriver - 1));
            driverComboBox.setEnabled(driverComboBox.getItemCount() > 1);
        }
    }

    /**
     * Repopulate the Bank/Patch ComboBox with valid entries after a Device/Driver change
     */
    private class DriverActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            IPatchDriver driver = (IPatchDriver) driverComboBox.getSelectedItem();
            bankComboBox.removeAllItems();
            patchNumComboBox.removeAllItems();
            if (driver != null) {
                String[] bankNumbers = driver.getBankNumbers();
                if (bankNumbers != null && bankNumbers.length > 1) {
                    for (int i = 0; i < bankNumbers.length; i++) {
                        bankComboBox.addItem(bankNumbers[i]);
                    }
                    bankComboBox.setSelectedIndex(Math.min(bankNum, bankComboBox.getItemCount() - 1));
                }
                if (driver.isSingleDriver()) {
                    String[] patchNumbers = getPatchNumbers(driver);
                    if (patchNumbers.length > 1) {
                        for (int i = 0; i < patchNumbers.length; i++) {
                            patchNumComboBox.addItem(patchNumbers[i]);
                        }
                        patchNumComboBox.setSelectedIndex(Math.min(patchNum, patchNumComboBox.getItemCount() - 1));
                    }
                }
            }
            bankComboBox.setEnabled(bankComboBox.getItemCount() > 1);
            patchNumComboBox.setEnabled(patchNumComboBox.getItemCount() > 1);
        }
    }

    /**
     * This method returns the list of patch numbers, which may change according
     * to the dialog type (some have patch locations to which you can send but
     * not store)
     */
    protected String[] getPatchNumbers(IPatchDriver driver) {
        return driver.getPatchNumbers();
    }
}
