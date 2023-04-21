package fpuna.ia.othello;

import fpuna.ia.othello.Utils.Casilla;
import fpuna.ia.othello.Utils.Tablero;
import fpuna.ia.othello.jugador.Jugador;

/**
 *
 * @author gusamasan
 */
public class Juego extends Thread {

    private GUI intefazGUI;

    private boolean pararJuego;

    private short turno;

    private Jugador jugadorConFichaBlanca, jugadorConFichaNegra, jugadorDeTurno;

    private Tablero tableroFichaBlanca, tableroFichaNegra;

    /** Constructores ********************************************************/
    public Juego(GUI gui) {
        this.intefazGUI = gui;
        this.pararJuego = false;
    }

    /**
     * Duerme al hilo por un rato (generalemente, cerca de 1 segundo)
     *
     * @autor gusamasan
     */
    public static void esperarUnRato() {
        try {
            Thread.sleep(800);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Regorna el objeto que representa al jugador con ficha blanca
     *
     * @return  instancia de la clase <code>Jugador</code> que representa
     *          al jugador
     */
    public Jugador getJugadorConFichaBlanca() {
        return jugadorConFichaBlanca;
    }

    /**
     * Asigna el objeto que representa al jugador con ficha blanca
     *
     * @param  instancia de la clase <code>Jugador</code> que representa
     *          al jugador
     */
    public void setJugadorConFichaBlanca(Jugador jugadorConFichaBlanca) {
        this.jugadorConFichaBlanca = jugadorConFichaBlanca;
    }

    /**
     * Regorna el objeto que representa al jugador con ficha negra
     *
     * @return  instancia de la clase <code>Jugador</code> que representa
     *          al jugador
     */
    public Jugador getJugadorConFichaNegra() {
        return jugadorConFichaNegra;
    }

    /**
     * Asigna el objeto que representa al jugador con ficha negra
     *
     * @param  instancia de la clase <code>Jugador</code> que representa
     *          al jugador
     */
    public void setJugadorConFichaNegra(Jugador jugadorConFichaNegra) {
        this.jugadorConFichaNegra = jugadorConFichaNegra;
    }

    /**
     * Retorna el jugador de turno
     *
     * @return  instancia de la clase <code>Jugador</code> que representa
     *          al jugador
     */
    public Jugador getJugadorDeTurno() {
        return jugadorDeTurno;
    }

    /**
     * Asigna el objeto que representa al jugador de turno
     *
     * @param  instancia de la clase <code>Jugador</code> que representa
     *          al jugador
     */
    public void setJugadorDeTurno(Jugador jugadorDeTurno) {
        this.jugadorDeTurno = jugadorDeTurno;
    }

    /**
     * Indica si el juego fue parado o no
     *
     * @return  <code>true</code> si el juego fue parado; <code>false</code>
     *          en caso contrario
     */
    public boolean estaParado() {
        return (this.pararJuego);
    }

    /**
     * Sobreescribe el método run de Thread
     */
    @Override
    public void run() {
        jugar();
    }

    /**
     * Inicia el juego
     *
     * @autor gusamasan
     */
    public void jugar() {
        Tablero nuevoTablero;
        this.pararJuego = false;
        this.turno = ConstanteOthello.TURNO_FICHA_NEGRA;
        while (!pararJuego) {
            nuevoTablero = this.jugadorDeTurno.jugar(turno);
            if (!pararJuego && nuevoTablero != null) {
                this.intefazGUI.setTablero(nuevoTablero);
                this.intefazGUI.refrescarTablero();
                if (turno == ConstanteOthello.TURNO_FICHA_NEGRA) {
                    this.jugadorDeTurno = this.jugadorConFichaBlanca;
                    this.intefazGUI.avisarTurnoFichaBlanca();
                    turno = ConstanteOthello.TURNO_FICHA_BLANCA;
                    this.tableroFichaNegra = nuevoTablero;
                } else {
                    this.jugadorDeTurno = this.jugadorConFichaNegra;
                    this.intefazGUI.avisarTurnoFichaNegra();
                    turno = ConstanteOthello.TURNO_FICHA_NEGRA;
                    this.tableroFichaBlanca = nuevoTablero;
                }
                if (nuevoTablero.EsFinalDeJuego()) {
                    pararJuego = true;
                    this.intefazGUI.avisarFinalizacionDelJuego(nuevoTablero.Puntos(Casilla.FICHA_NEGRA), nuevoTablero.Puntos(Casilla.FICHA_BLANCA));
                }
                this.jugadorDeTurno.setTablero(nuevoTablero);
            } else {
                if (turno == ConstanteOthello.TURNO_FICHA_NEGRA) {
                    this.tableroFichaNegra = null;
                    this.jugadorDeTurno = this.jugadorConFichaBlanca;
                    this.intefazGUI.avisarTurnoFichaBlanca();
                    turno = ConstanteOthello.TURNO_FICHA_BLANCA;
                    this.intefazGUI.avisarPasoTurnoFichaNegra();
                } else {
                    this.tableroFichaBlanca = null;
                    this.jugadorDeTurno = this.jugadorConFichaNegra;
                    this.intefazGUI.avisarTurnoFichaNegra();
                    turno = ConstanteOthello.TURNO_FICHA_NEGRA;
                    this.intefazGUI.avisarPasoTurnoFichaBlanca();
                }
                if (this.tableroFichaNegra == null && this.tableroFichaBlanca == null) pararJuego = true;
            }
            this.esperarUnRato();
            this.intefazGUI.limpiarAvisoPasoTurno();
        }
    }

    /**
     * Envía una señal al juego de tal manera a que cese la ejecución
     */
    public void pararJuego() {
        this.pararJuego = true;
    }
}
