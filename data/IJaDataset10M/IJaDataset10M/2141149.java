package com.gwt.client.login.client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTree;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JEditorPane;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.Choice;
import java.io.IOException;
import java.net.UnknownHostException;
import javax.swing.JRadioButton;

public class PatientInfoUI {

    private JFrame frmPatientInformation;

    private JTextField textField;

    private JTextField textField_1;

    private JTextField textField_2;

    private JTextField textField_3;

    private JTextField textField_4;

    private Transmitter tm;

    /**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    PatientInfoUI window = new PatientInfoUI();
                    window.frmPatientInformation.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the application.
	 */
    public PatientInfoUI() {
        initialize();
    }

    /**
	 * Initialize the contents of the frame.
	 */
    private void initialize() {
        frmPatientInformation = new JFrame();
        frmPatientInformation.setTitle("Patient Information");
        frmPatientInformation.setBounds(100, 100, 523, 380);
        frmPatientInformation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JLabel lblPatientInformation = new JLabel("Patient Information");
        JLabel lblMedicalInformation = new JLabel("Medical Information");
        JLabel lblName = new JLabel("Name :");
        textField = new JTextField();
        textField.setEditable(false);
        textField.setColumns(10);
        JLabel lblAddress = new JLabel("Address :");
        final JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JLabel lblPhoneNumber = new JLabel("Phone Number :");
        textField_1 = new JTextField();
        textField_1.setEditable(false);
        textField_1.setColumns(10);
        JLabel lblEmail = new JLabel("Email :");
        textField_2 = new JTextField();
        textField_2.setEditable(false);
        textField_2.setColumns(10);
        JLabel lblGender = new JLabel("Gender :");
        final JRadioButton rdbtnMale = new JRadioButton("Male");
        rdbtnMale.setEnabled(false);
        final JTextArea textArea_1 = new JTextArea();
        textArea_1.setEditable(false);
        final JRadioButton rdbtnFemale = new JRadioButton("Female");
        rdbtnFemale.setEnabled(false);
        final JButton btnSubmitChanges = new JButton("Submit Changes");
        btnSubmitChanges.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                textField.setEditable(false);
                textField_1.setEditable(false);
                textField_2.setEditable(false);
                textArea.setEditable(false);
                rdbtnMale.setEnabled(false);
                rdbtnFemale.setEnabled(false);
                textField_3.setEditable(false);
                textField_4.setEditable(false);
                textArea_1.setEditable(false);
                btnSubmitChanges.setEnabled(false);
            }
        });
        btnSubmitChanges.setEnabled(false);
        JButton btnEdit = new JButton("Edit");
        btnEdit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                textField.setEditable(true);
                textField_1.setEditable(true);
                textField_2.setEditable(true);
                textArea.setEditable(true);
                rdbtnMale.setEnabled(true);
                rdbtnFemale.setEnabled(true);
                btnSubmitChanges.setEnabled(true);
            }
        });
        JButton btnEdit_1 = new JButton("Edit");
        btnEdit_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                textField_3.setEditable(true);
                textField_4.setEditable(true);
                textArea_1.setEditable(true);
                btnSubmitChanges.setEnabled(true);
            }
        });
        JLabel lblPharmacy = new JLabel("Pharmacy :");
        JLabel lblInsuranceCarrier = new JLabel("Insurance Carrier :");
        JLabel lblAllergies = new JLabel("Allergies :");
        textField_3 = new JTextField();
        textField_3.setEditable(false);
        textField_3.setColumns(10);
        textField_4 = new JTextField();
        textField_4.setEditable(false);
        textField_4.setColumns(10);
        JButton btnViewMedicalHistory = new JButton("View Medical History");
        btnViewMedicalHistory.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                MedicalHistoryUI mui = new MedicalHistoryUI();
                mui.setVisible(true);
                frmPatientInformation.setVisible(false);
                frmPatientInformation.dispose();
            }
        });
        GroupLayout groupLayout = new GroupLayout(frmPatientInformation.getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(19).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(lblEmail).addPreferredGap(ComponentPlacement.RELATED).addComponent(textField_2, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE).addGap(83)).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(lblPatientInformation).addPreferredGap(ComponentPlacement.RELATED, 18, Short.MAX_VALUE).addComponent(btnEdit)).addGroup(groupLayout.createSequentialGroup().addComponent(lblAddress).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(textArea, GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)).addGroup(groupLayout.createSequentialGroup().addGap(8).addComponent(lblName).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(textField, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))).addGap(82)).addGroup(groupLayout.createSequentialGroup().addComponent(lblPhoneNumber).addPreferredGap(ComponentPlacement.RELATED).addComponent(textField_1, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE).addGap(53))).addGroup(groupLayout.createSequentialGroup().addComponent(lblGender).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(rdbtnFemale).addComponent(rdbtnMale)).addGap(143))).addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(lblMedicalInformation).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnEdit_1)).addGroup(groupLayout.createSequentialGroup().addComponent(lblPharmacy).addPreferredGap(ComponentPlacement.RELATED).addComponent(textField_3, GroupLayout.PREFERRED_SIZE, 125, GroupLayout.PREFERRED_SIZE)).addGroup(groupLayout.createSequentialGroup().addComponent(lblInsuranceCarrier).addPreferredGap(ComponentPlacement.RELATED).addComponent(textField_4, GroupLayout.PREFERRED_SIZE, 118, GroupLayout.PREFERRED_SIZE)).addGroup(groupLayout.createSequentialGroup().addComponent(lblAllergies).addPreferredGap(ComponentPlacement.RELATED).addComponent(textArea_1))).addGap(31)).addGroup(groupLayout.createSequentialGroup().addGap(194).addComponent(btnViewMedicalHistory).addPreferredGap(ComponentPlacement.RELATED, 49, Short.MAX_VALUE).addComponent(btnSubmitChanges).addGap(46)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addGap(18).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPatientInformation).addComponent(btnEdit).addComponent(lblMedicalInformation).addComponent(btnEdit_1)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(lblName).addComponent(lblPharmacy).addComponent(textField_3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblInsuranceCarrier).addComponent(textField_4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(15).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblAllergies).addComponent(textArea_1, GroupLayout.PREFERRED_SIZE, 78, GroupLayout.PREFERRED_SIZE))).addGroup(groupLayout.createSequentialGroup().addGroup(groupLayout.createParallelGroup(Alignment.LEADING).addComponent(lblAddress).addComponent(textArea, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)).addGap(13).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblPhoneNumber).addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblEmail).addComponent(textField_2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGap(3))).addPreferredGap(ComponentPlacement.RELATED).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(lblGender).addComponent(rdbtnMale)).addPreferredGap(ComponentPlacement.RELATED).addComponent(rdbtnFemale).addGap(38).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnViewMedicalHistory).addComponent(btnSubmitChanges)).addGap(43)));
        frmPatientInformation.getContentPane().setLayout(groupLayout);
    }

    public void setVisible(boolean b) {
        frmPatientInformation.setVisible(b);
    }
}
