package co.edu.javeriana.arquitectura.proyecto.drogueria.java;

import co.edu.javeriana.arquitectura.proyecto.drogueria.*;
import java.util.List;
import webservices.ServiciosDrogueriaService;

/**
 *
 * @author pipe
 */
public class DrogueriaClienteJavaWS implements DrogueriaCliente {

    public ServiciosDrogueriaService servicios;

    public DrogueriaClienteJavaWS() {
        servicios = new ServiciosDrogueriaService();
    }

    public String getNIT() {
        return servicios.getServiciosDrogueriaPort().getNIT();
    }

    public List<Double> getInfoCUM(String CUM) {
        return servicios.getServiciosDrogueriaPort().getInfoCUM(CUM);
    }

    public String Comprar(String NombreCliente, String Direccion, List<String> CUMS, List<Integer> cantidades, String Telefono) {
        return servicios.getServiciosDrogueriaPort().comprar(NombreCliente, Direccion, CUMS, cantidades, Telefono);
    }
}
