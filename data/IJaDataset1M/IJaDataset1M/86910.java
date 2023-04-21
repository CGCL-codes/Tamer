package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

public class BusquedaArticulos {

    public Component crearComponentes() {
        JLabel eId = new JLabel("ID");
        JLabel eNombre = new JLabel("Nombre");
        JTextField tId = new JTextField();
        JTextField tNombre = new JTextField();
        JButton botonBuscar = new JButton("Buscar");
        JButton botonSalir = new JButton("Salir");
        JTable tabla;
        String titColumna[] = new String[4];
        CargaDatos();
        tabla = new JTable();
        tabla.setShowHorizontalLines(false);
        tabla.setRowSelectionAllowed(true);
        tabla.setColumnSelectionAllowed(true);
        tabla.setSelectionForeground(Color.white);
        tabla.setSelectionBackground(Color.red);
        JScrollPane panelScroll = new JScrollPane(tabla);
        botonBuscar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        botonSalir.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
            }
        });
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        pane.setLayout(new GridLayout(2, 1));
        pane.setLayout(new BorderLayout());
        pane.add(tabla);
        pane.add(panelScroll);
        pane.add(eId);
        pane.add(tId);
        pane.add(eNombre);
        pane.add(tNombre);
        pane.add(botonBuscar);
        pane.add(botonSalir);
        return pane;
    }

    public void CargaDatos() {
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }
        JFrame frame = new JFrame("Selecci�n de la B�squeda");
        BusquedaArticulos app = new BusquedaArticulos();
        Component contenido = app.crearComponentes();
        frame.getContentPane().add(contenido, BorderLayout.CENTER);
        frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        frame.pack();
        frame.setVisible(true);
    }
}
