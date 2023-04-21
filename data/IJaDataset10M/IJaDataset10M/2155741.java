package artic.gui;

import artic.model.ModelFacade;
import artic.util.Logger;
import javax.swing.text.Document;

public class EquationPanel extends javax.swing.JPanel {

    private EquationGroupListModel equationGroupListModel;

    private EquationListModel equationListModel;

    private Document nameTextFieldDocument = new RegExpValidatingDocument("\\S*");

    private Document formulaTextFieldDocument = new RegExpValidatingDocument("[ a-z0-9()+*/\\.\\-]*");

    /** Creates new form EquationPanel */
    public EquationPanel() {
        equationGroupListModel = new EquationGroupListModel();
        equationListModel = new EquationListModel();
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        groupLabel = new javax.swing.JLabel();
        equationLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        groupList = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        equationList = new javax.swing.JList();
        removeEquationButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        formulaLabel = new javax.swing.JLabel();
        formulaTextField = new javax.swing.JTextField();
        setEquationInfoButton = new javax.swing.JButton();
        addEquationButton = new javax.swing.JButton();
        commentLabel = new javax.swing.JLabel();
        commentTextField = new javax.swing.JTextField();
        setName("Form");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(artic.gui.ArticEditor.class).getContext().getResourceMap(EquationPanel.class);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel1.border.title")));
        jPanel1.setName("jPanel1");
        groupLabel.setText(resourceMap.getString("groupLabel.text"));
        groupLabel.setName("groupLabel");
        equationLabel.setText(resourceMap.getString("equationLabel.text"));
        equationLabel.setName("equationLabel");
        jScrollPane1.setName("jScrollPane1");
        groupList.setFont(resourceMap.getFont("groupList.font"));
        groupList.setModel(equationGroupListModel);
        groupList.setName("groupList");
        groupList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                groupListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(groupList);
        jScrollPane2.setName("jScrollPane2");
        equationList.setFont(resourceMap.getFont("equationList.font"));
        equationList.setModel(equationListModel);
        equationList.setName("equationList");
        equationList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                equationListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(equationList);
        removeEquationButton.setText(resourceMap.getString("removeEquationButton.text"));
        removeEquationButton.setName("removeEquationButton");
        removeEquationButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeEquationButtonActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().addContainerGap().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(groupLabel).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 196, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 339, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(equationLabel)).addContainerGap()).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(483, Short.MAX_VALUE).add(removeEquationButton).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1Layout.createSequentialGroup().add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(groupLabel).add(equationLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE).add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 201, Short.MAX_VALUE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(removeEquationButton).addContainerGap()));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("jPanel2.border.title")));
        jPanel2.setName("jPanel2");
        nameLabel.setText(resourceMap.getString("nameLabel.text"));
        nameLabel.setName("nameLabel");
        nameTextField.setDocument(nameTextFieldDocument);
        nameTextField.setFont(resourceMap.getFont("nameTextField.font"));
        nameTextField.setText(resourceMap.getString("nameTextField.text"));
        nameTextField.setName("nameTextField");
        formulaLabel.setText(resourceMap.getString("formulaLabel.text"));
        formulaLabel.setName("formulaLabel");
        formulaTextField.setDocument(formulaTextFieldDocument);
        formulaTextField.setFont(resourceMap.getFont("formulaTextField.font"));
        formulaTextField.setText(resourceMap.getString("formulaTextField.text"));
        formulaTextField.setName("formulaTextField");
        setEquationInfoButton.setText(resourceMap.getString("setEquationInfoButton.text"));
        setEquationInfoButton.setName("setEquationInfoButton");
        setEquationInfoButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setEquationInfoButtonActionPerformed(evt);
            }
        });
        addEquationButton.setText(resourceMap.getString("addEquationButton.text"));
        addEquationButton.setName("addEquationButton");
        addEquationButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEquationButtonActionPerformed(evt);
            }
        });
        commentLabel.setText(resourceMap.getString("commentLabel.text"));
        commentLabel.setName("commentLabel");
        commentTextField.setFont(resourceMap.getFont("commentTextField.font"));
        commentTextField.setText(resourceMap.getString("commentTextField.text"));
        commentTextField.setName("commentTextField");
        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().addContainerGap().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup().add(addEquationButton).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(setEquationInfoButton)).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(formulaLabel).add(nameLabel).add(commentLabel)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(formulaTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE).add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 228, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(commentTextField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 890, Short.MAX_VALUE)))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel2Layout.createSequentialGroup().add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(nameLabel).add(nameTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 18, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(formulaLabel).add(formulaTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(commentLabel).add(commentTextField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(setEquationInfoButton).add(addEquationButton)).addContainerGap()));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap().add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap()));
    }

    private void groupListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (groupList.isSelectionEmpty()) return;
        String equationGroup = (String) groupList.getSelectedValue();
        try {
            equationListModel.update(equationGroup);
        } catch (Exception e) {
            Logger.getInstance().error(e);
            return;
        }
        clearTextFields();
        equationList.clearSelection();
    }

    private void equationListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        if (groupList.isSelectionEmpty()) return;
        if (equationList.isSelectionEmpty()) return;
        EquationListModel.Item item = (EquationListModel.Item) equationList.getSelectedValue();
        nameTextField.setText(item.getEquationInfo().getName());
        commentTextField.setText(item.getEquationInfo().getComment());
        formulaTextField.setText(item.getEquationInfo().getFormula());
    }

    private void removeEquationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (groupList.isSelectionEmpty()) return;
        if (equationList.isSelectionEmpty()) {
            Logger.getInstance().warn("An equation must be selected.");
            return;
        }
        EquationListModel.Item item = (EquationListModel.Item) equationList.getSelectedValue();
        ModelFacade.EquationInfo info = item.getEquationInfo();
        try {
            ModelFacade.removeEquation(info.getGroupName(), info.getName());
            Logger.getInstance().info("Equation removed.");
            equationList.clearSelection();
            equationListModel.update(info.getGroupName());
            clearTextFields();
        } catch (Exception e) {
            Logger.getInstance().error(e);
        }
    }

    private void addEquationButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (groupList.isSelectionEmpty()) {
            Logger.getInstance().warn("A group must be selected.");
            return;
        }
        if (nameTextField.getText().length() == 0) {
            Logger.getInstance().warn("The equation name field is empty.");
            return;
        }
        String formula = formulaTextField.getText().trim();
        if (formula.length() == 0) {
            Logger.getInstance().warn("The equation formula field is empty.");
            return;
        }
        String groupName = (String) groupList.getSelectedValue();
        try {
            ModelFacade.addEquation(groupName, nameTextField.getText(), formula, commentTextField.getText());
            Logger.getInstance().info("Equation added.");
            equationList.clearSelection();
            equationListModel.update(groupName);
            clearTextFields();
        } catch (Exception e) {
            Logger.getInstance().error(e);
        }
    }

    private void setEquationInfoButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (groupList.isSelectionEmpty()) {
            Logger.getInstance().warn("A group must be selected.");
            return;
        }
        if (equationList.isSelectionEmpty()) {
            Logger.getInstance().warn("An equation must be selected.");
            return;
        }
        if (nameTextField.getText().length() == 0) {
            Logger.getInstance().warn("The equation name field is empty.");
            return;
        }
        String formula = formulaTextField.getText().trim();
        if (formula.length() == 0) {
            Logger.getInstance().warn("The equation formula field is empty.");
            return;
        }
        String groupName = (String) groupList.getSelectedValue();
        EquationListModel.Item item = (EquationListModel.Item) equationList.getSelectedValue();
        ModelFacade.EquationInfo info = item.getEquationInfo();
        try {
            ModelFacade.setEquationInfo(groupName, info.getName(), nameTextField.getText(), formula, commentTextField.getText());
            Logger.getInstance().info("Equation information changed.");
            equationList.clearSelection();
            equationListModel.update(groupName);
            clearTextFields();
        } catch (Exception e) {
            Logger.getInstance().error(e);
        }
    }

    private javax.swing.JButton addEquationButton;

    private javax.swing.JLabel commentLabel;

    private javax.swing.JTextField commentTextField;

    private javax.swing.JLabel equationLabel;

    private javax.swing.JList equationList;

    private javax.swing.JLabel formulaLabel;

    private javax.swing.JTextField formulaTextField;

    private javax.swing.JLabel groupLabel;

    private javax.swing.JList groupList;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JLabel nameLabel;

    private javax.swing.JTextField nameTextField;

    private javax.swing.JButton removeEquationButton;

    private javax.swing.JButton setEquationInfoButton;

    public void updateData() {
        clearTextFields();
        groupList.clearSelection();
        equationList.clearSelection();
        try {
            equationGroupListModel.update();
            equationListModel.update(null);
        } catch (Exception e) {
            Logger.getInstance().error(e);
        }
    }

    public String getSelectedEquation() {
        if (equationList.isSelectionEmpty()) return null;
        EquationListModel.Item item = (EquationListModel.Item) equationList.getSelectedValue();
        return item.getEquationInfo().getName();
    }

    private void clearTextFields() {
        nameTextField.setText(null);
        commentTextField.setText(null);
        formulaTextField.setText(null);
    }
}
