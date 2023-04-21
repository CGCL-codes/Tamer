package isclient;

import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @version 	1.0
 * @author
 */
public class Client implements ActionListener {

    public static void main(String[] args) {
        new Client();
    }

    LoginScreen ls;

    public Client() {
        ls = new LoginScreen(this);
        ls.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("CANCEL")) {
            System.exit(0);
        } else if (e.getActionCommand().equals("OK")) {
            LoginSequence lsq = new LoginSequence();
            String result = lsq.connect(ls.loginName.getText(), new String(ls.paswd.getPassword()));
            if (result == null) {
                JOptionPane.showMessageDialog(ls, "Welcome in Imperium Space :)", "Login successfull", JOptionPane.INFORMATION_MESSAGE);
                (new GalaxyMap()).run(1, lsq.getSocket());
            } else JOptionPane.showMessageDialog(ls, "Bad Login", "Error", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Result : " + result);
        }
    }
}
