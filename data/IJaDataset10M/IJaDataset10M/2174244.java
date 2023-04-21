package fiuba.algo3.juego.controlador.operacionesJuego;

import fiuba.algo3.juego.controlador.ControladorJuegoAlgo42full;
import fiuba.algo3.juego.controlador.operacionesAlgo.Controlable;

public class BotonGControlable implements Controlable {

    ControladorJuegoAlgo42full controlador;

    public BotonGControlable(ControladorJuegoAlgo42full ctrl) {
        controlador = ctrl;
    }

    @Override
    public void activarEfecto() {
        System.out.println("GuardarJuego()");
        controlador.persistirPlano("savegame.dat");
    }
}
