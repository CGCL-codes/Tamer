package newgen.presentation.holdings;

/**
 *
 * @author  root
 */
public class EnumChronoFilterPanel extends javax.swing.JPanel {

    /** Creates new form EnumChronoFilterPanel */
    public EnumChronoFilterPanel() {
        initComponents();
    }

    public java.util.Hashtable getEnumerationChronoValues() {
        java.util.Hashtable ht = new java.util.Hashtable();
        if (rbCurrentAll.isSelected()) {
            ht.put("Current", "C");
        } else if (jRadioButton2.isSelected()) {
            ht.put("Current", "B");
        } else if (jRadioButton3.isSelected()) {
            ht.put("Current", "A");
        }
        if (jRadioButton4.isSelected()) {
            ht.put("Compress", "C");
        } else if (jRadioButton5.isSelected()) {
            ht.put("Compress", "A");
        } else if (jRadioButton6.isSelected()) {
            ht.put("Compress", "B");
        }
        if (jRadioButton7.isSelected()) {
            ht.put("Bound", "C");
        } else if (jRadioButton8.isSelected()) {
            ht.put("Bound", "A");
        } else if (jRadioButton9.isSelected()) {
            ht.put("Bound", "B");
        }
        if (jRadioButton10.isSelected()) {
            ht.put("LibHolding", "C");
        } else if (jRadioButton11.isSelected()) {
            ht.put("LibHolding", "A");
        } else if (jRadioButton12.isSelected()) {
            ht.put("LibHolding", "B");
        }
        return ht;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        rbCurrentAll = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jRadioButton3 = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        jRadioButton4 = new javax.swing.JRadioButton();
        jRadioButton5 = new javax.swing.JRadioButton();
        jRadioButton6 = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jRadioButton7 = new javax.swing.JRadioButton();
        jRadioButton8 = new javax.swing.JRadioButton();
        jRadioButton9 = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        jRadioButton10 = new javax.swing.JRadioButton();
        jRadioButton11 = new javax.swing.JRadioButton();
        jRadioButton12 = new javax.swing.JRadioButton();
        setLayout(new java.awt.GridBagLayout());
        jLabel1.setText("Current/Archived");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel1, gridBagConstraints);
        buttonGroup1.add(rbCurrentAll);
        rbCurrentAll.setSelected(true);
        rbCurrentAll.setText("All");
        rbCurrentAll.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCurrentAllActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        add(rbCurrentAll, gridBagConstraints);
        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setText("Current");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jRadioButton2, gridBagConstraints);
        buttonGroup1.add(jRadioButton3);
        jRadioButton3.setText("Archived");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jRadioButton3, gridBagConstraints);
        jLabel2.setText("Compress type");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel2, gridBagConstraints);
        buttonGroup2.add(jRadioButton4);
        jRadioButton4.setSelected(true);
        jRadioButton4.setText("All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        add(jRadioButton4, gridBagConstraints);
        buttonGroup2.add(jRadioButton5);
        jRadioButton5.setText("Compressed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jRadioButton5, gridBagConstraints);
        buttonGroup2.add(jRadioButton6);
        jRadioButton6.setText("Not compressed");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jRadioButton6, gridBagConstraints);
        jLabel3.setText("Bound status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel3, gridBagConstraints);
        buttonGroup3.add(jRadioButton7);
        jRadioButton7.setSelected(true);
        jRadioButton7.setText("All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        add(jRadioButton7, gridBagConstraints);
        buttonGroup3.add(jRadioButton8);
        jRadioButton8.setText("Bound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jRadioButton8, gridBagConstraints);
        buttonGroup3.add(jRadioButton9);
        jRadioButton9.setText("Un bound");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jRadioButton9, gridBagConstraints);
        jLabel4.setText("Library holdings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        add(jLabel4, gridBagConstraints);
        buttonGroup4.add(jRadioButton10);
        jRadioButton10.setText("All");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        add(jRadioButton10, gridBagConstraints);
        buttonGroup4.add(jRadioButton11);
        jRadioButton11.setSelected(true);
        jRadioButton11.setText("This Library holdings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        add(jRadioButton11, gridBagConstraints);
        buttonGroup4.add(jRadioButton12);
        jRadioButton12.setText("Other than this library holdings");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 3;
        add(jRadioButton12, gridBagConstraints);
    }

    private void rbCurrentAllActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private javax.swing.ButtonGroup buttonGroup1;

    private javax.swing.ButtonGroup buttonGroup2;

    private javax.swing.ButtonGroup buttonGroup3;

    private javax.swing.ButtonGroup buttonGroup4;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JRadioButton jRadioButton10;

    private javax.swing.JRadioButton jRadioButton11;

    private javax.swing.JRadioButton jRadioButton12;

    private javax.swing.JRadioButton jRadioButton2;

    private javax.swing.JRadioButton jRadioButton3;

    private javax.swing.JRadioButton jRadioButton4;

    private javax.swing.JRadioButton jRadioButton5;

    private javax.swing.JRadioButton jRadioButton6;

    private javax.swing.JRadioButton jRadioButton7;

    private javax.swing.JRadioButton jRadioButton8;

    private javax.swing.JRadioButton jRadioButton9;

    private javax.swing.JRadioButton rbCurrentAll;
}
