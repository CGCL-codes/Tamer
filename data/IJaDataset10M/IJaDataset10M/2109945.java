package at.voctrainee.ui.rich.swing;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class AdminLoginPanel extends JPanel {

    private JLabel loginLogoLabel = new JLabel();

    private JLabel loginLabel = new JLabel();

    private JTextField loginTxtA = new JTextField();

    private JLabel passwLabel = new JLabel();

    private JPasswordField passwTxtA = new JPasswordField();

    private JButton loginBtn = new JButton();

    public AdminLoginPanel() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        this.setVisible(true);
        this.setSize(new Dimension(600, 700));
        loginLogoLabel.setText("       Administration");
        loginLogoLabel.setBounds(new Rectangle(5, 10, 575, 245));
        loginLogoLabel.setFont(new Font("Dialog", 1, 60));
        loginLabel.setText("LOGIN:");
        loginLabel.setBounds(new Rectangle(95, 340, 50, 20));
        loginLabel.setFont(new Font("Dialog", 1, 14));
        loginTxtA.setBounds(new Rectangle(155, 340, 105, 25));
        passwLabel.setText("Passwort:");
        passwLabel.setBounds(new Rectangle(270, 340, 70, 20));
        passwLabel.setFont(new Font("Dialog", 1, 14));
        passwTxtA.setBounds(new Rectangle(350, 340, 120, 25));
        loginBtn.setText("Login");
        loginBtn.setBounds(new Rectangle(480, 340, 80, 25));
        this.add(loginBtn, null);
        this.add(passwTxtA, null);
        this.add(passwLabel, null);
        this.add(loginLabel, null);
        this.add(loginTxtA, null);
        this.add(loginLogoLabel, null);
    }
}
