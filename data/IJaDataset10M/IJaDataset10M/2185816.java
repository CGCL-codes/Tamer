package net.jfellow.loganalyser.gui;

import net.jfellow.common.util.ExportException;
import java.io.*;
import java.util.prefs.*;
import javax.swing.*;

/**
 *
 * @author  wiedthom
 */
public class JDialogExport extends javax.swing.JDialog {

    private static String FILE = "loganalyser_export_file";

    private static String SEPARATOR = "loganalyser_export_separator";

    private static String CELL_ENCLOSING = "loganalyser_export_cell_enclosing";

    private static String COLUMNS = "loganalyser_export_columns";

    private static String AUTOMATIC_EXPORT = "loganalyser_automatic_export";

    private static String MAX_EXPORT_FILE_SIZE = "loganalyser_export_max_file_size";

    private static String MAX_EXPORT_FILE_COUNT = "loganalyser_export_max_file_count";

    private static String file;

    private static String separator;

    private static String cellEnclosing;

    private static String columns;

    private static boolean automaticallyExport;

    private static int maxExportFileSize;

    private static int maxExportFileCount;

    private JFrameLogFileAnalyser caller;

    private javax.swing.JButton jButtonCancel;

    private javax.swing.JButton jButtonDefault;

    private javax.swing.JButton jButtonFile;

    private javax.swing.JButton jButtonOK;

    private javax.swing.JCheckBox jCheckBoxAutomaticExportON;

    private javax.swing.JLabel jLabelAutoExportDetails;

    private javax.swing.JLabel jLabelAutoExportReached;

    private javax.swing.JLabel jLabelCellEnclosing;

    private javax.swing.JLabel jLabelCellEnclosingComment;

    private javax.swing.JLabel jLabelColumns;

    private javax.swing.JLabel jLabelColumnsComment;

    private javax.swing.JLabel jLabelFile;

    private javax.swing.JLabel jLabelMaxFileCount;

    private javax.swing.JLabel jLabelMaxFileSize;

    private javax.swing.JLabel jLabelSeparatoorComment;

    private javax.swing.JLabel jLabelSeparator;

    private javax.swing.JPanel jPanelAutomaticExport;

    private javax.swing.JPanel jPanelButtons;

    private javax.swing.JPanel jPanelFile;

    private javax.swing.JPanel jPanelFormat;

    private javax.swing.JPanel jPanelMain;

    private javax.swing.JTextField jTextFieldCellEnclosing;

    private javax.swing.JTextField jTextFieldColumns;

    private javax.swing.JTextField jTextFieldFile;

    private javax.swing.JTextField jTextFieldMaxFileCount;

    private javax.swing.JTextField jTextFieldMaxFileSize;

    private javax.swing.JTextField jTextFieldMaximumLines;

    private javax.swing.JTextField jTextFieldSeparator;

    /** Creates new form JDialogExport */
    public JDialogExport(JFrameLogFileAnalyser caller) {
        super((JFrame) caller, true);
        this.caller = caller;
        initComponents();
        this.loadUserPrefs();
        this.loadConfiguration();
        String maxLines = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_MAX_ROWS);
        this.jTextFieldMaximumLines.setText(maxLines);
        this.getRootPane().setDefaultButton(this.jButtonOK);
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanelMain = new javax.swing.JPanel();
        jPanelFormat = new javax.swing.JPanel();
        jLabelSeparator = new javax.swing.JLabel();
        jTextFieldSeparator = new javax.swing.JTextField();
        jLabelSeparatoorComment = new javax.swing.JLabel();
        jLabelCellEnclosing = new javax.swing.JLabel();
        jTextFieldCellEnclosing = new javax.swing.JTextField();
        jLabelCellEnclosingComment = new javax.swing.JLabel();
        jLabelColumns = new javax.swing.JLabel();
        jTextFieldColumns = new javax.swing.JTextField();
        jLabelColumnsComment = new javax.swing.JLabel();
        jLabelFile = new javax.swing.JLabel();
        jTextFieldFile = new javax.swing.JTextField();
        jButtonFile = new javax.swing.JButton();
        jPanelAutomaticExport = new javax.swing.JPanel();
        jCheckBoxAutomaticExportON = new javax.swing.JCheckBox();
        jTextFieldMaximumLines = new javax.swing.JTextField();
        jLabelAutoExportReached = new javax.swing.JLabel();
        jPanelFile = new javax.swing.JPanel();
        jLabelMaxFileSize = new javax.swing.JLabel();
        jTextFieldMaxFileSize = new javax.swing.JTextField();
        jLabelMaxFileCount = new javax.swing.JLabel();
        jTextFieldMaxFileCount = new javax.swing.JTextField();
        jLabelAutoExportDetails = new javax.swing.JLabel();
        jPanelButtons = new javax.swing.JPanel();
        jButtonOK = new javax.swing.JButton();
        jButtonDefault = new javax.swing.JButton();
        jButtonCancel = new javax.swing.JButton();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setTitle("Export");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        jPanelMain.setLayout(new java.awt.GridBagLayout());
        jPanelFormat.setLayout(new java.awt.GridBagLayout());
        jLabelSeparator.setText("Cell Separator");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        jPanelFormat.add(jLabelSeparator, gridBagConstraints);
        jTextFieldSeparator.setMinimumSize(new java.awt.Dimension(150, 20));
        jTextFieldSeparator.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelFormat.add(jTextFieldSeparator, gridBagConstraints);
        jLabelSeparatoorComment.setText("Usually \",\" or \";\" but can be any String");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        jPanelFormat.add(jLabelSeparatoorComment, gridBagConstraints);
        jLabelCellEnclosing.setText("Cell Enclosing");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFormat.add(jLabelCellEnclosing, gridBagConstraints);
        jTextFieldCellEnclosing.setMinimumSize(new java.awt.Dimension(150, 20));
        jTextFieldCellEnclosing.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanelFormat.add(jTextFieldCellEnclosing, gridBagConstraints);
        jLabelCellEnclosingComment.setText("Usually \" ' \" or \" \" \" but can be any String");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFormat.add(jLabelCellEnclosingComment, gridBagConstraints);
        jLabelColumns.setText("Columns");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFormat.add(jLabelColumns, gridBagConstraints);
        jTextFieldColumns.setMinimumSize(new java.awt.Dimension(150, 20));
        jTextFieldColumns.setPreferredSize(new java.awt.Dimension(150, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanelFormat.add(jTextFieldColumns, gridBagConstraints);
        jLabelColumnsComment.setText("<html>\nExample: \"1, 3-6, 9\". If empty all are selected.\n</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFormat.add(jLabelColumnsComment, gridBagConstraints);
        jLabelFile.setText("File");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFormat.add(jLabelFile, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 0, 5);
        jPanelFormat.add(jTextFieldFile, gridBagConstraints);
        jButtonFile.setText("...");
        jButtonFile.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonFileActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        jPanelFormat.add(jButtonFile, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelMain.add(jPanelFormat, gridBagConstraints);
        jPanelAutomaticExport.setLayout(new java.awt.GridBagLayout());
        jPanelAutomaticExport.setBorder(new javax.swing.border.TitledBorder("Automatic Export"));
        jCheckBoxAutomaticExportON.setMnemonic('S');
        jCheckBoxAutomaticExportON.setText("Automatically Export Table after the Maximum of");
        jCheckBoxAutomaticExportON.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAutomaticExportONActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanelAutomaticExport.add(jCheckBoxAutomaticExportON, gridBagConstraints);
        jTextFieldMaximumLines.setEnabled(false);
        jTextFieldMaximumLines.setMinimumSize(new java.awt.Dimension(80, 20));
        jTextFieldMaximumLines.setPreferredSize(new java.awt.Dimension(80, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
        jPanelAutomaticExport.add(jTextFieldMaximumLines, gridBagConstraints);
        jLabelAutoExportReached.setText("Lines has reached");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanelAutomaticExport.add(jLabelAutoExportReached, gridBagConstraints);
        jPanelFile.setLayout(new java.awt.GridBagLayout());
        jLabelMaxFileSize.setText("Maximum File Size");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanelFile.add(jLabelMaxFileSize, gridBagConstraints);
        jTextFieldMaxFileSize.setMinimumSize(new java.awt.Dimension(59, 20));
        jTextFieldMaxFileSize.setPreferredSize(new java.awt.Dimension(59, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelFile.add(jTextFieldMaxFileSize, gridBagConstraints);
        jLabelMaxFileCount.setText("MB, maximum File Count");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanelFile.add(jLabelMaxFileCount, gridBagConstraints);
        jTextFieldMaxFileCount.setMinimumSize(new java.awt.Dimension(59, 20));
        jTextFieldMaxFileCount.setPreferredSize(new java.awt.Dimension(59, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
        jPanelFile.add(jTextFieldMaxFileCount, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanelAutomaticExport.add(jPanelFile, gridBagConstraints);
        jLabelAutoExportDetails.setText("<html>If the table in the main window has reached its maximum count of lines then the table is automatically exported to file(s). The table in the main window is cleared after the export.</html>");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 5, 5, 5);
        jPanelAutomaticExport.add(jLabelAutoExportDetails, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanelMain.add(jPanelAutomaticExport, gridBagConstraints);
        jPanelButtons.setLayout(new java.awt.GridBagLayout());
        jButtonOK.setMnemonic('O');
        jButtonOK.setText("OK");
        jButtonOK.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonOKActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanelButtons.add(jButtonOK, gridBagConstraints);
        jButtonDefault.setMnemonic('D');
        jButtonDefault.setText("Default");
        jButtonDefault.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDefaultActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        jPanelButtons.add(jButtonDefault, gridBagConstraints);
        jButtonCancel.setMnemonic('C');
        jButtonCancel.setText("Cancel");
        jButtonCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonCancelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 0);
        jPanelButtons.add(jButtonCancel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(11, 0, 0, 0);
        jPanelMain.add(jPanelButtons, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 12, 12);
        getContentPane().add(jPanelMain, gridBagConstraints);
        pack();
    }

    private void jButtonDefaultActionPerformed(java.awt.event.ActionEvent evt) {
        this.setDefaultValues();
    }

    private void jCheckBoxAutomaticExportONActionPerformed(java.awt.event.ActionEvent evt) {
        this.enableDisableFileSettings();
    }

    private void jButtonFileActionPerformed(java.awt.event.ActionEvent evt) {
        this.chooseFile();
    }

    private void jButtonOKActionPerformed(java.awt.event.ActionEvent evt) {
        this.saveSettings();
        if (!this.export()) {
            return;
        }
        this.closeMySelf();
    }

    private void jButtonCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.closeMySelf();
    }

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {
        this.closeMySelf();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Method public static void main(String args[]) not implemented.");
    }

    private void loadUserPrefs() {
        this.setLocationRelativeTo(null);
    }

    private void loadConfiguration() {
        file = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_FILE);
        if ((file == null) || "".equals(file)) {
            file = JDialogExport.getFile();
        }
        separator = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_SEPARATOR);
        if ((separator == null) || "".equals(separator)) {
            separator = JDialogExport.getSeparator();
        }
        cellEnclosing = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_CELL_ENCLOSING);
        if ((cellEnclosing == null) || "".equals(cellEnclosing)) {
            cellEnclosing = JDialogExport.getCellEnclosing();
        }
        columns = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_COLUMNS);
        if ((columns == null) || "".equals(columns)) {
            columns = JDialogExport.getColumns();
        }
        this.jTextFieldFile.setText(file);
        this.jTextFieldSeparator.setText(separator);
        this.jTextFieldCellEnclosing.setText(cellEnclosing);
        this.jTextFieldColumns.setText(columns);
        boolean b = false;
        String s = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_AUTOMATIC);
        if ((s != null) && !"".equals(s)) {
            b = Boolean.parseBoolean(s);
        } else {
            b = JDialogExport.isAutomaticallyExport();
        }
        this.jCheckBoxAutomaticExportON.setSelected(b);
        int i = 10;
        s = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_MAX_FILE_SIZE);
        if ((s != null) && !"".equals(s)) {
            try {
                i = Integer.parseInt(s);
            } catch (java.lang.NumberFormatException e) {
                i = JDialogExport.getMaxExportFileSize();
            }
        } else {
            i = JDialogExport.getMaxExportFileSize();
        }
        this.jTextFieldMaxFileSize.setText(Integer.toString(i));
        i = 10;
        s = JFrameLogFileAnalyser.getProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_MAX_FILE_COUNT);
        if ((s != null) && !"".equals(s)) {
            try {
                i = Integer.parseInt(s);
            } catch (java.lang.NumberFormatException e) {
                i = JDialogExport.getMaxExportFileCount();
            }
        } else {
            i = JDialogExport.getMaxExportFileCount();
        }
        this.jTextFieldMaxFileCount.setText(Integer.toString(i));
        this.enableDisableFileSettings();
    }

    private void saveSettings() {
        java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        file = this.jTextFieldFile.getText();
        separator = this.jTextFieldSeparator.getText();
        cellEnclosing = this.jTextFieldCellEnclosing.getText();
        columns = this.jTextFieldColumns.getText();
        automaticallyExport = this.jCheckBoxAutomaticExportON.isSelected();
        caller.setAutomaticExportON(automaticallyExport);
        String s = this.jTextFieldMaxFileSize.getText();
        try {
            maxExportFileSize = Integer.parseInt(s);
        } catch (java.lang.NumberFormatException e) {
            this.popError("The export file size is not a number.");
            return;
        }
        s = this.jTextFieldMaxFileCount.getText();
        try {
            maxExportFileCount = Integer.parseInt(s);
        } catch (java.lang.NumberFormatException e) {
            this.popError("The export file count is not a number.");
            return;
        }
        prefs.put(JDialogExport.FILE, file);
        prefs.put(JDialogExport.SEPARATOR, separator);
        prefs.put(JDialogExport.CELL_ENCLOSING, cellEnclosing);
        prefs.put(JDialogExport.COLUMNS, columns);
        prefs.putBoolean(JDialogExport.AUTOMATIC_EXPORT, automaticallyExport);
        prefs.putInt(JDialogExport.MAX_EXPORT_FILE_SIZE, maxExportFileSize);
        prefs.putInt(JDialogExport.MAX_EXPORT_FILE_COUNT, maxExportFileCount);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_FILE, file);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_SEPARATOR, separator);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_CELL_ENCLOSING, cellEnclosing);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_COLUMNS, columns);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_AUTOMATIC, automaticallyExport);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_MAX_FILE_SIZE, maxExportFileSize);
        JFrameLogFileAnalyser.setProperty(JFrameLogFileAnalyser.PROP_KEY_EXPORT_MAX_FILE_COUNT, maxExportFileCount);
        caller.writePropsFile();
    }

    private void closeMySelf() {
        this.setVisible(false);
        this.dispose();
    }

    private boolean checkColumns() {
        columns = this.jTextFieldColumns.getText();
        java.util.List l = null;
        try {
            l = getColumnsAsList();
        } catch (Exception e) {
            this.popError(e.getMessage());
            return false;
        }
        if (l == null) {
            this.popError("Format of columns not correct.");
            return false;
        }
        return true;
    }

    /**
     * Returns a List of Integers or null if an error occured.
     */
    public static java.util.List getColumnsAsList() throws ExportException {
        java.util.List l = new java.util.ArrayList();
        String s = getColumns();
        if (s.equals("")) {
            return l;
        }
        String[] commataSepColumns = s.split(",");
        int commataLength = commataSepColumns.length;
        for (int i = 0; i < commataLength; i++) {
            String[] commataString = commataSepColumns[i].trim().split("-");
            int lineStringLength = commataString.length;
            if (lineStringLength == 1) {
                try {
                    int a = Integer.parseInt(commataString[0].trim());
                    Integer currentColumn = new Integer(a);
                    l.add(currentColumn);
                    continue;
                } catch (Exception e) {
                    throw new ExportException("Incorrect format of column count. Found: " + commataString[0].trim() + e.getMessage());
                }
            }
            if (lineStringLength != 2) {
                throw new ExportException("Incorrect format of column count. Found: " + s);
            }
            String from = commataString[0].trim();
            String to = commataString[1].trim();
            try {
                int a = Integer.parseInt(from);
                int b = Integer.parseInt(to);
                for (; a <= b; a++) {
                    Integer currentColumn = new Integer(a);
                    l.add(currentColumn);
                }
            } catch (Exception e) {
                throw new ExportException("Incorrect format of column count. Found: " + s + ". Original Message is: " + e.getMessage());
            }
        }
        return l;
    }

    public static String getFile() {
        if (file != null) {
        } else {
            java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
            String defaultFile = System.getProperty("user.home") + File.separator + "loganalayser_default_export.csv";
            file = prefs.get(JDialogExport.FILE, defaultFile);
            if (file == null) {
                file = defaultFile;
            } else {
                File checkFile = new File(file);
                if (!checkFile.exists()) {
                    file = defaultFile;
                } else if (!checkFile.isFile()) {
                    file = defaultFile;
                }
            }
        }
        return file;
    }

    public static void setFile(String s) {
        file = s;
    }

    public static String getSeparator() {
        if (separator != null) {
        } else {
            java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
            String defaultSeparator = ";";
            separator = prefs.get(JDialogExport.SEPARATOR, defaultSeparator);
        }
        return separator;
    }

    public static String getCellEnclosing() {
        if (cellEnclosing != null) {
        } else {
            java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
            String defaultCellEnclosing = "\"";
            cellEnclosing = prefs.get(JDialogExport.CELL_ENCLOSING, defaultCellEnclosing);
        }
        return cellEnclosing;
    }

    public static void setSeparator(String s) {
        separator = s;
    }

    public static void setCellEnclosing(String s) {
        cellEnclosing = s;
    }

    public static String getColumns() {
        if (columns != null) {
        } else {
            java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
            String defaultColumns = "";
            columns = prefs.get(JDialogExport.COLUMNS, defaultColumns);
        }
        return columns;
    }

    public static void setColumns(String s) {
        columns = s;
    }

    public static boolean isAutomaticallyExport() {
        java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
        automaticallyExport = prefs.getBoolean(JDialogExport.AUTOMATIC_EXPORT, false);
        return automaticallyExport;
    }

    public static void setAutomaticallyExport(boolean aAutomaticallyExport) {
        automaticallyExport = aAutomaticallyExport;
    }

    public static int getMaxExportFileSize() {
        java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
        maxExportFileSize = prefs.getInt(JDialogExport.MAX_EXPORT_FILE_SIZE, 10);
        return maxExportFileSize;
    }

    public static void setMaxExportFileSize(int aMaxExportFileSize) {
        maxExportFileSize = aMaxExportFileSize;
    }

    public static int getMaxExportFileCount() {
        java.util.prefs.Preferences prefs = Preferences.userNodeForPackage(JDialogExport.class);
        maxExportFileCount = prefs.getInt(JDialogExport.MAX_EXPORT_FILE_COUNT, 10);
        return maxExportFileCount;
    }

    public static void setMaxExportFileCount(int aMaxExportFileCount) {
        maxExportFileCount = aMaxExportFileCount;
    }

    private boolean export() {
        if (!this.checkColumns()) {
            return false;
        }
        try {
            if (!caller.export()) {
                return false;
            }
        } catch (Exception e) {
            this.popError(e.getMessage());
        }
        return true;
    }

    private void chooseFile() {
        file = this.jTextFieldFile.getText();
        File dir = null;
        File preSelectedFile = null;
        if ((file != null) && !"".equals(file)) {
            preSelectedFile = new File(file);
            if (!preSelectedFile.exists()) {
                preSelectedFile = null;
            } else {
                dir = preSelectedFile.getParentFile();
                if (dir != null) {
                    if (!dir.exists()) {
                        dir = null;
                    }
                }
            }
        }
        JFileChooser chooser = new JFileChooser();
        if (preSelectedFile != null) {
            chooser.setSelectedFile(preSelectedFile);
        } else if (dir != null) {
            chooser.setCurrentDirectory(dir);
        }
        chooser.setDialogTitle("Choose Export File");
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            file = chooser.getSelectedFile().getAbsolutePath();
            this.jTextFieldFile.setText(file);
        }
    }

    private void setDefaultValues() {
        this.jTextFieldCellEnclosing.setText("\"");
        this.jTextFieldColumns.setText("");
        this.jTextFieldMaxFileCount.setText("10");
        this.jTextFieldMaxFileSize.setText("10");
        this.jTextFieldSeparator.setText(";");
        this.jTextFieldFile.setText(JDialogExport.getFile());
    }

    private void enableDisableFileSettings() {
        this.jTextFieldMaxFileCount.setEnabled(this.jCheckBoxAutomaticExportON.isSelected());
        this.jTextFieldMaxFileSize.setEnabled(this.jCheckBoxAutomaticExportON.isSelected());
    }

    private void popError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
