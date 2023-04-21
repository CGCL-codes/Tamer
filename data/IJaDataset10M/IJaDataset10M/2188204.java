package client.graphics;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import client.logic.PrcLogic;
import entity.beans.Patient;
import entity.beans.User;

/**
 * This code was edited or generated using CloudGarden's Jigloo
 * SWT/Swing GUI Builder, which is free for non-commercial
 * use. If Jigloo is being used commercially (ie, by a corporation,
 * company or business for any purpose whatever) then you
 * should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details.
 * Use of Jigloo implies acceptance of these licensing terms.
 * A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
 * THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
 * LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
@SuppressWarnings("serial")
public class NewPatient extends JFrame {

    private JLabel jLabel1;

    private JLabel jLabel2;

    private JLabel jLabel3;

    private JLabel jLabel4;

    private JLabel jLabel5;

    private JComboBox jComboBox4;

    private JComboBox jComboBox3;

    private JComboBox jComboBox2;

    private JLabel jLabel8;

    private JComboBox jComboBox1;

    private JButton jButton2;

    private JButton jButton1;

    private JLabel jLabel7;

    private JLabel jLabel6;

    private JTextField jTextField5;

    private JTextField jTextField4;

    private JTextField jTextField3;

    private JTextField jTextField2;

    private JTextField jTextField1;

    private PrcLogic prc;

    private String[] fdoctors;

    List<User> fdlist;

    private JTextField jTextField6;

    private JLabel jLabel9;

    public NewPatient(PrcLogic prc) {
        super("Nuovo Paziente");
        this.prc = prc;
        fdlist = prc.getFD();
        fdoctors = new String[fdlist.size()];
        for (int i = 0; i < fdlist.size(); i++) {
            fdoctors[i] = "" + fdlist.get(i).getName() + " " + fdlist.get(i).getSurname();
        }
        initGUI();
        this.setVisible(true);
    }

    private void initGUI() {
        try {
            {
                GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
                getContentPane().setLayout(thisLayout);
                getContentPane().setBackground(new java.awt.Color(128, 128, 255));
                {
                    jLabel1 = new JLabel();
                    jLabel1.setText("MECS - Pagina Paziente");
                    jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 28));
                }
                {
                    jLabel7 = new JLabel();
                    jLabel7.setText("Medico di Famiglia :");
                }
                {
                    jButton1 = new JButton();
                    jButton1.setText("Inserisci Paziente");
                    jButton1.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            jButton1ActionPerformed(evt);
                        }
                    });
                }
                {
                    jButton2 = new JButton();
                    jButton2.setText("PRC Home");
                    jButton2.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            jButton2ActionPerformed(evt);
                        }
                    });
                }
                {
                    jLabel9 = new JLabel();
                    jLabel9.setText("Contatto del medico:");
                }
                {
                    jTextField6 = new JTextField();
                }
                {
                    jLabel2 = new JLabel();
                    jLabel2.setText("Nome :");
                }
                {
                    jLabel8 = new JLabel();
                    jLabel8.setText("Data di nascita :");
                }
                {
                    ComboBoxModel jComboBox2Model = new DefaultComboBoxModel(new String[] { "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno", "Luglio", "Agosto", "Settemre", "Ottobre", "Novembre", "Dicembre" });
                    jComboBox2 = new JComboBox();
                    jComboBox2.setModel(jComboBox2Model);
                    jComboBox2.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            jComboBox2ActionPerformed(evt);
                        }
                    });
                    jComboBox2.setEnabled(false);
                }
                {
                    ComboBoxModel jComboBox3Model = new DefaultComboBoxModel(new String[] { "1", "2" });
                    jComboBox3 = new JComboBox();
                    jComboBox3.setModel(jComboBox3Model);
                    jComboBox3.setEnabled(false);
                }
                {
                    int anno = Calendar.getInstance().get(Calendar.YEAR);
                    String[] anni = new String[131];
                    for (int i = (anno - 130); i < (anno + 1); i++) {
                        anni[i - (anno - 130)] = String.valueOf(i);
                    }
                    ComboBoxModel jComboBox4Model = new DefaultComboBoxModel(anni);
                    jComboBox4 = new JComboBox();
                    jComboBox4.setModel(jComboBox4Model);
                    jComboBox4.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent evt) {
                            jComboBox4ActionPerformed(evt);
                        }
                    });
                }
                {
                    ComboBoxModel jComboBox1Model = new DefaultComboBoxModel(fdoctors);
                    jComboBox1 = new JComboBox();
                    jComboBox1.setModel(jComboBox1Model);
                }
                {
                    jTextField1 = new JTextField();
                }
                {
                    jTextField2 = new JTextField();
                }
                {
                    jLabel3 = new JLabel();
                    jLabel3.setText("Cognome :");
                }
                {
                    jTextField3 = new JTextField();
                }
                {
                    jLabel4 = new JLabel();
                    jLabel4.setText("SSN :");
                }
                {
                    jTextField4 = new JTextField();
                }
                {
                    jLabel5 = new JLabel();
                    jLabel5.setText("Indirizzo :");
                }
                {
                    jTextField5 = new JTextField();
                }
                {
                    jLabel6 = new JLabel();
                    jLabel6.setText("Telefono :");
                }
                thisLayout.setVerticalGroup(thisLayout.createSequentialGroup().addContainerGap(18, 18).addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(31).addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE).addGap(11)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)).addComponent(jComboBox3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, Short.MAX_VALUE).addComponent(jButton2, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))).addGap(0, 46, GroupLayout.PREFERRED_SIZE).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jComboBox1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel7, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jTextField1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField2, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(0, 12, GroupLayout.PREFERRED_SIZE).addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))).addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField3, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, GroupLayout.PREFERRED_SIZE).addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 0, GroupLayout.PREFERRED_SIZE).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField4, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jTextField5, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jButton1, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(jLabel6, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).addContainerGap(70, 70));
                thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup().addContainerGap().addGroup(thisLayout.createParallelGroup().addGroup(thisLayout.createSequentialGroup().addGroup(thisLayout.createParallelGroup().addComponent(jLabel4, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addComponent(jLabel6, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE).addComponent(jLabel5, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 67, GroupLayout.PREFERRED_SIZE).addComponent(jLabel3, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE).addComponent(jLabel2, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)).addGap(24).addGroup(thisLayout.createParallelGroup().addGroup(thisLayout.createSequentialGroup().addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addComponent(jTextField4, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addComponent(jTextField3, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addComponent(jTextField2, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE)).addGroup(thisLayout.createSequentialGroup().addComponent(jTextField1, GroupLayout.PREFERRED_SIZE, 153, GroupLayout.PREFERRED_SIZE))).addGap(0, 368, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jLabel8, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox4, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE).addGroup(thisLayout.createParallelGroup().addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addGap(0, 127, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addGap(18).addComponent(jComboBox2, GroupLayout.PREFERRED_SIZE, 73, GroupLayout.PREFERRED_SIZE).addGap(21).addGroup(thisLayout.createParallelGroup().addComponent(jButton1, GroupLayout.Alignment.LEADING, 0, 309, Short.MAX_VALUE).addGroup(thisLayout.createSequentialGroup().addComponent(jTextField6, GroupLayout.PREFERRED_SIZE, 309, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jLabel9, GroupLayout.PREFERRED_SIZE, 139, GroupLayout.PREFERRED_SIZE).addGap(0, 170, Short.MAX_VALUE)).addGroup(thisLayout.createSequentialGroup().addGroup(thisLayout.createParallelGroup().addComponent(jLabel7, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE).addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup().addComponent(jComboBox3, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE).addGap(74))).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(thisLayout.createParallelGroup().addGroup(thisLayout.createSequentialGroup().addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 176, GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addComponent(jButton2, GroupLayout.Alignment.LEADING, 0, 176, Short.MAX_VALUE)))))))).addContainerGap(37, 37));
            }
            pack();
            this.setSize(680, 400);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jButton2ActionPerformed(ActionEvent evt) {
        this.dispose();
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
        Calendar birthdate = Calendar.getInstance();
        String name = jTextField1.getText();
        String surname = jTextField2.getText();
        String ssn = jTextField3.getText();
        String address = jTextField4.getText();
        String contact = jTextField6.getText();
        if (!jTextField5.getText().matches("[0-9]+")) JOptionPane.showMessageDialog(null, "Inserire correttamente il numero di telefono!"); else {
            Double phone = Double.parseDouble(jTextField5.getText());
            try {
                birthdate.set(Calendar.YEAR, Integer.parseInt(jComboBox4.getSelectedItem().toString()));
                birthdate.set(Calendar.MONTH, jComboBox2.getSelectedIndex());
                birthdate.set(Calendar.DATE, jComboBox3.getSelectedIndex() + 1);
            } catch (Exception e) {
                JOptionPane.showConfirmDialog(null, "Attenzione, selezionare una data!");
            }
            int fdId = fdlist.get(jComboBox1.getSelectedIndex()).getId();
            if (name.equals("") | surname.equals("") | ssn.equals("") | contact.equals("")) JOptionPane.showMessageDialog(null, "Mancano dei campi obbligatori"); else {
                if (!jTextField5.getText().matches("[0-9]+")) {
                    JOptionPane.showMessageDialog(null, "Inserirse solo numeri nel campo \"telefono\".");
                } else {
                    prc.insertPatient(ssn, address, name, surname, birthdate, fdId, phone, contact);
                    Patient patient = prc.searchPatient(ssn);
                    JOptionPane.showMessageDialog(null, "Dati salvati correttamente.");
                    @SuppressWarnings("unused") PatientPersonalPage ppp = new PatientPersonalPage(prc, patient);
                    this.dispose();
                }
            }
        }
    }

    private void jComboBox4ActionPerformed(ActionEvent evt) {
        jComboBox2.setEnabled(true);
        jComboBox2.setSelectedIndex(0);
    }

    private void jComboBox2ActionPerformed(ActionEvent evt) {
        int index = jComboBox2.getSelectedIndex();
        String[] days;
        if (index == 3 | index == 5 | index == 8 | index == 12) {
            days = new String[30];
            for (int i = 1; i < 31; i++) days[i - 1] = Integer.toString(i);
            DefaultComboBoxModel dayz = new DefaultComboBoxModel(days);
            jComboBox3.setModel(dayz);
        } else if (index == 1) {
            int year = Integer.parseInt(jComboBox4.getSelectedItem().toString());
            int i;
            if (year % 4 == 0 & year % 100 != 0) i = 29; else if (year % 4 == 0 & year % 100 == 0 & year % 400 == 0) i = 29; else i = 28;
            days = new String[i];
            for (int k = 1; k < i + 1; k++) days[k - 1] = Integer.toString(k);
            DefaultComboBoxModel dayz = new DefaultComboBoxModel(days);
            jComboBox3.setModel(dayz);
        } else {
            days = new String[31];
            for (int i = 1; i < 32; i++) days[i - 1] = Integer.toString(i);
            DefaultComboBoxModel dayz = new DefaultComboBoxModel(days);
            jComboBox3.setModel(dayz);
        }
        jComboBox3.setEnabled(true);
    }
}
