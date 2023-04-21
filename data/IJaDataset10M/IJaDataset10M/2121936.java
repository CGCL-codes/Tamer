package net.sourceforge.trafalgar.androidtrafalgar;

import java.util.ArrayList;
import java.util.Random;

/**
 * Datos del jugador.
 *
 * El jugador se crea cuando el usuario se conecta al servidor con éxito.
 */
public class Jugador {

    private boolean miTurno;

    private TableroPropio miTablero;

    private TableroAjeno tableroAdversario;

    private String nombre;

    private ArrayList<String> listaAdversarios;

    private Random generator;

    /**
     * Constructor
     */
    public Jugador() {
        super();
        generator = new Random();
        miTablero = new TableroPropio();
        tableroAdversario = new TableroAjeno();
    }

    /**
     * Establece el nombre del jugador
     * 
     * @param nombre el nombre del jugador
     */
    public void setNombre(String _nombre) {
        nombre = _nombre;
    }

    public Jugador getJugador() {
        return this;
    }

    /**
     * Devuelve el nombre del jugador
     * 
     * @return el nombre del jugador
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece si es el turno del jugador o no.
     * 
     * @param turno booleano que indica si es el turno del jugador
     */
    public void setTurno(boolean turno) {
        this.miTurno = turno;
    }

    /**
     * Devuelve si es el turno del jugador o no
     * 
     * @return booleano que indica si es el turno del jugador
     */
    public boolean esMiTurno() {
        return miTurno;
    }

    /**
     * Establece si es el turno del jugador o no.
     * 
     * @param turno booleano que indica si es el turno del jugador
     */
    public void setTableroPropio(TableroPropio _miTablero) {
        this.miTablero = _miTablero;
    }

    /**
     * Devuelve si es el turno del jugador o no
     * 
     * @return booleano que indica si es el turno del jugador
     */
    public TableroPropio getMiTablero() {
        return this.miTablero;
    }

    /**
     * Establece si es el turno del jugador o no.
     * 
     * @param turno booleano que indica si es el turno del jugador
     */
    public void setTableroAjeno(TableroAjeno _tableroAjeno) {
        this.tableroAdversario = _tableroAjeno;
    }

    /**
     * Devuelve si es el turno del jugador o no
     * 
     * @return booleano que indica si es el turno del jugador
     */
    public TableroAjeno getTableroAdversario() {
        return tableroAdversario;
    }

    /**
     * Guarda la lista de adversarios disponibles en el servidor.
     * 
     * @param listaAdversarios lista de nombres de adversarios disponibles
     */
    public void setListaAdversarios(ArrayList<String> _listaAdversarios) {
        listaAdversarios = _listaAdversarios;
    }

    /**
     * Devuelve la lista de adversarios conectados al servidor
     * 
     * @return listaAdversarios lista de nombres de adversarios disponibles
     */
    public ArrayList<String> getListaAdversarios() {
        return listaAdversarios;
    }

    /**
     * Selecciona un adversario de forma aleatoria de la lista de usuarios disponibles
     * 
     * @return el nombre del jugador seleccionado
     */
    public String seleccionarAdversarioAleatorio() {
        int rnd = generator.nextInt(listaAdversarios.size());
        String nombreElegido = listaAdversarios.get(rnd);
        return nombreElegido;
    }
}
