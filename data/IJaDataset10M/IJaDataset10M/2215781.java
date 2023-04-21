package dgp;

import frontend.GeneNetwork;
import frontend.ParameterFile;

/**
 *
 * @author  paresh
 */
public class GaussianBlurJPanel extends javax.swing.JPanel {

    private GeneNetwork gN;

    private GaussianBlur gB;

    /** Creates new form GaussianBlurJPanel */
    public GaussianBlurJPanel() {
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        scaleFactorjTextField = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        localDistancejTextField = new javax.swing.JTextField();
        localDistancejButton = new javax.swing.JButton();
        jLabel1.setText("Scaling factor");
        scaleFactorjTextField.setText("1.0");
        jToggleButton1.setText("Set Factor");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Global Blur");
        jCheckBox1.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        jCheckBox1.setMargin(new java.awt.Insets(0, 0, 0, 0));
        jCheckBox1.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jCheckBox1StateChanged(evt);
            }
        });
        localDistancejTextField.setText("3.0");
        localDistancejTextField.setEnabled(false);
        localDistancejButton.setText("Set Distance");
        localDistancejButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                localDistancejButtonActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(17, 17, 17).addComponent(localDistancejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(localDistancejButton)).addGroup(layout.createSequentialGroup().addGap(12, 12, 12).addComponent(scaleFactorjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jToggleButton1)).addComponent(jLabel1).addComponent(jCheckBox1)).addContainerGap(36, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(scaleFactorjTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jToggleButton1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCheckBox1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(localDistancejTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(localDistancejButton)).addContainerGap(405, Short.MAX_VALUE)));
    }

    private void localDistancejButtonActionPerformed(java.awt.event.ActionEvent evt) {
        gB.cutOffDistance = Double.parseDouble(localDistancejTextField.getText());
    }

    private void jCheckBox1StateChanged(javax.swing.event.ChangeEvent evt) {
        if (jCheckBox1.isSelected()) {
            gB.globalDistance = true;
            localDistancejTextField.setEnabled(false);
            localDistancejButton.setEnabled(false);
        } else {
            gB.globalDistance = false;
            localDistancejTextField.setEnabled(true);
            localDistancejButton.setEnabled(true);
        }
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        gB.expScaleK = Double.parseDouble(scaleFactorjTextField.getText());
    }

    public void setUp(GeneNetwork g, GaussianBlur g1) {
        gN = g;
        gB = g1;
    }

    public void initPFile(ParameterFile pFile) {
        if (pFile == null) return;
        double cutOffDistance = Double.parseDouble((String) pFile.get("cutOffDistance"));
        int iglobalDistance = Integer.parseInt((String) pFile.get("globalDistance"));
        double expScaleK = Double.parseDouble((String) pFile.get("expScaleK"));
        gB.cutOffDistance = cutOffDistance;
        gB.globalDistance = (iglobalDistance == 1);
        gB.expScaleK = expScaleK;
    }

    private javax.swing.JCheckBox jCheckBox1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JToggleButton jToggleButton1;

    private javax.swing.JButton localDistancejButton;

    private javax.swing.JTextField localDistancejTextField;

    private javax.swing.JTextField scaleFactorjTextField;
}
