package dgp;

import frontend.GeneNetwork;

/**
 *
 * @author  paresh
 */
public class LcFilterJPanel extends javax.swing.JPanel {

    private GeneNetwork gN;

    private LCFilter lcf;

    /** Creates new form LcFilterJPanel */
    public LcFilterJPanel() {
        initComponents();
    }

    public void setUp(GeneNetwork g, LCFilter l) {
        gN = g;
        lcf = l;
        logNextMatrixjCheckBox.setSelected(lcf.logNextMatrix);
        kValuejTextField.setText("" + lcf.kValue);
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        logNextMatrixjCheckBox = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        kValuejTextField = new javax.swing.JTextField();
        setkValuejButton = new javax.swing.JButton();
        jLabel1.setText("Logging:");
        logNextMatrixjCheckBox.setText("next Matrix");
        logNextMatrixjCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        logNextMatrixjCheckBox.setMargin(new java.awt.Insets(0, 0, 0, 0));
        logNextMatrixjCheckBox.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                logNextMatrixjCheckBoxStateChanged(evt);
            }
        });
        jLabel2.setText("kValue :");
        kValuejTextField.setText("1.0");
        setkValuejButton.setText("Set kValue");
        setkValuejButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setkValuejButtonMouseReleased(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(logNextMatrixjCheckBox)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(kValuejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(setkValuejButton))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(kValuejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(setkValuejButton)).addGap(31, 31, 31).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(logNextMatrixjCheckBox).addContainerGap(506, Short.MAX_VALUE)));
    }

    private void setkValuejButtonMouseReleased(java.awt.event.MouseEvent evt) {
        lcf.kValue = Double.parseDouble(kValuejTextField.getText());
    }

    private void logNextMatrixjCheckBoxStateChanged(javax.swing.event.ChangeEvent evt) {
        lcf.logNextMatrix = logNextMatrixjCheckBox.isSelected();
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JTextField kValuejTextField;

    private javax.swing.JCheckBox logNextMatrixjCheckBox;

    private javax.swing.JButton setkValuejButton;
}
