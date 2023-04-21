package entornoAntares.gui.panelNave.panelGalaxia;

import java.awt.event.KeyEvent;
import humanInput.MapaTecla;
import humanInput.TecladoListener;

public class TecladoListenerPanelGalaxia extends TecladoListener implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    public SectorObjeto3d objeto3d;

    public static final int TECLA_IZQUIERDA = 0;

    public static final int TECLA_DERECHA = 1;

    public static final int TECLA_ARRIBA = 2;

    public static final int TECLA_ABAJO = 3;

    public static final int TECLA_ADELANTE = 4;

    public static final int TECLA_ATRAS = 5;

    public TecladoListenerPanelGalaxia() {
        teclas.add(new MapaTecla(KeyEvent.VK_LEFT, "Left"));
        teclas.add(new MapaTecla(KeyEvent.VK_RIGHT, "Right"));
        teclas.add(new MapaTecla(KeyEvent.VK_PAGE_UP, "Up"));
        teclas.add(new MapaTecla(KeyEvent.VK_PAGE_DOWN, "Down"));
        teclas.add(new MapaTecla(KeyEvent.VK_UP, "Forward"));
        teclas.add(new MapaTecla(KeyEvent.VK_DOWN, "Backward"));
    }

    public void tick(double tiempo, double dt) {
        if (dt == 0.0d) {
            int nt = teclas.size();
            for (int i = 0; i < nt; i++) {
                teclas.elementAt(i).flagPulsada = false;
            }
            return;
        }
        if (teclas.elementAt(TECLA_IZQUIERDA).flagPulsada) {
            teclas.elementAt(TECLA_IZQUIERDA).flagPulsada = false;
            objeto3d.moverIzquierda(dt);
        }
        if (teclas.elementAt(TECLA_DERECHA).flagPulsada) {
            teclas.elementAt(TECLA_DERECHA).flagPulsada = false;
            objeto3d.moverDerecha(dt);
        }
        if (teclas.elementAt(TECLA_ARRIBA).flagPulsada) {
            teclas.elementAt(TECLA_ARRIBA).flagPulsada = false;
            objeto3d.moverArriba(dt);
        }
        if (teclas.elementAt(TECLA_ABAJO).flagPulsada) {
            teclas.elementAt(TECLA_ABAJO).flagPulsada = false;
            objeto3d.moverAbajo(dt);
        }
        if (teclas.elementAt(TECLA_ADELANTE).flagPulsada) {
            teclas.elementAt(TECLA_ADELANTE).flagPulsada = false;
            objeto3d.moverAdelante(dt);
        }
        if (teclas.elementAt(TECLA_ATRAS).flagPulsada) {
            teclas.elementAt(TECLA_ATRAS).flagPulsada = false;
            objeto3d.moverAtras(dt);
        }
    }
}
