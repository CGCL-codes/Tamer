package ge.telasi.tasks.ui.flowpermission;

import ge.telasi.tasks.model.FlowPermission;

/**
 * @author dimitri
 */
public class FlowPermissionPanel extends javax.swing.JPanel {

    public FlowPermissionPanel() {
        initComponents();
        enableButtons();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        cmbOperation = new ge.telasi.tasks.ui.flowpermission.FlowOperationCombo();
        jLabel2 = new javax.swing.JLabel();
        cmbRole = new ge.telasi.tasks.ui.flowpermission.FlowRoleCombo();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cmbInitStatus = new ge.telasi.tasks.ui.flowpermission.TaskStatusCombo();
        cmbInitStatus.setDefaultModel();
        cmbFinalStatus = new ge.telasi.tasks.ui.flowpermission.TaskStatusCombo();
        cmbFinalStatus.setDefaultModel();
        jLabel1.setText("ოპერაცია:");
        cmbOperation.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbOperationActionPerformed(evt);
            }
        });
        jLabel2.setText("როლი:");
        jLabel3.setText("დავალების სტატუსი:");
        jLabel4.setText("საბოლოო სტატუსი:");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2).addComponent(jLabel1).addComponent(jLabel3).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(cmbInitStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cmbFinalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(cmbOperation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(cmbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(cmbInitStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(cmbFinalStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void cmbOperationActionPerformed(java.awt.event.ActionEvent evt) {
        enableButtons();
    }

    private ge.telasi.tasks.ui.flowpermission.TaskStatusCombo cmbFinalStatus;

    private ge.telasi.tasks.ui.flowpermission.TaskStatusCombo cmbInitStatus;

    private ge.telasi.tasks.ui.flowpermission.FlowOperationCombo cmbOperation;

    private ge.telasi.tasks.ui.flowpermission.FlowRoleCombo cmbRole;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private Integer taskType;

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    public FlowPermission getFlowPermission() {
        FlowPermission formObject = new FlowPermission();
        formObject.setOperation(cmbOperation.getSelection());
        formObject.setRole(cmbRole.getSelection());
        formObject.setTaskStatus(cmbInitStatus.getSelection());
        if (formObject.getOperation() == FlowPermission.OPERATION_CHANGE_STATUS) {
            formObject.setFinalStatus(cmbFinalStatus.getSelection());
        }
        formObject.setTaskType(taskType);
        return formObject;
    }

    private void enableButtons() {
        boolean changeStatus = cmbOperation.getSelection() == FlowPermission.OPERATION_CHANGE_STATUS;
        cmbOperation.setEnabled(true);
        cmbRole.setEnabled(true);
        cmbInitStatus.setEnabled(true);
        cmbFinalStatus.setEnabled(true && changeStatus);
        cmbRole.setOperation(cmbOperation.getSelection());
    }
}
