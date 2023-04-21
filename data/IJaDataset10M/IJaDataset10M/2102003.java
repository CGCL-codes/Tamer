package jpdstore.gui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class PasswordAskDialog extends JDialog {

    private char[] password;

    private JPasswordField pwd;

    public PasswordAskDialog(JFrame parent, String title) {
        super(parent, title, true);
        Container cpane = getContentPane();
        cpane.setLayout(new BorderLayout());
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new EmptyBorder(10, 10, 10, 10));
        cpane.add(p);
        p.add(BorderLayout.NORTH, new JLabel("Password:"));
        p.add(BorderLayout.CENTER, pwd = new JPasswordField(20));
        JPanel pnl = new JPanel(new FlowLayout());
        JButton b;
        pnl.add(b = new JButton("OK"));
        b.setMnemonic(KeyEvent.VK_O);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                password = pwd.getPassword();
                dispose();
            }
        });
        getRootPane().setDefaultButton(b);
        pnl.add(b = new JButton("Cancel"));
        b.setMnemonic(KeyEvent.VK_C);
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        p.add(BorderLayout.SOUTH, pnl);
        setResizable(false);
        pack();
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    public char[] getPassword() {
        return password;
    }

    public String getPasswordString() {
        if (password == null) return null;
        return new String(password);
    }
}
