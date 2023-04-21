package provisorio;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;
import java.util.ArrayList;

public class EstadiaHotel extends Servicio {

    private XMLGregorianCalendar fhEntrada;

    private XMLGregorianCalendar fhSalida;

    private String tipoHabitacion;

    private int cantidadEstrellas;

    public String clase = "Estadia en Hotel";

    public String getClase() {
        return clase;
    }

    public int getCantidadEstrellas() {
        return cantidadEstrellas;
    }

    public void setCantidadEstrellas(int cantidadEstrellas) {
        this.cantidadEstrellas = cantidadEstrellas;
    }

    public XMLGregorianCalendar getFhEntrada() {
        return fhEntrada;
    }

    public void setFhEntrada(XMLGregorianCalendar fhEntrada) {
        this.fhEntrada = fhEntrada;
    }

    public XMLGregorianCalendar getFhSalida() {
        return fhSalida;
    }

    public void setFhSalida(XMLGregorianCalendar fhSalida) {
        this.fhSalida = fhSalida;
    }

    public String getTipoHabitacion() {
        return tipoHabitacion;
    }

    public void setTipoHabitacion(String tipoHabitacion) {
        this.tipoHabitacion = tipoHabitacion;
    }

    public List<String> getResumen() {
        List<String> lis = new ArrayList<String>();
        lis.add("Tipo de habitacion: " + tipoHabitacion);
        lis.add("Estrellas: " + Integer.toString(cantidadEstrellas));
        return lis;
    }
}
