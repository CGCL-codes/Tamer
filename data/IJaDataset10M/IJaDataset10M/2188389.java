package com.edp1.imagensgraficas.poli;

import javax.swing.JFrame;

public class DesenhaPoligonos {

    public static void main(String args[]) {
        JFrame frame = new JFrame("Desenhando Polígonos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        PoligonosJPanel poligonosJPanel = new PoligonosJPanel();
        frame.add(poligonosJPanel);
        frame.setSize(280, 270);
        frame.setVisible(true);
    }
}
