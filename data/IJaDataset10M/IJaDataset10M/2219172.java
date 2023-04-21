package BE;

import java.util.Calendar;

/**
 *
 * @author Billy
 */
public class partida {

    int idPartida;

    int idTorneo;

    int idJugadorBlancas;

    int idJugadorNegras;

    String chrRegistrado;

    Calendar fechaJuego;

    String movimientos;

    String resultado;

    String nombresBlanco, nombresNegro, nombreTorneo;

    String eloBlancas, eloNegras;

    int idApertura;

    /** Creates a new instance of partida */
    public partida() {
    }

    public int dameIdPartida() {
        return this.idPartida;
    }

    public void setIdPartida(int id) {
        this.idPartida = id;
    }

    public int dameIdTorneo() {
        return this.idTorneo;
    }

    public void setIdTorneo(int id) {
        this.idTorneo = id;
    }

    public int dameIdJugadorBlancas() {
        return this.idJugadorBlancas;
    }

    public void setIdJugadorBlancas(int id) {
        this.idJugadorBlancas = id;
    }

    public void setIdJugadorNegras(int id) {
        this.idJugadorNegras = id;
    }

    public int dameIdJugadorNegras() {
        return this.idJugadorNegras;
    }

    public String dameEstadoRegistrado() {
        return this.chrRegistrado;
    }

    public void setEstadoRegistrado(String estado) {
        this.chrRegistrado = estado;
    }

    public Calendar dameFechaJuego() {
        return this.fechaJuego;
    }

    public void setFechaJuego(Calendar fecha) {
        this.fechaJuego = fecha;
    }

    public String dameMovimientos() {
        return this.movimientos;
    }

    public void setMovimimentos(String movs) {
        this.movimientos = movs;
    }

    public String dameResultado() {
        return this.resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String dameNombreJugadorBlanco() {
        return nombresBlanco;
    }

    public String dameNombreJugadorNegro() {
        return nombresNegro;
    }

    public void setNombreJugadorBlanco(String nombre) {
        this.nombresBlanco = nombre;
    }

    public void setNombreJugadorNegro(String nombre) {
        this.nombresNegro = nombre;
    }

    public void setNombreTorneo(String nombre) {
        this.nombreTorneo = nombre;
    }

    public String dameNombreTorneo() {
        return nombreTorneo;
    }

    public void setEloBlancas(String elo) {
        this.eloBlancas = elo;
    }

    public String dameEloBlancas() {
        return eloBlancas;
    }

    public void setEloNegras(String elo) {
        this.eloNegras = elo;
    }

    public String dameEloNegras() {
        return eloNegras;
    }

    public void setApertura(int id) {
        this.idApertura = id;
    }

    public int dameidApertura() {
        return this.idApertura;
    }
}
