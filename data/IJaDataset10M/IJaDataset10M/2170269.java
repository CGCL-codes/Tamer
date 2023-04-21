package vista.ventanas;

import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class VentanaCreditos extends JFrame {

    private JFrame ventanaMenu = null;

    public VentanaCreditos(JFrame ventanaMenu) {
        this.ventanaMenu = ventanaMenu;
        this.setSize(800, 600);
        this.setTitle("Creditos - Car Tunnning Experience 2008");
        this.setLocationRelativeTo(null);
        this.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarVentana();
            }
        });
        this.setResizable(false);
        this.setAlwaysOnTop(false);
        this.setVisible(false);
        showInfo();
    }

    private void showInfo() {
        JPanel panelInfo = new JPanel();
        panelInfo.setVisible(true);
        this.add(panelInfo);
        JTextArea texto = new JTextArea(800, 600);
        texto.setText("<html>\n" + "<ul>\n" + "<b><font size=+3>Car Tunning Experience 2008</font></b>\n" + "" + "" + "<p>" + "<li>Color and font test:\n" + "<li>" + "<li><font color=red>red</font>\n" + "<li><font color=blue>blue</font>\n" + "<li><font color=green>green</font>\n" + "<li><font size=-2>small</font>\n" + "<li><font size=+2>large</font>\n" + "<li><i>italic</i>\n" + "<li><b>bold</b>\n" + "\n" + "<a href=\"http://code.google.com/p/car-tunning-experience-2008/\">Sitio web oficial</a>\n" + "<li>http://code.google.com/p/car-tunning-experience-2008/\n" + "</ul>\n");
        texto.setBackground(new Color(0, 0, 0, 0));
        texto.setVisible(true);
        JLabel label = new JLabel("LABEL") {

            public Dimension getPreferredSize() {
                return new Dimension(800, 600);
            }

            public Dimension getMinimumSize() {
                return new Dimension(700, 500);
            }

            public Dimension getMaximumSize() {
                return new Dimension(900, 700);
            }
        };
        label.setVisible(true);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setText(texto.getText());
        panelInfo.add(label);
    }

    private void cerrarVentana() {
        ventanaMenu.setVisible(true);
        this.dispose();
    }
}
