package org.scrinch.gui;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.scrinch.model.OriginType;
import org.scrinch.model.OriginTypeFactory;
import org.scrinch.model.ScrinchEnvToolkit;
import org.scrinch.model.WorkType;
import org.scrinch.model.WorkTypeFactory;

public class PreferencesDialog extends javax.swing.JDialog {

    private int nextWorkTypeId = WorkTypeFactory.getInstance().getWorkTypeList().size();

    private int nextOriginTypeId = OriginTypeFactory.getInstance().getOriginTypeList().size();

    private List<WorkType> workTypeListToBeAdded = new ArrayList<WorkType>();

    private List<OriginType> originTypeListToBeAdded = new ArrayList<OriginType>();

    private List<WorkTypePanel> workTypePanelList = new ArrayList<WorkTypePanel>();

    private List<OriginTypePanel> originTypePanelList = new ArrayList<OriginTypePanel>();

    public PreferencesDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setPreferredSize(new Dimension(800, 400));
        for (WorkType workTypeIterator : WorkTypeFactory.getInstance().getWorkTypeList()) {
            if (!workTypeIterator.equals(WorkTypeFactory.getInstance().getDefaultWorkType())) {
                this.addWorkType(workTypeIterator);
            }
        }
        for (OriginType originTypeIterator : OriginTypeFactory.getInstance().getOriginTypeList()) {
            if (!originTypeIterator.equals(OriginTypeFactory.getInstance().getDefaultOriginType())) {
                this.addOriginType(originTypeIterator);
            }
        }
        if (ScrinchEnvToolkit.getInstance().isBurnUpChart()) {
            burnUpChartRadioButton.setSelected(true);
        } else {
            burnDownChartRadioButton.setSelected(true);
        }
    }

    private void cancelChanges() {
        this.dispose();
    }

    private boolean changesConfirmed() {
        boolean changesConfirmed = true;
        StringBuffer warning = new StringBuffer("");
        for (WorkTypePanel workTypePanelIterator : workTypePanelList) {
            WorkType workType = workTypePanelIterator.getType();
            if (workTypePanelIterator.toBeRemoved()) {
                if (warning.length() > 0) {
                    warning.append(", ");
                }
                warning.append(workType.getLabel());
            }
        }
        for (OriginTypePanel originTypePanelIterator : originTypePanelList) {
            OriginType originType = originTypePanelIterator.getType();
            if (originTypePanelIterator.toBeRemoved()) {
                if (warning.length() > 0) {
                    warning.append(", ");
                }
                warning.append(originType.getLabel());
            }
        }
        if (warning.length() > 0) {
            changesConfirmed = false;
            warning.insert(0, "You are about to delete following types : ");
            warning.append(".\nAll items using those types will be set to Undefined.");
            int optionChoosen = JOptionPane.showConfirmDialog(this, warning, "Are you sure ?", JOptionPane.YES_NO_OPTION);
            if (optionChoosen == JOptionPane.OK_OPTION) {
                changesConfirmed = true;
            }
        }
        return changesConfirmed;
    }

    private void doChanges() {
        for (WorkType workTypeToBeAdded : workTypeListToBeAdded) {
            WorkTypeFactory.getInstance().addWorkType(workTypeToBeAdded);
        }
        for (OriginType originTypeToBeAdded : originTypeListToBeAdded) {
            OriginTypeFactory.getInstance().addOriginType(originTypeToBeAdded);
        }
        for (WorkTypePanel workTypePanelIterator : workTypePanelList) {
            WorkType workType = workTypePanelIterator.getType();
            if (workTypePanelIterator.toBeRemoved()) {
                WorkTypeFactory.getInstance().dispose(workType);
            } else {
                workType.setDescription(workTypePanelIterator.getDescription());
                workType.setLabel(workTypePanelIterator.getLabel());
            }
        }
        for (OriginTypePanel originTypePanelIterator : originTypePanelList) {
            OriginType originType = originTypePanelIterator.getType();
            if (originTypePanelIterator.toBeRemoved()) {
                OriginTypeFactory.getInstance().dispose(originType);
            } else {
                originType.setDescription(originTypePanelIterator.getDescription());
                originType.setLabel(originTypePanelIterator.getLabel());
            }
        }
        ScrinchEnvToolkit.getInstance().setBurnUpChart(burnUpChartRadioButton.isSelected());
        this.dispose();
    }

    private void createNewWorkType() {
        WorkType newWorkType = new WorkType("Work Type #" + nextWorkTypeId++);
        workTypeListToBeAdded.add(newWorkType);
        addWorkType(newWorkType);
    }

    private void addWorkType(WorkType workType) {
        WorkTypePanel newWorkTypePanel = new WorkTypePanel(this);
        newWorkTypePanel.setType(workType);
        addWorkTypePanel(newWorkTypePanel);
    }

    private void addWorkTypePanel(WorkTypePanel panel) {
        workTypePanelList.add(panel);
        workTypePanel.add(panel);
        workTypePanel.updateUI();
    }

    private void createNewOriginType() {
        OriginType newOriginType = new OriginType("Origin Type #" + nextOriginTypeId++);
        originTypeListToBeAdded.add(newOriginType);
        addOriginType(newOriginType);
    }

    private void addOriginType(OriginType originType) {
        OriginTypePanel newOriginTypePanel = new OriginTypePanel(this);
        newOriginTypePanel.setType(originType);
        addOriginTypePanel(newOriginTypePanel);
    }

    private void addOriginTypePanel(OriginTypePanel panel) {
        originTypePanelList.add(panel);
        originTypePanel.add(panel);
        originTypePanel.updateUI();
    }

    private void initComponents() {
        burnChartButtonGroup = new javax.swing.ButtonGroup();
        jPanel8 = new javax.swing.JPanel();
        burnUpChartRadioButton = new javax.swing.JRadioButton();
        burnDownChartRadioButton = new javax.swing.JRadioButton();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        originTypePanel = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        addOriginTypeButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        workTypePanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        addWorkTypeButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        acceptChangesButton = new javax.swing.JButton();
        cancelChangesButton = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Burn UP/DOWN Chart"));
        burnChartButtonGroup.add(burnUpChartRadioButton);
        burnUpChartRadioButton.setText("UP");
        jPanel8.add(burnUpChartRadioButton);
        burnChartButtonGroup.add(burnDownChartRadioButton);
        burnDownChartRadioButton.setText("DOWN");
        jPanel8.add(burnDownChartRadioButton);
        getContentPane().add(jPanel8, java.awt.BorderLayout.NORTH);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Origin types"));
        jPanel2.setLayout(new java.awt.BorderLayout());
        jScrollPane1.setBorder(null);
        originTypePanel.setLayout(new javax.swing.BoxLayout(originTypePanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(originTypePanel);
        jPanel2.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        addOriginTypeButton.setText("NEW");
        addOriginTypeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addOriginTypeButtonActionPerformed(evt);
            }
        });
        jPanel5.add(addOriginTypeButton);
        jPanel2.add(jPanel5, java.awt.BorderLayout.SOUTH);
        jPanel1.add(jPanel2);
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Work types"));
        jPanel3.setLayout(new java.awt.BorderLayout());
        jScrollPane2.setBorder(null);
        workTypePanel.setLayout(new javax.swing.BoxLayout(workTypePanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane2.setViewportView(workTypePanel);
        jPanel3.add(jScrollPane2, java.awt.BorderLayout.CENTER);
        addWorkTypeButton.setText("NEW");
        addWorkTypeButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addWorkTypeButtonActionPerformed(evt);
            }
        });
        jPanel6.add(addWorkTypeButton);
        jPanel3.add(jPanel6, java.awt.BorderLayout.SOUTH);
        jPanel1.add(jPanel3);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jPanel4.setLayout(new java.awt.GridLayout(2, 0));
        jLabel1.setText("NB : checked types will be removed. Related item types will be set to Undefined.");
        jPanel4.add(jLabel1);
        acceptChangesButton.setText("OK");
        acceptChangesButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                acceptChangesButtonActionPerformed(evt);
            }
        });
        jPanel7.add(acceptChangesButton);
        cancelChangesButton.setText("Cancel");
        cancelChangesButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelChangesButtonActionPerformed(evt);
            }
        });
        jPanel7.add(cancelChangesButton);
        jPanel4.add(jPanel7);
        getContentPane().add(jPanel4, java.awt.BorderLayout.SOUTH);
        pack();
    }

    private void acceptChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        if (changesConfirmed()) {
            doChanges();
        }
    }

    private void addWorkTypeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createNewWorkType();
    }

    private void cancelChangesButtonActionPerformed(java.awt.event.ActionEvent evt) {
        cancelChanges();
    }

    private void addOriginTypeButtonActionPerformed(java.awt.event.ActionEvent evt) {
        createNewOriginType();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new PreferencesDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JButton acceptChangesButton;

    private javax.swing.JButton addOriginTypeButton;

    private javax.swing.JButton addWorkTypeButton;

    private javax.swing.ButtonGroup burnChartButtonGroup;

    private javax.swing.JRadioButton burnDownChartRadioButton;

    private javax.swing.JRadioButton burnUpChartRadioButton;

    private javax.swing.JButton cancelChangesButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JPanel jPanel6;

    private javax.swing.JPanel jPanel7;

    private javax.swing.JPanel jPanel8;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JPanel originTypePanel;

    private javax.swing.JPanel workTypePanel;
}
