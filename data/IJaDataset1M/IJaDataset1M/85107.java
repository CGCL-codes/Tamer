package backupit.gui;

import backupit.core.FileInfo;
import backupit.core.FileInfoSet;
import backupit.core.BackupFactory;
import backupit.core.ResourceIdentifier;
import backupit.core.io.FileInfoSetReader;
import backupit.gui.tree.FileInfoSetTreeNode;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

class FileBrowser implements Runnable {

    private CreateBackupDialog parent;

    private JTextField textField;

    private LoadFiles files;

    private javax.swing.JTree tree;

    private JTextField fileCount;

    private JTextField totalBytes;

    public FileBrowser(CreateBackupDialog parent, JTextField textField, javax.swing.JTree tree, LoadFiles files, JTextField fileCount, JTextField totalBytes) {
        this.parent = parent;
        this.textField = textField;
        this.files = files;
        this.tree = tree;
        this.fileCount = fileCount;
        this.totalBytes = totalBytes;
    }

    public void run() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        if (textField.getText() != null) {
            chooser.setCurrentDirectory(new File(textField.getText()));
        }
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
            this.textField.setText(chooser.getSelectedFile().getAbsolutePath());
            this.files = new LoadFiles(this.parent, this.textField, this.tree, this.fileCount, this.totalBytes, parent.getHidden().isSelected(), parent.getUpdate().isSelected(), parent.getRecursive().isSelected());
            new Thread(this.files).start();
        }
    }
}

class LoadFiles implements Runnable {

    private CreateBackupDialog parent;

    private javax.swing.JTree tree;

    private javax.swing.JTextField textField;

    private FileInfoSet files;

    private JTextField fileCount;

    private JTextField totalBytes;

    private boolean hidden = false;

    private boolean update = false;

    private boolean recursive = true;

    public FileInfoSet getFiles() {
        return files;
    }

    public LoadFiles(CreateBackupDialog parent, javax.swing.JTextField sourceTextField, javax.swing.JTree sourceLocationTree, JTextField fileCount, JTextField totalBytes, boolean hidden, boolean update, boolean recursive) {
        this.parent = parent;
        this.textField = sourceTextField;
        this.tree = sourceLocationTree;
        this.hidden = hidden;
        this.update = update;
        this.recursive = recursive;
        this.fileCount = fileCount;
        this.totalBytes = totalBytes;
    }

    private void updateInfo() {
        fileCount.setText(files.size() + "");
        long totalBytesSize = 0;
        for (FileInfo file : files) totalBytesSize += file.getLength();
        totalBytes.setText(totalBytesSize + "");
    }

    public void run() {
        File source_dir = new File(textField.getText());
        FileInfoSetReader source = new FileInfoSetReader(source_dir.getPath(), update, hidden, recursive);
        files = source.loadFiles();
        DefaultTreeModel model = (DefaultTreeModel) this.tree.getModel();
        model.setRoot(new FileInfoSetTreeNode(files));
        this.parent.restrictions();
        this.updateInfo();
    }
}

/**
 *
 * @author  dbotelho
 */
public class CreateBackupDialog extends javax.swing.JDialog {

    private BackupListFrame frame;

    private LoadFiles destinationFiles;

    private LoadFiles sourceFiles;

    protected boolean restrictions() {
        addButton.setEnabled(sourceTextField.getText().trim().length() > 0 && destinationTextField.getText().trim().length() > 0 && backupNameTextField.getText().trim().length() > 0);
        return addButton.isEnabled();
    }

    /** Creates new form CreateBackupDialog */
    public CreateBackupDialog(BackupListFrame frame) {
        super(frame, true);
        this.frame = frame;
        initComponents();
        restrictions();
    }

    public javax.swing.JCheckBox getHidden() {
        return hidden;
    }

    public javax.swing.JCheckBox getRecursive() {
        return recursive;
    }

    public javax.swing.JCheckBox getUpdate() {
        return update;
    }

    private void initComponents() {
        jSplitPane1 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        sourceTextField = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        sourceLocationTree = new javax.swing.JTree();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        sourceFileCountTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        sourceTotalBytesTextField = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        destinationTextField = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        destinationLocationTree = new javax.swing.JTree();
        DefaultTreeModel model = (DefaultTreeModel) this.destinationLocationTree.getModel();
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(File.separator);
        model.setRoot(root);
        jButton3 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        destinationFileCountTextField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        destinationTotalBytesTextField = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        simulate = new javax.swing.JCheckBox();
        hidden = new javax.swing.JCheckBox();
        update = new javax.swing.JCheckBox();
        recursive = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        backupNameTextField = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        jSplitPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jSplitPane1.setDividerLocation(245);
        jSplitPane1.setDividerSize(4);
        jSplitPane1.setOneTouchExpandable(true);
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Source"));
        jLabel1.setText("Location:");
        sourceTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sourceTextFieldActionPerformed(evt);
            }
        });
        model = (DefaultTreeModel) this.sourceLocationTree.getModel();
        root = new DefaultMutableTreeNode(File.separator);
        model.setRoot(root);
        sourceLocationTree.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sourceLocationTreeMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(sourceLocationTree);
        jButton2.setText("Browse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));
        jLabel3.setText("File count:");
        sourceFileCountTextField.setEditable(false);
        sourceFileCountTextField.setText("0");
        jLabel4.setText("Total bytes:");
        sourceTotalBytesTextField.setEditable(false);
        sourceTotalBytesTextField.setText("0");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(sourceFileCountTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE).addComponent(sourceTotalBytesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE)).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(sourceFileCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(sourceTotalBytesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))));
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup().addComponent(jLabel1).addGap(4, 4, 4).addComponent(sourceTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton2).addContainerGap()).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jButton2).addComponent(sourceTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jSplitPane1.setLeftComponent(jPanel4);
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Destination"));
        jLabel2.setText("Location:");
        destinationTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                destinationTextFieldActionPerformed(evt);
            }
        });
        destinationLocationTree.setBorder(null);
        jScrollPane3.setViewportView(destinationLocationTree);
        jButton3.setText("Browse");
        jButton3.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Info"));
        jLabel5.setText("File count:");
        destinationFileCountTextField.setEditable(false);
        destinationFileCountTextField.setText("0");
        jLabel6.setText("Total bytes:");
        destinationTotalBytesTextField.setEditable(false);
        destinationTotalBytesTextField.setText("0");
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel6).addComponent(jLabel5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(destinationFileCountTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE).addComponent(destinationTotalBytesTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(destinationFileCountTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(destinationTotalBytesTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))));
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup().addComponent(jLabel2).addGap(4, 4, 4).addComponent(destinationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton3).addContainerGap()).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jButton3).addComponent(destinationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)));
        jSplitPane1.setRightComponent(jPanel5);
        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Options"));
        simulate.setText("Simulate the backup process");
        simulate.setEnabled(false);
        hidden.setText("Also backup hidden files");
        update.setText("Also backup modified file");
        recursive.setText("Backup directories recursively");
        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(simulate).addComponent(hidden).addComponent(update).addComponent(recursive)).addContainerGap(253, Short.MAX_VALUE)));
        jPanel7Layout.setVerticalGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel7Layout.createSequentialGroup().addComponent(simulate).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(hidden).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(update).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(recursive)));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Backup"));
        jLabel7.setText("Backup name:");
        backupNameTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backupNameTextFieldActionPerformed(evt);
            }
        });
        backupNameTextField.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyTyped(java.awt.event.KeyEvent evt) {
                backupNameTextFieldKeyTyped(evt);
            }
        });
        addButton.setText("Add");
        addButton.setFocusable(false);
        addButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        addButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        addButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });
        cancelButton.setText("Cancel");
        cancelButton.setFocusable(false);
        cancelButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cancelButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel7).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(backupNameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 339, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(addButton).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cancelButton))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(backupNameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE).addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void sourceTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        sourceFiles = new LoadFiles(this, this.sourceTextField, this.sourceLocationTree, this.sourceFileCountTextField, this.sourceTotalBytesTextField, hidden.isSelected(), update.isSelected(), recursive.isSelected());
        new Thread(sourceFiles).start();
        this.restrictions();
    }

    private void sourceLocationTreeMouseClicked(java.awt.event.MouseEvent evt) {
        if (evt.getClickCount() == 2 && this.sourceLocationTree.getLeadSelectionPath() != null && sourceLocationTree.getLeadSelectionPath().getParentPath().getLastPathComponent() != null) {
            DefaultTreeModel model = (DefaultTreeModel) this.sourceLocationTree.getModel();
            if (sourceLocationTree.getLastSelectedPathComponent().equals(this.sourceLocationTree.getLeadSelectionPath().getLastPathComponent())) System.out.println(sourceLocationTree.getLastSelectedPathComponent() + " " + sourceLocationTree.getLeadSelectionPath().getParentPath().getLastPathComponent() + " " + this.sourceLocationTree.getLeadSelectionPath().getLastPathComponent());
            sourceFiles = new LoadFiles(this, this.sourceTextField, this.sourceLocationTree, this.sourceFileCountTextField, this.sourceTotalBytesTextField, hidden.isSelected(), update.isSelected(), recursive.isSelected());
            new Thread(sourceFiles).start();
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.EventQueue.invokeLater(new FileBrowser(this, this.sourceTextField, this.sourceLocationTree, this.sourceFiles, this.sourceFileCountTextField, this.sourceTotalBytesTextField));
    }

    private void destinationTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        destinationFiles = new LoadFiles(this, this.destinationTextField, this.destinationLocationTree, this.destinationFileCountTextField, this.destinationTotalBytesTextField, hidden.isSelected(), update.isSelected(), recursive.isSelected());
        new Thread(new LoadFiles(this, this.destinationTextField, this.destinationLocationTree, this.destinationFileCountTextField, this.destinationTotalBytesTextField, hidden.isSelected(), update.isSelected(), recursive.isSelected())).start();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.EventQueue.invokeLater(new FileBrowser(this, this.destinationTextField, this.destinationLocationTree, this.destinationFiles, this.destinationFileCountTextField, this.destinationTotalBytesTextField));
    }

    private void addButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.frame.addBackup(BackupFactory.getDirBackupInstance(backupNameTextField.getText(), new ResourceIdentifier(sourceTextField.getText()), new ResourceIdentifier(destinationTextField.getText()), hidden.isSelected(), update.isSelected(), recursive.isSelected(), simulate.isSelected()));
        this.dispose();
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    private void backupNameTextFieldKeyTyped(java.awt.event.KeyEvent evt) {
        restrictions();
    }

    private void backupNameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        if (restrictions()) addButtonActionPerformed(evt);
    }

    private javax.swing.JButton addButton;

    private javax.swing.JTextField backupNameTextField;

    private javax.swing.JButton cancelButton;

    private javax.swing.JTextField destinationFileCountTextField;

    private javax.swing.JTree destinationLocationTree;

    private javax.swing.JTextField destinationTextField;

    private javax.swing.JTextField destinationTotalBytesTextField;

    private javax.swing.JCheckBox hidden;

    private javax.swing.JButton jButton2;

    private javax.swing.JButton jButton3;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane3;

    private javax.swing.JSplitPane jSplitPane1;

    private javax.swing.JCheckBox recursive;

    private javax.swing.JCheckBox simulate;

    private javax.swing.JTextField sourceFileCountTextField;

    private javax.swing.JTree sourceLocationTree;

    private javax.swing.JTextField sourceTextField;

    private javax.swing.JTextField sourceTotalBytesTextField;

    private javax.swing.JCheckBox update;
}
