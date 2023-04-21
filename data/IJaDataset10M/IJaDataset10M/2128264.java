package net.sf.xpontus.plugins.validation.batchvalidation;

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.JTextField;
import net.sf.xpontus.utils.XPontusComponentsUtils;

/**
 * Dialog class for batch validation
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class BatchValidationDialogView extends javax.swing.JDialog {

    private BatchValidationController controller;

    /** Creates new form BatchValidationDialogView
     * @param parent
     * @param modal 
     */
    public BatchValidationDialogView(Frame parent, boolean modal) {
        super(parent, modal);
        controller = new BatchValidationController(this);
        initComponents();
        this.setTitle("Batch Validation");
        ((DefaultComboBoxModel) extensionsList.getModel()).addElement("*.xml");
    }

    /**
     * 
     * @return
     */
    public JList getPathList() {
        return pathList;
    }

    /**
     * 
     * @return
     */
    public JTextField getExtensionTF() {
        return extensionTF;
    }

    /**
     * 
     */
    public BatchValidationDialogView() {
        this((Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }

    /**
     * 
     * @return
     */
    public JCheckBox getRecurseOption() {
        return recurseOption;
    }

    private void initComponents() {
        recurseOption = new javax.swing.JCheckBox();
        bottomPanel = new javax.swing.JPanel();
        validateButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        inputPanel = new javax.swing.JPanel();
        addPathButton = new javax.swing.JButton();
        removePathButton = new javax.swing.JButton();
        pathListScrollPane = new javax.swing.JScrollPane();
        pathList = new javax.swing.JList();
        extensionsSettingsPanel = new javax.swing.JPanel();
        extensionTF = new javax.swing.JTextField();
        extensionsListLabel = new javax.swing.JLabel();
        removeExtensionButton = new javax.swing.JButton();
        addExtensionButton = new javax.swing.JButton();
        editExtensionsButton = new javax.swing.JButton();
        fileExtensionTF = new javax.swing.JLabel();
        extensionsListScrollPane = new javax.swing.JScrollPane();
        extensionsList = new javax.swing.JList();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        recurseOption.setSelected(true);
        recurseOption.setText("Directory recursion");
        validateButton.setText("Validate");
        validateButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, controller, BatchValidationController.VALIDATE_FILES_METHOD));
        bottomPanel.add(validateButton);
        closeButton.setText("Cancel");
        closeButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, controller, BatchValidationController.CLOSE_WINDOW_METHOD));
        bottomPanel.add(closeButton);
        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input settings"));
        addPathButton.setText("Add files/folders");
        addPathButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, controller, BatchValidationController.ADD_FILE_METHOD));
        removePathButton.setText("Remove");
        removePathButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, controller, BatchValidationController.REMOVE_FILE_METHOD));
        pathList.setModel(new DefaultComboBoxModel());
        pathListScrollPane.setViewportView(pathList);
        org.jdesktop.layout.GroupLayout inputPanelLayout = new org.jdesktop.layout.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(inputPanelLayout.createSequentialGroup().addContainerGap().add(addPathButton).add(59, 59, 59).add(pathListScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 214, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(removePathButton).addContainerGap()));
        inputPanelLayout.setVerticalGroup(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(inputPanelLayout.createSequentialGroup().addContainerGap().add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(addPathButton).add(pathListScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(removePathButton)).addContainerGap()));
        extensionsSettingsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Extensions settings"));
        extensionsListLabel.setText("Extensions list");
        removeExtensionButton.setText("Remove");
        removeExtensionButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, controller, BatchValidationController.REMOVE_EXTENSION_METHOD));
        addExtensionButton.setText("Add");
        addExtensionButton.addActionListener((ActionListener) EventHandler.create(ActionListener.class, controller, BatchValidationController.ADD_EXTENSION_METHOD));
        editExtensionsButton.setText("Edit");
        fileExtensionTF.setText("File extension to add");
        extensionsList.setModel(new DefaultComboBoxModel());
        extensionsListScrollPane.setViewportView(extensionsList);
        org.jdesktop.layout.GroupLayout extensionsSettingsPanelLayout = new org.jdesktop.layout.GroupLayout(extensionsSettingsPanel);
        extensionsSettingsPanel.setLayout(extensionsSettingsPanelLayout);
        extensionsSettingsPanelLayout.setHorizontalGroup(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(extensionsSettingsPanelLayout.createSequentialGroup().addContainerGap().add(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(extensionsListLabel).add(extensionsSettingsPanelLayout.createSequentialGroup().add(extensionsListScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(editExtensionsButton).add(removeExtensionButton)).add(84, 84, 84).add(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(fileExtensionTF).add(org.jdesktop.layout.GroupLayout.TRAILING, extensionsSettingsPanelLayout.createSequentialGroup().add(extensionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 90, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 27, Short.MAX_VALUE).add(addExtensionButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))))).addContainerGap(21, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)));
        extensionsSettingsPanelLayout.linkSize(new java.awt.Component[] { editExtensionsButton, removeExtensionButton }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        extensionsSettingsPanelLayout.setVerticalGroup(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(extensionsSettingsPanelLayout.createSequentialGroup().addContainerGap().add(extensionsListLabel).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(extensionsSettingsPanelLayout.createSequentialGroup().add(fileExtensionTF).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(extensionsSettingsPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(addExtensionButton).add(extensionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).add(extensionsSettingsPanelLayout.createSequentialGroup().add(editExtensionsButton).add(20, 20, 20).add(removeExtensionButton)).add(extensionsListScrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(20, 20, 20).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(recurseOption).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, bottomPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, extensionsSettingsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(org.jdesktop.layout.GroupLayout.LEADING, inputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE))).add(21, 21, 21)));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 183, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(extensionsSettingsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(4, 4, 4).add(recurseOption).add(12, 12, 12).add(bottomPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 46, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private javax.swing.JButton addExtensionButton;

    private javax.swing.JButton addPathButton;

    private javax.swing.JPanel bottomPanel;

    private javax.swing.JButton closeButton;

    private javax.swing.JButton editExtensionsButton;

    private javax.swing.JTextField extensionTF;

    private javax.swing.JList extensionsList;

    private javax.swing.JLabel extensionsListLabel;

    private javax.swing.JScrollPane extensionsListScrollPane;

    private javax.swing.JPanel extensionsSettingsPanel;

    private javax.swing.JLabel fileExtensionTF;

    private javax.swing.JPanel inputPanel;

    private javax.swing.JList pathList;

    private javax.swing.JScrollPane pathListScrollPane;

    private javax.swing.JCheckBox recurseOption;

    private javax.swing.JButton removeExtensionButton;

    private javax.swing.JButton removePathButton;

    private javax.swing.JButton validateButton;

    /**
     * 
     * @return
     */
    public JList getExtensionsList() {
        return extensionsList;
    }

    public void enableControlButtons(boolean b) {
        closeButton.setEnabled(b);
        validateButton.setEnabled(b);
    }

    /**
     * 
     * @param extensionsList
     */
    public void setExtensionsList(JList extensionsList) {
        this.extensionsList = extensionsList;
    }
}
