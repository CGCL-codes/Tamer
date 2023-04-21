package jlokg.lodger;

import fr.nhb.Utilities;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import jlokg.parameters.LKGContratGarantie;

/**
 *
 * @author  bruno
 */
public class LKGLocContratGarantieUI extends javax.swing.JDialog {

    private LKGLocContratGarantie locContratGarantie;

    /** Creates new form LKGLocContratGarantieUI */
    public LKGLocContratGarantieUI(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    public LKGLocContratGarantieUI(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    private void initComponents() {
        jLabel1 = new javax.swing.JLabel();
        jCBTypeContratGarantie = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTFTitre = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jBtSave = new javax.swing.JButton();
        jBtClose = new javax.swing.JButton();
        jDateChooserDateContrat = new com.toedter.calendar.JDateChooser();
        jDateChooserEcheance = new com.toedter.calendar.JDateChooser();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        jLabel1.setText("Type");
        jCBTypeContratGarantie.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel2.setText("Titre");
        jLabel3.setText("Date");
        jLabel4.setText("Echéance");
        jLabel5.setText("Commentaire");
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);
        jBtSave.setText("Enregistrer");
        jBtSave.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtSaveActionPerformed(evt);
            }
        });
        jBtClose.setText("Fermer");
        jBtClose.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtCloseActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel4).addComponent(jLabel3)).addGap(35, 35, 35).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jDateChooserEcheance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jDateChooserDateContrat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jTFTitre).addComponent(jCBTypeContratGarantie, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 259, Short.MAX_VALUE)))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jBtClose).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtSave))).addGap(20, 20, 20)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2)).addGroup(layout.createSequentialGroup().addComponent(jCBTypeContratGarantie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTFTitre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(35, 35, 35).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addGap(12, 12, 12).addComponent(jLabel4)).addGroup(layout.createSequentialGroup().addComponent(jDateChooserDateContrat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jDateChooserEcheance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))).addGap(40, 40, 40).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jBtSave).addComponent(jBtClose)).addContainerGap()));
        pack();
    }

    private void jBtSaveActionPerformed(java.awt.event.ActionEvent evt) {
        if (controlInput()) {
            updateLocContratGarantie();
        }
    }

    private void jBtCloseActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new LKGLocContratGarantieUI(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }

    private javax.swing.JButton jBtClose;

    private javax.swing.JButton jBtSave;

    private javax.swing.JComboBox jCBTypeContratGarantie;

    private com.toedter.calendar.JDateChooser jDateChooserDateContrat;

    private com.toedter.calendar.JDateChooser jDateChooserEcheance;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextField jTFTitre;

    private javax.swing.JTextArea jTextArea1;

    public void display(LKGLocContratGarantie locContratGarantie) {
        this.initCombobox();
        this.locContratGarantie = locContratGarantie;
        this.jTFTitre.setText(this.locContratGarantie.getTitle());
        HashMap map = jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGContratGarantie.class.getName());
        LKGContratGarantie contrat = (LKGContratGarantie) map.get(Integer.parseInt(this.locContratGarantie.getContratGarantieCode()));
        this.jCBTypeContratGarantie.setSelectedItem(contrat);
        this.jDateChooserDateContrat.setDateFormatString(Utilities.getSimpleDateFormatString());
        this.jDateChooserEcheance.setDateFormatString(Utilities.getSimpleDateFormatString());
        if (this.locContratGarantie.getDateReception() != null) {
            this.jDateChooserDateContrat.setDate(new java.util.Date(this.locContratGarantie.getDateReception().getTime()));
        }
        if (this.locContratGarantie.getDateEcheance() != null) {
            this.jDateChooserEcheance.setDate(new java.util.Date(this.locContratGarantie.getDateEcheance().getTime()));
        }
    }

    private void initCombobox() {
        DefaultComboBoxModel comboModel = new DefaultComboBoxModel(jlokg.engine.JLoKGEngine.getJLoKGEngine().getDictionary(LKGContratGarantie.class.getName()).values().toArray());
        this.jCBTypeContratGarantie.setModel(comboModel);
    }

    private boolean controlInput() {
        if (jTFTitre.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Le titre est obligatoire !", "", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void updateLocContratGarantie() {
        locContratGarantie.setTitle(this.jTFTitre.getText());
        if (this.jDateChooserDateContrat.getCalendar() != null) {
            locContratGarantie.setDateReception(this.jDateChooserDateContrat.getCalendar().getTime());
        }
        if (this.jDateChooserEcheance.getCalendar() != null) {
            locContratGarantie.setDateEcheance(this.jDateChooserEcheance.getCalendar().getTime());
        }
        LKGContratGarantie contrat = (LKGContratGarantie) this.jCBTypeContratGarantie.getSelectedItem();
        locContratGarantie.setContratGarantieCode(String.valueOf(contrat.getCode()));
        LKGLocContratGarantieFacade facade = new LKGLocContratGarantieFacade();
        if (this.locContratGarantie.getCode().equals("")) {
            facade.saveLKGLocContratGarantie(this.locContratGarantie);
        } else {
            facade.saveOrUpdateLKGLocContratGarantie(this.locContratGarantie);
        }
        JOptionPane.showMessageDialog(this, "Contrat/Garantie sauvegard�(e) !", "", JOptionPane.PLAIN_MESSAGE);
    }

    public JButton getJBtSave() {
        return jBtSave;
    }
}
