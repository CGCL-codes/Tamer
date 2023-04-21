package BE;

import java.util.Calendar;

/**
 *
 * @author Billy
 */
public class torneo {

    int idTorneo;

    boolean terminado;

    String nombreTorneo;

    String ciudad;

    Calendar fechaInicio;

    Calendar fechaFin;

    String organizador;

    int numeroDivisiones;

    /** Creates a new instance of torneo */
    public torneo() {
    }

    public int getIdTorneo() {
        return idTorneo;
    }

    public void setTorneo(int idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getnombreTorneo() {
        return nombreTorneo;
    }

    public void setnombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getOrganizador() {
        return organizador;
    }

    public void setOrganizador(String organizador) {
        this.organizador = organizador;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Calendar fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Calendar getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Calendar fechafin) {
        this.fechaFin = fechafin;
    }

    public int getNumeroDivisiones() {
        return this.numeroDivisiones;
    }

    public void setNumeroDivisiones(int nDivisiones) {
        this.numeroDivisiones = nDivisiones;
    }

    public boolean getTerminoTorneo() {
        return terminado;
    }

    public void setTerminoTorneo(boolean flag) {
        terminado = flag;
    }
}
