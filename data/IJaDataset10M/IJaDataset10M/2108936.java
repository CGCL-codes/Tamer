package ar.edu.unlp.info.diseyappweb.model;

import java.util.ArrayList;
import java.util.Calendar;
import ar.edu.unlp.info.diseyappweb.model.exception.EstadoOTInvalido;

/**
 * La Clase Suspendida representa el estado de la orden de trabajo al ser suspendida
 * por faltante de materias primas.
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public class Suspendida extends EstadoOT {

    /**
	 * Crea una nueva instancia de la clase Suspendida.
	 */
    public Suspendida() {
        super();
    }

    /**
	 * Crea una nueva instancia de la clase Suspendida de la cual se establece la fecha de inicio.
	 * 
	 * @param fecha Fecha de comienzo del estado.
	 */
    public Suspendida(Calendar fecha) {
        super(fecha);
    }

    /**
	 * Agrega al listado la orden de trabajo en caso de que el estado actual sea suspendida.
	 * 
	 * @param ordenTrabajo Orden de trabajo a analizar.
	 * @param otsSuspendidas Ordenes de trabajo encontradas que se encuentran suspendidas.
	 */
    public void agregarOTSuspendida(OrdenTrabajo ordenTrabajo, ArrayList<OrdenTrabajo> otsSuspendidas) {
        otsSuspendidas.add(ordenTrabajo);
    }

    /**
	 * Modifica el estado de la orden de trabajo de modo que pase a estar aceptada.
	 * 
	 * @param ordenTrabajo Orden de trabajo a modificar.
	 * @param fecha Fecha en la que se acepta la orden de trabajo.
	 * @throws EstadoOTInvalido Ocurre cuando la fecha de inicio del estado es menor a la fecha
	 * de comienzo del estado actual.
	 */
    public void aceptar(OrdenTrabajo ordenTrabajo, Calendar fecha) throws EstadoOTInvalido {
        ordenTrabajo.pasarAlEstado(new Aceptado(fecha));
    }

    public String getNombre() {
        return "Suspendida";
    }

    public boolean estaSuspendida() {
        return true;
    }
}
