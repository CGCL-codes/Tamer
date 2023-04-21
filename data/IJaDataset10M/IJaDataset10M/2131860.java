package ar.fi.uba.celdas.correccion.prediccion;

import java.util.Collection;
import ar.fi.uba.celdas.interfaces.bc.IBaseConocimientos;
import ar.fi.uba.celdas.interfaces.bc.Regla;
import ar.fi.uba.celdas.interfaces.bc.Sensor;
import ar.fi.uba.celdas.interfaces.correccion.ICorreccion;

public abstract class HeuristicaPrediccion implements ICorreccion {

    @Override
    public abstract void corregirRegla(Regla original, IBaseConocimientos bc);

    protected void actualizar(Regla original, Collection<Sensor> condicionesANegar, IBaseConocimientos bc) {
        float promExitos = (float) original.getExitos() / original.getUso();
        if (promExitos < 0.5f) {
            original.getCondicionesNegadas().addAll(condicionesANegar);
        } else {
            try {
                Regla nueva = (Regla) original.clone();
                nueva.getCondicionesNegadas().addAll(condicionesANegar);
                bc.getReglas().add(nueva);
            } catch (CloneNotSupportedException ex) {
            }
        }
    }
}
