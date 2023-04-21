package Persistencia.Entidades;

import java.util.Date;

/**
 *
 * @author diego
 */
public class PesoDeReclamoAgente implements PesoDeReclamo {

    private PesoDeReclamoImplementacion implementacion;

    public int getcantidadReclamosDesde() {
        return implementacion.getcantidadReclamosDesde();
    }

    public int getcantidadReclamosHasta() {
        return implementacion.getcantidadReclamosHasta();
    }

    public Date getfechaDesde() {
        return implementacion.getfechaDesde();
    }

    public Date getfechaHasta() {
        return implementacion.getfechaHasta();
    }

    public int getpeso() {
        return implementacion.getpeso();
    }

    public void setcantidadReclamosDesde(int newVal) {
        implementacion.setcantidadReclamosDesde(newVal);
    }

    public void setcantidadReclamosHasta(int newVal) {
        implementacion.setcantidadReclamosHasta(newVal);
    }

    public void setfechaDesde(Date newVal) {
        implementacion.setfechaDesde(newVal);
    }

    public void setfechaHasta(Date newVal) {
        implementacion.setfechaHasta(newVal);
    }

    public void setpeso(int newVal) {
        implementacion.setpeso(newVal);
    }

    /**
     * @return the implementacion
     */
    public PesoDeReclamoImplementacion getImplementacion() {
        return implementacion;
    }

    /**
     * @param implementacion the implementacion to set
     */
    public void setImplementacion(PesoDeReclamoImplementacion implementacion) {
        this.implementacion = implementacion;
    }
}
