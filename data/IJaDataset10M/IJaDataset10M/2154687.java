package pantallajuegoreducido;

import java.awt.Graphics;
import java.util.Vector;

/**
 *
 * @author Administrador
 */
public class ManejadorPantallaJuego {

    private int tipoPersonaje;

    private int usrID;

    private int tipoEscenario;

    private Vector grafico;

    private ManejadorAcciones manejadorAcciones;

    private String nombre;

    private PantallaJuego pantalla;

    private Graphics g;

    private int cantJugadores;

    private Vector<InfoJugadores> jugadores;

    public ManejadorPantallaJuego(int id, int escenario, int cantJugadores, Vector<InfoJugadores> jugadores) {
        this.cantJugadores = cantJugadores;
        this.jugadores = jugadores;
    }

    public void despliegaPantallaJuego(int escenario) {
        pantalla = new PantallaJuego();
        pantalla.generarEscenario(pantalla);
        pantalla.cargarJugadores(cantJugadores, pantalla, jugadores);
    }

    public void redibujarPantalla(Graphics g) {
        pantalla.paint(g);
    }

    public void recibeFinPartida() {
        pantalla.cerrarPantalla();
    }
}
