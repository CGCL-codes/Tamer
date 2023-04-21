package logica.entidades;

import java.util.Iterator;
import java.util.Random;
import logica.Posicion;
import logica.escenario.Calculador;
import logica.escenario.Escenario;

public class FantasmaNaranja extends Fantasma {

    private boolean persiguiendoPacMan;

    protected boolean arrasando;

    private int casillasArrasadas;

    private int distanciaAlPivote;

    private int pasosArrasados;

    public FantasmaNaranja(Escenario escenario, Posicion posModoSeparacion, int duracionModoAzul, float velocidad, int puntosAlSerComido) {
        super(escenario, posModoSeparacion, duracionModoAzul, velocidad, puntosAlSerComido);
        this.arrasando = false;
        this.casillasArrasadas = 0;
        this.distanciaAlPivote = 21;
        this.persiguiendoPacMan = true;
        this.pasosArrasados = 0;
    }

    public void estrategizar() {
        Random generadorRandom;
        int random = 1;
        if (this.arrasando) this.arrasar(); else {
            generadorRandom = new Random();
            random = generadorRandom.nextInt(20);
            if ((random == 1) && (this.casillasArrasadas < 7)) {
                this.arrasando = true;
                this.arrasar();
            } else this.perseguir();
        }
    }

    protected void arrasar() {
        NoJugador comestible;
        Iterator<NoJugador> iteradorCasillero;
        long valorDelComestible;
        if ((this.casillasArrasadas >= 7) || (this.pasosArrasados >= 20)) {
            this.arrasando = false;
            return;
        } else {
            this.movimientoAlAzar();
            iteradorCasillero = this.getEscenario().getUeb(this.getPosicion()).iterator();
            this.pasosArrasados++;
            while (iteradorCasillero.hasNext()) {
                comestible = iteradorCasillero.next();
                valorDelComestible = comestible.activar();
                if (valorDelComestible != 0) this.casillasArrasadas++;
            }
        }
    }

    protected void perseguir() {
        Calculador calculador = this.getEscenario().calculador();
        Posicion posicionPacman = this.getEscenario().getPacman().getPosicion();
        Posicion posicionPivote = this.getPosicionModoSeparacion();
        Posicion posicionActual = this.getPosicion();
        if ((this.persiguiendoPacMan) && (this.distanciaAlPivote > 20)) {
            if ((posicionActual.distanciaHasta(posicionPivote) < posicionActual.distanciaHasta(posicionPacman)) && !(posicionActual.equals(posicionPivote))) {
                this.moverHacia(calculador.direccionHaciaMenorCaminoEntre(this.getPosicion(), posicionPivote));
                this.persiguiendoPacMan = false;
            } else {
                this.moverHacia(calculador.direccionHaciaMenorCaminoEntre(this.getPosicion(), posicionPacman));
                this.persiguiendoPacMan = true;
            }
        } else {
            if (posicionActual.distanciaHasta(posicionPivote) == 0) {
                this.moverHacia(calculador.direccionHaciaMenorCaminoEntre(this.getPosicion(), posicionPacman));
                this.distanciaAlPivote = 0;
                this.persiguiendoPacMan = true;
            } else {
                this.distanciaAlPivote++;
                if (this.persiguiendoPacMan) this.moverHacia(calculador.direccionHaciaMenorCaminoEntre(this.getPosicion(), posicionPacman)); else this.moverHacia(calculador.direccionHaciaMenorCaminoEntre(this.getPosicion(), posicionPivote));
            }
        }
    }

    public boolean estaArrasando() {
        return this.arrasando;
    }
}
