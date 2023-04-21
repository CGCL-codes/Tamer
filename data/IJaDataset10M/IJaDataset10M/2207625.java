package modelo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ar.uba.fi.algo3.titiritero.ControladorJuego;
import ar.uba.fi.algo3.titiritero.Posicionable;
import ar.uba.fi.algo3.titiritero.Dibujable;

public class Controlador1942 extends ControladorJuego {

    private boolean pausa;

    public Controlador1942(boolean activarReproductor) {
        super(activarReproductor);
    }

    public void removerDibujablesDePosicionable(Posicionable posicionable) {
        List<Dibujable> conjuntoAeliminar = new ArrayList<Dibujable>();
        Iterator<Dibujable> iterador = dibujables.iterator();
        while (iterador.hasNext()) {
            Dibujable dibujable = iterador.next();
            if (dibujable.getPosicionable() == posicionable) {
                conjuntoAeliminar.add(dibujable);
            }
        }
        iterador = conjuntoAeliminar.iterator();
        while (iterador.hasNext()) {
            this.removerDibujable(iterador.next());
        }
    }

    public void comenzarJuego(int i) {
        super.comenzarJuego(i);
        System.out.println("DIBUJABLES");
        System.out.println(dibujables);
    }

    public void pausar() {
        pausa = true;
    }

    public void invertirPausa() {
        pausa = !pausa;
    }

    public void run() {
        pausa = false;
        this.comenzarJuego();
    }

    public void refresh() {
        this.dibujar();
    }

    public void comenzarJuego() {
        estaEnEjecucion = true;
        try {
            while (estaEnEjecucion) {
                if (!pausa) {
                    simular();
                    dibujar();
                }
                Thread.sleep(intervaloSimulacion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void limpiarDibujables() {
        dibujables = new ArrayList<Dibujable>();
    }
}
