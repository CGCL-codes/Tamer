package fatture;

import java.awt.Color;
import javax.swing.JOptionPane;

/**
 *
 * @author  sasch
 */
public class jdDataPagamentoFattura extends javax.swing.JDialog {

    public int tipoPagamentoID = -1, clienteID = -1;

    public String err = "";

    /** Creates new form jdDataPagamentoFattura */
    public jdDataPagamentoFattura(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jcbTipoPagamento = new javax.swing.JComboBox();
        jbAnnulla = new javax.swing.JButton();
        jbProcedi = new javax.swing.JButton();
        jcbRegistraCassa = new javax.swing.JCheckBox();
        jftDataPagamento = new javax.swing.JFormattedTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("jMagazzino- imposta data pagamento");
        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        jPanel1.setBackground(new java.awt.Color(204, 255, 204));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Imposta data pagamento"));
        jLabel1.setText("Data pagamento:");
        jLabel2.setText("Pagamento effettuato con:");
        jcbTipoPagamento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jbAnnulla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/cancella_20.png")));
        jbAnnulla.setText("Annulla");
        jbAnnulla.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbAnnullaMousePressed(evt);
            }
        });
        jbProcedi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/immagini/procedi.png")));
        jbProcedi.setText("Procedi");
        jbProcedi.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                jbProcediMousePressed(evt);
            }
        });
        jcbRegistraCassa.setBackground(new java.awt.Color(204, 255, 204));
        jcbRegistraCassa.setText("Registra nella prima nota");
        try {
            jftDataPagamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jftDataPagamento.setFont(new java.awt.Font("Tahoma", 1, 18));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addGap(14, 14, 14).addComponent(jftDataPagamento, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbRegistraCassa)).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbTipoPagamento, 0, 417, Short.MAX_VALUE))).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jbProcedi).addGap(18, 18, 18).addComponent(jbAnnulla)))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(jftDataPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jcbRegistraCassa)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jcbTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbAnnulla).addComponent(jbProcedi))));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void jbProcediMousePressed(java.awt.event.MouseEvent evt) {
        err = "";
        if (!globali.jcFunzioni.controllaInserimentoData(jftDataPagamento.getText())) {
            err = "Hai inserito una data di avvenuto pagamento ERRATA !\n";
            jftDataPagamento.setBackground(Color.RED);
        } else jftDataPagamento.setBackground(Color.WHITE);
        Object pag[] = (Object[]) jcbTipoPagamento.getSelectedItem();
        if ((Integer) pag[2] == -1) {
            err = "Non hai scelto nessun tipo di pagamento !\n";
            jcbTipoPagamento.setBackground(Color.RED);
        } else jcbTipoPagamento.setBackground(Color.WHITE);
        if (err.length() != 0) JOptionPane.showMessageDialog(null, "Sono stati riscontrati i seguenti errori:\n" + err, "Errore", JOptionPane.ERROR_MESSAGE);
        this.setVisible(false);
    }

    private void jbAnnullaMousePressed(java.awt.event.MouseEvent evt) {
        err = "-";
        this.setVisible(false);
    }

    private void formWindowOpened(java.awt.event.WindowEvent evt) {
        globali.jcFunzioni.caricaTipiPagamenti(tipoPagamentoID, clienteID, jcbTipoPagamento);
        jcbTipoPagamento.setRenderer(new globali.renderComboBox.renderPagamenti());
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                jdDataPagamentoFattura dialog = new jdDataPagamentoFattura(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JButton jbAnnulla;

    private javax.swing.JButton jbProcedi;

    public javax.swing.JCheckBox jcbRegistraCassa;

    public javax.swing.JComboBox jcbTipoPagamento;

    public javax.swing.JFormattedTextField jftDataPagamento;
}
