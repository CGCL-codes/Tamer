package ucm.si.TeoriaActividad.actividad;

import ucm.si.TeoriaActividad.estado.EstadoTA;
import ucm.si.basico.ecuaciones.Proposicion;

/**
 * @author Niko, Jose Antonio, Ivan
 *
 */
public abstract class ProposicionTA extends Proposicion<EstadoTA> {

    public boolean esCierta(EstadoTA state) {
        return comparaActividad(state);
    }

    public abstract boolean comparaActividad(EstadoTA estado);
}
