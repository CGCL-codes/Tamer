package jmash;

import javax.swing.JPanel;

/**
 *
 * @author  Alessandro
 */
public class AlcoolContent extends javax.swing.JInternalFrame {

    private static final long serialVersionUID = 5747138793857156261L;

    public JPanel getResultPanel() {
        return jPanel1;
    }

    public void setOG(double OG) {
        fldOG.setGravity(OG);
    }

    public void setFG(double FG) {
        fldFG.setGravity(FG);
    }

    public AlcoolContent() {
        initComponents();
        this.fldOG.setModelFormat(1.040, 1, 2, 0.001, "0.000", "AlcoolContent.fldOG");
        this.fldFG.setModelFormat(1.010, 1.0, 2, 0.001, "0.000", "AlcoolContent.fldFG");
        this.fldRG.setModelFormat(1.010, 1.0, 2, 0.001, "0.000", "AlcoolContent.fldRG");
        this.fldAA.setModelFormat(1.0, 1.0, 100.0, 0.001, "0.0 %", "AlcoolContent.fldAA");
        this.fldAR.setModelFormat(1.0, 1.0, 100.0, 0.001, "0.0 %", "AlcoolContent.fldAR");
        this.fldAlc.setModelFormat(1.0, 0.0, 100.0, 0.001, "0.0 %", null);
        this.fldAlcP.setModelFormat(1.0, 0.0, 100.0, 0.001, "0.0 %", null);
        this.fldCal.setModelFormat(1.0, 0.0, 1000000000.0, 0.1, "0.0 cal/L", null);
        setBorder(Utils.getDefaultBorder());
        setBackground(getBackground().darker());
        calc();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        fldOG = new jmash.component.JGravitySpinner();
        fldFG = new jmash.component.JGravitySpinner();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        fldAA = new jmash.component.JMashSpinner();
        jLabel5 = new javax.swing.JLabel();
        fldAR = new jmash.component.JMashSpinner();
        jLabel6 = new javax.swing.JLabel();
        fldAlc = new jmash.component.JMashSpinner();
        jLabel3 = new javax.swing.JLabel();
        fldRG = new jmash.component.JGravitySpinner();
        jButton1 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        fldCal = new jmash.component.JMashSpinner();
        jLabel8 = new javax.swing.JLabel();
        fldAlcP = new jmash.component.JMashSpinner();
        getContentPane().setLayout(new java.awt.GridBagLayout());
        setClosable(true);
        setIconifiable(true);
        setTitle("Alcool");
        jPanel2.setLayout(new java.awt.GridBagLayout());
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Dati letture"));
        jPanel2.setPreferredSize(new java.awt.Dimension(320, 80));
        jLabel1.setText("Original Gravity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel2.add(jLabel1, gridBagConstraints);
        jLabel2.setText("Final Gravity");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel2.add(jLabel2, gridBagConstraints);
        fldOG.setPreferredSize(new java.awt.Dimension(128, 18));
        fldOG.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fldOGStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(fldOG, gridBagConstraints);
        fldFG.setPreferredSize(new java.awt.Dimension(128, 18));
        fldFG.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                fldFGStateChanged(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel2.add(fldFG, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        getContentPane().add(jPanel2, gridBagConstraints);
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Risultati"));
        jPanel1.setPreferredSize(new java.awt.Dimension(320, 180));
        jPanel1.setRequestFocusEnabled(false);
        jLabel4.setText("Attenuazione apparente");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel4, gridBagConstraints);
        fldAA.setEnabled(false);
        fldAA.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(fldAA, gridBagConstraints);
        jLabel5.setText("Attenuazione reale");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel5, gridBagConstraints);
        fldAR.setEnabled(false);
        fldAR.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 20;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(fldAR, gridBagConstraints);
        jLabel6.setText("Alcool v/v");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel6, gridBagConstraints);
        fldAlc.setEnabled(false);
        fldAlc.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(fldAlc, gridBagConstraints);
        jLabel3.setText("FG reale");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel3, gridBagConstraints);
        fldRG.setEnabled(false);
        fldRG.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(fldRG, gridBagConstraints);
        jButton1.setText("?");
        jButton1.setPreferredSize(new java.awt.Dimension(39, 18));
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new java.awt.GridBagConstraints());
        jLabel7.setText("Calorie");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel7, gridBagConstraints);
        fldCal.setEnabled(false);
        fldCal.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(fldCal, gridBagConstraints);
        jLabel8.setText("Alcool p/p");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        jPanel1.add(jLabel8, gridBagConstraints);
        fldAlcP.setEnabled(false);
        fldAlcP.setPreferredSize(new java.awt.Dimension(128, 20));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(2, 2, 2, 2);
        jPanel1.add(fldAlcP, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 10);
        getContentPane().add(jPanel1, gridBagConstraints);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 368) / 2, (screenSize.height - 338) / 2, 368, 338);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        new ShowFormula("", new String[] { "FG_plato^reale=\\frac{\\left(0.22 + 0.001 * OG_plato\\right)*OG_plato+FG_plato}{1+\\left(0.22 + 0.001 * OG_plato\\right)}", "Att_apparente=\\frac{OG_plato-FG_plato}{OG_plato}", "Att_reale=\\frac{OG_plato-FG_plato^reale}{OG_plato}" }).startModal(this);
    }

    private void fldFGStateChanged(javax.swing.event.ChangeEvent evt) {
        calc();
    }

    private void fldOGStateChanged(javax.swing.event.ChangeEvent evt) {
        calc();
    }

    private void calc() {
        double og = fldOG.getGravity();
        double fg = fldFG.getGravity();
        double oe = Utils.SG2Plato(og);
        double ae = Utils.SG2Plato(fg);
        double q = 0.22 + 0.001 * oe;
        double re = (q * oe + ae) / (1 + q);
        double rsg = Utils.Plato2SG(re);
        double aa = (oe - ae) / oe;
        double ra = (oe - re) / oe;
        double aw = 0.01 * (oe - re) / (2.0665 - 0.010665 * oe);
        double cal = (6.9 * aa + 4.0 * (re - 0.1)) * 10 * fg;
        double av = aw * fg / 0.794;
        this.fldRG.setGravity(rsg);
        this.fldAA.setDoubleValue(aa);
        this.fldAR.setDoubleValue(ra);
        this.fldAlc.setDoubleValue(av);
        this.fldAlcP.setDoubleValue(aw);
        this.fldCal.setDoubleValue(cal);
    }

    private jmash.component.JMashSpinner fldAA;

    private jmash.component.JMashSpinner fldAR;

    private jmash.component.JMashSpinner fldAlc;

    private jmash.component.JMashSpinner fldAlcP;

    private jmash.component.JMashSpinner fldCal;

    private jmash.component.JGravitySpinner fldFG;

    private jmash.component.JGravitySpinner fldOG;

    private jmash.component.JGravitySpinner fldRG;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
