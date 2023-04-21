package ar.fi.uba.celdas.correccion.prediccion.heuristicas;

import java.util.ArrayList;
import java.util.Collection;
import ar.fi.uba.celdas.correccion.prediccion.HeuristicaPrediccion;
import ar.fi.uba.celdas.interfaces.bc.IBaseConocimientos;
import ar.fi.uba.celdas.interfaces.bc.Regla;
import ar.fi.uba.celdas.interfaces.bc.Sensor;

public class Ignorancia extends HeuristicaPrediccion {

    @Override
    public void corregirRegla(Regla original, IBaseConocimientos bc) {
        if (bc == null || original == null) {
            return;
        }
        Collection<Sensor> situacionInicial = bc.getEstadoAnterior();
        Collection<Sensor> estadosConocidos = getCondicionesConocidas(bc);
        Collection<Sensor> condicionesANegar = new ArrayList<Sensor>();
        for (Sensor estadoInicial : situacionInicial) {
            if (!estadosConocidos.contains(estadoInicial)) {
                condicionesANegar.add(estadoInicial);
            }
        }
        if (!condicionesANegar.isEmpty()) {
            actualizar(original, condicionesANegar, bc);
        }
    }

    private Collection<Sensor> getCondicionesConocidas(IBaseConocimientos bc) {
        Collection<Sensor> condicionesConocidas = new ArrayList<Sensor>();
        for (Regla regla : bc.getReglas()) {
            Collection<Sensor> condicionesRegla = new ArrayList<Sensor>();
            condicionesRegla.addAll(regla.getCondiciones());
            condicionesRegla.addAll(regla.getCondicionesNegadas());
            for (Sensor sensor : condicionesRegla) {
                if (!condicionesConocidas.contains(sensor)) {
                    condicionesConocidas.add(sensor);
                }
            }
        }
        return condicionesConocidas;
    }
}
