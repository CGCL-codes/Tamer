package controlador;

import logica.SistemaGranDT;
import vistas.RegistrarPuntajeJugadorFrame;

public class RegistrarPuntajeJugadorControlador {

    private RegistrarPuntajeJugadorFrame frame;

    private SistemaGranDT logica;

    private JugadorTableModelP tableModelJ;

    public RegistrarPuntajeJugadorControlador() {
        this.logica = SistemaGranDT.getInstance();
        this.logica.cargarJugadores();
        tableModelJ = new JugadorTableModelP(this.logica.getJugadores());
        this.frame = new RegistrarPuntajeJugadorFrame();
        this.frame.getTableJugadores().setModel(tableModelJ);
        this.frame.setControlador(this);
        this.frame.setVisible(true);
    }

    public RegistrarPuntajeJugadorFrame getFrame() {
        return frame;
    }

    public void setFrame(RegistrarPuntajeJugadorFrame frame) {
        this.frame = frame;
    }

    public void guardarNuevosPuntajes() {
        this.logica.getAdminDAO().updatePuntajesPorId(tableModelJ.getDatalist());
        this.logica.liberarJugadores();
    }
}
