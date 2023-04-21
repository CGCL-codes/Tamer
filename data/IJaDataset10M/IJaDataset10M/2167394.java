package it.gestioneimmobili;

import it.gestioneimmobili.event.GestioneImmobiliAnagraficaEvent;
import it.gestioneimmobili.event.GestioneImmobiliAnagraficaEventManager;
import it.gestioneimmobili.hibernate.bean.Anagrafica;
import it.gestioneimmobili.hibernate.bean.view.AnagraficaView;
import it.gestioneimmobili.process.ProcessAnagrafica;
import it.gestioneimmobili.util.JTextFieldLimit;
import it.gestioneimmobili.util.JTextFieldNumericLimit;
import it.gestioneimmobili.util.Util;
import java.util.List;

/**
 *
 * @author  __USER__
 */
public class CercaAnagraficaUI extends javax.swing.JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1298152831744822408L;

    /** Creates new form AnagraficaUI */
    public CercaAnagraficaUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(parent);
        this.cognome.setDocument(new JTextFieldLimit(20));
        this.indirizzo.setDocument(new JTextFieldLimit(50));
        this.ragioneSociale.setDocument(new JTextFieldLimit(10));
        this.cellulare.setDocument(new JTextFieldNumericLimit(13));
        this.nome.setDocument(new JTextFieldLimit(20));
        this.email.setDocument(new JTextFieldLimit(30));
        this.citta.setDocument(new JTextFieldLimit(20));
        this.telefono.setDocument(new JTextFieldNumericLimit(13));
        this.telefonoUfficio.setDocument(new JTextFieldNumericLimit(13));
        this.fax.setDocument(new JTextFieldNumericLimit(13));
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cognome = new javax.swing.JTextField();
        indirizzo = new javax.swing.JTextField();
        ragioneSociale = new javax.swing.JTextField();
        telefono = new javax.swing.JTextField();
        nome = new javax.swing.JTextField();
        email = new javax.swing.JTextField();
        citta = new javax.swing.JTextField();
        cellulare = new javax.swing.JTextField();
        telefonoUfficio = new javax.swing.JTextField();
        fax = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setTitle("Cerca Anagrafica");
        setModal(true);
        setResizable(false);
        jLabel1.setText("Cognome");
        jLabel2.setText("Indirizzo");
        jLabel3.setText("Ragione sociale");
        jLabel4.setText("Cellulare");
        jLabel5.setText("Nome");
        jLabel6.setText("Email");
        jLabel7.setText("Città");
        jLabel9.setText("Telefono");
        jLabel10.setText("Telefono Ufficio");
        jLabel11.setText("Fax");
        jButton1.setText("Annulla");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Cerca");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel10).add(16, 16, 16).add(telefonoUfficio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 102, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel11).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(jLabel5).add(36, 36, 36).add(nome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(jLabel7).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).add(citta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 146, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup().add(jLabel3).add(32, 32, 32).add(ragioneSociale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 83, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))))).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup().add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(fax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jLabel6)).add(layout.createSequentialGroup().add(16, 16, 16).add(jLabel9).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(telefono, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(layout.createSequentialGroup().add(jLabel4).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(cellulare, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)).add(email, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE))).add(layout.createSequentialGroup().add(57, 57, 57).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING).add(jLabel2).add(jLabel1)).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false).add(indirizzo).add(cognome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE))))).add(layout.createSequentialGroup().add(jButton2).addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED).add(jButton1))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING).add(layout.createSequentialGroup().add(34, 34, 34).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel5).add(nome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel1).add(cognome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(25, 25, 25).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel3).add(ragioneSociale, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel2).add(indirizzo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(17, 17, 17).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel7).add(jLabel9).add(citta, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(telefono, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel4).add(cellulare, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(22, 22, 22).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jLabel10).add(telefonoUfficio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel11).add(fax, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE).add(jLabel6).add(email, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)).add(19, 19, 19).add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE).add(jButton1).add(jButton2)).addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        Anagrafica an = new Anagrafica(cognome.getText(), indirizzo.getText(), ragioneSociale.getText(), cellulare.getText(), nome.getText(), email.getText(), citta.getText(), null, telefono.getText(), telefonoUfficio.getText(), fax.getText(), null, null, "", true);
        ProcessAnagrafica proc = new ProcessAnagrafica();
        try {
            List<AnagraficaView> ll = proc.searchView(an);
            this.setVisible(false);
            GestioneImmobiliAnagraficaEventManager.getInstance().listChanged(new GestioneImmobiliAnagraficaEvent(this, true, false, ll, null));
        } catch (Exception e) {
            Util.showErroreCustom(this, "Errore generico sul db");
            e.printStackTrace();
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private javax.swing.JTextField cellulare;

    private javax.swing.JTextField citta;

    private javax.swing.JTextField cognome;

    private javax.swing.JTextField email;

    private javax.swing.JTextField fax;

    private javax.swing.JTextField indirizzo;

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel10;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JLabel jLabel7;

    private javax.swing.JLabel jLabel9;

    private javax.swing.JTextField nome;

    private javax.swing.JTextField ragioneSociale;

    private javax.swing.JTextField telefono;

    private javax.swing.JTextField telefonoUfficio;
}
