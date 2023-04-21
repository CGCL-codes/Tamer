package vista.ventanas;

import java.awt.Color;
import java.awt.Dimension;
import java.util.*;
import javax.swing.JFrame;
import modelo.Pista;
import vista.imagenAuto.PanelRecorrido;
import vista.imagenTramo.Posicion;
import control.Usuario;

public class VentanaManejar extends JFrame implements Observer {

    private PanelCarril panelUsuario = null;

    private modelo.Usuario usuario = null;

    public void update(Observable arg0, Object arg1) {
        panelUsuario.actualizarVelocidad(usuario.getAuto().getVelocidad());
    }

    public VentanaManejar(modelo.Usuario usuario, Pista pista) {
        this.setResizable(false);
        this.usuario = usuario;
        this.setSize(1000, 620);
        this.setTitle("Manejar - Car Tunnning Experience 2008");
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        Dimension dimensionPanel = new Dimension((int) (this.getSize().width * 0.8), (int) (this.getSize().height * .846));
        this.panelUsuario = PanelCarril.createPanelCarrilVistaAutoDesdeAtras(dimensionPanel, new Posicion(0, (int) (getSize().width * 0.06452)), usuario);
        this.add(panelUsuario);
        this.add(new PanelDeInformacion(new Dimension((int) (getSize().width * 0.2), (int) (getSize().height)), new Posicion((int) (getSize().width * 0.8), 0), usuario));
        this.add(new PanelRecorrido(usuario.getNombre(), usuario.getAuto(), pista, new Dimension((int) (this.getSize().width * 0.8), (int) (getSize().width * 0.03226)), new Posicion(0, (int) (getSize().width * 0.03226)), Color.RED));
        this.setAlwaysOnTop(true);
        this.setVisible(false);
    }
}
