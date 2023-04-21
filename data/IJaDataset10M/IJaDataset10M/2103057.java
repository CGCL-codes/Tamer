package GUI;

import java.awt.Frame;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Rectangle;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Opciones extends Frame {

    private JButton BotonJugar = new JButton();

    private JButton BotonActualizar = new JButton();

    private JButton BotonEstadisticas = new JButton();

    private JLabel LabelDado = new JLabel(new ImageIcon("Imagenes/dados.jpg"));

    private JLabel LabelFlechas = new JLabel(new ImageIcon("Imagenes/flechas.jpg"));

    private JLabel LabelEstadisticas = new JLabel(new ImageIcon("Imagenes/estadisticas.jpg"));

    private JLabel LabelFichas = new JLabel(new ImageIcon("Imagenes/fichas.jpg"));

    public Opciones() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(null);
        BotonJugar.setText("Jugar");
        BotonJugar.setBounds(new Rectangle(40, 95, 150, 40));
        BotonJugar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                color(e);
            }
        });
        BotonActualizar.setText("Actualizar Datos");
        BotonActualizar.setBounds(new Rectangle(40, 155, 150, 40));
        BotonActualizar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ActualizarD(e);
            }
        });
        BotonEstadisticas.setText("Estadisticas");
        BotonEstadisticas.setBounds(new Rectangle(40, 220, 150, 40));
        LabelDado.setBounds(new Rectangle(255, 90, 80, 60));
        LabelFlechas.setBounds(new Rectangle(255, 150, 80, 55));
        LabelEstadisticas.setBounds(new Rectangle(255, 220, 80, 55));
        LabelFichas.setBounds(new Rectangle(395, 90, 165, 175));
        this.add(LabelFichas, null);
        this.add(LabelEstadisticas, null);
        this.add(LabelFlechas, null);
        this.add(LabelDado, null);
        this.add(BotonEstadisticas, null);
        this.add(BotonActualizar, null);
        this.add(BotonJugar, null);
    }

    private void ActualizarD(ActionEvent e) {
        Actualizar act = new Actualizar();
        act.setSize(700, 400);
        act.setTitle("PARQUES v1.0");
        act.setLocationRelativeTo(null);
        act.show();
        this.hide();
    }

    private void color(ActionEvent e) {
        ElegirColor elcol = new ElegirColor();
        elcol.setSize(700, 400);
        elcol.setTitle("PARQUES v1.0");
        elcol.setLocationRelativeTo(null);
        elcol.show();
        this.hide();
    }
}
