package BeoZip;

import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.beowurks.BeoCommon.BaseButton;
import com.beowurks.BeoCommon.Util;

public class SelectFilesChooser implements ActionListener {

    protected static final int ACCESSORY_NONE = 0;

    protected static final int ACCESSORY_QUICKZIP = 1;

    protected static final int ACCESSORY_EXTRACTION = 2;

    private static JFileChooser foChooser = new JFileChooser();

    private final MainFrame1 foMainFrame;

    private int fnTextFieldCount;

    private JTextField txtFileNameTextField1 = null;

    private final JPanel pnlAccessory1 = new JPanel();

    private final Box boxButtons1 = Box.createVerticalBox();

    private final JCheckBox chkIncludeSubFolders1 = new JCheckBox();

    private final JCheckBox chkSavePathInformation1 = new JCheckBox();

    private final BaseButton btnAddFiles1 = new BaseButton();

    private final BaseButton btnCancel1 = new BaseButton();

    private final JPanel pnlAccessory2 = new JPanel();

    private final JCheckBox chkSelectAllFiles1 = new JCheckBox();

    private final JCheckBox chkUsePathNames1 = new JCheckBox();

    private final JCheckBox chkOverwriteFiles1 = new JCheckBox();

    public SelectFilesChooser(final MainFrame1 toMainFrame) {
        this.foMainFrame = toMainFrame;
        this.findFileNameField();
        this.setupParameters();
        this.setupListeners();
        this.setupCheckBoxes();
        this.setupButtons();
        this.setupLayouts();
    }

    private void setupLayouts() {
        this.boxButtons1.add(this.btnAddFiles1, null);
        this.boxButtons1.add(Box.createVerticalStrut(10));
        this.boxButtons1.add(this.btnCancel1, null);
        this.pnlAccessory1.setLayout(new GridBagLayout());
        this.pnlAccessory1.add(this.chkIncludeSubFolders1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 4, 4));
        this.pnlAccessory1.add(this.chkSavePathInformation1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 20, 4), 4, 4));
        this.pnlAccessory1.add(this.boxButtons1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(20, 4, 4, 4), 4, 4));
        this.pnlAccessory2.setLayout(new GridBagLayout());
        this.pnlAccessory2.add(this.chkSelectAllFiles1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 4, 4));
        this.pnlAccessory2.add(this.chkUsePathNames1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 4, 4));
        this.pnlAccessory2.add(this.chkOverwriteFiles1, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(4, 4, 20, 4), 4, 4));
    }

    private void setupListeners() {
        this.btnAddFiles1.addActionListener(this);
        this.btnCancel1.addActionListener(this);
    }

    private void setupButtons() {
        this.btnAddFiles1.setText("Add File(s)");
        this.btnAddFiles1.setMnemonic('A');
        this.btnAddFiles1.setToolTipText("Add files through selection or by the wildcard pattern");
        this.btnCancel1.setText("Cancel");
        this.btnCancel1.setToolTipText("Cancel this operation");
    }

    private void setupCheckBoxes() {
        this.chkIncludeSubFolders1.setText("Include Sub-Folders");
        this.chkSavePathInformation1.setText("Save Path Information");
        this.chkSelectAllFiles1.setText("Select All Files");
        this.chkUsePathNames1.setText("Use Path Names");
        this.chkOverwriteFiles1.setText("Overwrite Existing Files");
    }

    private void setupParameters() {
        SelectFilesChooser.foChooser.setFileHidingEnabled(false);
        SelectFilesChooser.foChooser.setAcceptAllFileFilterUsed(false);
    }

    public SelectFileReturnValue selectFileDialog(final String tcStartFolder, final int tnFileSelectionMode, final boolean tlMultiSelection, final boolean tlHiddenDirectories, final boolean tlHiddenFiles, final boolean tlZipFilesOnly, final String tcTitle, final String tcButtonLabel, final int tnAccessoryType) {
        final Cursor loCursor = this.foMainFrame.getCursor();
        this.foMainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
        SelectFilesChooser.foChooser.setFileSelectionMode(tnFileSelectionMode);
        SelectFilesChooser.foChooser.setMultiSelectionEnabled(tlMultiSelection);
        SelectFilesChooser.foChooser.setCurrentDirectory(new File(tcStartFolder));
        SelectFilesChooser.foChooser.setDialogTitle(tcTitle);
        SelectFilesChooser.foChooser.removeChoosableFileFilter(SelectFilesChooser.foChooser.getFileFilter());
        SelectFilesChooser.foChooser.setFileFilter(new FileDialogFilter(tnFileSelectionMode, tlHiddenDirectories, tlHiddenFiles, tlZipFilesOnly));
        switch(tnAccessoryType) {
            case SelectFilesChooser.ACCESSORY_QUICKZIP:
                this.chkIncludeSubFolders1.setSelected(this.foMainFrame.foAppProperties.getOptionIncludeSubFoldersQZ());
                this.chkSavePathInformation1.setSelected(this.foMainFrame.foAppProperties.getOptionSavePathInformationQZ());
                SelectFilesChooser.foChooser.setAccessory(this.pnlAccessory1);
                SelectFilesChooser.foChooser.setSelectedFile(new File(Util.includeTrailingBackslash(tcStartFolder) + "*.*"));
                SelectFilesChooser.foChooser.setControlButtonsAreShown(false);
                break;
            case SelectFilesChooser.ACCESSORY_EXTRACTION:
                this.chkSelectAllFiles1.setSelected(this.foMainFrame.foAppProperties.getOptionExtractionSelectAllFiles());
                this.chkSelectAllFiles1.setEnabled(!this.foMainFrame.foAppProperties.getOptionExtractionSelectAllFiles());
                this.chkUsePathNames1.setSelected(this.foMainFrame.foAppProperties.getOptionExtractionUsePathNames());
                this.chkOverwriteFiles1.setSelected(this.foMainFrame.foAppProperties.getOptionExtractionOverWriteFiles());
                SelectFilesChooser.foChooser.setAccessory(this.pnlAccessory2);
                SelectFilesChooser.foChooser.setSelectedFile(new File(""));
                SelectFilesChooser.foChooser.setControlButtonsAreShown(true);
                break;
            case SelectFilesChooser.ACCESSORY_NONE:
                SelectFilesChooser.foChooser.setAccessory(null);
                SelectFilesChooser.foChooser.setSelectedFile(new File(""));
                SelectFilesChooser.foChooser.setControlButtonsAreShown(true);
                break;
        }
        final int lnValue = SelectFilesChooser.foChooser.showDialog(this.foMainFrame, tcButtonLabel);
        this.foMainFrame.setCursor(loCursor);
        if (lnValue == JFileChooser.APPROVE_OPTION) {
            switch(tnAccessoryType) {
                case SelectFilesChooser.ACCESSORY_QUICKZIP:
                    this.foMainFrame.foAppProperties.setOptionIncludeSubFoldersQZ(this.chkIncludeSubFolders1.isSelected());
                    this.foMainFrame.foAppProperties.setOptionSavePathInformationQZ(this.chkSavePathInformation1.isSelected());
                    break;
                case SelectFilesChooser.ACCESSORY_EXTRACTION:
                    this.foMainFrame.foAppProperties.setOptionExtractionSelectAllFiles(this.chkSelectAllFiles1.isSelected());
                    this.foMainFrame.foAppProperties.setOptionExtractionUsePathNames(this.chkUsePathNames1.isSelected());
                    this.foMainFrame.foAppProperties.setOptionExtractionOverWriteFiles(this.chkOverwriteFiles1.isSelected());
                    break;
            }
            final SelectFileReturnValue loValue = new SelectFileReturnValue();
            loValue.CurrentDirectory = SelectFilesChooser.foChooser.getCurrentDirectory();
            loValue.SelectedFiles = SelectFilesChooser.foChooser.getSelectedFiles();
            if ((SelectFilesChooser.foChooser.getSelectedFile() == null) || (tnAccessoryType == SelectFilesChooser.ACCESSORY_QUICKZIP)) {
                loValue.SelectedFile = new File(Util.includeTrailingBackslash(SelectFilesChooser.foChooser.getCurrentDirectory().getPath()) + this.txtFileNameTextField1.getText());
            } else {
                loValue.SelectedFile = SelectFilesChooser.foChooser.getSelectedFile();
            }
            return (loValue);
        }
        return (null);
    }

    private void findFileNameField() {
        this.fnTextFieldCount = 0;
        this.loopThroughTheComponents(SelectFilesChooser.foChooser);
        if (this.fnTextFieldCount != 1) {
            Global.errorException(this.foMainFrame, "There are more than 1 JTextFields in JFileChooser.");
        }
    }

    private void loopThroughTheComponents(final JComponent toComp) {
        if (toComp instanceof JTextField) {
            this.txtFileNameTextField1 = (JTextField) toComp;
            this.fnTextFieldCount++;
        }
        final int lnCount = toComp.getComponentCount();
        for (int i = 0; i < lnCount; i++) {
            if (toComp.getComponent(i) instanceof JComponent) {
                this.loopThroughTheComponents((JComponent) toComp.getComponent(i));
            }
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Object loSource = e.getSource();
        if (loSource == this.btnAddFiles1) {
            SelectFilesChooser.foChooser.approveSelection();
        } else if (loSource == this.btnCancel1) {
            SelectFilesChooser.foChooser.cancelSelection();
        }
    }
}

class SelectFileReturnValue {

    public File[] SelectedFiles;

    public File SelectedFile;

    public File CurrentDirectory;
}
