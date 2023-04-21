package de.ios.framework.gui.builder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Fenster zum Eingeben von Zeilen/Spalten-Positionen.
 */
public class SizeFrame extends JFrame implements WindowListener, ActionListener {

    /**
   * Konstanten f�r den action-Konstruktorparameter.
   */
    public static final int Spalte = 1;

    public static final int Zeile = 2;

    GridBagLayout gbl = new GridBagLayout();

    GridBagConstraints gbc = new GridBagConstraints();

    protected JTextField tfPosition = new JTextField(2);

    protected JLabel lBezeichnung = new JLabel("Position:");

    protected JButton btOk = new JButton("Ok");

    protected Konstruktionsfenster kf;

    protected int action;

    /**
   * Konstruktor.
   * @param kf Konstruktionsfenster, dessen Groesse veraendert wird.
   * @param action Gibt an, ob Spalte oder Zeile eingef�gt wird.
   * @param title Titel des Fensters.
   */
    public SizeFrame(Konstruktionsfenster kf, int action, String title) {
        this.kf = kf;
        this.action = action;
        this.getContentPane().setLayout(gbl);
        setTitle(title);
        addWindowListener(this);
        packComponents();
        setVisible(true);
    }

    /**
   * Erstellt die Oberflaeche.
   */
    protected void packComponents() {
        gbc.anchor = gbc.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(lBezeichnung, gbc);
        this.getContentPane().add(lBezeichnung);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbl.setConstraints(tfPosition, gbc);
        this.getContentPane().add(tfPosition);
        gbc.anchor = gbc.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbl.setConstraints(btOk, gbc);
        this.getContentPane().add(btOk);
        btOk.addActionListener(this);
        pack();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btOk) {
            int position = Integer.parseInt(tfPosition.getText());
            if (position > 0) {
                if (action == Spalte) {
                    kf.spalteEinfuegen(position);
                } else if (action == Zeile) {
                    kf.zeileEinfuegen(position);
                }
            }
            dispose();
        }
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        dispose();
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }
}
