package pogo.appli;

import pogo.gene.DevState;
import pogo.gene.DevStateTable;
import pogo.gene.PogoDefs;
import javax.swing.*;
import java.util.Vector;

public class NotAllowedDialog extends javax.swing.JDialog implements PogoAppliDefs, PogoDefs {

    private static int returnStatus = PogoAppliDefs.RET_CANCEL;

    private Vector stVector;

    public NotAllowedDialog(JFrame parent, String allow_what, Vector stVect) {
        super(parent, true);
        stVector = stVect;
        initComponents();
        setTitle(allow_what + " Allowed For States");
        nameLbl.setText(allow_what + " Allowed For States:");
        pack();
        PogoAppli.centerDialog(this, parent);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        okBtn = new javax.swing.JButton();
        cancelBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        nameLbl = new javax.swing.JLabel();
        setBackground(new java.awt.Color(198, 178, 168));
        setTitle("Command Allowed Window");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });
        jPanel1.setLayout(new java.awt.FlowLayout(2, 5, 5));
        okBtn.setText("OK");
        okBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });
        jPanel1.add(okBtn);
        cancelBtn.setText("Cancel");
        cancelBtn.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelBtnActionPerformed(evt);
            }
        });
        jPanel1.add(cancelBtn);
        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);
        jPanel2.setLayout(new java.awt.GridBagLayout());
        java.awt.GridBagConstraints gridBagConstraints1;
        nameLbl.setText("Command Allowed for Selected States: ");
        nameLbl.setForeground(java.awt.Color.black);
        nameLbl.setFont(new java.awt.Font("Arial", 1, 14));
        nameLbl.setPreferredSize(new java.awt.Dimension(400, 40));
        gridBagConstraints1 = new java.awt.GridBagConstraints();
        gridBagConstraints1.gridx = 1;
        gridBagConstraints1.gridy = 1;
        gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
        jPanel2.add(nameLbl, gridBagConstraints1);
        radioBtn = new JRadioButton[stVector.size()];
        for (int i = 0; i < stVector.size(); i++) {
            DevState state = (DevState) stVector.elementAt(i);
            radioBtn[i] = new JRadioButton(state.name);
            radioBtn[i].setFont(new java.awt.Font("Arial", 1, 12));
            radioBtn[i].setSelected(true);
            gridBagConstraints1.gridx = 1;
            gridBagConstraints1.gridy = i + 2;
            gridBagConstraints1.anchor = java.awt.GridBagConstraints.WEST;
            jPanel2.add(radioBtn[i], gridBagConstraints1);
        }
        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
    }

    private void cancelBtnActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(PogoAppliDefs.RET_CANCEL);
    }

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {
        doClose(PogoAppliDefs.RET_OK);
    }

    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {
        doClose(PogoAppliDefs.RET_CANCEL);
    }

    public int showDialog(DevStateTable stTable) {
        if (stTable != null) for (int i = 0; i < stTable.size(); i++) for (int j = 0; j < radioBtn.length; j++) {
            DevState state = (DevState) stTable.elementAt(i);
            if (state.name.equals(radioBtn[j].getText()) == true) radioBtn[j].setSelected(false);
        }
        setVisible(true);
        return returnStatus;
    }

    private void doClose(int retStatus) {
        returnStatus = retStatus;
        setVisible(false);
        dispose();
    }

    public DevStateTable getInput() {
        Vector forbStates = new Vector();
        for (int i = 0; i < stVector.size(); i++) if (radioBtn[i].getSelectedObjects() == null) forbStates.addElement(stVector.elementAt(i));
        return new DevStateTable(forbStates);
    }

    private JRadioButton radioBtn[];

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton okBtn;

    private javax.swing.JButton cancelBtn;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JLabel nameLbl;
}
