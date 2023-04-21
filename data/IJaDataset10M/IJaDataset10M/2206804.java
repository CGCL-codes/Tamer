package ar.edu.unlp.info.diseyappweb.ui.node;

import ar.edu.unlp.info.diseyappweb.model.OrdenTrabajo;
import ar.edu.unlp.info.diseyappweb.ui.util.Constants;

/**
 * 
 * @author Martinelli, Federico
 * @author Ducos, David
 */
public abstract class OrdenTrabajoNode extends Node {

    private OrdenTrabajo ordenTrabajo;

    private Integer cantidad;

    private Integer idOrdenTrabajoElegido;

    @SuppressWarnings("unchecked")
    public String informacion() {
        if (this.getIdOrdenTrabajoElegido() == null) {
            this.setOrdenTrabajo((OrdenTrabajo) this.getSession().get(Constants.CTS_ORDENTRABAJO_ENTIDAD));
        } else {
            this.setOrdenTrabajo(this.getOrdenTrabajoController().traerOrdenDeTrabajoPorId(this.getIdOrdenTrabajoElegido()));
            this.getSession().put(Constants.CTS_ORDENTRABAJO_ENTIDAD, this.getOrdenTrabajo());
        }
        return super.informacion();
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Integer getIdOrdenTrabajoElegido() {
        return idOrdenTrabajoElegido;
    }

    public void setIdOrdenTrabajoElegido(Integer idOrdenTrabajoElegido) {
        this.idOrdenTrabajoElegido = idOrdenTrabajoElegido;
    }

    public void setOrdenTrabajo(OrdenTrabajo ordenTrabajo) {
        this.ordenTrabajo = ordenTrabajo;
    }

    public OrdenTrabajo getOrdenTrabajo() {
        return ordenTrabajo;
    }
}
