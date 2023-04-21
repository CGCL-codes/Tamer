package logica.entidades;

import logica.Posicion;
import logica.escenario.Calculador;
import logica.escenario.Escenario;

public class FantasmaRojo extends Fantasma {

    private float velocidadEnModoRapido;

    public FantasmaRojo(Escenario escenario, Posicion posModoSeparacion, float duracionModoAzul, float velocidad, int puntosAlSerComido) {
        super(escenario, posModoSeparacion, duracionModoAzul, velocidad, puntosAlSerComido);
        this.velocidadEnModoRapido = velocidad + velocidad * 0.2f;
    }

    private void activarModoRapido() {
        this.setVelocidad(this.velocidadEnModoRapido);
    }

    private boolean pasarLimiteDePuntitosRestantes() {
        int limiteModoRapido = (int) ((this.getEscenario().getPuntosTotales()) * (0.2f));
        int puntitosRestantes = this.getEscenario().getPuntosRestantes();
        if (puntitosRestantes <= limiteModoRapido) return true; else return false;
    }

    public void estrategizar() {
        Calculador calc = this.getEscenario().calculador();
        if (this.pasarLimiteDePuntitosRestantes()) this.activarModoRapido();
        this.moverHacia(calc.direccionHaciaMenorCaminoEntre(this.getPosicion(), this.getEscenario().getPacman().getPosicion()));
    }
}
