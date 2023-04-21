package Persistencia.Entidades;

/**
 *
 * @author diego
 */
public class EstadoFallaTecnicaAgente implements EstadoFallaTecnica {

    private EstadoFallaTecnicaImplementacion implementacion;

    public int getcodigoEstadoFalla() {
        return implementacion.getcodigoEstadoFalla();
    }

    public String getnombreEstado() {
        return implementacion.getnombreEstado();
    }

    public void setcodigoEstadoFalla(int newVal) {
        implementacion.setcodigoEstadoFalla(newVal);
    }

    public void setnombreEstado(String newVal) {
        implementacion.setnombreEstado(newVal);
    }

    /**
     * @return the implementacion
     */
    public EstadoFallaTecnicaImplementacion getImplementacion() {
        return implementacion;
    }

    /**
     * @param implementacion the implementacion to set
     */
    public void setImplementacion(EstadoFallaTecnicaImplementacion implementacion) {
        this.implementacion = implementacion;
    }
}
