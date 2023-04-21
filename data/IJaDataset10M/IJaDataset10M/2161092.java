package com.gwt.client.login.client;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PatientUI {

    private JFrame frame;

    /**
	 * Launch the application.
	 */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    PatientUI window = new PatientUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
	 * Create the application.
	 */
    public PatientUI() {
        initialize();
    }

    /**
	 * Initialize the contents of the frame.
	 */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel lblWelcome = new JLabel("Welcome Patient");
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Tahoma", Font.BOLD, 15));
        JButton btnNewButton_1 = new JButton("Request an appointment");
        btnNewButton_1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                RequestAppointmentUI raui = new RequestAppointmentUI();
                raui.setVisible(true);
            }
        });
        JButton btnViewMedicalHistory = new JButton("View Patient Information");
        btnViewMedicalHistory.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                PatientInfoUI pinfo = new PatientInfoUI();
                pinfo.setVisible(true);
            }
        });
        JButton btnLogout = new JButton("Logout");
        btnLogout.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                LoginUI login = new LoginUI();
                login.setVisible(true);
                frame.setVisible(false);
                frame.dispose();
            }
        });
        JButton btnViewInvoice = new JButton("View Invoice");
        btnViewInvoice.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                InvoiceScreen is = new InvoiceScreen();
                InvoiceScreen.setvisible(true);
            }
        });
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addGroup(groupLayout.createSequentialGroup().addComponent(lblWelcome, GroupLayout.PREFERRED_SIZE, 434, GroupLayout.PREFERRED_SIZE).addGap(0)).addGroup(groupLayout.createSequentialGroup().addGap(36).addComponent(btnNewButton_1, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE).addGap(34).addComponent(btnViewMedicalHistory, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE).addGap(50)).addGroup(Alignment.LEADING, groupLayout.createSequentialGroup().addGap(169).addComponent(btnViewInvoice).addContainerGap(197, Short.MAX_VALUE)).addGroup(Alignment.LEADING, groupLayout.createSequentialGroup().addGap(179).addComponent(btnLogout, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE).addContainerGap(201, Short.MAX_VALUE)));
        groupLayout.setVerticalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout.createSequentialGroup().addComponent(lblWelcome).addGap(28).addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(btnNewButton_1).addComponent(btnViewMedicalHistory, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(ComponentPlacement.UNRELATED).addComponent(btnViewInvoice).addGap(92).addComponent(btnLogout).addGap(43)));
        frame.getContentPane().setLayout(groupLayout);
    }

    /**
	 * 
	 * @param b
	 */
    public void setVisible(boolean b) {
        frame.setVisible(b);
    }
}
