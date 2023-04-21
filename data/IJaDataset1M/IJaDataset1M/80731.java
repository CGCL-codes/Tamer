package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Frame;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JMenu;

public class Presentacion extends Frame {

    private JLabel LabeLogo = new JLabel(new ImageIcon("Imagenes/fichas.jpg"));

    private JLabel labelDibujo = new JLabel();

    private JLabel labelTagade = new JLabel(new ImageIcon("Imagenes/logtagade.jpg"));

    private JButton BotonComenzar = new JButton();

    public Presentacion() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Presentacion pres = new Presentacion();
        pres.setTitle("PARQUES v1.0");
        pres.setSize(700, 400);
        pres.setLocationRelativeTo(null);
        pres.show();
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        LabeLogo.setBounds(new Rectangle(0, 55, 280, 130));
        labelDibujo.setBounds(new Rectangle(30, 10, 300, 185));
        labelTagade.setBounds(new Rectangle(305, 225, 290, 115));
        BotonComenzar.setText("Comenzar");
        BotonComenzar.setBounds(new Rectangle(45, 260, 145, 55));
        BotonComenzar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                comenzar(e);
            }
        });
        this.add(BotonComenzar, null);
        this.add(labelTagade, null);
        this.add(labelDibujo, null);
        this.add(LabeLogo, null);
    }

    private void comenzar(ActionEvent e) {
        LogIn log = new LogIn();
        Fondo fondo = new Fondo();
        fondo.setSize(1000, 800);
        fondo.setTitle("PARQUES v1.0");
        fondo.setLocationRelativeTo(null);
        fondo.show();
        log.setSize(500, 400);
        log.setTitle("PARQUES v1.0");
        log.setLocationRelativeTo(null);
        log.show();
        this.hide();
    }
}
