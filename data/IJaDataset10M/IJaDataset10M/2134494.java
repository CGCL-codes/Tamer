package com.pbxworkbench.campaign.ui.swing.campwiz;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFileChooser;
import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.swing.EventListModel;
import com.pbxworkbench.campaign.domain.Callee;
import com.pbxworkbench.campaign.ui.Helper;
import com.pbxworkbench.commons.ui.swing.BaseFormPanel;

/**
 * 
 * @author __USER__
 */
public class NumberListEditorPanel extends BaseFormPanel {

    private EventListModel<Callee> calleesListModel;

    private final List<Callee> calleeList;

    private BasicEventList<Callee> calleeEvtList;

    /** Creates new form NewCalleeGroupPanel */
    public NumberListEditorPanel(List<Callee> calleeList) {
        super("Create Callee List");
        this.calleeList = calleeList;
        calleeEvtList = new BasicEventList<Callee>();
        calleeEvtList.addAll(calleeList);
        calleesListModel = new EventListModel<Callee>(calleeEvtList);
        initComponents();
        removeBtn.setEnabled(false);
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        numberList = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        toAddField = new javax.swing.JTextField();
        addBtn = new javax.swing.JButton();
        importBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        numberList.setModel(calleesListModel);
        numberList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {

            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                numberListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(numberList);
        jLabel1.setText("Numbers");
        jLabel4.setText("Enter comma-separated list of numbers and press \"Add\"");
        addBtn.setText("Add");
        addBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });
        importBtn.setText("Import From CSV");
        importBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importBtnActionPerformed(evt);
            }
        });
        removeBtn.setText("Remove");
        removeBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(toAddField, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 212, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(addBtn, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 62, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(69, 69, 69)).add(jLabel1).add(layout.createSequentialGroup().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(layout.createSequentialGroup().add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 212, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(removeBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(importBtn, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addContainerGap()));
        layout.linkSize(new java.awt.Component[] { jScrollPane1, toAddField }, org.jdesktop.layout.GroupLayout.HORIZONTAL);
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(toAddField, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(addBtn)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel1).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 138, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(layout.createSequentialGroup().add(importBtn).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(removeBtn))).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private void numberListValueChanged(javax.swing.event.ListSelectionEvent evt) {
        removeBtn.setEnabled(!numberList.isSelectionEmpty());
    }

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String commaSepNumbers = toAddField.getText();
        List<Callee> newCallees = Helper.parseCallees(commaSepNumbers);
        calleeEvtList.addAll(newCallees);
        calleeList.addAll(newCallees);
        toAddField.setText("");
        toAddField.requestFocus();
        fireChanged();
    }

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {
        Object[] calleesToDelete = numberList.getSelectedValues();
        List<Object> toDelete = Arrays.asList(calleesToDelete);
        calleeList.removeAll(toDelete);
        calleeEvtList.removeAll(toDelete);
        fireChanged();
    }

    private void importBtnActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser("Open CSV File");
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            List<Callee> newCallees = Helper.parseCallees(file);
            calleeList.addAll(newCallees);
            calleeEvtList.addAll(newCallees);
        }
        fireChanged();
    }

    private javax.swing.JButton addBtn;

    private javax.swing.JButton importBtn;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JList numberList;

    private javax.swing.JButton removeBtn;

    private javax.swing.JTextField toAddField;

    public String validateForm() {
        if (calleeList.isEmpty()) {
            return "List of callees is empty";
        } else return null;
    }
}
