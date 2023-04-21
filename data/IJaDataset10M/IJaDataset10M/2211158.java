package gui;

/**
 *
 * @author  Reza Alavi
 */
public class ShowMeaningPanel extends javax.swing.JPanel {

    /** Creates new form ShowMeaningPanel */
    public ShowMeaningPanel() {
        initComponents();
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        ShowMeaningChkBox = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        MeaningBody = new javax.swing.JEditorPane();
        jPanel1 = new javax.swing.JPanel();
        PassedBT = new javax.swing.JButton();
        FailedBT = new javax.swing.JButton();
        SkipBT = new javax.swing.JButton();
        setLayout(new java.awt.GridBagLayout());
        ShowMeaningChkBox.setText("Show Meaning");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(ShowMeaningChkBox, gridBagConstraints);
        jScrollPane1.setViewportView(MeaningBody);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(jScrollPane1, gridBagConstraints);
        PassedBT.setText("Passed");
        jPanel1.add(PassedBT);
        FailedBT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons/remove.gif")));
        FailedBT.setText("Failed");
        jPanel1.add(FailedBT);
        SkipBT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/resource/icons/learn.gif")));
        SkipBT.setText("Skip card");
        jPanel1.add(SkipBT);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        add(jPanel1, gridBagConstraints);
    }

    private javax.swing.JButton FailedBT;

    private javax.swing.JEditorPane MeaningBody;

    private javax.swing.JButton PassedBT;

    private javax.swing.JCheckBox ShowMeaningChkBox;

    private javax.swing.JButton SkipBT;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;
}
