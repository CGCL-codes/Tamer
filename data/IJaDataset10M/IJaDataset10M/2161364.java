package com.yellowninja.backup.ui;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.ValidationResultModel;
import com.jgoodies.validation.view.ValidationResultViewFactory;
import com.jgoodies.validation.util.DefaultValidationResultModel;
import com.jgoodies.validation.util.ValidationUtils;
import com.yellowninja.backup.util.Backup;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

/**
 * Allows a user to create a new backup object.
 * 
 * @author Devin Gillman
 * 
 */
public class BackupDialog extends JDialog implements ActionListener {

    static final long serialVersionUID = 1L;

    public static final int OK = 0;

    public static final int CANCEL = 1;

    public int result, id;

    private JFileChooser saveLocationChooser, backupLocationChooser;

    private JTextField nameField, backupLocationField, saveLocationField;

    private JRadioButton fullBackupButton, incrementalBackupButton;

    private JComboBox scheduleCombo;

    private ValidationResultModel validationResult;

    private String[] backupNames;

    private boolean editing;

    public BackupDialog(JFrame parent, String[] backupNames) {
        super(parent);
        setTitle("New Backup");
        editing = false;
        id = -1;
        this.backupNames = backupNames;
        this.result = CANCEL;
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        this.setModal(true);
        this.setResizable(false);
        CellConstraints cc = new CellConstraints();
        FormLayout layout = new FormLayout("f:p, 3dlu, f:p:g, 3dlu, f:p", "p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p, 3dlu, p");
        PanelBuilder panel = new PanelBuilder(layout);
        panel.setDefaultDialogBorder();
        JLabel nameLabel = new JLabel("Name");
        nameLabel.setToolTipText("The name field is a way to identify one backup from another.");
        panel.add(nameLabel, cc.xy(1, 1));
        nameField = new JTextField(10);
        panel.add(nameField, cc.xyw(3, 1, 3));
        JLabel backupLocationLabel = new JLabel("Backup Location");
        backupLocationLabel.setToolTipText("The backup location field is the field that conatins the path to the directory that should be backed up.");
        panel.add(backupLocationLabel, cc.xy(1, 3));
        backupLocationField = new JTextField(20);
        panel.add(backupLocationField, cc.xy(3, 3));
        JButton backupLocationButton = new JButton("Browse");
        backupLocationButton.setIcon(new ImageIcon(this.getClass().getResource("/com/yellowninja/backup/images/folder.png")));
        backupLocationButton.getAccessibleContext().setAccessibleDescription("Opens a file dialog so that you can browse to the location to backup.");
        backupLocationButton.setActionCommand("B_BROWSE");
        backupLocationButton.addActionListener(this);
        panel.add(backupLocationButton, cc.xy(5, 3));
        JLabel saveLocationLabel = new JLabel("Save Location");
        saveLocationLabel.setToolTipText("The save location is the field that contains the path to the directory where the backup should be saved.");
        panel.add(saveLocationLabel, cc.xy(1, 5));
        saveLocationField = new JTextField(20);
        panel.add(saveLocationField, cc.xy(3, 5));
        JButton saveLocationButton = new JButton("Browse");
        saveLocationButton.setIcon(new ImageIcon(this.getClass().getResource("/com/yellowninja/backup/images/folder.png")));
        saveLocationButton.getAccessibleContext().setAccessibleDescription("Opens a file dialog so that you can browse to the location to save the backup.");
        saveLocationButton.setActionCommand("S_BROWSE");
        saveLocationButton.addActionListener(this);
        panel.add(saveLocationButton, cc.xy(5, 5));
        JLabel typeLabel = new JLabel("Backup Type:");
        typeLabel.setToolTipText("Full backup backs up everything, everytime it is run. Incremental backups up only the changes.");
        panel.add(typeLabel, cc.xy(1, 7));
        fullBackupButton = new JRadioButton("Full");
        fullBackupButton.setMnemonic(KeyEvent.VK_F);
        fullBackupButton.setSelected(true);
        incrementalBackupButton = new JRadioButton("Incremental");
        incrementalBackupButton.setMnemonic(KeyEvent.VK_I);
        ButtonGroup group = new ButtonGroup();
        group.add(fullBackupButton);
        group.add(incrementalBackupButton);
        panel.add(createRadioButtonPanel(group), cc.xyw(3, 7, 3));
        JLabel scheduleLabel = new JLabel("Schedule:");
        scheduleLabel.setToolTipText("Sets the time between when each backup should be run.");
        panel.add(scheduleLabel, cc.xy(1, 9));
        String[] schedules = { "Daily", "Weekly" };
        scheduleCombo = new JComboBox(schedules);
        panel.add(scheduleCombo, cc.xyw(3, 9, 3));
        validationResult = new DefaultValidationResultModel();
        panel.add(ValidationResultViewFactory.createReportIconAndTextPane(validationResult), cc.xyw(1, 11, 5));
        panel.addSeparator("", cc.xyw(1, 13, 5));
        JButton okButton = new JButton("OK");
        okButton.setIcon(new ImageIcon(this.getClass().getResource("/com/yellowninja/backup/images/accept.png")));
        okButton.getAccessibleContext().setAccessibleDescription("Creates the new backup package.");
        okButton.setActionCommand("OK");
        okButton.addActionListener(this);
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setIcon(new ImageIcon(this.getClass().getResource("/com/yellowninja/backup/images/cancel.png")));
        cancelButton.getAccessibleContext().setAccessibleDescription("Cancels the new backup package, returning to the main screen without changes.");
        cancelButton.setActionCommand("CANCEL");
        cancelButton.addActionListener(this);
        panel.add(ButtonBarFactory.buildRightAlignedBar(okButton, cancelButton), cc.xyw(1, 15, 5));
        this.add(panel.getPanel());
        this.pack();
        backupLocationChooser = new JFileChooser();
        backupLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        saveLocationChooser = new JFileChooser();
        saveLocationChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    }

    public int getResult() {
        return this.result;
    }

    public Backup getBackup() {
        String value = (String) scheduleCombo.getSelectedItem();
        int schedule = 0;
        if (value.equals("Daily")) {
            schedule = 1;
        } else if (value.equals("Weekly")) {
            schedule = 7;
        }
        if (fullBackupButton.isSelected()) return new Backup(id, nameField.getText(), backupLocationField.getText(), saveLocationField.getText(), Backup.COMPLETE, schedule); else return new Backup(id, nameField.getText(), backupLocationField.getText(), saveLocationField.getText(), Backup.INCREMENTAL, schedule);
    }

    public void setBackup(Backup backup) {
        this.id = backup.getID();
        nameField.setText(backup.getName());
        backupLocationField.setText(backup.getBackupLocation());
        saveLocationField.setText(backup.getSaveLocation());
        validationResult.setResult(new ValidationResult());
        this.getRootPane().updateUI();
    }

    public void clearDialog() {
        nameField.setText("");
        backupLocationField.setText("");
        saveLocationField.setText("");
        validationResult.setResult(new ValidationResult());
        this.getRootPane().updateUI();
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("OK")) {
            check();
            if (!validationResult.hasErrors()) {
                this.result = OK;
                this.setVisible(false);
            } else {
                this.pack();
            }
        } else if (ae.getActionCommand().equals("CANCEL")) {
            this.result = CANCEL;
            this.setVisible(false);
            this.clearDialog();
        } else if (ae.getActionCommand().equals("B_BROWSE")) {
            int result = backupLocationChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                backupLocationField.setText(backupLocationChooser.getSelectedFile().getAbsolutePath());
            }
        } else if (ae.getActionCommand().equals("S_BROWSE")) {
            int result = saveLocationChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                saveLocationField.setText(saveLocationChooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    private void check() {
        ValidationResult result = new ValidationResult();
        if (ValidationUtils.isBlank(nameField.getText())) result.addError("The name field is mandatory, please enter a unique name."); else {
            for (int i = 0; i < backupNames.length; i++) {
                if (backupNames[i].equalsIgnoreCase(nameField.getText())) {
                    result.addError("A backup already exists with that name. Please enter a different backup name.");
                    break;
                }
            }
        }
        if (ValidationUtils.isBlank(backupLocationField.getText())) result.addError("The backup location field is mandatory, please enter a valid directory path."); else if (!(new File(backupLocationField.getText())).exists()) result.addError("The backup location does not exist, please choose a different location."); else if (!(new File(backupLocationField.getText())).canRead()) result.addError("Can not write to the backup location, please choose another location that can be read from.");
        if (ValidationUtils.isBlank(saveLocationField.getText())) result.addError("The save location field is mandatory, please enter a valid directory to save backups too."); else if (!(new File(saveLocationField.getText())).exists()) result.addError("The save location does not exist, please choose a different location."); else if (!(new File(saveLocationField.getText())).canWrite()) result.addError("Can not write to the save location, please choose another location that can be written too."); else if ((new File(saveLocationField.getText() + File.separator + nameField.getText())).exists()) {
            result.addError("The backup location already has a directory named " + nameField.getText() + ". Please\nenter a different backup name or remove the directory.");
        }
        validationResult.setResult(result);
    }

    private JPanel createRadioButtonPanel(ButtonGroup group) {
        CellConstraints cc = new CellConstraints();
        String columns = "";
        for (int i = 0; i < group.getButtonCount(); i++) {
            columns += "l:p:g";
            if (i < (group.getButtonCount() - 1)) columns += ", 3dlu, ";
        }
        FormLayout layout = new FormLayout(columns, "p");
        PanelBuilder panel = new PanelBuilder(layout);
        Enumeration<AbstractButton> buttons = group.getElements();
        int i = 1;
        while (buttons.hasMoreElements()) {
            panel.add(buttons.nextElement(), cc.xy(i, 1));
            i += 2;
        }
        return panel.getPanel();
    }

    public void setEditing(boolean editing) {
        if (editing) setTitle("Edit Backup"); else setTitle("New Backup");
        this.editing = editing;
    }

    public boolean isEditing() {
        return editing;
    }
}
