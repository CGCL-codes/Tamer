package br.upe.dsc.caeto.ui;

import java.util.Enumeration;
import javax.swing.ActionMap;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import org.jdesktop.application.Action;

public class NewApplicationDialog extends javax.swing.JDialog {

    private javax.swing.JButton addButton;

    private javax.swing.JLabel applicationNameLabel;

    private javax.swing.JTextField applicationTextField;

    private javax.swing.JButton cancelButton;

    private javax.swing.JSeparator jSeparator;

    private javax.swing.JComboBox projectComboBox;

    private javax.swing.JLabel projectLabel;

    private JTree tree;

    private ActionMap actionMap;

    private String input[];

    public NewApplicationDialog(java.awt.Frame parent, JTree tree) {
        super(parent, true);
        this.tree = tree;
        input = new String[2];
        initComponents();
    }

    public String[] getValues() {
        return input;
    }

    private void initComponents() {
        applicationNameLabel = new javax.swing.JLabel();
        projectLabel = new javax.swing.JLabel();
        applicationTextField = new javax.swing.JTextField();
        projectComboBox = new javax.swing.JComboBox();
        jSeparator = new javax.swing.JSeparator();
        addButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        actionMap = org.jdesktop.application.Application.getInstance(CaetoApplication.class).getContext().getActionManager().getActionMap(NewApplicationDialog.class, this);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        setTitle("New Application");
        applicationNameLabel.setText("Application name:");
        applicationNameLabel.setName("applicationNameLabel");
        projectLabel.setText("Project:");
        projectLabel.setName("projectLabel");
        applicationTextField.setName("applicationTextField");
        projectComboBox.setName("projectComboBox");
        addButton.setText("Add");
        addButton.setName("addButton");
        addButton.setPreferredSize(new java.awt.Dimension(85, 35));
        cancelButton.setText("Cancel");
        cancelButton.setName("cancelButton");
        cancelButton.setPreferredSize(new java.awt.Dimension(85, 35));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(applicationNameLabel).addComponent(projectLabel)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(projectComboBox, 0, 268, Short.MAX_VALUE).addComponent(applicationTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE)).addContainerGap()).addComponent(jSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(226, Short.MAX_VALUE).addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(applicationNameLabel).addComponent(applicationTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(projectLabel).addComponent(projectComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(addButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        initProjectComboBox();
        addButton.setAction(actionMap.get("addApplication"));
        pack();
    }

    @Action
    public void addApplication() {
        String name = applicationTextField.getText();
        if (!name.equals("")) {
            input[0] = name;
            input[1] = (String) projectComboBox.getSelectedItem();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Enter the application name.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void initProjectComboBox() {
        javax.swing.DefaultComboBoxModel model = new javax.swing.DefaultComboBoxModel();
        projectComboBox.setModel(model);
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) tree.getModel().getRoot();
        Enumeration<TreeNode> children = root.children();
        while (children.hasMoreElements()) {
            model.addElement(children.nextElement().toString());
        }
    }
}
