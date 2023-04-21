package com.javable.cese;

import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import com.javable.cese.ResourceManager;
import com.javable.utils.ExceptionDialog;
import com.javable.utils.ExtensionFileFilter;

/**
 * Generates dialog box to manage installed and install new models
 */
public class ModelOptions extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */
    public static final int RET_CANCEL = 0;

    /** A return status code - returned if OK button has been pressed */
    public static final int RET_OK = 1;

    private File dir, file;

    private ModelManager manager = CESE.getEnvironment().getModelManager();

    /**
     * Generates dialog box to manage installed and install new models
     * 
     * @param parent parent frame
     */
    public ModelOptions(java.awt.Frame parent) {
        super(parent, true);
        initComponents();
    }

    /**
     * @return the return status of this dialog - one of RET_OK or RET_CANCEL
     */
    public int getReturnStatus() {
        return returnStatus;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        listPanel = new javax.swing.JPanel();
        listScrollPane = new javax.swing.JScrollPane();
        modelList = new javax.swing.JList();
        modelPanel = new javax.swing.JPanel();
        selectionPanel = new javax.swing.JPanel();
        installButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        upButton = new javax.swing.JButton();
        downButton = new javax.swing.JButton();
        setTitle(ResourceManager.getResource("Model_Manager"));
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        okButton.setText(ResourceManager.getResource("OK"));
        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(okButton);
        cancelButton.setText(ResourceManager.getResource("Cancel"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);
        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        listPanel.setLayout(new java.awt.BorderLayout());
        listPanel.setBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EtchedBorder(), ResourceManager.getResource("Installed_models")));
        modelList.setCellRenderer(new ModelBoxRenderer());
        modelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listScrollPane.setViewportView(modelList);
        listPanel.add(listScrollPane, java.awt.BorderLayout.CENTER);
        modelPanel.add(listPanel, java.awt.BorderLayout.CENTER);
        selectionPanel.setLayout(new java.awt.GridBagLayout());
        installButton.setMnemonic('i');
        installButton.setText(ResourceManager.getResource("Install_Model..."));
        installButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(20, 5, 10, 5);
        selectionPanel.add(installButton, gridBagConstraints);
        removeButton.setMnemonic('r');
        removeButton.setText(ResourceManager.getResource("Remove_Model"));
        removeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        selectionPanel.add(removeButton, gridBagConstraints);
        upButton.setMnemonic('u');
        upButton.setText(ResourceManager.getResource("Move_up"));
        upButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                upButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 10, 5);
        selectionPanel.add(upButton, gridBagConstraints);
        downButton.setMnemonic('d');
        downButton.setText(ResourceManager.getResource("Move_down"));
        downButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                downButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(10, 5, 20, 5);
        selectionPanel.add(downButton, gridBagConstraints);
        modelPanel.add(selectionPanel, java.awt.BorderLayout.EAST);
        getContentPane().add(modelPanel, java.awt.BorderLayout.CENTER);
    }

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(RET_CANCEL);
    }

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {
        manager.getSelector().removeAllItems();
        for (int i = 0; i < modelList.getModel().getSize(); i++) {
            manager.getSelector().addItem(modelList.getModel().getElementAt(i));
        }
        doClose(RET_OK);
    }

    private void upButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int current = modelList.getSelectedIndex();
        if (current > 0) {
            Object item = ((DefaultListModel) modelList.getModel()).remove(current);
            ((DefaultListModel) modelList.getModel()).insertElementAt(item, current - 1);
        }
    }

    private void downButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int current = modelList.getSelectedIndex();
        if ((current < modelList.getModel().getSize() - 1) && (current >= 0)) {
            Object item = ((DefaultListModel) modelList.getModel()).remove(current);
            ((DefaultListModel) modelList.getModel()).insertElementAt(item, current + 1);
        }
    }

    private void removeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        int current = modelList.getSelectedIndex();
        if ((modelList.getModel().getSize() > 0) && (current >= 0)) ((DefaultListModel) modelList.getModel()).remove(current);
    }

    private void installButtonActionPerformed(java.awt.event.ActionEvent evt) {
        dir = new File(this.getClass().getResource(ResourceManager.getResource("mods_dir")).getFile());
        try {
            JFileChooser filechooser = new JFileChooser(dir);
            filechooser.setDialogTitle(ResourceManager.getResource("Install_Model"));
            filechooser.setApproveButtonMnemonic('I');
            filechooser.setApproveButtonToolTipText(ResourceManager.getResource("Install_model"));
            ExtensionFileFilter preferredFilter = new ExtensionFileFilter("jar", ResourceManager.getResource("Model_Code_Files"));
            filechooser.addChoosableFileFilter(preferredFilter);
            filechooser.setFileFilter(preferredFilter);
            int retVal = filechooser.showDialog(CESE.getEnvironment(), "Install");
            if (retVal == JFileChooser.APPROVE_OPTION) {
                file = filechooser.getSelectedFile();
                dir = filechooser.getCurrentDirectory();
                if (file.getPath() == null) return;
                manager.installModel(file);
                diagnoseInstall();
            }
        } catch (Exception e) {
            ExceptionDialog.showExceptionDialog(ResourceManager.getResource("Error"), ResourceManager.getResource("Error_installing_model."), e);
        }
    }

    private void diagnoseInstall() {
        String diagnoseString = "";
        int installed = manager.getModelInstaller().getInstalledNames().size();
        if (installed == 0) {
            ExceptionDialog.showExceptionDialog(ResourceManager.getResource("Installation_error"), ResourceManager.getResource("No_valid_models_found."), null);
        } else {
            for (int i = 0; i < installed; i++) {
                diagnoseString += (String) manager.getModelInstaller().getInstalledNames().get(i) + '\n';
            }
            setList();
            JOptionPane.showMessageDialog(CESE.getEnvironment(), installed + " " + ResourceManager.getResource("model(s)_installed") + '\n' + diagnoseString, ResourceManager.getResource("Installation_complete"), JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void setList() {
        modelList.setModel(new DefaultListModel());
        for (int i = 0; i < manager.getSelector().getModel().getSize(); i++) {
            ((DefaultListModel) modelList.getModel()).addElement(manager.getSelector().getModel().getElementAt(i));
        }
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    /**
     * Closes the dialog
     * 
     * @param evt
     */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(RET_CANCEL);
    }

    /**
     * Overrides setVisible(boolean b) in <code>JDialog</code>
     * 
     * @param b visibility
     */
    public void setVisible(boolean b) {
        if (b == true) {
            setResizable(true);
            setList();
            pack();
            setResizable(false);
            getRootPane().setDefaultButton(okButton);
            setLocationRelativeTo(getOwner());
        }
        super.setVisible(b);
    }

    private javax.swing.JPanel listPanel;

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JList modelList;

    private javax.swing.JPanel modelPanel;

    private javax.swing.JButton okButton;

    private javax.swing.JButton cancelButton;

    private javax.swing.JButton installButton;

    private javax.swing.JPanel selectionPanel;

    private javax.swing.JButton upButton;

    private javax.swing.JButton downButton;

    private javax.swing.JScrollPane listScrollPane;

    private javax.swing.JButton removeButton;

    private int returnStatus = RET_CANCEL;
}
