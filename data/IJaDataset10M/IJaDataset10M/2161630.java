package edu.ucla.stat.SOCR.distributome.gui;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

public class SOCROptionPane extends JOptionPane {

    public static void showMessageDialog(Component parent, String m) {
        JOptionPane popup = new JOptionPane();
        JTextArea msg = new JTextArea(m);
        msg.setEditable(true);
        Color bg = popup.getBackground();
        msg.setBackground(bg);
        popup.showMessageDialog(parent, msg);
    }
}
