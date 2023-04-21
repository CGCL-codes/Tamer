package neoAtlantis.utilidades.ctrlAcceso.disponibilidad;

import neoAtlantis.utilidades.ctrlAcceso.disponibilidad.interfaces.Disponibilidad;

/**
 * Calendario de disponibilidad con disponibilidad total
 * @version 1.0
 * @author Hiryu (asl_hiryu@yahoo.com)
 */
public class DisponibilidadTotal extends Disponibilidad {

    /**
     * Revisa la disponibilidad.
     * @return true si es disponible el acceso
     */
    public boolean existeDisponibilidad() {
        return true;
    }
}
