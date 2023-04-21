package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class InlogPaneel extends DomeinPaneel {

    private JLabel titel;

    private JLabel lblNaam;

    private JLabel lblPaswoord;

    private JTextField txfNaam;

    private JPasswordField pwdPaswoord;

    private JButton btnLogin;

    private JLabel lblMelding;

    public InlogPaneel(GuiController controller) {
        super(controller);
        maakPaneel();
    }

    private void maakPaneel() {
        try {
            InvoerListener invoer = new InvoerListener();
            this.setLayout(null);
            titel = new JLabel();
            titel.setText("Log in om het rent-a-carprogramma op te starten!");
            titel.setBounds(12, 19, 368, 21);
            titel.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 14));
            titel.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(titel);
            lblNaam = new JLabel();
            lblNaam.setText("Gebruikersnaam:");
            lblNaam.setBounds(78, 77, 109, 14);
            lblNaam.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(lblNaam);
            lblPaswoord = new JLabel();
            lblPaswoord.setText("Wachtwoord:");
            lblPaswoord.setBounds(78, 110, 109, 14);
            lblPaswoord.setHorizontalAlignment(SwingConstants.RIGHT);
            this.add(lblPaswoord);
            txfNaam = new JTextField();
            txfNaam.setBounds(199, 74, 116, 21);
            txfNaam.addActionListener(invoer);
            this.add(txfNaam);
            pwdPaswoord = new JPasswordField();
            pwdPaswoord.setBounds(199, 107, 116, 21);
            pwdPaswoord.addActionListener(invoer);
            this.add(pwdPaswoord);
            btnLogin = new JButton();
            btnLogin.setText("Log in!");
            btnLogin.setBounds(140, 145, 116, 21);
            btnLogin.addActionListener(invoer);
            this.add(btnLogin);
            lblMelding = new JLabel();
            lblMelding.setForeground(Color.red);
            lblMelding.setBounds(12, 197, 368, 22);
            lblMelding.setHorizontalAlignment(SwingConstants.CENTER);
            this.add(lblMelding);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class InvoerListener implements ActionListener {

        public void actionPerformed(ActionEvent arg0) {
            try {
                String naam = txfNaam.getText();
                String wachtwoord = pwdPaswoord.getText();
                int gebruikersId = domeinController.getMedewerkers().loginMedewerker(naam, wachtwoord);
                domeinController.setHuidigeMedewerker(gebruikersId);
                guiController.startHoofdscherm();
            } catch (IllegalArgumentException e) {
                lblMelding.setText(e.getMessage());
            }
        }
    }
}
