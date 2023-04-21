package gui;

import integration.importer.HSSFImporter;
import java.io.File;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;

public class ImportDialog extends javax.swing.JDialog {

    private static final long serialVersionUID = 1L;

    public ImportDialog(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    private void initComponents() {
        importLabel = new JLabel();
        txtfldImportFrom = new JTextField();
        btnBrowse = new JButton();
        btnCancel = new JButton();
        btnStart = new JButton();
        ResourceMap resourceMap = Application.getInstance(gui.FundNetPricesApplication.class).getContext().getResourceMap(ImportDialog.class);
        setTitle(resourceMap.getString("title"));
        setName("importDialog");
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        ActionMap actionMap = Application.getInstance(gui.FundNetPricesApplication.class).getContext().getActionMap(ImportDialog.class, this);
        importLabel.setText(resourceMap.getString("importLabel.text"));
        importLabel.setName("importLabel");
        btnBrowse.setName("browseButton");
        btnBrowse.setAction(actionMap.get("browseFile"));
        btnCancel.setName("cancelButton");
        btnCancel.setAction(actionMap.get("cancelImport"));
        btnStart.setName("startButton");
        btnStart.setAction(actionMap.get("startImport"));
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(26, 26, 26).addComponent(importLabel).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(btnStart, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE).addComponent(txtfldImportFrom, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 247, GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(btnBrowse).addComponent(btnCancel)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnBrowse).addComponent(txtfldImportFrom, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(importLabel)).addGap(18, 18, 18).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(btnStart).addComponent(btnCancel)).addContainerGap()));
        pack();
    }

    @Action
    public void cancelImport() {
        dispose();
    }

    @Action
    public void browseFile() {
        JFileChooser fc = new JFileChooser();
        fc.addChoosableFileFilter(new FileNameExtensionFilter("Excel(*.xls)", "xls"));
        int ret = fc.showOpenDialog(this);
        if (ret == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            txtfldImportFrom.setText(file.getAbsolutePath());
        }
    }

    @Action
    public ImportTask startImport() {
        final File file = new File(txtfldImportFrom.getText());
        if (!file.isFile() || !file.getAbsolutePath().endsWith(".xls")) {
            JOptionPane.showMessageDialog(this, "Please specify a valid Excel file.", "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return new ImportTask(Application.getInstance(FundNetPricesApplication.class), file);
    }

    private class ImportTask extends Task {

        private File file;

        ImportTask(Application app, File file) {
            super(app);
            this.file = file;
        }

        @Override
        protected Object doInBackground() throws Exception {
            setComponentsEnabled(false);
            setMessage("Importing Excel...");
            HSSFImporter importer = new HSSFImporter();
            try {
                importer.importData(file);
                JOptionPane.showMessageDialog(ImportDialog.this, "Import completed.", "Completed", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(ImportDialog.this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            return null;
        }

        @Override
        protected void finished() {
            setMessage("Import finished.");
            ImportDialog.this.dispose();
        }

        private void setComponentsEnabled(boolean flag) {
            ImportDialog.this.importLabel.setEnabled(flag);
            ImportDialog.this.txtfldImportFrom.setEnabled(flag);
            ImportDialog.this.btnBrowse.setEnabled(flag);
            ImportDialog.this.btnStart.setEnabled(flag);
            ImportDialog.this.btnCancel.setEnabled(flag);
        }
    }

    private JButton btnBrowse;

    private JButton btnCancel;

    private JButton btnStart;

    private JLabel importLabel;

    private JTextField txtfldImportFrom;
}
