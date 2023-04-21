package dioscuri.config;

import dioscuri.GUI;
import dioscuri.config.Emulator.Architecture.Modules.Fdc.Floppy;
import dioscuri.util.Utilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.math.BigInteger;

/**
 *
 * @author Bram Lohman
 * @author Bart Kiers
 */
@SuppressWarnings("serial")
public class FdcConfigDialog extends ConfigurationDialog {

    private JCheckBox enabledCheckBox;

    private JCheckBox insertedCheckBox;

    private JComboBox driveLetterComboxBox;

    private JComboBox diskFormatComboBox;

    private JCheckBox writeProtectedCheckBox;

    private JButton imageBrowseButton;

    private JFormattedTextField updateIntField;

    dioscuri.config.Emulator emuConfig;

    /**
     *
     * @param parent -
     */
    public FdcConfigDialog(GUI parent) {
        super(parent, "FDC Configuration", false, ModuleType.FDC);
    }

    /**
     * Read in params from XML.
     */
    @Override
    protected void readInParams() {
        emuConfig = parent.getEmuConfig();
        Floppy drive = emuConfig.getArchitecture().getModules().getFdc().getFloppy().get(0);
        Integer updateInt = emuConfig.getArchitecture().getModules().getFdc().getUpdateintervalmicrosecs().intValue();
        boolean enabled = drive.isEnabled();
        boolean inserted = drive.isInserted();
        String driveLetterIndex = drive.getDriveletter();
        String diskFormatIndex = drive.getDiskformat();
        boolean writeProtected = drive.isWriteprotected();
        String imageFormatPath = Utilities.resolvePathAsString(drive.getImagefilepath());
        this.updateIntField.setValue(updateInt);
        this.enabledCheckBox.setSelected(enabled);
        this.insertedCheckBox.setSelected(inserted);
        this.driveLetterComboxBox.setSelectedItem(driveLetterIndex);
        this.diskFormatComboBox.setSelectedItem(diskFormatIndex);
        this.writeProtectedCheckBox.setSelected(writeProtected);
        this.imageFilePathLabel.setText(imageFormatPath);
        this.selectedFile = new File(imageFormatPath);
    }

    /**
     * Initialise the panel for data entry.
     */
    @Override
    protected void initMainEntryPanel() {
        JLabel updateIntLabel = new JLabel("Update Interval");
        JLabel updateIntUnitLabel = new JLabel("microseconds");
        JLabel enabledLabel = new JLabel("Enabled");
        JLabel insertedLabel = new JLabel("Inserted");
        JLabel driveLetterLabel = new JLabel("Drive Letter");
        JLabel diskFormatLabel = new JLabel("Disk Format");
        JLabel writeProtectedLabel = new JLabel("Write Protected");
        JLabel imageFileLabel = new JLabel("Image File");
        this.populateControls();
        GridBagLayout gridbag = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        mainEntryPanel = new JPanel(gridbag);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 10, 0, 10);
        c.weightx = 1.0;
        c.gridwidth = 1;
        gridbag.setConstraints(updateIntLabel, c);
        mainEntryPanel.add(updateIntLabel);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(updateIntField, c);
        mainEntryPanel.add(updateIntField);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(updateIntUnitLabel, c);
        mainEntryPanel.add(updateIntUnitLabel);
        c.gridwidth = 1;
        gridbag.setConstraints(enabledLabel, c);
        mainEntryPanel.add(enabledLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(enabledCheckBox, c);
        mainEntryPanel.add(enabledCheckBox);
        c.gridwidth = 1;
        gridbag.setConstraints(insertedLabel, c);
        mainEntryPanel.add(insertedLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(insertedCheckBox, c);
        mainEntryPanel.add(insertedCheckBox);
        c.gridwidth = 1;
        gridbag.setConstraints(driveLetterLabel, c);
        mainEntryPanel.add(driveLetterLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(driveLetterComboxBox, c);
        mainEntryPanel.add(driveLetterComboxBox);
        c.gridwidth = 1;
        gridbag.setConstraints(diskFormatLabel, c);
        mainEntryPanel.add(diskFormatLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(diskFormatComboBox, c);
        mainEntryPanel.add(diskFormatComboBox);
        c.gridwidth = 1;
        gridbag.setConstraints(writeProtectedLabel, c);
        mainEntryPanel.add(writeProtectedLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(writeProtectedCheckBox, c);
        mainEntryPanel.add(writeProtectedCheckBox);
        c.gridwidth = 1;
        gridbag.setConstraints(imageFileLabel, c);
        mainEntryPanel.add(imageFileLabel);
        c.gridwidth = GridBagConstraints.RELATIVE;
        gridbag.setConstraints(imageFilePathLabel, c);
        mainEntryPanel.add(imageFilePathLabel);
        c.gridwidth = GridBagConstraints.REMAINDER;
        gridbag.setConstraints(imageBrowseButton, c);
        mainEntryPanel.add(imageBrowseButton);
    }

    /**
     * Initalise the GUI Controls.
     */
    private void populateControls() {
        imageFilePathLabel = new JTextField("");
        imageFilePathLabel.setEditable(false);
        updateIntField = new JFormattedTextField();
        updateIntField.setValue(0);
        updateIntField.setColumns(10);
        enabledCheckBox = new JCheckBox();
        enabledCheckBox.setSelected(true);
        insertedCheckBox = new JCheckBox();
        insertedCheckBox.setSelected(true);
        DefaultComboBoxModel driveLetterModel = new DefaultComboBoxModel();
        driveLetterModel.addElement("A");
        driveLetterModel.addElement("B");
        driveLetterComboxBox = new JComboBox(driveLetterModel);
        driveLetterComboxBox.setSelectedIndex(0);
        DefaultComboBoxModel diskFormatModel = new DefaultComboBoxModel();
        diskFormatModel.addElement("1.2M");
        diskFormatModel.addElement("1.44M");
        diskFormatModel.addElement("2.88M");
        diskFormatModel.addElement("160K");
        diskFormatModel.addElement("180K");
        diskFormatModel.addElement("320K");
        diskFormatModel.addElement("360K");
        diskFormatModel.addElement("720K");
        diskFormatComboBox = new JComboBox(diskFormatModel);
        diskFormatComboBox.setSelectedIndex(1);
        writeProtectedCheckBox = new JCheckBox();
        imageBrowseButton = new JButton("Browse");
        imageBrowseButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                launchFileChooser();
            }
        });
    }

    /**
     * Get the params from the GUI.
     * 
     * @return object array of params.
     */
    @Override
    protected Emulator getParamsFromGui() {
        Floppy drive = emuConfig.getArchitecture().getModules().getFdc().getFloppy().get(0);
        emuConfig.getArchitecture().getModules().getFdc().setUpdateintervalmicrosecs((BigInteger.valueOf(((Number) updateIntField.getValue()).intValue())));
        drive.setEnabled(enabledCheckBox.isSelected());
        drive.setInserted(insertedCheckBox.isSelected());
        drive.setDriveletter(driveLetterComboxBox.getSelectedItem().toString());
        drive.setDiskformat(diskFormatComboBox.getSelectedItem().toString());
        drive.setWriteprotected(writeProtectedCheckBox.isSelected());
        drive.setImagefilepath(selectedFile.getAbsoluteFile().toString());
        return emuConfig;
    }
}
