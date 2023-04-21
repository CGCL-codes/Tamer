package edu.upc.pruebas;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import edu.upc.entidades.Residente;
import edu.upc.metodos.MetodosResidente;

public class ResidenteTest {

    MetodosResidente metodos;

    @Before
    public void inicio() {
        metodos = new MetodosResidente();
    }

    public void registrarResidente() {
        metodos.registrarResidente("43811752", "Jorge Luis Camarena Cueva", 24, "jlccsc19@hotmail.com", "1234", "Activo");
        metodos.registrarResidente("44160504", "Junior Benites Cisneros", 24, "jlccs419@hotmail.com", "1234", "Activo");
        metodos.registrarResidente("24234334", "Jorge Luis Camarena Cueva", 24, "jlccec19@hotmail.com", "1234", "Activo");
        assertEquals(3, metodos.getResidentes().size());
        ListarResidentes();
    }

    public void modificarResidente() {
        registrarResidente();
        System.out.println("");
        System.out.println("Elijo Redifente a modificar");
        System.out.println("--------------------------------");
        Residente obtenido = metodos.obtenerResidente("43811752");
        System.out.println("Dni: " + obtenido.getRes_dni() + "  Nombre: " + obtenido.getRes_nombre() + "  Edad: " + obtenido.getRes_edad() + "  Correo: " + obtenido.getRes_corre() + "  Estado: " + obtenido.getRes_estado());
        System.out.println("");
        System.out.println("Actualizo el Administrador");
        System.out.println("--------------------------------");
        obtenido.setEdad(26);
        System.out.println("Dni: " + obtenido.getRes_dni() + "  Nombre: " + obtenido.getRes_nombre() + "  Edad: " + obtenido.getRes_edad() + "  Correo: " + obtenido.getRes_corre() + "  Estado: " + obtenido.getRes_estado());
    }

    @Test
    public void EliminarResidente() {
        registrarResidente();
        Residente obtenido = metodos.obtenerResidente("43811752");
        System.out.println("Eliminar Residente Dni: " + obtenido.getRes_dni());
        metodos.getResidentes().remove(obtenido);
        ListarResidentes();
    }

    public void ListarResidentes() {
        System.out.println("");
        System.out.println("---------------LISTADO RESIDENTES-------------------");
        System.out.println("----------------------------------------------------");
        for (Residente listado : metodos.getResidentes()) {
            System.out.println("Dni: " + listado.getRes_dni() + "  Nombre: " + listado.getRes_nombre() + "  Edad: " + listado.getRes_edad() + "  Correo: " + listado.getRes_corre() + "  Estado: " + listado.getRes_estado());
        }
        System.out.println("");
    }
}
