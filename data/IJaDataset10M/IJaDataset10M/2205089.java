package gui;

import game.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class DlgOnlineAnmeldung extends JDialog implements ActionListener {

    private JLabel lbl;

    private JTextField txtName;

    private JButton btnOk;

    private VerwaltungClient meineVerwaltung;

    private JPasswordField txtPwd;

    public DlgOnlineAnmeldung(VerwaltungClient ss_verwaltung) {
        super();
        meineVerwaltung = ss_verwaltung;
        JPanel panel1 = new JPanel();
        panel1.setLayout(new FlowLayout());
        lbl = new JLabel("Dein Name:");
        txtName = new JTextField(10);
        btnOk = new JButton("Anmelden");
        btnOk.addActionListener(this);
        panel1.add(lbl);
        panel1.add(txtName);
        JPanel panel2 = new JPanel();
        txtPwd = new JPasswordField(10);
        panel2.setLayout(new FlowLayout());
        panel2.add(new JLabel("Passwort:"));
        panel2.add(txtPwd);
        JPanel panel3 = new JPanel();
        panel3.add(btnOk);
        this.setLayout(new GridLayout(3, 1));
        this.add(panel1);
        this.add(panel2);
        this.add(panel3);
        this.pack();
        this.setResizable(false);
        this.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - (this.getWidth() / 2), Toolkit.getDefaultToolkit().getScreenSize().height / 2 - (this.getHeight() / 2));
        this.setModal(true);
        this.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Anmelden")) {
            if (txtName.getText().trim().equals("")) {
                txtName.setText("Atomfred II");
            } else {
                this.dispose();
                meineVerwaltung.onlineAnmeldung(txtName.getText(), txtPwd.getText());
            }
        }
    }
}
