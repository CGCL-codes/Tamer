package humanInput;

import java.util.Vector;
import javax.media.opengl.awt.ComponentEvents;
import motor3d.*;

public class GestorEntrada {

    private Object componente;

    private Vector<TecladoListener> tecladoListeners;

    private RatonListenerCamaraLibre ratonListenerCamaraLibre;

    private RatonListenerCamaraOrbital ratonListenerCamaraOrbital;

    public boolean shiftPulsado;

    public boolean ctrlPulsado;

    public boolean altPulsado;

    public GestorEntrada(Object componente) {
        this.componente = componente;
        tecladoListeners = new Vector<TecladoListener>();
        ratonListenerCamaraLibre = new RatonListenerCamaraLibre();
        ratonListenerCamaraLibre.setGestor(this);
        ratonListenerCamaraOrbital = new RatonListenerCamaraOrbital();
        ratonListenerCamaraOrbital.setGestor(this);
    }

    public Object getComponente() {
        return componente;
    }

    public int getNumTecladoListeners() {
        return tecladoListeners.size();
    }

    public TecladoListener getTecladoListener(int i) {
        return tecladoListeners.elementAt(i);
    }

    public void addTecladoListener(TecladoListener tecladoListener) {
        if (!tecladoListeners.contains(tecladoListener)) {
            tecladoListeners.add(tecladoListener);
            tecladoListener.setGestor(this);
            ((ComponentEvents) componente).addKeyListener(tecladoListener);
        }
    }

    public void removeTecladoListener(int i) {
        TecladoListener tecladoListener = tecladoListeners.elementAt(i);
        tecladoListener.setGestor(null);
        tecladoListeners.remove(i);
        ((ComponentEvents) componente).removeKeyListener(tecladoListener);
    }

    public void removeTecladoListener(TecladoListener tecladoListener) {
        tecladoListener.setGestor(null);
        tecladoListeners.remove(tecladoListener);
        ((ComponentEvents) componente).removeKeyListener(tecladoListener);
    }

    public RatonListenerCamaraLibre getRatonListenerCamaraLibre() {
        return ratonListenerCamaraLibre;
    }

    public void setRatonListenerCamaraLibre(Vector3d posicion, TransformacionYXZ transformacion) {
        ratonListenerCamaraLibre.setPosicionYTransformacion(posicion, transformacion);
        ratonListenerCamaraLibre.addToComponent();
    }

    public void unsetRatonListenerCamaraLibre() {
        ratonListenerCamaraLibre.removeFromComponent();
    }

    public RatonListenerCamaraOrbital getRatonListenerCamaraOrbital() {
        return ratonListenerCamaraOrbital;
    }

    public void setRatonListenerCamaraOrbital(Vector3d posicion, TransformacionYXZ transformacion) {
        ratonListenerCamaraOrbital.setPosicionYTransformacion(posicion, transformacion);
        ratonListenerCamaraOrbital.addToComponent();
    }

    public void unsetRatonListenerCamaraOrbital() {
        ratonListenerCamaraOrbital.removeFromComponent();
    }

    public void tick(double tiempo, double dt) {
        int ntl = tecladoListeners.size();
        for (int i = 0; i < ntl; i++) {
            tecladoListeners.elementAt(i).tick(tiempo, dt);
        }
        if (ratonListenerCamaraLibre.activado) {
            ratonListenerCamaraLibre.tick(tiempo, dt);
        }
        if (ratonListenerCamaraOrbital.activado) {
            ratonListenerCamaraOrbital.tick(tiempo, dt);
        }
    }
}
