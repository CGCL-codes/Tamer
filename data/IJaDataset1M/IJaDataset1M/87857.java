package orgii_micro_uff;

/**
 *
 * @author  Heliomar kann
 */
public class Detalhes extends javax.swing.JDialog {

    /** Creates new form Detalhes */
    public Detalhes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        jppasso = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtInstrucao = new javax.swing.JTextField();
        txtMneumonico = new javax.swing.JTextField();
        btnContinuar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Detalhes");
        setModal(true);
        setName("jddetalhes");
        setResizable(false);
        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel16.setText("Instrução");
        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14));
        jLabel17.setText("Mnemônico");
        txtInstrucao.setEditable(false);
        txtMneumonico.setEditable(false);
        btnContinuar.setText("Continuar");
        btnContinuar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnContinuarActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout jppassoLayout = new org.jdesktop.layout.GroupLayout(jppasso);
        jppasso.setLayout(jppassoLayout);
        jppassoLayout.setHorizontalGroup(jppassoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jppassoLayout.createSequentialGroup().add(48, 48, 48).add(jppassoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jLabel16).add(jLabel17)).add(14, 14, 14).add(jppassoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, jppassoLayout.createSequentialGroup().add(txtInstrucao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 229, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addContainerGap(75, Short.MAX_VALUE)).add(jppassoLayout.createSequentialGroup().add(txtMneumonico, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 87, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 128, Short.MAX_VALUE).add(btnContinuar).addContainerGap()))));
        jppassoLayout.setVerticalGroup(jppassoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jppassoLayout.createSequentialGroup().addContainerGap().add(jppassoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel16).add(txtInstrucao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).addContainerGap(57, Short.MAX_VALUE)).add(org.jdesktop.layout.GroupLayout.TRAILING, jppassoLayout.createSequentialGroup().addContainerGap(54, Short.MAX_VALUE).add(jppassoLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel17).add(txtMneumonico, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(btnContinuar)).addContainerGap()));
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jppasso, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(jppasso, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE));
        pack();
    }

    private void btnContinuarActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new Detalhes(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    public javax.swing.JButton btnContinuar;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    public javax.swing.JPanel jppasso;

    public javax.swing.JTextField txtInstrucao;

    public javax.swing.JTextField txtMneumonico;
}
