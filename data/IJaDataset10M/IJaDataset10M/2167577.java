package kino.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import kino.GUI.Toolkit.BitmapKomponente;
import kino.GUI.Toolkit.FensterSchliesser;
import kino.GUI.Toolkit.GUIFrame;

public class UeberDialog extends GUIFrame {

    public UeberDialog() {
        super("�ber Kino 98");
        setzeIcon("icons/about.gif");
        Panel Hp = new Panel();
        Hp.setLayout(new BorderLayout());
        addWindowListener(new FensterSchliesser());
        setForeground(Color.white);
        setBackground(Color.black);
        Panel Hp1 = new Panel();
        Hp1.add(new BitmapKomponente("splash_anim.gif"), "North");
        Panel Hp2 = new Panel();
        Hp2.setLayout(new BorderLayout());
        Hp2.add(new Label("TU Dresden, Fakult�t Informatik", Label.CENTER), "North");
        Hp2.add(new Label("Kino 98", Label.CENTER), "West");
        Hp2.add(new Label("Gruppe 5c", Label.CENTER), "East");
        Panel Hp3 = new Panel();
        Hp3.setLayout(new GridLayout(5, 1));
        Hp3.add(new Label("Markus Kramer", Label.CENTER));
        Hp3.add(new Label("Kai Koegler", Label.CENTER));
        Hp3.add(new Label("Enrico Roga", Label.CENTER));
        Hp3.add(new Label("Stephan G�tter", Label.CENTER));
        Hp3.add(new Label("Jan Dittberner", Label.CENTER));
        Hp.add(Hp1, "North");
        Hp.add(Hp2, "Center");
        Hp.add(Hp3, "South");
        add(Hp);
        pack();
        ausrichten(MITTE);
        setResizable(false);
        setVisible(true);
    }
}
