package logica.entidades;

import logica.Posicion;
import logica.escenario.Escenario;

public class FantasmaNaranjaParaPruebas extends FantasmaNaranja {

    public FantasmaNaranjaParaPruebas(Escenario escenario, Posicion posModoSeparacion, int duracionModoAzul, float velocidad, int puntosAlSerComido) {
        super(escenario, posModoSeparacion, duracionModoAzul, velocidad, puntosAlSerComido);
    }

    public void estrategizar() {
        if (this.arrasando) this.arrasar(); else this.perseguir();
    }

    public void activarPerseguir() {
        this.arrasando = false;
    }

    public void activarArrasar() {
        this.arrasando = true;
    }
}
